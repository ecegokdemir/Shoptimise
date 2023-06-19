package com.ecegokdemir.shoptimise

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.ecegokdemir.shoptimise.databinding.ActivityProfileEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileEditBinding

    private companion object{
        private const val TAG ="PROFILE_EDIT_TAG"
    }

    //Firebase Auth for auth related tasks
    private lateinit var firebaseAuth: FirebaseAuth

    //ProgressDialog to show while profile update
    private lateinit var progressDialog: ProgressDialog

    private var myUserType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        //get instance of firebase auth for Auth releated tasks
        firebaseAuth = FirebaseAuth.getInstance()
        loadMyInfo()

        //handle toolbarBackBtn click, go-back
        binding.toolbarBackBtn.setOnClickListener {
            onBackPressed()
        }

        binding.updateBtn.setOnClickListener {
            validateData()
        }
    }

    private var name = ""
    private var dob = ""
    private var email = ""
    private var phone = ""


    private fun validateData(){
        //input data
        name = binding.nameEt.text.toString().trim()
        dob = binding.dobEt.text.toString().trim()
        email = binding.emailEt.text.toString().trim()
        phone = binding.phoneEt.text.toString().trim()

        updateProfileDb()
    }

    private fun updateProfileDb(){
        Log.d(TAG, "updateProfileDb: ")
        //show progress
        progressDialog.setMessage("Updating user info")
        progressDialog.show()
        //setup data in hashmap to update to firebase db
        val hashMap = HashMap<String,Any>()
        hashMap["name"] = "$name"
        hashMap["dob"] = "$dob"
        hashMap["phoneNumber"] = "$phone"

        //Database reference of user to update info
        val reference = FirebaseDatabase.getInstance().getReference("Users")
        reference.child("${firebaseAuth.uid}")
            .updateChildren(hashMap)
            .addOnSuccessListener {
                Log.d(TAG, "updateProfileDb: Updated...")
                progressDialog.dismiss()
                Utils.toast(this,"Updated...")
            }
            .addOnFailureListener {e ->
                Log.e(TAG, "updateProfileDb: ", e)
                progressDialog.dismiss()
                Utils.toast(this,"Failed to update due to ${e.message}")
            }
    }


    private fun loadMyInfo(){
        Log.d(TAG, "loadMyInfo: ")

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child("${firebaseAuth.uid}")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get user info
                    val dob = "${snapshot.child("dob").value}"
                    val email = "${snapshot.child("email").value}"
                    val name = "${snapshot.child("name").value}"
                    val phone = "${snapshot.child("phoneNumber").value}"
                    val profileImageUrl = "${snapshot.child("profileImageUrl").value}"
                    var timestamp = "${snapshot.child("timestamp").value}"
                    myUserType = "${snapshot.child("userType").value}"


                    if(myUserType.equals("Email",true) || myUserType.equals("Google",true)){

                        binding.emailTil.isEnabled = false
                        binding.emailEt.isEnabled = false
                    }
                    //set data to UI
                    binding.emailEt.setText(email)
                    binding.dobEt.setText(dob)
                    binding.nameEt.setText(name)
                    binding.phoneEt.setText(phone)

                    //Set profile image to profileIv
                    try {
                        Glide.with(this@ProfileEditActivity)
                            .load(profileImageUrl)
                            .placeholder(R.drawable.icon_person_white)
                            .into(binding.profileIv)
                    }
                    catch (e: Exception){
                        Log.e(TAG, "onDataChange: ", e)
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })

    }

}



























