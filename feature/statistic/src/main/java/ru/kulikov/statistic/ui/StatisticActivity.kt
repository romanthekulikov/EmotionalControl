package ru.kulikov.statistic.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.kulikov.core.utils.router.Router
import ru.kulikov.statistic.R
import ru.kulikov.statistic.di.StatisticComponent
import javax.inject.Inject

class StatisticActivity : AppCompatActivity() {

    @Inject
    lateinit var router: Router

    init {
        StatisticComponent.getInstance().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.statistic_enter)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    companion object {
        fun createIntent(fromContext: Context): Intent = Intent(fromContext, StatisticActivity::class.java)
    }
}