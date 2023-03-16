package com.github.procrastinotteam.procrastinot.search

import android.os.Bundle
import com.github.procrastinotteam.procrastinot.BaseActivity
import com.github.procrastinotteam.procrastinot.R


class SearchActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        super.onCreateDrawer()
    }
}