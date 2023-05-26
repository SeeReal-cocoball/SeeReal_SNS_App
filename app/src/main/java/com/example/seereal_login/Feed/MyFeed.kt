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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 동그라미 (친구피드)누르면 아래 화면이 바뀌어야 한다.

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
}