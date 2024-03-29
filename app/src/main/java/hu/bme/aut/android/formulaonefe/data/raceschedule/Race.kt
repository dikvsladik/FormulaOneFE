package hu.bme.aut.android.formulaonefe.data.raceschedule

data class Race(
    val Circuit: Circuit,
    val FirstPractice: FirstPractice,
    val Qualifying: Qualifying,
    val SecondPractice: SecondPractice,
    val Sprint: Sprint,
    val ThirdPractice: ThirdPractice,
    val date: String,
    val raceName: String,
    val round: String,
    val season: String,
    val time: String,
    val url: String
)