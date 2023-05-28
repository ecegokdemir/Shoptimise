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


        binding.bottomNavigation.setOnItemSelectedListener {
            item ->
            when(item.itemId){
                R.id.menu_home -> {
                    // Home item clicked, show HomeFragment

                    true
                }
                R.id.menu_chats -> {
                    // Chats item clicked, show ChatsFragment

                    true
                }
                R.id.menu_my_ads -> {
                    // My Ads item clicked, show MyAdsFragment

                    true
                }
                R.id.menu_account -> {
                    // Account item clicked, show AccountFragment

                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}