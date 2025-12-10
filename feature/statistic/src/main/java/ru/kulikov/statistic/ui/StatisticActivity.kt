package ru.kulikov.statistic.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.kulikov.core.utils.base.UiEvent
import ru.kulikov.core.utils.router.Router
import ru.kulikov.core.utils.router.Screen
import ru.kulikov.statistic.R
import ru.kulikov.statistic.data.PeriodMode
import ru.kulikov.statistic.databinding.ActivityStatisticBinding
import ru.kulikov.statistic.di.StatisticComponent
import javax.inject.Inject

class StatisticActivity : AppCompatActivity() {
    @Inject
    lateinit var router: Router

    private lateinit var binding: ActivityStatisticBinding

    private val viewModel: StatisticViewModel by viewModels {
        StatisticViewModel.Factory()
    }

    init {
        StatisticComponent.getInstance().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        addEventListener()
        observeState()
        addListeners()
    }

    private fun addEventListener() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is UiEvent.ShowToast -> Toast.makeText(this@StatisticActivity, event.message, Toast.LENGTH_LONG).show()
                        UiEvent.Navigate -> {
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
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                if (state.statisticEntries.first !== binding.barChartHistogram.data) {
                    setHistogram(state.statisticEntries.first, state.statisticEntries.second)
                }
                binding.tabLayoutPeriod.getTabAt(state.mode.tabPosition - 1)
            }
        }
    }

    private fun addListeners() {
        binding.buttonBack.setOnClickListener {
            router.navigateTo(Screen.MainScreen(this))
            finish()
        }

        binding.tabLayoutPeriod.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    PeriodMode.WEEK.tabPosition -> viewModel.changeMode(PeriodMode.WEEK)
                    PeriodMode.MONTH.tabPosition -> viewModel.changeMode(PeriodMode.MONTH)
                    PeriodMode.YEAR.tabPosition -> viewModel.changeMode(PeriodMode.YEAR)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { /* Nothing */
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                viewModel.loadData()
            }
        })

        binding.imagePreviousButton.setOnClickListener {
            viewModel.moveBack()
        }

        binding.imageNextButton.setOnClickListener {
            viewModel.moveForward()
        }
    }

    private fun setHistogram(barData: BarData?, valueFormatter: ValueFormatter?) {
        binding.barChartHistogram.data = barData
        binding.barChartHistogram.notifyDataSetChanged()
        binding.barChartHistogram.invalidate()
        binding.barChartHistogram.xAxis.valueFormatter = valueFormatter
    }

    companion object {
        fun createIntent(fromContext: Context): Intent = Intent(fromContext, StatisticActivity::class.java)
    }
}