package com.videshkarmakar.itube.ui.screens.settings.appearance

import dagger.hilt.android.lifecycle.HiltViewModel
import com.videshkarmakar.itube.data.local.PlayerPreferences
import com.videshkarmakar.itube.ui.screens.settings.SettingsViewModel
import com.videshkarmakar.itube.utils.DateContextMode
import com.videshkarmakar.itube.utils.DateDisplayMode
import com.videshkarmakar.itube.utils.DateFormatStyle
import javax.inject.Inject

@HiltViewModel
class DateTimeViewModel
    @Inject
    constructor(
        private val preferences: PlayerPreferences,
    ) : SettingsViewModel() {
        val mode = preferences.dateDisplayMode.asState(DateDisplayMode.RELATIVE)
        val formatStyle = preferences.dateFormatStyle.asState(DateFormatStyle.SYSTEM)
        val listsMode = preferences.dateModeLists.asState(DateContextMode.DEFAULT)
        val watchMode = preferences.dateModeWatch.asState(DateContextMode.DEFAULT)
        val descriptionMode = preferences.dateModeDescription.asState(DateContextMode.DEFAULT)

        fun setMode(value: DateDisplayMode) = write { preferences.setDateDisplayMode(value) }

        fun setFormatStyle(value: DateFormatStyle) = write { preferences.setDateFormatStyle(value) }

        fun setListsMode(value: DateContextMode) = write { preferences.setDateModeLists(value) }

        fun setWatchMode(value: DateContextMode) = write { preferences.setDateModeWatch(value) }

        fun setDescriptionMode(value: DateContextMode) = write { preferences.setDateModeDescription(value) }
    }
