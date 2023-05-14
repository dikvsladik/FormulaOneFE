
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object DriverStandings : Screen("driver_standings")
    object RaceSchedule : Screen("race_schedule")
    object RaceResult : Screen("race_result")
    object DriverInformation : Screen("driver_information")
    object WorldChampions : Screen("world_champions")
}
