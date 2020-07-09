package com.example.kidsApp

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val progressDialog = ProgressDialog(this, R.style.MyAlertDialogStyle)

        auth = Firebase.auth
        signUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
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