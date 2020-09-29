package com.example.projectswitch

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login_page.*
import kotlinx.android.synthetic.main.activity_register_page.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.regex.Pattern

class RegisterPage : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    lateinit var prgDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)

        auth = FirebaseAuth.getInstance()

        prgDialog = ProgressDialog(this@RegisterPage)

        btnRegisterBulb.setOnClickListener {

            validateEmail()

            when {
                editTextPassword.text?.isEmpty()!! -> {
                    editTextPassword.error = "Field cannot be empty"
                    editTextPassword.requestFocus()

                }
                editTextEmail.text?.isEmpty()!! -> {
                    editTextEmail.error = "Field cannot be empty"
                    editTextEmail.requestFocus()
                }
                else -> {
                    prgDialog.setTitle("Please wait")
                    prgDialog.setMessage("Setting up your account")
                    prgDialog.show()
                    registerUser()
                }
            }


        }

    }


    private fun registerUser() {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()



        if (email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {
                        checkLoggedInState()

                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@RegisterPage,
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
            Toast.makeText(this, "You are not logged in", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "You are successfully registered!!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }
    }

    fun signInOption(view: View) {
        val intent = Intent(this, LoginPage::class.java)
        startActivity(intent)

    }

    private fun validateEmail(): Boolean {
        val email = editTextEmail.text.toString()

        when {
            email.isEmpty() -> {
                editTextEmail.error = "Field cannot be empty"
                prgDialog.dismiss()
                return false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                editTextEmail.error = "Please enter a valid email"
                prgDialog.dismiss()
                return false
            }
            else -> {
                editTextEmail.error = null
                return true
            }
        }

    }

    fun acceptTheTerm(view: View) {
        val box = findViewById<CheckBox>(R.id.checkBox)
        box.toggle()

    }


}
