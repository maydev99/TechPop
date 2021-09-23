package com.bombadu.techpop.util

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.bombadu.techpop.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


object Utils  {


    fun getTimeStamp(): String {
        return (System.currentTimeMillis() / 1000).toString()
    }



    fun getImageUri(context: Context, image: Bitmap): Uri? {
        val imagePath = File(context.cacheDir, "images")
        val newFile = File(imagePath, "image.png")
        val contentUri =
            FileProvider.getUriForFile(context, "com.example.myapp.fileprovider", newFile)

        try {
            val cachePath = File(context.cacheDir, "images")
            cachePath.mkdirs() // don't forget to make the directory
            val stream =
                FileOutputStream("$cachePath/image.png") // overwrites this image every time
            image.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return contentUri

    }

    fun convertTimestampToDate(timeStamp: String) : String {
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = timeStamp.toLong() * 1000
        return android.text.format.DateFormat.format("E, M.d.yyyy hh:mm:ss a", calendar).toString()
    }

    fun convertServerDateToDate(date: String) : String? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val myDate = dateFormat.parse(date)
        val formatter = SimpleDateFormat("E, M.d.yyyy", Locale.getDefault())
        return formatter.format(myDate!!)
    }

    fun convertDaysToTimestampTime(days: Int) : Long {
        return days.toLong() * 86400
    }

    @SuppressLint("SetTextI18n")
    fun showAboutDialog(context: Context, version: String, buildDate: String) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.about_dialog_layout)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val versionTextView = dialog.findViewById<TextView>(R.id.about_version_text_view)
        val buildDateTextView = dialog.findViewById<TextView>(R.id.about_build_date_text_view)
        versionTextView.text = "TechPop $version"
        buildDateTextView.text = "Build Date: $buildDate"
        dialog.show()

    }

    fun makeAToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun sendToWebPage(context: Context, webUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(webUrl)
        context.startActivity(intent)
    }


}