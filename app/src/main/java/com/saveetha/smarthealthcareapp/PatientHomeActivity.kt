//package com.saveetha.smarthealthcareapp
//
//import android.Manifest
//import android.app.Activity
//import android.content.ActivityNotFoundException
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.location.Geocoder
//import android.net.Uri
//import android.os.Bundle
//import android.speech.RecognizerIntent
//import android.util.Log
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.widget.SearchView
//import androidx.core.app.ActivityCompat
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices
//import com.google.android.material.button.MaterialButton
//import com.saveetha.smarthealthcareapp.adapters.HospitalAdapter
//import com.saveetha.smarthealthcareapp.ApiService
//import com.saveetha.smarthealthcareapp.models.Hospital
//import com.saveetha.smarthealthcareapp.network.RetrofitClient
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.util.*
//import kotlin.jvm.java
//
//class PatientHomeActivity : AppCompatActivity() {
//
//    private lateinit var rvHospitals: RecyclerView
//    private lateinit var hospitalAdapter: HospitalAdapter
//    private val hospitals = mutableListOf<Hospital>()
//
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    private lateinit var searchView: SearchView
//    private lateinit var btnVoiceSearch: ImageView
//
//    private val LOCATION_PERMISSION_REQUEST_CODE = 101
//    private val VOICE_RECOGNITION_REQUEST_CODE = 102
//
//    private var currentLat: Double? = null
//    private var currentLng: Double? = null
//    private var currentLocality: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_patient_home)
//
//        val btnTips = findViewById<LinearLayout>(R.id.btnTips)
//        val emergencyButton = findViewById<MaterialButton>(R.id.emergencyButton)
//        val tvLocation = findViewById<TextView>(R.id.tvLocation)
//        btnVoiceSearch = findViewById(R.id.btnVoiceSearch) // 🔊 Mic button for voice search
//        rvHospitals = findViewById(R.id.rvHospitals)
//        searchView = findViewById(R.id.searchView)
//
//        // 📢 Voice Search Click Listener
//        btnVoiceSearch.setOnClickListener {
//            startVoiceRecognition()
//        }
//        val imgProfile = findViewById<ImageView>(R.id.imgProfile)
//        loadProfileImage(imgProfile)
//
//        imgProfile.setOnClickListener {
//            val userId = intent.getStringExtra("user_id") ?: ""
//            val intent = Intent(this, PatientProfileActivity::class.java)
//            intent.putExtra("user_id", userId)
//            startActivity(intent)
//        }
//
//
//        // 🔍 SearchView Listener
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                filterHospitals(query)
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                filterHospitals(newText)
//                return true
//            }
//        })
//
//        // 📍 Location Click Listener (opens Maps)
//        tvLocation.setOnClickListener {
//            openNearbyHospitalsInMaps()
//        }
//
//        // 🏥 RecyclerView setup
//        hospitalAdapter = HospitalAdapter(this, hospitals)
//        rvHospitals.layoutManager = LinearLayoutManager(this)
//        rvHospitals.adapter = hospitalAdapter
//
//        // 📡 Location Services Setup
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        checkLocationPermission()
//
//        // 🌐 Load Hospital Data
//        loadHospitals()
//
//        val btnDoctor = findViewById<LinearLayout>(R.id.btndoctor)
//        btnDoctor.setOnClickListener {
//            val intent = Intent(this, PatientDoctorSearchActivity::class.java)
//            startActivity(intent)
//        }
//
//        // 💡 Health Tips Button
//        btnTips.setOnClickListener {
//            startActivity(Intent(this, ActivityHealthyTips::class.java))
//        }
//
//        val btnLabs = findViewById<LinearLayout>(R.id.btnLabs)
//        btnLabs.setOnClickListener {
//            val intent = Intent(this, NearbyLabsMapActivity::class.java)
//            startActivity(intent)
//        }
//
//
//
//        // 🚨 Emergency Button
//        emergencyButton.setOnClickListener {
//            startActivity(Intent(this, EmergencyActivity::class.java))
//        }
//
//    }
//
//    // ✅ Start voice recognition using built-in speech recognizer
//    private fun startVoiceRecognition() {
//        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak a hospital name or location")
//
//        try {
//            startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE)
//        } catch (e: ActivityNotFoundException) {
//            Toast.makeText(this, "Voice search not supported", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    // ✅ Handle voice input result and filter hospitals
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
//            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
//            if (!result.isNullOrEmpty()) {
//                val spokenText = result[0]
//                searchView.setQuery(spokenText, true) // Submit query to filter
//            }
//        }
//    }
//
//    private fun checkLocationPermission() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//            != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                LOCATION_PERMISSION_REQUEST_CODE
//            )
//        } else {
//            fetchLocation()
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()
//            && grantResults[0] == PackageManager.PERMISSION_GRANTED
//        ) {
//            fetchLocation()
//        }
//    }
//
//    private fun fetchLocation() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//            != PackageManager.PERMISSION_GRANTED
//        ) return
//
//        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//            if (location != null) {
//                currentLat = location.latitude
//                currentLng = location.longitude
//
//                val geocoder = Geocoder(this, Locale.getDefault())
//                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
//
//                if (!addresses.isNullOrEmpty()) {
//                    val address = addresses[0]
//
//                    // Log all possible components to help debug
//                    Log.d("FullAddress", "FeatureName: ${address.featureName}")
//                    Log.d("FullAddress", "SubLocality: ${address.subLocality}")
//                    Log.d("FullAddress", "Locality: ${address.locality}")
//                    Log.d("FullAddress", "SubAdminArea: ${address.subAdminArea}")
//                    Log.d("FullAddress", "AdminArea: ${address.adminArea}")
//                    Log.d("FullAddress", "PostalCode: ${address.postalCode}")
//                    Log.d("FullAddress", "CountryName: ${address.countryName}")
//                    Log.d("FullAddress", "FullAddressLine: ${address.getAddressLine(0)}")
//
//                    // Compose full location manually, skipping null or empty parts
//                    val locationParts = listOf(
//                        address.subLocality,
//                        address.locality,
//                        address.subAdminArea,
//                        address.adminArea,
//                        address.countryName
//                    ).filterNotNull().filter { it.isNotEmpty() }
//
//                    val displayLocation = locationParts.joinToString(", ")
//                    findViewById<TextView>(R.id.tvLocation).text = "📍$displayLocation"
//
//                    // Use for sorting logic
//                    currentLocality = address.locality ?: address.subAdminArea ?: address.adminArea
//                } else {
//                    findViewById<TextView>(R.id.tvLocation).text = "📍Location not found"
//                }
//
//                sortHospitalsByLocalityAndDistance()
//            } else {
//                Log.w("Location", "Location is null")
//            }
//        }.addOnFailureListener {
//            Log.e("LocationError", it.message ?: "Failed to get location")
//        }
//    }
//
//
//    private fun loadHospitals() {
//        val api = RetrofitClient.instance.create(ApiService::class.java)
//        api.getHospitals().enqueue(object : Callback<List<Hospital>> {
//            override fun onResponse(call: Call<List<Hospital>>, response: Response<List<Hospital>>) {
//                if (response.isSuccessful && response.body() != null) {
//                    hospitals.clear()
//                    hospitals.addAll(response.body()!!)
//
//                    if (currentLat != null && currentLng != null) {
//                        sortHospitalsByLocalityAndDistance()
//                    } else {
//                        hospitalAdapter.notifyDataSetChanged()
//                    }
//                } else {
//                    Toast.makeText(this@PatientHomeActivity, "No hospitals found", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<List<Hospital>>, t: Throwable) {
//                Log.e("HospitalError", t.message ?: "Unknown error")
//                Toast.makeText(this@PatientHomeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    private fun sortHospitalsByLocalityAndDistance() {
//        if (currentLat == null || currentLng == null) return
//
//        hospitals.sortWith(compareBy(
//            { !it.address.contains(currentLocality ?: "", ignoreCase = true) },
//            { distanceBetween(currentLat!!, currentLng!!, it.latitude, it.longitude) }
//        ))
//
//        hospitalAdapter.updateList(hospitals)
//    }
//
//    private fun distanceBetween(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
//        val result = FloatArray(1)
//        android.location.Location.distanceBetween(lat1, lng1, lat2, lng2, result)
//        return result[0] // in meters
//    }
//
//    private fun filterHospitals(query: String?) {
//        val filteredList = if (!query.isNullOrEmpty()) {
//            hospitals.filter {
//                it.name.contains(query, ignoreCase = true) ||
//                        it.address.contains(query, ignoreCase = true)
//            }
//        } else {
//            hospitals
//        }
//
//        hospitalAdapter.updateList(filteredList)
//    }
//
//    private fun openNearbyHospitalsInMaps() {
//        if (currentLat != null && currentLng != null) {
//            val uri = Uri.parse("geo:$currentLat,$currentLng?q=hospitals")
//            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
//            mapIntent.setPackage("com.google.android.apps.maps")
//
//            if (mapIntent.resolveActivity(packageManager) != null) {
//                startActivity(mapIntent)
//            } else {
//                val fallback = Intent(
//                    Intent.ACTION_VIEW,
//                    Uri.parse("https://www.google.com/maps/search/hospitals/@$currentLat,$currentLng,15z")
//                )
//                startActivity(fallback)
//            }
//        } else {
//            Toast.makeText(this, "Current location not available", Toast.LENGTH_SHORT).show()
//        }
//    }
//    private fun loadProfileImage(imageView: ImageView) {
//        val userId = intent.getStringExtra("user_id") ?: return
//
//        Glide.with(this)
//            .load("http://192.168.24.116/smart_healthcare_app/profile_images/profile_$userId.jpg")
//            .placeholder(R.drawable.ic_profile)
//            .error(R.drawable.ic_profile)
//            .circleCrop()
//            .into(imageView)
//    }
//}















package com.saveetha.smarthealthcareapp

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.saveetha.smarthealthcareapp.adapters.HospitalAdapter
import com.saveetha.smarthealthcareapp.models.Hospital
import com.saveetha.smarthealthcareapp.network.ApiService
import com.saveetha.smarthealthcareapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PatientHomeActivity : AppCompatActivity() {

    private lateinit var rvHospitals: RecyclerView
    private lateinit var hospitalAdapter: HospitalAdapter
    private val hospitals = mutableListOf<Hospital>()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var searchView: SearchView
    private lateinit var btnVoiceSearch: ImageView

    private val LOCATION_PERMISSION_REQUEST_CODE = 101
    private val VOICE_RECOGNITION_REQUEST_CODE = 102

    private var currentLat: Double? = null
    private var currentLng: Double? = null
    private var currentLocality: String? = null

    private lateinit var imgProfile: ImageView
    private var userId: String = ""
    private val PROFILE_UPDATE_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_home)

        // Initialize views
        rvHospitals = findViewById(R.id.rvHospitals)
        searchView = findViewById(R.id.searchView)
        btnVoiceSearch = findViewById(R.id.btnVoiceSearch)
        imgProfile = findViewById(R.id.imgProfile)

        // Get user ID from intent
        userId = intent.getStringExtra("user_id") ?: ""

        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Setup RecyclerView
        hospitalAdapter = HospitalAdapter(this, hospitals)
        rvHospitals.layoutManager = LinearLayoutManager(this)
        rvHospitals.adapter = hospitalAdapter

        // Load profile image
        loadProfileImage()

        // Set up click listeners
        setupClickListeners()

        // Check location permission and fetch location
        checkLocationPermission()

        // Load hospital data
        loadHospitals()

    }

    private fun setupClickListeners() {
        // Profile image click
        imgProfile.setOnClickListener {
            val email = intent.getStringExtra("user_email")
            val intent = Intent(this, PatientProfileActivity::class.java).apply {
                putExtra("user_id", userId)
                putExtra("user_email", email)
            }
            startActivityForResult(intent, PROFILE_UPDATE_REQUEST_CODE)
        }



        // Voice search click
        btnVoiceSearch.setOnClickListener {
            startVoiceRecognition()
        }

        // Location text click
        findViewById<TextView>(R.id.tvLocation).setOnClickListener {
            openNearbyHospitalsInMaps()
        }

        // Search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterHospitals(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterHospitals(newText)
                return true
            }
        })

        // Menu buttons
        findViewById<LinearLayout>(R.id.btnTips).setOnClickListener {
            startActivity(Intent(this, ActivityHealthyTips::class.java))
        }

        findViewById<LinearLayout>(R.id.btndoctor).setOnClickListener {
            startActivity(Intent(this, PatientDoctorSearchActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.btnLabs).setOnClickListener {
            startActivity(Intent(this, NearbyLabsMapActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.emergencyButton).setOnClickListener {
            startActivity(Intent(this, EmergencyActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.btnbot) // ✅ if it's LinearLayout
            .setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }
    }

    private fun loadProfileImage() {
        // Check SharedPreferences first
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val savedImageUrl = sharedPref.getString("profile_image_patient_$userId", null)

        val imageUrl = savedImageUrl ?:
        "http://192.168.24.116/smart_healthcare_app/patient_profiles/profile_$userId.jpg"

        Glide.with(this)
            .load("$imageUrl?t=${System.currentTimeMillis()}") // Cache busting
            .placeholder(R.drawable.ic_profile)
            .error(R.drawable.ic_profile)
            .circleCrop()
            .into(imgProfile)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Handle profile update result
        if (requestCode == PROFILE_UPDATE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            loadProfileImage()
        }

        // Handle voice recognition result
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!result.isNullOrEmpty()) {
                val spokenText = result[0]
                searchView.setQuery(spokenText, true)
            }
        }
    }

    private fun startVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak a hospital name or location")
        }

        try {
            startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Voice search not supported", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            fetchLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchLocation()
        }
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) return

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLat = location.latitude
                currentLng = location.longitude

                val geocoder = Geocoder(this, Locale.getDefault())
                try {
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    if (!addresses.isNullOrEmpty()) {
                        val address = addresses[0]
                        currentLocality = address.locality ?: address.subAdminArea ?: address.adminArea

                        // Build display location string
                        val locationParts = listOf(
                            address.subLocality,
                            address.locality,
                            address.subAdminArea,
                            address.adminArea,
                            address.countryName
                        ).filterNotNull().filter { it.isNotEmpty() }

                        findViewById<TextView>(R.id.tvLocation).text = "📍${locationParts.joinToString(", ")}"
                    } else {
                        findViewById<TextView>(R.id.tvLocation).text = "📍Location found (details unavailable)"
                    }
                } catch (e: Exception) {
                    Log.e("GeocoderError", "Error getting location details", e)
                    findViewById<TextView>(R.id.tvLocation).text = "📍${location.latitude}, ${location.longitude}"
                }

                sortHospitalsByLocalityAndDistance()
            } else {
                Log.w("Location", "Location is null")
                Toast.makeText(this, "Couldn't get current location", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            Log.e("LocationError", "Failed to get location", e)
            Toast.makeText(this, "Location error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadHospitals() {
        val api = RetrofitClient.instance.create(ApiService::class.java)
        api.getHospitals().enqueue(object : Callback<List<Hospital>> {
            override fun onResponse(call: Call<List<Hospital>>, response: Response<List<Hospital>>) {
                if (response.isSuccessful && response.body() != null) {
                    hospitals.clear()
                    hospitals.addAll(response.body()!!)

                    if (currentLat != null && currentLng != null) {
                        sortHospitalsByLocalityAndDistance()
                    } else {
                        hospitalAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(this@PatientHomeActivity, "No hospitals found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Hospital>>, t: Throwable) {
                Log.e("HospitalError", "Failed to load hospitals", t)
                Toast.makeText(
                    this@PatientHomeActivity,
                    "Error loading hospitals: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun sortHospitalsByLocalityAndDistance() {
        if (currentLat == null || currentLng == null) return

        hospitals.sortWith(compareBy(
            { !it.address.contains(currentLocality ?: "", ignoreCase = true) },
            { distanceBetween(currentLat!!, currentLng!!, it.latitude, it.longitude) }
        ))

        hospitalAdapter.updateList(hospitals)
    }

    private fun distanceBetween(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
        val result = FloatArray(1)
        android.location.Location.distanceBetween(lat1, lng1, lat2, lng2, result)
        return result[0] // in meters
    }

    private fun filterHospitals(query: String?) {
        val filteredList = if (!query.isNullOrEmpty()) {
            hospitals.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.address.contains(query, ignoreCase = true)
            }
        } else {
            hospitals
        }

        hospitalAdapter.updateList(filteredList)
    }

    private fun openNearbyHospitalsInMaps() {
        if (currentLat != null && currentLng != null) {
            val uri = Uri.parse("geo:$currentLat,$currentLng?q=hospitals")
            val mapIntent = Intent(Intent.ACTION_VIEW, uri).apply {
                setPackage("com.google.android.apps.maps")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            try {
                startActivity(mapIntent)
            } catch (e: ActivityNotFoundException) {
                // Fallback to browser-based maps
                val fallback = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/maps/search/hospitals/@$currentLat,$currentLng,15z")
                )
                startActivity(fallback)
            }
        } else {
            Toast.makeText(this, "Current location not available", Toast.LENGTH_SHORT).show()
            checkLocationPermission()
        }
    }
}
