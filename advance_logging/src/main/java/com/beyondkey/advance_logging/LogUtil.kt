package com.beyondkey.advance_logging

import android.os.Environment
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/*
*
* Created by Anil Chourasiya at 21/05/2022
* */
object LogUtil {
    private const val LOG_LEVEL_VERBOSE = 1
    private const val LOG_LEVEL_DEBUG = 2
    private const val LOG_LEVEL_INFO = 3
    private const val LOG_LEVEL_WARN = 4
    private const val LOG_LEVEL_ERROR = 5
    private const val LOG_FILE_MIN_SIZE = (1024 * 1024 * 4 //4MB
            ).toLong()
    private const val LOG_FILE_MAX_SIZE = (1024 * 1024 * 5 //5MB
            ).toLong()
    private var APPLICATION_ID = "Advance_Logging"
    private var SaveFileInFolder = Constant.EXTERNAL_FOLDER
    private var fileOutputExtension:String = Constant.EXTENSION_TXT


    /**
     * writes log level e in debug build only
     * @see LogUtil.d
     *
     * @see LogUtil.v
     *
     * @see LogUtil.i
     *
     * @see LogUtil.w
     *
     * @param tag TAG string
     * @param msg Message to write
     */
    fun e(
        tag: String,
        msg: String,
        buildType: Boolean,
        writeToFile: Boolean,
        applicationID: String,
        createLogFileDateWise: Boolean,
        savedFilelFolder: String,
        fileExtension:String
    ) {
        log(LOG_LEVEL_ERROR, tag, msg, buildType)
        APPLICATION_ID = applicationID
        this.SaveFileInFolder = savedFilelFolder
        GlobalScope.launch {
            writeLogToFile(
                tag,
                msg,
                LOG_LEVEL_ERROR,
                writeToFile,
                createLogFileDateWise,
                savedFilelFolder,
                "e",
                fileExtension

            )
        }
    }

    /**
     * writes log level w in debug build only
     * @see LogUtil.d
     *
     * @see LogUtil.v
     *
     * @see LogUtil.i
     *
     * @see LogUtil.w
     *
     * @param tag TAG string
     * @param msg Message to write
     */
    fun w(
        tag: String,
        msg: String,
        buildType: Boolean,
        writeToFile: Boolean,
        applicationID: String,
        createLogFileDateWise: Boolean,
        savedFilelFolder: String,
        fileExtension:String
    ) {
        log(LOG_LEVEL_WARN, tag, msg, buildType)
        APPLICATION_ID = applicationID
        this.SaveFileInFolder = savedFilelFolder
        GlobalScope.launch {
            writeLogToFile(
                tag,
                msg,
                LOG_LEVEL_WARN,
                writeToFile,
                createLogFileDateWise,
                savedFilelFolder,
                "w",
                fileExtension
            )
        }
    }

    /**
     * writes log level d in debug build only
     * @see LogUtil.d
     *
     * @see LogUtil.v
     *
     * @see LogUtil.i
     *
     * @see LogUtil.w
     *
     * @param tag TAG string
     * @param msg Message to write
     */
    fun d(
        tag: String,
        msg: String,
        buildType: Boolean,
        writeToFile: Boolean,
        applicationID: String,
        createLogFileDateWise: Boolean,
        savedFilelFolder: String,
        fileExtension:String
    ) {
        log(LOG_LEVEL_DEBUG, tag, msg, buildType)
        APPLICATION_ID = applicationID
        this.SaveFileInFolder = savedFilelFolder
        GlobalScope.launch {
            writeLogToFile(
                tag,
                msg,
                LOG_LEVEL_DEBUG,
                writeToFile,
                createLogFileDateWise,
                savedFilelFolder,
                "d",
                fileExtension
            )
        }
    }

    /**
     * writes log level i in debug build only
     * @see LogUtil.d
     *
     * @see LogUtil.v
     *
     * @see LogUtil.i
     *
     * @see LogUtil.w
     *
     * @param tag TAG string
     * @param msg Message to write
     */
    fun i(
        tag: String,
        msg: String,
        buildType: Boolean,
        writeToFile: Boolean,
        applicationID: String,
        createLogFileDateWise: Boolean,
        savedFilelFolder: String,
        fileExtension:String
    ) {
        log(LOG_LEVEL_INFO, tag, msg, buildType)
        APPLICATION_ID = applicationID
        this.SaveFileInFolder = savedFilelFolder
        GlobalScope.launch {
            writeLogToFile(
                tag,
                msg,
                LOG_LEVEL_INFO,
                writeToFile,
                createLogFileDateWise,
                savedFilelFolder,
                "i",
                fileExtension
            )
        }
    }

    /**
     * writes log level v in debug build only
     * @see LogUtil.d
     *
     * @see LogUtil.v
     *
     * @see LogUtil.i
     *
     * @see LogUtil.w
     *
     * @param tag TAG string
     * @param msg Message to write
     */
    fun v(
        tag: String,
        msg: String,
        buildType: Boolean,
        writeToFile: Boolean,
        applicationID: String,
        createLogFileDateWise: Boolean,
        savedFilelFolder: String,
        fileExtension:String
    ) {
        log(LOG_LEVEL_VERBOSE, tag, msg, buildType)
        APPLICATION_ID = applicationID
        this.SaveFileInFolder = savedFilelFolder
        GlobalScope.launch {
            writeLogToFile(
                tag,
                msg,
                LOG_LEVEL_VERBOSE,
                writeToFile,
                createLogFileDateWise,
                savedFilelFolder,
                "v",
                fileExtension
            )
        }
    }

    fun printStackTrace(
        tag: String,
        exception: Exception,
        buildType: Boolean,
        writeToFile: Boolean,
        createLogFileDateWise: Boolean,
        savedFilelFolder: String,
        fileExtension:String
    ) {
        if (buildType) {
            exception.printStackTrace()
        }
        this.SaveFileInFolder = savedFilelFolder
        GlobalScope.launch {
            writeLogToFile(
                tag,
                exception.message,
                writeToFile,
                createLogFileDateWise,
                savedFilelFolder,
                "printStackTrace",
                fileExtension
            )
            writeLogToFile(
                tag,
                Log.getStackTraceString(exception),
                writeToFile,
                createLogFileDateWise,
                savedFilelFolder,
                "printStackTrace",
                fileExtension
            )
        }
    }

    private fun log(level: Int, tag: String, msg: String, buildType: Boolean) {
        /*long currentTime = System.currentTimeMillis();
        msg = "[Time_ms:" + currentTime + "] " + msg;*/
        var msg: String? = msg
        if (buildType) {
            if (msg == null) {
                msg = ""
            }
            msg = Thread.currentThread().name + ": " + msg
            when (level) {
                LOG_LEVEL_DEBUG -> Log.d(tag, msg)
                LOG_LEVEL_ERROR -> Log.e(tag, msg)
                LOG_LEVEL_INFO -> Log.i(tag, msg)
                LOG_LEVEL_WARN -> Log.w(tag, msg)
                LOG_LEVEL_VERBOSE -> Log.v(tag, msg)
                else -> Log.v(tag, msg)
            }
        }
    }

    private fun trimFileIfRequired(outputFile: File) {
        Log.e("LogUtil", "Trimming Log file as file size > " + LOG_FILE_MAX_SIZE)
        val tempFile = tempFile
        val f: RandomAccessFile? = null
        try {
            trimFileToZeroSize(tempFile)
            copy(outputFile, tempFile) //copy content of outputFile to tempFile
            trimFileToZeroSize(outputFile) //remove content form output file
            copySkipInitialData(
                tempFile,
                outputFile,
                LOG_FILE_MAX_SIZE - LOG_FILE_MIN_SIZE
            ) //copy content back from temp file to out put file, skiping initial data
            trimFileToZeroSize(tempFile) //remove content from temp file
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (f != null) {
                try {
                    f.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun trimFileToZeroSize(fileToTrim: File) {
        var f: RandomAccessFile? = null
        try {
            f = RandomAccessFile(fileToTrim, "rw")
            f.setLength(0)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (f != null) {
                try {
                    f.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun copy(src: File?, dst: File?) {
        val `in`: InputStream = FileInputStream(src)
        try {
            val out: OutputStream = FileOutputStream(dst)
            try {
                // Transfer bytes from in to out
                val buf = ByteArray(1024)
                var len: Int
                while (`in`.read(buf).also { len = it } > 0) {
                    out.write(buf, 0, len)
                }
            } finally {
                out.close()
            }
        } finally {
            `in`.close()
        }
    }

    /**
     *
     * @param src
     * @param dst
     * @param bytesToSkip bytes to skip from start of src file
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun copySkipInitialData(src: File, dst: File, bytesToSkip: Long) {
        val `in`: InputStream = FileInputStream(src)
        try {
            val out: OutputStream = FileOutputStream(dst)
            try {
                `in`.skip(bytesToSkip) //skip Data from start
                // Transfer bytes from in to out
                val buf = ByteArray(1024)
                var len: Int
                while (`in`.read(buf).also { len = it } > 0) {
                    out.write(buf, 0, len)
                }
            } finally {
                out.close()
            }
        } finally {
            `in`.close()
        }
    }

    /**
     *
     * @return temp file to copy content
     */
    private val tempFile: File
        private get() {
            var logDir:File
            if(SaveFileInFolder.equals(Constant.INTERNAL_FOLDER)){
                logDir = File(logFileDirPathInternal)
            }else{
                logDir = File(logFileDirPathExternal)
            }

            if (logDir.exists() && !logDir.isDirectory) {
                logDir.delete()
            }
            if (!logDir.exists()) {
                logDir.mkdirs()
            }
            val tempFile = File(logDir, tempLogFileName)
            if (!tempFile.exists()) {
                try {
                    tempFile.createNewFile()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return tempFile
        }

    /**
     * prints long data like api response in muliple lines, this method does not put data on logs txt file.
     * @param TAG
     * @param data
     */
    fun printLongData(TAG: String?, data: String, buildType: Boolean) {
        if (buildType) {
            val chunkSize = 2048
            var i = 0
            while (i < data.length) {
                Log.d(TAG, data.substring(i, Math.min(data.length, i + chunkSize)))
                i += chunkSize
            }
        }
    }

    private fun writeLogToFile(
        tag: String,
        msg: String,
        logLevel: Int,
        writeToFile: Boolean,
        createLogFileDateWise: Boolean,
        savedFilelFolder: String,
        methodName:String,
        fileExtension:String

    ) {
        var tag = tag
        when (logLevel) {
            LOG_LEVEL_DEBUG -> tag = "D/$tag"
            LOG_LEVEL_VERBOSE -> tag = "V/$tag"
            LOG_LEVEL_INFO -> tag = "I/$tag"
            LOG_LEVEL_WARN -> tag = "W/$tag"
            LOG_LEVEL_ERROR -> tag = "E/$tag"
        }
        writeLogToFile(tag, msg, writeToFile, createLogFileDateWise,savedFilelFolder,methodName,fileExtension)
    }

    /**
     * writes log to file, this method does not check build type
     * @param tag TAG string
     * @param msg msg
     */
    private fun writeLogToFile(
        tag: String,
        msg: String?,
        writeToFile: Boolean,
        createLogFileDateWise: Boolean,
        savedFilelFolder: String,
        methodName: String,
        fileExtension:String
    ) {
        this.fileOutputExtension = fileExtension
        if (writeToFile) {
            var msg = msg
            if (msg == null) {
                msg = ""
            }
            val date = Date()
            val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS")
            val strDateTime = sdf.format(date)
            msg = "[$strDateTime] $tag : $msg"

            if(fileExtension.equals(Constant.EXTENSION_HTML)){
                when (methodName){
                    "e" -> msg = "<font color = \"#FE0906\">"+msg+"</font><br />"
                    "w" ->  msg = "<font color = \"#E3E303\">"+msg+"</font><br />"
                    "d" ->  msg = "<font color = \"##2874A6\">"+msg+"</font><br />"
                    "i" ->  msg = "<font color = \"#035FE3\">"+msg+"</font><br />"
                    "v" ->  msg = "<font color = \"##F4D03F\">"+msg+"</font><br />"
                    "printStackTrace" ->  msg = "<font color = \"#566573\">"+msg+"</font><br />"
                }
            }
            val logDir:File
            if(savedFilelFolder.equals(Constant.INTERNAL_FOLDER)){
                logDir = File(logFileDirPathInternal)
            }else{
                logDir = File(logFileDirPathExternal)
            }
            if (logDir.exists() && !logDir.isDirectory) {
                logDir.delete()
            }
            if (!logDir.exists()) {
                logDir.mkdirs()
            }
            var outputFile: File
            if (createLogFileDateWise) {
                outputFile = File(logDir, getTodayLogFileName(fileExtension))
            } else {
                outputFile = File(logDir, logFileName(fileExtension))
            }

            if (!outputFile.exists()) {
                try {
                    outputFile.createNewFile()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            try {
                val buf = BufferedWriter(FileWriter(outputFile, true))
                buf.append(msg)
                buf.newLine()
                buf.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                val fileSizeInBytes = outputFile.length()
                //                Log.d("LogUtil","Log file Size in bytes: "+fileSizeInBytes+" (max size is: "+LOG_FILE_MAX_SIZE+")");
                if (fileSizeInBytes > LOG_FILE_MAX_SIZE) {
                    trimFileIfRequired(outputFile)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteDailyLogFilesOlderThanDays(days: Int, savedFilelFolder: String) {
        this.SaveFileInFolder = savedFilelFolder
        val dailyLogFileDir:File
        if(savedFilelFolder.equals(Constant.INTERNAL_FOLDER)){
            dailyLogFileDir = File(logFileDirPathInternal)
        }else{
            dailyLogFileDir = File(logFileDirPathExternal)
        }
        for (file in dailyLogFileDir.listFiles()) {
            try {
                val fileNameWithoutExt = getBaseName(file.name)
                val sdf = SimpleDateFormat(DAILY_LOG_FILE_NAME_DATE_FORMAT)
                val fileDate = sdf.parse(fileNameWithoutExt)
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DATE, -days) //go back to specified days
                val dateFilesToDeleteBefore = calendar.time
                if (fileDate.before(dateFilesToDeleteBefore)) {
                    file.delete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Gets the base name, without extension, of given file name.
     *
     *
     * e.g. getBaseName("file.txt") will return "file"
     *
     * @param fileName
     * @return the base name
     */
    private fun getBaseName(fileName: String): String {
        val index = fileName.lastIndexOf('.')
        return if (index == -1) {
            fileName
        } else {
            fileName.substring(0, index)
        }
    }

    const val DAILY_LOG_FILE_NAME_DATE_FORMAT = "dd-MM-yyyy"
    private fun getTodayLogFileName(fileOutputExtension : String):String{
        val sdf =
            SimpleDateFormat(DAILY_LOG_FILE_NAME_DATE_FORMAT)
        val calendar = Calendar.getInstance()
        val formattedDate = sdf.format(calendar.time)
        return "$formattedDate.$fileOutputExtension"
    }


    //shift one day before
    private val yesterdayLogFileName: String
        get() {
            val sdf =
                SimpleDateFormat(DAILY_LOG_FILE_NAME_DATE_FORMAT)
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DATE, -1) //shift one day before
            val formattedDate = sdf.format(calendar.time)
            return "$formattedDate.txt"
        }


    private val logFileDirPathExternal: String
        get() = try {
            Environment.getExternalStorageDirectory().absolutePath + "/" + APPLICATION_ID + "/log"
        } catch (e: Exception) {
            "/storage/emulated/0" + "/" + APPLICATION_ID + "/log"
        }

    private val logFileDirPathInternal: String
        get() = try {
            Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + APPLICATION_ID + "/log"
        } catch (e: Exception) {
            "/storage/emulated/0" + "/Android/data/" + APPLICATION_ID + "/log"
        }

    private fun logFileName(fileExtension: String): String{
        return "logs_" + APPLICATION_ID + fileExtension
    }


    private val tempLogFileName: String
        get() = "temp_logs_" + APPLICATION_ID + fileOutputExtension

    fun getAllLogsFileForUpload(filelFolder : String) : MutableList<String>{
        val fileList: MutableList<String> = ArrayList()
        try {
            val logDir:String
            if(filelFolder.equals(Constant.INTERNAL_FOLDER)){
                logDir = File(logFileDirPathInternal).toString()
            }else{
                logDir = File(logFileDirPathExternal).toString()
            }
            val file = File(logDir)
            val arr: Array<String> = file.list()
            for (i in arr) {
                if (i.endsWith(fileOutputExtension)) {
                    fileList.add(i)
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        return fileList
    }
}