package com.example.seereal_login

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.seereal_login.databinding.ActivitySignPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignPassword : AppCompatActivity() {
    private val binding by lazy { ActivitySignPasswordBinding.inflate(layoutInflater) }
    private lateinit var databaseRef: DatabaseReference  // database reference 가져오기
    private lateinit var firebaseAuth: FirebaseAuth

    // firebase database에 저장하는 함수 구현
    private fun saveUserToFirebase(user: User) {
        val userId = firebaseAuth.currentUser?.uid
        userId?.let {
            databaseRef.child(it).setValue(user) // firebase database에 user 객체 저장
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // 회원가입이 성공적으로 완료됨
                        Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        // 다음 화면으로 이동하거나 로그인 등의 추가 작업 수행
                    } else {
                        // 회원가입 실패
                        Toast.makeText(this, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 사용자가 입력했던 전화번호, 닉네임 정보 받아야 함
        val receivedPhoneNumber = intent.getStringExtra("signPhoneNumber")
        val recievedNickname = intent.getStringExtra("signNickName")

        val firebaseDatabase = FirebaseDatabase.getInstance() // firebase 인스턴스 초기화
        databaseRef = firebaseDatabase.reference // databasereference 초기화

        // users 노드에 접근하고, 사용자 정보를 저장하거나 가져올 수 있다.
        val usersRef: DatabaseReference = firebaseDatabase.getReference("users")  // users 레퍼런스 가져오기

        // 비밀번호 입력
        var password = binding.signpassword
        var signpasswordbutton = binding.signpasswordbtn // 회원가입 완료 버튼
        var signPasswordInput = password.text.toString()
        // 버튼을 누르면 사용자가 입력한 전화번호, 닉네임, 비밀번호가 데이터베이스에 저장되고 회원가입이 완료되며
        // 메인화면이 보인다.

        signpasswordbutton.setOnClickListener(){
            val user = User(receivedPhoneNumber, recievedNickname, signPasswordInput)
            saveUserToFirebase(user)
        }
    }
}