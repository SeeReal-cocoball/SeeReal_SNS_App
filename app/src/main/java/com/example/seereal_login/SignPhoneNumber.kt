package com.example.seereal_login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.seereal_login.databinding.ActivitySignPhoneNumberBinding

class SignPhoneNumber : AppCompatActivity() {
    fun navigateToSignNickname() {
        val intent = Intent(this@SignPhoneNumber, SignNickname::class.java)
        startActivity(intent)
    }

    private val binding by lazy { ActivitySignPhoneNumberBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 사용자 이미 있는지 확인 안해도 됨

        var signPhone = binding.signphone
        var signphonebutton = binding.signphonebtn

        signphonebutton.setOnClickListener(){
            // 사용자가 입력한 전화번호
            var signPhoneInput = signPhone.text.toString()
            // 입력한 회원가입 전화번호 비밀번호 페이지로 넘기기
            val sendText = Intent(this, SignPassword::class.java)
            sendText.putExtra("signPhoneNumber", signPhoneInput)
            startActivity(sendText)

            navigateToSignNickname()
        }


    }
}