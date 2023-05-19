package com.example.seereal_login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.seereal_login.databinding.ActivityMainBinding
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var databaseRef: DatabaseReference  // database reference 가져오기

    // PasswordPage로 전환하는 함수
    fun navigateToPasswordPage() {
        val intent = Intent(this@MainActivity, PasswordPage::class.java)
        startActivity(intent)
    }

    fun navigateToRegisterPage() {
        val intent2 = Intent(this@MainActivity, SignPhoneNumber::class.java)
        startActivity(intent2)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 데이터베이스에서 데이터를 읽거나 쓰려면 databasereference 인스턴스가 필요하다
        val firebaseDatabase = FirebaseDatabase.getInstance() // firebase 인스턴스 초기화
        databaseRef = firebaseDatabase.reference // databasereference 초기화

        // users 노드에 접근하고, 사용자 정보를 저장하거나 가져올 수 있다.
        val usersRef: DatabaseReference = firebaseDatabase.getReference("users")  // users 레퍼런스 가져오기

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
                Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT)
                    .show()

            }

            //val valueEventListener = object : ValueEventListener
            usersRef.orderByChild("phonenum").equalTo(phoneTxt).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(
                                this@MainActivity,
                                "Hi welcome to SeeReal",
                                Toast.LENGTH_SHORT).show()
                            navigateToPasswordPage()
                            Log.d("REAL","success")
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Phone number not found",
                                Toast.LENGTH_SHORT
                            ).show()
                            // FCM 구현해야함
                            // 일단은 회원가입 화면으로 바로 넘어가게 구현
                            navigateToRegisterPage()
                        }
                    }
                   override fun onCancelled(databaseError: DatabaseError) {
                        // 에러 처리
                        Toast.makeText(
                            this@MainActivity,
                            "Error: " + databaseError.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }
    }


