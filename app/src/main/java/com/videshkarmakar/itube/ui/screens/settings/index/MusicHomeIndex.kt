package com.videshkarmakar.itube.ui.screens.settings.index

import com.videshkarmakar.itube.R
import com.videshkarmakar.itube.ui.components.music.section.MusicHomeShelf
import com.videshkarmakar.itube.ui.components.settings.SettingEntry
import com.videshkarmakar.itube.ui.components.settings.SettingsDestination

internal object MusicHomeIndex {
    private val entries: Map<MusicHomeShelf, SettingEntry> =
        MusicHomeShelf.entries.associateWith { shelf ->
            SettingEntry(
                key = "music_home.${shelf.name.lowercase()}",
                title = shelf.labelRes,
                section = R.string.settings_music_home_sections,
                destination = SettingsDestination.MUSIC_HOME,
            )
        }

    fun entry(shelf: MusicHomeShelf): SettingEntry = entries.getValue(shelf)

    val all: List<SettingEntry> = MusicHomeShelf.entries.map(::entry)
}
