package bangkit.mobiledev.cek_apel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import bangkit.mobiledev.cek_apel.R
import bangkit.mobiledev.cek_apel.databinding.ActivityMainBinding
import bangkit.mobiledev.cek_apel.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            // Jika belum login, arahkan ke LoginActivity
            val loginIntent = Intent(this, LoginActivity::class.java)
            loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(loginIntent)
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup NavController dan BottomNavigationView
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.navView.setupWithNavController(navController)

        // Tambahkan custom listener untuk BottomNavigationView
        binding.navView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.navigation_home, true) // Bersihkan stack sampai Home
                        .build()
                    navController.navigate(R.id.navigation_home, null, navOptions)
                    true
                }
                R.id.navigation_scan -> {
                    navController.navigate(R.id.navigation_scan)
                    true
                }
                R.id.navigation_article -> {
                    navController.navigate(R.id.navigation_article)
                    true
                }
                R.id.navigation_settings -> {
                    navController.navigate(R.id.navigation_settings)
                    true
                }
                else -> false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.d("NavigationDebug", "Current destination: ${destination.id} - ${destination.label}")
        }

    }
}