package com.ecegokdemir.shoptimise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.ecegokdemir.shoptimise.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    //View Binding
    private lateinit var binding: ActivityMainBinding

    //Firebase auth for auth related tasks
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //activity_main.xml = ActivityMainBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get instance of firebase auth for Auth related tasks
        firebaseAuth = FirebaseAuth.getInstance()
        //check if user is logged in or not
        if(firebaseAuth.currentUser == null){
            startLoginOptions()
        }

        //By default show HomeFragment
        showHomeFragment()

        binding.bottomNavigation.setOnItemSelectedListener {
            item ->
            when(item.itemId){
                R.id.menu_home -> {
                    // Home item clicked, show HomeFragment
                    showHomeFragment()

                    true
                }
                R.id.menu_chats -> {
                    // Chats item clicked, show ChatsFragment
                    showChatsFragment()

                    true
                }
                R.id.menu_my_ads -> {
                    // My Ads item clicked, show MyAdsFragment
                    showMyAdsFragment()

                    true
                }
                R.id.menu_account -> {
                    // Account item clicked, show AccountFragment
                    showAccountFragment()

                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun showHomeFragment(){
        //change toolbar textView text to Home
        binding.toolbarTitleTv.text = "Home"
        //Show HomeFragment
        val fragment = HomeFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.FragmentsFL.id,fragment,"HomeFragment")
        fragmentTransaction.commit()
    }

    private fun showChatsFragment(){
        //change toolbar textView text to Chats
        binding.toolbarTitleTv.text = "Chats"
        //Show ChatsFragment
        val fragment = ChatsFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.FragmentsFL.id,fragment,"ChatsFragment")
        fragmentTransaction.commit()
    }

    private fun showMyAdsFragment(){
        //change toolbar textView text to MyAds
        binding.toolbarTitleTv.text = "MyAds"
        //Show MyAdsFragment
        val fragment = MyAdsFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.FragmentsFL.id,fragment,"MyAdsFragment")
        fragmentTransaction.commit()
    }

    private fun showAccountFragment(){
        //change toolbar textView text to Account
        binding.toolbarTitleTv.text = "Account"
        //Show AccountFragment
        val fragment = AccountFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.FragmentsFL.id,fragment,"AccountFragment")
        fragmentTransaction.commit()
    }

    private fun startLoginOptions(){
        startActivity(Intent(this,LoginOptionsActivity::class.java))
    }

}








