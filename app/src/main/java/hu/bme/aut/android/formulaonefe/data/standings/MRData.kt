package hu.bme.aut.android.formulaonefe.data.standings
import kotlinx.serialization.Serializable

@Serializable
data class MRData(
    val StandingsTable: StandingsTable,
    val limit: String,
    val offset: String,
    val series: String,
    val total: String,
    val url: String,
    val xmlns: String
)