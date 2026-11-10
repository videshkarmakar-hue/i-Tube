package com.videshkarmakar.itube.ui.screens.settings.appearance

import android.net.Uri
import dagger.hilt.android.lifecycle.HiltViewModel
import com.videshkarmakar.itube.data.local.AppFontPreferences
import com.videshkarmakar.itube.data.local.AppFontSelection
import com.videshkarmakar.itube.data.local.FontImportResult
import com.videshkarmakar.itube.ui.screens.settings.SettingsViewModel
import com.videshkarmakar.itube.ui.theme.AppFont
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FontViewModel
    @Inject
    constructor(
        private val preferences: AppFontPreferences,
    ) : SettingsViewModel() {
        val selection = preferences.selection.asState(AppFontSelection())

        private val _importResult = MutableStateFlow<FontImportResult?>(null)

        /** What the last file pick did, until the page has said so. */
        val importResult: StateFlow<FontImportResult?> = _importResult.asStateFlow()

        fun select(font: AppFont) = write { preferences.setFont(font) }

        fun import(uri: Uri) =
            write {
                val result = preferences.customFonts.import(uri)
                if (result is FontImportResult.Imported) preferences.setCustomFont(result.displayName)
                _importResult.value = result
            }

        fun consumeImportResult() {
            _importResult.value = null
        }
    }
