package hu.bme.aut.android.formulaonefe.data.result

data class Race(
    val Circuit: Circuit,
    val Results: List<ResultX>,
    val date: String,
    val raceName: String,
    val round: String,
    val season: String,
    val time: String,
    val url: String
)