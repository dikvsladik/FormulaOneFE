package hu.bme.aut.android.formulaonefe.network
import hu.bme.aut.android.formulaonefe.data.driverinformation.DriverInformation
import hu.bme.aut.android.formulaonefe.data.raceschedule.RaceSchedule
import hu.bme.aut.android.formulaonefe.data.result.MRData
import hu.bme.aut.android.formulaonefe.data.result.RaceTable
import hu.bme.aut.android.formulaonefe.data.standings.DriverStandings
import hu.bme.aut.android.formulaonefe.data.worldchampoins.Champions
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GetFormulaDriverStandingsApi {
    @GET("{year}/driverStandings.json")
    suspend fun getFormula(@Path("year") year: String): Response<DriverStandings>
    @GET("{year}.json")
    suspend fun getSchedule(@Path("year") year: String): Response<RaceSchedule>

    @GET( "{year}/{race}/results.json")
    suspend fun getResult(@Path("year") year:String,@Path("race") race:String): Response<hu.bme.aut.android.formulaonefe.data.result.Result>
    @GET( "current/last/results.json")
    suspend fun getLatest(): Response<hu.bme.aut.android.formulaonefe.data.result.Result>
    @GET( "{year}/drivers.json")
    suspend fun getDrivers(@Path("year") year: String): Response<DriverInformation>
    @GET( "driverstandings/1.json?limit=100")
    suspend fun getChampions(): Response<Champions>
}