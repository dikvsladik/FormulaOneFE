package hu.bme.aut.android.formulaonefe

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.animation.circular.CircularRevealPlugin
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.placeholder.placeholder.PlaceholderPlugin
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(imageVector = ImageVector.vectorResource(id = R.drawable.f1_logo),
                            contentDescription = "F1 smol icon",modifier = Modifier.size(120.dp), tint = Color.White )
                    }
                },
                backgroundColor = Color(255, 24, 1)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Navigate to settings */ },backgroundColor = Color(211, 211, 211)) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        },
        backgroundColor = Color(51, 51, 51)
    ) {

        val myCustomFont = FontFamily(Font(R.font.formula1_bold_web_0))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .background(Color(51, 51, 51)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.navigate(Screen.DriverStandings.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(255, 24, 1))
            ) {
                Text(
                    "Driver Standings",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontFamily = myCustomFont
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate(Screen.RaceSchedule.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(255, 24, 1))
            ) {
                Text(
                    "Race Schedule",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontFamily = myCustomFont
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate(Screen.RaceResult.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(255, 24, 1))
            ) {
                Text(
                    "Race Result",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontFamily = myCustomFont
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate(Screen.DriverInformation.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(255, 24, 1))
            ) {
                Text(
                    "Driver Information",
                    color = Color.White,
                    fontSize = 27.sp,
                    fontFamily = myCustomFont
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate(Screen.WorldChampions.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(255, 24, 1))
            ) {
                Text(
                    "World Champions",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontFamily = myCustomFont
                )
            }
        }
    }
}
