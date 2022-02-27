package com.vs.learn.algebra

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.widget.TextView

@SuppressLint("AppCompatCustomView")
class CounterType : TextView {
    private lateinit var mText: List<String>
    private var mIndex = 0
    private var mDelay: Long = 150 // in ms

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    )

    private val mHandler = Handler()
    private val characterAdder: Runnable = object : Runnable {
        override fun run() {
            text = mText[mIndex++]
            if (mIndex < mText.size) {
                mHandler.postDelayed(this, mDelay)
            }
        }
    }

    fun animateText(txt: List<String>) {
        mText = txt
        mIndex = 0
        text = ""
        mHandler.removeCallbacks(characterAdder)
        mHandler.postDelayed(characterAdder, mDelay)
    }

    fun setCharacterDelay(m: Long) {
        mDelay = m
    }
}