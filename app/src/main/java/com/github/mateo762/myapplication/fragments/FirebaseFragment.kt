package com.github.mateo762.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.github.mateo762.myapplication.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class FirebaseFragment : Fragment() {
    private lateinit var email: EditText
    private lateinit var phone: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.firebase_main, container, false)
        email = view.findViewById(R.id.emailField)
        phone = view.findViewById(R.id.phoneField)
        val set = view.findViewById<Button>(R.id.setButton)
        var get = view.findViewById<Button>(R.id.getButton)
        set.setOnClickListener{
            val emailText = email.text.toString()
            var phoneText = phone.text.toString()
            Log.d("TAG", "Clicked Set $emailText $phoneText")
            val db: DatabaseReference = Firebase.database.reference
            db.child(phoneText).setValue(emailText)



        }
        get.setOnClickListener{
            val emailText = email.text.toString()
            var phoneText = phone.text.toString()
            Log.d("TAG", "Clicked Get $emailText $phoneText")
        }



        return view
    }


}