package com.saveetha.smarthealthcareapp
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.saveetha.smarthealthcareapp.network.ApiService
import okhttp3.ResponseBody
import com.saveetha.smarthealthcareapp.network.RetrofitClient

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DoctorHomeActivity : AppCompatActivity() {

    private lateinit var spinnerState: Spinner
    private lateinit var spinnerDistrict: Spinner
    private lateinit var spinnerDivision: Spinner
    private lateinit var spinnerMandal: Spinner
    private lateinit var spinnerGP: Spinner
    private lateinit var saveButton: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_home)

        spinnerState = findViewById(R.id.spinnerState)
        spinnerDistrict = findViewById(R.id.spinnerDistrict)
        spinnerDivision = findViewById(R.id.spinnerDivision)
        spinnerMandal = findViewById(R.id.spinnerMandal)
        spinnerGP = findViewById(R.id.spinnerGP)
        saveButton = findViewById(R.id.saveButton)

        setupSpinners()

        saveButton.setOnClickListener {
            val selectedState = spinnerState.selectedItem.toString()
            val selectedDistrict = spinnerDistrict.selectedItem.toString()
            val selectedDivision = spinnerDivision.selectedItem.toString()
            val selectedMandal = spinnerMandal.selectedItem.toString()
            val selectedGP = spinnerGP.selectedItem.toString()

            val userId = getSharedPreferences("MyPrefs", MODE_PRIVATE).getInt("user_id", -1)
            if (userId == -1) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ UPDATED: Call API to save doctor hospital address
            val apiService = RetrofitClient.instance.create(ApiService::class.java)

            val call = apiService.saveDoctorHospitalAddress(
                userId,
                selectedState,
                selectedDistrict,
                selectedDivision,
                selectedMandal,
                selectedGP
            )

            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@DoctorHomeActivity, "Address saved!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@DoctorHomeActivity, DoctorSlotActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@DoctorHomeActivity, "Failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@DoctorHomeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })

        }
    }

    private fun setupSpinners() {
        val sampleStates = listOf("Select State", "Andhra Pradesh")
        val sampleDistricts = listOf("Select District", "Alluri Sitharama Raju", "Anakapalli", "Ananthapuramu", "Annamayya", "Bapatla", "Chittoor", "Dr. B.R. Ambedkar Konaseema", "East Godavari", "Eluru",
            "Guntur", "Kakinada", "Krishna", "Kurnool", "Nandyal", "Ntr", "Palnadu", "Parvathipuram Manyam", "Prakasam", "Srikakulam", "Sri Potti Sriramulu Nellore", "Sri Sathya Sai", "Tirupati", "Visakhapatnam",
            "Vizianagaram", "West Godavari", "Y.S.R. Kadapa")

        val districtToDivisionsMap = mapOf(
            "East Godavari" to listOf("Select Division", "RAJAMAHENDRAVARAM", "KOVVURU"),
            "Alluri Sitharama Raju" to listOf("Select Division", "PADERU", "RAMPACHODAVARAM"),
            "Anakapalli" to listOf("Select Division", "ANAKAPALLI", "NARSIPATNAM"),
            "Ananthapuramu" to listOf("Select Division", "Anantapur", "Kalyandurg", "Penukonda"),
            "Annamayya" to listOf("Select Division", "Rayachoti", "Rajampeta", "Madanapalle"),
            "Bapatla" to listOf("Select Division", "Bapatla", "Chirala", "Addanki"),
            "Chittoor" to listOf("Select Division", "Chittoor", "Palamaner", "Madanapalle"),
            "Dr. B.R. Ambedkar Konaseema" to listOf("Select Division", "Amalapuram", "Razole", "Ramachandrapuram"),
            "Eluru" to listOf("Select Division", "Eluru", "Jangareddygudem", "Nuzvid"),
            "Guntur" to listOf("Select Division", "Guntur", "Tenali", "Narasaraopet"),
            "Kakinada" to listOf("Select Division", "Kakinada", "Peddapuram", "Pithapuram", "Tuni", "Rampachodavaram"),
            "Krishna" to listOf("Select Division", "Vijayawada", "Machilipatnam", "Nuzvid"),
            "Kurnool" to listOf("Select Division", "Kurnool", "Adoni", "Pattikonda"),
            "Nandyal" to listOf("Select Division", "Nandyal", "Atmakur", "Dhone"),
            "NTR"     to listOf("Select Division", "Vijayawada", "Nandigama", "Tiruvuru"),
            "Palnadu" to listOf("Select Division", "Narasaraopet", "Sattenapalli", "Gurazala"),
            "parvathipuram_manyam" to listOf("Select Division","Parvathipuram", "Palakonda"),
            "Prakasam" to listOf("Select Division", "Ongole", "Kanigiri", "Markapuram"),
            "Srikakulam" to listOf("Select Division", "Palasa", "Tekkali", "Srikakulam"),
            "Sri Potti Sriramulu Nellore" to listOf("Select Division", "Nellore", "Atmakur", "Kavali", "Kandukur"),
            "Sri Sathya Sai" to listOf("Select Division", "Dharmavaram", "Kadiri", "Penukonda", "Puttaparthi"),
            "Tirupati" to listOf("Select Division", "Gudur", "Sullurupeta", "Srikalahasti", "Tirupati"),
            "Visakhapatnam" to listOf("Select Division", "Bheemunipatnam", "Visakhapatnam"),
            "Vizianagaram" to listOf("Select Division", "Vizianagaram", "Parvathipuram", "Bobbili"),
            "West Godavari" to listOf("Select Division", "Eluru", "Jangareddygudem", "Narasapuram"),
            "Y.S.R. Kadapa" to listOf("Select Division", "Kadapa", "Jammalamadugu", "Badvel", "Pulivendula"),


        )

        val divisionToMandalsMap = mapOf(
            "RAJAMAHENDRAVARAM" to listOf("Select Mandal", "RANGAMPETA", "GOKAVARAM", "KADIAM", "KORUKONDA", "RAJAMAHENDRAVARAM RURAL", "RAJAMAHENDRAVARAM URBAN", "RAJANAGARAM", "SEETHANAGRAM","ANAPARTHI","BICCAVOLE"),
            "KOVVURU" to listOf("Select Mandal", "TALLAPUDI", "GOPALAPURAM","NALLAJERLA", "DEVARAPALLE", "KOVVUR", "CHAGALLU", "NIDADAVOLE", "UNDRAJAVARAM", "PERAVALI"),

            "PADERU" to listOf("Select Mandal", "ANANTHAGIRI", "ARAKUVALLEY", "CHINTHAPALLI", "DUMBRIGUDA", "G.K.VEEDHI", "G.MADUGULA", "HUKUMPETA", "KOYYURU", "MUNCHINGPUT", "PADERU", "PEDABAYULU"),
            "RAMPACHODAVARAM" to listOf("Select Mandal", "MAREDUMILLI", "DEVIPATNAM", "Y.RAMAVARAM", "ADDATEEGALA", "GANGAVARAM", "RAMPACHODAVARAM", "RAJAVOMMANGI", "KUNAVARAM", "CHINTOOR", "VARARAMACHANDRAPURAM", "NELLIPAKA"),

            "ANAKAPALLI" to listOf("Select Mandal", "ANAKAPALLI", "ATCHUTAPURAM", "BUTCHIAHPETA", "KASIMKOTA", "CHODAVARAM", "DEVARAPALLI", "ELAMANCHILI", "K.KOTAPADU", "RAMBILLI", "MUNAGAPAKA", "PARAWADA", "SABBAVARAM"),
            "NARSIPATNAM" to listOf("Select Mandal", "CHEEDIKADA", "MADUGULA", "GOLUGONDA", "KOTAURATLA", "MAKAVARAPALEM", "NAKKAPALLI", "NARSIPATNAM", "NATHAVARAM", "PAYAKARAOPETA", "RAVIKAMATHAM", "ROLUGUNTA", "S.RAYAVARAM"),

            "Anantapur" to listOf("Select Mandal", "Anantapur Rural", "Atmakur", "Bukkaraya Samudram", "Garladinne", "Kudair", "Narpala", "Peddapappur", "Putlur", "Raptadu", "Singanamala", "Tadipatri", "Vidapanakal", "Yellanur"),
            "Kalyandurg" to listOf("Select Mandal", "Amarapuram", "Brahmasamudram", "Beluguppa", "Gummagatta", "Kundurpi", "Kalyandurg", "Settur", "Ramagiri", "Kanekal", "Kambadur", "D. Hirehal", "Rayadurg"),
            "Penukonda" to listOf( "Select Mandal", "Penukonda Rural", "Hindupur", "Chilamathur", "Lepakshi", "Parigi", "Roddam", "Somandepalle", "Madakasira", "Rolla", "Agali", "Gudibanda"),
            "Rayachoti" to listOf("Select Mandal", "Rayachoti", "Chinnamandyam", "Gurramkonda", "Lakkireddypalli", "Kalakada", "Galiveedu", "Ramapuram", "Sambepalli", "T.Sundupalle", "Chakrayapet", "Veeraballi"),
            "Rajampeta" to listOf("Select Mandal", "Rajampet", "Pullampet", "Penagalur", "Obulavaripalle", "Nandalur", "Sidhout", "Vontimitta", "Yerraguntla", "Chitvel"),
            "Madanapalle" to listOf("Select Mandal", "Madanapalle", "Ramasamudram", "B.Kothakota", "Peddamandyam", "Thamballapalle", "Kurabalakota", "Mulakalacheruvu"),
            "Bapatla" to listOf("Select Mandal", "Bapatla", "Karlapalem", "Pittalavanipalem", "Cherukupalle", "Repalle", "Nizampatnam", "Bhattiprolu"),
            "Chirala" to listOf("Select Mandal", "Chirala", "Vetapalem", "Karamchedu", "Inkollu", "Martur", "Chinaganjam", "Janakavaram Panguluru"),
            "Addanki" to listOf("Select Mandal", "Addanki", "Ballikurava", "Marripudi", "Santhamaguluru", "Parchur", "Yeddanapudi", "Naguluppalapadu"),
            "Chittoor" to listOf("Select Mandal", "Chittoor", "Puthalapattu", "Gangadhara Nellore", "Vedurukuppam"),
            "Palamaner" to listOf("Select Mandal", "Palamaner", "Kuppam", "Punganur", "Gudupalle"),
            "Madanapalle" to listOf("Select Mandal", "Madanapalle", "Thamballapalle", "Kurabalakota", "Peddamandyam"),
            "Amalapuram" to listOf("Select Mandal", "Amalapuram", "Atreyapuram", "Uppalaguptam", "Mummidivaram", "Ainavilli", "Ambajipeta", "Katrenikona", "Allavaram"),
            "Razole" to listOf("Select Mandal", "Razole", "Malikipuram", "Sakhinetipalli", "Mamidikuduru", "I. Polavaram"),
            "Ramachandrapuram" to listOf("Select Mandal", "Ramachandrapuram", "Rayavaram", "Alamuru", "Kothapeta", "Kapileswarapuram"),
            "Eluru" to listOf("Select Mandal", "Eluru", "Denduluru", "Pedavegi", "Pedapadu", "Unguturu", "Bhimadole", "Nidamarru", "Ganapavaram", "Kaikaluru", "Mandavalli", "Kalidindi", "Mudinepalli", "Eluru Rural"),
            "Jangareddygudem" to listOf("Select Mandal", "Jangareddygudem", "Polavaram", "Buttayagudem", "Jeelugumilli", "Koyyalagudem", "Kukunoor", "Velerupadu", "Kamavarapukota", "T. Narasapuram", "Dwaraka Tirumala"),
            "Nuzvid" to listOf("Select Mandal", "Nuzvid", "Agiripalli", "Chatrai", "Musunuru", "Chintalapudi", "Lingapalem"),
            "Guntur" to listOf("Select Mandal", "Guntur East", "Guntur West", "Pedakakani", "Tadikonda", "Prathipadu", "Phirangipuram", "Medikonduru", "Edlapadu", "Duggirala", "Chebrolu"),
            "Tenali" to listOf("Select Mandal", "Tenali", "Vemuru", "Kollipara", "Kollur", "Bhattiprolu", "Cherukupalle", "Repalle", "Nizampatnam"),
            "Narasaraopet" to listOf("Select Mandal", "Narasaraopet", "Chilakaluripet", "Sattenapalli", "Piduguralla", "Macherla", "Dachepalli", "Bellamkonda", "Nadikudi", "Vinukonda", "Ipur", "Savalyapuram"),
            "Kakinada" to listOf("Select Mandal", "Kakinada Rural", "Kakinada Urban", "Karapa", "Pedapudi", "U. Kothapalli"),
            "Peddapuram" to listOf("Select Mandal", "Peddapuram", "Gollaprolu", "Samalkota", "Gandepalle", "Pithapuram"),
            "Pithapuram" to listOf("Select Mandal", "Pithapuram", "Jaggampeta", "Yeleswaram"),
            "Tuni" to listOf("Select Mandal", "Tuni", "Thondangi", "Sankhavaram", "Prathipadu"),
            "Rampachodavaram" to listOf("Select Mandal", "Rampachodavaram", "Maredumilli", "Addateegala", "Gangavaram"),
            "Vijayawada" to listOf("Select Mandal", "Vijayawada Rural", "Vijayawada Urban", "Ibrahimpatnam", "G. Konduru", "Kankipadu", "Penamaluru", "Thotlavalluru"),
            "Machilipatnam" to listOf("Select Mandal", "Machilipatnam", "Challapalli", "Koduru", "Avanigadda", "Nagayalanka", "Pedana", "Ghantasala"),
            "Nuzvid" to listOf("Select Mandal", "Nuzvid", "Agiripalli", "Musunuru", "Chatrai", "Vissannapeta", "Mandavalli", "Kaikaluru"),
            "Kurnool" to listOf("Select Mandal", "Kurnool Urban", "Kurnool Rural", "Gudur", "C. Belagal", "Kallur", "Orvakal", "Kodumur", "Veldurthi"),
            "Adoni" to listOf("Select Mandal", "Adoni", "Gonegandla", "Holagunda", "Kosigi", "Kowthalam", "Mantralayam", "Nandavaram", "Pedda Kadubur", "Yemmiganur", "Aspari", "Chippagiri", "Pattikonda", "Maddikera", "Tuggali", "Krishnagiri"),
            "Pattikonda" to listOf("Select Mandal", "Alur", "Aspari", "Chippagiri", "Devanakonda", "Halaharvi", "Maddikera", "Pattikonda", "Tuggali", "Krishnagiri"),
            "Nandyal" to listOf("Select Mandal", "Nandyal", "Gospadu", "Sirvel", "Dornipadu", "Uyyalawada", "Chagalamarri", "Rudravaram", "Mahanandi", "Allagadda", "Panyam", "Gadivemula", "Sanjamala", "Kolimigundla"),
            "Atmakur" to listOf("Select Mandal", "Bandi Atmakur", "Srisailam", "Atmakur", "Velgode", "Nandikotkur", "Pagidyala","J.Bungalow", "Kothapalle", "Pamulapadu", "Midthur"),
            "Dhone" to listOf("Select Mandal", "Dhone", "Bethamcherla", "Peapully", "Banaganapalli", "Owk", "Koilakuntla"),
            "Nandigama" to listOf("Select Mandal", "Nandigama", "Kanchikacherla", "Chandarlapadu", "Jaggayyapeta", "Vatsavai", "Veerullapadu"),
            "Tiruvuru" to listOf("Select Mandal", "Tiruvuru", "A. Konduru", "Gampalagudem", "Reddigudem", "Visannapeta"),
            "Narasaraopet" to listOf("Select Mandal", "Narasaraopet", "Chilakaluripet", "Edlapadu", "Ipur", "Nadendla", "Narasaraopet", "Nuzendla", "Rompicherla", "Savalyapuram", "Vinukonda"),
            "Sattenapalli" to listOf("Select Mandal", "Amaravathi", "Atchampet", "Bellamkonda", "Krosuru", "Macherla"),
            "Gurazala" to listOf("Select Mandal", "Gurazala", "Bellamkonda", "Chilakaluripet", "Nadendla"),
            "Parvathipuram" to listOf("Select Mandal", "Parvathipuram", "Seethanagaram", "Balijipeta", "Salur", "Pachipenta", "Makkuva", "Komarada", "Garugubilli"),
            "Palakonda" to listOf("Select Mandal", "Palakonda","Gummalakshmipuram", "Kurupam", "Jiyyammavalasa", "Seethampeta", "Bhamini", "Veeraghattam"),
//            "Parvathipuram" to listOf("Select GP", "Parvathipuram", "Narsipuram", "Buduruvada", "Lakshminarayanapuram"),
//            "Salur" to listOf("Select GP", "Salur", "Turpu Salur", "Then Salur", "Narsipatnam"),
//            "Komarada" to listOf("Select GP", "Komarada", "Purnapadu", "Mogaltor","Jalapati"),
//            "Palakonda" to listOf("Select GP", "Palakonda", "G. L. Puram", "Mogilicherla", "Nallamalla"),
//            "Gummalakshmipuram" to listOf("Select GP", "Gummalakshmipuram", "Ramabhadrapuram", "Jodugulli"),
//            "Kurupam" to listOf("Select GP", "Kurupam", "Pachanagada", "Jalabujili"),
//            "Jiyyammavalasa" to listOf("Select GP", "Jiyyammavalasa", "Kothapeta", "Sombuvari Palli"),
//            "Seethampeta" to listOf("Select GP", "Seethampeta", "Pachagonda", "Chinthapalli"),
            "Parvathipuram" to listOf("Select Mandal", "Parvathipuram", "Seethanagaram", "Balijipeta", "Salur","Pachipenta", "Makkuva", "Komarada", "Garugubilli"),
            "Palakonda" to listOf("Select Mandal", "Palakonda", "Gummalakshmipuram","Kurupam", "Jiyyammavalasa", "Seethampeta", "Bhamini", "Veeraghattam"),
            "Ongole" to listOf("Select Mandal", "Ongole Urban", "Ongole Rural", "Chimakurthy", "Kondapi", "Kothapatnam", "Maddipadu", "Mundlamuru", "Naguluppalapadu", "Santhanuthalapadu", "Singarayakonda", "Tangutur", "Thallur", "Zarugumalli"),
            "Kanigiri" to listOf("Select Mandal", "Chandrasekharapuram", "Darsi", "Donakonda", "Hanumanthunipadu", "Kanigiri", "Konakanamitla", "Kurichedu", "Marripudi", "Pamuru", "Pedacherlopalle", "Podili", "Ponnaluru", "Veligandla"),
            "Markapuram" to listOf("Select Mandal", "Ardhaveedu", "Bestavaripeta", "Cumbum", "Dornala", "Giddalur", "Komarolu", "Markapuram", "Pedda Araveedu", "Pullalacheruvu", "Racherla", "Tarlupadu", "Tripuranthakam", "Yerragondapalem"),
            "Palasa" to listOf("Select Mandal", "Ichchapuram", "Kaviti", "Sompeta", "Kanchili", "Palasa", "Mandasa", "Vajrapukotturu", "Nandigam"),
            "Tekkali" to listOf("Select Mandal", "Tekkali", "Santhabommali", "Kotabommali", "Saravakota", "Meliaputti", "Pathapatnam", "Kothuru", "Hiramandalam", "Lakshminarasupeta"),
            "Srikakulam" to listOf("Select Mandal", "Srikakulam", "Gara", "Amadalavalasa", "Ponduru", "Sarubujjili", "Burja", "Narasannapeta", "Polaki", "Etcherla", "Laveru", "Ranastalam", "Ganguvarisigadam", "Jalumuru"),
            "Nellore" to listOf("Select Mandal", "Nellore Urban", "Nellore Rural", "Kovur", "Buchireddipalem", "Indukurpet", "Thotapalligudur", "Muthukur", "Venkatachalam", "Manubolu", "Podalakur", "Rapur", "Sydapuram"),
        "Atmakur" to listOf("Select Mandal", "Atmakur", "Chejerla", "Ananthasagaram", "Anumasamudrampeta", "Marripadu", "Sangam", "Seetharamapuram", "Udayagiri", "Vinjamur", "Varikuntapadu", "Duttalur"),
        "Kavali" to listOf("Select Mandal", "Kavali", "Allur", "Vidavalur", "Kodavalur", "Vinjamur", "Dagadarthi", "Bogole", "Jaladanki", "Kaligiri", "Duttalur"),
        "Kandukur" to listOf("Select Mandal", "Kandukur", "Lingasamudram", "Gudluru", "Ulavapadu", "Voletivaripalem", "Kondapuram", "Varikuntapadu"),
            "Nellore" to listOf("Select Mandal", "Nellore Rural", "Nellore Urban", "Kovur", "Buchireddipalem", "Indukurpet", "Thotapalli Gudur", "Muthukur", "Venkatachalam", "Manubolu", "Podalakur", "Rapur", "Sydapuram"),
            "Atmakur" to listOf("Select Mandal", "Atmakur", "Ananthasagaram", "Anumasamudrampeta", "Chejerla", "Kaluvoya", "Marripadu", "Sangam", "Seetharamapuram", "Udayagiri"),
            "Kavali" to listOf("Select Mandal", "Kavali", "Allur", "Bogole", "Dagadarthi", "Duttalur", "Jaladanki", "Kaligiri", "Kodavalur", "Vidavalur", "Vinjamur"),
            "Kandukur" to listOf("Select Mandal", "Kandukur", "Gudluru", "Kondapuram", "Lingasamudram", "Ulavapadu", "Varikuntapadu", "Voletivaripalem"),
            "Dharmavaram" to listOf("Select Mandal", "Bathalapalle", "Chennekothapalle", "Dharmavaram", "Kanaganapalle", "Mudigubba", "Ramagiri", "Tadimarri"),
            "Kadiri" to listOf("Select Mandal", "Amadagur", "Gandlapenta", "Kadiri", "Nallacheruvu", "Nambulapulakunta", "Talupula", "Tanakal"),
            "Penukonda" to listOf("Select Mandal", "Agali", "Amarapuram", "Chilamathur", "Gudibanda", "Hindupur", "Lepakshi", "Madakasira", "Parigi", "Penukonda", "Roddam", "Rolla", "Somandepalle"),
            "Puttaparthi" to listOf("Select Mandal", "Bukkapatnam", "Gorantla", "Kothacheruvu", "Nallamada", "Obuladevaracheruvu", "Puttaparthi"),
            "Gudur" to listOf("Select Mandal", "Balayapalle", "Chillakur", "Chittamur", "Dakkili", "Gudur", "Kota", "Vakadu", "Venkatagiri"),
            "Sullurupeta" to listOf("Select Mandal", "Buchinaidu Kandriga", "Doravarisatram", "Naidupeta", "Ozili", "Pellakur","Satyavedu", "Sullurupeta", "Tada", "Varadaiahpalem"),
            "Srikalahasti" to listOf("Select Mandal", "K.V.B.Puram", "Nagalapuram", "Narayanavanam", "Pichatur", "Renigunta", "Srikalahasti", "Thottambedu", "Yerpedu"),
            "Tirupati" to listOf("Select Mandal", "Chandragiri", "Chinnagottigallu", "Pakala", "Puttur", "Ramachandrapuram", "Tirupati Rural", "Tirupati Urban", "Vadamalapeta", "Yerravaripalem"),
            "Bheemunipatnam" to listOf("Select Mandal", "Anandapuram", "Bheemunipatnam", "Visakhapatnam (Rural)", "Seethammadhara","Padmanabham"),
            "Visakhapatnam" to listOf("Select Mandal", "Gajuwaka", "Pedagantyada", "Gopalapatnam", "Mulagada", "Maharanipeta","Pendurthi"),
        "Parvathipuram" to listOf("Select Mandal", "Parvathipuram", "Balijipeta", "Salur", "Seethanagaram", "Pachipenta", "Makkuva", "Garugubilli", "Komarada", "Kurupam", "Gummalakshmipuram"),
            "Vizianagaram" to listOf("Select Mandal", "Vizianagaram", "Denkada", "Bondapalli", "Gantyada", "Jami", "Garividi", "Pusapatirega", "Nellimarla", "Dattirajeru", "Gajapathinagaram"),
            "Bobbili" to listOf("Select Mandal", "Bobbili", "Ramabhadrapuram", "Badangi", "Therlam", "Cheepurupalli", "Merakamudidam"),
            "Eluru" to listOf("Select Mandal", "Eluru", "Denduluru", "Pedapadu", "Pedavegi", "Nuzvid", "Musunuru", "Bhimadole", "Kaikalur"),
            "Jangareddygudem" to listOf("Select Mandal", "Jangareddygudem", "Tadepalligudem", "Jeelugumilli", "Buttayagudem", "Polavaram", "Kamavarapukota"),
            "Narasapuram" to listOf("Select Mandal", "Narasapuram", "Palakoderu", "Penumantra", "Mogalthur", "Achanta", "Undi", "Veeravasaram"),
            "Kadapa" to listOf("Select Mandal", "Kadapa", "Chennur", "Vallur", "Sidhout", "Chinthakommadinne", "Kamalapuram", "Pendlimarri"),
            "Jammalamadugu" to listOf("Select Mandal", "Jammalamadugu", "Muddanur", "Proddatur", "Duvvur", "Mylavaram", "Rajupalem"),
            "Badvel" to listOf("Select Mandal", "Badvel", "B.Kodur", "Porumamilla", "Kalasapadu", "Gopavaram", "Mydukur"),
            "Pulivendula" to listOf("Select Mandal", "Pulivendula", "Vemula", "Vempalle", "Lingala", "Simhadripuram", "Thondur"),
            )

        val mandalToGPsMap = mapOf(
            "RANGAMPETA" to listOf("Select GP", "ELAKOLANU", "G. DONTHAMURU", "KOTAPADU", "MARRIPUDI", "MUKUNDAVARAM", "NALLAMILLI", "PATHA DODDIGUNTA", "PEDARAYAVARAM", "RANGAMPETA", "SINGAMPALLE", "SOUTH THIRUPATHI RAJAPURAM", "SUBHADRAMPETA", "VADISALERU", "VEERAMPALEM", "VENKATAPURAM"),
            "GOKAVARAM" to listOf("Select GP", "ATCHUTAPURAM", "BHUPATIPALEM", "GADELAPALEM", "GOKAVARAM", "GUMMALLADUDDI", "KALIJOLLA", "KOTHAPALLE", "KRISHNUNIPALEM", "MALLAVARAM", "RAMPA YERRAMPALEM", "SIVARAMAPATNAM", "SUDIKONDA", "TAKURUPALEM", "THANTIKONDA", "TIRUMALAYAPALEM"),
            "KADIAM" to listOf("Select GP", "DAMIREDDIPALLE", "DULLA", "JEGURUPADU", "KADIAM", "MURAMANDA", "VEERAVARAM", "VEMAGIRI"),
            "KORUKONDA" to listOf("Select GP", "BOLLEDDUPALEM", "BURUGUPUDI", "BUTCHEMPETA", "DOSAKAYALAPALLI", "GADALA", "GADARADA", "JAMBUPATNAM", "KANUPURU", "KAPAVARAM", "KORUKONDA", "KOTI", "KOTIKESAVARAM", "MADHURAPUDI", "MUNAGALA", "NARASAPURAM", "NARASIMHAPURA AGRAHARAM", "NIDIGATLA", "RAGHAVAPURAM", "SRIRANGAPATNAM"),
            "RAJAMAHENDRAVARAM RURAL" to listOf("Select GP", "BOMMURU (U)", "DOWLESWARAM(PT)", "HUKUMPETA (U)", "KATHERU", "KOLAMURU", "MORAMPUDI", "RAJAVOLU", "RAJHAMUNDRY(NMA)", "TORREDU"),
            "RAJAMAHENDRAVARAM URBAN" to listOf("Select GP", "RAJAMAHENDRAVARAM (MUNICIPAL CORPORATION)"),
            "RAJANAGARAM" to listOf("Select GP", "BHUPALAPATNAM", "G. YERRAMPALEM", "JAGANNADHAPURAM AGRAHARAM", "KALAVACHERLA", "KANAVARAM", "KONDA GUNTURU", "MUKKINADA", "NAMAVARAM", "NANDARADA", "NARENDRAPURAM", "PALACHARLA", "PATHA THUNGAPADU", "RAJANAGARAM", "SRIKRISHNAPATNAM", "THOKADA", "VELUGUBANDA", "VENKATAPURAM"),
            "SEETHANAGRAM" to listOf("Select GP", "BOBBILLANKA", "CHINAKONDEPUDI", "HUNDESWARAPURAM", "JALIMUDI", "KATAVARAM", "KUNAVARAM", "MIRTHIPADU", "MUGGAULLA", "MUNIKUDALI", "MUNLAKALLANKA", "NAGAMPALLE", "NALLAGONDA", "PURUSHOTHAPATNAM", "RAGHUDEVAPURAM", "SEETHANAGARAM", "SINGAVARAM", "VANGALAPUDI"),
            "ANAPARTHI" to listOf("Select GP", "DUPPALAPUDI", "KOPPAVARAM", "KUTUKULURU", "MAHENDRAWADA", "PEDAPARTHI", "POLAMURU", "PULAGURTHA", "RAMAVARAM"),
            "BICCAVOLE" to listOf("Select GP", "ARIKAREVULA","BALABHADRAPURAM","BICCAVOLU", "ILLAPALLE", "KAPAVARAM", "KOMARIPALEM", "KONKUDURU", "MELLURU", "PANDALAPAKA", "RALLAKHANDRIKA", "RANGAPURAM", "THUMMALAPALLE", "TOSSIPUDI", "VOOLAPALLE"),
            "TALLAPUDI" to listOf("Select GP", "BAYYAVARAM (U.I)", "THADIPUDI", "RAGOLAPALLE", "THUPAKULAGUDEM", "POCHAVARAM", "PAIDIMETTA", "PRAKKILANKA", "GAJJARAM", "ANNADEVARAPETA", "VEERABHADRAPURAM (U.I)", "KUKUNURU", "THALLAPUDI", "VEGESWARAPURAM", "BALLIPADU", "PEDDEVAM", "TIRUGUDUMETTA", "NALLAMILLIPADU (U.I)", "MALAKAPALLE", "RAVURUPADU"),
            "GOPALAPURAM" to listOf("Select GP", "KARAGAPADU", "SAGIPADU", "DONDAPUDI", "GANGOLU", "SAGGONDA", "BHIMOLU", "KOVVURUPADU", "GUDDIGUDEM", "NANDIGUDEM", "KARICHARLAGUDEM", "JAGANNADHAPURAM", "GANGAVARAM (U.I)", "KOMATIGUNTA", "VADALAKUNTA", "GOPALAPURAM", "VELLACHINTALAGUDEM", "CHITYALA", "VENKATAYAPALEM", "CHERUKUMILLI"),
            "NALLAJERLA" to listOf("Select GP", "ANUMUNI LANKA", "POTHAVARAM", "ANANTHAPALLE", "SANJEEVA PURAM(U.I)", "VEERAVALLI", "GUNDEPALLE", "CHODAVARAM (WEST)", "CHEEPURU GUDEM", "NALLAJERLA", "DUBACHERLA", "MARELLAMUDI", "AVAPADU", "PRAKASARAO PALEM", "TELIKICHERLA"),
            "DEVARAPALLE" to listOf("Select GP", "YADAVOLE", "CHINNAYAGUDEM", "YERNAGUDEM", "TYAJAMPUDI", "KURUKURU", "PALLANTLA", "DHUMANTHUNIGUDEM", "DEVARAPALLE", "LAXMIPURAM", "BANDAPURAM", "DUDDUKURU","GOWRIPATNAM", "KONDAGUDEM"),
            "KOVVUR" to listOf("Select GP", "DECHERLA", "ISUKAPATLAPANGIDI", "DOMMERU", "DHARMAVARAM", "PENAKANAMETTA", "CHIDIPI", "KUMARADEVAM", "ARIKIREVULA", "NANDAMURU", "PASIVEDALA", "VEMULURU", "THOGUMMI", "VADAPALLE", "MADDURU", "CHIGURULANKA (UI)", "MADDURULANKA", "KOVVURU MPL"),
            "CHAGALLU" to listOf("Select GP", "CHIKKALA", "CHAGALLU", "NELATURU", "MALLAVARAM", "MARKONDAPADU", "NANDIGAMPADU", "UNAGATLA", "KALAVALAPALLE", "SINGANAMUPPAVARAM", "BRAHMANAGUDEM", "DARAVARAM"),
            "NIDADAVOLE" to listOf("Select GP", "AMMEPALLE (U.I)", "MEDIPALLE (U.I)", "KORUMAMIDI", "VISSAMPALEM", "TADIMALLA", "UNAKARAMILLI", "RAVIMETLA", "SANKARAPURAM", "SURAPURAM", "THIMMARAJUPALEM", "NIDADAVOLE (R)", "GOPAVARAM", "VIJJESWARAM", "PURUSHOTHAPALLE", "PANDALAPARRU", "D.MUPPAVARAM", "ATLAPADU", "SINGAVARAM", "J.KHANDRIKA (U.I)", "SETTIPETA", "MUNIPALLE", "KALAVACHERLA", "JEEDIGUNTALANKA (U.I)", "JEEDIGUNTA", "KORUPALLE", "PENDYALA"),
            "UNDRAJAVARAM" to listOf("Select GP", "KALDHARI", "VELIVENNU", "DAMMENNU", "MORTHA", "CHILAKAPADU", "PASALAPUDI", "SURYARAOPALEM", "VADLURU", "SATYAWADA", "CHIVATAM", "KARRAVARISAVARAM", "PALANGI", "UNDRAJAVARAM", "VELAGADURRU", "TADIPARRU"),
            "PERAVALI" to listOf("Select GP", "NADUPALLE", "KANURU", "KANURUAGRAHARAM", "USULUMARRU", "TEEPARRU", "KAKARAPARRU", "AJJARAM", "PERAVALI", "KAPAVARAM", "KOTHAPALLEAGRAHARAM", "MUKKAMALA", "KHANDAVALLI", "MALLESWARAM", "PITTALAVEMAVARAM"),
            "ANANTHAGIRI" to listOf("Select GP", "Addategela", "Ananthagiri", "Baliaguda @ Cheedivalasa", "Baliyaguda", "Ballagaruvu", "Ballamamidi", "Bandakonda", "Bandavalasa", "Bangarampeta", "Banjoda", "Bediliguda", "Bembi", "Bhagamarivalasa", "Bhalluguda", "Bheemavaram", "Bheesupuram", "Billakamba", "Billakota", "Bodaguda", "Boddaputtu", "Bonduguda", "Bondyaguda", "Bongija", "Bonuru", "Boodi", "Boraborivalasa", "Boringuvalasa", "Borra", "Borrapalem", "Buddigaruvu", "Buruga", "Burugulapadu", "Busipadu", "Chappadi", "Chatakamba", "Cheedigaruvu", "Cheedimetta", "Cheedivalasa", "Cherukubidda", "Chilakalagedda", "Chimiti", "China Konela", "China Rabha", "Chippapalle", "Chitrallapalem", "Chittampadu", "Chittamvalasa", "Dabbalapadu", "Damsalavalasa", "Dekkapuram", "Dhanukota", "Dibbapalem", "Diguva Kambavalasa", "Diguvamallelu", "Diguvasonabha", "Donkaiputtu", "Dumbrivalasa", "Eetamanuvalasa", "Eguvamallelu", "Eguvamamidi", "Eguvasobha", "Gaddibanda", "Gadilalova", "Gangavaram", "Garugubilli", "Gazzellagaruvu", "Getuvalasa", "Gommangipadu", "Gondiguda", "Gorregommu", "Gottipadu", "Gudem", "Gumma", "Heetaguda", "Jakariguda", "Jalada", "Jamuguda", "Jeelugulapadu", "Jeenapadu", "Kadarevu", "Kamalapuram", "Kambavalasa", "Kantipuram", "Kapativalasa", "Karaiguda", "Karakavalasa", "Karivesu", "Kasipatnam", "Katika", "Katimanuvalasa", "Katturu", "Kittalingi", "Kivarla", "Kodamaguda", "Kondemkota", "Kondiba", "Kotaparti", "Kottavalasa", "Kotturu", "Kundururi", "Lanjilaguda", "Limbaguda @ Nimmada", "Mangagumma", "Mettavalasa", "Mungaravalasa", "Nandigugudu", "Pathapaderu", "Sundruputtu", "Tiyyagedda", "Vankachinta", "Varukaru"),
            "ARAKUVALLEY" to listOf("Select GP", "Adaru", "Addumanda", "Amalaguda", "Antiparthi", "Bairuguda", "Bamsuguda", "Bandapanuvalasa", "Baski", "Battivalasa", "Beddaguda", "Bishnuguda", "Boduguda", "Bondaguda", "Bondam", "Borrachinta","Borrakaluvalasa", "Borriguda", "Bosubeda", "Boyiguda", "Chandrapoda", "Cheedivalasa", "Chellubadi", "Chendrapodaru", "Chinagangagudi", "Chinalabudu", "Chittamgondi", "Chompi", "Dabuguda", "Dalapatiguda", "Danasalavalasa", "Dappuguda", "Davadaguda", "Dellipadu", "Devarapalle", "Doraguda", "Dumbriguda", "Dungiyaputtu", "Gadyaguda", "Gangasanivalasa", "Ganjayavalasa", "Garudaguda", "Gatapadu", "Gattaraguda", "Gatuguda", "Girliguda", "Gondiguda", "Gugguda", "Hattaguda", "Iragai", "Jaginivalasa", "Janamguda", "Kaguvalasa", "Karakavalasa", "Kikkatiguda", "Kunduriguda", "Kuruseela", "Lamtampadu", "Limbaguda", "Madaguda", "Mala Singaram", "Manjuguda", "Mettapadu", "Morriguda", "Musiriguda", "Nanda Araku", "Nandiguda", "Padmapuram", "Panirangini", "Pathaballuguda", "Peda Labudu", "Pedagaruvu", "Pittamarriguda", "Polamguda", "Pooluguda", "Pottangipadu", "Urumulu", "Vanthalaguda", "Vanthamuru", "Varra"),
            "CHINTHAPALLI" to listOf("Select GP", "Anjalam", "Annavaram", "Antharla", "Asirada", "Baddimetta", "Badravaram", "Balarampuram", "Bandabayalu", "Bangarugummi", "Barikadorapakalu", "Basangi Kothuru", "Bharatabada", "Bhasavadaram", "Bhyheemavaram", "Boddigudem", "Bonangi", "Boragadavalli", "Borrali", "Budubadi", "Chandragiri", "Cheelavada", "Cheemacherla", "Chendagadda", "Chervupadu", "Chinapendyalapadu", "Chinthaladibbada", "Chinthala Bommadevada", "Chinthanapalle", "Chippalavalasa", "Chodidumvalasa", "Choketipadu", "Chouryapadu", "Dagada", "Dagadavalasa", "Doddbhoda", "Dommadimetta", "Dumbriguda", "Edlapalli", "Gadirevula", "Galasana", "Gandigotha", "Gasulagalapalle", "Geddamavaram", "Gogole", "Gopalapuram", "Govindapuram", "Govindareddypuram", "Gudigalla", "Gudulavalasa", "Gundamukkalu", "Gurravaram", "Injerla", "Jaggampupadu", "Jampupadu", "Jonnalagadda", "Kakumanu", "Kalavacherla", "Kalyanapadu", "Kammadu", "Kankivadu","Kapulakalur", "Karavali", "Kariveedu", "Kas Annayyappapalle", "Kasireddypalem", "Kattaimangala", "Khaniyapalem", "Kondamaddi", "Kondapalle", "Kondapuram", "Korugallu", "Kothagudem", "Kottavaram", "Kudupudi", "Kumburthi", "Kunipalle", "Kundrathonipalem", "Lakkavaram", "Lalapiryapadu", "Lingasamudram", "Maddulapudi", "Mamidipalle", "Mandalapalem", "Mannuvalu", "Marriwada", "Mekalaguda", "Mellachintalapadu", "Mittasala", "Modugapalle", "Muddaraopeta", "Munthamalli", "Murlavalasa", "Nadukuduru", "Nagulacheruvu", "Narasinganapalle", "Natavalasa", "Neralacheruvu", "Nidavarthipalem", "Padarai", "Pahandshi", "Palem", "Parlapalle", "Parpamreddigari", "Pedabstada", "Pedachinthala", "Pedacheruviri", "Peddacheemacherla", "Permatepalle", "Rajalakshmi", "Ramagiri", "Ramakrishnapuram", "Rangacharla", "Rangivaari", "Rapparapalem", "Ravulapalem", "Reddivaram", "Sankavaram", "Saripedia", "Satyavaram", "Sekharapalem", "Sodupeta", "Srikakulam", "Subbavaram", "Tammugada", "Tapaskonda", "Tirumalagiri", "Tummalavalasa", "Vaddirajuvalasa", "Vadumalli", "Valalagudem", "Venkatapuram", "Vijayawada", "Vontimitta", "Yelamanchili", "Yerutalapalle"),
            "DUMBRIGUDA" to listOf("Select GP", "Addagaravalasa", "Anterivalasa", "Arathawada", "Bachupeta", "Ballagada", "Barkurala", "Borsalavalasa", "Buliguda", "Butchaivalasa", "Chandralalayapaalem", "Chervuguda", "Chodavaram", "Dongarikanda", "Dumbriguda", "Gandapathinna", "Ganganapalle", "Gopalapatnam", "Goyyapanka", "Gudupalli", "Gummavaram", "Guntakalapalle", "Gurralavalasa", "Hajipalli", "Indaram", "Jaggingigudem", "Jamindiaripalem", "Jangendragaripalli", "Kachkovalam", "Kalikilavalasa", "Kammapuvalasa", "Kanakavaram", "Karimalavalasa", "Kelavaravalasa", "Kodavalavalasa", "Komativuga", "Kothagudem", "Kunchakonda", "Kurmanivalasa", "Lingala", "Madanpalle", "Macherla", "Makavarapadu", "Mallikelavalasa", "Mangarida", "Markabangaram", "Mokilavalasa", "Mushagalankala", "Nadupolepadu", "Nellipeta", "Odavala", "Palacheruvu", "Panamvalasa", "Pedakandukur", "Pedagangavaram", "Peddavalasa", "Ramakommu", "Saggapeta", "Satyanarayanapuram", "Shivapuram", "Sivalingampuram", "Tirumalagudi", "Vadekapadu", "Vadishi", "Vandyalavalasa", "Vanthavalasa", "Varicolavalasa"),
            "G.K.VEEDHI" to listOf("Select GP", "Upper Sileru Project", "Adagarapalle", "Agraharam", "Ammavaridharakonda", "Annavaram", "Asurodda", "Badasallu", "Bathunuru", "Boddalalagondi", "Boddamamidi", "Boddamanupakalu", "Bonampalle", "Boyalapalem", "Burugupakalu", "Busikonda", "Busulu", "Challanisilpa", "Chapagedda", "Chaparathipalem", "Cheedigunta", "Chintalapadu", "Devarapalle", "Galikonda", "Gudem Colony", "Gudem Kotha Veedhi", "Gudem Patha Veedhi", "Jerrila", "Kondrupalle", "Lakkavarapupeta", "Mondigedda", "Neelavaram", "Peddavalasa", "Rinthada", "Sankada", "Vanthadapalle"),
            "G.MADUGULA" to listOf("Select GP", "A.Bandaveedhi", "Addulu", "Agampadu", "Akuthota", "Alagam", "Ambalamamidi", "Anarbha", "Andanapalle", "Andangisingi", "Aragadapalle", "Bagarugudi", "Balamanusanka", "Balijipeta", "Bandaveedhi", "Beeram", "Bobbampadu", "Boddagondi", "Boddumamidi", "Bondapalle", "Borramamidi", "Buradaveedhi", "Buruguveedhi", "Busipalle", "Buttakota", "Chapagedda","Cheekumbanda", "Cheemakurapalem", "Devarapalle", "G.M.Kothuru", "Gadderai", "Gadigunta", "Godugumamidi", "Gondimelaka", "Gummalagondi", "Gummadigondi", "Indugula", "Peddabayalu", "Padmapuram", "Palakonda", "Peddagaruvu", "Vedurupalle",),
            "HUKUMPETA" to listOf("Select GP", "Babbili Jiripudi","Chennuru", "Chepa", "Chimmani Peta", "Damulapadu", "G G Inavolu", "Gudepalli", "Gurramvalasa", "Jonnavalasa", "Kallapeta", "Kondambadi", "Kothuru", "Madhavaram", "Mutipeta", "Panavaram", "Parvathipuram", "Pedapulapadu", "Ramrajugudem", "Sriramnagar", "Talakkapeta", "Turkayamjal", "Uligadi",/* … up to ~80 villages */),
            "KOYYURU" to listOf("Select GP", "Adlivada","Arjunavalasa", "Balijipeta", "Bodiguda", "Buragapadu", "Chinnapuram", "Chittalingapuram", "G.Mogawula", "Gadikota", "Gollapalle", "Gudivada", "Gurrappakonda", "Janajiguda", "Kesavapuram", "Kondalapalem", "Korikonna", "Krishnapuram", "Kummaram", "Lakkavaram", "Lokkilogdi", "Mogipalem", "Mudhinepalem",/* … up to ~110 villages */),
            "MUNCHINGPUT" to listOf("Select GP", "Aparajapuram", "Appalanarayanapuram", "Bodinavula", "Chinagummuluru", "Chintalaguda", "Dabbanda", "Devarapalle", "Duddukonda", "Goddipeta", "Kadarada", "Kakarlapalli", "Kambalapadu", "Kanchugummuluru", "Karriguda", "Kodagandi", "Kodaputtu", "Kondajangi", "Kothakota", "Munchingput", "Peddagummuluru", "Podi", "Rallagedda", "Ravigundam", "Thakarapadu", "Thimeliputtu", "Thurumamidi", "Vadapalem", "Vaddadi",),
            "PADERU" to listOf("Select GP", "Ananthagiri", "Bhimavaram", "Bodduru", "Chinakodapalli", "Chintalaguda", "Chintapalle", "Chodavaram", "Dasampeta", "Gangaraju Madugula", "Garlamadugu", "Gondiputtu", "Gudari", "Jalaput", "Kanchugummuluru", "Karakavalasa", "Kasarada", "Koppulapadu", "Korukonda", "Kothakota", "Labbarthi", "Madugula", "Makavarapalem", "Malakapalli", "Marika", "Mongia", "Muthukonduru", "Paderu","Padmapuram", "Rallagedda", "Tiruvada", "Valasapalli",),
            "PEDABAYULU" to listOf("Select GP", "Arempudi", "Arlampadu", "Arle Konda", "B.G.Kothuru","Balaramapuram", "Bandigedda", "Battivalasa", "Bhimavaram", "Boddagandi", "Bodduru", "Buragam", "Channavaram", "Chidipalli", "Chinagedda", "Chintalapudi", "Chintapalli", "Devarapalle", "Dummeda", "Duppalapadu", "G.Kothuru", "Gaddapeta", "Galiapadu", "Gampalagondi", "Gondiputtu", "Gumpakota","Jarrela", "Juttada", "K.J.Puram", "Kadalagedda", "Kamalapuram", "Kanchugummuluru", "Karakavalasa", "Kasanapalli", "Katchaluru", "Koda Gondi", "Kodaputtu", "Kondajangi", "Korikonda", "Kothakota", "Kothapalli", "Labbarthi", "Laxmipuram", "Maddigaruvu", "Makavarapalem", "Malakapalli", "Marika", "Mulakgondi", "Musirikonda", "Mutyabanda", "Paderu", "Pakkalapadu", "Pedabayalu", "Rajuluru", "Ravigundam", "Ravikaput", "Sirasapalli", "Thakarapadu", "Thimeliputtu", "Vadapalli", "Vanjangi",),
            "MAREDUMILLI" to listOf("Select GP", "Addarivalasa", "Akumamidikota", "Arjunalova", "Banda", "Bhimavaram", "Bodlanka", "Boduluru", "Busigandi", "Chakkavada", "Chatlavada", "Chavidikota", "D. Velamalakota", "Daravada", "Denduluru", "Devarapalle", "Dorachintalapalem", "Doramamidi", "Egavalasa", "Elivada", "Goguvalasa", "Gondivada", "Goramamidi", "Gudisa", "Gujjumamidivalasa", "Gumpenagandi", "Gundrathi", "Ijjaluru", "Ivampalle", "Kadumuru", "Kakuru", "Katchalavada", "Kondavada", "Kuduru", "Kundada", "Kutravada", "Maddiveedu", "Madduluru","Mallavaram", "Maredumilli", "Muchilivada", "Munjamamidi", "Munthamamidi", "Musuru", "Narsapuram", "Nellore", "Nukaletivada", "Nurupudi", "Pamulamamidi", "Pamuleru", "Pandirimamidikota", "Pedamallupadu", "Peddamarri", "Pedduru", "Potlavada", "Pujaripakalu", "Pullangi", "Pusiwada", "Puttagondilanka", "Ramannavalasa", "Siripanlova", "Sripuram", "Sunnampadu", "Thadepalle", "Thurruru", "Thurumamidi", "Vakkuluru", "Valamuru", "Vetukuru", "Vuthaluru", "Vyadapudi"),
            "DEVIPATNAM" to listOf("Select GP", "Addateegala", "Adiri", "Agraharam", "Annavaram", "Arjunadibba", "Atlantinapeta", "Bodigudem", "Bodipalem", "Bojjaigudem", "Chandrapenta", "Chavatimmidivaram", "Chigicherla", "Chintalamori", "Chota Gummuluru", "Dadikipuram", "Dalapadu", "Devipatnam", "G. Peddi Gudem", "Gajupatiram", "Gangavaram", "Gollapalem", "Gudupudi", "Guptipalem", "Jaggampeta", "Kallamuru", "Kandryalu", "Kasulapalle", "Konavaram", "Korlapudi", "Kothuruppu", "Kovarru", "Kuppalapalem", "Laxmipuram", "Mamidipalem", "Mamidikudikallu", "Mogaligonda", "Munagapadu", "Muppavaram", "Nagaram", "Nallagerivari", "Nellipaka", "Palagallu", "Parvathirajapuram", "Pedagudem", "Pedanombutti", "Polavarapalem", "Rajavlakavaram", "Sanarthalapalem", "Singarajupuram", "Somasila", "Thalavaipalem", "Thopalapura", "Turupu Kolukulangi", "Vadapalem", "Vasapuram", "Vasanthapalle"),
            "Y.RAMAVARAM" to listOf("Select GP", "Adupulametta", "Allurigedda", "Ammapeta", "Annampalem", "Antilova", "Babbilova", "Bachaluru", "Bandigedda", "Bheemudugadda", "Boddagondi", "Boddagunta", "Boddapalle", "Boddumamidi", "Bullojupalem", "Buradakota", "Buradavalasa", "Buruguwada", "Busikota", "Chamagedda", "Chanaganuru", "Chaparai", "Chavitidibbalu", "Chendurthi", "Chilakaveedhilanka", "Chinavulempadu", "Chinta Koyya", "Chinthakarrapalem", "Chinthalapudi",
                "Dabbamamidi", "Dadalikavada", "Dalipadu", "Daragedda", "Daralova", "Devaramadugula", "D. Mamidivada", "Donarai", "Donkarai", "Doragondi", "Dorawada", "Dubela", "Dumpavalasa", "Edlakonda", "Gandempalle", "Ganganuru", "Gannavaram", "Gellavada", "Gobbilapanukulu", "Godugurayi", "Gondikota", "Goppulathotamamidi", "Goramanda", "Gummarapalem", "Gurtedu", "Irlavada", "Jajigedda", "Jajivalasa", "Jalagalova", "Jangalathota", "K Yerragonda", "Kadarikota", "Kakkonda", "Kallepugonda", "Kanatalabanda", "Kanivada", "Kappalabanda", "Karnikota", "Kathirala", "Kokitagondi", "Komaravaram", "Koppulakota", "Koramatigondi","Kota", "Kotabandichippalamamidi", "Kothakota", "Kothapakalu", "Kunkumamidi", "Lingavaram", "Mangampadu", "Marriguda", "Mulasalapalem", "Munagalapudi","Muvvalavada", "Nagalova", "Nakkalapadu", "Nakkarathipalem", "Neelapalem", "Nellikota", "Nulakamamidi", "Nuvvugantipalem", "P Yerragonda", "Paidiputta", "Palagondi", "Panasalapalem","Panasalova", "Panchadaralanka", "Pasaruginne", "Pathakota", "Pedavulempadu", "Perikivalasa", "Polamanugondi", "Poolova", "Pulimetala", "Pulusumamidi", "Putikunta", "Puttagandi", "Puttapalle", "Rachapalem", "Rakota", "Ramulakonda","Ratsavalasa", "Ravvagadda", "Regadipalem", "Revadikota", "Sesharai", "Simhadripalem", "Singanakota", "Singavaram", "Sirimetla", "Thadikota", "Thangedukota", "Totakurapalem", "Tulusuru", "Tumikelapadu", "Vadisalova", "Vanamamidigondi", "Vattigedda", "Vedullapalle", "Veerampalem", "Vejuvada", "Villarti", "Vootlabanda", "Y. Ramavaram", "Yarlagadda", "Yerragoppula", "Yerramreddipalem"),

            "ADDATEEGALA" to listOf("Select GP", "Addateegala", "Anigeru", "Annampalem", "Anukulapalem", "Atchiyyapeta", "Badadam", "Bandakonda", "Bandamamillu", "Bhimavaram", "Bhimudupakalu", "Bodlanka", "Chakirevula", "Chaparatipalem", "Chikkapugedda", "Chinamunakanagedda", "Chinavadisakarra", "Chinna Addateegala", "Chinnampadu","Chodavaram", "Choppanagadda", "Dabbapalem", "Dakodu", "D. Ammapeta", "Darsinuthula @ Regulapadu", "Dhanayampalem", "D. Kothuru", "D. Krishnavaram", "D. Pinjarikonda", "D. Ramavaram", "Ducherthi","Duppalapalem", "Gadichinnampalem", "Gavarayyapeta", "Gondolu", "Gontuvanipalem", "Gottilapadu", "Jajipalem", "Jalluru", "Jayachintalapalem", "Kalimamidi", "Kimmuru", "Kinaparti", "Konalova", "Kothurupadu", "Kottampalem", "Kovelapalem", "Languparti", "Latchireddipalem", "Makaram", "Mallavaram Mamillu", "Mamidipalem", "Matlapadu", "Mitlapalem", "Mulakayala Bhimavaram", "Nimmalapalem", "Nukarai", "Paidiputtapadu", "Panasaloddi", "Panukuratipalem", "Papampeta", "Pedamunakanagedda", "Peddavadisakarra", "Penikelapadu", "Puligogulapadu", "Raj anagaram", "Ravigudem", "Ravulapalem", "Rayapalle", "Rollagedda", "Sarampeta", "Sarampetapadu", "Seetharam", "Settipalle", "Somannapalem", "Thimmapuram", "Thungamadugula", "Tirumalawada", "Tiyyamamidi", "Uligogula", "Uppalapadu", "Vangalamadugu", "Vedullakonda", "Veerabhadrapuram", "Veeravaram", "Venkatanagaram", "Vetamamidi", "Vutlapalem", "Yellapuram", "Yellavaram"),
            "GANGAVARAM" to listOf("Select GP", "Gangavaram", "Molleru", "Nellipudi", "Amudalabanda", "Jaggampalem"),
            "RAMPACHODAVARAM" to listOf("Select GP", "Rampachodavaram", "Busigudem", "Akuru", "B. Velamalakota","B.Ramannapalem", "Bandapalle"),
            "RAJAVOMMANGI" to listOf("Select GP", "Rajavommangi","Bornagudem", "Jeelugulapadu", "Komarapuram", "Kondapalle", "Thantikonda"),
            "KUNAVARAM" to listOf("Select GP", "Kunavaram", "Chakoriguda", "Korikonda", "Kodirikonda", "Thaturu", "Yedavalli"),
            "CHINTOOR" to listOf("Select GP", "Chintoor", "Gummaladoddi", "Mallampalem", "Ramannapalem", "Todipalli", "Vanaparthy"),
            "VARARAMACHANDRAPURAM" to listOf("Select GP", "Vararamachandrapuram", "Addateegala", "Cheedipalem", "Doddipalem", "Panagadda", "Ponukumilli"),
            "NELLIPAKA" to listOf("Select GP", "Nellipaka", "Pedageddada", "I. Polavaram", "Potlapadu", "Yerramreddipalem", "Bheemavaram"),
            "ANAKAPALLI" to listOf("Select GP","Allikhanudupalem", "Anakapalle (M)", "Bhatlapudi", "Bowluvada (CT)", "Chintanippula Agraharam", "Golagam", "Gopalapuram", "Jagannadhapuram", "Koduru", "Kondupalem", "Koppaka", "Kunchangi", "Kundram", "Makavaram", "Mamidipalem", "Maredupudi", "Maredupudi Agraharam", "Marturu", "Mettapalem", "Papayya Palem", "Papayya Santha Palem", "Pisinikada", "Rajupalem", "Rebaka", "Sampathipuram", "Sankaram", "Seethanagaram", "Tagarampudi", "Thummapala", "Valluru", "Venkupalem", "Vetajangalapalem", "Vooderu"),
            "ATCHUTAPURAM" to listOf("Select GP", "Andalapalle", "Bhogapuram", "Cheemalapalle", "Chippada", "Chodapalle", "Dibbapalem", "Dopperla", "Dosuru", "Duppituru", "Gangamambapura Agraharam", "Haripalem", "Iravada", "Jagannadhapura Agraharam", "Jaggannapeta", "Janguluru", "Jogannapalem", "Khajipalem", "Kondakarla", "Maduthuru", "Maruturu", "Melupaka Jagannadhapuram", "Nadimpalle", "Nunaparthi", "Pedapadu", "Pudimadaka", "Ravipalem", "Somavaram", "Tantadi", "Thimmarajupeta", "Uddlapalem", "Uppavaram", "Veduruvada", "Yerravaram"),
            "KASIMKOTA" to listOf("Select GP", "Addam", "Ammeensahabpeta", "Atcherla", "Bayyavaram", "Charakam", "Chintalapalem", "Eswarapalle / Chowduvada", "Gobburu", "Gobburupalem", "Gurugu / Bheemavaram", "Kasimkota", "Kothapalle", "Narasapuram", "Narasingibilly", "Nutulaguntla Palem", "Perantalapalem", "Singavaram", "Somavaram", "Sundarayyapeta", "Tallapalem", "Tegada", "Theeda", "Ugginapalem", "Veduruparthi", "Yenugutuni", "Zattapureddituni"),
            "CHODAVARAM" to listOf("Select GP", "Chodavaram", "Juttada", "Sabbavaram", "Gudivada", "Pendurthi", "Kotturu", "Golugonda"),
            "DEVARAPALLI" to listOf("Select GP", "Devarapalli","Kothapalle", "Kodavatipudi", "Marripalem", "Yerravaram", "Kondakotam", "Ramayogi Agraharam"),
            "ELAMANCHILI" to listOf("Select GP", "Elamanchili","Gurajanapalle", "Haripuram", "Kothuru", "Sarvasiddhi", "Juttigapudi", "Gollalapalem"),
            "K.KOTAPADU" to listOf("Select GP", "Kasimkota", "K.Kotapadu", "Kancherlapalem", "Peddipalem", "Chintagatla", "Narsipatnam Agraharam", "Velagapudi"),
            "RAMBILLI" to listOf("Select GP", "Rambilli", "Appikonda", "Devipuram", "Krishnampalem", "Lododdi", "Ponduru", "Ravikamatham"),
            "MUNAGAPAKA" to listOf("Select GP", "Munagapaka", "Devada", "Gopalapatnam", "Venkatapuram", "Vedullavalasa", "Sabbavaram (Outskirts)", "Rajapudi"),
            "PARAWADA" to listOf("Select GP", "Parawada", "Cheepurupalle", "Narsipatnam", "Vishnupuram", "Jaduguda", "Vallurupalem", "Gollapalem"),
            "SABBAVARAM" to listOf("Select GP", "Sabbavaram", "Pendurthi", "Gajuwaka (fringe)", "Boduvalasa", "Darapalem", "Gudivada", "Gollalapalem"),
            "CHEEDIKADA" to listOf("Select GP", "Cheedikada", "Appikonda", "Dondapadu", "Gannavaram", "Gollapalem", "Mattaparru", "Pedabondapadu", "Uppada"),
            "MADUGULA" to listOf("Select GP", "Madugula", "Bowluvada", "Cherukupalle", "Gangavaram", "Govindapuram", "Kaligam", "Katchaluru", "Ravikamatham"),
            "GOLUGONDA" to listOf("Select GP", "Golugonda", "Kothavalasa", "Pedabondapadu", "Talapalle", "Yendada", "Gudemchintala", "Mamidivalasa"),
            "KOTAURATLA" to listOf("Select GP", "Kotauratla", "Govindapuram", "Meravai", "Pusapadu", "Ramachandrapuram", "Valluru", "Yanamadala"),
            "MAKAVARAPALEM" to listOf("Select GP", "Makavarapalem", "Chintala Agraharam", "Thondavada", "Bheemunipatnam (Rural)", "Gandhinagaram", "Rajavaram"),
            "NAKKAPALLI" to listOf("Select GP", "Nakkapalli", "Krishnapuram", "Basavapalem", "Kothapeta", "Gollavilli", "S. Rayavaram (Rural)"),
            "NARSIPATNAM" to listOf("Select GP", "Narsipatnam", "Pedaboddepalle", "Chintapalle", "Addateegala (Rural)", "Mallempeta", "Gudivada (Narsipatnam)", "Ravikamatham (Outskirts)"),
            "NATHAVARAM" to listOf("Select GP", "Nathavaram", "Rajavaram", "Ramachandrapuram", "Rajayyapeta", "Gandhinagaram", "Kondam"),
            "BUTCHIAHPETA" to listOf("Select GP", "Aithampudi", "Appampalem", "Bhatlova", "Butchayyapeta", "China Madina", "Chinappannapalem", "Chintapaka", "Chittiyyapalem", "Dibbidi", "Gantikorlam", "Gunnempudi", "Kandipudi", "Karaka", "Komallapudi", "Kondapalem", "Kondapalem Agraharam", "Kondempudi", "L Singavaram", "Lopudi", "Mallam", "Mallam Bhupathipalem", "Mangalapuram", "Neelakantapuram", "Nimmalova", "P Bheemavaram", "Pangidi", "Peda Madina", "Pedapudi", "Pedapudi Agraharam", "Polepalle", "Pottidorapalem", "R Bheemavaram", "R Sivarampuram", "Rajam"),
            "PAYAKARAOPETA" to listOf("Select GP", "Payakaraopeta", "Peddapuram", "Mangavaram", "Gopalapatnam"),
            "RAVIKAMATHAM" to listOf("Select GP", "Ravikamatham", "Yelakapadu", "Bapiraju Kothapalli", "Chintapalli"),
            "ROLUGUNTA" to listOf("Select GP", "Rolugunta", "Chintalapudi", "Chintapalle", "Kothuru"),
            "S.RAYAVARAM" to listOf("Select GP", "S.Rayavaram", "Pedanandipalli", "Gollalapalem", "Kancheru"),
            "ANANTAPUR" to listOf("Select GP", "Anantapuramu", "Alamuru", "Chiyyedu", "Gollapalle", "Itikalapalle", "Kurugunta", "Narayanapuram", "Papampeta", "Rachanapalle"),
            "ATMAKUR" to listOf("Select GP", "Atmakur", "Cheyyarajupalle", "Gummagudipahad", "Muppavaram", "Vennur"),
            "BUKKARAYA SAMUDRAM" to listOf("Select GP", "Bukkaraya Samudram", "Cherlopalle", "Narayanapuram", "Brahmanapalle", "Basavanapalle"),
            "GARLADINNE" to listOf("Select GP", "Garladinne", "Kodimi", "Kottala", "Kondampalle", "Pandlapuram"),
            "KUDERU" to listOf("Select GP", "Kuderu", "Chinnampalli", "Konakondla", "Kappalabanda", "Gollaladinne"),
            "PUTTAPARTHI" to listOf("Select GP", "Puttaparthi", "Nallamada", "Gollapalli", "Venkatampalli", "Kothacheruvu"),
            "NARPALA" to listOf("Select GP", "Narpala", "Kodimi", "Timmampeta", "Kondapalle", "Chandana"),
            "SINGANAMALA" to listOf("Select GP", "Singanamala", "Maddulur", "Kalyandurgam", "Venkatampalli", "Nerimetla"),
            "TADIPATRI" to listOf("Select GP", "Tadipatri", "Chennampalle", "Gollapalle", "Vemulapadu", "Reddipalle"),
            "TALUPULA" to listOf("Select GP", "Talupula", "Obuladevaracheruvu", "Gorantla", "Gandla Penta", "Burakayalakota"),
            "URAVAKONDA" to listOf("Select GP", "Uravakonda", "Hulikunta", "Eguvaguttapalle", "Konakondla", "Gulyam"),
            "VIDAPANAKAL" to listOf("Select GP", "Vidapanakal", "Chinaganipalli", "Pathapalle", "Kundurpi", "Yellampalli"),
            "YADIKI" to listOf("Select GP", "Yadiki", "Pottipadu", "Pullagurthi", "Thurakapalli", "Budagavi"),
            "YELLANUR" to listOf("Select GP", "Yellanur", "Kondapuram", "Kondakamarla", "Medukurthy", "Mundlapalle"),

                "Amarapuram" to listOf("Select GP","Gollahalli", "Mallinayanapalli"),
                "Brahmasamudram" to listOf("Select GP","Mopidi", "Rachumarri"),
                "Beluguppa" to listOf("Select GP","Vepalaparthi", "Cherlopalli"),
                "Gummagatta" to listOf("Pottipadu", "Kottapalli"),
                "Kundurpi" to listOf("Select GP","Kannepalli", "Dorigallu"),
                "Kalyandurg" to listOf("Select GP","D.Hirehal", "Pampanur"),
                "Settur" to listOf("Select GP","Vepulaparthi", "D.Cherlopalli"),
                "Ramagiri" to listOf("Select GP","Balijapalli", "Nelagonda"),
                "Kanekal" to listOf("Select GP","Doddipalli", "Chintalayapalli"),
                "Kambadur" to listOf("Select GP","Bhatrachalam", "Kondapuram"),
                "D. Hirehal" to listOf("Select GP","Bommaghatta", "Venkatampalli"),
                "Rayadurg" to listOf("Select GP","Venkatampalli", "Jambuladinne"),
                "Penukonda" to listOf("Select GP","Hanimireddipalli", "Marrivemula"),
                "Hindupur" to listOf("Select GP","Chinnamallepalli", "Vasapuram"),
                "Chilamathur" to listOf("Select GP","Kadirepalli", "Devarahalli"),
                "Lepakshi" to listOf("Select GP","Kodikonda", "Basavanapalli"),
                "Parigi" to listOf("Select GP","Peddachinnampalli", "Chamaluru"),
                "Roddam" to listOf("Select GP","Jeedipalli", "Gollapuram"),
                "Somandepalle" to listOf("Select GP","Madigubba", "Ganjikunta"),
                "Madakasira" to listOf("Select GP","Obuladevaracheruvu", "Kodikonda"),
                "Rolla" to listOf("Select GP","Nallacheruvu", "Lakshmipura"),
                "Agali" to listOf("Select GP","Vepalur", "Kogira"),
                "Gudibanda" to listOf("Select GP","Budili", "Gudemaranahalli"),
            "Anantapur Rural" to listOf("Select GP", "Papampeta", "A. Narayanapuram", "Akkampalli", "Akuthotapalli", "Alamuru", "Anantapur (R)", "Chinnampalli", "Chiyyedu", "Itikalapalle", "Kakkalapalli", "Kakkalapalli Colony", "Kamarupalli", "Kandukur", "Katiganikalva", "Kattakindapalli", "Kodimi", "Kurugunta", "Mannila", "Narasanayunikunta", "Pulakunta", "Rachanapalle", "Rajiv Colony", "Rudrampeta", "Somaladoddi", "Thaticherla", "Upparapalli"),
            "Rayachoti" to listOf("Select GP", "Abbavaram", "Botlacheruvu", "Cherlopalle", "Dullavaripalle", "Gorlamudiveedu", "Guntimadugu", "Indukurupalle", "Katimayakunta", "Madhavaram", "Masapet", "Peddakalvapalle", "Pemmadapalle", "Sibyala", "Syamalavaripalle", "Yandapalle", "Yerranagupalle"),
            "Chinnamandyam" to listOf("Select GP", "Bonamala", "Chinnamandem", "Devagudipalle", "Diguvagottiveedu", "Kalibanda", "Mallur", "Paramatikona", "T.Sakibanda", "Vandadi"),
            "Gurramkonda" to listOf("Select GP", "Gurramkonda", "Tharigonda", "Marripadu", "Pasalavandla Palle", "Cherlopalle"),
            "Sambepalli" to listOf("Select GP", "Settipalle", "Devapatla", "Narayanareddipalle", "Dudyala", "Sambepalli"),
            "Kalakada" to listOf("Select GP", "Kalakada", "Therkathota", "Gandlapenta", "Kukalivaripalle", "Ramapura"),
            "Galiveedu" to listOf("Select GP", "Galiveedu","Talakona", "Thumula Cheruvu", "Vedarajupalle", "Ramupalli"),
            "Pileru" to listOf("Select GP", "Pileru", "Gangamgunta", "Chinnamandem", "Dodda", "Vagavalasa"),
            "Lakkireddypalli" to listOf("Select GP", "Lakkireddypalli", "Mariyal", "Chandrampalle", "Gollapalle", "Chennamacharla"),
            "T.Sundupalle" to listOf("Select GP", "T.Sundupalle", "Pullampeta", "Yerraballi", "Siddavaram", "Vempalli"),
            "Chakrayapet" to listOf("Select GP", "Chakrayapet", "Settivari Palle", "Yellampalle", "Dudyala", "Gollapalle", "Mallela", "Venkatapuram"),
            "Veeraballi" to listOf("Select GP", "Veeraballi", "Pannur", "B.Kothakota", "Devagudi", "Gollapalli", "Mandapalle", "Nekunampeta"),
            "Rajampet" to listOf("Select GP", "Rajampet", "Koneruvaripalle", "Gudipadu", "Mangampet", "Gollapalle", "Chintakommadinne"),
            "Pullampet" to listOf("Select GP", "Pullampet", "Bhakarapet", "Kalikiri", "Rallapalle", "Gandlur", "Chennamreddipalle"),
            "Penagalur" to listOf("Select GP", "Penagalur", "Yadavolu", "Ravindra Nagar", "Anantarajupeta", "Nekkonda", "Sirasanambedu"),
            "Obulavaripalle" to listOf("Select GP", "Obulavaripalle", "Koduru", "Kothapalli", "Gollapalle", "Donthiralla", "Ankireddipalle"),
            "Nandalur" to listOf("Select GP", "Nandalur", "Chitlur", "B.Kothapalle", "Kalikiri", "Siddavaram", "Thallapaka"),
            "Sidhout" to listOf("Select GP", "Sidhout", "Kummarapalle", "Ravindra Nagar", "Nagireddipalle", "Madhavaram", "Venkatapuram"),
            "Vontimitta" to listOf("Select GP", "Vontimitta", "Polavaram", "Nallaguntla", "Tirumalaiahpalle", "Kakarla", "Kalikiri"),
            "Yerraguntla" to listOf("Select GP", "Yerraguntla", "Mallela", "Kondepalle", "Mittameedapalle", "Venkatapuram", "Obulapuram"),
            "Chitvel" to listOf("Select GP", "Chitvel", "Konduru", "Bhakarapet", "Pathapalle", "Maddileti", "Gollapalli"),
            "Madanapalle" to listOf("Select GP", "Madanapalle", "Basinikonda", "Angallu", "Bandameedapalle", "Vayalpad", "Nimmagallu", "Vepanapalle"),
            "Ramasamudram" to listOf("Select GP","Ramasamudram", "Thambadi", "Lakshminarasapuram", "Alavandlapalle", "Gollapalle", "Bheemaganipalle"),
            "B.Kothakota" to listOf("Select GP", "B.Kothakota", "Bairupalle", "Jalluru", "Somala", "Kondapuram", "Kadirinayanipalle"),
            "Peddamandyam" to listOf("Select GP", "Peddamandyam", "Kondreddigaripalle", "Nellimanda", "Yerraballi", "Tadimarri", "Anjaneyapalle"),
            "Thamballapalle" to listOf("Select GP", "Thamballapalle", "Thatimakula Palle", "Chilamathur", "Mulakalacheruvu", "Chittiboyanapalle", "Kurabalakota"),
            "Kurabalakota" to listOf("Select GP", "Kurabalakota", "Alapalle", "Gundlapalle", "Kothacheruvu", "Gangireddipalle", "Marripalle"),
            "Mulakalacheruvu" to listOf("Select GP", "Mulakalacheruvu", "Velicherla", "Reddivaripalle", "Kalluru", "Marrimakulapalle", "Singarayapalle"),
            "Bapatla" to listOf("Select GP", "Bapatla East", "Bapatla West", "Murukondapadu", "Maruproluvaripalem", "Narasayapalem"),
            "Karlapalem" to listOf("Select GP", "Karlapalem", "Perali", "Ganapavaram"),
            "Pittalavanipalem" to listOf("Select GP", "Pittalavanipalem", "Appikatla", "Poondla"),
            "Cherukupalle" to listOf("Select GP", "Cherukupalle", "Bhattiprolu", "Pallekona"),
            "Repalle" to listOf("Select GP", "Repalle", "Kesanapalle", "Jillellamudi"),
            "Nizampatnam" to listOf("Select GP", "Nizampatnam", "Nagaram", "Chakalakonda"),
            "Bhattiprolu" to listOf("Select GP", "Bhattiprolu", "Chilumuru", "Kuchinapudi"),
            "Chirala" to listOf("Select GP", "Chirala", "Vejendla", "Jandrapeta", "Perala", "Ramapuram"),
            "Vetapalem" to listOf("Select GP", "Vetapalem", "Koppolu", "Vedayapalem", "Ipurupalem"),
            "Karamchedu" to listOf("Select GP", "Karamchedu", "Anantharam", "Vadlamudi", "Chimakurthy"),
            "Inkollu" to listOf("Select GP", "Inkollu", "Chimmapudi", "Gudipudi", "Vadarevu"),
            "Martur" to listOf("Select GP", "Martur", "Parchur", "Marripudi", "Neravetla"),
            "Chinaganjam" to listOf("Select GP", "Chinaganjam", "Pathapadu", "Rajupalem", "Daggumalli"),
            "Janakavaram Panguluru" to listOf("Select GP", "Janakavaram", "Panguluru", "Gopalamvaripalem", "Somavarappadu"),
            "Addanki" to listOf("Select GP", "Addanki", "Chinna Ganjam", "Lingamguntla", "Kondamudusu", "Nidamanuru"),
            "Ballikurava" to listOf("Select GP", "Ballikurava", "Narasayapalem", "Devarayabhotlapalem", "Ayyannapalem"),
            "Marripudi" to listOf("Select GP", "Marripudi", "Kondamudusu", "Janapadu", "Santhamaguluru"),
            "Santhamaguluru" to listOf("Select GP", "Santhamaguluru", "Pathepuram", "Munagapadu", "Mutluru"),
            "Parchur" to listOf("Select GP", "Parchur", "Veeravasaram", "Pamidipadu", "Chimakurthy"),
            "Yeddanapudi" to listOf("Select GP", "Yeddanapudi", "Mutluru", "Chilakapadu", "Santhagudipadu"),
            "Naguluppalapadu" to listOf("Select GP", "Naguluppalapadu", "Sivaramapuram","Basavapalem", "Chandavaram"),
            "Chittoor" to listOf("Select GP", "Murakambattu", "Kanipakam", "Mittoor", "Mangasamudram", "Durugadda"),
            "Puthalapattu" to listOf("Select GP", "Puthalapattu", "Gangudupalle", "Vallivedu", "Brahmanapalle"),
            "Gangadhara Nellore" to listOf("Select GP", "G.D. Nellore", "Kalikiri", "Yellampalle", "Gollapalle"),
            "Vedurukuppam" to listOf("Select GP", "Vedurukuppam", "Chinapandraka", "Perumallapalle"),
            "Srirangarajapuram" to listOf("Select GP", "S.R. Puram", "Tirumani", "Gudur", "Mogilicharla"),
            "Penumuru" to listOf("Select GP", "Penumuru", "Kallur", "Diguva Ambedu"),
            "Irala" to listOf("Select GP", "Irala", "Mogilivaripalle", "Diguvapalle"),

            "Palamaner" to listOf("Select GP", "Palamaner", "Appinapalle", "Pedduru", "Venugopalapuram"),
            "Kuppam" to listOf("Select GP", "Kuppam", "Dudekonda", "Ammagaripalle", "Peddabrahmadevam"),
            "Gudupalle" to listOf("Select GP", "Gudupalle", "Gollapalle", "Marthadipalle"),
            "Punganur" to listOf("Select GP", "Punganur", "Kadapanatham", "Devalampet", "Gollapalle"),
            "Venkatagirikota" to listOf("Select GP", "Venkatagirikota", "Agraharam", "Gudipalle"),
            "Baireddipalle" to listOf("Select GP", "Baireddipalle", "Gundlapalle", "Palaguttapalle"),
            "Ramakuppam" to listOf("Select GP", "Ramakuppam", "Vogili", "Ellampalle"),

            "Madanapalle" to listOf("Select GP", "Madanapalle", "Basinikonda", "Punganur Road", "Bandameedapalle"),
            "Thamballapalle" to listOf("Select GP", "Thamballapalle", "Peddabommalapuram", "Gundrevula", "Chowdepalle"),
            "Kurabalakota" to listOf("Select GP", "Kurabalakota", "Kalicherla", "Thuluvapalle", "Mallikarjunapuram"),
            "Peddamandyam" to listOf("Select GP", "Peddamandyam", "Gandlapenta", "Devalacheruvu"),
            "Ramasamudram" to listOf("Select GP", "Ramasamudram", "Gollapalle", "Kurupalle"),
            "Mulakalacheruvu" to listOf("Select GP", "Mulakalacheruvu", "Cherukumukkala", "Peddamallepalle"),
            "B.Kothakota" to listOf("Select GP", "B.Kothakota", "Thotakanuma", "Gundlapalle", "Kothakota Cross"),
            "Amalapuram" to listOf("Select GP","Amalapuram Town", "Bandarulanka", "Edarapalli", "Valluru", "Appanapalli"),
            "Atreyapuram" to listOf("Select GP","Atreyapuram", "Ryali", "Jonnada", "Vadapalle"),
            "Uppalaguptam" to listOf("Select GP","Uppalaguptam", "Bheemanapalli", "Kadali", "Injaram"),
            "Mummidivaram" to listOf("Select GP","Mummidivaram", "Pasarlapudi", "Lakkavaram", "Kesanapalli"),
            "Ainavilli" to listOf("Select GP","Ainavilli", "Kodavali", "Vakatippa", "Kondukuduru"),
            "Ambajipeta" to listOf("Select GP","Ambajipeta", "Vilasa", "Iragavaram", "Pulletikurru"),
            "Katrenikona" to listOf("Select GP","Katrenikona", "Mogalikuduru", "Kesanapalli", "Mullipadu"),
            "Allavaram" to listOf("Select GP","Allavaram", "Bendapudi", "Talarivillu", "Komaragiripatnam"),
            "Razole" to listOf("Select GP","Razole", "Chintalapudi", "Kothalanka", "Lakshmiwada"),
            "Malikipuram" to listOf("Select GP","Malikipuram", "Geddada", "Kesavadasupalem", "Botlakurru"),
            "Sakhinetipalli" to listOf("Select GP","Sakhinetipalli", "Antharvedipalem", "Chirrayanam", "Kesavaram"),
            "Mamidikuduru" to listOf("Select GP","Mamidikuduru", "Nagaram", "Komarada", "Adurru"),
            "I. Polavaram" to listOf("Select GP","I. Polavaram", "Gudapalli", "Tatipaka", "Ponnamanda"),
            "Ramachandrapuram" to listOf("Select GP","Ramachandrapuram", "Bikkavolu", "Draksharama", "Velampalem"),
            "Rayavaram" to listOf("Select GP","Rayavaram", "Chodavaram", "Burugupudi", "Chebrolu"),
            "Alamuru" to listOf("Select GP","Alamuru", "Gonugudem", "Karavaka", "Kandulapalem"),
            "Kothapeta" to listOf("Select GP","Kothapeta", "Mandapalle", "Vadapalem", "Nadipudi"),
            "Kapileswarapuram" to listOf("Select GP","Kapileswarapuram", "Ibrahimpatnam", "Ankampalem", "Buddampadu"),
            "Eluru" to listOf("Select GP", "Eluru","Gavaravaram", "Tangellamudi", "Sanivarapupeta", "Chataparru", "Gudivakalanka"),
            "Denduluru" to listOf("Select GP", "Denduluru", "Gopannapalem", "Compireddipalle", "Vegavaram"),
            "Pedavegi" to listOf("Select GP","Pedavegi", "Vatluru", "Tammileru Colony"),
            "Pedapadu" to listOf("Select GP", "Pedapadu", "Kalaparru", "Burugagudem", "Satyavolu"),
            "Unguturu" to listOf("Select GP", "Unguturu", "Chebrolu", "Badampudi", "Kaikaram"),
            "Bhimadole" to listOf("Select GP", "Bhimadole", "Bachibabu Palem", "Chebrolu GP"),
            "Nidamarru" to listOf("Select GP", "Nidamarru", "Narasapuram", "Pedakantavelly"),
            "Ganapavaram" to listOf("Select GP", "Ganapavaram", "Vemavaram", "Pedanindrakolanu"),
            "Kaikalur" to listOf("Select GP", "Kaikalur", "Chanduluru", "Chejerla"),
            "Mandavalli" to listOf("Select GP", "Mandavalli", "Geddada", "Pedarayavaram"),
            "Kalidindi" to listOf("Select GP", "Kalidindi", "Mudivarthi", "Bapulapadu"),
            "Mudinepalli" to listOf("Select GP", "Mudinepalli", "Kondapalle", "Chegireddypalem"),
            "Eluru_Rural" to listOf("Select GP", "Sanivarapupeta", "Tangellamudi", "Gavaravaram"),
            "Jangareddygudem" to listOf("Select GP", "Jangareddygudem", "Lakkavaram", "Vegavaram"),
            "Buttayagudem" to listOf("Select GP", "Buttayagudem", "Venkatapuram"),
            "Jeelugumilli" to listOf("Select GP", "Jeelugumilli", "Ravikampadu"),
            "Koyyalagudem" to listOf("Select GP", "Koyyalagudem", "Yerrampeta"),
            "Polavaram" to listOf("Select GP", "Polavaram", "Pattisam"),
            "T. Narasapuram" to listOf("Select GP", "T. Narasapuram", "Thallapudi"),
            "Kamavarapukota" to listOf("Select GP", "Kamavarapukota", "Rayannapalem"),
            "Kukunoor" to listOf("Select GP", "Kukunoor"),
            "Velairpadu" to listOf("Select GP", "Velairpadu"),
            "Nuzvid" to listOf("Select GP", "Nuzvid", "Hanuman Junction"),
            "Agiripalli" to listOf("Select GP", "Agiripalli", "Adivinekkalam"),
            "Chatrai" to listOf("Select GP", "Chatrai", "Chennur"),
            "Musunuru" to listOf("Select GP", "Musunuru", "Kavuluru"),
            "Chintalapudi" to listOf("Select GP", "Chintalapudi", "Allipalle"),
            "Lingapalem" to listOf("Select GP", "Lingapalem", "Malleswaram"),
            "Guntur East" to listOf("Select GP", "Budampadu", "Etukuru", "Gorantla", "Jonnalagadda"),
        "Guntur West" to listOf("Select GP", "Ankireddypalem", "Chowdavaram", "Nallapadu", "Pedapalakaluru", "Pothuru"),
        "Pedakakani" to listOf("Select GP", "Pedakakani", "Namburu", "Koppuravuru", "Takkellapadu", "Uppalapadu"),
        "Medikonduru" to listOf("Select GP", "Medikonduru", "Mandapadu", "Dokiparru", "Ponugupadu"),
        "Phirangipuram" to listOf("Select GP", "Phirangipuram", "Other major settlements"),
        "Prathipadu" to listOf("Select GP", "Prathipadu", "Surikonda", "Other leading villages"),
        "Tadikonda" to listOf("Select GP", "Tadikonda", "Amaravathi", "Vaddamanu"),
        "Vatticherukuru" to listOf("Select GP", "Vatticherukuru", "Pedakancherla", "Other population centers"),
        "Pedanandipadu" to listOf("Select GP", "Pedanandipadu", "Ravipadu", "Other large GPs"),
        "Tullur" to listOf("Select GP", "Thullur", "Velagapudi", "Other cluster villages"),
// --- Tenali Division ---
            "Tenali" to listOf("Select GP", "Angalakuduru", "Burripalem", "Katevaram", "Kolakaluru", "Nandivelugu", "Pedaravuru", "Pinapadu", "Sangam Jagarlamudi"),
            "Vemuru" to listOf("Select GP", "Vemuru", "Ananthavaram", "Abbanagudavalli", "Bhattiprolu", "Nidubrolu", "Penumarru"),
            "Kollipara" to listOf("Select GP", "Kollipara", "Munnangi", "Zamidintakurru", "Gurazada", "Jampani"),
            "Kollur" to listOf("Select GP", "Kollur", "Ananthavaram", "Meduru", "Chilumuru", "China Ganjam"),
            "Bhattiprolu" to listOf("Select GP", "Bhattiprolu", "Appikatla", "Manchala", "Surepalli", "Sangamjagarlamudi"),
            "Cherukupalle" to listOf("Select GP", "Cherukupalle", "Bhavadevarapalle", "Kodithipparru", "Mandavalli", "Kothapeta"),
            "Repalle" to listOf("Select GP", "Repalle", "Bethapudi", "Chodayapalem", "Gangadipalem", "Isukapalle", "Peteru", "Potumeraka", "Visweswaram"),
            "Nizampatnam" to listOf("Select GP", "Nizampatnam", "Bodapadu", "East Narsapur", "Gopalamvaripalem", "Gurujala", "Lingamguntla", "Nagaram", "Rajavolu"),

            // --- Narasaraopet Division ---
            "Narasaraopet" to listOf("Select GP", "Narasaraopet", "Guvvalapalem", "Kesanupalli", "Kothapalle", "Munnangi", "Narikonda", "Peddakurapadu"),
            "Chilakaluripet" to listOf("Select GP", "Chilakaluripet", "Bodhanam", "Jonnalagadda", "Lingamguntla", "Mothadaka", "Nidamanuru"),
            "Sattenapalli" to listOf("Select GP", "Sattenapalli", "Gurazala", "Kondamodu", "Madala", "Muppalla", "Nagaram", "Ponnur"),
            "Piduguralla" to listOf("Select GP", "Piduguralla", "Achampet", "Chinapulivarru", "Kambhampadu", "Nakarikallu", "Tadikonda"),
            "Macherla" to listOf("Select GP", "Macherla", "Gadipudi", "Macherla Rural", "Rentachintala", "Rompicherla", "Vellatur"),
            "Dachepalli" to listOf("Select GP", "Dachepalli", "Gurazala", "Kambhampadu", "Pittalavanipalem", "Vellatur", "Karampudi"),
            "Bellamkonda" to listOf("Select GP", "Bellamkonda", "Chamarru", "Lankapalli", "Pasarlapudi", "Rajupalem", "Utukuru"),
            "Nadikudi" to listOf("Select GP", "Nadikudi", "Bodrayapalle", "Chigurupadu", "Dharmavaram", "Edlapadu", "Peddavaram"),
            "Vinukonda" to listOf("Select GP", "Vinukonda", "Chinarikatla", "Kanamarlapudi", "Marripudi", "Rajupalem", "Thimmapuram"),
            "Ipur" to listOf("Select GP", "Ipur", "Dasaripalem", "Gundlapalli", "Ipurupalem", "Peddavaram", "Ramireddipalem"),
            "Savalyapuram" to listOf("Select GP", "Savalyapuram", "Achampalli", "Chinapalem", "Kondamudusuvaripalem", "Nekarikallu", "Ramapuram"),
            "Kakinada Rural" to listOf("Select GP", "Turangi", "Valasapakala", "Kandrakota", "Thimmapuram"),
            "Kakinada Urban" to listOf("Select Village/GP", "Jagannaickpur", "Sarpavaram", "Indrapalem"),
            "Karapa" to listOf("Select Village/GP", "Karapa", "K. Kathipudi", "Penuguduru", "Peddada"),
            "Pedapudi" to listOf("Select Village/GP", "Pedapudi", "Devarapalli", "Chollangi", "Chebrolu"),
            "U. Kothapalli" to listOf("Select Village/GP", "Uppalanka", "Kothapalli", "Sankarlanka"),

            "Peddapuram" to listOf("Select Village/GP", "Peddapuram", "Kandrakota", "Duppalapudi"),
            "Gollaprolu" to listOf("Select Village/GP", "Gollaprolu", "Yelamanchili", "Chebrolu"),
            "Samalkota" to listOf("Select Village/GP", "Samalkota", "Kesanapalli", "Rayavaram"),
            "Gandepalle" to listOf("Select Village/GP", "Gandepalli", "Jaggampeta"),
            "Pithapuram" to listOf("Select Village/GP", "Pithapuram", "Madhavapuram", "Kottam", "Kandrakota"),

            "Tuni" to listOf("Select Village/GP", "Tuni", "Vemavaram", "Kirlampudi"),
            "Thondangi" to listOf("Select Village/GP", "Thondangi", "Peddapurapadu", "G. Kothapalli"),
            "Sankhavaram" to listOf("Select Village/GP", "Sankhavaram", "Rajaram", "Krishna Devi Peta"),
            "Prathipadu" to listOf("Select Village/GP", "Prathipadu", "Koppaka", "Komaripalem"),

            "Rampachodavaram" to listOf("Select Village/GP", "Rampachodavaram", "Gummaluru", "Nellipaka"),
            "Maredumilli" to listOf("Select Village/GP", "Maredumilli", "Valamuru", "Venkatapuram"),
            "Addateegala" to listOf("Select Village/GP", "Addateegala", "Gandepalli", "Chintapalli"),
            "Gangavaram" to listOf("Select Village/GP", "Gangavaram", "Mallavaram", "Kondamodalu"),
// Vijayawada Division
            "Vijayawada Rural" to listOf("Select Gram Panchayat", "Enikepadu", "Jakkampudi", "Gollapudi"),
            "Vijayawada Urban" to listOf("Select Gram Panchayat", "Patamata", "Benz Circle", "Governorpet"),
            "Ibrahimpatnam" to listOf("Select Gram Panchayat", "Ibrahimpatnam", "Kondapalli", "Jupudi"),
            "G. Konduru" to listOf("Select Gram Panchayat", "G. Konduru", "Rayanapadu", "Chandragudem"),
            "Kankipadu" to listOf("Select Gram Panchayat", "Kankipadu", "Kesarapalli", "Lankapalli"),
            "Penamaluru" to listOf("Select Gram Panchayat", "Penamaluru", "Poranki", "Yanamalakuduru"),
            "Thotlavalluru" to listOf("Select Gram Panchayat", "Thotlavalluru", "Kunchanapalli", "Vuyyuru"),

            // Machilipatnam Division
            "Machilipatnam" to listOf("Select Gram Panchayat", "Machilipatnam", "Pedana", "Machavaram"),
            "Challapalli" to listOf("Select Gram Panchayat", "Challapalli", "Koduru", "Chinnapuram"),
            "Koduru" to listOf("Select Gram Panchayat", "Koduru", "Lingareddypalem", "Moguluru"),
            "Avanigadda" to listOf("Select Gram Panchayat", "Avanigadda", "Puligadda", "Edurumondi"),
            "Nagayalanka" to listOf("Select Gram Panchayat", "Nagayalanka", "Gullalamoda", "Pallipalem"),
            "Pedana" to listOf("Select Gram Panchayat", "Pedana", "Chintalapudi", "Kummamuru"),
            "Ghantasala" to listOf("Select Gram Panchayat", "Ghantasala", "Cherukupalli", "Kolluru"),

            // Nuzvid Division
            "Nuzvid" to listOf("Select Gram Panchayat", "Nuzvid", "Annavaram", "Reddigudem"),
            "Agiripalli" to listOf("Select Gram Panchayat", "Agiripalli", "Tiruvuru", "Kanchikacherla"),
            "Musunuru" to listOf("Select Gram Panchayat", "Musunuru", "Chekkapalli", "Kodurupadu"),
            "Chatrai" to listOf("Select Gram Panchayat", "Chatrai", "Vemavaram", "Meduru"),
            "Vissannapeta" to listOf("Select Gram Panchayat", "Vissannapeta", "Tadinada", "Lingala"),
            "Mandavalli" to listOf("Select Gram Panchayat", "Mandavalli", "Chintapadu", "Lellapudi"),
            "Kaikaluru" to listOf("Select Gram Panchayat", "Kaikaluru", "Kalidindi", "Pallevada"),
            "Kurnool Urban" to listOf("Select GP", "Kurnool Municipal Corporation"),
            "Kurnool Rural" to listOf("Select GP", "G. Singavaram", "Gargeyapuram", "Gondiparla", "Rudravaram"),
            "Adoni" to listOf("Select GP", "Adoni Municipality", "Pedda Thumbalam","Madire", "Ganekal","Naranapuram"),
            "Gudur" to listOf("Select GP", "Gudur", "Budidapadu", "Chanugondla", "Gudipadu"),
            "C. Belagal" to listOf("Select GP", "C. Belagal", "Malasomapuran", "Palukudoddi", "Gundrevula"),
            "Orvakal" to listOf("Select GP", "Orvakal", "Nannur", "Uppalapadu", "Palakolanu"),
            "Veldurthi" to listOf("Select GP", "Veldurthi", "Cherukulapadu", "Ramallakota", "Sri Rangapuram"),
            "Kurnool Urban" to listOf("Select GP", "B.Tandrapadu", "Panchalingala", "Venkatrayapuram", "Jagannatha Gattu"),
            "Kurnool Rural" to listOf("Select GP", "Sunkesula", "Pullur", "Nidugal", "Pasupula"),
            "Gudur" to listOf("Select GP", "Gudur", "Chinapadava", "Kalugotla", "Pulakurthi"),
            "C. Belagal" to listOf("Select GP", "C. Belagal", "Nidhanur", "Kambadur", "Erikindi Thanda"),
            "Kallur" to listOf("Select GP", "Midthur", "Bollavaram", "Lakshmi Nagar", "Kallur"),
            "Orvakal" to listOf("Select GP", "Orvakal", "Bollavaram", "Panyam", "Laddugandla"),
            "Kodumur" to listOf("Select GP", "Kodumur", "Pulakurthi", "Yerradoddi", "Nerawada"),
            "Veldurthi" to listOf("Select GP", "Veldurthi", "Cherukulapadu", "Ramallakota", "Sri Rangapuram"),
            "Alur" to listOf("Select GP", "Alur", "Chinayarunatham", "Somanathahalli", "Singanamala"),
            "Aspari" to listOf("Select GP", "Aspari", "Sulekal", "Chinnamandadi", "Yerradoddi"),
            "Chippagiri" to listOf("Select GP", "Chippagiri", "Mallikarjunahalli", "Garladinne", "Veeranna Gutta"),
            "Devanakonda" to listOf("Select GP", "Devanakonda", "Yadiki", "Nandivargam", "Mundlamuru"),
            "Halaharvi" to listOf("Select GP", "Halaharvi", "Mukkunda", "Nagamalla Doddi", "Kosigi"),
            "Maddikera" to listOf("Select GP", "Maddikera", "Thimmapuram", "Thuggali", "Garladinne"),
            "Pattikonda" to listOf("Select GP", "Pattikonda", "Maruthi Nagar", "Amaravati", "Thumadam"),
            "Tuggali" to listOf("Select GP", "Tuggali", "Alaganoor", "Somayajula", "Pinnapuram"),
            "Krishnagiri" to listOf("Select GP", "Krishnagiri", "Laddagiri", "Hanumanthapuram", "Gonegandla"),
            "Nandyal" to listOf("Select GP", "Nandyal Rural", "Kothapalle", "Gajulapalle", "Srinivasa Nagar"),
            "Gospadu" to listOf("Select GP", "Gospadu", "Moguluru", "Maruthi Nagar", "Pathapalle"),
            "Sirvel" to listOf("Select GP", "Sirvel", "Basinepalle", "Nallagatla", "Brahmanapalle"),
            "Dornipadu" to listOf("Select GP", "Dornipadu", "Pottipadu", "Uyyalawada", "Konidedu"),
            "Uyyalawada" to listOf("Select GP", "Uyyalawada", "Kotakonda", "Yallur", "Devanakonda"),
            "Chagalamarri" to listOf("Select GP", "Chagalamarri", "Yerraguntla", "Balapanur", "Gospadu Thanda"),
            "Rudravaram" to listOf("Select GP", "Rudravaram", "Velugodu", "Mylavaram", "Niduzuvvi"),
            "Mahanandi" to listOf("Select GP", "Mahanandi", "Thimmapuram", "Brahmanapalle", "Kandukur"),
            "Allagadda" to listOf("Select GP", "Allagadda", "Chabolu", "Kambalapadu", "Gundla Palle"),
            "Panyam" to listOf("Select GP", "Panyam", "Gowrapuram", "Mittapalle", "Chabolu"),
            "Gadivemula" to listOf("Select GP", "Gadivemula", "Gani", "Thurupu Ramapuram", "Pathakodamagundla"),
            "Sanjamala" to listOf("Select GP", "Sanjamala", "Gospadu", "Pedda Mallepalle", "Chintalayapalle"),
            "Kolimigundla" to listOf("Select GP", "Kolimigundla", "Rachamalla", "Kondapuram", "Rajupalem"),
            "Bandi Atmakur" to listOf("Select GP", "Bandi Atmakur", "Kambalapadu", "Chintalayapalle", "Bhogapalle"),
            "Srisailam" to listOf("Select GP", "Srisailam", "Lingala", "Ganjihalli", "Domalapenta"),
            "Atmakur" to listOf("Select GP", "Atmakur", "Pedda Kottala", "Gani", "Nandivargam"),
            "Velgode" to listOf("Select GP", "Velgode", "Thimmapuram", "Basinenipalle", "Banaganapalle"),
            "Nandikotkur" to listOf("Select GP", "Nandikotkur", "Padakandla", "Kottapalle", "Chinnatekur"),
            "Pagidyala" to listOf("Select GP", "Pagidyala", "Thudumul", "Nehrunagar", "Yallur"),
            "J.Bungalow" to listOf("Select GP", "J.Bungalow", "Chinnaboinpalle", "Vankayalapadu", "Gadidam"),
            "Kothapalle" to listOf("Select GP", "Kothapalle", "Komarolu", "Peddapalle", "Racherla"),
            "Pamulapadu" to listOf("Select GP", "Pamulapadu", "Alluru", "Darsipadu", "Gopavaram"),
            "Midthur" to listOf("Select GP", "Midthur", "Kondapuram", "Kareddipadu", "Utukur"),
            "Dhone" to listOf("Select GP", "Dhone", "Somayajulapalle", "Chennampalle", "Peddapadu", "Gundla Palle", "Kalludevanahalli", "Kotha Kota", "Brahmana Kotkur", "Rangapuram", "Erragudi", "Ayyalur", "Rangapuram"),
            "Bethamcherla" to listOf("Select GP", "Bethamcherla", "Kowlur", "Malapuram", "Pamulapadu", "Banaganapalle", "Kalugotla", "Jammalamadugu", "Tummala Palle", "Uppalapadu"),
            "Peapully" to listOf("Select GP", "Peapully", "Loddipalle", "Gospadu", "Allur", "Velgode", "Vallampadu", "Lingala", "Brahmanapalle", "Gani", "Peddamudium"),
            "Banaganapalli" to listOf("Select GP", "Banaganapalli", "Koilakuntla", "Owk", "Chabolu", "Boyalakuntla", "Devanakonda", "Nandyalampadu", "Palukuru"),
            "Owk" to listOf("Select GP", "Owk", "Yerraguntla", "Pamulapadu", "Muddavaram", "Gani", "E. Cherlopalle", "Vasanthapuram"),
            "Koilakuntla" to listOf("Select GP", "Koilakuntla", "Uyyalawada", "Thummalapalle", "Chinna Vemula", "Kandukur", "Balapanur", "Chabolu", "Thangedupalle"),
            "Nandigama" to listOf("Select GP", "Nandigama", "Ameenapet", "Kakulapadu", "Madhavaram", "Lingala", "Munagala", "Kethaveerunipadu", "Tanikella", "Pedanandipadu"),
            "Kanchikacherla" to listOf("Select GP", "Kanchikacherla", "Nandigama (Rural)", "Mylavaram", "Anigandlapadu", "Kethanakonda", "Narsapuram", "Veeravalli", "Chandapuram"),
            "Chandarlapadu" to listOf("Select GP", "Chandarlapadu", "Chevitikallu", "Veladi", "Bhavanipuram", "Gundlapalli", "Kunkalagunta", "Muppalla", "Rayanapadu"),
            "Jaggayyapeta" to listOf("Select GP", "Jaggayyapeta", "Dachepalli", "Machavaram", "Chinapadu","Vallurupalem", "Kodavatikallu", "Chandupatla","Chilukuripadu"),
            "Vatsavai" to listOf("Select GP", "Vatsavai", "Tallur", "Lingamguntla", "Chinnakodur", "Jupudi", "Mallavaram","Vemavaram"),
            "Veerullapadu" to listOf("Select GP", "Veerullapadu", "Kattubadipalem", "Krishnaraopalem", "Munagacherla", "Tiruvuru", "Bhavanipuram", "Chandragudem", "Marripalem"),
            "Tiruvuru" to listOf("Select GP", "Tiruvuru", "Lingala", "Kuntamukkala", "Mylavaram", "Pamidimukkala", "Kallur", "Vallampatla", "Kondaparva", "Munagapadu"),
            "A. Konduru" to listOf("Select GP", "A. Konduru", "G. Konduru", "Kambhampadu", "Vissannapeta", "Duggirala", "Chintalapadu", "Konduru", "Chinapadu"),
            "Gampalagudem" to listOf("Select GP", "Gampalagudem", "Ravikampadu", "Chandragudem", "Kunkalagunta", "Vemavaram", "Lingala", "Nandigama", "Gopalapuram"),
            "Reddigudem" to listOf("Select GP", "Reddigudem", "Pangidigudem", "Kowtharam", "Kataram", "Narsapuram", "Chandarlapadu", "Pedaparupudi", "Bapulapadu"),
            "Visannapeta" to listOf("Select GP", "Visannapeta", "Tadinada", "Chandapuram", "Gopavaram", "Krishnapuram", "Tiruvuru Rural", "Pulluru", "Nuzvid Rural"),
            "Narasaraopet" to listOf("Select GP", "Narasaraopet Urban", "Ravipadu", "Yellamanda", "Gopalapuram", "Ponnur", "Lingamguntla", "Mutluru", "Chinaganjam", "Chinakakani"),
            "Chilakaluripet" to listOf("Select GP", "Chilakaluripet", "Narakodur", "Gunturvaripalem", "Purushothapatnam", "Kondabala Vari Palem", "Kondavaram", "Kotha Patnam", "Thimmapuram", "Jonnalagadda"),
            "Edlapadu" to listOf("Select GP", "Edlapadu", "Lingamguntla", "Machavaram", "Vayyakallu", "Chilakaluripet Rural", "Tadikonda", "Lam", "Phirangipuram"),
            "Ipur" to listOf("Select GP", "Ipur", "Karampudi", "Madala", "Narsapur", "Rajupalem", "Veldurthi", "Gudur", "Mutukuru"),
            "Nadendla" to listOf("Select GP", "Nadendla", "Lingamguntla", "Parchur", "Ballikurava", "Kollur", "Karlapalem", "Sattenapalle Rural"),
            "Nuzendla" to listOf("Select GP", "Nuzendla", "Rentachintala", "Chinapulivarru", "Yerragondapalem", "Vinukonda Rural", "Vemuru", "Nagaram"),
            "Rompicherla" to listOf("Select GP", "Rompicherla", "Lakkarajuvari Palem", "Kondamudi", "Chebrolu", "Karlakunta", "Pallapadu", "Peravali", "Sattenapalle Rural"),
            "Savalyapuram" to listOf("Select GP", "Savalyapuram", "Karampudi", "Yelamanda", "Ipur Rural", "Muppalla", "Ponnur", "Machavaram"),
            "Vinukonda" to listOf("Select GP", "Vinukonda", "Loyapalle", "Mallavaram", "Thimmanapalem", "Lingareddypalem", "Kambhampadu", "Mittapalle", "Pullalacheruvu"),
            "Amaravathi" to listOf("Select GP", "Amaravathi", "Velagapudi", "Mandadam", "Uddandarayunipalem", "Venkatapalem", "Rayapudi", "Lingayapalem", "Thullur"),
            "Atchampet" to listOf("Select GP", "Atchampet", "Pedakurapadu", "Gudipudi", "Gurazala", "Chintapalli", "Rajupalem", "Durgi"),
            "Bellamkonda" to listOf("Select GP", "Bellamkonda", "Macherla Rural", "Kambhampadu", "Appapuram", "Peddavaram", "Cheemalapadu", "Zangamarajupalle"),
            "Krosuru" to listOf("Select GP", "Krosuru", "Kambhampadu", "Kondamallepalle", "Kalluru", "Chilumuru", "Gundlapadu", "Tadikonda"),
            "Macherla" to listOf("Select GP", "Macherla Urban", "Rentachintala", "Karampudi", "Thummalacheruvu", "Bollapalle", "Durgi", "Peddavura"),
            "Gurazala" to listOf("Select GP", "Gurazala", "Karampudi", "Peddavura", "Rentachintala", "Durgi", "Machavaram", "Kondrapole", "Nekarikallu", "Ippagunta"),
            "Bellamkonda" to listOf("Select GP", "Bellamkonda", "Appapuram", "Kambhampadu", "Peddavaram", "Cheemalapadu", "Zangamarajupalle"),
            "Chilakaluripet" to listOf("Select GP", "Chilakaluripet", "Lam", "Chimakurthy", "Gollapudi", "Thimmapuram", "Nandigama (Rural)"),
            "Nadendla" to listOf("Select GP", "Nadendla", "Tummalacheruvu", "Lingamguntla", "Purushothapatnam", "Vellatur", "Ananthavaram"),
            "Parvathipuram" to listOf("Select GP", "Parvathipuram (Municipality)", "Narsipuram", "Buduruvada", "Lakshminarayanapuram"),
            "Seethanagaram" to listOf("Select GP", "Seethanagaram", "Joganpeta", "Pedabhogila", "Sangamvalasa"),
            "Balijipeta" to listOf("Select GP", "Balijipeta", "Mirthipadu", "MettaGandlapenta", "Pedakurupulanka"),
            "Salur" to listOf("Select GP", "Salur (Municipality)", "Turpu Salur", "Then Salur", "Narsipatnam"),
            "Pachipenta" to listOf("Select GP", "Pachipenta", "Kumili", "Giddangunta", "Venkatapuram"),
            "Makkuva" to listOf("Select GP", "Makkuva", "Mentada", "Malladi", "Gajulamamidala"),
            "Komarada" to listOf("Select GP", "Komarada", "Buduruvada", "Purnapadu", "Galavilli"),
            "Garugubilli" to listOf("Select GP", "Garugubilli", "Kothapeta", "Saravena", "Kumili"),
            "Palakonda" to listOf("Select GP", "Palakonda", "Kotturu", "Ramabhadrapuram", "Lingalavalasa", "Madhavapuram"),
            "Gummalakshmipuram" to listOf("Select GP", "Gummalakshmipuram","Gondi", "Rayagada", "Kurukutti", "Chintalavalasa"),
            "Kurupam" to listOf("Select GP", "Kurupam", "Kondawada", "Kondrajupeta", "Thotapalli", "Dibbaguda"),
            "Jiyyammavalasa" to listOf("Select GP", "Jiyyammavalasa", "Komarada", "Gorumamidapeta", "Gollapadu", "Laxmipuram"),
            "Seethampeta" to listOf("Select GP", "Seethampeta", "Pedduru", "Garlapadu", "Chinamerangi", "Tittivalasa"),
            "Bhamini" to listOf("Select GP", "Bhamini", "Kottapeta", "Mandasa", "Giribhadra", "Burja"),
            "Veeraghattam" to listOf("Select GP","Veeraghattam", "Chinamerangi", "Kothavalasa", "Singarayavalasa", "Jagannadhapuram"),
            // — Ongole Division —
            "Ongole Urban" to listOf("Select GP", "Ongole Municipal Corporation"),
            "Chimakurthy" to listOf("Select GP", "Chimakurthy", "Bandlamudi", "PNaidupalem"),
            "Naguluppalapadu" to listOf("Select GP", "Naguluppalapadu", "Devavaram"),
            // — Kanigiri Division —
            "Kanigiri" to listOf("Select GP", "Kanigiri", "Chakirla", "Peramgudipalli", "Gosulaveedu"),
            "Darsi" to listOf("Select GP", "Darsi", "Darsi Town Panchayat"),
            "Ardhaveedu" to listOf("Select GP", "Ardhaveedu", "Bollupalli", "Gannepalli"),
            "Bestavaripeta" to listOf("Select GP", "Bestavaripeta", "Pedda Obinenipalle", "Nekunambad"),
            // — Markapuram Division —
            "Markapuram" to listOf("Select GP", "Markapuram Municipality"),
            "Giddalur" to listOf("Select GP", "Giddalur Nagar Panchayat"),
            "Cumbum" to listOf("Select GP", "Cumbum", "Ananthasagaram"),
            "Pullalacheruvu" to listOf("Select GP", "Pullalacheruvu", "Pitikayagulla"),
            "Palasa" to listOf("Select GP", "Palasa", "Kothapeta", "Chandiputtuga", "Makavarapalem", "Gunduparti", "Loddaputti", "Metturu"),

            "Ichchapuram" to listOf("Select GP", "Ichchapuram", "Gollapeta", "Kanchili", "Kaviti", "Jagannadhapuram", "Kotabommali", "Iddivanipalem"),

            "Kaviti" to listOf("Select GP", "Kaviti", "Baruva", "Vamsadhara", "Ravivalasa", "Chinna Burusu", "Bodram", "Kaniti"),

            "Sompeta" to listOf("Select GP", "Sompeta", "Gollagandi", "Balaga", "Korlam", "Rattakanna", "M.N.Peta", "Gollagandi"),

            "Kanchili" to listOf("Select GP", "Kanchili", "Kothapalli", "Karajada", "Gudem", "Bompada", "Devupalli", "Kanchugummala"),

            "Mandasa" to listOf("Select GP", "Mandasa", "Chintada", "Kalingapatnam", "Sirellapalli", "Rallapeta", "Tekkali", "Ganguvada"),

            "Vajrapukotturu" to listOf("Select GP", "Vajrapukotturu", "Rajapuram", "Metturu", "Kothuru", "J.R.Puram", "Manikyapuram", "Goppili"),
            "Nandigam" to listOf("Select GP", "Nandigam", "Killipalem", "Ravivalasa", "Boppadam", "Kaviti", "Konda Jatlapalem", "Rayapadu"),
            "Tekkali" to listOf("Select GP", "Tekkali", "Killipalem", "Gotturu", "Sompeta", "Jinkibhadra", "Ranasthalam"),
            "Santhabommali" to listOf("Select GP", "Santhabommali", "Rajapuram", "Gollalavalasa", "Mandasa", "Nuvvalarevu"),
            "Kotabommali" to listOf("Select GP", "Kotabommali", "Pydibheemavaram", "Ganguvari Sigadam", "Korlam", "Kothapeta"),
            "Saravakota" to listOf("Select GP", "Saravakota", "Narasannapeta", "Chandiputtuga", "Kalingapatnam", "Komanapalli"),
            "Meliaputti" to listOf("Select GP", "Meliaputti", "Temburu", "Tamarapalli", "Seetharampuram", "Sugguru"),
            "Pathapatnam" to listOf("Select GP", "Pathapatnam", "Gotturu", "Alikam", "Patharlapalli", "Dandumuramamidi"),
            "Kothuru" to listOf("Select GP", "Kothuru", "Kothapeta", "Chinnadugam", "Patha Kotapadu", "Kudurupaka"),
            "Hiramandalam" to listOf("Select GP", "Hiramandalam", "Domburu", "Pedapeta", "Chidimi", "Bamini"),
            "Lakshminarasupeta" to listOf("Select GP", "Lakshminarasupeta", "Krishnapuram", "Ponduru", "Rudrakota", "Sitanagaram"),
            "Srikakulam" to listOf("Select GP", "Alikam", "Arasavilli", "Balaga", "Balivada", "Batteru", "Byri", "Byrivanipeta", "Gudem", "Ippili", "Kallepalle", "Karajada", "Lankam", "Lingalavalasa", "Mofusbandar", "Naira", "Pathasrikakulam", "Patrunivalasa", "Peddapadu", "Ponnam", "Ragolu", "Sanivada", "Silagamsingivalasa", "Singupuram", "Thandemvalasa", "Vakalavalasa", "Voppangi"),
            "Srikakulam" to listOf("Select GP", "Alikam", "Balivada", "Batteru", "Byri", "Byrivanipeta", "Gudem", "Ippili", "Kallepalle", "Karajada", "Lankam", "Lingalavalasa", "Mofusbandar", "Naira", "Pathasrikakulam", "Patrunivalasa", "Peddapadu", "Ponnam", "Ragolu", "Sanivada", "Silagamsingivalasa", "Singupuram", "Thandemvalasa", "Vakalavalasa", "Voppangi"),
            "Amadalavalasa" to listOf("Select GP", "Akkivaram", "Akkulapeta", "Anandapuram", "Belamam", "Bobbilipeta", "Chinnajonnavalasa","Chintalapeta", "Chittivalasa", "Dandemvalasa", "Dusi", "Garimellakotha Valasa", "Hanumanthapuram", "Kalivaram", "Kanugulavalasa", "Kata‑(char)yulapeta", "Korapam", "Korlakota", "Kothavalasa", "Marrikothavalasa", "Munagavalasa", "Nelliparthi", "Nimmathorlavada", "Ponnampeta", "Ramachandrapuram", "Santha Kothavalasa", "Srinivasacharyulu Peta", "Sylada", "Thotada", "Turakapeta", "Vanjangi", "Vanjangipeta", "Vedullavalasa"),
            "Ponduru" to listOf("Select GP", "Alamajipeta", "Atchipolavalasa", "Banam", "Bhagavandasupeta", "Boddepalle", "Buridikancharam", "Dallavalasa", "Dallipeta (Donka Palle)", "Dharmapuram", "Gandredu", "Gokarnapalle", "Gorinta", "Kalyanipeta", "Kanimetta", "Kesavadasupuram", "Kinthali", "Kollipeta", "Konchada", "Kotipalle", "Krishnapuram", "Laidam", "Lolugu", "Malakam", "Modallavalasa", "Nandivada", "Narasapuram", "Penubarthi", "Pillalavalasa", "Pullajipeta", "Ramadasupuram", "Rapaka", "Singuru", "Tadivalasa", "Thandyam", "Thanem", "Tholapi", "Venkatarayuni Gudem"),
            "Sarubujjili" to listOf("Select GP", "Amrutalinga Nagaram", "Avatarabad", "Boppadam", "Buddivalasa", "Buridivalasa", "Chapalavalasa", "Chiguruvalasa", "Chinavenkatapuram", "Chinnakakitapalle", "Dakaravalasa", "Gonepadu", "Isakalapalem", "Katakamayyapeta", "Kondavalasa", "Kondraguda", "Kothakota", "Lakshimipuram", "Loddalakakitapalle", "Marripadu", "Matalabpeta", "Moolasowlapuram","Nandikonda", "Palavalasa", "Parvathalapeta", "Peddakakitapalle", "Peddasowlapuram", "Peddavenkatapuram", "Purushottapuram", "Rasulpeta", "Ravivalasa", "Rotta Valasa", "Sarubujjili", "Shalanthri", "Sindhuvada", "Suryanarayanapuram", "Telikipenta", "Thamminaidupeta", "Turakapeta", "Vennelavalasa", "Vijayarampuram", "Yeragam"),
            "Burja" to listOf("Select GP", "Burja", "Bodhupadu", "Dommasivari Kothavalasa", "Dantu", "Gorintapalem", "Jorangivalasa", "Kodurupeta", "Kommadibr"),
            "Narasannapeta" to listOf("Select GP", "Narasannapeta", "Chodavaram", "Nimmada", "Bellamvari Palem", "Thalama Bandalu"),
            "Polaki" to listOf("Select GP", "Polaki","Kallepalli", "Kaviti", "Kokkirakota", "Balabhadra Puram"),
            "Etcherla" to listOf("Select GP", "Etcherla", "Murapaka", "Patha Etcherla", "Palavalasa"),
            "Laveru" to listOf("Select GP", "Laveru","Ranasthalam", "Komanapalli", "Mettapeta"),
            "Ranastalam" to listOf("Select GP", "Ranastalam","Gollapadu", "Jalumuru", "Narasannapeta"),
            "Ganguvarisigadam" to listOf("Select GP", "Ganguvarisigadam","Pydibhimavaram", "Sanivada", "Pathapatnam"),
            "Jalumuru" to listOf("Select GP", "Jalumuru", "Burja", "Chittivalasa", "Singupuram"),
            "Atmakur" to listOf("Select GP", "Aravedu", "Atmakur", "Bandarupalle", "Battepadu", "Botikarlapadu", "Boyila Chiruvella", "Chiruvella Khandrika", "Depuru", "Gandlavedu", "Jangalapalle", "Kanupurupalle", "Karatampadu", "Mahimalur", "Nabbinagaram", "Nagulapadu", "Narampeta", "Nellorepalem", "Nuvvurupadu", "Padakandla", "Pamidipadu", "Vasili"),
            "Nellore Rural" to listOf("Select GP", "Vaviletipadu", "Golla Kandukur"),
            "Nellore Urban" to listOf("Select GP", "Balajinagar", "Stonehousepet"),
            "Kovur" to listOf("Select GP", "Kovur", "Thumallapenta"),
            "Buchireddipalem" to listOf("Select GP", "Thotapalli", "Juvvalapalem"),
            "Indukurpet" to listOf("Select GP", "Mypadu", "Gangapatnam"),
            "Thotapalli Gudur" to listOf("Select GP", "Gudur", "Kolluru"),
            "Muthukur" to listOf("Select GP", "Krishnapatnam", "Nelaturu"),
            "Venkatachalam" to listOf("Select GP", "Venkatachalam", "Gopalapatnam"),
            "Manubolu" to listOf("Select GP", "Manubolu", "Inamadugu"),
            "Podalakur" to listOf("Select GP", "Podalakur", "Utukur"),
            "Rapur" to listOf("Select GP", "Rapur", "Venkatapuram"),
            "Sydapuram" to listOf("Select GP", "Sydapuram", "Nellatur"),
            "Atmakur" to listOf("Select GP", "Atmakur", "Kollur"),
            "Ananthasagaram" to listOf("Select GP", "Ananthasagaram", "Mamidipalli"),
            "Anumasamudrampeta" to listOf("Select GP", "Anumasamudrampeta", "Yerraballi"),
            "Chejerla" to listOf("Select GP", "Chejerla", "Thallapalli"),
            "Kaluvoya" to listOf("Select GP", "Kaluvoya", "Chavatapalem"),
            "Marripadu" to listOf("Select GP", "Marripadu", "Alliguntapadu"),
            "Sangam" to listOf("Select GP", "Sangam", "Peddapavani"),
            "Seetharamapuram" to listOf("Select GP", "Seetharamapuram", "Balayapalli"),
            "Udayagiri" to listOf("Select GP", "Udayagiri", "Padakandla"),
            "Kavali" to listOf("Select GP", "Kavali", "Rudrakota"),
            "Allur" to listOf("Select GP", "Allur", "Chennuru"),
            "Bogole" to listOf("Select GP", "Bogole", "Zuvvaladinne"),
            "Dagadarthi" to listOf("Select GP", "Dagadarthi", "Manneti Palli"),
            "Duttalur" to listOf("Select GP", "Duttalur", "Chakalakonda"),
            "Jaladanki" to listOf("Select GP", "Jaladanki", "Gummuluru"),
            "Kaligiri" to listOf("Select GP", "Kaligiri", "Kondayapalem"),
            "Kodavalur" to listOf("Select GP", "Kodavalur", "Perumallapadu"),
            "Vidavalur" to listOf("Select GP", "Vidavalur", "Nagulavellatur"),
            "Vinjamur" to listOf("Select GP", "Vinjamur", "Ramayapalem"),
            "Kandukur" to listOf("Select GP", "Kandukur", "Chimakurthy"),
            "Gudluru" to listOf("Select GP", "Gudluru", "Krishnapuram"),
            "Lingasamudram" to listOf("Select GP", "Lingasamudram", "Vikkiralapeta"),
            "Pamuru" to listOf("Select GP", "Pamuru", "Tettu"),
            "Pedacherlopalli" to listOf("Select GP", "Pedacherlopalli", "Kuppagunta"),
            "Ulavapadu" to listOf("Select GP", "Ulavapadu", "Kaligiri"),
            "Voletivaripalem" to listOf("Select GP", "Voletivaripalem", "Chimidipudi"),
//.............
            "Parvathipuram" to listOf("Select GP", "Parvathipuram", "Narsipuram", "Buduruvada", "Lakshminarayanapuram"),
            "Salur" to listOf("Select GP", "Salur", "Turpu Salur", "Then Salur", "Narsipatnam"),
            "Komarada" to listOf("Select GP", "Komarada", "Purnapadu", "Mogaltor","Jalapati"),
            "Palakonda" to listOf("Select GP", "Palakonda", "G. L. Puram", "Mogilicherla", "Nallamalla"),
            "Gummalakshmipuram" to listOf("Select GP", "Gummalakshmipuram", "Ramabhadrapuram", "Jodugulli"),
            "Kurupam" to listOf("Select GP", "Kurupam", "Pachanagada", "Jalabujili"),
            "Jiyyammavalasa" to listOf("Select GP", "Jiyyammavalasa", "Kothapeta", "Sombuvari Palli"),
            "Seethampeta" to listOf("Select GP", "Seethampeta", "Pachagonda", "Chinthapalli"),
//...............................
            "Bathalapalle" to listOf("Select GP", "Apparacheruvu", "Bathalapalle", "Chennarayapatnam", "D Cherlopalle", "Dampetla", "Edula Mustur", "Gariselapalle", "Malyavantham", "Obulapuram", "Raghavampalle", "Sangala"),
            "Chennekothapalle" to listOf("Select GP", "Chennekothapalle"),
            "Dharmavaram" to listOf("Select GP", "Buddareddipalle", "Chigicherla", "Darsimala", "Elukuntla", "Gotlur", "Kunuthuru", "Mallakalva", "Nelakota", "Pothulanagepalle", "Ravulacheruvu", "Regatipalle", "Subbaraopeta", "Thummala"),
            "Ramagiri" to listOf("Select GP", "Ramagiri", "Polepalli", "Kuntimaddi", "Ganthemarri", "Nasanakota", "MotarchinthaPalli", "Kondapuram", "Peruru", "Dubbarlapalli"),
            "Tadimarri" to listOf("Select GP", "Tadimarri"    ),
            "Kadiri" to listOf("Select GP", "Alampur", "Bathalapalle", "Chalamakuntapalle", "Chippalamadugu", "Kadiri", "Kadiri Brahmanapalle", "Kadirikuntlapalle", "Kalasamudram", "Kondamanayanipalem", "Kowlepalle", "Motukapalle", "Muthyalacheruvu", "Pandulakunta", "Patnam", "Yeguvapalle"),
            "Gandlapenta" to listOf("Select GP", "Chamachenubylu", "Chamalagondi", "Gandlapenta", "Godduvelagala", "Jeenulakunta", "Kamathampalle", "Kurumamidi", "Maddivarigondi", "Malameedapalle", "Somayajulapalle", "Thummalabylu", "Veparala"),
            "Amadagur" to listOf("Select GP", "Amadagur"),
            "Gandlapenta" to listOf("Select GP", "Gandlapenta", "Chamachenubylu", "Chamalagondi", "Godduvelagala", "Jeenulakunta", "Kamathampalle", "Kurumamidi","Maddivarigondi", "Malameedapalle", "Somayajulapalle", "Thummalabylu", "Veparala"),
            "Kadiri" to listOf("Select GP", "Alampur", "Bathalapalle", "Chalamakuntapalle", "Chippalamadugu", "Kadiri", "Kadiri Brahmanapalle", "Kadirikuntlapalle", "Kalasamudram", "Kondamanayunipalem", "Kowlepalle", "Motukapalle", "Muthyalacheruvu", "Pandulakunta", "Patnam", "Yeguvapalle"),
            "Nallacheruvu" to listOf("Select GP", "Allugundu", "Jogannapeta", "Kadiri Pulakunta", "Maddimadugu", "Nallacheruvu", "Oravoy", "Panthulacheruvu", "Mulakalapalle", "Talamarlavandlapalli", "Tavalamarri", "Ubicherla"),
            "Nambulapulakunta" to listOf("Select GP", "Nambulapulakunta"),
            "Talupula" to listOf("Select GP", "Bandlapalli", "Batrepalli", "Eedulakuntlapalli", "Gollapalli Thanda", "Gunduvaripalli", "Kurli", "Lakkasamudram", "Nuthanakalva", "Obulareddipalli", "Odulapalli", "Peddannavaripalli", "Penabadivandlapalli", "Polthalavaripalli", "Puligundlapalli", "Talupula (N)", "Udumulakurty", "Vepamanipeta"),
            "Tanakal" to listOf("Select GP", "Agraharampalle", "T Sadum", "Balasamudram", "Bonthalapalle", "Cheekatimani Palle", "Chinnaramannagaripalle", "Danduvaripalle", "Diguvamandalapalle", "Ethodu", "Gurrambailu", "Kokkanti", "Korthikota", "Kotapalle", "Maddinayanipalem", "Malreddipalle", "Mundlavaripalle", "Tanakal", "Tavalam D Thottipalli", "Ulavalavandlapalle", "Venkatrayanipalle"),
            "Agali" to listOf("Select GP", "Agali", "Gollahalli", "Pulikuntla", "Kuduragundi", "Kuntimaddi"),
            "Amarapuram" to listOf("Select GP", "Amarapuram", "Mallinayanapalli", "Gollahalli", "Maravapalli", "Kambagiri Palli"),
            "Chilamathur" to listOf("Select GP", "Chilamathur", "Naginayanicheruvu", "Kadamalakunta", "Nandiganahalli", "Beerepalli"),
            "Gudibanda" to listOf("Select GP", "Gudibanda", "Malugur", "Kothapalli", "Obuladevaracheruvu", "Dodagatta"),
            "Hindupur" to listOf("Select GP", "Hindupur Rural", "Devanakonda", "Cherlopalli", "Lakshmampalli", "Basavanapalli"),
            "Lepakshi" to listOf("Select GP", "Lepakshi", "Kodikonda", "Basavanapalli", "Demakethepalli", "Hulikuntla"),
            "Madakasira" to listOf("Select GP", "Madakasira", "Kodigenahalli", "Venkatampalli", "Chikkabanavara", "Kothacheruvu"),
            "Parigi" to listOf("Select GP", "Parigi", "Chennarayapatna", "Thirumaladevarapalli", "Gollapuram", "Devanakonda"),
            "Penukonda" to listOf("Select GP", "Penukonda", "Doddakunta", "Basavanapalli", "Kappalabanda", "Kondapura"),
            "Roddam" to listOf("Select GP", "Roddam", "Talamarla", "Chinnamallepalli", "Kuntimaddi", "Chinnaobinenipalli"),
            "Rolla" to listOf("Select GP", "Rolla", "Gorantla", "Chelimilla", "Somaghatta", "Chinnamanthur"),
            "Somandepalle" to listOf("Select GP", "Somandepalle", "Veerapuram", "Kothacheruvu", "Mudivedu", "Kakkalapalli"),
            "Bukkapatnam" to listOf("Select GP", "Bukkapatnam", "Akkaram", "Kundurpi", "Peddachintakunta", "Chinnamandem"),
            "Gorantla" to listOf("Select GP", "Gorantla", "Somaghatta", "Gollapuram", "Bodisanipalli", "Malakavemula"),
            "Kothacheruvu" to listOf("Select GP", "Kothacheruvu", "Venkatampalli", "Bhagyanagar", "Rachuru", "Dasarapalli"),
            "Nallamada" to listOf("Select GP", "Nallamada", "Thumukunta", "Yerragudi", "Velpanuru", "Kandlapalli"),
            "Obuladevaracheruvu" to listOf("Select GP", "Obuladevaracheruvu", "Yerraguntapalli", "Jilledubandalapalli", "Chinaganipalli", "Rachuru"),
            "Puttaparthi" to listOf("Select GP", "Puttaparthi", "Gollapalli", "Kothacheruvu Rural", "Kodapaganipalli", "Chellagurki"),
            "Chandragiri" to listOf("Select GP", "Agarala", "Arepalle", "Bheemavaram", "Chandragiri", "Chintagunta", "Dornakambala", "Ithepalle", "Kalroadpalle", "Kotala", "Mamandur", "Mittapalem", "Mungilipattukothapalle", "Nagapatla", "Narasingapuram", "Panapakam", "Pullaiahgaripalle", "Ramireddipalle", "Reddivaripalle", "Sanambatla", "Seshapuram", "Thondawada"),
            "Chinnagottigallu" to listOf("Select GP", "Bhakarapet", "Chinnagottigallu", "Chittecherla", "Chattevaripalem", "Devarakonda", "Diguvuru", "Kotabylu", "RangannagariGadda", "Thippireddigaripalle", "Yeguvuru"),
            "Pakala" to listOf("Select GP", "Pakala" ),
            "Puttur" to listOf("Select GP", "Puttur"),
            "Ramachandrapuram" to listOf("Select GP", "Ramachandrapuram"),
            "Tirupati Rural" to listOf("Select GP", "Avilala", "Brahmana Pattu", "Cherlopalle", "Chiguruwada North", "Chiguruwada South", "Daminedu", "Durga Samudram", "Gollapalle", "Kalur", "Kothur", "Kuntrapakam", "KupuChandrapeta", "MallamGunta", "Mallavaram", "Mundlapudi", "Nallamani Kalva", "Padi", "PaidiPalle", "Panakam", "PathaKalva", "Perur", "Pudipatla", "RamanujaPalle", "Thanapalle", "ThummalaGunta", "Tiruchanur", "Vedanthapuram", "Vemur", "Yogimallavarum"),
            "Tirupati Urban" to listOf("Select GP", "Akkarampalle", "Chennayyagunta", "Konkachennaiahgunta", "Mangalam", "Settipalle", "Timminaidupalle", "Tirumala", "Tirupati MC"),
            "Vadamalapeta" to listOf("Select GP", "Vadamalapeta"),
            "Yerravaripalem" to listOf("Select GP", "Yerravaripalem"),

            "Anandapuram" to listOf("Select GP", "Anandapuram", "Bakurupalem", "Boni", "Chandaka", "Dabbanda", "Gambheeram", "Gandigundam", "Gidijala", "Gorinta", "Gottipalle", "Palavalasa", "Pandalapaka", "Peddipalem", "Sontyam", "Vellanki", "Vemulavalasa"),
            "Bheemunipatnam" to listOf("Select GP", "Bheemunipatnam", "Kapuluppada", "Chippada", "Chepaluppada", "Dakamarri", "Majjivalasa", "Mulakuddu", "Sanganivalasa", "Tallavalasa", "Tatituru", "Chittivalasa", "Nerallavalasa"),
            "Visakhapatnam (Rural)" to listOf("Select GP", "Madhurawada", "Kommadi", "Rushikonda", "Yendada", "Simhachalam", "Pothinamallayyapalem", "Paradesipalem", "Adivivaram", "Bakkannapalem"),
            "Seethammadhara" to listOf("Select GP", "Seethammadhara", "HB Colony", "MVP Colony", "Sivajipalem", "Venkojipalem", "Muralinagar", "Lalithanagar", "Satyam Junction", "KRM Colony", "Balayya Sastri Layout", "Sampath Vinayaka Temple Area", "East Point Colony", "Dwaraka Nagar (North)", "Lawsons Bay"),
            "Padmanabham" to listOf("Select GP", "Ananthavaram", "Ayinada", "Bapirajutallavalasa", "Bhandevupuram", "Korada", "Kovvada", "Krishnapuram", "Maddi", "Pandrangi", "Potnuru", "Revidi", "Reddipalle Agraharam", "Padmanabham"),
            "Gajuwaka" to listOf("Select GP", "Gajuwaka", "Akkireddypalem", "Kurmannapalem", "Aganampudi", "Auto Nagar", "Sheela Nagar", "Duvvada"),
            "Pedagantyada" to listOf("Select GP", "Pedagantyada", "Scindia", "Sriharipuram", "Old Gajuwaka", "NAD Kotha Road", "Vepagunta"),
            "Gopalapatnam" to listOf("Select GP", "Gopalapatnam", "Simhachalam", "Mindi", "Lankelapalem", "Kancharapalem (partial)", "Sabbavaram Border Area"),
            "Mulagada" to listOf("Select GP", "Mulagada", "Malkapuram", "Sriharipuram (partial)", "Port Area", "Container Depot Area", "Saligramapuram"),
            "Maharanipeta" to listOf("Select GP", "Maharanipeta", "Jagadamba Junction", "One Town", "Poorna Market", "Turner's Choultry", "Allipuram"),
            "Pendurthi" to listOf("Select GP", "Pendurthi", "Sabbavaram (partial)", "Sujatha Nagar", "Gidijala", "Chinamushidiwada", "Narava", "Sontyam"),
            "Parvathipuram" to listOf("Select GP", "Parvathipuram", "Rompalli", "Gandhinagaram", "Gavarapeta", "Kondawada", "Tadiparthi"),
            "Balijipeta" to listOf("Select GP", "Balijipeta", "Kondakarakam", "Gangannapadu", "Marrivalasa", "Pedamanapuram", "Kudrevada"),
            "Salur" to listOf("Select GP", "Salur", "Thotapalle", "Kothavalasa", "Rajayyapeta", "Thadikonda", "Boddagandi"),
            "Seethanagaram" to listOf("Select GP", "Seethanagaram", "Goppili", "Tamarapalli", "Chappidipalle", "Boddavalasa", "Chinamerangi"),
            "Pachipenta" to listOf("Select GP", "Pachipenta", "Thota", "Kondaboru", "Rajupeta", "Boddivalasa", "Chintalavalasa"),
            "Makkuva" to listOf("Select GP", "Makkuva", "Buradapalli", "Nellipaka", "Kotipam", "Gunjala", "Kintada"),
            "Garugubilli" to listOf("Select GP", "Garugubilli", "Lakshmipuram", "Kondareddi", "Palavalasa", "Muddada", "Mulakalapalli"),
            "Komarada" to listOf("Select GP", "Komarada", "Kondavalsa", "Kuneru", "Puliputti", "Bhimavaram", "Pusalapadu"),
            "Kurupam" to listOf("Select GP", "Kurupam", "Cheruvupalli", "Thurupuvalasa", "Lakkidam", "Kothavalasa", "Gumada"),
            "Gummalakshmipuram" to listOf("Select GP", "Gummalakshmipuram", "Ukkamba", "Chintalapadu", "Dummangi", "Peddapadu", "Pachipenta (partial)"),
            "Vizianagaram" to listOf("Select GP", "Vizianagaram Rural", "Kothapeta", "Chintalavalasa", "Ravivalasa", "Kothavalasa", "Pedamajjipalem"),
            "Denkada" to listOf("Select GP", "Denkada", "Kondagandredu", "Akkivaram", "Rega", "Duppalavalasa", "Kothapalle"),
            "Bondapalli" to listOf("Select GP", "Bondapalli", "Thimmapuram", "Gantyada", "Gudivada", "Marrivalasa", "Peddavara"),
            "Gantyada" to listOf("Select GP", "Gantyada", "Kondakarakam", "Gunkalam", "Kondaguddi", "Bheemadevarpalle", "Pathikayavalasa"),
            "Jami" to listOf("Select GP", "Jami", "Kondavelagada", "Bhogapuram (partial)", "Mamidipalli", "Brahmanatarla", "Puliparti"),
            "Garividi" to listOf("Select GP", "Garividi", "Kothavalasa", "Jagannadhapuram", "Bhupatipalem", "Peddipalem", "Neelavathi"),
            "Pusapatirega" to listOf("Select GP", "Pusapatirega", "Boddapeta", "Chelluru", "Srirampuram", "Kallepalli", "Dibbalapalem"),
            "Nellimarla" to listOf("Select GP", "Nellimarla", "Jonnada", "Gurla", "Alajangi", "Mokhasa Kalavalapalli", "Ravada"),
            "Dattirajeru" to listOf("Select GP", "Dattirajeru", "Jaddetivalasa", "Maradam", "Ramatheertham", "Peddabrahmadevam", "Tadipudi"),
            "Gajapathinagaram" to listOf("Select GP", "Gajapathinagaram", "Thotapalle", "Gollapalli", "Vangara", "Veerabhadrapuram", "Sirivada"),
            "Bobbili" to listOf("Select GP", "Bobbili Rural", "Rajupeta", "Nellimarla (partial)", "Kanapaka", "Thatipudi", "Kondaboru"),
            "Ramabhadrapuram" to listOf("Select GP", "Ramabhadrapuram", "Kandipudi", "Ravivalasa", "Komatipalli", "Lakshmipuram", "Mulakalapalli"),
            "Badangi" to listOf("Select GP", "Badangi", "Chollapadam", "Pachipenta (partial)", "Kandipudi", "Pedduru", "Pangidi"),
            "Therlam" to listOf("Select GP", "Therlam", "Pedduru", "Gantyada (partial)", "Srirampuram", "Rompalli", "Gajapathinagaram (partial)"),
            "Cheepurupalli" to listOf("Select GP", "Cheepurupalli", "Seethanagaram (partial)", "Gorlapeta", "Akkivaram", "Patharevu", "Mutcherla"),
            "Merakamudidam" to listOf("Select GP", "Merakamudidam", "Pedduru", "Padmapuram", "Duppalavalasa", "Nandigam", "Bheemavaram"),
            "Eluru" to listOf("Select GP", "Sanivarapupeta", "Tangellamudi", "Powerpet", "Ashok Nagar", "Lakshavarapu", "Venkatapuram"),
            "Denduluru" to listOf("Select GP", "Denduluru", "Chodimella", "Uppugudem", "Kolupudi", "Kothuru", "Yadavole"),
            "Pedapadu" to listOf("Select GP", "Pedapadu", "Chilukuru", "Kongapadu", "Kumudavalli", "Bhimavaram (rural)", "Maddurupadu"),
            "Pedavegi" to listOf("Select GP", "Pedavegi", "Gopalapuram", "Kovvali", "Moparru", "Chintalapudi", "Kothuru"),
            "Nuzvid" to listOf("Select GP", "Nuzvid Rural", "Vadlamannadu", "Mustabada", "Thotlavalluru", "Ramannapeta", "Lingala"),
            "Musunuru" to listOf("Select GP", "Musunuru", "Kokkiligadda", "Kuntamukkala", "Kokkireni", "Maredumilli", "Singanapalli"),
            "Bhimadole" to listOf("Select GP", "Bhimadole", "Pragadavaram", "Velagaleru", "Ramachandrapuram", "Kethavaram"),
            "Kaikalur" to listOf("Select GP", "Kaikalur", "Mandavalli", "Rayakurru", "Paleru", "Guntupalli", "Kalakurru"),
            "Jangareddygudem" to listOf("Select GP", "Jangareddygudem", "Timmannapalem", "Rangapuram", "Mallavaram", "Chodavaram"),
            "Tadepalligudem" to listOf("Select GP", "Tadepalligudem Rural", "Pentapadu", "Chodimella", "Togummi", "Gopannapalem"),
            "Jeelugumilli" to listOf("Select GP", "Jeelugumilli", "Ankampalem", "Patha Koderu", "Tadepalle", "Rajavaram"),
            "Buttayagudem" to listOf("Select GP", "Buttayagudem", "Bayyannapeta", "Korukonda", "Marampalli", "Saripalle"),
            "Polavaram" to listOf("Select GP", "Polavaram", "Bodugudem", "Jalipudi", "Yerrampeta", "Dondapudi"),
            "Kamavarapukota" to listOf("Select GP", "Kamavarapukota", "Devarapalle", "Venkatapuram", "Pusuguppa", "Madduru"),
            "Narasapuram" to listOf("Select GP", "Narasapuram", "Gudur", "Chintaparru", "Somaraju", "Kothota", "Kothapeta"),
            "Palakoderu" to listOf("Select GP", "Palakoderu", "Rayakuduru", "Kothapeta", "Vedurupaka", "Chilakuru"),
            "Penumantra" to listOf("Select GP", "Penumantra", "Poduru", "Ankalammagudem", "Pasupugudem", "Gudapalli"),
            "Mogalthur" to listOf("Select GP", "Mogalthur", "Thallarevu", "Yelamanchili", "Kothota", "Vadapalli"),
            "Achanta" to listOf("Select GP", "Achanta", "Sompalle", "Chintapalli", "Kalingapalem", "Kalidindi"),
            "Undi" to listOf("Select GP", "Undi", "Lakshmipuram", "Kodamanchili", "Velivennu", "Koderu"),
            "Veeravasaram" to listOf("Select GP", "Veeravasaram", "Nidamarru", "Koyyalagudem", "Mandavalli", "Panapaka"),
            "Kadapa" to listOf("Select GP", "Kadapa Rural", "Rajupalem", "Kakateeya Nagar", "Ravindra Nagar", "Ramanjaneyapuram"),
            "Chennur" to listOf("Select GP", "Chennur", "Kasanur", "Thippireddypalle", "Ananthapuram", "Gurramkonda"),
            "Vallur" to listOf("Select GP", "Vallur", "Brahmanapalle", "Mylavaram", "Thirumalayapalle", "Lingampalle"),
            "Sidhout" to listOf("Select GP", "Sidhout", "Madduru", "Lakkireddypalle", "Ayyavandlapalle", "Duvvur"),
            "Chinthakommadinne" to listOf("Select GP", "Chinthakommadinne", "Thondur", "Ravindra Nagar", "Nagasanipalle"),
            "Kamalapuram" to listOf("Select GP", "Kamalapuram", "Duvvur", "Cherlopalle", "Veerapunayunipalle", "Patur"),
            "Pendlimarri" to listOf("Select GP", "Pendlimarri", "Gandluru", "Karempudi", "Devapatla", "Gandlapenta"),
            "Jammalamadugu" to listOf("Select GP", "Jammalamadugu", "Somapuram", "Vemula", "Yerraguntla", "Brahmanapalle"),
            "Muddanur" to listOf("Select GP", "Muddanur", "Mylavaram", "Pulivendula (partial)", "Togurupeta", "Pathapalle"),
            "Proddatur" to listOf("Select GP", "Proddatur Rural", "Yerraguntla", "Y.S.R. Colony", "Papireddypalle", "Gandhinagar"),
            "Duvvur" to listOf("Select GP", "Duvvur", "Pathur", "Cherlopalle", "Gundlapalle", "Chennur (partial)"),
            "Mylavaram" to listOf("Select GP", "Mylavaram", "Lingampalle", "Siddapuram", "Tondur", "Kothapalle"),
            "Rajupalem" to listOf("Select GP", "Rajupalem", "Jammichettupalle", "Kondapuram", "Nagasanipalle"),
            "Badvel" to listOf("Select GP", "Badvel", "Nallacheruvu", "Nandalur", "Korlakunta", "Nagasanipalle"),
            "B.Kodur" to listOf("Select GP", "B. Kodur", "Kothapalle", "Chitvel", "Kamachinnayunipeta", "Peddagudipadu"),
            "Porumamilla" to listOf("Select GP", "Porumamilla", "Pullareddypeta", "Utukur", "Kamalapuram", "Kistampeta"),
            "Kalasapadu" to listOf("Select GP", "Kalasapadu", "Peddachellur", "Ambavaram", "Chinnakomerla", "Brahmamgari Matham"),
            "Gopavaram" to listOf("Select GP", "Gopavaram", "Veerapunayanipalle", "Chinnakudali", "Pydipalem", "Ganganapalle"),
            "Mydukur" to listOf("Select GP", "Mydukur", "Obulampalle", "Pothireddypalle", "Thallapalle", "Gundlamachupalli"),
            "Pulivendula" to listOf("Select GP", "Pulivendula", "Gangireddypalle", "Kondapuram", "Somapuram", "Rameswaram"),
            "Vemula" to listOf("Select GP", "Vemula", "Mallela", "Devagudi", "Thippaguntapalle", "Peddamudiyam"),
            "Vempalle" to listOf("Select GP", "Vempalle", "Gandlapenta", "Ravindra Nagar", "Kappatralla", "Bochireddypalle"),
            "Lingala" to listOf("Select GP", "Lingala", "Yerraguntla", "Padamatipalle", "Veeraballi", "Chinamerangi"),
            "Simhadripuram" to listOf("Select GP", "Simhadripuram", "Chinnamachupalli", "Jangampalli", "Velugodu", "Mogilipadu"),
            "Thondur" to listOf("Select GP", "Thondur", "Rajupalem", "Pathakandriga", "Kalakada", "Tholagudur"),

        )

        spinnerState.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sampleStates)
        spinnerDistrict.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sampleDistricts)

        spinnerDivision.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listOf("Select Division"))
        spinnerMandal.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listOf("Select Mandal"))
        spinnerGP.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listOf("Select GP"))

        spinnerDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedDistrict = spinnerDistrict.selectedItem.toString()
                val divisions = districtToDivisionsMap[selectedDistrict] ?: listOf("Select Division")
                spinnerDivision.adapter = ArrayAdapter(this@DoctorHomeActivity, android.R.layout.simple_spinner_dropdown_item, divisions)
                spinnerMandal.adapter = ArrayAdapter(this@DoctorHomeActivity, android.R.layout.simple_spinner_dropdown_item, listOf("Select Mandal"))
                spinnerGP.adapter = ArrayAdapter(this@DoctorHomeActivity, android.R.layout.simple_spinner_dropdown_item, listOf("Select GP"))
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerDivision.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedDivision = spinnerDivision.selectedItem.toString()
                val mandals = divisionToMandalsMap[selectedDivision] ?: listOf("Select Mandal")
                spinnerMandal.adapter = ArrayAdapter(this@DoctorHomeActivity, android.R.layout.simple_spinner_dropdown_item, mandals)
                spinnerGP.adapter = ArrayAdapter(this@DoctorHomeActivity, android.R.layout.simple_spinner_dropdown_item, listOf("Select GP"))
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerMandal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedMandal = spinnerMandal.selectedItem.toString()
                val gps = mandalToGPsMap[selectedMandal] ?: listOf("Select GP")
                spinnerGP.adapter = ArrayAdapter(this@DoctorHomeActivity, android.R.layout.simple_spinner_dropdown_item, gps)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}



//        spinnerState.adapter =
//            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sampleStates)
//        spinnerDistrict.adapter =
//            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sampleDistricts)
//
//        spinnerDivision.adapter =
//            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listOf("Select Division"))
//        spinnerMandal.adapter =
//            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listOf("Select Mandal"))
//        spinnerGP.adapter =
//            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listOf("Select GP"))
//
//        // DISTRICT → DIVISION
//        spinnerDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val selectedDistrict = spinnerDistrict.selectedItem.toString()
//                val divisions = districtToDivisionsMap[selectedDistrict] ?: listOf("Select Division")
//                spinnerDivision.adapter =
//                    ArrayAdapter(this@DoctorHomeActivity, android.R.layout.simple_spinner_dropdown_item, divisions)
//
//                // Reset lower levels
//                spinnerMandal.adapter =
//                    ArrayAdapter(this@DoctorHomeActivity, android.R.layout.simple_spinner_dropdown_item, listOf("Select Mandal"))
//                spinnerGP.adapter =
//                    ArrayAdapter(this@DoctorHomeActivity, android.R.layout.simple_spinner_dropdown_item, listOf("Select GP"))
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        }
//
//        // DIVISION → MANDAL
//        spinnerDivision.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val selectedDivision = spinnerDivision.selectedItem.toString()
//                val mandals = divisionToMandalsMap[selectedDivision] ?: listOf("Select Mandal")
//                spinnerMandal.adapter =
//                    ArrayAdapter(this@DoctorHomeActivity, android.R.layout.simple_spinner_dropdown_item, mandals)
//
//                // Reset GP
//                spinnerGP.adapter =
//                    ArrayAdapter(this@DoctorHomeActivity, android.R.layout.simple_spinner_dropdown_item, listOf("Select GP"))
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        }
//
//        // MANDAL → GP
//        spinnerMandal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val selectedMandal = spinnerMandal.selectedItem.toString()
//                val gps = mandalToGPsMap[selectedMandal] ?: listOf("Select GP")
//                spinnerGP.adapter =
//                    ArrayAdapter(this@DoctorHomeActivity, android.R.layout.simple_spinner_dropdown_item, gps)
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        }
//    }
//}
//














//package com.saveetha.smarthealthcareapp
//
//import android.os.Bundle
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//
//class DoctorHomeActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_doctor_home)
//
//        val userId = intent.getStringExtra("user_id") ?: "Unknown"
//        val welcomeText = findViewById<TextView>(R.id.tvWelcome)
//        welcomeText.text = "Welcome, Doctor! Your ID: $userId"
//    }
//}


//class DoctorHomeActivity : AppCompatActivity() {
//
//    private lateinit var spinnerState: Spinner
//    private lateinit var spinnerDistrict: Spinner
//    private lateinit var spinnerBlock: Spinner
//    private lateinit var spinnerGP: Spinner
//    private lateinit var saveButton: LinearLayout
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_doctor_home)
//
//        spinnerState = findViewById(R.id.spinnerState)
//        spinnerDistrict = findViewById(R.id.spinnerDistrict)
//        spinnerBlock = findViewById(R.id.spinnerBlock)
//        spinnerGP = findViewById(R.id.spinnerGP)
//        saveButton = findViewById(R.id.saveButton)
//
//        setupSpinners()
//
//        saveButton.setOnClickListener {
//            val selectedState = spinnerState.selectedItem.toString()
//            val selectedDistrict = spinnerDistrict.selectedItem.toString()
//            val selectedBlock = spinnerBlock.selectedItem.toString()
//            val selectedGP = spinnerGP.selectedItem.toString()
//
//            Toast.makeText(
//                this,
//                "Selected:\nState: $selectedState\nDistrict: $selectedDistrict\nBlock: $selectedBlock\nGP: $selectedGP",
//                Toast.LENGTH_LONG
//            ).show()
//
//            // You can proceed to save or send this data to server
//        }
//    }
//
////    private fun setupSpinners() {
////        val sampleStates = listOf("Select State", "Andhra Pradesh")
////        val sampleDistricts = listOf("Select District", "Alluri Sitharama Raju", "Anakapalli", "Ananthapuramu", "Annamayya", "Bapatla", "Chittoor", "Dr. B.R. Ambedkar Konaseema", "East Godavari", "Eluru", "Guntur", "Kakinada", "Krishna", "Kurnool", "Nandyal", "Ntr", "Palnadu", "Parvathipuram Manyam", "Prakasam", "Srikakulam", "Sri Potti Sriramulu Nellore", "Sri Sathya Sai", "Tirupati", "Visakhapatnam", "Vizianagaram", "West Godavari", "Y.S.R. Kadapa")
////        val sampleBlocks = listOf("Select Block", "Block A", "Block B", "Block C")
////        val sampleGPs = listOf("Select GP", "GP 1", "GP 2", "GP 3")
////
////        spinnerState.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sampleStates)
////        spinnerDistrict.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sampleDistricts)
////        spinnerBlock.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sampleBlocks)
////        spinnerGP.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sampleGPs)
////    }
////}
//
//
//private fun setupSpinners() {
//    val sampleStates = listOf("Select State", "Andhra Pradesh")
//    val sampleDistricts = listOf("Select District", "Alluri Sitharama Raju", "Anakapalli", "Ananthapuramu", "Annamayya", "Bapatla", "Chittoor", "Dr. B.R. Ambedkar Konaseema", "East Godavari", "Eluru", "Guntur", "Kakinada", "Krishna", "Kurnool", "Nandyal", "Ntr", "Palnadu", "Parvathipuram Manyam", "Prakasam", "Srikakulam", "Sri Potti Sriramulu Nellore", "Sri Sathya Sai", "Tirupati", "Visakhapatnam", "Vizianagaram", "West Godavari", "Y.S.R. Kadapa")
//
//    val districtToBlocksMap = mapOf(
//        "Anakapalli" to listOf("Select Block", "Atchutapuram", "Chodavaram", "Makavarapalem"),
//        "Guntur" to listOf("Select Block", "Amaravathi", "Mangalagiri", "Ponnur"),
//        "Chittoor" to listOf("Select Block", "Bangarupalem", "Kuppam", "Palamaner")
//    )
//
//    val sampleGPs = listOf("Select GP", "GP 1", "GP 2", "GP 3")
//
//    spinnerState.adapter =
//        ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sampleStates)
//    spinnerDistrict.adapter =
//        ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sampleDistricts)
//    spinnerGP.adapter =
//        ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sampleGPs)
//
//    // Set listener to update blocks dynamically
//    spinnerDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//        override fun onItemSelected(
//            parent: AdapterView<*>?,
//            view: View?,
//            position: Int,
//            id: Long
//        ) {
//            val selectedDistrict = spinnerDistrict.selectedItem.toString()
//            val blocks = districtToBlocksMap[selectedDistrict] ?: listOf("Select Block")
//            val blockAdapter = ArrayAdapter(
//                this@DoctorHomeActivity,
//                android.R.layout.simple_spinner_dropdown_item,
//                blocks
//            )
//            spinnerBlock.adapter = blockAdapter
//        }
//
//        override fun onNothingSelected(parent: AdapterView<*>?) {
//            // Do nothing
//        }
//    }
//}
//}

