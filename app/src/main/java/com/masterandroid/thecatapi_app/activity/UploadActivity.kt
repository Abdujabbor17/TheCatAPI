package com.masterandroid.thecatapi_app.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.masterandroid.thecatapi_app.R
import com.masterandroid.thecatapi_app.model.ResponseItem
import com.masterandroid.thecatapi_app.networking.RetrofitHttp
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UploadActivity : AppCompatActivity() {
    lateinit var image: ImageView
    lateinit var b_upload: Button
    lateinit var selectedImageUri: Uri
    val PICK_IMAGE = 1001

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uploded)
        initViews()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initViews() {
        image = findViewById(R.id.image)
        b_upload = findViewById(R.id.b_upload)
        image.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    listOf(Manifest.permission.READ_EXTERNAL_STORAGE).toTypedArray(),
                    2000
                )
            } else {
                openGallery()
            }
        }

        b_upload.setOnClickListener {
            Log.d("myURI", selectedImageUri.toString())

            val file =getFileFromUri(selectedImageUri)
            Log.d("path", file.toString())


            val reqFile: RequestBody = RequestBody.create("image/jpg".toMediaTypeOrNull(), file!!)
            val body: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", file!!.name, reqFile)
            Log.d("file", file!!.name)
            Log.d("file", reqFile.toString())

            //val name = RequestBody.create("text/plain".toMediaTypeOrNull(), "file")

            RetrofitHttp.posterService.createPhoto(body).enqueue(object : Callback<ResponseItem> {
                override fun onResponse(
                    call: Call<ResponseItem>,
                    response: Response<ResponseItem>
                ) {
                    Log.d("TAG", "onResponse: ${response.body()}")
                }

                override fun onFailure(call: Call<ResponseItem>, t: Throwable) {
                    Log.d("TAG", "onFailure: $t")
                }

            })
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, PICK_IMAGE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            selectedImageUri = data?.data!!
            image.setImageURI(selectedImageUri)
        }
    }


    fun getFileFromUri(uri: Uri): File? {
        if (uri.path == null) {
            return null
        }
        var realPath = String()
        val databaseUri: Uri
        val selection: String?
        val selectionArgs: Array<String>?
        if (uri.path!!.contains("/document/image:")) {
            databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            selection = "_id=?"
            selectionArgs = arrayOf(DocumentsContract.getDocumentId(uri).split(":")[1])
        } else {
            databaseUri = uri
            selection = null
            selectionArgs = null
        }
        try {
            val column = "_data"
            val projection = arrayOf(column)
            val cursor = this.contentResolver.query(
                databaseUri,
                projection,
                selection,
                selectionArgs,
                null
            )
            cursor?.let {
                if (it.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndexOrThrow(column)
                    realPath = cursor.getString(columnIndex)
                }
                cursor.close()
            }
        } catch (e: Exception) {
            Log.i("GetFileUri Exception:", e.message ?: "")
        }
        val path = if (realPath.isNotEmpty()) realPath else {
            when {
                uri.path!!.contains("/document/raw:") -> uri.path!!.replace(
                    "/document/raw:",
                    ""
                )
                uri.path!!.contains("/document/primary:") -> uri.path!!.replace(
                    "/document/primary:",
                    "/storage/emulated/0/"
                )
                else -> return null
            }
        }
        return File(path)
    }
}