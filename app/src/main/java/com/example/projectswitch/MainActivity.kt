package com.example.projectswitch

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login_page.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    lateinit var sharedPreference: SharedPreferences

    lateinit var prgDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        sharedPreference = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)

         prgDialog = ProgressDialog(this@MainActivity)


        if (sharedPreference.contains("email")){
            val email = sharedPreference.getString("email", " ")!!
            val password = sharedPreference.getString("password", " ")!!

            prgDialog.setTitle("Please wait")
            prgDialog.setMessage("Logging in")
            prgDialog.show()

            loginUser(email, password)
        }

       btnSignUp.setOnClickListener {
           val intent = Intent(this,RegisterPage::class.java)
           startActivity(intent)
       }

        firstLoginBtn.setOnClickListener {
            val intent = Intent(this,LoginPage::class.java)
            startActivity(intent)
        }

    }


    private fun loginUser(email: String, password:String){

        val editor = sharedPreference.edit()
        editor.putString(email,email)
        editor.putString(password,password)

        editor.apply()

        if (email.isNotEmpty() && password.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email,password).await()
                    withContext(Dispatchers.Main){
                        checkLoggedInState()

                        Toast.makeText(this@MainActivity, "Welcome", Toast.LENGTH_SHORT).show()

                    }
                }catch (e:Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MainActivity,"Check your internet connection", Toast.LENGTH_LONG).show()
                        prgDialog.dismiss()

                    }
                }
            }
        }
    }

    private fun checkLoggedInState(){
        if (auth.currentUser == null){
            Toast.makeText(this, "Register as a new user", Toast.LENGTH_LONG).show()
        }else{
            val intent = Intent(this,Homepage::class.java)
            startActivity(intent)
        }

    }




}