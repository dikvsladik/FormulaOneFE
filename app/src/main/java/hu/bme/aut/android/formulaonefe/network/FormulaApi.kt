package hu.bme.aut.android.formulaonefe.network

import hu.bme.aut.android.formulaonefe.data.raceschedule.RaceSchedule
import hu.bme.aut.android.formulaonefe.data.result.MRData
import hu.bme.aut.android.formulaonefe.data.result.RaceTable
import hu.bme.aut.android.formulaonefe.data.standings.DriverStandings
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://ergast.com/api/f1/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: GetFormulaDriverStandingsApi = retrofit.create(GetFormulaDriverStandingsApi::class.java)
}

class FormulaRepository(private val api: GetFormulaDriverStandingsApi) {
    suspend fun getFormula(year: String): DriverStandings? {
        val response = api.getFormula(year)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
    suspend fun getSchedule(year: String): RaceSchedule? {
        val response = api.getSchedule(year)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
    suspend fun getResult(year: String,race:String): hu.bme.aut.android.formulaonefe.data.result.Result? {
        val response = api.getResult(year,race)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
    suspend fun getLatest(): hu.bme.aut.android.formulaonefe.data.result.Result? {
        val response = api.getLatest()
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}
