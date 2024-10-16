package com.example.flightsearch.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.flightsearch.R
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Favorite

@Composable
fun FlightSearchView(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(dimensionResource(id = R.dimen.dp_0)),
    searchText: String,
    onSearchText: (String) -> Unit,
    searchOk: Boolean,
    onSearchOk: (Boolean) -> Unit,
    saveFavorite: (Favorite) -> Unit,
    deleteFavorite: (Favorite) -> Unit,
    suggestionItem: (String) -> Unit,
    listAirport: List<Airport>,
    listFavorite: List<Favorite>
){
    Column(
        modifier = modifier.padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        TextField(
            value = searchText,
            onValueChange = { onSearchText(it) },
            prefix = { Icon(
                imageVector = ImageVector.vectorResource(R.drawable.search_40),
                contentDescription = null
            )},
            placeholder = { Text(
                text = stringResource(R.string.test_field_search),
                style = MaterialTheme.typography.bodyLarge)
            },
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onSearchOk(true) }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(R.dimen.dp_2),
                    vertical = dimensionResource(R.dimen.dp_1)
                )
        )
        LazyColumn {
            when{
                searchOk -> {
                    val listAirportArrive = listAirport.filterNot { item ->
                        item.code == searchText ||
                        item.name == searchText
                    }
                    val airportDepart = listAirport.find { item ->
                        item.code == searchText ||
                        item.name == searchText
                    } ?: listAirport[0]
                    item {
                        Text(
                            text = stringResource(R.string.airport_title, searchText),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(
                                horizontal = dimensionResource(R.dimen.dp_2),
                                vertical = dimensionResource(R.dimen.dp_1)
                            )
                        )
                    }
                    items(listAirportArrive.size, key = {listAirportArrive[it].id}){
                        val favoriteItem = Favorite(
                            id = 0,
                            departureCode = airportDepart.code,
                            destinationCode = listAirportArrive[it].code
                        )
                        ItemAirport(
                            airportDepart = airportDepart,
                            airportArrive = listAirportArrive[it],
                            saveFavorite = { saveFavorite(favoriteItem) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                searchText.isNotEmpty() -> {
                    val listAirportSearch = listAirport.filter { item ->
                        item.code.contains(searchText) ||
                        item.name.contains(searchText)
                    }
                    items(listAirportSearch.size, key = {listAirportSearch[it].id}){
                        ItemSuggestion(
                            airport = listAirportSearch[it],
                            clickItem = { suggestionItem(listAirportSearch[it].code) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                searchText.isEmpty() -> {
                    item {
                        Text(
                            text = stringResource(R.string.favorite_title),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(
                                horizontal = dimensionResource(R.dimen.dp_2),
                                vertical = dimensionResource(R.dimen.dp_1)
                            )
                        )
                    }
                    items(listFavorite.size, key = {listFavorite[it].id}){
                        ItemFavorite(
                            favorite = listFavorite[it],
                            listAirport = listAirport,
                            deleteFavorite = { deleteFavorite(listFavorite[it]) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemSuggestion(
    airport: Airport,
    clickItem: () -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .clickable { clickItem() }
            .padding(dimensionResource(R.dimen.dp_1)),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = airport.code,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.dp_1)))
        Text(
            text = airport.name,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun ItemAirport(
    modifier: Modifier = Modifier,
    airportDepart: Airport,
    airportArrive: Airport,
    saveFavorite: () -> Unit
){
    Card(
        modifier = modifier.padding(
            horizontal = dimensionResource(R.dimen.dp_2),
            vertical = dimensionResource(R.dimen.dp_1)
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = modifier.padding(dimensionResource(R.dimen.dp_1)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(modifier = Modifier.weight(1f)) {
                Subitems(
                    title = stringResource(R.string.subitem_favorite1),
                    code = airportDepart.code,
                    name = airportDepart.name,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.dp_1)))
                Subitems(
                    title = stringResource(R.string.subitem_favorite2),
                    code = airportArrive.code,
                    name = airportArrive.name,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            IconButton(onClick = saveFavorite) {
                Icon(imageVector = ImageVector.vectorResource(
                    id = R.drawable.favorite_40),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun ItemFavorite(
    modifier: Modifier = Modifier,
    favorite: Favorite,
    listAirport: List<Airport>,
    deleteFavorite: () -> Unit
){
    val depart = listAirport.find { item ->
        item.code == favorite.departureCode
    } ?: listAirport[0]
    val destination = listAirport.find { item ->
        item.code == favorite.destinationCode
    } ?: listAirport[0]
    Card(
        modifier = modifier.padding(
            horizontal = dimensionResource(R.dimen.dp_2),
            vertical = dimensionResource(R.dimen.dp_1)
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = modifier.padding(dimensionResource(R.dimen.dp_1)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(modifier = Modifier.weight(1f)){
                Subitems(
                    title = stringResource(R.string.subitem_favorite1),
                    code = depart.code,
                    name = depart.name,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.dp_1)))
                Subitems(
                    title = stringResource(R.string.subitem_favorite2),
                    code = destination.code,
                    name = destination.name,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            IconButton(onClick = deleteFavorite) {
                Icon(imageVector = ImageVector.vectorResource(
                    id = R.drawable.favorite_40),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun Subitems(
    modifier: Modifier = Modifier,
    title: String,
    code: String,
    name: String
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = code,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.dp_1)))
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewView(){
    FlightSearchView(
        listAirport = getAirportList(),
        listFavorite = getFavoriteList(),
        modifier = Modifier.fillMaxSize(),
        searchOk = false,
        onSearchOk = {},
        searchText = "",
        onSearchText = {},
        saveFavorite = {},
        deleteFavorite = {},
        suggestionItem = {}
    )
}

private fun getAirportList(): List<Airport> = listOf(
    Airport(1, "AAA", "Airport AAA", 100),
    Airport(2, "BBB", "Airport BBB", 200),
    Airport(3, "CCC", "Airport CCC", 300)
)

private fun getFavoriteList(): List<Favorite> = listOf(
    Favorite(1, "AAA", "BBB"),
    Favorite(2, "BBB", "CCC"),
    Favorite(3, "CCC", "AAA")
)