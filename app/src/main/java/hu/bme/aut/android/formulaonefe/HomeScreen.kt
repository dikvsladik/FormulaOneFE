package hu.bme.aut.android.formulaonefe

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp


@Composable
fun HomeScreen(navController: NavController) {
    val MyCustomFont = FontFamily(Font(R.font.formula1_bold_web_0))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(Color(51,51,51)),
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
            Text("Driver Standings", color = Color.White, fontSize = 30.sp,fontFamily = MyCustomFont)
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
            Text("Race Schedule", color = Color.White, fontSize = 30.sp,fontFamily = MyCustomFont)
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
            Text("Race Result", color = Color.White, fontSize = 30.sp,fontFamily = MyCustomFont)
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
            Text("Driver Information", color = Color.White, fontSize = 27.sp,fontFamily = MyCustomFont)
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
            Text("World Champions", color = Color.White, fontSize = 28.sp,fontFamily = MyCustomFont)
        }
    }
}
