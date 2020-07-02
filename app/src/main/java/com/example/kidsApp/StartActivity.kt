package com.example.kidsApp

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val signInId = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val progressDialog = ProgressDialog(this, R.style.MyAlertDialogStyle)

        auth = Firebase.auth
        signUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
//            finish()
        }

        forgotPassword.setOnClickListener {
            if (email.editText?.text?.trim()?.length!! > 0) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email.editText?.text.toString())
                Toast.makeText(this, "Reset link sent", Toast.LENGTH_SHORT).show()
            } else {
                email.editText?.error = "Enter background valid email"
            }
        }

        loginBtn.setOnClickListener {
            progressDialog.setMessage("please wait!")
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog.show()
            if (email.editText?.text?.trim()?.length!! > 0 && password.editText?.text?.trim()?.length!! > 6) {
                auth.signInWithEmailAndPassword(
                    email.editText?.text.toString(),
                    password.editText?.text.toString()
                ).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user?.isEmailVerified!!) {
                            if (user.displayName != null) {
                                Toast.makeText(
                                    this, "Welcome ${user.displayName}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            progressDialog.dismiss()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(this, "Please verify email address", Toast.LENGTH_SHORT)
                                .show()
                            FirebaseAuth.getInstance().signOut()
                        }

                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else if (email.editText?.text?.length!! > 0) {
                progressDialog.dismiss()
                password.editText?.error = "Password should be grater than 6 letters"
            } else {
                progressDialog.dismiss()
                email.editText?.error = "Enter background valid email"
            }
        }


        // Google sign in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

//        signIn.setOnClickListener {
//            signIn()
//        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, signInId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == signInId) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } catch (e: ApiException) {
                Log.w("TAG", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithCredential:success")
                } else {
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                }
            }
    }

    override fun onStart() {
        super.onStart()
        auth = Firebase.auth
        val user = auth.currentUser
        if (FirebaseAuth.getInstance().currentUser != null && user!!.isEmailVerified) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}