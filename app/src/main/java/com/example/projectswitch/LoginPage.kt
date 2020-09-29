package com.example.projectswitch

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.CheckBox
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login_page.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginPage : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var rememberMe: CheckBox
    lateinit var sharedPreference: SharedPreferences

    lateinit var prgDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        auth = FirebaseAuth.getInstance()

        prgDialog = ProgressDialog(this@LoginPage)

        sharedPreference = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)

        mainLogin.setOnClickListener {

            validateEmail()


            when {
                editTextLoginEmail.text?.isEmpty()!! -> {
                    editTextLoginEmail.error = "Field cannot be empty"
                    editTextLoginEmail.requestFocus()

                }
                editTextLoginPassword.text?.isEmpty()!! -> {
                    editTextLoginPassword.error = "Field cannot be empty"
                    editTextLoginPassword.requestFocus()

                }
                else -> {
                    prgDialog.setTitle("Please wait")
                    prgDialog.setMessage("Logging in")
                    prgDialog.show()
                    loginUser()
                }
            }


        }

    }


    private fun loginUser() {
        val email = editTextLoginEmail.text.toString()
        val password = editTextLoginPassword.text.toString()
        rememberMe = findViewById(R.id.loginCheckBox)

        if (rememberMe.isChecked) {

            val editor = sharedPreference.edit()

            editor.remove("")
            editor.putString("email", email)
            editor.putString("password", password)

            editor.apply()

        }


        if (email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {
                        checkLoggedInState()

                        Toast.makeText(this@LoginPage, "Welcome", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@LoginPage,
                            "Check your internet connection",
                            Toast.LENGTH_LONG
                        ).show()
                        prgDialog.dismiss()

                    }
                }
            }
        }


    }

    private fun checkLoggedInState() {
        if (auth.currentUser == null) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_LONG).show()
        } else {
            val intent = Intent(this, Homepage::class.java)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    fun createNewAccount(view: View) {
        val intent = Intent(this, RegisterPage::class.java)
        startActivity(intent)
    }

    fun rememberMe(view: View) {
        val box = findViewById<CheckBox>(R.id.loginCheckBox)
        box.toggle()
    }

    private fun validateEmail(): Boolean {

        val email = editTextLoginEmail.text.toString()

        when {
            email.isEmpty() -> {
                editTextLoginEmail.error = "Field cannot be empty"
                prgDialog.dismiss()
                return false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                editTextLoginEmail.error = "Please enter a valid email"
                prgDialog.dismiss()
                return false
            }
            else -> {
                editTextLoginEmail.error = null
                return true
            }
        }


    }




}