package com.wenubey.rickandmortywiki.ui.components.how_to_use

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import androidx.compose.ui.unit.sp
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.theme.sourceCodeFamily
import com.wenubey.rickandmortywiki.utils.sendEmail
import com.wenubey.rickandmortywiki.utils.visitPlayStore

@Composable
fun FeedbackText() {
    val context = LocalContext.current
    val text = buildAnnotatedString {
        append(stringResource(R.string.feedback_first_part))
        pushStringAnnotation(
            stringResource(R.string.annotation_tag_feedback), annotation = stringResource(
                R.string.annotation_send_an_email
            )
        )
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold, color = Color.Magenta
            )
        ) {
            append(stringResource(R.string.feedback_second_part))
        }
        pop()
        append(stringResource(R.string.feedback_third_part))
        pushStringAnnotation(
            stringResource(R.string.annotation_tag_feedback), annotation = stringResource(
                R.string.annotation_visit_our_play_store_page
            )
        )
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold, color = Color.Magenta
            )
        ) {
            append(stringResource(R.string.feedback_fourth_part))
        }
        pop()
    }

    ClickableText(
        text = text,
        style = TextStyle(
            fontSize = 16.sp,
            fontSynthesis = FontSynthesis.Style,
            textAlign = TextAlign.Center,
            hyphens = Hyphens.Auto,
            fontFamily = sourceCodeFamily,
            textIndent = TextIndent(firstLine = 16.sp),
            lineBreak = LineBreak.Paragraph,
            lineHeight = 16.sp,
            color = MaterialTheme.colorScheme.primary

        ),
    ) { offset ->
        text.getStringAnnotations(
            tag = context.getString(R.string.annotation_tag_feedback), start = offset, end = offset
        ).firstOrNull()?.let { annotation ->
            when (annotation.item) {
                context.getString(R.string.annotation_send_an_email) -> sendEmail(
                    mailSubject = context.getString(R.string.feedback_mail_subject),
                    mailText = context.getString(R.string.feedback_mail_text),
                    context
                )

                context.getString(R.string.annotation_visit_our_play_store_page) -> {
                    visitPlayStore(context)
                }
            }
        }
    }
}

