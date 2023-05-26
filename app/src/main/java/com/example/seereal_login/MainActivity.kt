package com.example.seereal_login

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

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var databaseRef: DatabaseReference  // database reference 가져오기

    // PasswordPage로 전환하는 함수
    fun navigateToPasswordPage() {
        val intent = Intent(this@MainActivity, PasswordPage::class.java)
        startActivity(intent)
    }

    fun navigateToRegisterPage() {
        val intent2 = Intent(this@MainActivity, SignPhoneNumber::class.java)
        startActivity(intent2)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 데이터베이스에서 데이터를 읽거나 쓰려면 databasereference 인스턴스가 필요하다
        val firebaseDatabase = FirebaseDatabase.getInstance() // firebase 인스턴스 초기화
        databaseRef = firebaseDatabase.reference // databasereference 초기화

        // users 노드에 접근하고, 사용자 정보를 저장하거나 가져올 수 있다.
        val usersRef: DatabaseReference = firebaseDatabase.getReference("users")  // users 레퍼런스 가져오기

        // 전화번호가 일치하면(기존회원일경우) 비밀번호 입력창으로 넘어간다
        val phone = binding.phone
        val phoneBtn = binding.phonebtn

        binding.camera.setOnClickListener{
            val intent3 = Intent(this@MainActivity, Camera::class.java)
            startActivity(intent3)
        }


        // 입력 버튼을 누르고 회원 존재 여부를 판단한다
        // 회원이 존재 하지 않으면 register화면으로 넘어가는 팝업이 뜬다 => 구현해야 함
        phoneBtn.setOnClickListener() {
            // 사용자가 입력한 전화번호
            val phoneTxt = phone.text.toString()

            // Phone number를 입력하지 않고 버튼을 눌렀을 경우
            if (phoneTxt.isEmpty()) {Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show()}

            //val valueEventListener = object : ValueEventListener
            usersRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // 모든 outerkey를 확인해서 사용자가 입력한 전화번호와 일치하는 사용자 정보가 존재하는지 확인
                    for (outerSnapshot in dataSnapshot.children) {
                        val outerKey = outerSnapshot.key // 바깥쪽 "key" 값을 가져옴
                        Log.d("REAL", "Outer Key: $outerKey")

                        // 안쪽 key값 가져오기
//                        for (innerSnapshot in outerSnapshot.children) {
//                            val innerKey = innerSnapshot.key // 안쪽 "key" 값을 가져옴
//                            val innerValue = innerSnapshot.value // 해당 "key"에 대한 값을 가져옴
//                            Log.d("REAL", "Inner Key: $innerKey, Value: " +
//                                    "$innerValue")
//                        }
                        if(phoneTxt == outerKey){
                            Toast.makeText(this@MainActivity, "welcome", Toast.LENGTH_SHORT).show()
                            break;
                        } else{
                            Toast.makeText(this@MainActivity, "register now", Toast.LENGTH_SHORT).show()
                            //  회원가입 진행 여부 popup
                            AlertDialog.Builder(this@MainActivity)
                                .setTitle("Seereal을 시작해보세요!")
                                .setMessage("회원가입을 진행하시겠습니까??")
                                .setPositiveButton("네네!", DialogInterface.OnClickListener { dialog, which ->
                                    // 회원가입을 하겠다고 클릭했을 경우 전화번호 입력창으로 넘어감
                                    val intent = Intent(this@MainActivity, SignPhoneNumber::class.java)
                                    intent.putExtra("newPhoneNumber", phoneTxt) // 입력한 번화번호 넘겨주기
                                    startActivity(intent)
                                    })
                                .setNegativeButton("아니요", DialogInterface.OnClickListener { dialog, which ->
                                    Toast.makeText(this@MainActivity, "What?????", Toast.LENGTH_SHORT).show()
                                })
                                .setNeutralButton("...",DialogInterface.OnClickListener { dialog, which ->
                                    Toast.makeText(this@MainActivity, "...", Toast.LENGTH_SHORT).show() })
                                .show()
                        }
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
                })
            }
        }
    }


