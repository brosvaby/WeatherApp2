package com.brosvaby.android.weatherapp

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle

class LoadingDialog(context: Context): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        // test commit
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
        window?.run {
            setCancelable(false)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}