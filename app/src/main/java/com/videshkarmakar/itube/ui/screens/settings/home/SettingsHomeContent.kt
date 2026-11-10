package com.videshkarmakar.itube.ui.screens.settings.home

import androidx.compose.runtime.Immutable
import androidx.compose.ui.res.stringResource
import com.videshkarmakar.itube.BuildConfig
import com.videshkarmakar.itube.R
import com.videshkarmakar.itube.data.recommendation.FlowPersona
import com.videshkarmakar.itube.ui.components.settings.SettingsDestination
import com.videshkarmakar.itube.ui.components.settings.SettingsGroupScope
import com.videshkarmakar.itube.ui.components.settings.SettingsListScope
import com.videshkarmakar.itube.ui.components.settings.SettingsTarget
import com.videshkarmakar.itube.ui.components.settings.nav
import com.videshkarmakar.itube.ui.components.settings.switch
import com.videshkarmakar.itube.ui.components.shared.DeepFlowState
import com.videshkarmakar.itube.ui.components.shared.FlowNavRow
import com.videshkarmakar.itube.ui.components.shared.FlowSwitchRow
import com.videshkarmakar.itube.ui.components.shared.deepFlowStatus
import com.videshkarmakar.itube.ui.screens.settings.index.DestinationIndex
import com.videshkarmakar.itube.ui.screens.settings.index.HomeIndex
import com.videshkarmakar.itube.ui.screens.settings.about.DeveloperCreditHeader

@Immutable
internal data class SettingsHomeState(
    val selected: SettingsDestination?,
    val persona: FlowPersona?,
    val deepFlow: DeepFlowState,
)

internal class SettingsHomeActions(
    val onOpen: (SettingsTarget) -> Unit,
    val onDeepFlowChange: (Boolean) -> Unit,
    val onDurationClick: () -> Unit,
    val onSaveHistoryChange: (Boolean) -> Unit,
    val onScrobbleChange: (Boolean) -> Unit,
    val onShowOnSearchChange: (Boolean) -> Unit,
)

/** The settings list when no search is running. */
internal fun SettingsListScope.homeContent(
    state: SettingsHomeState,
    actions: SettingsHomeActions,
) {
    item("settings.developer_credit") {
        DeveloperCreditHeader()
    }

    item(HomeIndex.persona.key) {
        PersonaEntryCard(
            persona = state.persona,
            selected = state.selected == SettingsDestination.TASTE,
            onOpen = { actions.onOpen(SettingsTarget(SettingsDestination.TASTE)) },
        )
    }

    fun SettingsGroupScope.page(destination: SettingsDestination) =
        nav(
            entry = DestinationIndex.entry(destination),
            onClick = { actions.onOpen(SettingsTarget(destination)) },
            icon = destinationIcon(destination),
            selected = state.selected == destination,
            showChevron = state.selected == null,
        )

    group(key = "home.engine", header = R.string.settings_flow_engine_header) {
        row(HomeIndex.deepFlow.key) { shape ->
            FlowSwitchRow(
                title = stringResource(HomeIndex.deepFlow.title),
                supportingText = deepFlowStatus(state.deepFlow) ?: stringResource(R.string.deep_flow_mode_subtitle),
                checked = state.deepFlow.active,
                onCheckedChange = actions.onDeepFlowChange,
                leadingIcon = HomeRowIcons.DeepFlow,
                shape = shape,
            )
        }
        row(HomeIndex.deepFlowDuration.key) { shape ->
            FlowNavRow(
                title = stringResource(HomeIndex.deepFlowDuration.title),
                supportingText = deepFlowDurationLabel(state.deepFlow.expireHours),
                onClick = actions.onDurationClick,
                leadingIcon = HomeRowIcons.DeepFlowDuration,
                shape = shape,
            )
        }
        switch(
            HomeIndex.deepFlowHistory,
            checked = state.deepFlow.saveToHistory,
            onCheckedChange = actions.onSaveHistoryChange,
            icon = HomeRowIcons.DeepFlowHistory,
        )
        switch(
            HomeIndex.deepFlowScrobble,
            checked = state.deepFlow.scrobble,
            onCheckedChange = actions.onScrobbleChange,
            icon = HomeRowIcons.DeepFlowScrobble,
        )
        switch(
            HomeIndex.deepFlowOnSearch,
            checked = state.deepFlow.showOnSearch,
            onCheckedChange = actions.onShowOnSearchChange,
            icon = HomeRowIcons.DeepFlowOnSearch,
        )
        page(SettingsDestination.TOPICS)
    }
    group(key = "home.look", header = R.string.settings_group_look) {
        page(SettingsDestination.APPEARANCE)
        page(SettingsDestination.LANGUAGE_REGION)
        page(SettingsDestination.WIDGETS)
    }
    group(key = "home.watching", header = R.string.settings_group_watching) {
        page(SettingsDestination.PLAYBACK)
        page(SettingsDestination.QUALITY)
        page(SettingsDestination.CONTENT)
        page(SettingsDestination.INTEGRATIONS)
    }
    group(key = "home.data", header = R.string.settings_group_data) {
        page(SettingsDestination.BACKUP)
        page(SettingsDestination.SYNC)
        page(SettingsDestination.HISTORY)
        page(SettingsDestination.DOWNLOADS)
    }
    group(key = "home.system", header = R.string.settings_group_system) {
        page(SettingsDestination.NOTIFICATIONS)
        page(SettingsDestination.NETWORK)
        page(SettingsDestination.WELLBEING)
    }
    group(key = "home.about", header = R.string.settings_item_about_flow) {
        page(SettingsDestination.ABOUT)
    }
    group(key = "home.developer_credits", header = R.string.settings_item_developer_credits) {
        page(SettingsDestination.DEVELOPER_CREDITS)
    }
}
