package com.example.application_test0511

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.application_test0511.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
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
            val user = FriendData(nickname, password)
            userRef.child(number).setValue(user)
            Log.d("iise","데이터저장")
        }


/*
/*
        그냥 구현해본 것들 참고용
        writeNewUser("01012341234","AA","123")

        writeNewUser("01012340003","BB","123")
        writeNewUser("01012340004","CC","123")

        val userId = "01012340001" // 특정 유저의 ID (전화번호 등)

        val userRe = database.getReference("users").child(userId)
        val friendsMap = mutableMapOf<String, Boolean>()

        friendsMap["01012340002"] = true
        friendsMap["01012340003"] = true
        friendsMap["01012340004"] = true
        // 친구 목록 업데이트
        userRe.child("friends").setValue(friendsMap)
*/

        fun readUser () {
            val userRef = database.getReference("users")
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    for (outerSnapshot in dataSnapshot.children) {
                        val outerKey = outerSnapshot.key // 바깥쪽 "key" 값을 가져옴
                        Log.d("iise", "Outer Key: $outerKey")
                        for (innerSnapshot in outerSnapshot.children) {
                            val innerKey = innerSnapshot.key // 안쪽 "key" 값을 가져옴
                            val innerValue = innerSnapshot.value // 해당 "key"에 대한 값을 가져옴
                            Log.d("iise", "Inner Key: $innerKey, Value: " +
                                    "$innerValue")
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w("iise", "Failed to read value.", error.toException())
                }
            })
        }
        //readUser()

        val userRef = database.getReference("users")
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (outerSnapshot in dataSnapshot.children) {
                    val outerKey = outerSnapshot.key // 바깥쪽 "key" 값을 가져옴
                    Log.d("iise", "Outer Key: $outerKey")
                    for (innerSnapshot in outerSnapshot.children) {
                        val innerKey = innerSnapshot.key // 안쪽 "key" 값을 가져옴
                        val innerValue = innerSnapshot.value // 해당 "key"에 대한 값을 가져옴
                        Log.d("iise", "Inner Key: $innerKey, Value: " +
                                "$innerValue")
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("iise", "Failed to read value.", error.toException())
            }
        })


        fun basicReadWrite() {
            // [START write_message]
            // Write a message to the database
            val database = Firebase.database
            val myRef = database.getReference("message")

            myRef.setValue("Hello, World!")
            // [END write_message]

            // [START read_message]
            // Read from the database
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    val value = dataSnapshot.getValue<String>()
                    Log.d("iise", "Value is: $value")
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w("iise", "Failed to read value.", error.toException())
                }
            })
        }
//
//        basicReadWrite()
*/
    }
}