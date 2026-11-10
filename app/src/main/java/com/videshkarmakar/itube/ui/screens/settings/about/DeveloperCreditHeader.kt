package com.videshkarmakar.itube.ui.screens.settings.about

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.videshkarmakar.itube.R

private val CreditLogoSize = 56.dp

/** A compact developer-credit header shown at the top of the Settings section. */
@Composable
internal fun DeveloperCreditHeader() {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val open = { url: String -> runCatching { uriHandler.openUri(url) }.getOrElse { } }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = painterResource(R.drawable.ic_developer_credit),
            contentDescription = null,
            modifier = Modifier.size(CreditLogoSize),
            contentScale = ContentScale.Crop,
        )
        Text(
            text = stringResource(R.string.developer_credit_header),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        SocialLinksRow(open)
    }
}

/** Short social-link chips on a single row. */
@Composable
private fun SocialLinksRow(open: (String) -> Unit) {
    val links = listOf(
        Pair(stringResource(R.string.developer_credit_website_label), "https://videshkarmakar.carrd.co/"),
        Pair(stringResource(R.string.developer_credit_spotify_label), "https://open.spotify.com/artist/2eJEzi0mzJB60zVJf0OjWP"),
        Pair(stringResource(R.string.developer_credit_instagram_label), "https://www.instagram.com/code.red_videsh/"),
        Pair(stringResource(R.string.developer_credit_youtube_label), "https://www.youtube.com/@videshkarmakar2000"),
        Pair(stringResource(R.string.developer_credit_facebook_label), "https://www.facebook.com/gfx.videsh"),
    )

    Row(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .wrapContentSize(),
    ) {
        links.forEachIndexed { index, (label, url) ->
            if (index > 0) {
                Text(
                    text = " · ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { open(url) },
            )
        }
    }
}
