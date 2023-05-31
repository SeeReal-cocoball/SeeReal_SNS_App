package com.example.application_test0511

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seereal_login.User

import com.example.seereal_login.databinding.SearchfriendsViewBinding

class SearchAdapter (val myFriend:MutableList<User>): RecyclerView.Adapter<SearchAdapter
.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = SearchfriendsViewBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
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

    class ViewHolder (val binding: SearchfriendsViewBinding): RecyclerView.ViewHolder(binding
        .root) {
        fun bind(friend: User) {
            binding.nickname.text = friend.nickname
            //binding.profilePhoto.text = friend.profile
            binding.userIntroduction.text = friend.introduction
        }
    }
}