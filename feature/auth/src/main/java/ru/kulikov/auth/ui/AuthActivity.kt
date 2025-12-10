package ru.kulikov.auth.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.kulikov.auth.R
import ru.kulikov.auth.databinding.ActivityAuthBinding
import ru.kulikov.auth.di.AuthComponent
import ru.kulikov.core.utils.base.BaseActivity
import ru.kulikov.core.utils.base.UiEvent
import ru.kulikov.core.utils.router.Router
import ru.kulikov.core.utils.router.Screen
import javax.inject.Inject

class AuthActivity : BaseActivity() {

    @Inject
    lateinit var router: Router

    private lateinit var binding: ActivityAuthBinding
    val viewModel: AuthViewModel by viewModels {
        AuthViewModel.Factory()
    }

    init {
        AuthComponent.getInstance().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnApplyWindowInsets()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is UiEvent.ShowToast -> Toast.makeText(this@AuthActivity, event.message, Toast.LENGTH_LONG).show()
                        UiEvent.InProgress -> {

                        }

                        UiEvent.OutProgress -> {
                        }

                        UiEvent.Navigate -> router.navigateTo(Screen.EnterScreen(this@AuthActivity))
                    }
                }
            }
        }

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
        addTextWatchers()

        loadData()
    }

    private fun addTextWatchers() {
        binding.inputEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.setEmail(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /* Nothing */
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { /* Nothing */
            }

        })

        binding.inputPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.setPass(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /* Nothing */
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { /* Nothing */
            }

        })

        binding.inputConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.setConfirmPass(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /* Nothing */
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { /* Nothing */
            }
        })
    }

    private fun loadData() {
        viewModel.state.value.let { state ->
            setMode(state.isAuth)
            binding.inputEmail.setText(state.email)
            binding.inputPassword.setText(state.pass)
            binding.inputConfirmPassword.setText(state.confirmPass)
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch { observeState() }
    }

    private suspend fun observeState() {
        viewModel.state.collectLatest { state ->
            setMode(state.isAuth)
        }
    }

    private fun setMode(isAuth: Boolean) {
        if (isAuth) {
            binding.textAuthorize.setText(R.string.text_authorize)
            binding.textSwitchMode.setText(R.string.text_or_create_account)
            binding.enterButton.setText(R.string.text_authorize)
            binding.inputConfirmPassword.visibility = View.GONE
        } else {
            binding.textAuthorize.setText(R.string.text_create_account)
            binding.textSwitchMode.setText(R.string.text_or_authorize)
            binding.enterButton.setText(R.string.text_create_account)
            binding.inputConfirmPassword.visibility = View.VISIBLE
        }
    }

    private fun setOnApplyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            v.setPadding(0, 0, 0, ime.bottom)
            insets
        }
    }

    companion object {
        fun createIntent(fromContext: Context): Intent {
            return Intent(fromContext, AuthActivity::class.java)
        }
    }
}