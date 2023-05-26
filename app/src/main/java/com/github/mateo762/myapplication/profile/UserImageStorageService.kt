package com.github.mateo762.myapplication.profile

import android.net.Uri

interface UserImageStorageService {
    fun storeImage(uid: String?, imageUri: Uri?)
}