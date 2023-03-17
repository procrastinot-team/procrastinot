package com.github.mateo762.myapplication.upload_gallery

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mateo762.myapplication.databinding.UploadPictureBinding
import com.github.mateo762.myapplication.profile.ProfileGalleryAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UploadPictureActivity : AppCompatActivity() {

    private lateinit var binding: UploadPictureBinding
    private lateinit var imageUri : Uri

    private var default_user_string: String = "testUser"
    private var currentUser: String = default_user_string

    lateinit var imagesList: ArrayList<UserImage>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UploadPictureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectPictureButton.setOnClickListener {
            selectImage()
        }

        binding.uploadPictureButton.setOnClickListener {
            uploadImage()
        }

        binding.imageRecycler.layoutManager = LinearLayoutManager(this)
        imagesList = arrayListOf()

        //Populate the recycler view with the downloaded images from the Firebase references
        var currentUser = Firebase.auth.currentUser?.displayName

        if (currentUser == null){
            currentUser = default_user_string
        }


        val db = Firebase.database.reference.child("users").child(currentUser)
        db.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                imagesList = arrayListOf()
                if (snapshot.exists()){
                    for (dataSnapshot in snapshot.children) {
                        val image = dataSnapshot.getValue(UserImage::class.java)
                        imagesList.add(image!!)
                    }

                    binding.imageRecycler.adapter = ImageAdapter(imagesList, this@UploadPictureActivity)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UploadPictureActivity, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK){
            imageUri = data?.data!!
            binding.selectedImagePreview.setImageURI(imageUri)
        }
    }

    private fun uploadImage() {

        val progressDialog = ProgressDialog(this)

        progressDialog.setMessage("Uploading File..")
        progressDialog.setCancelable(false)
        progressDialog.show()

        //Generate a file name based on the upload time
        val formatter = SimpleDateFormat("yyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)

        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")
        var uploadTask = storageReference.putFile(imageUri)

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
                                "Try again",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }

                if (progressDialog.isShowing) progressDialog.dismiss()
            } else {
                //Handle failure
                if (progressDialog.isShowing) progressDialog.dismiss()
                Toast.makeText(this@UploadPictureActivity, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}