package com.example.kidsApp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_square_root_question.*
import java.util.*
import kotlin.random.Random

class SquareRootQuestionActivity : AppCompatActivity() {

    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_square_root_question)

        tts = TextToSpeech(applicationContext,
            TextToSpeech.OnInitListener { status ->
                if (status != TextToSpeech.ERROR) {
                    tts!!.language = Locale.US
                }
            })

        val squareAnswer = Random.nextInt(0, 101)

        val squareQuestion = squareAnswer * squareAnswer

        val displayQuestion = "√ $squareQuestion"

        question.text = displayQuestion

        val tw = findViewById<TypeWriter>(R.id.text)
        tw.setCharacterDelay(150)
        tw.animateText("Hey, Kid")
        Handler().postDelayed({
            tw.text = ""
            tw.setCharacterDelay(150)
            tw.animateText("Lets test your maths")
        }, 2200)

        submit.setOnClickListener {
            hideKeyboard(this)
            if (answer.editText?.text?.trim().toString() != "") {
                if (squareAnswer == answer.editText?.text.toString().toInt()) {
                    tw.setCharacterDelay(100)
                    tw.animateText("That`s correct")
                    tts!!.speak(
                        "Thats correct answer, Well done",
                        TextToSpeech.QUEUE_ADD,
                        null,
                        null
                    )
                    AnswerStatusImg.setImageDrawable(
                        resources.getDrawable(
                            R.drawable.ic_correct,
                            null
                        )
                    )
                    Handler().postDelayed({
                        tw.text = ""
                        tw.setCharacterDelay(150)
                        tw.animateText("Well done")
                    }, 2200)
                    Toast.makeText(this, "Correct answer", Toast.LENGTH_SHORT).show()
                } else {
                    tw.setCharacterDelay(100)
                    tw.animateText("That`s incorrect")
                    tts!!.speak(
                        "Thats incorrect answer, Try again!",
                        TextToSpeech.QUEUE_ADD,
                        null,
                        null
                    )
                    AnswerStatusImg.setImageDrawable(
                        resources.getDrawable(
                            R.drawable.ic_incorrect,
                            null
                        )
                    )
                    Handler().postDelayed({
                        tw.text = ""
                        tw.setCharacterDelay(150)
                        tw.animateText("Try  again!")
                    }, 2200)
                }
            } else {
                answer.error = "Please enter Something"
            }
        }
    }

    private fun hideKeyboard(activity: Activity) {
        val view = activity.findViewById<View>(android.R.id.content)
        if (view != null) {
            val imm: InputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}