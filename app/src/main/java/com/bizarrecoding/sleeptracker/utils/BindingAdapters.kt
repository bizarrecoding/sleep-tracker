package com.bizarrecoding.sleeptracker.utils

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("app:hideIfNull")
fun hideIfNull(view: View, present: Boolean){
    view.visibility = if (present) View.VISIBLE else View.GONE
}
