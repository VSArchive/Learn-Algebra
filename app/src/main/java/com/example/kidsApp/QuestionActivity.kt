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
import kotlinx.android.synthetic.main.activity_question.*
import java.util.*

class QuestionActivity : AppCompatActivity() {

    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        tts = TextToSpeech(applicationContext,
            TextToSpeech.OnInitListener { status ->
                if (status != TextToSpeech.ERROR) {
                    tts!!.language = Locale.US
                }
            })

        if (QuestionDetailsActivity.questionDisplayTypePosition == 0) {
            question.text = QuestionDetailsActivity.question
        } else {
            when (QuestionDetailsActivity.rankOfDigits) {
                1 -> {
                    if (QuestionDetailsActivity.spinnerPosition == 1) {
                        question1.textSize = 150f
                    } else {
                        question1.textSize = 300f
                    }
                }
                2 -> {
                    if (QuestionDetailsActivity.spinnerPosition == 1) {
                        question1.textSize = 100f
                    } else {
                        question1.textSize = 200f
                    }
                }
                3 -> {
                    if (QuestionDetailsActivity.spinnerPosition == 1) {
                        question1.textSize = 85f
                    } else {
                        question1.textSize = 150f
                    }
                }
                4 -> {
                    if (QuestionDetailsActivity.spinnerPosition == 1) {
                        question1.textSize = 75f
                    } else {
                        question1.textSize = 100f
                    }
                }
            }
            question1.setCharacterDelay(QuestionDetailsActivity.speed.toLong())
            question1.animateText(QuestionDetailsActivity.question.split(",").map { it.trim() })
        }

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
                if (String.format("%.2f", QuestionDetailsActivity.Answer)
                        .toDouble() == String.format(
                        "%.2f",
                        answer.editText?.text.toString().toDouble()
                    ).toDouble()
                ) {
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