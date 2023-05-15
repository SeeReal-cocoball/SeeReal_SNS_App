package com.example.seereal_login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.seereal_login.databinding.ActivityMainBinding
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        class MyFirebaseMessagingService : FirebaseMessagingService() {
            override fun onNewToken(token: String) {
                super.onNewToken(token)
                // 토큰이 갱실될 때 마다 서버에 토큰을 전달하여 갱신처리를 해야함.
            }
            override fun onMessageReceived(remoteMessage: RemoteMessage) {
                // FCM 메시지 수신 시 처리할 코드 작성
            }
        }
    }
}