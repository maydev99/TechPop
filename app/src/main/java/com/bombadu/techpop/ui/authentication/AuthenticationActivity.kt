package com.bombadu.techpop.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bombadu.techpop.R
import com.bombadu.techpop.databinding.ActivityAuthenticationBinding
import com.bombadu.techpop.ui.update.UpdateActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding
    private lateinit var auth: FirebaseAuth

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)




    }

    private fun createSignInIntent() {

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())


        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.techpopiconstore)
            .setTheme(R.style.Theme_TechPop_Auth)
            .build()
        signInLauncher.launch(signInIntent)

    }



    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        //val response = result.idpResponse

        if (result.resultCode == RESULT_OK) {
            startActivity(Intent(this, UpdateActivity::class.java))
            finish()
            // ...
        } else {
            Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
            Log.i("MAY", "Login Failed: ${result.idpResponse}")

        }
    }



    override fun onStart() {
        super.onStart()
        //Checks if user is logged in
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            binding.signInButton.visibility = View.INVISIBLE
            startActivity(Intent(this, UpdateActivity::class.java))
            finish()
        } else {
            binding.signInButton.visibility = View.VISIBLE
            binding.signInButton.setOnClickListener {
                createSignInIntent()
            }
        }
    }
}