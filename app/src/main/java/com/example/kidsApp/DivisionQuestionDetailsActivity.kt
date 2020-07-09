package com.example.kidsApp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_division_question_details.*
import kotlin.random.Random

class DivisionQuestionDetailsActivity : AppCompatActivity() {
    companion object {
        var Answer = 0.0
        var Remainder = 0
        var AnswerArray = ""
        var RemainderArray = ""
        var question: String = ""
        var questionArray = ""
        var speed: Int = 1000
        var spinnerPosition: Int = 0
        var questionDisplayTypePosition: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_division_question_details)

        drop_animation_view.setDrawables(
            R.drawable.plus,
            R.drawable.minus,
            R.drawable.mul,
            R.drawable.division
        )
        drop_animation_view.startAnimation()

        val typeOfNumbers = resources.getStringArray(R.array.TypeOfNumbersForDivision)
        val typeOfDisplay = resources.getStringArray(R.array.TypeOfQuestion)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line, typeOfNumbers
            )
            spinner.adapter = adapter
        }
        if (questionDisplayType != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line, typeOfDisplay
            )
            questionDisplayType.adapter = adapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerPosition = position
            }
        }
        questionDisplayType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                questionDisplayTypePosition = 0
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                questionDisplayTypePosition = position
                if (position == 0) {
                    seekBar.visibility = View.GONE
                    textAnimationDuration.visibility = View.GONE
                } else {
                    seekBar.visibility = View.VISIBLE
                    textAnimationDuration.visibility = View.VISIBLE
                }
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                speed = progress * 20
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Log.d("onStartTrackingTouch", "Seek Bar")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Log.d("onStopTrackingTouch", "Seek Bar")
            }
        })
        askQuestion.setOnClickListener {
            question = ""
            questionArray = ""
            AnswerArray = ""
            Answer = 0.0

            val sizeOf1stDigit: Int
            val sizeOf2ndDigit: Int
            if (sizeOf1digit.editText?.text?.trim()?.length != 0 && sizeOf2digit.editText?.text?.length != 0) {
                sizeOf1stDigit = sizeOf1digit.editText?.text.toString().toInt()
                sizeOf2ndDigit = sizeOf2digit.editText?.text.toString().toInt()
                generate(sizeOf1stDigit, sizeOf2ndDigit)
            } else if (sizeOf1digit.editText?.text?.trim()?.length != 0) {
                sizeOf1stDigit = sizeOf1digit.editText?.text.toString().toInt()
                sizeOf2ndDigit = 2
                generate(sizeOf1stDigit, sizeOf2ndDigit)
            } else if (sizeOf2digit.editText?.text?.trim()?.length != 0) {
                sizeOf1stDigit = 2
                sizeOf2ndDigit = sizeOf2digit.editText?.text.toString().toInt()
                generate(sizeOf1stDigit, sizeOf2ndDigit)
            } else if (sizeOf1digit.editText?.text?.trim()?.length == 0 && sizeOf2digit.editText?.text?.length == 0) {
                sizeOf1stDigit = 2
                sizeOf2ndDigit = 2
                generate(sizeOf1stDigit, sizeOf2ndDigit)
            } else {
                sizeOf1stDigit = 1
                sizeOf2ndDigit = 1
                generate(sizeOf1stDigit, sizeOf2ndDigit)
            }
        }
    }

    private fun generate(sizeOf1stDigit: Int, sizeOf2ndDigit: Int) {
        val x: Int
        val y: Int
        val a: Int
        val b: Int
        when (sizeOf1stDigit) {
            1 -> {
                x = 1; y = 10
            }
            2 -> {
                x = 10; y = 100
            }
            3 -> {
                x = 100; y = 1000
            }
            4 -> {
                x = 1000; y = 10000
            }
            else -> {
                sizeOf1digit.error = "digits should be less than 5"
                return
            }
        }
        when (sizeOf2ndDigit) {
            1 -> {
                a = 1; b = 10
            }
            2 -> {
                a = 10; b = 100
            }
            3 -> {
                a = 100; b = 1000
            }
            4 -> {
                a = 1000; b = 10000
            }
            else -> {
                sizeOf1digit.error = "digits should be less than 5"
                return
            }
        }
        val questions = if (noOfQuestions.editText?.text?.toString()?.trim() != "") {
            noOfQuestions.editText?.text.toString().toInt()
        } else {
            1
        }
        for (i in 1..questions) {
            when (spinnerPosition) {
                0 -> {
                    val random1stInt = Random.nextInt(x, y)
                    val randomInt = Random.nextInt(0, 10)

                    Answer = randomInt.toDouble()
                    AnswerArray = if (AnswerArray == "") {
                        "$Answer"
                    } else {
                        "$AnswerArray,$Answer"
                    }
                    question = "${randomInt * random1stInt} ÷ $random1stInt"
                }
                1 -> {
                    val random1stInt = Random.nextInt(x, y)
                    var random2ndInt: Int
                    random2ndInt = if (a < random1stInt) {
                        Random.nextInt(a, random1stInt)
                    } else {
                        Random.nextInt(a, random1stInt + a)
                    }
                    Answer = (random1stInt / random2ndInt).toDouble()
                    Remainder = random1stInt % random2ndInt
                    if (AnswerArray == "") {
                        AnswerArray = "$Answer"
                        RemainderArray = "$Remainder"
                    } else {
                        AnswerArray = "$AnswerArray,$Answer"
                        RemainderArray = "$RemainderArray,$Remainder"
                    }
                    question = "$random1stInt ÷ $random2ndInt"
                }
                else -> {
                    val random1stDouble =
                        String.format("%.2f", Random.nextDouble(x.toDouble(), y.toDouble()))
                            .toDouble()
                    val random2ndDouble =
                        String.format("%.2f", Random.nextDouble(a.toDouble(), b.toDouble()))
                            .toDouble()
                    Answer = random1stDouble / random2ndDouble

                    question = "$random1stDouble ÷ $random2ndDouble"
                }
            }
            questionArray = if (questionArray == "") {
                question
            } else {
                "$questionArray,$question"
            }
        }

        startActivity(Intent(this, DivisionQuestionActivity::class.java))
    }
}