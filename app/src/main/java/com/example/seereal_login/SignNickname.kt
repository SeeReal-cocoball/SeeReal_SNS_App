package com.example.seereal_login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.seereal_login.databinding.ActivitySignNicknameBinding
import com.example.seereal_login.databinding.ActivitySignPhoneNumberBinding
import com.google.firebase.database.*

class SignNickname : AppCompatActivity() {

    private val binding by lazy { ActivitySignNicknameBinding.inflate(layoutInflater) }
    private lateinit var databaseRef: DatabaseReference  // database reference 가져오기

    // 비밀번호 입력창으로 이동
    fun navigateToSignPassword() {
        val intent = Intent(this@SignNickname, SignPassword::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val firebaseDatabase = FirebaseDatabase.getInstance() // firebase 인스턴스 초기화
        databaseRef = firebaseDatabase.reference // databasereference 초기화

        // users 노드에 접근하고, 사용자 정보를 저장하거나 가져올 수 있다.
        val usersRef: DatabaseReference = firebaseDatabase.getReference("users")  // users 레퍼런스 가져오기

        var signNickname = binding.signnickname
        var signNicknamebutton = binding.signnicknamebtn

        // 데이터 베이스에 닉네임 중복 확인 if else-> 사용자가 입력한 닉네임 패스워드 화면으로 넘기기
        signNicknamebutton.setOnClickListener() {

            // 사용자가 입력한 닉네임
            var signNickNameInput = signNickname.text.toString()

            // Phone number를 입력하지 않고 버튼을 눌렀을 경우
            if (signNickNameInput.isEmpty()) {
                Toast.makeText(this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }

            usersRef.orderByChild("nickname").equalTo(signNickNameInput).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // 해당 닉네임이 데이터베이스에 존재할 경우 다시 입력해야 함
                        Toast.makeText(
                            this@SignNickname,
                            "닉네임이 존재합니다. 다시 입력해주세요",
                            Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            this@SignNickname,
                            "안녕하세요 ${signNickNameInput} 님!",
                            Toast.LENGTH_SHORT
                        ).show()
                        // 정보도 넘겨야 함
                        val sendText = Intent(this@SignNickname, SignPassword::class.java)
                        sendText.putExtra("signPhoneNickName", signNickNameInput)
                        startActivity(sendText)
                        navigateToSignPassword()
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // 에러 처리
                    Toast.makeText(
                        this@SignNickname,
                        "Error: " + databaseError.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}
