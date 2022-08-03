package com.pedrogomez.hnmob.utils.extensions

import android.content.Context
import android.util.Log
import android.widget.Toast
import java.lang.Exception

fun String?.isValid() = this != null && this.isNotEmpty() && !this.equals("null",true)

fun Any?.print(){
    //Log.d("LogTagKoin", " $this")
}

fun shortToast(context: Context,msg:String){
    try{
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }catch (e: Exception){

    }
}