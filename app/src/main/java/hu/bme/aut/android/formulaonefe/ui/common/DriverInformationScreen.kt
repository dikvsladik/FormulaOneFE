package hu.bme.aut.android.formulaonefe.ui.common

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import hu.bme.aut.android.formulaonefe.data.driverinformation.DriverInformation

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange

import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleCoroutineScope
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.animation.circular.CircularRevealPlugin
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.placeholder.placeholder.PlaceholderPlugin
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin
import hu.bme.aut.android.formulaonefe.R
import hu.bme.aut.android.formulaonefe.data.driverinformation.Driver
import hu.bme.aut.android.formulaonefe.network.ApiClient
import hu.bme.aut.android.formulaonefe.network.FormulaRepository
import hu.bme.aut.android.formulaonefe.ui.tools.YearPickerScreen1950
import kotlinx.coroutines.launch
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DriverInformationScreen(
    driversList: DriverInformation?,
    lifecycleScope: LifecycleCoroutineScope,
    onDriverItemClick: (Driver) -> Unit
) {
    val updatedDriverInformation = remember { mutableStateOf(driversList) }
    val yearPicked = remember { mutableStateOf(LocalDate.now().year) }
    val driversList = remember { mutableStateOf<DriverInformation?>(null) }
    val showYearPicker = remember { mutableStateOf(false) }
    val repository = FormulaRepository(ApiClient.api)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (showYearPicker.value) {
            YearPickerScreen1950 { selectedYear ->
                lifecycleScope.launch {
                    val newDriversList = repository.getDrivers(selectedYear.toString())
                    yearPicked.value = selectedYear
                    updatedDriverInformation.value = newDriversList
                }
                showYearPicker.value = false
            }
        } else {
            updatedDriverInformation.value?.let { driverInfo ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Display the year
                    Box(
                        modifier = Modifier
                            .fillMaxWidth().clip( RoundedCornerShape(8.dp))
                            .background(Color(0xFFDC0000)), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = updatedDriverInformation.value!!.MRData.DriverTable.season,
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

                    // Display the drivers list in two columns
                    val halfListSize = updatedDriverInformation.value!!.MRData.DriverTable.Drivers.size / 2
                    LazyColumn {
                        itemsIndexed(updatedDriverInformation.value!!.MRData.DriverTable.Drivers.chunked(2)) { index, drivers ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                drivers.forEach { driver ->
                                    DriverItem(driver = driver, onItemClick = { onDriverItemClick(driver) })
                                }
                            }
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = { showYearPicker.value = !showYearPicker.value },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            backgroundColor = Color(0xFFDC0000)
        ) {
            Icon(Icons.Filled.DateRange, contentDescription = "Év kiválasztása")
        }
    }
}
@Composable
fun DriverItem(driver: Driver, onItemClick: () -> Unit) {
    // Replace this URL with the actual image URL for the driver
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
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .clickable { onItemClick() }
            .padding(2.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable { onItemClick() }
                .padding(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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

            Text(
                text = "${driver.givenName} ${driver.familyName}",
                fontFamily = myCustomFont,
                modifier = Modifier
                    .padding(6.dp)
                    .widthIn(max = 90.dp), // Set a maximum width for the driver name
                textAlign = TextAlign.Center, // Center-align the text
                maxLines = 2, // Allow the text to wrap up to 2 lines
                overflow = TextOverflow.Ellipsis // Add ellipsis if the text exceeds the maximum lines
            )
        }
    }
}

