package com.example.application_test0511

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.application_test0511.databinding.SearchfriendsVeiwBinding

class SearchAdapter (val myFriend:MutableList<FriendData>): RecyclerView.Adapter<SearchAdapter
.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = SearchfriendsVeiwBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
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

    class ViewHolder (val binding: SearchfriendsVeiwBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: FriendData) {
            binding.nickname.text = friend.nickname
            binding.profilePhoto.text = friend.profile
            binding.userIntroduction.text = friend.introduction
        }
    }
}