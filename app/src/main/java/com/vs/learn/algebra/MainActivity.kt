package com.vs.learn.algebra

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val immersiveFlagTimeOut = 500L
    private lateinit var container: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)
        container = findViewById(R.id.background)
        val fullScreenFlags =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        container.postDelayed({
            container.systemUiVisibility = fullScreenFlags
        }, immersiveFlagTimeOut)
        val popIn = AnimationUtils.loadAnimation(this, R.anim.popin)
        Addition.startAnimation(popIn)
        AdditionSubtraction.startAnimation(popIn)
        Multiplication.startAnimation(popIn)
        Division.startAnimation(popIn)
        SquareRoot.startAnimation(popIn)
        CubeRoot.startAnimation(popIn)

        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }

        drop_animation_view.setDrawables(
            R.drawable.plus,
            R.drawable.minus,
            R.drawable.mul,
            R.drawable.division
        )
        drop_animation_view.startAnimation()

        Addition.setOnClickListener {
            val intent = Intent(this, QuestionDetailsActivity::class.java)
            intent.putExtra("operation", "plus")
            startActivity(intent)
        }
        AdditionSubtraction.setOnClickListener {
            val intent = Intent(this, QuestionDetailsActivity::class.java)
            intent.putExtra("operation", "plusMinus")
            startActivity(intent)
        }
        Multiplication.setOnClickListener {
            val intent = Intent(this, QuestionDetailsActivity::class.java)
            intent.putExtra("operation", "multi")
            startActivity(intent)
        }
        Division.setOnClickListener {
            val intent = Intent(this, DivisionQuestionDetailsActivity::class.java)
            intent.putExtra("operation", "div")
            startActivity(intent)
        }
        SquareRoot.setOnClickListener {
            val intent = Intent(this, SquareRootQuestionActivity::class.java)
            startActivity(intent)
        }
        CubeRoot.setOnClickListener {
            val intent = Intent(this, CubeRootQuestionActivity::class.java)
            startActivity(intent)
        }
    }
}