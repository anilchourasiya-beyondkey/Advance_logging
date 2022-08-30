package com.beyondkey.advance_logging

import android.os.Environment
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

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
    fun e(tag: String, msg: String, buildType: Boolean, writeToFile: Boolean, applicationID:String) {
        log(LOG_LEVEL_ERROR, tag, msg, buildType)
        APPLICATION_ID = applicationID
        GlobalScope.launch {
            writeLogToFile(tag, msg, LOG_LEVEL_ERROR, writeToFile)
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
    fun w(tag: String, msg: String, buildType: Boolean, writeToFile: Boolean, applicationID:String) {
        log(LOG_LEVEL_WARN, tag, msg, buildType)
        APPLICATION_ID = applicationID
        GlobalScope.launch {
            writeLogToFile(tag, msg, LOG_LEVEL_WARN,writeToFile)
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
    fun d(tag: String, msg: String, buildType: Boolean, writeToFile: Boolean, applicationID:String) {
        log(LOG_LEVEL_DEBUG, tag, msg, buildType)
        APPLICATION_ID = applicationID
        GlobalScope.launch {
            writeLogToFile(tag, msg, LOG_LEVEL_DEBUG, writeToFile)
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
    fun i(tag: String, msg: String, buildType: Boolean, writeToFile: Boolean, applicationID:String) {
        log(LOG_LEVEL_INFO, tag, msg, buildType)
        APPLICATION_ID = applicationID
        GlobalScope.launch {
            writeLogToFile(tag, msg, LOG_LEVEL_INFO, writeToFile)
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
    fun v(tag: String, msg: String, buildType: Boolean, writeToFile: Boolean, applicationID:String) {
        log(LOG_LEVEL_VERBOSE, tag, msg, buildType)
        APPLICATION_ID = applicationID
        GlobalScope.launch {
            writeLogToFile(tag, msg, LOG_LEVEL_VERBOSE,writeToFile)
        }
    }

    fun printStackTrace(tag: String, exception: Exception, buildType : Boolean, writeToFile: Boolean = false) {
        if (buildType) {
            exception.printStackTrace()
        }
        GlobalScope.launch {
            writeLogToFile(tag, exception.message, writeToFile)
            writeLogToFile(tag, Log.getStackTraceString(exception), writeToFile)
        }
    }

    private fun log(level: Int, tag: String, msg: String, buildType : Boolean ) {
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
            copySkipInitialData(tempFile,
                outputFile,
                LOG_FILE_MAX_SIZE - LOG_FILE_MIN_SIZE) //copy content back from temp file to out put file, skiping initial data
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
    fun copy(src: File?, dst: File?) {
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
            val logDir = File(logFileDirPath)
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

    private fun writeLogToFile(tag: String, msg: String, logLevel: Int, writeToFile: Boolean) {
        var tag = tag
        when (logLevel) {
            LOG_LEVEL_DEBUG -> tag = "D/$tag"
            LOG_LEVEL_VERBOSE -> tag = "V/$tag"
            LOG_LEVEL_INFO -> tag = "I/$tag"
            LOG_LEVEL_WARN -> tag = "W/$tag"
            LOG_LEVEL_ERROR -> tag = "E/$tag"
        }
        writeLogToFile(tag, msg, writeToFile)
    }

    /**
     * writes log to file, this method does not check build type
     * @param tag TAG string
     * @param msg msg
     */
    private fun writeLogToFile(tag: String, msg: String?, writeToFile: Boolean) {
        if (writeToFile) {
            var msg = msg
            if (msg == null) {
                msg = ""
            }
            val date = Date()
            val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS")
            val strDateTime = sdf.format(date)
            msg = "[$strDateTime] $tag : $msg"
            val logDir = File(logFileDirPath)
            if (logDir.exists() && !logDir.isDirectory) {
                logDir.delete()
            }
            if (!logDir.exists()) {
                logDir.mkdirs()
            }
            val outputFile = File(logDir, logFileName)
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

    fun deleteDailyLogFilesOlderThanDays(days: Int) {
        val dailyLogFileDir = File(logFileDirPath)
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
    private val todayLogFileName: String
        get() {
            val sdf =
                SimpleDateFormat(DAILY_LOG_FILE_NAME_DATE_FORMAT)
            val calendar = Calendar.getInstance()
            val formattedDate = sdf.format(calendar.time)
            return "$formattedDate.txt"
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

    private val logFileDirPath: String
        get() = try {
            Environment.getExternalStorageDirectory().absolutePath + "/" + APPLICATION_ID + "/log"
        } catch (e: Exception) {
            "/storage/emulated/0" + "/Android/data/" + APPLICATION_ID + "/log"
        }

    private val logFileName: String
        get() = "logs_" + APPLICATION_ID + ".txt"

    private val tempLogFileName: String
        private get() = "temp_logs_" + APPLICATION_ID + ".txt"
}