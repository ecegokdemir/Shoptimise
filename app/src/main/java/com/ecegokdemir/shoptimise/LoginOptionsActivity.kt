package com.ecegokdemir.shoptimise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ecegokdemir.shoptimise.databinding.ActivityLoginOptionsBinding

class LoginOptionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginOptionsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityLoginOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.closeBtn.setOnClickListener {
            onBackPressed()
        }
    }
}