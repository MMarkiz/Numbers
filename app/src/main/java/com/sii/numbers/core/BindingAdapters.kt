package com.sii.numbers.core

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("imageUrl")
fun setImageFromUrl(view: ImageView, imageUrl: String?) {
    Picasso.get().load(imageUrl).into(view)
}
