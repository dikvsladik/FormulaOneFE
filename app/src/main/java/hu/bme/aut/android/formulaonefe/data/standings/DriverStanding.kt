package hu.bme.aut.android.formulaonefe.data.standings
import kotlinx.serialization.Serializable

@Serializable
data class DriverStanding(
    val Constructors: List<Constructor>,
    val Driver: Driver,
    val points: String,
    val position: String,
    val positionText: String,
    val wins: String
)