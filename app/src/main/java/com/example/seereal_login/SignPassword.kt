package com.example.seereal_login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.seereal_login.databinding.ActivitySignPasswordBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignPassword : AppCompatActivity() {
    private val binding by lazy { ActivitySignPasswordBinding.inflate(layoutInflater) }
    private lateinit var databaseRef: DatabaseReference  // database reference 가져오기
    private fun newUser (userId : String, nickname:String, password:String){
        val user = User(nickname, password)
        databaseRef.child("users").child(userId).setValue(user)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val firebaseDatabase = FirebaseDatabase.getInstance() // firebase 인스턴스 초기화
        databaseRef = firebaseDatabase.reference // databasereference 초기화
        // users 노드에 접근하고, 사용자 정보를 저장하거나 가져올 수 있다.
        //val usersRef: DatabaseReference = firebaseDatabase.getReference("users")

        val inputpassword = binding.signpassword  // 비밀번호 입력
        val checkpassword = binding.checkpassword  // 비밀번호 확인

        // 사용자가 입력했던 전화번호, 닉네임 정보
        val receivedPhoneNumber = intent.getStringExtra("signPhoneNumber")
        val recievedNickname = intent.getStringExtra("signNickName")

        binding.signpasswordbtn.setOnClickListener{
            val signPasswordInput = inputpassword.text.toString()
            val checkPasswordInput = checkpassword.text.toString()

            if(signPasswordInput == checkPasswordInput){
                newUser(receivedPhoneNumber!!,recievedNickname!!,signPasswordInput)
                // database에 회원정보 저장
//                val usersRef = firebaseDatabase.getReference("users")
//                val  outerKey = usersRef.child(receivedPhoneNumber!!) // 전화번호를 키로 사용
//                Log.d("REAL","outer key is ${outerKey}")
//                outerKey.child("nickname").setValue(recievedNickname)
//                outerKey.child("password").setValue(signPasswordInput)

                Log.d("REAL","success regist")
                Toast.makeText(this, "db에 저장 완료, 회원가입 완료!",Toast.LENGTH_SHORT).show()

                val intent = Intent(this@SignPassword, MainPage::class.java)
                startActivity(intent)

            } else {
                Toast.makeText(this, "try again",Toast.LENGTH_SHORT).show()
            }
        }
    }
}