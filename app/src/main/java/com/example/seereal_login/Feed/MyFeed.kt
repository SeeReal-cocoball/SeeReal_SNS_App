package com.example.seereal_login.Feed

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.seereal_login.Camera.Camera
import com.example.seereal_login.Friend.Friend_manage
import com.example.seereal_login.R
import com.example.seereal_login.databinding.ActivityMyFeedBinding
import com.example.seereal_login.databinding.ActivitySignNicknameBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyFeed : AppCompatActivity() {

    val binding by lazy { ActivityMyFeedBinding.inflate(layoutInflater) }
    private var imageUri: Uri? = null

    // 메뉴 바를 생성해서 화면에 붙이기
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        binding.mainToolbar.inflateMenu(R.menu.main_menus)
        return true
    }

    //메뉴 아이템을 만들고 해당하는 메뉴아이템을 화면에 할당 후 엑티비티 전환할 수 있도록 할당
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.goToFriendList -> {
                val intent3 = Intent(this@MyFeed, Friend_manage::class.java)
                startActivity(intent3)
                true
            }
            R.id.goToMyProfile -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.mainToolbar)

        // test ##################
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val user = sharedPreferences.getString("user", "")
        Log.d("geon_local_feed","피드에서 로컬 저장소 접근 $user")


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