package hu.bme.aut.android.formulaonefe.ui.common

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import hu.bme.aut.android.formulaonefe.data.worldchampoins.Champions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.formulaonefe.data.worldchampoins.StandingsLists

import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize

import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleCoroutineScope
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.animation.circular.CircularRevealPlugin
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.placeholder.placeholder.PlaceholderPlugin
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin
import hu.bme.aut.android.formulaonefe.R
import kotlinx.coroutines.launch

@Composable
fun WorldChampionsScreen(champions: Champions?, lifecycleScope: LifecycleCoroutineScope,) {
    val championsList = remember { mutableStateOf(champions) }

    val sortedStandingsLists = championsList.value?.MRData?.StandingsTable?.StandingsLists?.sortedByDescending { it.season }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display the year
        Box(
            modifier = Modifier
                .fillMaxWidth().clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFDC0000)), contentAlignment = Alignment.Center
        ) {
            Text(
                text = "World Champions",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = myCustomFont,
                modifier = Modifier.padding(3.dp),
                style = TextStyle(
                    fontSize = 30.sp,
                    shadow = Shadow(color = Color.Black, blurRadius = 3f)
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(sortedStandingsLists ?: emptyList()) { index, standingsList ->
                if (index % 2 == 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Top
                    ) {
                        ChampionItem(standingsList,lifecycleScope)

                        if (index + 1 < (sortedStandingsLists?.size ?: 0)) {
                            ChampionItem(sortedStandingsLists!![index + 1],lifecycleScope)
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}


@Composable
fun ChampionItem(standingsList: StandingsLists,lifecycleScope: LifecycleCoroutineScope) {
    val driver = standingsList.DriverStandings.first().Driver
    val constructor = standingsList.DriverStandings.first().Constructors.first()


    val myCustomFont = FontFamily(Font(R.font.formula1_bold_web_0))

    val givenName = driver.givenName
    val familyName = driver.familyName
    val initials = givenName.take(3) + familyName.take(3)
    val formattedName = "${givenName}_${familyName}".replace(" ", "_")
    val driverImage = when(driver.driverId) {
        "de_vries"->"https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/N/NYCDEV01_Nyck_De%20Vries/nycdev01.png.transform/2col-retina/image.png"
        else->"https://media.formula1.com/content/dam/fom-website/drivers/${
            initials.first().uppercaseChar()
        }/${initials.uppercase()}" + "01_$formattedName/${initials.lowercase()}01.png.transform/2col-retina/image.png"
    }
    val context = LocalContext.current
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(2.dp).clickable(true, onClick = {
                val openUrlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(driver.url))
                lifecycleScope.launch {
                    context.startActivity(openUrlIntent)
                }
            })
    )
    {
        Column(
            modifier = Modifier
                .padding(2.dp).clickable(true, onClick = {
                    val openUrlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(driver.url))
                    lifecycleScope.launch {
                        context.startActivity(openUrlIntent)
                    }
                }),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = standingsList.season, fontFamily = myCustomFont, fontSize = 20.sp,color = Color(0xFFDC0000),
                modifier = Modifier
                    .padding(1.dp)
                    .widthIn(max = 100.dp), // Set a maximum width for the driver name
                textAlign = TextAlign.Center, // Center-align the text
                maxLines = 3, // Allow the text to wrap up to 2 lines
                overflow = TextOverflow.Ellipsis )
            GlideImage(
                imageModel = {
                    driverImage
                },
                component = rememberImageComponent {
                    +CircularRevealPlugin()
                    +ShimmerPlugin(
                        baseColor = Color(0xFFC0C0C0),
                        highlightColor = Color(0xFFCD7F32)
                    )
                    +PlaceholderPlugin.Failure(painterResource(id = R.drawable.unkowndriver))
                },
                failure = {
                    Text(text = " ")
                },
                imageOptions = ImageOptions(
                    requestSize = IntSize(453, 255),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center

                ),
                modifier = Modifier.padding(2.dp)

                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
            )
            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                Text(text = "${driver.givenName} ${driver.familyName}", fontFamily = myCustomFont,
                    modifier = Modifier
                        .padding(1.dp)
                        .widthIn(max = 130.dp), // Set a maximum width for the driver name
                    textAlign = TextAlign.Center, // Center-align the text
                    maxLines = 3, // Allow the text to wrap up to 2 lines
                    overflow = TextOverflow.Ellipsis )
                Text(text = constructor.name, fontFamily = myCustomFont,
                    modifier = Modifier
                        .padding(1.dp)
                        .widthIn(max = 100.dp), // Set a maximum width for the driver name
                    textAlign = TextAlign.Center, // Center-align the text
                    maxLines = 3, // Allow the text to wrap up to 2 lines
                    overflow = TextOverflow.Ellipsis )// Add ellipsis if the text exceeds the maximum lines)
            }

        }
    }

}

