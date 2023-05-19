package com.github.mateo762.myapplication

import com.github.mateo762.myapplication.profile.ProfileGalleryAdapter
import com.github.mateo762.myapplication.profile.ProfileGalleryItem
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ProfileGalleryAdapterTest {

    private lateinit var adapter: ProfileGalleryAdapter

    @Before
    fun setup() {
        adapter = ProfileGalleryAdapter()
    }

    @Test
    fun testGetItemCountWithNullGalleryItems() {
        assertEquals(0, adapter.itemCount)
    }

    @Test
    fun testGetItemCountWithEmptyGalleryItems() {
        adapter.galleryItems = ArrayList()
        assertEquals(0, adapter.itemCount)
    }

    @Test
    fun testGetItemCountWithNonEmptyGalleryItems() {
        val items = ArrayList<ProfileGalleryItem>()
        items.add(ProfileGalleryItem(R.mipmap.ic_launcher))
        items.add(ProfileGalleryItem(R.mipmap.ic_launcher))
        adapter.galleryItems = items
        assertEquals(2, adapter.itemCount)
    }

}
