package com.vs.learn.algebra

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_division_question.*
import java.util.*

class DivisionQuestionActivity : AppCompatActivity() {

    private var tts: TextToSpeech? = null
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_division_question)
        tts = TextToSpeech(
            applicationContext
        ) { status ->
            if (status != TextToSpeech.ERROR) {
                tts!!.language = Locale.US
            }
        }

        question.text = ""
        if (DivisionQuestionDetailsActivity.questionDisplayTypePosition == 0) {
            question.text =
                DivisionQuestionDetailsActivity.questionArray.split(",").map { it.trim() }[index]
        } else {
            question1.setCharacterDelay(DivisionQuestionDetailsActivity.speed.toLong())
            question1.animateText(
                DivisionQuestionDetailsActivity.questionArray.split(",")
                    .map { it.trim() }[index].split("÷").map { it.trim() })
        }

        if (DivisionQuestionDetailsActivity.spinnerPosition == 0 || DivisionQuestionDetailsActivity.spinnerPosition == 2) {
            remainder.visibility = View.GONE
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
                if (String.format(
                        "%.2f",
                        DivisionQuestionDetailsActivity.AnswerArray.split(",")
                            .map { it.trim() }[index].toDouble()
                    )
                        .toDouble() == String.format(
                        "%.2f",
                        answer.editText?.text.toString().toDouble()
                    ).toDouble()
                ) {
                    if (DivisionQuestionDetailsActivity.spinnerPosition == 1) {
                        if (remainder.editText?.text?.trim().toString() != "") {
                            if (String.format(
                                    "%.2f",
                                    DivisionQuestionDetailsActivity.RemainderArray.split(",")
                                        .map { it.trim() }[index].toDouble()
                                )
                                    .toDouble() == String.format(
                                    "%.2f",
                                    remainder.editText?.text.toString().toDouble()
                                ).toDouble()
                            ) {
                                correctAnswer(tw)
                            } else {
                                wrongAnswer(tw)
                            }
                        } else {
                            remainder.error = "Please enter Something"
                        }
                    } else {
                        correctAnswer(tw)
                    }
                } else {
                    wrongAnswer(tw)
                }
            } else {
                answer.error = "Please enter Something"
            }
        }
    }

    private fun correctAnswer(tw: TypeWriter) {
        tw.setCharacterDelay(100)
        tw.animateText("That`s correct")
        ++index
        if (index < DivisionQuestionDetailsActivity.questionArray.split(",")
                .map { it.trim() }.size
        ) {
            if (DivisionQuestionDetailsActivity.questionDisplayTypePosition == 0) {
                question.text = DivisionQuestionDetailsActivity.questionArray.split(",")
                    .map { it.trim() }[index]
            } else {
                question1.setCharacterDelay(DivisionQuestionDetailsActivity.speed.toLong())
                question1.animateText(
                    DivisionQuestionDetailsActivity.questionArray.split(",")
                        .map { it.trim() }[index].split(" ÷ ").map { it.trim() })
            }
        } else {
            index = 0
        }
        tts!!.speak(
            "Thats correct answer, Well done",
            TextToSpeech.QUEUE_ADD,
            null,
            null
        )
        AnswerStatusImg.setImageDrawable(resources.getDrawable(R.drawable.ic_correct, null))
        Handler().postDelayed({
            tw.text = ""
            tw.setCharacterDelay(150)
            tw.animateText("Well done")
        }, 2200)
        Toast.makeText(this, "Correct answer", Toast.LENGTH_SHORT).show()
    }

    private fun wrongAnswer(tw: TypeWriter) {
        tw.setCharacterDelay(100)
        tw.animateText("That`s incorrect")
        ++index
        if (index < DivisionQuestionDetailsActivity.questionArray.split(",")
                .map { it.trim() }.size
        ) {
            if (DivisionQuestionDetailsActivity.questionDisplayTypePosition == 0) {
                question.text = DivisionQuestionDetailsActivity.questionArray.split(",")
                    .map { it.trim() }[index]
            } else {
                question1.setCharacterDelay(DivisionQuestionDetailsActivity.speed.toLong())
                question1.animateText(
                    DivisionQuestionDetailsActivity.questionArray.split(",")
                        .map { it.trim() }[index].split(" ÷ ").map { it.trim() })
            }
        } else {
            index = 0
        }
        tts!!.speak(
            "That's incorrect answer!",
            TextToSpeech.QUEUE_ADD,
            null,
            null
        )
        AnswerStatusImg.setImageDrawable(resources.getDrawable(R.drawable.ic_incorrect, null))
        Handler().postDelayed({
            tw.text = ""
            tw.setCharacterDelay(150)
            tw.animateText("Try  again!")
        }, 2200)
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