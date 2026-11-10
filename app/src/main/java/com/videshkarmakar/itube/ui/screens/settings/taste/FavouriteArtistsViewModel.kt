package com.videshkarmakar.itube.ui.screens.settings.taste

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.videshkarmakar.itube.data.recommendation.music.FavouriteArtistCatalog
import com.videshkarmakar.itube.data.recommendation.music.FavouriteArtistPicker
import com.videshkarmakar.itube.data.recommendation.music.FavouriteArtistsStore
import javax.inject.Inject

@HiltViewModel
internal class FavouriteArtistsViewModel
    @Inject
    constructor(
        store: FavouriteArtistsStore,
        catalog: FavouriteArtistCatalog,
    ) : ViewModel() {
        val picker = FavouriteArtistPicker(viewModelScope, store, catalog)
    }
