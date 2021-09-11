package com.bombadu.techpop.ui.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bombadu.techpop.R
import com.bombadu.techpop.ui.authentication.AuthenticationActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class AccountFragment : Fragment() {

override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signOutButton = view.findViewById<Button>(R.id.account_sign_out_button)
        val profileImageView = view.findViewById<ImageView>(R.id.profile_image_view)
        val nameTextView = view.findViewById<TextView>(R.id.account_name_text_view)
        val user = FirebaseAuth.getInstance().currentUser
        val userImage= user?.photoUrl
        val name = user?.displayName
        nameTextView.text = name
        Picasso.get().load(userImage).into(profileImageView)
        signOutButton.setOnClickListener {
            signOut()
        }
    }


    private fun signOut() {

        AuthUI.getInstance()
            .signOut(requireContext())
            .addOnCompleteListener {
                startActivity(Intent(requireContext(), AuthenticationActivity::class.java))
                Toast.makeText(requireContext(), "Signed Out", Toast.LENGTH_SHORT).show()
                activity?.finish()
            }


    }

}