# Advance_logging

### Saved file location

https://user-images.githubusercontent.com/112402423/187854248-c9b872c1-7b1c-41ed-89e3-00befabfa361.mp4





### Step 1. Add the JitPack repository to your build file
```gradle
//Add it in your root build.gradle at the end of repositories:

allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

### Step 2. Add the dependency

```gradle
dependencies {
	        implementation 'com.github.anilchourasiya-beyondkey:Advance_logging:4.0'
	}
  ```
  
### Step 3. For use the advance logging use below code

  ```gradle
 LogUtil.d(
    TAG,
    "Hello Advance Logging",
    com.my_package.my_app.BuildConfig.DEBUG, //writing the logs only debug build and pass `true` for release build as well.
    true, //writeToFile
    com.my_package.my_app.BuildConfig.APPLICATION_ID, //log file name
    true //createLogFileDateWise 
        )

//To delete old log files which were written date wise use the below code      

LogUtil.deleteDailyLogFilesOlderThanDays(1)

	
```
  
This requires permission to access the storage   
---
#### AndroidManifest.xml     
 ```gradle
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
        tools:ignore="ScopedStorage" />
	
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />

<uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE"
        tools:ignore="ScopedStorage" />

<provider
      android:name="androidx.core.content.FileProvider"
      android:authorities="${applicationId}.fileprovider"
      android:exported="false"
      android:grantUriPermissions="true">
      <meta-data
          android:name="android.support.FILE_PROVIDER_PATHS"
          android:resource="@xml/providerfile" />
</provider>
```
#### providerfile.xml   
```gradle
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <external-files-path
        name="images"
        path="Pictures"/>

    <external-path
        name="external_files"
        path="." />
</paths>

```

#### Code level changes   
```gradle
Android 11
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
                return
            }
        }
        
Below Android 11
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
```
         
            
       
