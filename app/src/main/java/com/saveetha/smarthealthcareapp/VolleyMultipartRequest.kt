package com.saveetha.smarthealthcareapp

import com.android.volley.AuthFailureError
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException

open class VolleyMultipartRequest(
    method: Int,
    url: String,
    private val mListener: Response.Listener<NetworkResponse>,
    private val mErrorListener: Response.ErrorListener
) : Request<NetworkResponse>(method, url, mErrorListener) {

    private val twoHyphens = "--"
    private val lineEnd = "\r\n"
    private val boundary = "apiclient-" + System.currentTimeMillis()

    override fun getHeaders(): MutableMap<String, String> {
        return HashMap() // add custom headers here if needed
    }

    @Throws(AuthFailureError::class)
    override fun getBodyContentType(): String {
        return "multipart/form-data;boundary=$boundary"
    }

    @Throws(AuthFailureError::class)
    override fun getBody(): ByteArray {
        val bos = ByteArrayOutputStream()
        val dos = DataOutputStream(bos)

        try {
            // Text parameters
            val params = params
            if (params != null && params.isNotEmpty()) {
                for ((key, value) in params) {
                    buildTextPart(dos, key, value)
                }
            }

            // Data parameters (files)
            val data = getByteData()
            if (data != null && data.isNotEmpty()) {
                for ((key, dataPart) in data) {
                    buildFilePart(dos, dataPart, key)
                }
            }

            // End of multipart/form-data.
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd)

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bos.toByteArray()
    }

    private fun buildTextPart(dos: DataOutputStream, parameterName: String, parameterValue: String) {
        try {
            dos.writeBytes(twoHyphens + boundary + lineEnd)
            dos.writeBytes("Content-Disposition: form-data; name=\"$parameterName\"$lineEnd")
            dos.writeBytes("Content-Type: text/plain; charset=UTF-8$lineEnd")
            dos.writeBytes(lineEnd)
            dos.writeBytes(parameterValue + lineEnd)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun buildFilePart(dos: DataOutputStream, dataFile: DataPart, inputName: String) {
        dos.writeBytes(twoHyphens + boundary + lineEnd)
        dos.writeBytes("Content-Disposition: form-data; name=\"$inputName\"; filename=\"${dataFile.fileName}\"$lineEnd")
        if (!dataFile.type.isNullOrBlank()) {
            dos.writeBytes("Content-Type: ${dataFile.type}$lineEnd")
        } else {
            dos.writeBytes("Content-Type: application/octet-stream$lineEnd")
        }
        dos.writeBytes(lineEnd)

        dos.write(dataFile.content)

        dos.writeBytes(lineEnd)
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<NetworkResponse> {
        return try {
            Response.success(response, HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: Exception) {
            Response.error(com.android.volley.ParseError(e))
        }
    }

    override fun deliverResponse(response: NetworkResponse) {
        mListener.onResponse(response)
    }

    override fun deliverError(error: com.android.volley.VolleyError) {
        mErrorListener.onErrorResponse(error)
    }

    open fun getByteData(): Map<String, DataPart>? {
        return null
    }

    data class DataPart(
        var fileName: String,
        var content: ByteArray,
        var type: String = "application/octet-stream"
    )
}
