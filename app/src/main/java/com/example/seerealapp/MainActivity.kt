package com.example.seerealapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.seerealapp.databinding.ActivityMainBinding.*
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    val binding by lazy { inflate(layoutInflater) }


    // 메뉴 바를 생성해서 화면에 붙이기 위한 과정
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        binding.mainToolbar.inflateMenu(R.menu.main_menus)
        return true
    }

    // 레코드로 갈 수 있는 메뉴 아이템을 만들고 해당하는 메뉴아이템을 화면에 할당 후 엑티비티 전환할 수 있도록 할당
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.goToFriendList -> {
                true
            }
            R.id.goToMyProfile -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.mainToolbar)






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