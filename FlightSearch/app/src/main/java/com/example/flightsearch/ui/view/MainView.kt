package com.example.flightsearch.ui.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.flightsearch.ui.viewmodel.ViewModelFlightSearch
import com.example.flightsearch.ui.viewmodel.ViewModelsProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.R

@Composable
fun MainView(
    vm: ViewModelFlightSearch = viewModel(factory = ViewModelsProvider.factory)
){
    val listAirport by vm.getAllAirport().collectAsState(emptyList())
    val listFavorite by vm.getAllFavorite().collectAsState(emptyList())
    val uiState by vm.uiState.collectAsState()
    val saveText by vm.saveText.collectAsState()
    if(saveText.isNotEmpty()){
        vm.setSearchText(saveText)
    }
    Scaffold(topBar = { TopBar() }) { paddingValues ->
        FlightSearchView(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues,
            listAirport = listAirport,
            listFavorite = listFavorite,
            searchText = uiState.searchText,
            onSearchText = { text ->
                vm.setSearchText(text)
                if(uiState.searchText.isEmpty()) vm.setSearchOk(false)
            },
            searchOk = uiState.searchOk,
            onSearchOk = { ok ->
                vm.saveSearchText(uiState.searchText)
                vm.setSearchOk(ok)
            },
            saveFavorite = { itemSave -> vm.insertFavoriteRoute(itemSave) },
            deleteFavorite = { itemDelete -> vm.deleteFavoriteRoute(itemDelete) },
            suggestionItem = { codeItem ->
                vm.setSearchText(codeItem)
                vm.saveSearchText(uiState.searchText)
                vm.setSearchOk(true)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(){
    TopAppBar(title = { stringResource(id = R.string.app_name) })
}