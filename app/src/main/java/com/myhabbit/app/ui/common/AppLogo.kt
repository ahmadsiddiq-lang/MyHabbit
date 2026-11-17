package com.myhabbit.app.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Logo MyHabbit — kotak bersudut dengan tanda centang (mockup UI.png).
 * [container]/[iconColor] bisa diatur: putih-di-ungu (splash) atau
 * ungu-di-putih (top bar).
 */
@Composable
fun AppLogo(
    size: Dp = 28.dp,
    container: Color = Color.White,
    iconColor: Color = Color(0xFF5246C8),
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .background(container, RoundedCornerShape(size * 0.3f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(size * 0.62f)
        )
    }
}
