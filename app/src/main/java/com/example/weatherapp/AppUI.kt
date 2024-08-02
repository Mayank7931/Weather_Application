package com.example.weatherapp

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapp.apirelated.NetworkResponse
import com.example.weatherapp.apirelated.WeatherData
import com.example.weatherapp.ui.theme.Green3
import com.example.weatherapp.ui.theme.Saffron1
import com.example.weatherapp.ui.theme.White2


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@Preview
@Composable
fun AppUI(viewModel: WeatherViewModel) {

    val gradient = Brush.verticalGradient(listOf(Saffron1, White2, Green3))


    Scaffold(topBar = { TopAppBar(modifier = Modifier.background(gradient),
        title = { Text(text = "Weather",
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontSize = 30.sp, color = Color.White)},
        colors = TopAppBarDefaults.topAppBarColors(Color.Transparent)
    )})
    {innerpadding->

        var location by remember {
            mutableStateOf("")
        }

        val weatherResult = viewModel.weatherResult.observeAsState()

        HorizontalDivider()

        Surface(modifier = Modifier
            .padding(innerpadding)
            ) {

            Background()

            Column {
                Row (modifier = Modifier
                    .padding(10.dp)
                    ){
                    OutlinedTextField(modifier = Modifier.weight(1f),value = location, onValueChange = {
                        location = it
                    }, placeholder = { Text(text = "search the location")})

                    Spacer(modifier = Modifier.height(35.dp))

                    val keyBoardController = LocalSoftwareKeyboardController.current

                    IconButton(onClick = {  keyBoardController?.hide()
                        viewModel.getWeatherData(location) })
                    {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "search")
                    }
                }


                when(val result  = weatherResult.value){
                    is NetworkResponse.Error -> {
                        Text(text = "failed to load data")
                    }
                    NetworkResponse.Loading -> {
                        CircularProgressIndicator()
                    }
                    is NetworkResponse.Sucess -> {
                        
                        ShowData(data = result.data)
                        
                        
                    }
                    null -> {}
                }

                }




        }

    }
}

//@Preview
@Composable
fun ShowData(data : WeatherData ) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)) {

        Row (modifier = Modifier.fillMaxWidth()){
            Icon(imageVector = Icons.Default.LocationOn, contentDescription = "location symbol", tint = Saffron1)
            
            Spacer(modifier = Modifier.width(15.dp))

            Text(text = "${data.location.name}  ( ${data.location.country} )", fontSize = 20.sp)
        }
        
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "${data.current.temp_c}  ° C", fontSize = 30.sp,
            modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        
       AsyncImage(modifier = Modifier
           .size(200.dp)
           .fillMaxWidth()
           .align(Alignment.CenterHorizontally), 
           model = "https:${data.current.condition.icon}".replace("64x64","128x128"), 
           contentDescription = data.current.condition.icon )

//        Icon(data.current.condition.icon, contentDescription = "condition")

        // other details
        
        Spacer(modifier = Modifier.height(20.dp))
        


        Column (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom){

            ElevatedCard(
                elevation = CardDefaults.elevatedCardElevation(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(20.dp),
            )
            {
                Text(text = "Stats", modifier = Modifier.fillMaxWidth(), fontSize = 35.sp, textAlign = TextAlign.Center)

                Spacer(modifier = Modifier.height(50.dp))

                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                )
                {
                    Row {
                        ShowOtherDetails(parameter = "Humidity", value = data.current.humidity)

                        Spacer(modifier = Modifier.width(80.dp))

                        ShowOtherDetails(
                            parameter = "Pressure",
                            value = data.current.pressure_mb + "mb"
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Row {
                        ShowOtherDetails(
                            parameter = "Wind Speed",
                            value = data.current.wind_kph + "km/h"
                        )

                        Spacer(modifier = Modifier.width(100.dp))

                        ShowOtherDetails(parameter = "UV", value = data.current.uv)
                    }
                }
            }
        }

    }
}

@Composable
fun ShowOtherDetails(parameter:String, value:String) {

    Column {
        Text(text = parameter,
            fontSize = 25.sp,
            textAlign = TextAlign.Start)

        Text(text = value,
            fontSize = 20.sp)
    }
}

@Preview
@Composable
fun Background(modifier: Modifier = Modifier) {

    val backgroundGradient = Brush.horizontalGradient(listOf(Color.LightGray, Color.White,Color.Cyan))

    Box(modifier = Modifier.fillMaxSize()
        .background(backgroundGradient)
    ){
//        Image(painter = painterResource(id = R.drawable.weather),
//            contentDescription = "background",
//            contentScale = ContentScale.FillBounds,
//            modifier = Modifier.matchParentSize())
    }

}