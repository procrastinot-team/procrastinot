package com.github.mateo762.myapplication.profile

import android.net.Uri
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import java.util.UUID
import javax.inject.Inject

class UserImageStorageServiceImpl @Inject constructor(
    private val storageReference: StorageReference,
    private val databaseReference: DatabaseReference
) :
    UserImageStorageService {
    override fun storeImage(uid: String?, imageUri: Uri?) {
        if (imageUri != null && uid != null) {
            val imagesRef = storageReference.child("users/${uid}/images/${UUID.randomUUID()}.jpg")
            val uploadTask = imagesRef.putFile(imageUri)
            uploadTask.addOnSuccessListener { uri ->
                databaseReference.child("users").child(uid).child("url").setValue(uri.toString())
            }
        }
    }
}