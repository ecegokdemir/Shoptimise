package com.ecegokdemir.shoptimise

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import com.ecegokdemir.shoptimise.databinding.ActivityRegisterEmailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.internal.Util

class RegisterEmailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterEmailBinding

    private companion object{
        private const val TAG = "Register_TAG"
    }

    private lateinit var firebaseAuth: FirebaseAuth

    //ProgressDialog to show while sign-up
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get instance of firebase auth for Auth related tasks
        firebaseAuth = FirebaseAuth.getInstance()

        //init/setup ProgressDialog to show while sign-up
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle toolbarBackBtn click, go-back
        binding.toolbarBackBtn.setOnClickListener {
            onBackPressed()
        }


        binding.haveAccountTv.setOnClickListener {
            onBackPressed()
        }

        binding.registerBtn.setOnClickListener {
            validateData()
        }
    }

    private var email = ""
    private var password = ""
    private var cPassword = ""

    private fun validateData(){

        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()
        cPassword = binding.cPasswordEt.text.toString().trim()

        Log.d(TAG,"validateData: email: $email")
        Log.d(TAG,"validateData: password: $password")
        Log.d(TAG,"validateData: confirm password: $cPassword")

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //if email pattern is invalid, show error
            binding.emailEt.error = "Invalid Email Pattern"
            binding.emailEt.requestFocus()
        }
        else if (password.isEmpty()){
            //if password is not entered, show error
            binding.passwordEt.error = "Enter Password"
            binding.passwordEt.requestFocus()
        }
        else if (cPassword.isEmpty()){
            //if confirm password is not entered, show error
            binding.cPasswordEt.error = "Enter Confirm Password"
            binding.cPasswordEt.requestFocus()
        }
        else if (password != cPassword){
            //if password and confirm password is mot same, show error
            binding.cPasswordEt.error = "Password Doesn't Match "
            binding.cPasswordEt.requestFocus()
        }
        else{
            //all data is valid,start sign-up
                registerUser()
        }
    }

    private fun registerUser(){
        //show progress
        Log.d(TAG,"registerUser: ")
        //show progress
        progressDialog.setMessage("Creating account")
        progressDialog.show()

        //start user sign-up
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                //User Register success, We also need to save user to firebase db
                Log.d(TAG, "registerUser: Register Success")
                updateUserInfo()

            }
            .addOnFailureListener { e ->
                //User register failed
                Log.e(TAG,"registerUser: ", e)
                progressDialog.dismiss()
                Utils.toast(this,"Failed to create account due to ${e.message}")
            }
    }

    private fun updateUserInfo(){
        Log.d(TAG, "updateUserInfo: ")
        //change progress dialog message
        progressDialog.setMessage("Saving User Info")

        //get current timestamp
        val timestamp = Utils.getTimestamp()
        val registeredUserEmail = firebaseAuth.currentUser!!.email
        val registeredUserUid = firebaseAuth.uid

        //setup data to save in firebase realtime db.
        //Most of data will be empty and will set in edit profile
        val hashmap = HashMap<String, Any>()
        hashmap["name"] = ""
        hashmap["phoneCode"] = ""
        hashmap["phoneNumber"] = ""
        hashmap["profileImageUrl"] = ""
        hashmap["dob"] = ""
        hashmap["userType"] = "Email" // possible values email/google
        hashmap["typingTo"] = ""
        hashmap["timestamp"] = timestamp
        hashmap["onlineStatus"] = true
        hashmap["email"] = "$registeredUserEmail"
        hashmap["uid"] = "$registeredUserUid"

        //set data to frease db
        val reference = FirebaseDatabase.getInstance().getReference("Users")
        reference.child(registeredUserUid!!)
            .setValue(hashmap)
            .addOnSuccessListener {
                //Firebase db save success
                Log.d(TAG, "updateUserInfo: User registered...")
                progressDialog.dismiss()
                //Start MainActivity
                startActivity(Intent(this,MainActivity::class.java))
                finishAffinity()//finish current and all activities
            }
            .addOnFailureListener { e ->
                //Firebase db save failed
                Log.d(TAG, "updateUserInfo: ", e)
                progressDialog.dismiss()
                Utils.toast(this,"Failed to save user info due to ${e.message}")
            }





    }
}














