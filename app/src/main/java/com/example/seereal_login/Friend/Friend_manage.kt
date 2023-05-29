package com.example.seereal_login.Friend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.seereal_login.R
import com.example.seereal_login.User
import com.example.seereal_login.databinding.ActivityFriendManageBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Friend_manage : AppCompatActivity() {

    val binding by lazy { ActivityFriendManageBinding.inflate(layoutInflater) }
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 내친구 친구찾기 fragment 설정
        val searchFriendFragment = SearchFriendFragment()
        val myFriendFragment = MyFriendFragment()

        val myFrags = listOf(
            searchFriendFragment, myFriendFragment
        )

        val fragAdapter = FragmentAdapter(this)
        fragAdapter.fragList = myFrags
        binding.viewpager.adapter = fragAdapter

        // fragment 레이블 설정
        val tabs = listOf("검색","내친구")
        TabLayoutMediator(binding.tabLayout,binding.viewpager){ tab, position ->
            tab.text = tabs.get(position)
        }.attach()



        // 이건 그냥 데이터베이스에 데이터 추가하는 부분 구현해봄
        val database = Firebase.database

        fun writeNewUser(number: String, nickname: String, password: String) {
            val userRef = database.getReference("users")
            val user = User(nickname, password)
            userRef.child(number).setValue(user)
            Log.d("iise","데이터저장")
        }


    }
}