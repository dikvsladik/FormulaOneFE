package hu.bme.aut.android.formulaonefe.ui.tools

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.animation.circular.CircularRevealPlugin
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.placeholder.placeholder.PlaceholderPlugin
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin
import hu.bme.aut.android.formulaonefe.R
import hu.bme.aut.android.formulaonefe.data.driverinformation.Driver
import java.time.LocalDate
import java.time.Period


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DriverDetailsScreen(driver: Driver, onWikipediaButtonClick: () -> Unit) {

    val myCustomFont = FontFamily(Font(R.font.formula1_bold_web_0))
    val context = LocalContext.current
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
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "${driver.givenName} ${driver.familyName}", fontFamily = myCustomFont, fontSize = 36.sp, modifier = Modifier
                    .padding(6.dp)
                .widthIn(max = 300.dp), // Set a maximum width for the driver name
                textAlign = TextAlign.Center, // Center-align the text
                maxLines = 2, // Allow the text to wrap up to 2 lines
                overflow = TextOverflow.Ellipsis)

            Spacer(modifier = Modifier.height(16.dp))

            GlideImage(
                imageModel = {driverImage

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
                    requestSize = IntSize(900, 750),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center

                ),
                modifier = Modifier.padding(2.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Black,CircleShape)
            )
            Spacer(modifier = Modifier.height(16.dp))


            val largerFontSize = 24.sp
            val variableColor = Color.Red

            val nationalityText = buildAnnotatedString {
                append("He is a ")
                withStyle(SpanStyle(color = variableColor, fontSize = largerFontSize)) {
                    append(driver.nationality)
                }
                append(" racer")
            }
            Text(text = nationalityText, fontFamily = myCustomFont, fontSize = 18.sp)

            Spacer(modifier = Modifier.height(16.dp))

            val permanentNumberText = buildAnnotatedString {
                append("His Permanent Number is ")
                withStyle(SpanStyle(color = variableColor, fontSize = largerFontSize)) {
                    append(driver.permanentNumber.toString())
                }
            }
            Text(text = permanentNumberText, fontFamily = myCustomFont, fontSize = 18.sp)

            Spacer(modifier = Modifier.height(16.dp))

            val dateOfBirth = LocalDate.parse(driver.dateOfBirth)
            val age = Period.between(dateOfBirth, LocalDate.now()).years
            val ageText = buildAnnotatedString {
                append("He is ")
                withStyle(SpanStyle(color = variableColor, fontSize = largerFontSize)) {
                    append(age.toString())
                }
                append(" years old")
            }
            Text(text = ageText, fontFamily = myCustomFont, fontSize = 18.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val openUrlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(driver.url))
                    context.startActivity(openUrlIntent)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFDC0000))
            ) {
                Text(text = "Go to Wikipedia", fontSize = 25.sp, fontFamily = myCustomFont)
            }
        }
    }
}
