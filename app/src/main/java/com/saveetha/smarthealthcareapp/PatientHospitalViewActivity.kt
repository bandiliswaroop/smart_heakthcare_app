

//package com.saveetha.smarthealthcareapp
//
//import android.net.Uri
//import android.os.Bundle
//import android.widget.ImageView
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.bumptech.glide.Glide
//import com.saveetha.smarthealthcareapp.models.Hospital
//
//class PatientHospitalViewActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_patient_hospital_view)
//
//        val hospital = intent.getSerializableExtra("hospital") as? Hospital
//
//        if (hospital == null) {
//            Toast.makeText(this, "Failed to load hospital", Toast.LENGTH_SHORT).show()
//            finish()
//            return
//        }
//
//        val imgHospital = findViewById<ImageView>(R.id.imgHospitalPreview)
//        val tvName = findViewById<TextView>(R.id.tvHospitalName)
//        val tvAbout = findViewById<TextView>(R.id.tvAboutHospital)
//        val tvAddress = findViewById<TextView>(R.id.tvAddress)
//        val tvContact = findViewById<TextView>(R.id.tvContact)
//        val tvEmergency = findViewById<TextView>(R.id.tvEmergency)
//
//        tvName.text = hospital.name
//        tvAbout.text = hospital.about
//        tvAddress.text = hospital.address
//        tvContact.text = hospital.contact
//
//        if (hospital.emergency_services == 1) {
//            tvEmergency.visibility = TextView.VISIBLE
//        } else {
//            tvEmergency.visibility = TextView.GONE
//        }
//
//        // Load image with Glide
//        Glide.with(this)
//            .load(Uri.parse(hospital.logo_path))
//            .placeholder(R.drawable.ic_placeholder_image)
//            .into(imgHospital)
//    }
//}


//
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
//class PatientHospitalViewActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_patient_hospital_view)
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
//
//        val specialties = intent.getStringExtra("specialties")?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
//        val technologiesNames = intent.getStringArrayListExtra("technologyNames") ?: arrayListOf()
//        val technologiesUris = intent.getStringArrayListExtra("technologyUris") ?: arrayListOf()
//        val facilities = intent.getStringExtra("facilities")?.split(",") ?: emptyList()
//
//        // Bind text fields
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
//        // Setup specialties
//        rvSpecialties.layoutManager = GridLayoutManager(this, 3)
//        rvSpecialties.adapter = PreviewSpecialtyAdapter(specialties)
//
//        // Setup technologies (combine name + uri into TechnologyItem)
//        val techItems = mutableListOf<TechnologyItem>()
//        for (i in technologiesNames.indices) {
//            val nameItem = technologiesNames[i]
//            val uriItem = if (i < technologiesUris.size) technologiesUris[i] else ""
//            techItems.add(TechnologyItem(nameItem, uriItem))
//        }
//
//        rvTechnologies.layoutManager = GridLayoutManager(this, 2)
//
//        rvTechnologies.adapter = PreviewTechnologyAdapter(techItems)
//
//        // Setup facilities
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





//FINAL//










//
//
//
//package com.saveetha.smarthealthcareapp
//
//import android.net.Uri
//import android.os.Bundle
//import android.widget.ImageView
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.bumptech.glide.Glide
//import com.saveetha.smarthealthcareapp.models.Hospital
//
//class PatientHospitalViewActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_patient_hospital_view)
//
//        val hospital = intent.getSerializableExtra("hospital") as? Hospital
//
//        if (hospital == null) {
//            Toast.makeText(this, "Failed to load hospital", Toast.LENGTH_SHORT).show()
//            finish()
//            return
//        }
//
//        val imgHospital = findViewById<ImageView>(R.id.imgHospitalPreview)
//        val tvName = findViewById<TextView>(R.id.tvHospitalName)
//        val tvAbout = findViewById<TextView>(R.id.tvAboutHospital)
//        val tvAddress = findViewById<TextView>(R.id.tvAddress)
//        val tvContact = findViewById<TextView>(R.id.tvContact)
//        val tvEmergency = findViewById<TextView>(R.id.tvEmergency)
//
//        tvName.text = hospital.name
//        tvAbout.text = hospital.about
//        tvAddress.text = hospital.address
//        tvContact.text = hospital.contact
//
//        if (hospital.emergency_services == 1) {
//            tvEmergency.visibility = TextView.VISIBLE
//        } else {
//            tvEmergency.visibility = TextView.GONE
//        }
//
//        // Load image with Glide
//        Glide.with(this)
//            .load(Uri.parse(hospital.logo_path))
//            .placeholder(R.drawable.ic_placeholder_image)
//            .into(imgHospital)
//    }
//}


//final................................................................
//package com.saveetha.smarthealthcareapp
//
//import android.net.Uri
//import android.os.Bundle
//import android.view.View
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.saveetha.smarthealthcareapp.adapters.PreviewSpecialtyAdapter
//import com.saveetha.smarthealthcareapp.adapters.PreviewTechnologyAdapter
//import com.saveetha.smarthealthcareapp.models.Hospital
//import com.saveetha.smarthealthcareapp.models.TechnologyItem
//
//class PatientHospitalViewActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_patient_hospital_view)
//
//        val hospital = intent.getSerializableExtra("hospital") as? Hospital
//
//        if (hospital == null) {
//            Toast.makeText(this, "Failed to load hospital", Toast.LENGTH_SHORT).show()
//            finish()
//            return
//        }
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
//        // Set static fields
//        tvName.text = hospital.name
//        tvAbout.text = hospital.about
//        tvAddress.text = hospital.address
//        tvContact.text = hospital.contact
//        tvEmergency.visibility = if (hospital.emergency_services == 1) View.VISIBLE else View.GONE
//
//        Glide.with(this)
//            .load(Uri.parse(hospital.logo_path))
//            .placeholder(R.drawable.ic_placeholder_image)
//            .into(imgHospital)
//
//        // Parse specialties string into list
//        val specialtiesList = hospital.specialties?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
//        rvSpecialties.layoutManager = GridLayoutManager(this, 3)
//        rvSpecialties.adapter = PreviewSpecialtyAdapter(specialtiesList)
//
//        // Parse technologies into TechnologyItem list
//        val techNames = hospital.technologies?.split(",") ?: emptyList()
//        val techImages = hospital.technology_image_uris ?: emptyList()
//        val techItems = mutableListOf<TechnologyItem>()
//
//        for (i in techNames.indices) {
//            val name = techNames[i]
//            val uri = if (i < techImages.size) techImages[i] else ""
//            techItems.add(TechnologyItem(name, uri))
//        }
//
//        rvTechnologies.layoutManager = GridLayoutManager(this, 2)
//        rvTechnologies.adapter = PreviewTechnologyAdapter(techItems)
//
//        // Parse facilities string into name-count and inflate views
//        val facilityList = hospital.facilities?.split(",") ?: emptyList()
//        facilityList.forEach {
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
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saveetha.smarthealthcareapp.adapters.PreviewSpecialtyAdapter
import com.saveetha.smarthealthcareapp.adapters.PreviewTechnologyAdapter
import com.saveetha.smarthealthcareapp.models.Hospital
import com.saveetha.smarthealthcareapp.models.TechnologyItem
import android.content.Intent
import android.widget.Button


class PatientHospitalViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_hospital_view)

        val hospital = intent.getSerializableExtra("hospital") as? Hospital

        if (hospital == null) {
            Toast.makeText(this, "Failed to load hospital", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val imgHospital = findViewById<ImageView>(R.id.imgHospitalPreview)
        val tvName = findViewById<TextView>(R.id.tvHospitalName)
        val tvAbout = findViewById<TextView>(R.id.tvAboutHospital)
        val tvAddress = findViewById<TextView>(R.id.tvAddress)
        val tvContact = findViewById<TextView>(R.id.tvContact)
        val tvEmergency = findViewById<TextView>(R.id.tvEmergency)
        val rvSpecialties = findViewById<RecyclerView>(R.id.rvSpecialtiesPreview)
        val rvTechnologies = findViewById<RecyclerView>(R.id.rvTechnologiesPreview)
        val facilityContainer = findViewById<LinearLayout>(R.id.facilityPreviewContainer)

        // Set static text fields
        tvName.text = hospital.name
        tvAbout.text = hospital.about
        tvAddress.text = hospital.address
        tvContact.text = hospital.contact
        tvEmergency.visibility = if (hospital.emergency_services == 1) View.VISIBLE else View.GONE

        // Load hospital logo with base URL
        val baseUrl = "http://192.168.222.116/smart_healthcare_app/"
        val fullUrl = baseUrl + hospital.logo_path

        Glide.with(this)
            .load(fullUrl)
            .placeholder(R.drawable.ic_placeholder_image)
            .error(R.drawable.ic_placeholder_image)
            .into(imgHospital)


        // Specialties setup
        val specialtiesList = hospital.specialties?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
        rvSpecialties.layoutManager = GridLayoutManager(this, 3)
        rvSpecialties.adapter = PreviewSpecialtyAdapter(specialtiesList)

        // Technologies setup
        val techNames = hospital.technologies?.split(",")?.map { it.trim() } ?: emptyList()

// Clean the string manually (since Gson fails due to escape issues)
        val rawJson = hospital.technology_image_uris?.toString() ?: "[]"
        val cleanList = rawJson
            .removePrefix("[")
            .removeSuffix("]")
            .replace("\"", "")          // remove all quotes
            .replace("\\/", "/")        // fix escaped slashes
            .split(",")
            .map { it.trim() }          // final cleaned list like: uploads/tech_XXX.jpg

        val techItems = mutableListOf<TechnologyItem>()
        for (i in techNames.indices) {
            val name = techNames[i]
            val imagePath = if (i < cleanList.size) cleanList[i] else ""
            val fullUrl = "http://192.168.222.116/smart_healthcare_app/" + imagePath
            Log.d("TECH_IMAGE", "Name=$name, URL=$fullUrl")
            techItems.add(TechnologyItem(name, fullUrl))
        }

        rvTechnologies.layoutManager = GridLayoutManager(this, 2)
        rvTechnologies.adapter = PreviewTechnologyAdapter(techItems)


        // Facilities setup
        val facilityList = hospital.facilities?.split(",") ?: emptyList()
        facilityList.forEach {
            val parts = it.split(":")
            if (parts.size == 2) {
                val view = layoutInflater.inflate(R.layout.item_facility_preview, facilityContainer, false)
                view.findViewById<TextView>(R.id.tvFacilityName).text = parts[0]
                view.findViewById<TextView>(R.id.tvFacilityCount).text = parts[1]
                facilityContainer.addView(view)
            }
        }
        // Get hospitalId from hospital object
        val hospitalId = hospital.id

// Get doctorId if passed from previous screen (optional)
        val doctorId = intent.getIntExtra("doctor_id", -1) // default -1 if not found

        // Book Appointment click
        val btnBook = findViewById<Button>(R.id.btnBookAppointment)
        btnBook.setOnClickListener {
            val intent = Intent(this, BookAppointmentActivity::class.java)
            startActivity(intent)
        }
            //..............................................................//
//        val intent = Intent(this, BookAppointmentActivity::class.java)
//        intent.putExtra("doctor_id", doctorId)
//        intent.putExtra("hospital_id", hospitalId)
//        startActivity(intent)
        btnBook.setOnClickListener {
            val intent = Intent(this, BookAppointmentActivity::class.java)
            intent.putExtra("doctor_id", doctorId)
            intent.putExtra("hospital_id", hospitalId)
            startActivity(intent)
        }


//.......................................................................//
        val btnDirections = findViewById<View>(R.id.btnGetDirections)
        btnDirections.setOnClickListener {
            val address = hospital.address.trim()
            if (address.isNotEmpty()) {
                val mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address))
                val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
                mapIntent.setPackage("com.google.android.apps.maps")

                if (mapIntent.resolveActivity(packageManager) != null) {
                    startActivity(mapIntent)
                } else {
                    Toast.makeText(this, "Google Maps is not installed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Address not available", Toast.LENGTH_SHORT).show()
            }
        }

    }
}


