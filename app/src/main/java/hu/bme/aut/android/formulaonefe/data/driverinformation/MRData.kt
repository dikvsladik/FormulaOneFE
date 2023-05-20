package hu.bme.aut.android.formulaonefe.data.driverinformation

data class MRData(
    val DriverTable: DriverTable,
    val limit: String,
    val offset: String,
    val series: String,
    val total: String,
    val url: String,
    val xmlns: String
)