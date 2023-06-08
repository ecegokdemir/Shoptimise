package com.ecegokdemir.shoptimise

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.ecegokdemir.shoptimise.databinding.ActivityLoginOptionsBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class LoginOptionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginOptionsBinding

    private companion object{
        private const val TAG = "LOGIN_OPTIONS_TAG"
    }

    private lateinit var progressDialog : ProgressDialog

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var mGoogleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityLoginOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()

        //Configure Google Sign In Options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso)



        //handle closeBtn click, go-back
        binding.closeBtn.setOnClickListener {
            onBackPressed()
        }

        //handle loginEmailBtn click, open LoginEmailActivity to login with Email & Password
        binding.loginEmailBtn.setOnClickListener {
            startActivity(Intent(this,LoginEmailActivity::class.java))
        }

        //handle loginGoogleBtn click, begin google signIn
        binding.loginGoogleBtn.setOnClickListener {
            beginGoogleLogin()
        }
    }

    private fun beginGoogleLogin(){
        Log.d(TAG, "beginGoogleLogin: ")
        //intent to launch google Sign In Options Dialog
        val googleSignInIntent = mGoogleSignInClient.signInIntent
        googleSignInARL.launch(googleSignInIntent)

    }
    private val googleSignInARL = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result ->

        Log.d(TAG, "googleSignInARL: ")

        if(result.resultCode == RESULT_OK){

            val data = result.data
            //Task to get the GoogleSignInAccount from intent
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

           try {
               //get the GoogleSignInAcoount from intent
                val account = task.getResult(ApiException::class.java)
               Log.d(TAG, "googleSignInARL: Account ID: ${account.id}")
                //SignIn Success, let's signIn with Firebase Auth
                firebaseAuthWithGoogleAccount(account.idToken)
           }
           catch ( e: Exception){//Google SignIn Failed, show exception
               Log.e(TAG, "googleSignInARL: ",e )
               Utils.toast(this, "${e.message}")
           }
        }
        else{
            //User has cancelled Google SignIn
            Utils.toast(this,"Cancelled...!")
        }
    }

    private fun firebaseAuthWithGoogleAccount(idToken: String?){
        Log.d(TAG, "firebaseAuthWithGoogleAccount: idToken: $idToken")

        val credential = GoogleAuthProvider.getCredential(idToken,null)
        //SignIn in to firebase auth using Google Credentials
        firebaseAuth.signInWithCredential(credential)

            .addOnSuccessListener { authResult ->

                if (authResult.additionalUserInfo!!.isNewUser){
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: New User, Account created...")
                    //New User, Account created. Let's save user info to firebase realtime database
                    updateUserInfoDb()
                }else{
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Existing User, Logged In...")
                    //existing User, Signed In. No need to save user info to firebase realtime database, Start MainActivity
                    startActivity(Intent(this,MainActivity::class.java))
                    finishAffinity()
                }
            }
            .addOnFailureListener{e ->
                Log.e(TAG, "firebaseAuthWithGoogleAccount: ", e)
                Utils.toast(this,"${e.message}")
            }
    }

    private fun updateUserInfoDb(){
        Log.d(TAG, "updateUserInfoDb: ")
        //set message and show progress dialog
        progressDialog.setMessage("Saving User Info")
        progressDialog.show()

        //get current timestamp e.g. to show user registration date/time
        val timestamp = Utils.getTimestamp()
        val registeredUserEmail = firebaseAuth.currentUser?.email //get email of registered user
        val registeredUserUid = firebaseAuth.uid //get uid of registered user
        val name = firebaseAuth.currentUser?.displayName //Since each Google user has name so we can get it to save in firebase db

        //setup data to save in firebase realtime db, most of the data will be empty and will set in edit profile
        val hashMap = HashMap<String, Any?>()
        hashMap["name"] = "$name"
        hashMap["phoneCode"] = ""
        hashMap["phoneNumber"] = ""
        hashMap["profileImageUrl"] = ""
        hashMap["dob"] = ""
        hashMap["userType"] = "Google" // possible values Email/Google
        hashMap["typingTo"] = ""
        hashMap["timestamp"] = timestamp
        hashMap["onlineStatus"] = true
        hashMap["email"] = registeredUserEmail
        hashMap["uid"] = registeredUserUid


        //set data to direbase db
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(registeredUserUid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                Log.d(TAG, "updateUserInfoDb: User Info saved")
                progressDialog.dismiss()


                startActivity(Intent(this,MainActivity::class.java))
                finishAffinity()
            }
            .addOnFailureListener{ e ->
                progressDialog.dismiss()
                Log.e(TAG, "updateUserInfoDb: ", e)
                Utils.toast(this,"Failed to save user Info due to ${e.message}")
            }


    }

}













