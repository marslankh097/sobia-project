package com.example.budgetly.utils.shared_components
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.secondaryBgColor
import ir.kaaveh.sdpcompose.sdp

//@Composable
//fun GlideImage(
//    modifier: Modifier = Modifier,
//    model: Any,
//    contentScale: ContentScale = ContentScale.Fit,
//    corners: Int = 0,
//    showBackground: Boolean = false,
//    onClick: (() -> Unit)? = null
//) {
//    val clickModifier = if (onClick != null) Modifier.safeClickAble {
//        onClick()
//    } else Modifier
//    GlideImage(
//        model = model,
//         modifier = modifier
//             .clip(RoundedCornerShape(corners.sdp))
//             .then(if (showBackground) Modifier.background(secondaryBgColor) else Modifier)
//             .then(clickModifier),
//        contentScale = contentScale,
//        )
//}
@Composable
fun GifImage(
    modifier: Modifier = Modifier,
    gifUri: String,
    placeholder: Int,
    contentScale: ContentScale = ContentScale.Fit
) {
    val context = LocalContext.current

    // ✅ ImageLoader remembered
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                add(GifDecoder.Factory())
            }
            .build()
    }

    // ✅ Request remembered (important!)
    val request = remember(gifUri) {
        ImageRequest.Builder(context)
            .data(gifUri)
            .placeholder(placeholder)
            .error(placeholder)
            .fallback(placeholder)
            .build()
    }

    val painter = rememberAsyncImagePainter(
        model = request,
        imageLoader = imageLoader
    )

    Image(
        painter = painter,
        contentDescription = null,
        contentScale = contentScale,
        modifier = modifier
    )
}

@Composable
fun CoilImage(
    modifier: Modifier = Modifier,
    model: Any?,
    @DrawableRes placeholder: Int,
    contentScale: ContentScale = ContentScale.Fit,
    corners: Int = 0,
    showBackground: Boolean = false,
    bgColor: androidx.compose.ui.graphics.Color = secondaryBgColor,
    tintColor: androidx.compose.ui.graphics.Color = primaryColor,
    applyTint:Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(model)
            .placeholder(placeholder)
            .error(placeholder)
            .fallback(placeholder)
            .build()
    )
    val clickModifier = if (onClick != null) Modifier.safeClickAble {
        onClick()
    } else Modifier
    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier
            .clip(RoundedCornerShape(corners.sdp))
            .then(if (showBackground) Modifier.background(bgColor) else Modifier)
            .then(clickModifier),
        contentScale = contentScale,
        colorFilter = if(applyTint) ColorFilter.tint(color = tintColor) else null
    )
}
