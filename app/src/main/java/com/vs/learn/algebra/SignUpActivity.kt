package com.vs.learn.algebra

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    companion object {
        var user = ""
        var emailPerson = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val progressDialog = ProgressDialog(this, R.style.MyAlertDialogStyle)

        auth = Firebase.auth
        signUpBtn.setOnClickListener {
            progressDialog.setMessage("please wait!")
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog.show()
            if (email.editText?.text?.trim()?.length!! > 0 && password.editText?.text?.trim()?.length!! > 6) {
                auth.createUserWithEmailAndPassword(
                    email.editText?.text.toString(),
                    password.editText?.text.toString()
                ).addOnSuccessListener {
                    val userAuth = Firebase.auth.currentUser
                    userAuth!!.sendEmailVerification()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("TAG", "Email sent.")
                                progressDialog.dismiss()
                                emailPerson = email.editText?.text.toString()
                                user = userName.editText?.text.toString()
                                val intent = Intent(this, UserDetailsActivity::class.java)
                                startActivity(intent)
                            }
                        }
                }.addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(
                        this, "SignUp failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                progressDialog.dismiss()
                Toast.makeText(this, "Enter Valid details", Toast.LENGTH_SHORT).show()
            }
        }
        logIn.setOnClickListener {
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }
    }
}