package com.videshkarmakar.itube.ui.screens.settings.about

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.videshkarmakar.itube.ui.components.settings.SettingsPage

private val LogoSize = 80.dp
private val HeaderPadding = 24.dp
private val HeaderSpacing = 8.dp

/** Developer credits page: developer bio, contact links, and build details. */
@Composable
internal fun DeveloperCreditScreen(
    onBack: (() -> Unit)?,
    highlight: String?,
) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    val open = { url: String -> runCatching { uriHandler.openUri(url) } }

    SettingsPage(
        title = stringResource(R.string.settings_item_developer_credits),
        onBack = onBack,
        highlight = highlight,
    ) {
        item("developer.header") { DeveloperHeader() }

        group(key = "developer.bio", header = null) {
            row("developer.bio.one") { shape ->
                SectionText(text = stringResource(R.string.developer_credits_bio_one))
            }
            row("developer.bio.two") { shape ->
                SectionText(text = stringResource(R.string.developer_credits_bio_two))
            }
            row("developer.bio.three") { shape ->
                SectionText(text = stringResource(R.string.developer_credits_bio_three))
            }
        }

        group(key = "developer.connect", header = R.string.developer_credits_connect_header) {
            row("developer.connect.desc") { shape ->
                SectionText(text = stringResource(R.string.developer_credits_connect_desc))
            }
            row("developer.website") { shape ->
                LinkRow(
                    shape = shape,
                    titleRes = R.string.developer_credits_website_label,
                    value = stringResource(R.string.developer_credits_website_value),
                    onClick = { open("https://videshkarmakar.carrd.co/") },
                )
            }
            row("developer.spotify") { shape ->
                LinkRow(
                    shape = shape,
                    titleRes = R.string.developer_credits_spotify_label,
                    value = stringResource(R.string.developer_credits_spotify_value),
                    onClick = { open("https://open.spotify.com/artist/2eJEzi0mzJB60zVJf0OjWP") },
                )
            }
            row("developer.instagram") { shape ->
                LinkRow(
                    shape = shape,
                    titleRes = R.string.developer_credits_instagram_label,
                    value = stringResource(R.string.developer_credits_instagram_value),
                    onClick = { open("https://www.instagram.com/code.red_videsh/") },
                )
            }
            row("developer.youtube") { shape ->
                LinkRow(
                    shape = shape,
                    titleRes = R.string.developer_credits_youtube_label,
                    value = stringResource(R.string.developer_credits_youtube_value),
                    onClick = { open("https://www.youtube.com/@videshkarmakar2000") },
                )
            }
            row("developer.facebook") { shape ->
                LinkRow(
                    shape = shape,
                    titleRes = R.string.developer_credits_facebook_label,
                    value = stringResource(R.string.developer_credits_facebook_value),
                    onClick = { open("https://www.facebook.com/gfx.videsh") },
                )
            }
        }

        group(key = "developer.build", header = R.string.developer_credits_dev_header) {
            row("developer.product") { shape ->
                InfoRow(shape = shape, titleRes = R.string.developer_credits_product_label, value = stringResource(R.string.developer_credits_product_value))
            }
            row("developer.version") { shape ->
                InfoRow(shape = shape, titleRes = R.string.developer_credits_version_label, value = stringResource(R.string.developer_credits_version_value))
            }
            row("developer.developed") { shape ->
                InfoRow(shape = shape, titleRes = R.string.developer_credits_developed_label, value = stringResource(R.string.developer_credits_developed_value))
            }
            row("developer.design") { shape ->
                InfoRow(shape = shape, titleRes = R.string.developer_credits_design_label, value = stringResource(R.string.developer_credits_design_value))
            }
            row("developer.platform") { shape ->
                InfoRow(shape = shape, titleRes = R.string.developer_credits_platform_label, value = stringResource(R.string.developer_credits_platform_value))
            }
        }

        group(key = "developer.ack", header = R.string.developer_credits_ack_header) {
            row("developer.ack.one") { shape ->
                SectionText(text = stringResource(R.string.developer_credits_ack_one))
            }
            row("developer.ack.two") { shape ->
                SectionText(text = stringResource(R.string.developer_credits_ack_two))
            }
            row("developer.ack.three") { shape ->
                SectionText(text = stringResource(R.string.developer_credits_ack_three))
            }
        }

        item("developer.footer") {
            DeveloperFooter()
        }
    }
}

@Composable
private fun DeveloperHeader() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = HeaderPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(HeaderSpacing),
    ) {
        AsyncImage(
            model = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.size(LogoSize),
            contentScale = ContentScale.Crop,
        )
        Text(
            text = stringResource(R.string.developer_credits_developer_name),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = stringResource(R.string.developer_credits_developer_role),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(R.string.developer_credits_tagline),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun InfoRow(
    shape: androidx.compose.ui.graphics.Shape,
    titleRes: Int,
    value: String,
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(titleRes),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun SectionText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
    )
}

@Composable
private fun LinkRow(
    shape: androidx.compose.ui.graphics.Shape,
    titleRes: Int,
    value: String,
    onClick: () -> Unit,
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
        )
    }
}

@Composable
private fun DeveloperFooter() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.developer_credits_copyright_footer),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
