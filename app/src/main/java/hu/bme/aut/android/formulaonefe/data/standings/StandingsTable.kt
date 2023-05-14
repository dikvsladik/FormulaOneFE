package hu.bme.aut.android.formulaonefe.data.standings
import kotlinx.serialization.Serializable

@Serializable
data class StandingsTable(
    val StandingsLists: List<StandingsLists>,
    val season: String
)