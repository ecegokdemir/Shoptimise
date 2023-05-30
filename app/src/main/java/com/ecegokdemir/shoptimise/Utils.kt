package com.ecegokdemir.shoptimise

import android.content.Context
import android.widget.Toast

// A class that will contain static functions, constants, variables that I will be used in whole application
object Utils {

   // A function to show Toast
   fun toast(context: Context,message: String){
       Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
   }

    // A function to get current timestamp
    fun getTimestamp() : Long {
        return  System.currentTimeMillis()
    }

}