package com.github.mateo762.myapplication.username

import androidx.collection.ArraySet
import kotlinx.coroutines.flow.Flow

/**
 * Service interface for the username checking if a username is available and choosing a username.
 */
interface UsernameService {

    /**
     * Method that returns a set of usernames.
     */
    fun getUsernames() : Flow<ArraySet<String>>

    /**
     * Method that posts a username to the usernames reference.
     */
    fun postUsernameToUsernames(username: String, uid: String) : Flow<Unit>

    /**
     * Method that updates the usernames under the user reference.
     */
    fun postUsernameToUser(username: String, uid: String) : Flow<Unit>

    /**
     * Method that deletes the username.
     */
    fun deleteUsername(username: String)
}