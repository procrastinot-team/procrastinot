package com.github.mateo762.myapplication

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import com.github.mateo762.myapplication.ui.upload.UploadImageScreen

// TODO: This class should be deleted ASAP

class UploadFragment : Fragment() {



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            contentDescription = "upload_compose_view"
            setContent {
                    UploadImageScreen(userId = "u0YjFxx6H9TwoFh6aEodiRKz8NX2", habitId = "7254b3c2-1d9f-4269-82a9-d761cc979a6d",
                    image = R.drawable.walk_1)
            }
        }
    }
}