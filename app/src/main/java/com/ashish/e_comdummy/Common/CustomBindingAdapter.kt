package com.ashish.e_comdummy.Common

import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.ashish.e_comdummy.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.imageview.ShapeableImageView

/**
 * Created by Ashish Tiwari on 30,April,2021
 */
@BindingAdapter("imageUrl", "placeholder", requireAll = false)
fun loadImage(img: AppCompatImageView, imageURL: String?, placeholder: Drawable?) {
    val url: String = imageURL ?: ""
    val holder: Drawable? = placeholder
        ?: ContextCompat.getDrawable(img.context, R.mipmap.ic_launcher)
    if (url.isNotEmpty())
        Glide.with(img.context)
            .load(url)
            .thumbnail(0.5f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(holder)
            .into(img)
    else
        Glide.with(img.context).load(placeholder).into(img)
}

@BindingAdapter("htmlText", "strikeThrough", requireAll = false)
fun setHtmlTextValue(textView: TextView, htmlText: String?, strikeThrough: Boolean = false) {
    if (htmlText == null) return
    val result: Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(htmlText)
    }
    if (strikeThrough)
        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    textView.text = result
}

@SuppressLint("SetTextI18n")
@BindingAdapter("amount", "isStrike", requireAll = false)
fun currencyFormatINR(textView: TextView, value: String?, strike: Boolean = false) {
    if (value == null) return

    val amount = value.toDouble()

    if (strike)
        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

    textView.text = textView.context.getString(R.string.Rupees) + " " + String.format("%,.0f", amount)
}