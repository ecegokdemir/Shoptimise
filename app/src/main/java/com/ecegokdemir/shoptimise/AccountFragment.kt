package com.ecegokdemir.shoptimise

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ecegokdemir.shoptimise.databinding.FragmentAccountBinding



class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding

    private companion object{
        private const val TAG ="ACCOUNT_TAG"
    }

    //Context Auth for auth related tasks
    private lateinit var mContext: Context

    //Firebase Auth for auth related tasks
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onAttach(context: Context) {
        //get and init the context for this fragment class
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get instance of firebase auth for Auth related tasks
        firebaseAuth = FirebaseAuth.getInstance()


        loadMyInfo()

        binding.logoutCv.setOnClickListener{
            firebaseAuth.signOut()//logout user
            //start MainActivity
            startActivity(Intent(mContext, MainActivity::class.java))
            activity?.finishAffinity()
        }

        binding.editProfileCv.setOnClickListener {
            startActivity(Intent(mContext , ProfileEditActivity::class.java))
        }

    }

    private fun loadMyInfo(){

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child("${firebaseAuth.uid}")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get user info
                    val dob = "${snapshot.child("dob").value}"
                    val email = "${snapshot.child("email").value}"
                    val name = "${snapshot.child("name").value}"
                    val phoneCode = "${snapshot.child("phoneCode").value}"
                    val phoneNumber = "${snapshot.child("phoneNumber").value}"
                    val profileImageUrl = "${snapshot.child("profileImageUrl").value}"
                    var timestamp = "${snapshot.child("timestamp").value}"
                    val userType = "${snapshot.child("userType").value}"

                    //concatenate phone code and phone number to make full phone number
                    val phone = phoneCode+phoneNumber

                    //to avoid null or format exceptions
                    if(timestamp == "null"){
                        timestamp = "0"
                    }
                    //format timestamp to dd/NN/yyyy
                    val formattedDate = Utils.formatTimestampDate(timestamp.toLong())

                    //set data to UI
                    binding.emailTv.text = email
                    binding.nameTv.text = name
                    binding.dobTv.text = dob
                    binding.phoneTv.text = phone
                    binding.memberSinceTv.text = formattedDate

                    if(userType == "Email"){

                        //UserType is Email, have to check if verified or not
                        val isVerified = firebaseAuth.currentUser!!.isEmailVerified

                        if(isVerified){
                            //Verified
                            binding.verificationTv.text = "Verified"
                        }else{
                            //Not Verified
                            binding.verificationTv.text = "Not Verified"
                        }
                    }else{
                        //UserType is Google or phone, no need to chech if verified or not as it is already verified
                        binding.verificationTv.text = "Verified"
                    }

                    //Set profile image to profileIv
                    try {
                        Glide.with(mContext)
                            .load(profileImageUrl)
                            .placeholder(R.drawable.icon_person_white)
                            .into(binding.profileIv)
                    }
                    catch (e : Exception){
                        //Failed to get/set image show exception in log
                        Log.e(TAG, "onDataChange: ", e)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }


}































