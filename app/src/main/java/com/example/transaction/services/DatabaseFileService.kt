package com.example.transaction.services

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Environment
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.transaction.dal.DbContext
import com.example.transaction.dal.QueryBuilder
import com.example.transaction.helper.SettingsStore
import com.example.transaction.init.DatabaseModule
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.AesKeyStrength
import net.lingala.zip4j.model.enums.EncryptionMethod
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject


class DatabaseFileService @Inject constructor(
    private val appDatabase: DbContext,
    private val store: SettingsStore) {

    private val fileProviderName = "com.example.transaction.file.provider"

    suspend fun saveBackupFile(context: Context, destinationPath: Uri): Boolean {
        var result = false

        try {
            appDatabase.transactionDao().checkpoint(QueryBuilder.getCheckpointQuery())
            val destFile = context.contentResolver.openFileDescriptor(destinationPath, "w")
            if(destFile != null) {
                val fileStream = FileOutputStream(destFile.fileDescriptor)
                val dbFile = context.getDatabasePath(DatabaseModule.getDatabaseName())
                fileStream.write(dbFile.readBytes())
                fileStream.close()
                result = true
                destFile.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    fun restoreDatabase(context: Context, inputFile: Uri) {
        appDatabase.close()
        val dbFile = context.getDatabasePath(DatabaseModule.getDatabaseName())
        if (dbFile != null) {
            val fileInputDescriptor = context.contentResolver.openFileDescriptor(inputFile, "r")
            if(fileInputDescriptor != null) {
                val inputDatabase = FileInputStream(fileInputDescriptor.fileDescriptor)
                val fileStream = FileOutputStream(dbFile)
                fileStream.write(inputDatabase.readBytes())
                fileStream.close()
                fileInputDescriptor.close()
            }
        }
    }

    fun getDatabaseFile(context: Context): Uri {
        val dbFile = context.getDatabasePath(DatabaseModule.getDatabaseName())
        return dbFile.toUri()
    }

    suspend fun createZip(context: Context): Uri {

        appDatabase.transactionDao().checkpoint(QueryBuilder.getCheckpointQuery())
        val dbFile = context.getDatabasePath(DatabaseModule.getDatabaseName())
        val preference = store.getPreferenceValue(context)

        if (preference.zipBackup.toBoolean()) {
            val zipPassword = preference.zipPassword

            val path2 =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            val zipParameters = ZipParameters()
            zipParameters.isEncryptFiles = true
            zipParameters.encryptionMethod = EncryptionMethod.AES
            zipParameters.aesKeyStrength = AesKeyStrength.KEY_STRENGTH_256
            zipParameters.fileNameInZip = "TransactionDatabase.db"

            val exportPath = path2!!.path + "/TransactionDatabase.zip"

            val zipFile = ZipFile(exportPath, zipPassword.toCharArray())
            zipFile.addFile(dbFile, zipParameters)

            return FileProvider.getUriForFile(
                context,
                fileProviderName,
                zipFile.file
            )
        }

        return FileProvider.getUriForFile(
            context,
            fileProviderName,
            dbFile
        )
    }

    private fun checkPermission(context: Context): Boolean {
        return if (SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val result =
                ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE)
            val result1 =
                ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE)
            result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }
    }
}