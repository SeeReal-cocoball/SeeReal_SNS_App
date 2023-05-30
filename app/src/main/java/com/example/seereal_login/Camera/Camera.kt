package com.example.seereal_login.Camera

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.seereal_login.Feed.MyFeed
import com.example.seereal_login.databinding.ActivityCameraBinding
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat


class Camera : AppCompatActivity() {
    val PERMISSIONS = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )
    val PERMISSIONS_REQUEST = 100

    private val binding by lazy { ActivityCameraBinding.inflate(layoutInflater) }

    private val REQUEST_IMAGE_CAPTURE = 1

    var imgUri : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 권한 체크 및 요청
        checkPermissions(PERMISSIONS, PERMISSIONS_REQUEST)

        // 카메라 촬영 클릭 이벤트, 카메라 기능 Intent
        binding.btnCamera.setOnClickListener{
            val cameraApp = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try{
                startActivityForResult(cameraApp, REQUEST_IMAGE_CAPTURE)
            } catch (e : ActivityNotFoundException) {
                Toast.makeText(this,"사진을 찍을 수 없어요",Toast.LENGTH_SHORT).show()
            }
        }

        // img update to my feed
        binding.updateImg.setOnClickListener{
            val intent = Intent(this, MyFeed::class.java)
            intent.putExtra("imageUri", imgUri.toString()) // 이미지의 URI를 전달
            startActivity(intent)
        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode) {
                // 내부 저장소에 사진을 저장한다.
                REQUEST_IMAGE_CAPTURE-> {
                    // 비트맵 로컬 저장소
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    saveBitmapAsJPGFile(imageBitmap)
                    binding.imageView.setImageBitmap(imageBitmap)

                    // 이미지를 찍을 후에만 update button 활성화
                    binding.updateImg.isEnabled = true
                }
            }
        }
    }
    private fun newJpgFileName() : String {
        val sdf = SimpleDateFormat("yyyyMMdd")
        val filename = sdf.format(System.currentTimeMillis())
        return "${filename}.jpg"
    }

    private fun saveBitmapAsJPGFile(bitmap: Bitmap) {
        val path = File(filesDir, "image")
        if(!path.exists()){
            path.mkdirs()
        }
        val file = File(path, newJpgFileName())
        var imageFile: OutputStream? = null

        try{
            file.createNewFile()
            imageFile = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageFile)
            imageFile.close()
            imgUri = Uri.parse(file.absolutePath)
            Toast.makeText(this, "친구들과 공유하세요!", Toast.LENGTH_LONG).show()

            // 파이어베이스에도 사진 저장
            val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val user = sharedPreferences.getString("user", "")

            // 파이어 베이스 저장소 사용
            val storage: FirebaseStorage = FirebaseStorage.getInstance()
            val storageRef: StorageReference = storage.reference

            // 사용자 폴더 경로 생성
            val folderPath = "users/$user/feed/"

            // 사용자 폴더 레퍼런스 가져오기
            val userFolderRef = storageRef.child(folderPath)

            // 이미지 파일 업로드
            val fileUri = Uri.fromFile(File(imgUri.toString()))
            val fileName = newJpgFileName()
            val imageRef = userFolderRef.child(fileName)
            val uploadTask = imageRef.putFile(fileUri)

        }catch (e: Exception){
            null
        }
    }

    // Permission
    private fun checkPermissions(permissions: Array<String>, permissionsRequest: Int): Boolean {
        val permissionList : MutableList<String> = mutableListOf()
        for(permission in permissions){
            val result = ContextCompat.checkSelfPermission(this, permission)
            if(result != PackageManager.PERMISSION_GRANTED){
                permissionList.add(permission)
            }
        }
        if(permissionList.isNotEmpty()){
            ActivityCompat.requestPermissions(this, permissionList.toTypedArray(), PERMISSIONS_REQUEST)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for(result in grantResults){
            if(result != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "권한을 승인해주세요.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}