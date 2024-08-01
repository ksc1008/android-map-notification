package ksc.campus.tech.kakao.map.presentation.views

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.loader.ResourcesProvider
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.common.base.Strings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ksc.campus.tech.kakao.map.R
import ksc.campus.tech.kakao.map.databinding.ActivitySplashScreenBinding
import ksc.campus.tech.kakao.map.domain.repositories.FirebaseRemoteConfigRepository
import ksc.campus.tech.kakao.map.presentation.viewmodels.SplashActivityViewModel

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySplashScreenBinding
    private val viewModel: SplashActivityViewModel by viewModels()
    private var shouldDisplaySplashScreen = true
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.appState.collect{
                    shouldDisplaySplashScreen = (it == FirebaseRemoteConfigRepository.AppState.UNKNOWN)

                    when(it){
                        FirebaseRemoteConfigRepository.AppState.UNAVAILABLE ->
                            binding.alertText = resources.getString(R.string.service_unavailable_message)
                        FirebaseRemoteConfigRepository.AppState.ON_MAINTENANCE ->
                            binding.alertText = resources.getString(R.string.maintenance_message)
                        FirebaseRemoteConfigRepository.AppState.ON_SERVICE ->
                            switchToMainActivity()
                        else -> {}

                    }
                }
            }
        }

        splashScreen.setKeepOnScreenCondition{
            shouldDisplaySplashScreen
        }

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun switchToMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}