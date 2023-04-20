package com.github.mateo762.myapplication.upload_gallery

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.databinding.ActivityUploadPictureBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class UploadPictureActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadPictureBinding
    private lateinit var imageUri: Uri

    private var defaultUserString: String = "testUser"
    private var currentUser: String = defaultUserString

    private val SUCCESS = 100

    lateinit var imagesList: ArrayList<UserImage>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadPictureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectPictureButton.setOnClickListener {
            selectImage()
        }

        binding.uploadPictureButton.setOnClickListener {
            uploadImage()
        }

        binding.imageRecycler.layoutManager = LinearLayoutManager(this)
        imagesList = arrayListOf()

        //Populate the recycler view with the downloaded images from the current user firebase database
        var currentUser = Firebase.auth.currentUser?.displayName

        if (currentUser == null) {
            currentUser = defaultUserString
        }

        val db = Firebase.database.reference.child("users").child(currentUser)
//        db.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                imagesList = arrayListOf()
//                if (snapshot.exists()) {
//                    for (dataSnapshot in snapshot.children) {
//                        val image = dataSnapshot.getValue(UserImage::class.java)
//                        image?.let {imagesList.add(image)}
//                    }
//
//                    binding.imageRecycler.adapter =
//                        ImageAdapter(imagesList, this@UploadPictureActivity)
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(this@UploadPictureActivity, error.toString(), Toast.LENGTH_SHORT)
//                    .show()
//            }
//        })


    }

    //Select an image from the gallery
    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, SUCCESS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SUCCESS && resultCode == RESULT_OK) {
            if (data?.data != null){
                imageUri = data.data!!
            }

            binding.selectedImagePreview.setImageURI(imageUri)
        }
    }

    //Upload an image to the Firebase Storage and then save under the current user database
    private fun uploadImage() {

        val progressDialog = ProgressDialog(this)

        progressDialog.setMessage(getString(R.string.progress_indicator))
        progressDialog.setCancelable(false)
        progressDialog.show()

        //Generate a file name based on the upload time
        val formatter = SimpleDateFormat(getString(R.string.file_name_date), Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)

        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")
        var uploadTask = storageReference.putFile(imageUri)

        //Retrieve the download url of the file once the task has uploaded
        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            storageReference.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //Handle successful upload
                binding.selectedImagePreview.setImageURI(null)

                val downloadUrl = task.result
                Toast.makeText(
                    this@UploadPictureActivity,
                    "Successfully uploaded to $downloadUrl",
                    Toast.LENGTH_SHORT
                ).show()

                //Store the download url of the uploaded image under the user
                val userRef = Firebase.database.reference.child("users").child("$currentUser")
                val key = userRef.push().key

                if (key != null) {
                    val imageKey = userRef.child("images").push().key
                    val imageRef = userRef.child(key).child("userImage").setValue("$downloadUrl")
                        .addOnSuccessListener {
                            println("Successfully added new image reference")
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                this@UploadPictureActivity,
                                getString(R.string.try_again),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }

                if (progressDialog.isShowing) progressDialog.dismiss()
            } else {
                //Handle failure
                if (progressDialog.isShowing) progressDialog.dismiss()
                Toast.makeText(this@UploadPictureActivity, getString(R.string.failed), Toast.LENGTH_SHORT).show()
            }
        }
    }
}