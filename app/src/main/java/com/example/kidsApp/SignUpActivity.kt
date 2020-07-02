package com.example.kidsApp

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val progressDialog = ProgressDialog(this, R.style.MyAlertDialogStyle)

        auth = Firebase.auth
        signUpBtn.setOnClickListener {
            progressDialog.setMessage("please wait!")
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog.show()
            val mUserDbRef = FirebaseDatabase.getInstance().reference
            if (email.editText?.text?.trim()?.length!! > 0 && password.editText?.text?.trim()?.length!! > 6) {
                auth.createUserWithEmailAndPassword(
                    email.editText?.text.toString(),
                    password.editText?.text.toString()
                ).addOnSuccessListener {

                    val user = Firebase.auth.currentUser
                    user!!.sendEmailVerification()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("TAG", "Email sent.")
                                val map: HashMap<String, Any> = HashMap()
                                map["email"] = email.editText?.text.toString()
                                map["username"] = userName.editText?.text.toString()
                                map["id"] = auth.currentUser!!.uid
                                progressDialog.dismiss()
                                mUserDbRef.child(auth.currentUser!!.uid).setValue(map)
//                                val intent = Intent(this, MainActivity::class.java)
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                                startActivity(intent)
                                finish()
                            }
                        }
                }.addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(
                        this, "SignUp failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else{
                progressDialog.dismiss()
                Toast.makeText(this,"Enter Valid details",Toast.LENGTH_SHORT).show()
            }
        }
        logIn.setOnClickListener {
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }
    }
}