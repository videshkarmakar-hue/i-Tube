package com.videshkarmakar.itube.ui.screens.settings.index

import com.videshkarmakar.itube.R
import com.videshkarmakar.itube.ui.components.settings.SettingEntry
import com.videshkarmakar.itube.ui.components.settings.SettingsDestination
import com.videshkarmakar.itube.widget.core.FlowWidgetEntry
import com.videshkarmakar.itube.widget.core.FlowWidgets

internal object WidgetsIndex {
    fun entryFor(widget: FlowWidgetEntry) =
        SettingEntry(
            key = "widgets.${widget.id}",
            title = widget.label,
            summary = widget.description,
            section = R.string.settings_widgets_add_header,
            keywords = R.string.settings_keywords_widgets,
            destination = SettingsDestination.WIDGETS,
        )

    val all = FlowWidgets.catalog.map(::entryFor)
}
