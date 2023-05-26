package com.example.seereal_login.Camera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.seereal_login.R
import com.example.seereal_login.databinding.ActivityCameraBinding
import com.example.seereal_login.databinding.ActivityMainBinding
// version check
class Camera : AppCompatActivity() {
    private val binding by lazy { ActivityCameraBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}