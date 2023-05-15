package com.example.seereal_login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.seereal_login.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var databaseRef: DatabaseReference  // database reference 가져오기

    // PasswordPage로 전환하는 함수
    fun navigateToPasswordPage() {
        val intent = Intent(this@MainActivity, PasswordPage::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 데이터베이스에서 데이터를 읽거나 쓰려면 databasereference 인스턴스가 필요하다
        val firebaseDatabase = FirebaseDatabase.getInstance() // firebase 인스턴스 초기화
        databaseRef = firebaseDatabase.reference // databasereference 초기화

        // 전화번호가 일치하면 비밀번호 입력창으로 넘어간다
        val phone = binding.phone
        val phoneBtn = binding.phonebtn

        // 입력 버튼을 누르고 회원 존재 여부를 판단한다
        // 회원이 존재 하지 않으면 register화면으로 넘어가는 팝업이 뜬다 => 구현해야 함
        phoneBtn.setOnClickListener() {
            // 사용자가 입력한 전화번호
            val phoneTxt = phone.text.toString()

            // Phone number를 입력하지 않고 버튼을 눌렀을 경우
            if (phoneTxt.isEmpty()) {
                Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show()
            }

            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val databasePhone = dataSnapshot.child("users").child("user1").child("phonenum").getValue(String::class.java)
                    val databaseNickname = dataSnapshot.child("users").child("user1").child("nickname").getValue(String::class.java)

                    if (databasePhone == phoneTxt) {
                        Log.d("REAL", "matching success!")
                        Toast.makeText(this@MainActivity, "Hi $databaseNickname welcome to SeeReal", Toast.LENGTH_SHORT)
                            .show()
                        intent.putExtra("nicknametopassword", "${databaseNickname}") // 텍스트 정보 추가
                        navigateToPasswordPage()

                    } else {
                        // 일치하는 정보가 데이터베이스에 존재하지 않는 경우
                        Toast.makeText(this@MainActivity, "Please register", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // 쿼리 수행이 취소된 경우 발생하는 오류 처리를 수행합니다.
                }
            }
            // ValueEventListener를 등록하여 데이터베이스의 값을 가져옴
            databaseRef.addListenerForSingleValueEvent(valueEventListener)
        }
    }
}