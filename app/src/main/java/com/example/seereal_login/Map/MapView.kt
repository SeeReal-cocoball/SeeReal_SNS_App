package com.example.seereal_login.Map

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.seereal_login.R
import com.example.seereal_login.databinding.ActivityMapViewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class MapView : AppCompatActivity(), OnMapReadyCallback {

    private val binding by lazy { ActivityMapViewBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val user = sharedPreferences.getString("user", "")

        val database = Firebase.database
        val today = SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis())

        val usersRef = database.getReference("users")
        val userFeed = usersRef.child(user.toString())
        userFeed.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val latitude = dataSnapshot.child("feed").child(today).child("latitude").value
                val longitude = dataSnapshot.child("feed").child(today).child("longitude").value

                val todayFeed = LatLng(latitude as Double, longitude as Double)
                googleMap.addMarker(
                    MarkerOptions()
                        .position(todayFeed)
                        .title("이곳에서 추억을 만들었어요!")
                )
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(todayFeed, 12.0F))
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}