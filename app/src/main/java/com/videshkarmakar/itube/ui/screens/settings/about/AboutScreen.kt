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
import com.videshkarmakar.itube.ui.components.settings.nav

private enum class AboutDialog { NOTICES }

private const val CREATOR_URL = "https://github.com/A-EDev"

private val LogoSize = 80.dp
private val HeaderPadding = 24.dp
private val HeaderSpacing = 8.dp

/** The app's about page: brand identity, app information, legal links, and build details. */
@Composable
internal fun AboutScreen(
    onBack: (() -> Unit)?,
    highlight: String?,
) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    var dialog by rememberSaveable { mutableStateOf<AboutDialog?>(null) }
    val open = { url: String -> runCatching { uriHandler.openUri(url) } }

    val version = remember {
        runCatching { context.packageManager.getPackageInfo(context.packageName, 0) }
            .getOrNull()
            ?.let { it.versionName to it.longVersionCode.toString() }
    }

    SettingsPage(
        title = stringResource(R.string.app_name),
        onBack = onBack,
        highlight = highlight,
    ) {
        item("about.header") { AboutHeader(version) }

        group(key = "about.app_info", header = R.string.about_app_info_header) {
            row("about.app") { shape ->
                InfoRow(shape = shape, titleRes = R.string.about_app_label, value = stringResource(R.string.about_app_value))
            }
            row("about.version") { shape ->
                InfoRow(shape = shape, titleRes = R.string.about_version_label, value = version?.first ?: stringResource(R.string.unknown))
            }
            row("about.platform") { shape ->
                InfoRow(shape = shape, titleRes = R.string.about_platform_label, value = stringResource(R.string.about_platform_value))
            }
            row("about.developer") { shape ->
                InfoRow(shape = shape, titleRes = R.string.about_developer_label, value = stringResource(R.string.about_developer_value))
            }
            row("about.uiux") { shape ->
                InfoRow(shape = shape, titleRes = R.string.about_uiux_label, value = stringResource(R.string.about_uiux_value))
            }
        }

        item("about.section.intro") {
            SectionText(text = stringResource(R.string.about_section_one))
            SectionText(text = stringResource(R.string.about_section_two))
            SectionText(text = stringResource(R.string.about_section_three))
            SectionText(text = stringResource(R.string.about_section_four))
            SectionText(text = stringResource(R.string.about_section_five))
        }

        group(key = "about.legal", header = R.string.about_legal_header) {
            row("about.privacy") { shape ->
                LinkRow(
                    shape = shape,
                    titleRes = R.string.about_privacy_label,
                    descRes = R.string.about_privacy_desc,
                    onClick = { open("https://github.com/A-EDev/privacy") },
                )
            }
            row("about.terms") { shape ->
                LinkRow(
                    shape = shape,
                    titleRes = R.string.about_terms_label,
                    descRes = R.string.about_terms_desc,
                    onClick = { open("https://github.com/A-EDev/terms") },
                )
            }
            row("about.open_source") { shape ->
                LinkRow(
                    shape = shape,
                    titleRes = R.string.about_open_source_label,
                    descRes = R.string.about_open_source_desc,
                    onClick = { dialog = AboutDialog.NOTICES },
                )
            }
            row("about.third_party") { shape ->
                LinkRow(
                    shape = shape,
                    titleRes = R.string.about_third_party_label,
                    descRes = R.string.about_third_party_desc,
                    onClick = { dialog = AboutDialog.NOTICES },
                )
            }
            row("about.contact") { shape ->
                LinkRow(
                    shape = shape,
                    titleRes = R.string.about_contact_label,
                    descRes = R.string.about_contact_desc,
                    onClick = { open(CREATOR_URL) },
                )
            }
            row("about.report") { shape ->
                LinkRow(
                    shape = shape,
                    titleRes = R.string.about_report_label,
                    descRes = R.string.about_report_desc,
                    onClick = { open(CREATOR_URL) },
                )
            }
            row("about.copyright") { shape ->
                LinkRow(
                    shape = shape,
                    titleRes = R.string.about_copyright_label,
                    descRes = R.string.about_copyright_desc,
                    onClick = { open("https://github.com/A-EDev/copyright") },
                )
            }
        }

        group(key = "about.build", header = R.string.about_build_header) {
            row("about.build.value") { shape ->
                InfoRow(shape = shape, titleRes = R.string.about_build_value, value = stringResource(R.string.about_copyright_footer))
            }
        }

        item("about.footer") {
            AboutFooter()
        }
    }

    when (dialog) {
        AboutDialog.NOTICES -> NoticesDialog(onDismiss = { dialog = null })
        null -> Unit
    }
}

@Composable
private fun AboutHeader(version: Pair<String?, String>?) {
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
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = stringResource(R.string.about_tagline),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(R.string.v_version_template, version?.first ?: "1.0.0.0", version?.second ?: "0"),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(R.string.app_tagline),
            style = MaterialTheme.typography.bodySmall,
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
    descRes: Int,
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
                text = stringResource(descRes),
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
private fun AboutFooter() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.about_copyright_footer),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(R.string.about_footer),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

