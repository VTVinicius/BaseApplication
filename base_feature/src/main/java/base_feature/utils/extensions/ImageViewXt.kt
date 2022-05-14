package base_feature.utils.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.example.base_feature.BuildConfig

fun ImageView.loadUrl(
    url: String?,
    @DrawableRes resPlaceholder: Int? = null,
    onFailure: (Exception?) -> Unit = {},
    onSuccess: () -> Unit = {},
    cleanCache: Boolean = false
) {
    getRequestBuilder(url, resPlaceholder, onFailure, onSuccess, cleanCache)?.into(this)
}

fun ImageView.loadUrlWithCircular(
    url: String?,
    @DrawableRes resPlaceholder: Int? = null,
    onFailure: (Exception?) -> Unit = {},
    onSuccess: () -> Unit = {},
    cleanCache: Boolean = false
) {
    getRequestBuilder(url, resPlaceholder, onFailure, onSuccess, cleanCache)
        ?.transition(DrawableTransitionOptions.withCrossFade())
        ?.transform(CenterCrop(), CircleCrop())?.into(this)
}

fun ImageView.getRequestBuilder(
    url: String?,
    @DrawableRes resPlaceholder: Int? = null,
    onFailure: (Exception?) -> Unit = {},
    onSuccess: () -> Unit = {},
    cleanCache: Boolean = false,
): RequestBuilder<Drawable>? {
    if (url.isNullOrBlank()) return null
    return Glide.with(this)
        .load(url)
        .apply {
            if(cleanCache) {
                diskCacheStrategy(DiskCacheStrategy.NONE)
                skipMemoryCache(true)
                signature(ObjectKey(System.currentTimeMillis().toString()))
            }
            resPlaceholder?.let { placeholder(it) }
        }
        .centerCrop()
        .fitCenter()
        .addListener(getGlideCallback(onSuccess, onFailure))
}

private fun getGlideCallback(
    onSuccess: () -> Unit,
    onFailure: (Exception?) -> Unit
): RequestListener<Drawable> {
    return object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            if (BuildConfig.DEBUG) e?.printStackTrace()
            onFailure(e)
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            onSuccess()
            return false
        }

    }
}