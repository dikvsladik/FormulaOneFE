package hu.bme.aut.android.formulaonefe.data.standings
import kotlinx.serialization.Serializable

@Serializable
data class StandingsLists(
    val DriverStandings: List<DriverStanding>,
    val round: String,
    val season: String
)