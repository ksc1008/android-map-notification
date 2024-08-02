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

    private fun bindData(){
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.appState.collect{
                    shouldDisplaySplashScreen = (it.serviceState == FirebaseRemoteConfigRepository.AppState.UNKNOWN)

                    if(it.serviceState == FirebaseRemoteConfigRepository.AppState.ON_SERVICE){
                        switchToMainActivity()
                    }
                }
            }
        }
    }

    private fun bindActivityLayout(){
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        bindData()

        splashScreen.setKeepOnScreenCondition {
            shouldDisplaySplashScreen
        }

        bindActivityLayout()
    }

    private fun switchToMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}