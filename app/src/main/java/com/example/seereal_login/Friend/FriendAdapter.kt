package com.example.application_test0511

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seereal_login.User
import com.example.seereal_login.databinding.MyfriendsViewBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class FriendAdapter (private val context: Context, val myFriend:MutableList<User>): RecyclerView.Adapter<FriendAdapter
.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MyfriendsViewBinding.inflate(LayoutInflater.from(parent.context), parent,
            false)
        return ViewHolder(binding, context)
    }

    override fun onBindViewHolder(
        holder: FriendAdapter
        .ViewHolder, position: Int
    ) {
        val friend = myFriend.get(position)
        holder.bind(friend)
    }

    override fun getItemCount(): Int {
        return myFriend.size
    }

    // 받아온 데이터 화면에 연결
    class ViewHolder(val binding: MyfriendsViewBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: User) {

            val storageReference = Firebase.storage.reference
            val imageView = binding.imageView4
            val sdf = SimpleDateFormat("yyyyMMdd")
            val time = sdf.format(System.currentTimeMillis())

//            val defaultImageRef = storageReference.child("users/defaultUserImg.png")
//            defaultImageRef.downloadUrl.addOnSuccessListener { uri ->
//
//                Picasso.get().load(uri).into(imageView)
//            }

            val ImageRef = storageReference.child("users/${friend.phone}/profile/${friend
                .phone}_profile.png")
            ImageRef.downloadUrl.addOnSuccessListener { uri ->
                // 이미지 다운로드 성공
                Picasso.get().load(uri).into(imageView)
            }.addOnFailureListener {
                // 이미지 다운로드 실패 시 기본 이미지 등을 처리할 수 있습니다.
                val defaultImageRef = storageReference.child("users/defaultUserImg.png")
                defaultImageRef.downloadUrl.addOnSuccessListener { uri ->

                    Picasso.get().load(uri).into(imageView)
                }
            }

            binding.nickname.text = friend.nickname

            binding.userIntroduction.text = friend.introduction

            val sharedPreferences =
                context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val username = sharedPreferences.getString("user", "")

            binding.delFriendBtn.setOnClickListener {
                val database = Firebase.database
                val userRef = database.getReference("users")
                Log.d("geon_btn_cl_test", "${friend.phone}")
                userRef.child("$username").child("friends").child("${friend.phone}")
                    .removeValue()
            }
        }

    }
}

