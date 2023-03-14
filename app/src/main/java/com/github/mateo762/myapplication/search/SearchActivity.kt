package com.github.mateo762.myapplication.search

import android.os.Bundle
import com.github.mateo762.myapplication.BaseActivity
import com.github.mateo762.myapplication.R


class SearchActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        super.onCreateDrawer();
    }
}