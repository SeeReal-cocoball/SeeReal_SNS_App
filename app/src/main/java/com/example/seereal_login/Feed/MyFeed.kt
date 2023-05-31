package com.example.seereal_login.Feed

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.seereal_login.Camera.Camera
import com.example.seereal_login.Friend.Friend_manage
import com.example.seereal_login.Map.MapView
import com.example.seereal_login.R
import com.example.seereal_login.databinding.ActivityMyFeedBinding
import com.example.seereal_login.databinding.ActivitySignNicknameBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.squareup.picasso.Picasso



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

        binding.goToMap.setOnClickListener{
            val intent = Intent(this,MapView::class.java)
            startActivity(intent)
        }


        // 피드에 대한 위치정보 및 유저 닉네임 수정
        val database = Firebase.database
        val today = SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis())

        val usersRef = database.getReference("users")
        val userFeed = usersRef.child(user.toString())

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


        // Firebase Storage 초기화
        val storage: FirebaseStorage = FirebaseStorage.getInstance()
        val storageRef: StorageReference = storage.reference

        // put image on screen
        val imageUri = intent.getStringExtra("imageUri")
        if (imageUri != null) {
            val uri = Uri.parse(imageUri)
            binding.myImg.setImageURI(uri)
        } else {
            // 이미지 다운로드 및 표시
            val sdf = SimpleDateFormat("yyyyMMdd")
            val filename = sdf.format(System.currentTimeMillis())

            val imageRef: StorageReference = storageRef.child("users")
                .child("$user").child("feed")
                .child("$filename.jpg")

            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // 이미지 다운로드 성공
                Picasso.get().load(uri).into(binding.myImg)
            }.addOnFailureListener { exception ->
                // 이미지 다운로드 실패
                Toast.makeText(this, "문제가 발생했습니다. 다시 시도해주세요", Toast.LENGTH_LONG).show()
            }
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