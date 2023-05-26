package com.github.mateo762.myapplication.profile

import android.util.Log
import com.github.mateo762.myapplication.di.FirebaseModule
import com.github.mateo762.myapplication.di.ProfileModule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [FirebaseModule::class]
)
class MockFirebaseModule {

    @Provides
    @Singleton
    fun provideFirebase(): DatabaseReference {
        val db = Firebase.database
        try {
            db.useEmulator("10.0.2.2", 9000)
        } catch (exception: Exception) {
            Log.d("MockFirebaseModule", exception.toString())
        }

        return db.reference
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage(): StorageReference {
        return Firebase.storage.reference
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}