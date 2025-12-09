package com.example.profile.ui

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
import com.example.profile.R
import com.example.profile.databinding.ActivityProfileBinding
import com.example.profile.di.ProfileComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.kulikov.core.utils.base.UiEvent
import ru.kulikov.core.utils.router.Router
import javax.inject.Inject

class ProfileActivity : AppCompatActivity() {

    @Inject
    lateinit var router: Router

    private lateinit var binding: ActivityProfileBinding

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModel.Factory()
    }

    init {
        ProfileComponent.getInstance().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        addEventListener()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        observeState()
        addListeners()

    }

    private fun addEventListener() {
        lifecycleScope.launch {
            viewModel.events.collectLatest { event ->
                when (event) {
                    is UiEvent.ShowToast -> Toast.makeText(this@ProfileActivity, event.message, Toast.LENGTH_LONG).show()
                    UiEvent.Navigate -> { /* Nothing */
                    }
                }
            }
        }
    }

    private fun addListeners() {
        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.inputName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.setName(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /* Nothing */
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { /* Nothing */
            }
        })

        binding.saveButton.setOnClickListener {
            viewModel.setUser()
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                if (state.name != binding.inputName.text.toString()) binding.inputName.setText(state.name)
                if (state.userId.toString() != binding.textUserId.text) binding.textUserId.text = state.userId.toString()
            }
        }
    }

    companion object {
        fun createIntent(fromContext: Context): Intent = Intent(fromContext, ProfileActivity::class.java)
    }
}