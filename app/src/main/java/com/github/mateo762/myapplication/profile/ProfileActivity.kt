package com.github.mateo762.myapplication.profile

import android.os.Bundle
import com.github.mateo762.myapplication.BaseActivity
import com.github.mateo762.myapplication.R


class ProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        super.onCreateDrawer()
    }
}