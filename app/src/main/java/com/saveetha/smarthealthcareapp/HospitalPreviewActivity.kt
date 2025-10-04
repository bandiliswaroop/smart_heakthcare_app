//package com.saveetha.smarthealthcareapp
//
//import android.net.Uri
//import android.os.Bundle
//import android.view.View
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.TextView
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.saveetha.smarthealthcareapp.adapters.SpecialtyAdapter
//import com.saveetha.smarthealthcareapp.adapters.TechnologyAdapter
//
//class HospitalPreviewActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_hospital_preview)
//
//        val imgHospital = findViewById<ImageView>(R.id.imgHospitalPreview)
//        val tvName = findViewById<TextView>(R.id.tvHospitalName)
//        val tvAbout = findViewById<TextView>(R.id.tvAboutHospital)
//        val tvAddress = findViewById<TextView>(R.id.tvAddress)
//        val tvContact = findViewById<TextView>(R.id.tvContact)
//        val tvEmergency = findViewById<TextView>(R.id.tvEmergency)
//        val rvSpecialties = findViewById<RecyclerView>(R.id.rvSpecialtiesPreview)
//        val rvTechnologies = findViewById<RecyclerView>(R.id.rvTechnologiesPreview)
//        val facilityContainer = findViewById<LinearLayout>(R.id.facilityPreviewContainer)
//
//        // Receive data from intent
//        val name = intent.getStringExtra("name") ?: ""
//        val about = intent.getStringExtra("about") ?: ""
//        val address = intent.getStringExtra("address") ?: ""
//        val contact = intent.getStringExtra("contact") ?: ""
//        val emergency = intent.getStringExtra("emergency") == "1"
//        val logoUri = intent.getStringExtra("logoUri")
//        val specialties = intent.getStringExtra("specialties")?.split(",") ?: emptyList()
//        val technologies = intent.getStringExtra("technologies")?.split(",") ?: emptyList()
//        val facilities = intent.getStringExtra("facilities")?.split(",") ?: emptyList()
//
//        // Bind to UI
//        tvName.text = name
//        tvAbout.text = about
//        tvAddress.text = address
//        tvContact.text = contact
//        tvEmergency.visibility = if (emergency) View.VISIBLE else View.GONE
//
//        if (!logoUri.isNullOrEmpty()) {
//            Glide.with(this).load(Uri.parse(logoUri)).into(imgHospital)
//        }
//
//        // Specialties RecyclerView
//        rvSpecialties.layoutManager = GridLayoutManager(this, 3)
//        rvSpecialties.adapter = SpecialtyAdapter(specialties.map { Specialty(it) })
//
//        // Technologies RecyclerView
//        rvTechnologies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        rvTechnologies.adapter = TechnologyAdapter(technologies.map { Technology(it) })
//
//        // Facilities container
//        facilities.forEach {
//            val parts = it.split(":")
//            if (parts.size == 2) {
//                val view = layoutInflater.inflate(R.layout.item_facility_preview, facilityContainer, false)
//                view.findViewById<TextView>(R.id.tvFacilityName).text = parts[0]
//                view.findViewById<TextView>(R.id.tvFacilityCount).text = parts[1]
//                facilityContainer.addView(view)
//            }
//        }
//    }
//}

//package com.saveetha.smarthealthcareapp
//
//import android.net.Uri
//import android.os.Bundle
//import android.view.View
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.saveetha.smarthealthcareapp.adapters.PreviewSpecialtyAdapter
//import com.saveetha.smarthealthcareapp.adapters.PreviewTechnologyAdapter
//import com.saveetha.smarthealthcareapp.models.TechnologyItem
//
//class HospitalPreviewActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_hospital_preview)
//
//        val imgHospital = findViewById<ImageView>(R.id.imgHospitalPreview)
//        val tvName = findViewById<TextView>(R.id.tvHospitalName)
//        val tvAbout = findViewById<TextView>(R.id.tvAboutHospital)
//        val tvAddress = findViewById<TextView>(R.id.tvAddress)
//        val tvContact = findViewById<TextView>(R.id.tvContact)
//        val tvEmergency = findViewById<TextView>(R.id.tvEmergency)
//        val rvSpecialties = findViewById<RecyclerView>(R.id.rvSpecialtiesPreview)
//        val rvTechnologies = findViewById<RecyclerView>(R.id.rvTechnologiesPreview)
//        val facilityContainer = findViewById<LinearLayout>(R.id.facilityPreviewContainer)
//
//        // Intent Data
//        val name = intent.getStringExtra("name") ?: ""
//        val about = intent.getStringExtra("about") ?: ""
//        val address = intent.getStringExtra("address") ?: ""
//        val contact = intent.getStringExtra("contact") ?: ""
//        val emergency = intent.getStringExtra("emergency") == "1"
//        val logoUri = intent.getStringExtra("logoUri")
//        val specialties = intent.getStringArrayListExtra("specialties") ?: arrayListOf()
//        val techNames = intent.getStringArrayListExtra("technologyNames") ?: arrayListOf()
//        val techUris = intent.getStringArrayListExtra("technologyUris") ?: arrayListOf()
//        val facilities = intent.getStringArrayListExtra("facilities") ?: arrayListOf()
//
//        // Set UI
//        tvName.text = name
//        tvAbout.text = about
//        tvAddress.text = address
//        tvContact.text = contact
//        tvEmergency.visibility = if (emergency) View.VISIBLE else View.GONE
//
//        if (!logoUri.isNullOrEmpty()) {
//            Glide.with(this).load(Uri.parse(logoUri)).into(imgHospital)
//        }
//
//        // Specialties
//        rvSpecialties.layoutManager = GridLayoutManager(this, 3)
//        rvSpecialties.adapter = PreviewSpecialtyAdapter(specialties)
//
//        // Technologies
//        val technologyItems = techNames.zip(techUris) { name, uri -> TechnologyItem(name, Uri.parse(uri)) }
//        rvTechnologies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        rvTechnologies.adapter = PreviewTechnologyAdapter(technologyItems)
//
//        // Facilities
//        facilities.forEach {
//            val parts = it.split(":")
//            if (parts.size == 2) {
//                val view = layoutInflater.inflate(R.layout.item_facility_preview, facilityContainer, false)
//                view.findViewById<TextView>(R.id.tvFacilityName).text = parts[0]
//                view.findViewById<TextView>(R.id.tvFacilityCount).text = parts[1]
//                facilityContainer.addView(view)
//            }
//        }
//    }
//}


package com.saveetha.smarthealthcareapp

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saveetha.smarthealthcareapp.adapters.PreviewSpecialtyAdapter
import com.saveetha.smarthealthcareapp.adapters.PreviewTechnologyAdapter
import com.saveetha.smarthealthcareapp.models.TechnologyItem

class HospitalPreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospital_preview)

        val imgHospital = findViewById<ImageView>(R.id.imgHospitalPreview)
        val tvName = findViewById<TextView>(R.id.tvHospitalName)
        val tvAbout = findViewById<TextView>(R.id.tvAboutHospital)
        val tvAddress = findViewById<TextView>(R.id.tvAddress)
        val tvContact = findViewById<TextView>(R.id.tvContact)
        val tvEmergency = findViewById<TextView>(R.id.tvEmergency)
        val rvSpecialties = findViewById<RecyclerView>(R.id.rvSpecialtiesPreview)
        val rvTechnologies = findViewById<RecyclerView>(R.id.rvTechnologiesPreview)
        val facilityContainer = findViewById<LinearLayout>(R.id.facilityPreviewContainer)

        // Receive data from intent
        val name = intent.getStringExtra("name") ?: ""
        val about = intent.getStringExtra("about") ?: ""
        val address = intent.getStringExtra("address") ?: ""
        val contact = intent.getStringExtra("contact") ?: ""
        val emergency = intent.getStringExtra("emergency") == "1"
        val logoUri = intent.getStringExtra("logoUri")

        val specialties = intent.getStringExtra("specialties")?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
        val technologiesNames = intent.getStringArrayListExtra("technologyNames") ?: arrayListOf()
        val technologiesUris = intent.getStringArrayListExtra("technologyUris") ?: arrayListOf()
        val facilities = intent.getStringExtra("facilities")?.split(",") ?: emptyList()

        // Bind text fields
        tvName.text = name
        tvAbout.text = about
        tvAddress.text = address
        tvContact.text = contact
        tvEmergency.visibility = if (emergency) View.VISIBLE else View.GONE

        if (!logoUri.isNullOrEmpty()) {
            Glide.with(this).load(Uri.parse(logoUri)).into(imgHospital)
        }

        // Setup specialties
        rvSpecialties.layoutManager = GridLayoutManager(this, 3)
        rvSpecialties.adapter = PreviewSpecialtyAdapter(specialties)

        // Setup technologies (combine name + uri into TechnologyItem)
        val techItems = mutableListOf<TechnologyItem>()
        for (i in technologiesNames.indices) {
            val nameItem = technologiesNames[i]
            val uriItem = if (i < technologiesUris.size) technologiesUris[i] else ""
            techItems.add(TechnologyItem(nameItem, uriItem))
        }

        rvTechnologies.layoutManager = GridLayoutManager(this, 2)

        rvTechnologies.adapter = PreviewTechnologyAdapter(techItems)

        // Setup facilities
        facilities.forEach {
            val parts = it.split(":")
            if (parts.size == 2) {
                val view = layoutInflater.inflate(R.layout.item_facility_preview, facilityContainer, false)
                view.findViewById<TextView>(R.id.tvFacilityName).text = parts[0]
                view.findViewById<TextView>(R.id.tvFacilityCount).text = parts[1]
                facilityContainer.addView(view)
            }
        }
    }
}
