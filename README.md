# Advance_logging
>Step 1. Add the JitPack repository to your build file
```gradle
Add it in your root build.gradle at the end of repositories:

allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

>Step 2. Add the dependency

```gradle
dependencies {
	        implementation 'com.github.anilchourasiya-beyondkey:Advance_logging:Tag'
	}
  ```
  
>For use the advance logging use below code

  ```LogUtil.d(
            TAG,
            "Hello Advance Logging",
            BuildConfig.DEBUG,
            true,
            BuildConfig.APPLICATION_ID
        )
```
  
 >This requires permission to access the storage
 
 ```
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
            
            
       
