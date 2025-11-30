package ru.kulikov.auth.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.kulikov.auth.R
import ru.kulikov.auth.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textSwitchMode.setOnClickListener {
            viewModel.setIsAuth(!viewModel.state.value.isAuth)
        }

        binding.enterButton.setOnClickListener {
            if (viewModel.state.value.isAuth) {
                viewModel.auth()
            } else {
                viewModel.createAccount()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch { observeState() }
    }

    private suspend fun observeState() {
        viewModel.state.collectLatest { state ->
            if (state.isAuth) {
                binding.textAuthorize.setText(R.string.text_authorize)
                binding.textSwitchMode.setText(R.string.text_or_create_account)
                binding.enterButton.setText(R.string.text_authorize)
            } else {
                binding.textAuthorize.setText(R.string.text_create_account)
                binding.textSwitchMode.setText(R.string.text_or_authorize)
                binding.enterButton.setText(R.string.text_create_account)
            }
        }
    }
}