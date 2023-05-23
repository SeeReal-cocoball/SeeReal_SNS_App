package com.example.application_test0511

data class FriendData(
    val nickname: String,
    val password: String? = null,
    val profile: String? = null,
    val introduction: String? = null,
    val friends: Map<String, Boolean>? = null
)

// 데이터 클래스 null 처리한 이유는 다양하게 활용할 때 값이 안들어가는 경우 많음