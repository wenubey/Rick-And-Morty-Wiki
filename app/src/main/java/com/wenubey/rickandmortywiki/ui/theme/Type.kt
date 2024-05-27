package com.wenubey.rickandmortywiki.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.wenubey.rickandmortywiki.R

val sourceCodeFamily = FontFamily(
    Font(R.font.source_code_pro_extra_light, FontWeight.ExtraLight),
    Font(R.font.source_code_pro_light, FontWeight.Light),
    Font(R.font.source_code_pro_regular, FontWeight.Normal),
    Font(R.font.source_code_pro_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.source_code_pro_medium, FontWeight.Medium),
    Font(R.font.source_code_pro_semi_bold, FontWeight.SemiBold),
    Font(R.font.source_code_pro_bold, FontWeight.Bold),
    Font(R.font.source_code_pro_extra_bold, FontWeight.ExtraBold),
    Font(R.font.source_code_pro_black, FontWeight.Black),
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = sourceCodeFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = sourceCodeFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = sourceCodeFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.25.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)