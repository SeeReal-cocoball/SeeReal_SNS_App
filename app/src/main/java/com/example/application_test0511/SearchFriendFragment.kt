package com.example.application_test0511

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application_test0511.databinding.FragmentSearchFriendBinding
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
 * Use the [SearchFriendFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFriendFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentSearchFriendBinding


    var originalList= mutableListOf<FriendData>()

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
        binding = FragmentSearchFriendBinding.inflate(inflater, container, false)

        // 파이어베이스 데이터베이스
        val database = Firebase.database

        val searchView = binding.searchView
        val usersRef = database.getReference("users")


        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                // 전체 유저 데이터 담아오기
                for (userSnapshot in snapshot.children) {

                    val nickname = userSnapshot.child("nickname").value.toString()
                    val profile = userSnapshot.child("profile").value?.toString() ?: ""
                    val introduction = userSnapshot.child("introduction").value?.toString() ?: ""

                    // userData 객체를 생성하여 정보를 저장
                    val userData = FriendData(nickname,null, profile, introduction)
                    originalList.add(userData)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("iise", "Failed to read friend data.", error.toException())
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                // 검색 버튼을 눌렀을 때 호출됩니다.
                performSearch(query)

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {

                // 검색어가 변경될 때마다 호출됩니다.
                val filteredList = originalList.filter {
                    it.nickname.contains(newText, ignoreCase = true)
                }

                val searchAdapter = SearchAdapter(filteredList as MutableList<FriendData>)
                binding.recyclerviewSF.adapter = searchAdapter
                binding.recyclerviewSF.layoutManager = LinearLayoutManager(binding.root.context)

                searchAdapter.notifyDataSetChanged()
                return true
            }
        })
        return binding.root
    }

    private fun updateRecyclerView(dataList: List<FriendData>) {

        // 어댑터에 새로운 데이터 설정하여 업데이트
        val searchAdapter = SearchAdapter(dataList as MutableList<FriendData>)
        binding.recyclerviewSF.adapter = searchAdapter
        binding.recyclerviewSF.layoutManager = LinearLayoutManager(binding.root.context)

        searchAdapter.notifyDataSetChanged()
    }

    private fun performSearch(query: String) {
        // 검색어를 사용
        val filteredList = originalList.filter { item ->
            item.nickname.contains(query, ignoreCase = true) // 대소문자를 구분하지 않고 검색어를 포함하는지 확인합니다.
        }

        updateRecyclerView(filteredList)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFriendFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFriendFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}