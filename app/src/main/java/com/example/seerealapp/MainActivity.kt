package com.example.seerealapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import com.example.seerealapp.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)




/*

//      < test 데이터 추가 코드 >

        data class UserData(
            val nickname: String,
            val password: String? = null,
            val profile: String? = null,
            val introduction: String? = null,
            val friends: Map<String, Boolean>? = null
        )

        fun writeNewUser() {
            val database = Firebase.database
            val userRef = database.getReference("users")
            //userRef.setValue("Hello, World!")
            for (i in 16..20) {
                val user = "010123400${i}"
                userRef.child(user).setValue(UserData("Vwxy$i","123","test","test"))
                Log.d("iise","각 번호 -> $user")
            }
            Log.d("iise", userRef.toString())
        }
        writeNewUser()
*/


    }
}