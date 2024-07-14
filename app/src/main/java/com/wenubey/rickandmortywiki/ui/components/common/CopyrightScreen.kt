package com.wenubey.rickandmortywiki.ui.components.common

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
import androidx.compose.ui.Alignment
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
import com.wenubey.rickandmortywiki.utils.openUrlInCustomTabs
import com.wenubey.rickandmortywiki.utils.sendEmail
import com.wenubey.rickandmortywiki.utils.visitPlayStore


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
                    modifier = Modifier.align(Alignment.CenterHorizontally),
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
            text.getStringAnnotations(
                tag = context.getString(R.string.annotation_tag_rick_and_morty),
                start = offset,
                end = offset
            )
                .firstOrNull()?.let { link ->
                    openUrlInCustomTabs(context, link.item)
                }
            text.getStringAnnotations(
                tag = context.getString(R.string.annotation_tag_adult_swim),
                start = offset,
                end = offset
            )
                .firstOrNull()
                ?.let { link ->
                    openUrlInCustomTabs(context, link.item)
                }
            text.getStringAnnotations(
                tag = context.getString(R.string.annotation_tag_email),
                start = offset,
                end = offset
            ).firstOrNull()
                ?.let {
                    sendEmail(
                        mailSubject = context.getString(R.string.copyright_mail_subject),
                        mailText = context.getString(R.string.feedback_mail_text),
                        context
                    )
                }
            text.getStringAnnotations(
                tag = context.getString(R.string.annotation_tag_play_developer),
                start = offset,
                end = offset
            )
                .firstOrNull()?.let {
                    visitPlayStore(context)
                }
        },
        style = TextStyle(
            fontSize = 24.sp,
            fontSynthesis = FontSynthesis.Style,
            textAlign = TextAlign.Justify,
            hyphens = Hyphens.Auto,
            fontFamily = sourceCodeFamily,
            textIndent = TextIndent(firstLine = 16.sp),
            lineBreak = LineBreak.Paragraph,
            lineHeight = 24.sp

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
                stringResource(id = R.string.annotation_tag_rick_and_morty),
                annotation = stringResource(R.string.rick_and_mort_website_link)
            )
            // TODO change color to when color palette created.
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.Magenta
                )
            ) {
                append(stringResource(R.string.copyright_rick_and_morty))
            }
            pop()

            append(stringResource(R.string.copyright_part_second))
            pushStringAnnotation(
                stringResource(id = R.string.annotation_tag_adult_swim),
                annotation = stringResource(id = R.string.adult_swim_link)
            )
            // TODO change color to when color palette created.
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.Magenta
                )
            ) {
                append(stringResource(R.string.copyright_adult_swim))
            }
            pop()
            append(stringResource(R.string.copyright_part_third))

            pushStringAnnotation(
                stringResource(id = R.string.annotation_tag_adult_swim),
                annotation = stringResource(id = R.string.adult_swim_link)
            )
            // TODO change color to when color palette created.
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.Magenta
                )
            ) {
                append(stringResource(R.string.copyright_adult_swim))
            }
            pop()

            append(stringResource(R.string.copyright_part_fourth))
            pushStringAnnotation(
                stringResource(id = R.string.annotation_tag_email),
                annotation = stringResource(R.string.developer_mail)
            )
            // TODO change color to when color palette created.
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.Magenta
                )
            ) {
                append(stringResource(R.string.copyright_email))
            }
            pop()
            append(stringResource(R.string.copyright_part_fifth))
            pushStringAnnotation(
                stringResource(id = R.string.annotation_tag_play_developer),
                annotation = stringResource(R.string.google_player_developer_link)
            )
            // TODO change color to when color palette created.
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.Magenta
                )
            ) {
                append(stringResource(R.string.copyright_developer_name))
            }
            pop()
            append(stringResource(id = R.string.copyright_part_sixth))
            append(stringResource(id = R.string.copyright_part_seventh))
        }

    }
}