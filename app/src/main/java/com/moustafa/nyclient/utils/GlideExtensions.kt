package com.moustafa.nyclient.utils

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target


/**
 * @author moustafasamhoury
 * created on Friday, 20 Sep, 2019
 */
/**
 * Loads image file from the web into the imageview using [imageUrl].
 * Providing [roundedCorners] will be applied as a radius in dp to the image corners
 * Also provides [onSuccess] and [onFailed] callback optional methods
 */
fun ImageView.load(
    imageUrl: String,
    roundedCorners: Int? = null,
    onSuccess: ((Drawable?) -> Unit)? = null,
    onFailed: (() -> Unit)? = null
) {
    var requestBuilder: GlideRequest<Drawable> = GlideApp.with(this)
        .load(imageUrl)
        .transition(withCrossFade())

    if (roundedCorners != null) {
        requestBuilder =
            requestBuilder.apply(RequestOptions().transform(RoundedCorners(roundedCorners.px())))
    }

    requestBuilder = requestBuilder.listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            onFailed?.invoke()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            onSuccess?.invoke(resource)
            return false
        }
    })
    requestBuilder.load(Uri.parse(imageUrl)).into(this)
}
