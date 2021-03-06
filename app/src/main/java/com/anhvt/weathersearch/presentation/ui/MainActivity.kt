package com.anhvt.weathersearch.presentation.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anhvt.weathersearch.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainVM: MainVM by viewModels()
    private lateinit var etSearchView: EditText
    private lateinit var btnSubmit: Button
    private lateinit var progressDialog: ProgressDialog
    private lateinit var tvError: TextView
    private lateinit var rvDetails: RecyclerView
    private lateinit var mainAdapter: MainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        observerUiStateChange()
        listenEffect()
    }

    private fun initUI() {
        progressDialog = ProgressDialog(this).apply {
            title = "Loading"
        }
        etSearchView = findViewById(R.id.et_search)
        btnSubmit = findViewById(R.id.btn_submit)
        btnSubmit.setOnClickListener {
            onSubmitClicked()
        }
        tvError = findViewById(R.id.tv_error_message)
        rvDetails = findViewById(R.id.rv_details)
        mainAdapter = MainAdapter()
        rvDetails.apply {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun observerUiStateChange() {
        lifecycleScope.launchWhenStarted {
            mainVM.state.collect { newState ->
                handleStateChanged(newState)
            }
        }
    }

    private fun handleStateChanged(newState: SearchState) {
        toggleLoading(newState.isLoading)
        if (newState.errorMessage != null) {
            rvDetails.visibility = View.GONE
            tvError.apply {
                visibility = View.VISIBLE
                text = newState.errorMessage
            }
        } else {
            rvDetails.apply {
                visibility = View.VISIBLE
                mainAdapter.submitList(newState.weatherList)
            }
            tvError.visibility = View.GONE
        }
    }

    private fun listenEffect() {
        lifecycleScope.launchWhenStarted {
            mainVM.uiEffect.collect { effect ->
                when (effect) {
                    is SearchEffect.NotSufficientLength -> {
                        etSearchView.error = effect.message
                    }
                }
            }
        }
    }

    private fun toggleLoading(isShown: Boolean) {
        if (isShown) {
            if (!progressDialog.isShowing) {
                progressDialog.show()
            }
        } else {
            if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }
        }
    }

    private fun onSubmitClicked() {
        mainVM.search(etSearchView.text.toString())
    }
}