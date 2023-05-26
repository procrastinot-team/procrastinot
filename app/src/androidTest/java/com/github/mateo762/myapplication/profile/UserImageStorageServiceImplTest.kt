package com.github.mateo762.myapplication.profile

import android.net.Uri
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class UserImageStorageServiceImplTest {

    private lateinit var service: UserImageStorageService
    private lateinit var storageReference: StorageReference
    private lateinit var childStorageReference: StorageReference
    private lateinit var uploadTask: UploadTask
    private lateinit var databaseReference: DatabaseReference
    private lateinit var uri: Uri

    @Before
    fun setUp() {
        storageReference = mock(StorageReference::class.java)
        childStorageReference = mock(StorageReference::class.java)
        uploadTask = mock(UploadTask::class.java)
        `when`(storageReference.child(anyString())).thenReturn(childStorageReference)
        `when`(childStorageReference.putFile(any())).thenReturn(uploadTask)
        databaseReference = mock(DatabaseReference::class.java)
        uri = mock(Uri::class.java)

        service = UserImageStorageServiceImpl(storageReference, databaseReference)

    }

    @Test
    fun storeImage() {
        //when
        service.storeImage("uid", uri)

        //then
        verify(storageReference).child(anyString())
        verify(childStorageReference).putFile(any())
    }
}