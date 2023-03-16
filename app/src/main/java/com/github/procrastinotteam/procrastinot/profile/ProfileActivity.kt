package com.github.procrastinotteam.procrastinot.profile

import android.os.Bundle
import com.github.procrastinotteam.procrastinot.BaseActivity
import com.github.procrastinotteam.procrastinot.R

class ProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        super.onCreateDrawer()
    }
}