package com.example.seereal_login


import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.seereal_login.Camera.Camera
import com.example.seereal_login.databinding.ActivityMainBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var databaseRef: DatabaseReference  // database reference 가져오기

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        // 전화번호가 일치하면(기존회원일경우) 비밀번호 입력창으로 넘어간다
        val phone = binding.phone
        val phoneBtn = binding.phonebtn
        val password = binding.originpassword

        // 입력 버튼을 누르고 회원 존재 여부를 판단한다
        // 회원이 존재 하지 않으면 register화면으로 넘어가는 팝업이 뜬다 => 구현해야 함
        phoneBtn.setOnClickListener {
            // 사용자가 입력한 전화번호
            val phoneTxt = phone.text.toString()
            val passwordTxt = password.text.toString()

            // Phone number를 입력하지 않고 버튼을 눌렀을 경우
            if (phoneTxt.isEmpty()) {
                Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show()
            } else {
                //val valueEventListener = object : ValueEventListener
                fun check() {
                    // 데이터베이스에서 데이터를 읽거나 쓰려면 databasereference 인스턴스가 필요하다
                    val firebaseDatabase = FirebaseDatabase.getInstance() // firebase 인스턴스 초기화
                    databaseRef = firebaseDatabase.reference // databasereference 초기화
                    // users 노드에 접근하고, 사용자 정보를 저장하거나 가져올 수 있다.
                    val usersRef: DatabaseReference = firebaseDatabase.getReference("users")  // users 레퍼런스 가져오기
                    usersRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            var count = 0
                            // 모든 outerkey를 확인해서 사용자가 입력한 전화번호와 일치하는 사용자 정보가 존재하는지 확인
                            for (outerSnapshot in dataSnapshot.children) {
                                val outerKey = outerSnapshot.key // 바깥쪽 "key" 값을 가져옴
                                // Log.d("REAL", "Outer Key: $outerKey")
                                if (phoneTxt == outerKey) {
                                    count = 1
                                    break
                                } else continue
                            }
                            if (count == 1) {
                                // 비밀번호 비교를 위한 변수 설정
                                val userCheck = dataSnapshot.child(phoneTxt).child("password").value
                                // 일치 시 로그인 진행
                                if (userCheck.toString() == passwordTxt) {
                                    val intent3 = Intent(this@MainActivity, Camera::class.java)
                                    startActivity(intent3)
                                } else {
                                    Toast.makeText(this@MainActivity, "비밀번호를 확인해주세요.", Toast
                                        .LENGTH_SHORT)
                                        .show()
                                }
                            } else {
                                Toast.makeText(this@MainActivity, "register now", Toast.LENGTH_SHORT)
                                    .show()
                                //  회원가입 진행 여부 popup
                                AlertDialog.Builder(this@MainActivity)
                                    .setTitle("Seereal을 시작해보세요!")
                                    .setMessage("회원정보가 없습니다. 회원가입을 진행하시겠습니까??")
                                    .setPositiveButton(
                                        "Yes",
                                        DialogInterface.OnClickListener { dialog, which ->
                                            // 회원가입을 하겠다고 클릭했을 경우 전화번호 입력창으로 넘어감
                                            val intent =
                                                Intent(this@MainActivity, SignPhoneNumber::class.java)
                                            startActivity(intent)
                                            //dialog.dismiss()
                                            Log.d("REAL", "turn to register page")
                                        })
                                    .setNegativeButton(
                                        "아니요",
                                        DialogInterface.OnClickListener { dialog, which ->
                                            Toast.makeText(
                                                this@MainActivity,
                                                "What?????",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            dialog.dismiss()
                                            Log.d("REAL", "do not register")
                                        })
                                    .setNeutralButton(
                                        "...",
                                        DialogInterface.OnClickListener { dialog, which ->
                                            Toast.makeText(this@MainActivity, "...", Toast.LENGTH_SHORT)
                                                .show()
                                        })
                                    .show()
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
                    })}
                check()
            }

        }


        //      < test 데이터 추가 코드 >
/*
        data class UserData(
            val nickname: String,
            val password: String? = null,
            val profile: String? = null,
            val introduction: String? = null,
            val friends: Map<String, Boolean>? = null
        )

        val animalList = listOf(
            "Cat", "Dog", "Elephant", "Lion", "Tiger",
            "Monkey", "Giraffe", "Kangaroo", "Penguin", "Dolphin",
            "Horse", "Bear", "Wolf", "Fox", "Zebra","Eagle", "Crocodile", "Koala", "Octopus", "Penguin",
            "Squirrel", "Whale", "Cheetah", "Butterfly", "Owl"
        )

        fun writeNewUser() {
            val database = Firebase.database
            val userRef = database.getReference("users")
            //userRef.setValue("Hello, World!")
            for (i in 10..25) {
                val user = "010123400${i}"
                userRef.child(user).setValue(UserData("${animalList[i-2]}$i", "123", "test",
                    "Hello, World! 0$i"))

            }
        }
        writeNewUser()

 */

    }


}
