package com.example.seereal_login


data class User(val phonenumber: String? = null, val nickname: String? = null,var password:String?=null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}
