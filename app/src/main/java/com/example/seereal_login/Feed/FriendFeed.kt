package com.example.seereal_login.Feed

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.example.seereal_login.Map.MapView
import com.example.seereal_login.R
import com.example.seereal_login.SignPassword
import com.example.seereal_login.databinding.ActivityFriendFeedBinding
import com.example.seereal_login.databinding.ActivityMyFeedBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FriendFeed : AppCompatActivity() {


    private val binding by lazy { ActivityFriendFeedBinding.inflate(layoutInflater) }
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 피드에 대한 유저 닉네임 수정
        // 추후 리사이클러 뷰를 통해서 내 친구 모두 불러올 예정
        val database = Firebase.database
        val today = SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis())

        val usersRef = database.getReference("users")
        val userFeed = usersRef.child("01012340002")

        userFeed.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val nickname = dataSnapshot.child("nickname").value
                val address = dataSnapshot.child("feed").child(today).child("address").value
                val showNick = binding.textView8
                val location = binding.location
                showNick.text = nickname.toString()
                location.text = address.toString()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        // update date
        var dateToday = binding.today

        val currentDate = getCurrentDate()
        dateToday.text = currentDate

        // 내 피드로 돌아가기
        binding.backMyFeed.setOnClickListener{
            val backMyFeed = Intent(this, MyFeed::class.java)
            startActivity(backMyFeed)
        }

        // 피드 불러오기
        val storage: FirebaseStorage = FirebaseStorage.getInstance()
        val storageRef: StorageReference = storage.reference

        val sdf = SimpleDateFormat("yyyyMMdd")
        val filename = sdf.format(System.currentTimeMillis())

        val imageRef: StorageReference = storageRef.child("users")
            .child("01012340002").child("feed")
            .child("$filename.jpg")

        imageRef.downloadUrl.addOnSuccessListener { uri ->
            // 이미지 다운로드 성공
            Picasso.get().load(uri).into(binding.myImg)
        }.addOnFailureListener { exception ->
            // 이미지 다운로드 실패
            Toast.makeText(this, "문제가 발생했습니다. 다시 시도해주세요", Toast.LENGTH_LONG).show()
        }


    }
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // 이미지 상태 저장
        outState.putParcelable("imageUri", imageUri)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // 이미지 상태 복원
        imageUri = savedInstanceState.getParcelable("imageUri")
    }
}
