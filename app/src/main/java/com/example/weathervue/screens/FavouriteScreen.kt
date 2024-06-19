package com.example.weathervue.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.weathervue.R
import com.example.weathervue.data.DataStoreKey
import com.example.weathervue.models.favouriteModel.Favourite
import com.example.weathervue.navigation.WeatherScreens
import com.example.weathervue.utils.Colors
import com.example.weathervue.viewModel.FavouriteViewModel
import com.example.weathervue.viewModel.HomeScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FavouriteScreen(
    modifier: Modifier = Modifier,
    favouriteViewModel: FavouriteViewModel = hiltViewModel(),
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel(),
    pagerState: PagerState,
    navController: NavHostController,
    ds: DataStoreKey,
    currentUnit: String?,
    //brush: ShaderBrush
) {
    // scroll to page
    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    val isShowBottomSheet = remember { mutableStateOf(false) }

    val favouriteList = favouriteViewModel.favList.collectAsState().value.reversed()

    Log.d("FavouriteScreen", "FavouriteScreen: $favouriteList")

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Colors.gradientColors)
                .systemBarsPadding(),
            verticalArrangement = Arrangement.Top,
        ) {

            TopBarOfFavouriteScreen(
                pagerState,
                coroutineScope,
                navController
            ) {
                isShowBottomSheet.value = true
            }

            LazyColumn(
                state = rememberLazyListState(),
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(start = 15.dp, end = 15.dp)
            ) {
                itemsIndexed(
                    items = favouriteList,
                    key = { _, listItem ->
                        listItem.hashCode()
                    }
                ) { index, favourite ->

                    SwipeBox(
                        onDelete = {
                            favouriteViewModel.deleteCity(favourite = favourite)
                        }
                    ) {
                        FavouriteCityRow(
                            modifier = Modifier.animateItemPlacement(tween(durationMillis = 200)),
                            favourite = favourite,
                        ) { city ->
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(0)
                                homeScreenViewModel.getWeatherData(city, currentUnit.toString())
                                ds.saveToCityStore("city", city)
                            }
                        }
                    }
                }
            }
        }
    }

    ModalBottomSheetMenu(isShowBottomSheet, interactionSource, coroutineScope, currentUnit, ds)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeBox(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {},
    content: @Composable () -> Unit
) {

    val swipeState = rememberSwipeToDismissBoxState()

    lateinit var icon: ImageVector
    lateinit var alignment: Alignment
    val color: Color

    when (swipeState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> {
            icon = Icons.Outlined.Delete
            alignment = Alignment.CenterStart
            color = MaterialTheme.colorScheme.background.copy(alpha = 0f)
        }

        SwipeToDismissBoxValue.EndToStart -> {
            icon = Icons.Outlined.Delete
            alignment = Alignment.CenterEnd
            color = MaterialTheme.colorScheme.background.copy(alpha = 0f)
        }

        SwipeToDismissBoxValue.Settled -> {
            icon = Icons.Outlined.Delete
            alignment = Alignment.CenterEnd
            color = MaterialTheme.colorScheme.background.copy(alpha = 0f)
        }
    }

    SwipeToDismissBox(
        modifier = modifier.animateContentSize(),
        enableDismissFromStartToEnd = false,
        state = swipeState,
        backgroundContent = {
            Box(
                contentAlignment = alignment,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
            ) {
                Icon(
                    modifier = Modifier.minimumInteractiveComponentSize(),
                    imageVector = icon, contentDescription = null,
                    tint = if (swipeState.dismissDirection == SwipeToDismissBoxValue.Settled || swipeState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) Color.Transparent else MaterialTheme.colorScheme.onBackground
                )
            }
        }
    ) {
        content()
    }

    when (swipeState.currentValue) {
        SwipeToDismissBoxValue.StartToEnd -> {}

        SwipeToDismissBoxValue.EndToStart -> {
            onDelete()
        }

        SwipeToDismissBoxValue.Settled -> {}
    }
}


@Composable
fun FavouriteCityRow(
    modifier: Modifier = Modifier,
    favourite: Favourite,
    onSearch: (String) -> Unit = {},
) {


    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onSearch(favourite.city)
            }
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.15f)),
    ) {
        Text(
            text = favourite.city + ", " + favourite.country,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .padding(start = 10.dp)
                .align(Alignment.CenterStart),
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ModalBottomSheetMenu(
    isShowBottomSheet: MutableState<Boolean>,
    interactionSource: MutableInteractionSource,
    coroutineScope: CoroutineScope,
    currentUnit: String?,
    ds: DataStoreKey
) {
    AnimatedVisibility(visible = isShowBottomSheet.value) {
        ModalBottomSheet(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .systemBarsPadding(),
            shape = RoundedCornerShape(20.dp),
            onDismissRequest = {
                isShowBottomSheet.value = false
            },
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.2f),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        isShowBottomSheet.value = false
                        coroutineScope.launch {
                            if (currentUnit.toString() == "metric") {
                                ds.saveToDataStore("units", "imperial")
                            } else {
                                ds.saveToDataStore("units", "metric")
                            }
                        }
                    },
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "Celsius(°C)",
                        color = if (currentUnit.toString() == "metric") Color.Green
                        else MaterialTheme.colorScheme.onBackground
                    )
                    if (currentUnit.toString() == "metric") {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowUp, contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "Fahrenheit(°F)",
                        color = if (currentUnit.toString() == "imperial") Color.Green
                        else MaterialTheme.colorScheme.onBackground
                    )
                    if (currentUnit.toString() == "imperial") {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowUp, contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopBarOfFavouriteScreen(
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    onChangerUnits: () -> Unit = {}
) {

    val showDropDownMenu = remember {
        mutableStateOf(false)
    }

    if (showDropDownMenu.value) {
        SettingDropDownMenu(
            showDropDownMenu = showDropDownMenu,
            navController = navController
        ) {
            onChangerUnits()
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        pagerState.animateScrollToPage(0)
                    }
                }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
            Text(
                text = "Favourites",
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
        }

        IconButton(
            modifier = Modifier.padding(end = 10.dp),
            onClick = {
                showDropDownMenu.value = true
            }) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = null
            )

        }

    }
}

@Composable
fun SettingDropDownMenu(
    showDropDownMenu: MutableState<Boolean>,
    navController: NavHostController,
    onChangeUnits: () -> Unit = {},
) {

    var expanded by remember {
        mutableStateOf(true)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = (-25).dp, y = 25.dp)
        //.wrapContentSize(align = Alignment.TopEnd)
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                showDropDownMenu.value = false
            },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.2f))
                .background(brush = Colors.menuBackgroundBrush)
        ) {

            DropdownMenuItem(text = {
                Row {
                    Icon(
                        modifier = Modifier
                            .size(22.dp),
                        painter = painterResource(id = R.drawable.change_unit),
                        contentDescription = null
                    )
                    Text(text = "   Change Units")
                }
            }, onClick = {
                onChangeUnits()
                expanded = false
                showDropDownMenu.value = false
            })

            DropdownMenuItem(text = {
                Row {
                    Icon(imageVector = Icons.Outlined.Info, contentDescription = null)
                    Text(text = "   About")
                }
            }, onClick = {
                expanded = false
                showDropDownMenu.value = false
                navController.navigate(WeatherScreens.AboutScreen.name)
            })

        }
    }
}
