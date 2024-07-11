package com.example.weathervue.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults.flingBehavior
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.weathervue.R
import com.example.weathervue.data.DataOrException
import com.example.weathervue.data.DataStoreKey
import com.example.weathervue.data.Resource
import com.example.weathervue.models.airQuality.AirQuality
import com.example.weathervue.models.currentWeather.CurrentWeather
import com.example.weathervue.models.favouriteModel.Favourite
import com.example.weathervue.models.weatherModel.Weather
import com.example.weathervue.models.weatherModel.WeatherItem
import com.example.weathervue.utils.Colors
import com.example.weathervue.utils.ShadedBox
import com.example.weathervue.utils.checkAirQuality
import com.example.weathervue.utils.formatDate
import com.example.weathervue.utils.formatDate2
import com.example.weathervue.utils.formatDateTime
import com.example.weathervue.utils.formatDecimals
import com.example.weathervue.viewModel.FavouriteViewModel
import com.example.weathervue.viewModel.HomeScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
) {

    /*var tileMode by remember { mutableStateOf(TileMode.Mirror) }
    val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "animatedOffset"
    )

    val brush = remember(animatedOffset, tileMode) {
        object : ShaderBrush() {
            override fun createShader(size: androidx.compose.ui.geometry.Size): Shader {
                val widthOffset = size.width * animatedOffset
                val heightOffset = size.height * animatedOffset
                return LinearGradientShader(
                    colors = listOf(Color(0xC178E9F8), Color(0xC4FDFDFD), Color(0xFF1E1E1E)),
                    from = Offset(widthOffset, heightOffset),
                    to = Offset(widthOffset + size.width, heightOffset + size.height),
                    tileMode = tileMode
                )
            }
        }
    }*/

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val ds = DataStoreKey(context)
    val currentState =
        ds.getDataStoreValue("units").collectAsState(initial = "metric").value.toString()
    val currentCity =
        ds.getCityStoreValue("city").collectAsState(initial = "orai").value.toString()

    val weatherData: State<DataOrException<Weather?, Boolean, Exception>> =
        homeScreenViewModel.weatherData.collectAsState()

    val currentWeatherData: State<DataOrException<CurrentWeather?, Boolean, Exception>> =
        homeScreenViewModel.currentWeatherData.collectAsState()

    val airQuality: State<Resource<AirQuality?>> =
        homeScreenViewModel.airQuality.collectAsState()

    val pagerState = rememberPagerState(pageCount = { 2 })
    val fling = flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(2)
    )

    val isOnline = homeScreenViewModel.isOnline.collectAsState(initial = false)

    val coord = weatherData.value.data?.city?.coord
    if (coord?.lat != null) {
        homeScreenViewModel.getCurrentWeather(
            lat = coord.lat.toString(),
            lon = coord.lon.toString(),
            unit = currentState
        )
    }

    val executeFirstTime = remember { mutableStateOf(true) }

    LaunchedEffect(key1 = currentCity, key2 = currentState) {
        homeScreenViewModel.isOnline.collect { isOnline ->
            if (isOnline) {
                delay(500) // Delay after checking online status
                if (executeFirstTime.value) {
                    homeScreenViewModel.getWeatherData(currentCity, currentState)
                }
                executeFirstTime.value = false
            }
        }
    }

    HorizontalPager(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onBackground),
        state = pagerState,
        flingBehavior = fling,
        beyondBoundsPageCount = 2
    ) { page ->
        when (page) {
            0 -> {
                HomeScreenPager(
                    modifier = modifier,
                    storeCity = currentCity,
                    currentUnit = currentState,
                    currentWeatherData = currentWeatherData.value,
                    airQuality = airQuality.value.data,
                    isOnline = isOnline,
                    //brush = brush,
                    dataStore = ds,
                    weatherData = weatherData.value,
                    viewModel = homeScreenViewModel,
                    pagerState = pagerState
                )
            }

            1 -> {
                FavouriteScreen(
                    pagerState = pagerState,
                    currentUnit = currentState,
                    // brush = brush,
                    navController = navController,
                    ds = ds,
                )
            }
        }
    }

    BackHandler {
        if(pagerState.currentPage == 1){
            coroutineScope.launch {
                pagerState.animateScrollToPage(0)
            }
        }else{
            navController.popBackStack()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenPager(
    modifier: Modifier,
    storeCity: String,
    currentUnit: String,
    currentWeatherData: DataOrException<CurrentWeather?, Boolean, Exception>,
    airQuality: AirQuality?,
    weatherData: DataOrException<Weather?, Boolean, Exception>,
    viewModel: HomeScreenViewModel,
    pagerState: PagerState,
    favouriteViewModel: FavouriteViewModel = hiltViewModel(),
    dataStore: DataStoreKey,
    isOnline: State<Boolean>,
    // brush: ShaderBrush,
) {

    Scaffold {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(brush = Colors.gradientColors)
                .padding(it)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            SearchField(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                pagerState = pagerState
            ) { city ->

                Log.d("Search", city)
                CoroutineScope(Dispatchers.Default).launch {
                    withContext(Dispatchers.Main) {
                        viewModel.getWeatherData(city, currentUnit)
                    }
                }
            }

            TextTitle(title = "Today's Weather")

            AnimatedVisibility(visible = !isOnline.value || weatherData.e?.message.toString() == "Not Found" || weatherData.loading == true) {
                when {
                    !isOnline.value -> ShadedBox(text = "internet issues")
                    weatherData.e?.message.toString() == "Not Found" -> ShadedBox(text = "City not found!")
                    weatherData.loading == true -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                                .clip(RoundedCornerShape(30.dp))
                                .height(80.dp)
                                .background(MaterialTheme.colorScheme.background.copy(0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

            AnimatedVisibility(weatherData.data != null || weatherData.e?.message.toString() == "Not Found") {

                val listOfFav = favouriteViewModel.favList.collectAsState().value

                val isCityFavoriteOrNot by remember(listOfFav, weatherData.data?.city?.name) {
                    derivedStateOf {
                        listOfFav.any { cityName ->
                            cityName.city == weatherData.data?.city?.name
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    weatherData.data?.let { it1 ->
                        TodayWeatherCard(
                            currentUnit = currentUnit,
                            todayWeather = it1,
                            currentWeatherData = currentWeatherData,
                            airQuality = airQuality
                        )

                        IconButton(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 10.dp, end = 27.dp),
                            onClick = {
                                if (isCityFavoriteOrNot) {
                                    favouriteViewModel.deleteCity(
                                        Favourite(
                                            city = weatherData.data?.city?.name.toString(),
                                            country = weatherData.data?.city?.country.toString()
                                        )
                                    )
                                } else {
                                    favouriteViewModel.insertCity(
                                        Favourite(
                                            city = weatherData.data?.city?.name.toString(),
                                            country = weatherData.data?.city?.country.toString()
                                        )
                                    )
                                }
                            }) {
                            Icon(
                                imageVector = if (isCityFavoriteOrNot) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = null,
                                tint = if (isCityFavoriteOrNot) Color.Red.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onBackground.copy(
                                    alpha = 0.8f
                                )
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(visible = weatherData.data != null) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ThisWeekWeather(
                        currentUnit = currentUnit,
                        weatherData = weatherData.data
                    )

                    WeatherForecast16Days(
                        currentUnit = currentUnit,
                        data = weatherData.data
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherForecast16Days(data: Weather?, currentUnit: String) {

    val unit = if (currentUnit == "metric") "°C" else "°F"

    Column {
        TextTitle(title = "16-days Forecast")

        AnimatedVisibility(visible = data?.list?.isNotEmpty() == true) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 10.dp)
                    .height(500.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.background.copy(0.07f))
                    .padding(5.dp)
                    .clip(RoundedCornerShape(20.dp))
            ) {
                data?.let {
                    items(items = it.list) { item: WeatherItem ->
                        WeatherCardDetails(
                            unit = unit,
                            weatherItem = item
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherCardDetails(modifier: Modifier = Modifier, weatherItem: WeatherItem, unit: String) {

    val imageUrl =
        "https://openweathermap.org/img/wn/${weatherItem.weather[0].icon}.png"

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.2f))
            .padding(8.dp)
    ) {
        Column {
            Text(
                text = formatDate2(weatherItem.dt),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.labelLarge.fontSize
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Text(
                        text = formatDecimals(weatherItem.temp.day) + unit,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        style = TextStyle(
                            brush = Colors.textBrush
                        )
                    )
                    Text(
                        text = " " + weatherItem.weather[0].main,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    )
                }

                AsyncImage(
                    model = imageUrl, contentDescription = "image Weather",
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .size(45.dp)
                        .background(Color.Cyan.copy(alpha = 0.07f))
                )
            }
        }

    }

}

@Composable
fun TodayWeatherCard(
    currentUnit: String,
    todayWeather: Weather,
    currentWeatherData: DataOrException<CurrentWeather?, Boolean, Exception>,
    airQuality: AirQuality?
) {

    val imageUrl =
        "https://openweathermap.org/img/wn/${currentWeatherData.data?.weather?.get(0)?.icon}.png"

    val isSeeDetails = remember { mutableStateOf(false) }

    val color by animateColorAsState(
        targetValue = if (isSeeDetails.value) MaterialTheme.colorScheme.background.copy(
            0.15f
        ) else MaterialTheme.colorScheme.background.copy(0.25f)
    )


    val interactionSource = remember { MutableInteractionSource() }

    val unit = if (currentUnit == "metric") "°C" else "°F"

    Column(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { isSeeDetails.value = !isSeeDetails.value }
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
            .padding(horizontal = 25.dp, vertical = 10.dp)
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .background(color)
            .padding(15.dp),
    ) {
        val weatherItem = todayWeather.list[0]

        Row {
            Column {
                Text(
                    text = todayWeather.city.name + ", " + todayWeather.city.country,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    fontWeight = FontWeight.Light
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.W300,
                                fontSize = 80.sp
                            )
                        ) {
                            append(currentWeatherData.data?.main?.let { formatDecimals(it.temp) }
                                ?: "00")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.W300,
                                fontSize = MaterialTheme.typography.displaySmall.fontSize,
                            )
                        ) {
                            append(unit)
                        }
                    },
                    style = TextStyle(
                        brush = Colors.textBrush
                    )
                )

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background.copy(0.1f))
                        .padding(7.dp),
                ) {
                    Text(
                        text = "feels like: ${currentWeatherData.data?.main?.let { formatDecimals(it.feels_like) }}$unit",
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Serif,
                        lineHeight = 16.sp
                    )
                    Text(
                        text = "air Quality: ${checkAirQuality(aqi = airQuality?.list?.get(0)?.main?.aqi ?: 55)}",
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Serif,
                        lineHeight = 16.sp
                    )
                }

            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = imageUrl, contentDescription = "",
                    modifier = Modifier.size(60.dp)
                )
                Text(text = weatherItem.weather[0].main)
            }
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { isSeeDetails.value = !isSeeDetails.value }
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = if (isSeeDetails.value) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "arrow",
                )
                Text(text = "See Details")
            }
        }

        Text(
            text = weatherItem.weather[0].description,
            modifier = Modifier.padding(vertical = 5.dp),
            style = TextStyle(
                brush = Colors.rainbowBrush,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
        )
        if (isSeeDetails.value) {
            SeeMoreDetailsAboutWeather(
                currentUnit = currentUnit,
                weatherItem = weatherItem
            )
        }
    }
}

@Composable
fun SeeMoreDetailsAboutWeather(
    currentUnit: String,
    weatherItem: WeatherItem
) {

    HumidityWindPressure(currentUnit = currentUnit, weatherItem = weatherItem)
    Spacer(modifier = Modifier.height(10.dp))
    SunrisesAndSunset(weatherItem)
    HorizontalDivider(
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        modifier = Modifier.padding(10.dp)
    )
    TempMaxMin(currentUnit = currentUnit, weatherItem = weatherItem)
    TempMornEveDayNight(currentUnit = currentUnit, weatherItem = weatherItem)
}

@Composable
fun TempMornEveDayNight(weatherItem: WeatherItem, currentUnit: String) {

    val unit = if (currentUnit == "metric") "°C" else "°F"

    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp, vertical = 10.dp)
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background.copy(0.1f))
            .padding(10.dp),
    ) {
        Text(
            text = "Temperature",
            fontSize = 13.sp
        )
        HorizontalDivider(
            modifier = Modifier.padding(5.dp),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Temperature(temp = weatherItem.temp.morn, name = "Morn: ", unit = unit)
                Temperature(temp = weatherItem.temp.eve, name = "Eve: ", unit = unit)
            }
            Column {
                Temperature(temp = weatherItem.temp.day, name = "Day: ", unit = unit)
                Temperature(temp = weatherItem.temp.night, name = "Night: ", unit = unit)
            }
        }


    }
}

@Composable
private fun Temperature(temp: Double, name: String, unit: String = "") {
    Row {
        Text(text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.W200,
                    fontSize = 16.sp
                )
            ) {
                append(name)
            }
            withStyle(
                style = SpanStyle(
                    brush = Colors.textBrush,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
            ) {
                append(formatDecimals(temp) + unit)
            }
        })
    }
}

@Composable
fun TempMaxMin(weatherItem: WeatherItem, currentUnit: String) {

    val unit = if (currentUnit == "metric") "°C" else "°F"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Image(
            painter = painterResource(id = R.drawable.temperature), contentDescription = "temp",
            modifier = Modifier.size(40.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Row {
                Icon(
                    modifier = Modifier.rotate(180f),
                    painter = painterResource(id = R.drawable.arrow_downward_24),
                    contentDescription = null,
                    tint = Color.Red.copy(alpha = 0.7f)
                )
                Text(text = " " + formatDecimals(weatherItem.temp.max) + unit)
            }

            Row {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_downward_24),
                    contentDescription = null,
                    tint = Color(0xFF0ED8F7).copy(alpha = 0.7f)
                )
                Text(text = " " + formatDecimals(weatherItem.temp.min) + unit)
            }
        }

    }

}

@Composable
fun HumidityWindPressure(weatherItem: WeatherItem, currentUnit: String) {

    val pressureUnit = if (currentUnit == "metric") "hPa" else "psi"
    val windUnit = if (currentUnit == "metric") "m/s" else "mph"

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background.copy(0.1f))
            .padding(10.dp),
    ) {
        Row {
            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.humidity),
                contentDescription = "Humidity",
            )
            Text(
                text = " Humidity: " + weatherItem.humidity.toString() + "%",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.labelLarge.fontSize
                )
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row {
            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.atmospheric_pressure),
                contentDescription = "atmospheric_pressure"
            )
            Text(
                text = " Atmospheric Pressure: " + weatherItem.pressure.toString() + pressureUnit,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.labelLarge.fontSize
                )
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row {
            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.blue_wind),
                contentDescription = "wind speed"
            )
            Text(
                text = " Wind Speed: " + weatherItem.speed.toString() + windUnit,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.labelLarge.fontSize
                )
            )
        }
    }
}

@Composable
fun SunrisesAndSunset(weatherItem: WeatherItem) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background.copy(0.1f)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = R.drawable.sunrise),
                    contentDescription = "sunrise"
                )
                Column(
                    modifier = Modifier.padding(start = 5.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = formatDateTime(weatherItem.sunrise))
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.padding(end = 5.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = formatDateTime(weatherItem.sunset))
                }
                Image(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = R.drawable.sunset),
                    contentDescription = "sunset"
                )

            }
        }
    }
}

@Composable
fun ThisWeekWeather(weatherData: Weather?, currentUnit: String) {

    TextTitle(title = "Weather this week")

    val weekData = weatherData?.list?.subList(0, 7)

    AnimatedVisibility(visible = weekData != null) {
        LazyRow(
            modifier = Modifier,
            contentPadding = PaddingValues(horizontal = 15.dp, vertical = 5.dp)
        ) {
            weekData?.let { items ->
                items(items = items) { item: WeatherItem ->
                    WeatherCard(
                        currentUnit = currentUnit,
                        weatherItem = item
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherCard(
    weatherItem: WeatherItem,
    currentUnit: String,
) {

    val unit = if (currentUnit == "metric") "°C" else "°F"

    val isSeeDetails = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val imageUrl =
        "https://openweathermap.org/img/wn/${weatherItem.weather[0].icon}.png"
    Row(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { isSeeDetails.value = !isSeeDetails.value }
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .padding(10.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.background.copy(0.2f))
            .wrapContentSize()
            .padding(10.dp)
    ) {

        Column {

            Text(
                text = formatDate(weatherItem.dt),
                fontSize = 15.sp,
                lineHeight = 17.sp,
            )
            Text(
                text = formatDecimals(weatherItem.temp.day) + unit,
                style = TextStyle(
                    brush = Colors.textBrush,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize
                )
            )
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onBackground.copy(0.2f))
            ) {
                AsyncImage(
                    model = imageUrl, contentDescription = "image", modifier = Modifier
                        .size(45.dp)
                )
                Text(
                    text = weatherItem.weather[0].main,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
            }
        }

        if (isSeeDetails.value) {
            Column(
                modifier = Modifier.padding(start = 15.dp)
            ) {
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.temperature),
                        contentDescription = "temp",
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = " " + formatDecimals(weatherItem.temp.max) + unit,
                        lineHeight = 17.sp,
                        color = Color(0xE475EAFA)
                    )
                    Text(
                        text = " " + formatDecimals(weatherItem.temp.min) + unit,
                        lineHeight = 17.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )
                }
                HumidityWindPressure(weatherItem = weatherItem, currentUnit = currentUnit)
                Text(
                    text = weatherItem.weather[0].description,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .align(Alignment.CenterHorizontally),
                    lineHeight = 17.sp,
                    style = TextStyle(
                        brush = Colors.rainbowBrush,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    onSearch: (String) -> Unit = {},
) {

    val searchQueryState = rememberSaveable { mutableStateOf("") }
    val keyBoardController = LocalSoftwareKeyboardController.current
    val valid = remember(searchQueryState.value) {
        searchQueryState.value.trim().isNotEmpty()
    }

    CommonTextFieldSearch(
        modifier = modifier,
        pagerState = pagerState,
        valueState = searchQueryState,
        placeHolder = "Search here..",
        onAction = KeyboardActions {
            if (!valid) return@KeyboardActions
            Log.d("Search", searchQueryState.value)
            onSearch(searchQueryState.value.trim())
            searchQueryState.value = ""
            keyBoardController?.hide()
        }
    )

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommonTextFieldSearch(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    valueState: MutableState<String>,
    placeHolder: String,
    onAction: KeyboardActions = KeyboardActions.Default,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    val coroutineScope = rememberCoroutineScope()

    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = {
            Text(
                text = placeHolder,
                color = MaterialTheme.colorScheme.onBackground,
                style = TextStyle(
                    fontSize = 14.sp
                )
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search, contentDescription = "",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(30.dp),
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Filled.Menu, contentDescription = "",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(25.dp)
                    .clickable {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    },
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
        },
        textStyle = TextStyle(
            fontSize = 20.sp
        ),
        maxLines = 1,
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 1f),
            focusedBorderColor = Color(0x9300B8D4).copy(alpha = 0.7f),
            unfocusedBorderColor = Color(0x6300B8D4).copy(alpha = 0.5f),
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction,
        shape = RoundedCornerShape(15.dp),
        modifier = modifier
            .fillMaxWidth(),
    )
}

@Composable
fun TextTitle(title: String) {
    Box(
        modifier = Modifier
            .padding(horizontal = 25.dp, vertical = 5.dp)
            .clip(CircleShape)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background.copy(0.2f)),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(start = 12.dp)
        )
    }
}
