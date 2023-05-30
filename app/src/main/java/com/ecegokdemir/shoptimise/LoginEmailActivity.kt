package com.ecegokdemir.shoptimise

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import com.ecegokdemir.shoptimise.databinding.ActivityLoginEmailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.internal.Util

class LoginEmailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginEmailBinding

    private companion object{
        private const val TAG = "LOGIN_TAG"
    }


    private lateinit var firebaseAuth: FirebaseAuth

    //ProgressDialog to show while sign-in
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get instance of firebase auth for Auth related tasks
        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle toolbarBackBtn click, go-back
        binding.toolbarBackBtn.setOnClickListener {
            onBackPressed()
        }


        binding.noAccountTv.setOnClickListener {
            startActivity(Intent(this, RegisterEmailActivity::class.java))
        }

        binding.loginBtn.setOnClickListener {
              validateData()
        }
    }

    private var email = ""
    private var password = ""

    private fun validateData(){

        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        Log.d(TAG,"validateData: email: $email")
        Log.d(TAG,"validateData: password: $password")

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //if email pattern is invalid, show error
            binding.emailEt.error = "Invalid Email format"
            binding.emailEt.requestFocus()
        }
        else if (password.isEmpty()){
            //if password is not entered, show error
            binding.passwordEt.error = "Enter Password"
            binding.passwordEt.requestFocus()
        }
        else{
            //email pattern is valid and password is entered, start login
            loginUser()
        }
    }

    private fun loginUser(){
        Log.d(TAG,"loginUser: ")
        //show progress
        progressDialog.setMessage("Logging In")
        progressDialog.show()

        //start user login
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                //User login success
                Log.d(TAG, "loginUser: Logged In...")
                progressDialog.dismiss()

                //Start MainActivity
                startActivity(Intent(this,MainActivity::class.java))
                finishAffinity() // finish current
            }
            .addOnFailureListener { e ->
                //User login failed
                Log.e(TAG,"LoginUser: ", e)
                progressDialog.dismiss()

                Utils.toast(this,"Unable to login due to ${e.message}")
            }
    }
}




















