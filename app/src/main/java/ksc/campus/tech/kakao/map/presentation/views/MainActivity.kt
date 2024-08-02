package ksc.campus.tech.kakao.map.presentation.views

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import ksc.campus.tech.kakao.map.R
import ksc.campus.tech.kakao.map.databinding.ActivityMainBinding
import ksc.campus.tech.kakao.map.presentation.viewmodels.SearchActivityViewModel
import ksc.campus.tech.kakao.map.presentation.views.adapters.SearchKeywordAdapter
import ksc.campus.tech.kakao.map.presentation.views.adapters.SearchKeywordClickCallback
import ksc.campus.tech.kakao.map.presentation.views.fragments.KakaoMapFragment
import ksc.campus.tech.kakao.map.presentation.views.fragments.SearchResultFragment
import ksc.campus.tech.kakao.map.presentation.views.services.MapFirebaseMessagingService


@BindingAdapter("app:keywords")
fun attachList(recyclerView: RecyclerView, items: StateFlow<List<String>>?){
    items?.let {
        (recyclerView.adapter as SearchKeywordAdapter).submitList(it.value)
    }
}

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val foregroundMessaging: ForegroundMessaging by lazy{
        ForegroundMessaging(this)
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            Toast.makeText(this, "해당 앱에 대한 알림 메시지를 표시하지 않습니다.",Toast.LENGTH_SHORT).show()
        }
    }

    private val fragmentManager = supportFragmentManager
    private lateinit var searchFragment: Fragment
    private lateinit var mapFragment: Fragment
    private lateinit var mainActivityBinding: ActivityMainBinding

    val searchViewModel: SearchActivityViewModel by viewModels()

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // 권한 요청 이유를 설명하는 UI를 표시
                showNotificationPermissionDialog()
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun showNotificationPermissionDialog() {
        AlertDialog.Builder(this@MainActivity).apply {
            setTitle(getString(R.string.ask_notification_permission_dialog_title))
            setMessage(
                String.format(
                    "다양한 알림 소식을 받기 위해 권한을 허용하시겠어요?\n(알림 에서 %s의 알림 권한을 허용해주세요.)",
                    getString(R.string.app_name)
                )
            )
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            setNegativeButton(getString(R.string.deny_notification_permission)) { _, _ -> }
            show()
        }
    }

    private fun initiateBinding(){
        mainActivityBinding.viewModel = searchViewModel
        mainActivityBinding.lifecycleOwner = this
        mainActivityBinding.rootActivity = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()

        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        initiateBinding()
        setContentView(mainActivityBinding.root)

        initiateFragments()
        initiateViews()
        initiateLiveDataObservation()

        foregroundMessaging.createNotificationChannel(this)
    }

    private fun initiateLiveDataObservation() {
        searchViewModel.searchText.observe(this) {
            mainActivityBinding.inputSearch.setQuery(it, false)
        }

        searchViewModel.activeContent.observe(this) {
            if (it == SearchActivityViewModel.ContentType.MAP) {
                switchToMapMenu()
            } else {
                switchToSearchMenu()
            }
        }
    }

    private fun initiateSaveKeywordRecyclerView() {
        val adapter =
            SearchKeywordAdapter(object : SearchKeywordClickCallback {
                override fun clickKeyword(keyword: String) {
                    searchViewModel.clickKeyword(keyword)
                }

                override fun clickRemove(keyword: String) {
                    searchViewModel.clickKeywordDeleteButton(keyword)
                }
            })

        mainActivityBinding.savedSearchBar.adapter = adapter
        mainActivityBinding.savedSearchBar.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initiateSearchView() {
        mainActivityBinding.inputSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchViewModel.submitQuery(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        mainActivityBinding.inputSearch.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                searchViewModel.switchContent(SearchActivityViewModel.ContentType.SEARCH_LIST)
            }
        }
    }

    private fun initiateFragments() {
        mapFragment = KakaoMapFragment()
        searchFragment = SearchResultFragment()
    }

    private fun initiateViews() {
        initiateSearchView()
        initiateSaveKeywordRecyclerView()
    }

    private fun switchToSearchMenu() {
        val fragmentReplaceTransaction = fragmentManager.beginTransaction()
        fragmentReplaceTransaction.replace(R.id.fragment_container_search_result, searchFragment)
        fragmentReplaceTransaction.commit()
    }

    private fun switchToMapMenu() {
        val fragmentReplaceTransaction = fragmentManager.beginTransaction()
        fragmentReplaceTransaction.replace(R.id.fragment_container_search_result, mapFragment)
        fragmentReplaceTransaction.commit()
    }
}
