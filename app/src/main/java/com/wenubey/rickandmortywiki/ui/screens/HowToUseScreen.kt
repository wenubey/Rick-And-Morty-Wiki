package com.wenubey.rickandmortywiki.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wenubey.rickandmortywiki.R
import com.wenubey.rickandmortywiki.ui.theme.sourceCodeFamily
import com.wenubey.rickandmortywiki.ui.viewmodels.settings.VideoPlayer
import com.wenubey.rickandmortywiki.utils.sendEmail
import com.wenubey.rickandmortywiki.utils.visitPlayStore


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HowToUseScreen(
    onNavigateBack: () -> Unit,
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = {
                Row {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(
                                id = R.string.cd_navigate_back
                            )
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            },
        )
    }) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            // What does double click
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    VideoPlayer(videoResource = R.raw.how_to_use)
                    Text(
                        text = stringResource(R.string.how_to_use_part_one),
                        style = MaterialTheme.typography.bodyMedium.copy(lineBreak = LineBreak.Heading),
                    )
                }
                HorizontalDivider(
                    color = Color.Magenta, modifier = Modifier.padding(vertical = 8.dp)
                )
                FeedbackText()
            }
        }

    }
}

@Composable
private fun FeedbackText() {
    val context = LocalContext.current
    val text = buildAnnotatedString {
        append(stringResource(R.string.feedback_first_part))
        pushStringAnnotation("Feedback", annotation = "Send an email")
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold, color = Color.Magenta
            )
        ) {
            append(stringResource(R.string.feedback_second_part))
        }
        pop()
        append(stringResource(R.string.feedback_third_part))
        pushStringAnnotation("Feedback", annotation = "Visit our Play Store page")
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
            tag = "Feedback", start = offset, end = offset
        ).firstOrNull()?.let { annotation ->
            when (annotation.item) {
                "Send an email" -> sendEmail(
                    mailSubject = context.getString(R.string.feedback_mail_subject),
                    mailText = context.getString(R.string.feedback_mail_text),
                    context
                )
                "Visit our Play Store page" -> visitPlayStore(context)
            }
        }
    }
}


