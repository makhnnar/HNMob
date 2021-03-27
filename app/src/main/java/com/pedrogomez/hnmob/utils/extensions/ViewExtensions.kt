package com.pedrogomez.hnmob.utils.extensions

import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View

fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.remove(){
    this.visibility = View.GONE
}

fun View.hide(){
    this.visibility = View.INVISIBLE
}

fun View.getColor(color:Int):Int{
    return if(Build.VERSION.SDK_INT>=23){
        this.resources.getColor(color,null)
    }else{
        this.resources.getColor(color)
    }
}

fun View.getDrawable(res:Int):Drawable{
    return if(Build.VERSION.SDK_INT>=23){
        this.resources.getDrawable(res,null)
    }else{
        this.resources.getDrawable(res)
    }
}