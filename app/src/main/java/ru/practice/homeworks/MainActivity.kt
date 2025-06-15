package ru.practice.homeworks

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.crashlytics.setCustomKeys
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practice.homeworks.base.BaseActivity
import ru.practice.homeworks.presentation.cats.MainFragment
import ru.practice.homeworks.util.NavigationAction
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val mainContainerId: Int = R.id.container

    private lateinit var navController: NavController

    @Inject
    lateinit var notificationHandler: NotificationHandler
    @Inject
    lateinit var permissionHandler: PermissionHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (!permissionHandler.isNotificationPermissionGranted()) {
            permissionHandler.requestNotificationPermission(this) { granted ->
                if (granted) {
                    Toast.makeText(this, "Разрешено", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Запрещено", Toast.LENGTH_SHORT).show()
                }
            }
        }

        Firebase.crashlytics.setCustomKeys{
            val id = UUID.randomUUID()
            key("userId","$id")
        }

        notificationHandler.createNotificationChannel()

        val remoteConfig = Firebase.remoteConfig
        remoteConfig.fetchAndActivate().addOnCompleteListener(this) { task ->
            if (task.isSuccessful){
                val chartFeature = remoteConfig.getBoolean("openChartFeature")

                Log.d("MyLog","$chartFeature")
                visibleAnalyticsFragment(chartFeature)
            }
        }

    }

    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent, caller)
        intent?.let {
            handleIntent(it)
        }
    }

    private fun handleIntent(intent: Intent){
        val feature = intent.getStringExtra(KEY_FEATURE)
        if (feature != null){
            navController.navigate(R.id.converterFragment)
            intent.removeExtra(KEY_FEATURE)
        }
    }

    private fun visibleAnalyticsFragment(state: Boolean){
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val menu = bottomNavigationView.menu

        menu.findItem(R.id.chartFragment).isVisible = state
    }

    companion object{
        const val KEY_FEATURE = "openFeature"
    }
}