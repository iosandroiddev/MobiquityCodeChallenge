package com.manohar.mobiquitycodechallenge.utils

import android.R.attr.textColor
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.manohar.mobiquitycodechallenge.R
import com.manohar.mobiquitycodechallenge.utils.ConstantKeys.weatherDateFormat
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


fun View.show() {
    visibility = View.VISIBLE
}

fun View.inVisible() {
    visibility = View.INVISIBLE
}

fun View.hide() {
    visibility = View.GONE
}


fun Context.getContextDrawable(color: Int): Drawable? {
    return ContextCompat.getDrawable(this, color)
}


fun Context.getFormattedDate(epochTime: Long): String {
    val date = Date(epochTime * 1000L)
    val format: DateFormat = SimpleDateFormat(weatherDateFormat, Locale.getDefault())
    return format.format(date)
}

fun View.showErrorSnackMessage(message: String, context: Context) {
    val snackBar: Snackbar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    val snackBarView = snackBar.view
    snackBarView.setBackgroundColor(context.getContextColor(R.color.humidity_color))
    val textView =
        snackBarView.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
    textView.setTextColor(context.getContextColor(R.color.white))
    snackBar.show()
}

fun Context.getContextColor(color: Int): Int {
    return ContextCompat.getColor(this, color)
}