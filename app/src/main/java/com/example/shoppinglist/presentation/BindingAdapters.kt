package com.example.shoppinglist.presentation

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.shoppinglist.R
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("numberToString")
fun bindNumberToString(textView: TextView, number: Int) {
    textView.text = number.toString()
}

@BindingAdapter("inputNameError")
fun bindInputNameError(textInputLayout: TextInputLayout, error: Boolean) {
    if (error)
        textInputLayout.error = textInputLayout.context.getString(R.string.error_input_name)
    else
        textInputLayout.error = null
}

@BindingAdapter("inputCountError")
fun bindInputCountError(textInputLayout: TextInputLayout, error: Boolean) {
    if (error)
        textInputLayout.error = textInputLayout.context.getString(R.string.error_input_count)
    else
        textInputLayout.error = null
}