package com.example.permission

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.permission.databinding.ActivityMainBinding
import java.net.URI

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.setTextButton.setOnClickListener {
            /* Glide
                 .with(this)
                 .load(AppCompatResources.getDrawable(this, R.drawable.ic_android_black_24dp))
                 .into(binding.imageView)*/

            //request permission from Gallery
            if (permissionPass()) {
                //go to Gallery
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 1)
            } else Toast.makeText(this, "دسترسی نیاز است", Toast.LENGTH_SHORT).show()
        }
    }

    private fun permissionPass(): Boolean {
        val listPermissionWeNeed = mutableListOf<String>()
        val storagePermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionWeNeed.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (listPermissionWeNeed.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionWeNeed.toTypedArray(), 9)
            return false
        }
        return true
    }

    /*override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1)
            permissionPass()
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            val imagePath = data?.data
            Glide.with(this).load(imagePath).into(binding.imageView)
            showNotification("این یک عنوان است", "تصویر تنظیم شد.")
        }
    }


    //Create a Notification
    private fun showNotification(title: String, description: String) {
        val channelId = "notification"
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(description)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = 
                NotificationChannel(channelId, "One", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notification.build())
    }

}