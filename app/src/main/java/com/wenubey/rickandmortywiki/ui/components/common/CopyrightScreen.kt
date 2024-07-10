package com.wenubey.rickandmortywiki.ui.components.common

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.theme.sourceCodeFamily
import com.wenubey.rickandmortywiki.utils.makeToast
import com.wenubey.rickandmortywiki.utils.openUrlInCustomTabs


@Composable
fun CopyrightScreen(
    onNavigateBack: () -> Unit,
) {
    val color = MaterialTheme.colorScheme.primary
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CommonTopAppBar(
                onBackButtonPressed = onNavigateBack,
                showNavigationIcon = true,
                title = stringResource(id = R.string.copyright_header),
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                CopyrightSection(modifier = Modifier.weight(1f), color = color)

                HorizontalDivider(
                    color = Color.Magenta,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
                Text(
                    text = stringResource(id = R.string.copyright_part_eighth),
                    fontSize = 16.sp,
                    color = color,
                    textAlign = TextAlign.Center,
                    fontFamily = sourceCodeFamily,
                )

            }
        },
    )
}

@Composable
private fun CopyrightSection(modifier: Modifier = Modifier, color: Color) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val text = getCopyRightText(color = color)

    ClickableText(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
            .verticalScroll(scrollState),
        text = text,
        onClick = { offset ->
            text.getStringAnnotations(tag = "rick_and_morty", start = offset, end = offset)
                .firstOrNull()?.let { link ->
                    context.openUrlInCustomTabs(link.item)
                }
            text.getStringAnnotations(tag = "adult_swim", start = offset, end = offset)
                .firstOrNull()
                ?.let { link ->
                    context.openUrlInCustomTabs(link.item)
                }
            text.getStringAnnotations(tag = "email", start = offset, end = offset).firstOrNull()
                ?.let { link ->
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        setData(Uri.parse("mailto:"))
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(link.item))
                    }
                    try {
                        context.startActivity(intent)
                    } catch (ex: ActivityNotFoundException) {
                        context.makeToast(R.string.error_email_app_not_found)
                    }
                }
            text.getStringAnnotations(tag = "play_developer", start = offset, end = offset)
                .firstOrNull()?.let { link ->
                    try {
                        val playStoreWebsiteIntent =
                            Intent(Intent.ACTION_VIEW, Uri.parse(link.item))
                        context.startActivity(playStoreWebsiteIntent)
                    } catch (e: ActivityNotFoundException) {
                        context.makeToast(R.string.error_email_app_not_found)
                    }
                }
        },
        style = TextStyle(
            fontSize = 16.sp,
            fontSynthesis = FontSynthesis.Style,
            textAlign = TextAlign.Justify,
            hyphens = Hyphens.Auto,
            fontFamily = sourceCodeFamily,
            textIndent = TextIndent(firstLine = 16.sp),
            lineBreak = LineBreak.Paragraph,
            lineHeight = 16.sp

        ),

        )

}

@Composable
private fun getCopyRightText(color: Color): AnnotatedString {
    return buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = color,
            )
        ) {
            append(stringResource(R.string.copyright_part_first))
            pushStringAnnotation(
                "rick_and_morty",
                annotation = "https://www.adultswim.com/videos/rick-and-morty"
            )
            // TODO change color to when color palette created.
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.Magenta
                )
            ) {
                append("Rick And Morty")
            }
            pop()

            append(stringResource(R.string.copyright_part_second))
            pushStringAnnotation("adult_swim", annotation = "https://www.adultswim.com")
            // TODO change color to when color palette created.
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.Magenta
                )
            ) {
                append("Adult Swim")
            }
            pop()
            append(stringResource(R.string.copyright_part_third))

            pushStringAnnotation("adult_swim", annotation = "https://www.adultswim.com")
            // TODO change color to when color palette created.
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.Magenta
                )
            ) {
                append("Adult Swim")
            }
            pop()

            append(stringResource(R.string.copyright_part_fourth))
            pushStringAnnotation("email", annotation = "mertfatihsimsek06@gmail.com")
            // TODO change color to when color palette created.
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.Magenta
                )
            ) {
                append(" email")
            }
            pop()
            append(stringResource(R.string.copyright_part_fifth))
            pushStringAnnotation(
                "play_developer",
                annotation = "https://play.google.com/store/apps/developer?id=wenubey"
            )
            // TODO change color to when color palette created.
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.Magenta
                )
            ) {
                append("wenubey")
            }
            pop()
            append(stringResource(id = R.string.copyright_part_sixth))
            append(stringResource(id = R.string.copyright_part_seventh))
        }

    }
}