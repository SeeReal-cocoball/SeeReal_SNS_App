package com.example.application_test0511

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.application_test0511.FriendData
import com.example.application_test0511.databinding.MyfriendsVeiwBinding

class FriendAdapter (val myFriend:MutableList<FriendData>): RecyclerView.Adapter<FriendAdapter
.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MyfriendsVeiwBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
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
    class ViewHolder(val binding:MyfriendsVeiwBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: FriendData) {
            binding.nickname.text = friend.nickname
            binding.profilePhoto.text = friend.profile
            binding.userIntroduction.text = friend.introduction
        }
    }

}