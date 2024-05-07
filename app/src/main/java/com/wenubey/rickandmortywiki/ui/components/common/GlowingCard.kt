package com.wenubey.rickandmortywiki.ui.components.common

import android.content.res.Configuration
import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wenubey.rickandmortywiki.ui.theme.RickAndMortyWikiTheme

@Composable
fun GlowingCard(
    statusContent: String,
    glowingColor: Color,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White,
    cornersRadius: Dp = 0.dp,
    glowingRadius: Dp = 20.dp,
    xShifting: Dp = 0.dp,
    yShifting: Dp = 0.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = Modifier.padding(vertical = 4.dp)) {
        Box(
            modifier = modifier
                .border(width =2.dp, shape = RoundedCornerShape(cornersRadius), color = glowingColor)
                .drawBehind {
                    val canvasSize = size
                    drawContext.canvas.nativeCanvas.apply {
                        drawRoundRect(
                            0f, // Left
                            0f, // Top
                            canvasSize.width, // Right
                            canvasSize.height, // Bottom
                            cornersRadius.toPx(), // Radius X
                            cornersRadius.toPx(), // Radius Y
                            Paint().apply {
                                color = containerColor.toArgb()
                                isAntiAlias = true
                                setShadowLayer(
                                    glowingRadius.toPx(),
                                    xShifting.toPx(), yShifting.toPx(),
                                    glowingColor
                                        .copy(alpha = 0.85f)
                                        .toArgb()
                                )
                            }
                        )
                    }
                }
        ) {
            content()
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .align(Alignment.BottomCenter)
                .background(glowingColor.copy(alpha = 0.8f)),
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                text = statusContent.uppercase(),
                color = Color.Black,
                fontSize = 24.sp,
            )
        }
    }

}



@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Light mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun GlowingCardPreview() {
    RickAndMortyWikiTheme {
        Surface {
            GlowingCard(
                modifier = Modifier.size(200.dp),
                glowingColor = Color.Red,
                content = {
                    Text(text = "Content", modifier = Modifier.align(Alignment.Center))
                },
                statusContent = "ALIVE"
            )
        }
    }
}