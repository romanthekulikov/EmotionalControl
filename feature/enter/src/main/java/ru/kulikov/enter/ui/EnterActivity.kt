package ru.kulikov.enter.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.kulikov.core.utils.router.Router
import ru.kulikov.enter.R
import ru.kulikov.enter.di.EnterComponent
import javax.inject.Inject

class EnterActivity : AppCompatActivity() {

    @Inject
    lateinit var router: Router

    init {
        EnterComponent.getInstance().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_enter)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    companion object {
        fun createIntent(fromContext: Context): Intent = Intent(fromContext, EnterActivity::class.java)
    }
}