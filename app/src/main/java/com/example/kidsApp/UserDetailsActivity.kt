package com.example.kidsApp

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_user_details.*

class UserDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        val maleOrFemale = MaleOrFemale.checkedRadioButtonId
        val sex = findViewById<RadioButton>(maleOrFemale).text
        val auth = Firebase.auth
        val mUserDbRef = Firebase.database.reference
        val map: HashMap<String, Any> = HashMap()

        Submit.setOnClickListener {
            map["email"] = SignUpActivity.emailPerson
            map["username"] = SignUpActivity.user
            map["studentName"] = studentName.editText?.text.toString()
            map["dob"] = DateOfBirth.editText?.text.toString()
            map["phoneNo"] = PhoneNo.editText?.text.toString()
            map["Country"] = Country.editText?.text.toString()
            map["sex"] = sex
            map["id"] = auth.currentUser!!.uid
            mUserDbRef.child("Users").child(auth.currentUser!!.uid).setValue(map)
                .addOnSuccessListener {
                    startActivity(Intent(this, StartActivity::class.java))
                    finish()
                }
        }
    }
}