package com.saveetha.smarthealthcareapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.saveetha.smarthealthcareapp.models.Hospital

@Suppress("UNCHECKED_CAST")
class HospitalMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var hospitals: List<Hospital>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospital_map)

        // Deserialize list of Serializable hospitals
        hospitals = intent.getSerializableExtra("hospitals") as? List<Hospital> ?: emptyList()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        if (hospitals.isEmpty()) return

        for (hospital in hospitals) {
            val latLng = LatLng(hospital.latitude, hospital.longitude)
            val marker = map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(hospital.name)
                    .snippet(hospital.address)
            )
            marker?.tag = hospital
        }

        map.setOnInfoWindowClickListener { marker ->
            val hospital = marker.tag as? Hospital
            hospital?.let {
                val intent = Intent(this, PatientHospitalViewActivity::class.java)
                intent.putExtra("hospital", it)
                startActivity(intent)
            }
        }

        val first = hospitals[0]
        val firstLatLng = LatLng(first.latitude, first.longitude)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 13f))
    }
}
