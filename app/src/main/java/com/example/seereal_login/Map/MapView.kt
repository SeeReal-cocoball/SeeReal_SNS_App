package com.example.seereal_login.Map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.seereal_login.R
import com.example.seereal_login.databinding.ActivityMapViewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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

                val path = "users/$user/feed/$today.jpg"
                val todayFeed = LatLng(latitude as Double, longitude as Double)

                downloadImageFromFirebaseStorage(path, onSuccess = { bitmap ->
                    // 이미지 다운로드 성공
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(todayFeed)
                            .title("이곳에서 추억을 만들었어요!")
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                    )
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(todayFeed, 14.0F))
                },
                    onFailure = { exception ->
                        // 이미지 다운로드 실패
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(todayFeed)
                                .title("이곳에서 추억을 만들었어요!")
                        )
                    })

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(todayFeed, 14.0F))
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun downloadImageFromFirebaseStorage(path: String, onSuccess: (Bitmap) -> Unit, onFailure: (Exception) -> Unit) {
        val storageRef = Firebase.storage.reference

        storageRef.child(path).getBytes(Long.MAX_VALUE).addOnSuccessListener { bytes ->
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            onSuccess(bitmap)
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }
}