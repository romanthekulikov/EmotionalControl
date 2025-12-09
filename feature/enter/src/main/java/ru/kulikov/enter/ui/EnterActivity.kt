package ru.kulikov.enter.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.kulikov.core.utils.base.UiEvent
import ru.kulikov.core.utils.router.Router
import ru.kulikov.core.utils.router.Screen
import ru.kulikov.enter.R
import ru.kulikov.enter.databinding.ActivityEnterBinding
import ru.kulikov.enter.di.EnterComponent
import javax.inject.Inject

class EnterActivity : AppCompatActivity() {

    @Inject
    lateinit var router: Router

    private lateinit var binding: ActivityEnterBinding
    private val viewModel: EnterViewModel by viewModels {
        EnterViewModel.Factory()
    }

    init {
        EnterComponent.getInstance().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        addListeners()
        addEventListener()
        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                if (state.partnerId.toString() != binding.inputPartnerId.text.toString()) binding.inputPartnerId.setText(state.partnerId.toString())
                binding.textYourId.text = "Твой id: ${state.userId}"
            }
        }
    }

    private fun addEventListener() {
        lifecycleScope.launch {
            viewModel.events.collectLatest { event ->
                when (event) {
                    is UiEvent.ShowToast -> Toast.makeText(this@EnterActivity, event.message, Toast.LENGTH_LONG).show()
                    UiEvent.Navigate -> {
                        Toast.makeText(this@EnterActivity, "Добро пожаловать!", Toast.LENGTH_LONG).show()
                        router.navigateTo(Screen.MainScreen(this@EnterActivity))
                    }
                }
            }
        }
    }

    private fun addListeners() {
        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.inputPartnerId.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val intNumber = s.toString().toIntOrNull()
                if (intNumber == null && s.toString().isNotEmpty()) {
                    binding.inputPartnerId.error = "Можно только цифры!!"
                    Toast.makeText(this@EnterActivity, "Можно только цифры!!", Toast.LENGTH_SHORT).show()
                } else if (intNumber != null && s.toString().isNotEmpty()) {
                    binding.inputPartnerId.error = null
                    viewModel.setPartnerId(intNumber)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /* Nothing */
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { /* Nothing */
            }

        })

        binding.buttonEnter.setOnClickListener {
            viewModel.enter()
        }
    }

    companion object {
        fun createIntent(fromContext: Context): Intent = Intent(fromContext, EnterActivity::class.java)
    }
}