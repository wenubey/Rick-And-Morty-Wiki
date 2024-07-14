package com.wenubey.rickandmortywiki.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.wenubey.rickandmortywiki.R

fun sendEmail(
    mailSubject: String,
    mailText: String,
    context: Context
) {
    val selectorIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse(context.getString(R.string.developer_mail_uri))
    }
    val emailIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.developer_mail)))
        putExtra(Intent.EXTRA_SUBJECT, mailSubject)
        putExtra(Intent.EXTRA_TEXT, mailText)
        selector = selectorIntent
    }

    try {
        context.startActivity(
            Intent.createChooser(
                emailIntent, context.getString(R.string.send_email)
            )
        )
    } catch (ex: ActivityNotFoundException) {
        return
    }

}

fun visitPlayStore(context: Context) {
    val marketUri = Uri.parse("market://details?id=${context.packageName}")
    val websiteUri = Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")

    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = marketUri
        setPackage(context.getString(R.string.play_market_package_name))
    }
    val websiteIntent = Intent(Intent.ACTION_VIEW, websiteUri)

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        context.startActivity(websiteIntent)
    }
}



fun openUrlInCustomTabs(context: Context,url: String) {
    val intent = CustomTabsIntent.Builder()
        .build()
    intent.launchUrl(context, Uri.parse(url))
}