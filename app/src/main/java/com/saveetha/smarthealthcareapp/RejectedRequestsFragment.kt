package com.saveetha.smarthealthcareapp.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.saveetha.smarthealthcareapp.R
import com.saveetha.smarthealthcareapp.adapter.HospitalRequestAdapter
import com.saveetha.smarthealthcareapp.model.HospitalRequest

class RejectedRequestsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HospitalRequestAdapter
    private val requestList = mutableListOf<HospitalRequest>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.hospitals_fragment, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        adapter = HospitalRequestAdapter(requestList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        loadRequests("Rejected")
        return view
    }

    private fun loadRequests(status: String) {
        val url = "http://192.168.24.116/smart_healthcare_app/update_request_status.php?status=$status"
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                requestList.clear()
                val dataArray = response.optJSONArray("data")
                if (dataArray != null) {
                    for (i in 0 until dataArray.length()) {
                        val obj = dataArray.getJSONObject(i)
                        requestList.add(HospitalRequest.fromJson(obj))
                    }
                    adapter.notifyDataSetChanged()
                }
            },
            { error ->
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
        Volley.newRequestQueue(requireContext()).add(request)
    }
}
