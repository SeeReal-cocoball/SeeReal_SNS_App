package com.example.seereal_login.Feed

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.example.seereal_login.R
import com.example.seereal_login.SignPassword
import com.example.seereal_login.databinding.ActivityFriendFeedBinding
import com.example.seereal_login.databinding.ActivityMyFeedBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FriendFeed : AppCompatActivity() {


    private val binding by lazy { ActivityFriendFeedBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        // 내 피드로 돌아가기
        binding.backMyFeed.setOnClickListener{
            val backMyFeed = Intent(this, MyFeed::class.java)
            startActivity(backMyFeed)

        }
    }
}