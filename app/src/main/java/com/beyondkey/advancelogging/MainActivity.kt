package com.beyondkey.advancelogging

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.beyondkey.advance_logging.LogUtil


class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.name
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnDeleteLogs = findViewById<Button>(R.id.btnDeleteLogs)

        btnDeleteLogs.setOnClickListener{
            LogUtil.deleteDailyLogFilesOlderThanDays(1)
        }
    }

    override fun onResume() {
        super.onResume()
        askForPermissions()
    }

    fun askForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
                return
            }
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
                )
            }
        }else{
            writeLogsInTheFile()
        }
    }

    fun writeLogsInTheFile() {
        LogUtil.d(
            TAG,
            "Hello Advance Logging",
            BuildConfig.DEBUG,
            true,
            BuildConfig.APPLICATION_ID,
            true
        )

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@MainActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION) ===
                                PackageManager.PERMISSION_GRANTED)) {

                        // check for android 11
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            if (Environment.isExternalStorageManager()) {
                                writeLogsInTheFile()
                            }
                        }else{
                            writeLogsInTheFile()
                        }

                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

}