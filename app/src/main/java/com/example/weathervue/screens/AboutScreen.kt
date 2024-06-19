package com.example.weathervue.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.weathervue.R
import com.example.weathervue.utils.Colors

@Composable
fun AboutScreen(
    navController: NavController = rememberNavController()
) {

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Colors.gradientColors)
                .systemBarsPadding(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TopBarOfAboutScreen(navController)


            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 40.dp, vertical = 80.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.2f)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Text(
                    text = stringResource(id = R.string.about_app),
                    color = MaterialTheme.colorScheme.onBackground,

                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Monospace
                    )
                )

                val annotatedText = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Normal
                        )
                    ) {
                        append("Click\n")
                    }
                    pushStringAnnotation(
                        tag = "URL",
                        annotation = "https://openweathermap.org/"
                    )
                    withStyle(
                        style = SpanStyle(
                            fontSize = 15.sp,
                            color = Color.Blue, fontWeight = FontWeight.W500
                        )
                    ) {
                        append(stringResource(id = R.string.get_api_data_from))
                    }
                    pop()
                }

                val uriHandler = LocalUriHandler.current
                ClickableText(text = annotatedText, onClick = { offset ->
                    annotatedText.getStringAnnotations(
                        tag = "URL", start = offset, end = offset
                    ).firstOrNull()?.let { annotation ->
                        uriHandler.openUri(annotation.item)
                    }
                })

                Spacer(modifier = Modifier.height(80.dp))

                Text(
                    text = stringResource(id = R.string.api_used),
                    color = MaterialTheme.colorScheme.onBackground,

                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Monospace
                    )
                )
                Text(
                    text = stringResource(id = R.string.api),
                    modifier = Modifier.padding(horizontal = 20.dp),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Monospace
                    )
                )
            }
        }
    }
}

@Composable
fun TopBarOfAboutScreen(navController: NavController) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
            Text(
                text = "About",
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
        }

    }


}
