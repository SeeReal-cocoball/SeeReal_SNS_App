package com.example.application_test0511

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seereal_login.User

import com.example.seereal_login.databinding.SearchfriendsViewBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SearchAdapter (private val context: Context, val myFriend:MutableList<User>): RecyclerView
.Adapter<SearchAdapter
.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = SearchfriendsViewBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(binding, context)
    }

    override fun getItemCount(): Int {

        return myFriend.size
    }

    override fun onBindViewHolder(
        holder: SearchAdapter
        .ViewHolder, position: Int
    ) {
        val friend = myFriend.get(position)
        holder.bind(friend)
    }

    class ViewHolder (val binding: SearchfriendsViewBinding, val context: Context ): RecyclerView.ViewHolder
        (binding
        .root) {

        fun bind(friend: User) {
            binding.nickname.text = friend.nickname
            //binding.profilePhoto.text = friend.profile
            binding.userIntroduction.text = friend.introduction

            val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val username = sharedPreferences.getString("user", "")

            binding.addFriendBtn.setOnClickListener{
                val database = Firebase.database
                val userRef = database.getReference("users")
                userRef.child("$username").child("friends").child("${friend.phone}").setValue(true)
            }
        }
    }
}