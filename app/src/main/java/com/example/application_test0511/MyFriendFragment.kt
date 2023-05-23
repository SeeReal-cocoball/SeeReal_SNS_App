package com.example.application_test0511

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application_test0511.databinding.FragmentMyFriendBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyFriendFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyFriendFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentMyFriendBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyFriendBinding.inflate(inflater, container, false)

        // 파이어베이스 데이터베이스
        val database = Firebase.database

        // 현재 유저정보 (임시로 할당)
        val userId = "01012340001"

        // 내 친구들 확인해서 리사이클러 뷰에 표시해주는 함수
        fun checkMyFriends () {

            // 데이터 클래스에 담기 위한 변수
            val myFriends= mutableListOf<FriendData>()

            // 데이터 베이스 경로 할당
            val usersRef = database.getReference("users")
            val userFriends = usersRef.child(userId).child("friends")

            userFriends.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (friendSnapshot in dataSnapshot.children) {
                        val friendId = friendSnapshot.key  // 유저의 친구들 식별자 가져오기

                        // 해당 친구의 정보를 가져오기 위해 users 경로에서 검색
                        val friendRef = usersRef.child(friendId.toString())
                        friendRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                // 해당 친구의 정보를 받아옴
                                val nickname = snapshot.child("nickname").value.toString()
                                val profile = snapshot.child("profile").value?.toString() ?: ""
                                val introduction = snapshot.child("introduction").value?.toString() ?: ""

                                // FriendData 객체를 생성하여 정보를 저장
                                val friendData = FriendData(nickname,null, profile, introduction)
                                myFriends.add(friendData)

                                // 친구들 정보가 다 담아졌을 때 리사이클러 뷰 연결
                                if (myFriends.size == dataSnapshot.childrenCount.toInt()) {
                                    val friendAdapter = FriendAdapter(myFriends)
                                    binding.recyclerviewMF.adapter = friendAdapter
                                    binding.recyclerviewMF.layoutManager = LinearLayoutManager(binding.root.context)
                                }
                            }


                            override fun onCancelled(error: DatabaseError) {
                                Log.w("iise", "Failed to read friend data.", error.toException())
                            }
                        })

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("iise", "Failed to read user's friends.", error.toException())
                }
            })

        }
        checkMyFriends()

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyFriendFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyFriendFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}