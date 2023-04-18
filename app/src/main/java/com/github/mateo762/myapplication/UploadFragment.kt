package com.github.mateo762.myapplication.ui.upload

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.mateo762.myapplication.R

class UploadFragment : Fragment() {



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                    UploadImageScreen(userId = "meCtC5TWlEcBDcy5Ss6hzf8Qg723", habitId = "7254b3c2-1d9f-4269-82a9-d761cc979a6c")
            }
        }
    }
}