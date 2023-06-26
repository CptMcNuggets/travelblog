package com.example.travelblog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceDataStore
import android.provider.ContactsContract.Data
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.preferencesDataStore
import com.example.travelblog.databinding.ActivityLoginBinding
import com.google.android.material.textfield.TextInputLayout
import java.util.prefs.Preferences


class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginPreferences: LoginPreferences by lazy {
        LoginPreferences(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(loginPreferences.isLoggedIn()) {
            startMainActivity()
            finish()
            return
        }
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginButton.setOnClickListener(object : OnClickListener{
            override fun onClick(v: View?) {
                onLoginClicked()
            }
        })
        binding.inputLoginLayout.editText?.addTextChangedListener(createTextWatcher(binding.inputLoginLayout))
        binding.inputPasswordLayout.editText?.addTextChangedListener(createTextWatcher(binding.inputPasswordLayout))
    }
    private fun onLoginClicked() {
        var username: String = binding.inputLoginText.text.toString()
        var password: String = binding.inputPasswordText.text.toString()
        if(username.isEmpty() || password.isEmpty()) {
            if(username.isEmpty()) {
                binding.inputLoginLayout.error = "Username must not be empty"
            }
            if(password.isEmpty()) {
                binding.inputPasswordLayout.error = "Password must not be empty"
            }
        }
        else if (username != "admin" || password != "admin") {
            showErrorDialog()
        }
        else {
            performLogin()
        }
    }
    private fun createTextWatcher(inputLayout: TextInputLayout): TextWatcher {
        return object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                inputLayout.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
    }
    private fun showErrorDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.error_login_title)
            .setMessage(R.string.error_login_message)
            .setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss()}
            .show()
    }
    private fun performLogin() {
        loginPreferences.setLoggedIn(loggedIn = true)
        binding.loginButton.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE
        binding.inputLoginLayout.isEnabled = false
        binding.inputPasswordLayout.isEnabled = false
        Handler().postDelayed({ startMainActivity()
                              finish()
                              }, 2000)
    }
    private fun startMainActivity() {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}