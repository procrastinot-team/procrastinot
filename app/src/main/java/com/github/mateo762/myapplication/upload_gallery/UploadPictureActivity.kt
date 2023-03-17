package com.github.mateo762.myapplication.upload_gallery

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mateo762.myapplication.UserImage
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

        val db = FirebaseDatabase.getInstance().getReference("images")
        db.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (dataSnapshot in snapshot.children) {
                        val image = dataSnapshot.getValue(UserImage::class.java)
                        imagesList.add(image!!)
                    }

                    binding.imageRecycler.adapter = ProfileGalleryAdapter()
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

        val formatter = SimpleDateFormat("yyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)

        var currentUser = Firebase.auth.currentUser?.displayName

        if (currentUser == null){
            currentUser = "testUser"
        }

        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

        storageReference.putFile(imageUri)
            .addOnSuccessListener {
                binding.selectedImagePreview.setImageURI(null)

                //Store the downloadUri under the user images
                val downloadUri = storageReference.downloadUrl
                Toast.makeText(this@UploadPictureActivity, "Successfully uploaded to $downloadUri", Toast.LENGTH_SHORT).show()

                val userRef = Firebase.database.reference.child("users").child("$currentUser")
                val key = userRef.push().key

                if (key != null) {
                    val imageKey = userRef.child("images").push().key
                    val imageRef = userRef.child(key).child("image").setValue("$downloadUri")
                        .addOnSuccessListener {
                            println("Successfully added new image reference")
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@UploadPictureActivity, "Try again", Toast.LENGTH_SHORT).show()
                        }
                }

                if (progressDialog.isShowing) progressDialog.dismiss()
            }

            .addOnFailureListener {
                if (progressDialog.isShowing) progressDialog.dismiss()
                Toast.makeText(this@UploadPictureActivity, "Failed", Toast.LENGTH_SHORT).show()
            }

    }
}