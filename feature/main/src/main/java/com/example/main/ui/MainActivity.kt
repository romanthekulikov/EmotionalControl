package com.example.main.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.main.R
import com.example.main.databinding.ActivityMainBinding
import com.example.main.di.MainComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.kulikov.core.utils.base.UiEvent
import ru.kulikov.core.utils.router.Router
import ru.kulikov.core.utils.router.Screen
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var router: Router

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels {
        MainViewModel.Factory()
    }

    init {
        MainComponent.getInstance().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        addEventListener()
        observeState()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.refresh_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        addListeners()
        binding.seekPartner.isEnabled = false
        viewModel.loadData()
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                binding.seekPartner.progress = (state.partnerEmotionalIndicator * 100).toInt()
                binding.imagePartnerEmoji.setImageResource(state.partnerEmotionalEmoji)
                binding.textPartnerIndicator.text = state.partnerName

                binding.seekUser.progress = (state.userEmotionalIndicator * 100).toInt()
                binding.imageUserEmoji.setImageResource(state.userEmotionalEmoji)
                binding.textUserIndicator.text = state.userName
            }
        }
    }

    private fun addEventListener() {
        lifecycleScope.launch {
            viewModel.events.collect { event ->
                when (event) {
                    is UiEvent.ShowToast -> Toast.makeText(this@MainActivity, event.message, Toast.LENGTH_LONG).show()
                    UiEvent.Navigate -> { /* Nothing */
                    }

                    UiEvent.InProgress -> {
                        binding.layoutProgressbar.visibility = View.VISIBLE
                        binding.progressbar.isActivated = true
                    }

                    UiEvent.OutProgress -> {
                        binding.layoutProgressbar.visibility = View.GONE
                        binding.progressbar.isActivated = false
                    }
                }
            }
        }
    }

    private fun addListeners() {
        binding.refreshLayout.setOnRefreshListener {
            viewModel.loadData()
            binding.refreshLayout.isRefreshing = false
        }

        binding.buttonBack.setOnClickListener {
            viewModel.forgotPartnerId()
            router.navigateTo(Screen.EnterScreen(this))
            finish()
        }

        binding.buttonProfile.setOnClickListener {
            router.navigateTo(Screen.ProfileScreen(this))
        }

        binding.seekUser.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.setUserEmotionalIndicator(progress.toDouble() / 100)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { /* Nothing */
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) { /* Nothing */
            }

        })

        binding.seekPartner.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.setPartnerEmotionalIndicator(progress.toDouble() / 100)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { /* Nothing */
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) { /* Nothing */
            }

        })

        binding.buttonSave.setOnClickListener {
            viewModel.saveUserIndicator()
        }

        binding.buttonStatistic.setOnClickListener {
            router.navigateTo(Screen.StatisticScreen(this))
        }
    }

    companion object {
        fun createIntent(fromContext: Context): Intent = Intent(fromContext, MainActivity::class.java)
    }
}