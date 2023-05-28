package com.ecegokdemir.shoptimise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.ecegokdemir.shoptimise.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //activity_main.xml = ActivityMainBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

}








