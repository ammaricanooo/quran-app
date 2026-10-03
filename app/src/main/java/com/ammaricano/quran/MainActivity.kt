package com.ammaricano.quran

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ammaricano.quran.service.AudioPlayerManager
import com.ammaricano.quran.ui.components.AudioPlayerBottomBar
import com.ammaricano.quran.ui.components.BottomNavBar
import com.ammaricano.quran.ui.components.LainnyaBottomSheet
import com.ammaricano.quran.ui.components.NavItem
import com.ammaricano.quran.ui.components.SettingsBottomSheet
import com.ammaricano.quran.ui.screens.artikel.ArtikelScreen
import com.ammaricano.quran.ui.screens.artikel.ArtikelViewModel
import com.ammaricano.quran.ui.screens.asmaulhusna.AsmaulHusnaScreen
import com.ammaricano.quran.ui.screens.asmaulhusna.AsmaulHusnaViewModel
import com.ammaricano.quran.ui.screens.bookmark.BookmarkScreen
import com.ammaricano.quran.ui.screens.bookmark.BookmarkViewModel
import com.ammaricano.quran.ui.screens.detail.SurahDetailScreen
import com.ammaricano.quran.ui.screens.detail.SurahDetailViewModel
import com.ammaricano.quran.ui.screens.doa.DoaScreen
import com.ammaricano.quran.ui.screens.doa.DoaViewModel
import com.ammaricano.quran.ui.screens.dzikir.DzikirScreen
import com.ammaricano.quran.ui.screens.dzikir.DzikirViewModel
import com.ammaricano.quran.ui.screens.hadits.HaditsScreen
import com.ammaricano.quran.ui.screens.hadits.HaditsViewModel
import com.ammaricano.quran.ui.screens.home.HomeScreen
import com.ammaricano.quran.ui.screens.home.HomeViewModel
import com.ammaricano.quran.ui.screens.jadwal.JadwalScreen
import com.ammaricano.quran.ui.screens.jadwal.JadwalViewModel
import com.ammaricano.quran.ui.screens.juz.JuzScreen
import com.ammaricano.quran.ui.screens.juz.JuzViewModel
import com.ammaricano.quran.ui.screens.kuis.KuisScreen
import com.ammaricano.quran.ui.screens.kuis.KuisViewModel
import com.ammaricano.quran.ui.screens.kultum.KultumScreen
import com.ammaricano.quran.ui.screens.kultum.KultumViewModel
import com.ammaricano.quran.ui.screens.maulid.MaulidScreen
import com.ammaricano.quran.ui.screens.maulid.MaulidViewModel
import com.ammaricano.quran.ui.screens.murottal.MurottalScreen
import com.ammaricano.quran.ui.screens.murottal.MurottalViewModel
import com.ammaricano.quran.ui.screens.profil.ProfilScreen
import com.ammaricano.quran.ui.screens.profil.ProfilViewModel
import com.ammaricano.quran.ui.screens.robithoh.RobithohScreen
import com.ammaricano.quran.ui.screens.robithoh.RobithohViewModel
import com.ammaricano.quran.ui.screens.tahlil.TahlilScreen
import com.ammaricano.quran.ui.screens.tahlil.TahlilViewModel
import com.ammaricano.quran.ui.theme.BgPrimary
import com.ammaricano.quran.ui.theme.QuranAppTheme

class MainActivity : ComponentActivity() {

    private lateinit var audioPlayerManager: AudioPlayerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        audioPlayerManager = AudioPlayerManager(this)

        setContent {
            QuranAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BgPrimary
                ) {
                    MainApp(audioPlayerManager = audioPlayerManager)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayerManager.release()
    }
}

@Composable
fun MainApp(audioPlayerManager: AudioPlayerManager) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: NavItem.Quran.route
    val context = LocalContext.current

    val playbackState by audioPlayerManager.playbackState.collectAsState()
    var isLainnyaSheetOpen by remember { mutableStateOf(false) }
    var isSettingsSheetOpen by remember { mutableStateOf(false) }

    val mainBottomBarRoutes = listOf(
        NavItem.Quran.route,
        NavItem.Jadwal.route,
        NavItem.Murottal.route,
        NavItem.Profil.route
    )

    val showBottomBar = currentRoute in mainBottomBarRoutes

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = NavItem.Quran.route,
            modifier = Modifier.fillMaxSize(),
            enterTransition = { fadeIn(animationSpec = tween(280)) + slideInHorizontally(animationSpec = tween(280)) { it / 6 } },
            exitTransition = { fadeOut(animationSpec = tween(240)) },
            popEnterTransition = { fadeIn(animationSpec = tween(280)) },
            popExitTransition = { fadeOut(animationSpec = tween(240)) + slideOutHorizontally(animationSpec = tween(240)) { it / 6 } }
        ) {
            // 1. Home (Quran Surah List)
            composable(NavItem.Quran.route) {
                val homeViewModel: HomeViewModel = viewModel()
                HomeScreen(
                    viewModel = homeViewModel,
                    onSurahClick = { nomor ->
                        navController.navigate("surah_detail/$nomor")
                    }
                )
            }

            // 2. Surah Detail
            composable(
                route = "surah_detail/{surahNomor}",
                arguments = listOf(navArgument("surahNomor") { type = NavType.IntType })
            ) { backStackEntry ->
                val surahNomor = backStackEntry.arguments?.getInt("surahNomor") ?: 1
                val detailViewModel: SurahDetailViewModel = viewModel(
                    factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                            val app = backStackEntry.destination.let {
                                navController.context.applicationContext as android.app.Application
                            }
                            return SurahDetailViewModel(app, surahNomor) as T
                        }
                    }
                )

                SurahDetailScreen(
                    viewModel = detailViewModel,
                    audioPlayerManager = audioPlayerManager,
                    onBack = { navController.popBackStack() }
                )
            }

            // 3. Jadwal Sholat
            composable(NavItem.Jadwal.route) {
                val jadwalViewModel: JadwalViewModel = viewModel()
                JadwalScreen(viewModel = jadwalViewModel)
            }

            // 4. Murottal 30 Juz
            composable(NavItem.Murottal.route) {
                val murottalViewModel: MurottalViewModel = viewModel()
                MurottalScreen(
                    viewModel = murottalViewModel,
                    audioPlayerManager = audioPlayerManager
                )
            }

            // 5. Profil (User Profile & Google Auth & Bookmarks)
            composable(NavItem.Profil.route) {
                val profilViewModel: ProfilViewModel = viewModel()
                ProfilScreen(
                    viewModel = profilViewModel,
                    onSurahClick = { nomor ->
                        navController.navigate("surah_detail/$nomor")
                    }
                )
            }

            // 6. Asmaul Husna
            composable("asmaul_husna") {
                val asmaulViewModel: AsmaulHusnaViewModel = viewModel()
                AsmaulHusnaScreen(viewModel = asmaulViewModel)
            }

            // 7. Doa Harian
            composable("doa") {
                val doaViewModel: DoaViewModel = viewModel()
                DoaScreen(viewModel = doaViewModel)
            }

            // 8. Dzikir Pagi & Petang
            composable("dzikir") {
                val dzikirViewModel: DzikirViewModel = viewModel()
                DzikirScreen(viewModel = dzikirViewModel)
            }

            // 9. Hadits Arbain
            composable("hadits") {
                val haditsViewModel: HaditsViewModel = viewModel()
                HaditsScreen(
                    viewModel = haditsViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            // 10. Tahlil & Yasin
            composable("tahlil") {
                val tahlilViewModel: TahlilViewModel = viewModel()
                TahlilScreen(
                    viewModel = tahlilViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            // 11. Juz 1-30
            composable("juz") {
                val juzViewModel: JuzViewModel = viewModel()
                JuzScreen(
                    viewModel = juzViewModel,
                    onSurahClick = { nomor ->
                        navController.navigate("surah_detail/$nomor")
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            // 12. Kuis Al-Qur'an
            composable("kuis") {
                val kuisViewModel: KuisViewModel = viewModel()
                KuisScreen(
                    viewModel = kuisViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            // 13. Doa Rabithah
            composable("robithoh") {
                val robithohViewModel: RobithohViewModel = viewModel()
                RobithohScreen(
                    viewModel = robithohViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 14. Materi Kultum
            composable("kultum") {
                val kultumViewModel: KultumViewModel = viewModel()
                KultumScreen(
                    viewModel = kultumViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 15. Artikel Islami
            composable("artikel") {
                val artikelViewModel: ArtikelViewModel = viewModel()
                ArtikelScreen(
                    viewModel = artikelViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 16. Kumpulan Maulid
            composable("maulid") {
                val maulidViewModel: MaulidViewModel = viewModel()
                MaulidScreen(
                    viewModel = maulidViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 17. Bookmark Screen (direct route)
            composable("bookmark") {
                val bookmarkViewModel: BookmarkViewModel = viewModel()
                BookmarkScreen(
                    viewModel = bookmarkViewModel,
                    onSurahClick = { nomor ->
                        navController.navigate("surah_detail/$nomor")
                    }
                )
            }
        }

        // Bottom Navigation Bar & Floating Audio Bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            AudioPlayerBottomBar(
                state = playbackState,
                onTogglePlayPause = { audioPlayerManager.togglePlayPause() },
                onClose = { audioPlayerManager.stop() }
            )

            if (showBottomBar) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(NavItem.Quran.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onOpenLainnyaSheet = { isLainnyaSheetOpen = true }
                )
            }
        }

        // Modal Bottom Sheet Menu Lainnya
        if (isLainnyaSheetOpen) {
            LainnyaBottomSheet(
                onDismiss = { isLainnyaSheetOpen = false },
                onNavigate = { route ->
                    when (route) {
                        "kiblat" -> {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://qiblafinder.withgoogle.com/"))
                            context.startActivity(intent)
                        }
                        "settings" -> {
                            isSettingsSheetOpen = true
                        }
                        else -> {
                            navController.navigate(route)
                        }
                    }
                }
            )
        }

        // Settings Modal Bottom Sheet
        if (isSettingsSheetOpen) {
            SettingsBottomSheet(
                onDismiss = { isSettingsSheetOpen = false }
            )
        }
    }
}
