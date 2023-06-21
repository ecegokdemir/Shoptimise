package com.ecegokdemir.shoptimise

import android.content.Context
import android.text.format.DateFormat
import android.widget.Toast
import java.util.*

// A class that will contain static functions, constants, variables that I will be used in whole application
object Utils {

    val categories = arrayOf(
        "All",
        "Mobiles",
        "Computer",
        "Electronics",
        "Vehicles",
        "Furniture",
        "Fashion",
        "Books",
        "Sports"
    )

    val categoryIcons = arrayOf(
        R.drawable.icon_category_all,
        R.drawable.icon_category_mobiles,
        R.drawable.icon_category_computer,
        R.drawable.icon_electronics,
        R.drawable.icon_category_vehicles,
        R.drawable.icon_category_furniture,
        R.drawable.icon_category_fashion,
        R.drawable.icon_category_books,
        R.drawable.icon_category_sports

    )

   // A function to show Toast
   fun toast(context: Context,message: String){
       Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
   }

    // A function to get current timestamp
    fun getTimestamp() : Long {
        return  System.currentTimeMillis()
    }

    fun formatTimestampDate(timestamp: Long) : String{
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp

        return DateFormat.format("dd/MM/yyyy", calendar).toString()
    }

}