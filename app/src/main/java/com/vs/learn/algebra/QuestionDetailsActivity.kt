package com.vs.learn.algebra

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_question_details.*
import kotlin.random.Random

class QuestionDetailsActivity : AppCompatActivity() {

    companion object {
        var Answer = 0.0
        var question: String = ""
        var speed: Int = 1000
        var spinnerPosition: Int = 0
        var rankOfDigits = 2
        var questionDisplayTypePosition: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_details)

        drop_animation_view.setDrawables(
            R.drawable.plus,
            R.drawable.minus,
            R.drawable.mul,
            R.drawable.division
        )
        drop_animation_view.startAnimation()

        val typeOfNumbers = resources.getStringArray(R.array.TypeOfNumbers)
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

        askQuestion.setOnClickListener {
            question = ""
            Answer = 0.0
            if (noOfDigits.editText?.text?.trim()?.length != 0 && totalDigits.editText?.text?.length != 0) {
                rankOfDigits = noOfDigits.editText?.text.toString().toInt()
                val totalNoOfDigits = totalDigits.editText?.text.toString().toInt()
                generate(rankOfDigits, totalNoOfDigits)
            } else if (noOfDigits.editText?.text?.trim()?.length != 0) {
                rankOfDigits = noOfDigits.editText?.text.toString().toInt()
                generate(rankOfDigits, 2)
            } else if (totalDigits.editText?.text?.trim()?.length != 0) {
                val totalNoOfDigits = totalDigits.editText?.text.toString().toInt()
                generate(2, totalNoOfDigits)
            } else {
                generate(2, 2)
            }
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                spinnerPosition = 0
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

        if (intent.getStringExtra("operation") == "plusMinus") {
            questionDisplayType.visibility = View.GONE
            questionDisplayTypePosition = 0
            seekBar.visibility = View.GONE
            textAnimationDuration.visibility = View.GONE
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
    }

    private fun generate(rankOfDigits: Int, totalNoOfDigits: Int) {
        val x: Int
        val y: Int
        when (rankOfDigits) {
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
                noOfDigits.error = "digits should be less than 5"
                return
            }
        }
        if (totalNoOfDigits <= 30) {
            for (i in 1..totalNoOfDigits) {
                if (spinnerPosition == 0) {
                    val randomInt = Random.nextInt(x, y)
                    var op = "+"
                    Log.e("", intent.getStringExtra("operation")!!)
                    if (intent.getStringExtra("operation") == "plus") {
                        op = "+"
                        Answer += randomInt
                    } else if (intent.getStringExtra("operation") == "plusMinus") {
                        val randOp = Random.nextInt(0, 2)
                        if (randOp == 0) {
                            op = "+"
                            Answer += randomInt
                        } else {
                            op = "-"
                            Answer -= randomInt
                        }
                    } else if (intent.getStringExtra("operation") == "multi") {
                        op = "X"
                        if (Answer == 0.0) {
                            Answer = randomInt.toDouble()
                        } else {
                            Answer *= randomInt
                        }
                    } else if (intent.getStringExtra("operation") == "div") {
                        op = "÷"
                        if (Answer == 0.0) {
                            Answer = randomInt.toDouble()
                        } else {
                            Answer /= randomInt
                        }
                    }

                    question = if (question == "") {
                        randomInt.toString()
                    } else {
                        if (questionDisplayTypePosition == 0) {
                            "$question $op $randomInt"
                        } else {
                            "$question,$randomInt"
                        }
                    }
                } else {
                    val randomDouble =
                        String.format("%.2f", Random.nextDouble(x.toDouble(), y.toDouble()))
                            .toDouble()
                    var op = "+"
                    Log.e("", intent.getStringExtra("operation")!!)
                    if (intent.getStringExtra("operation") == "plus") {
                        op = "+"
                        Answer += randomDouble
                    } else if (intent.getStringExtra("operation") == "plusMinus") {
                        val randOp = Random.nextInt(0, 2)
                        if (randOp == 0) {
                            op = "+"
                            Answer += randomDouble
                        } else {
                            op = "-"
                            Answer -= randomDouble
                        }
                    } else if (intent.getStringExtra("operation") == "multi") {
                        op = "X"
                        if (Answer == 0.0) {
                            Answer = randomDouble
                        } else {
                            Answer *= randomDouble
                        }
                    } else if (intent.getStringExtra("operation") == "div") {
                        op = "÷"
                        if (Answer == 0.0) {
                            Answer = randomDouble
                        } else {
                            Answer /= randomDouble
                        }
                    }

                    question = if (question == "") {
                        randomDouble.toString()
                    } else {
                        if (questionDisplayTypePosition == 0) {
                            "$question $op $randomDouble"
                        } else {
                            "$question,$randomDouble"
                        }
                    }
                }
            }

            startActivity(Intent(this, QuestionActivity::class.java))
        } else {
            totalDigits.error = "digits should be less than 30"
            return
        }
    }
}