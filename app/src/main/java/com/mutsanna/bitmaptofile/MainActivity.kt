package com.mutsanna.bitmaptofile

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.Bitmap
import android.widget.Toast
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the bitmap from assets and display into image view
        val bitmap = assetsToBitmap("tulip.jpg")
        // If bitmap is not null
        bitmap?.let {
            image_view_bitmap.setImageBitmap(bitmap)
        }


        // Click listener for button widget
        button.setOnClickListener{
            if(bitmap!=null){
                // Save the bitmap to a file and display it into image view
                val uri = bitmapToFile(bitmap)
                image_view_file.setImageURI(uri)

                // Display the saved bitmap's uri in text view
                text_view.text = uri.toString()

                // Show a toast message
                toast("Bitmap saved in a file.")
            }else{
                toast("bitmap not found.")
            }
        }
    }


    // Method to get a bitmap from assets
    private fun assetsToBitmap(fileName:String):Bitmap?{
        return try{
            val stream = assets.open(fileName)
            BitmapFactory.decodeStream(stream)
        }catch (e: IOException){
            e.printStackTrace()
            null
        }
    }


    // Method to save an bitmap to a file
    private fun bitmapToFile(bitmap: Bitmap): Uri {
        // Get the context wrapper
        val wrapper = ContextWrapper(applicationContext)

        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file,"${UUID.randomUUID()}.jpg")

        try{
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
        }catch (e:IOException){
            e.printStackTrace()
        }

        // Return the saved bitmap uri
        return Uri.parse(file.absolutePath)
    }
}



// Extension function to show toast message
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}