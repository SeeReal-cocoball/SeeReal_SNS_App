package com.example.seereal_login.Feed

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.seereal_login.databinding.ActivityMyFeedBinding
import com.example.seereal_login.databinding.ActivitySignNicknameBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyFeed : AppCompatActivity() {

    private val binding by lazy { ActivityMyFeedBinding.inflate(layoutInflater) }
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 동그라미 눌러서 친구 피드 들어가기
        binding.friend1.setOnClickListener{
            val friendFeed = Intent(this, FriendFeed::class.java)
            startActivity(friendFeed)
        }

        // update date
        var dateToday = binding.today

        val currentDate = getCurrentDate()
        dateToday.text = currentDate

        // put image on screen
        val imageUri = intent.getStringExtra("imageUri")
        if (imageUri != null) {
            val uri = Uri.parse(imageUri)
            binding.myImg.setImageURI(uri)
        }
    }

    // 오늘 날짜
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