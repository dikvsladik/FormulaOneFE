package hu.bme.aut.android.formulaonefe.data.raceschedule

data class MRData(
    val RaceTable: RaceTable,
    val limit: String,
    val offset: String,
    val series: String,
    val total: String,
    val url: String,
    val xmlns: String
)