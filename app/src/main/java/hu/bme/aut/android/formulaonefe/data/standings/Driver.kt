package hu.bme.aut.android.formulaonefe.data.standings
import kotlinx.serialization.Serializable

@Serializable
data class Driver(
    val code: String,
    val dateOfBirth: String,
    val driverId: String,
    val familyName: String,
    val givenName: String,
    val nationality: String,
    val permanentNumber: String,
    val url: String
)