package com.github.mateo762.myapplication.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.github.mateo762.myapplication.BaseActivity
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.coach_rating.CoachRatingViewModel
import com.github.mateo762.myapplication.databinding.ActivityProfileBinding
import com.github.mateo762.myapplication.followers.UserRepository
import com.github.mateo762.myapplication.username.UsernameActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Activity for displaying the profile information.
 */
abstract class ProfileActivity : BaseActivity(), CoroutineScope {

    // Hack to solve the jacoco report problem with hilt activity, which still doesn't have a
    // solution.
    //
    // More info can be found: https://issuetracker.google.com/issues/161300933#comment5
    @AndroidEntryPoint
    class EntryPoint : ProfileActivity()

    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var userImageStorageService: UserImageStorageService
    @Inject
    lateinit var auth: FirebaseAuth
    @Inject
    lateinit var db: DatabaseReference

    private val coachRatingViewModel: CoachRatingViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    private val job = Job()

    override val coroutineContext = job + Dispatchers.Main

    private var imageUri: Uri? = null

    private val user = auth.currentUser
    lateinit var binding: ActivityProfileBinding
    private lateinit var uid: String

    private lateinit var habitsAdapter: ProfileHabitsAdapter
    private lateinit var galleryAdapter: ProfileGalleryAdapter

    companion object {
        private val TAG = ProfileActivity::class.java.simpleName
        const val USER_ID_EXTRA = "userId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        handleProfileInfoChange(false)

        binding.coachRatingView.setViewModel(coachRatingViewModel)

        uid = intent.getStringExtra(USER_ID_EXTRA) ?: userRepository.getUserUid()
        val currentUserId = userRepository.getUserUid()

        checkIfDifferentUser(currentUserId)

        setOnClickListeners(currentUserId)

        habitsAdapter = ProfileHabitsAdapter()
        binding.habitsRecyclerView.adapter = habitsAdapter
        galleryAdapter = ProfileGalleryAdapter()
        binding.galleryRecyclerView.adapter = galleryAdapter

        setLiveDataObservers()
    }

    override fun onResume() {
        super.onResume()

        profileViewModel.getUserInfo(uid)
        profileViewModel.getHabitImages(uid)
        profileViewModel.getFollowingNumber(uid)
        profileViewModel.getFollowersNumber(uid)
        profileViewModel.getHabits(uid)
        binding.coachRatingView.getRatingStats(uid)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                imageUri = data?.data!!
                binding.profileImage.setImageURI(imageUri)
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        title = "" // the title is not set directly on the xml, avoid having two titles per screen
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun followUser(currentUserId: String, targetUserId: String) {
        launch {
            userRepository.followUser(currentUserId, targetUserId)
            binding.btnFollow.visibility = View.GONE
            binding.btnUnfollow.visibility = View.VISIBLE
        }
    }

    private fun unfollowUser(currentUserId: String, targetUserId: String){
        launch {
            userRepository.unfollowUser(currentUserId, targetUserId)
            binding.btnFollow.visibility = View.VISIBLE
            binding.btnUnfollow.visibility = View.GONE
        }
    }

    private fun handleProfileInfoChange(isEditCLicked: Boolean) {
        binding.btnEdit.visibility = if (isEditCLicked) View.GONE else View.VISIBLE
        binding.btnSave.visibility = if (isEditCLicked) View.VISIBLE else View.GONE
        
        binding.nameTextView.visibility = if (isEditCLicked) View.GONE else View.VISIBLE
        binding.nameEditText.isEnabled = isEditCLicked
        binding.nameEditText.isClickable = isEditCLicked
        binding.nameEditText.visibility = if (isEditCLicked) View.VISIBLE else View.GONE
        binding.emailTextView.visibility = if (isEditCLicked) View.GONE else View.VISIBLE
        binding.emailEditText.isEnabled = isEditCLicked
        binding.emailEditText.isClickable = isEditCLicked
        binding.emailEditText.visibility = if (isEditCLicked) View.VISIBLE else View.GONE
        binding.usernameTextView.visibility = if (isEditCLicked) View.GONE else View.VISIBLE
        binding.changeUsernameButton.visibility = if (isEditCLicked) View.VISIBLE else View.GONE
    }

    private fun setLiveDataObservers() {
        profileViewModel.habitLiveData.observe(this) {
            habitsAdapter.habits = it
        }
        profileViewModel.habitImagesLiveData.observe(this) {
            galleryAdapter.galleryItems = it
        }
        profileViewModel.statsLiveData.observe(this) { statsUiModel ->
            binding.habitCountText.text =
                getString(R.string.posted_habits, statsUiModel.totalNumberOfHabits.toString())
            binding.avgPerWeekText.text =
                getString(R.string.avg_days_week, statsUiModel.averageDaysInWeek.toString())

            binding.earliestTextView.text =
                getString(R.string.earlystart, statsUiModel.earliestStart)
            binding.latestTextView.text = getString(R.string.lateend, statsUiModel.latestEnd)
        }

        profileViewModel.followingLiveData.observe(this) {
            binding.followingTextView.text = getString(R.string.following, it.toString())
        }
        profileViewModel.followersLiveData.observe(this) {
            binding.followersTextView.text = getString(R.string.followers, it.toString())
        }
        profileViewModel.userInfoLiveData.observe(this) { userInfo ->
            binding.nameTextView.text = userInfo.name
            binding.emailTextView.text = userInfo.email
            binding.usernameTextView.text = userInfo.username

            Glide.with(applicationContext)
                .load(userInfo.url)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.profileImage)
        }
    }

    private fun onSaveButtonClicked(currentUserId: String) {
        // We save the updated value to your database or data storage
        val newName = binding.nameEditText.text.toString()
        val newEmail = binding.emailEditText.text.toString()
        binding.nameTextView.text = newName
        binding.emailTextView.text = newEmail

        handleProfileInfoChange(false)

        binding.profileImage.setOnClickListener { }

        db.child("users").child(uid).child("name").setValue(newName)
        db.child("users").child(uid).child("email").setValue(newEmail)

        userImageStorageService.storeImage(currentUserId, imageUri)

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(newName)
            .build()

        user?.updateProfile(profileUpdates)
    }

    private fun onEditButtonClicked() {
        // We enable the name and email edit texts such that they can be edited
        handleProfileInfoChange(true)
        binding.nameEditText.setText(binding.nameTextView.text)
        binding.emailEditText.setText(binding.emailTextView.text)

        binding.profileImage.setOnClickListener {
            val openGalleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(openGalleryIntent, 1000)
        }
    }

    private fun onChangeUsernameClicked() {
        val intent = Intent(this, UsernameActivity.EntryPoint::class.java).apply {
            putExtra(UsernameActivity.OLD_USERNAME_KEY, binding.usernameTextView.text)
        }
        startActivity(intent)
    }

    private fun setOnClickListeners(currentUserId: String) {
        binding.btnFollow.setOnClickListener {
            followUser(currentUserId, uid)
        }
        binding.btnUnfollow.setOnClickListener {
            unfollowUser(currentUserId, uid)
        }
        binding.changeUsernameButton.setOnClickListener {
            onChangeUsernameClicked()
        }
        binding.btnEdit.setOnClickListener {
            onEditButtonClicked()
        }
        binding.btnSave.setOnClickListener {
            onSaveButtonClicked(currentUserId)
        }
    }

    private fun checkIfDifferentUser(currentUserId: String) {
        if (uid == currentUserId) {
            binding.btnFollow.visibility = View.GONE
            binding.btnUnfollow.visibility = View.GONE
            binding.btnEdit.visibility = View.VISIBLE
        } else {
            binding.btnEdit.visibility = View.GONE
            // Check if the user is already following the profile
            launch {
                val isFollowing = userRepository.checkIfUserFollows(currentUserId, uid)
                if (isFollowing) {
                    // If the user is following the profile, show the 'btnUnfollow' button
                    binding.btnFollow.visibility = View.GONE
                    binding.btnUnfollow.visibility = View.VISIBLE
                } else {
                    binding.btnFollow.visibility = View.VISIBLE
                    binding.btnUnfollow.visibility = View.GONE
                }
            }
        }
    }
}