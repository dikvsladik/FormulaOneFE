package hu.bme.aut.android.formulaonefe.data.standings
import kotlinx.serialization.Serializable

@Serializable
data class Constructor(
    val constructorId: String,
    val name: String,
    val nationality: String,
    val url: String
)