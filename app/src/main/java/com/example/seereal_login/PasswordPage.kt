package com.example.seereal_login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.seereal_login.databinding.ActivityMainBinding
import com.example.seereal_login.databinding.ActivityPasswordPageBinding

class PasswordPage : AppCompatActivity() {
    private val binding by lazy { ActivityPasswordPageBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val passwordTextView = binding.nickname

        val password = intent.getStringExtra("nicknametopassword") // Intent로 전달된 텍스트 정보 가져오기
        passwordTextView.text = password // TextView에 텍스트 설정

        // 비밀번호 check 구현
    }
}