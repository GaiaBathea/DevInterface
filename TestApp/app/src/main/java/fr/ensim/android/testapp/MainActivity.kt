package fr.ensim.android.testapp
//Ajout de HomeScreen
import fr.ensim.android.testapp.ui.screen.HomeScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fr.ensim.android.testapp.ui.theme.TestAppTheme
import androidx.compose.material3.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.firebase.FirebaseApp
import fr.ensim.android.testapp.ui.screen.ArtDetailScreen
import fr.ensim.android.testapp.ui.screen.ConnexionScreen
import fr.ensim.android.testapp.ui.screen.DejaVuScreen
import fr.ensim.android.testapp.ui.screen.InscriptionScreen
import fr.ensim.android.testapp.ui.screen.ListeAScreen
import fr.ensim.android.testapp.ui.screen.MonBoardScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestAppTheme {
                val navController = rememberNavController()

                AppNavigation()


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(navController = navController)
                        }
                        composable("login") {
                            ConnexionScreen( navController = navController) // Ajoute ça !
                        }

                        composable("signup") {
                            InscriptionScreen(navController= navController) }


                        composable(
                            "detail/{image}/{title}/{artist}/{year}",
                            arguments = listOf(
                                navArgument("image") { type = NavType.StringType },
                                navArgument("title") { type = NavType.StringType },
                                navArgument("artist") { type = NavType.StringType },
                                navArgument("year") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            ArtDetailScreen(
                                navController = navController,
                                imageName = backStackEntry.arguments?.getString("image") ?: "",
                                title = backStackEntry.arguments?.getString("title") ?: "",
                                artist = backStackEntry.arguments?.getString("artist") ?: "",
                                year = backStackEntry.arguments?.getString("year") ?: ""
                            )
                        }

                        composable("monboard") { MonBoardScreen(navController) }
                        composable("dejavu") { DejaVuScreen(navController) }
                        composable("listea") { ListeAScreen(navController) }

                    }
                }
            }
        }
    }
}
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "connexion") {
        composable("connexion") {
            ConnexionScreen(navController)
        }
        composable("signup") {
            InscriptionScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestAppTheme {
        Greeting("Android")
    }
}