


// ApiService.kt
package com.saveetha.smarthealthcareapp.network


import ApiResponse
import com.saveetha.smarthealthcareapp.ClinicAppointmentRequest
import com.saveetha.smarthealthcareapp.adapter.DoctorSlot
import retrofit2.http.Query
import com.saveetha.smarthealthcareapp.api.DoctorSlotRequest
import com.saveetha.smarthealthcareapp.model.Appointment
import com.saveetha.smarthealthcareapp.model.BookingResponse
import com.saveetha.smarthealthcareapp.model.Clinic
import com.saveetha.smarthealthcareapp.model.ClinicAppointmentResponse
//import com.saveetha.smarthealthcareapp.model.GenericResponse
import com.saveetha.smarthealthcareapp.models.AppointmentResponse
import com.saveetha.smarthealthcareapp.models.BookAppointmentRequest
import com.saveetha.smarthealthcareapp.models.DoctorNameRequest
import com.saveetha.smarthealthcareapp.models.GenericResponse
import com.saveetha.smarthealthcareapp.models.Hospital
import com.saveetha.smarthealthcareapp.models.ReviewsResponse
import com.saveetha.smarthealthcareapp.models.UpdateAppointmentStatusRequest
import com.saveetha.smarthealthcareapp.models.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap


interface ApiService {

    @Multipart
    @POST("request_access.php")
    fun submitHospitalDetails(
        @Part("hospital_name") hospitalName: RequestBody,
        @Part("registration_number") registrationNumber: RequestBody,
        @Part("hospital_address") hospitalAddress: RequestBody,
        @Part("contact_number") contactNumber: RequestBody,
        @Part("admin_name") adminName: RequestBody,
        @Part("admin_email") adminEmail: RequestBody,
        @Part("admin_position") adminPosition: RequestBody,
        @Part("department") department: RequestBody,
        @Part("number_of_users") numberOfUsers: RequestBody,
        @Part("brief_description") briefDescription: RequestBody,
        @Part("user_id") userId: RequestBody,
        @Part hospital_license: MultipartBody.Part?,
        @Part registration_certificate: MultipartBody.Part?,
        @Part additional_documents: MultipartBody.Part?
    ): Call<ApiResponse>    // ✅ updated here

    @Multipart
    @POST("upload_hospital.php")
    fun uploadHospitalProfile(
        @PartMap formFields: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part logo: MultipartBody.Part?,
        @Part specialties_image: MultipartBody.Part?,
        @Part technologies_images: List<MultipartBody.Part>
    ): Call<ApiResponse>    // ✅ updated here

    @GET("get_latest_hospital.php")
    fun getLatestHospital(): Call<Hospital>

    @GET("get_hospitals.php")
    fun getHospitals(): Call<List<Hospital>>

    @POST("book_appointment.php")
    fun bookAppointment(@Body appointment: BookAppointmentRequest): Call<ApiResponse>   // ✅ updated here

//    @Multipart
//    @POST("update_patient_profile_pic.php")
//    fun uploadProfileImage(
//        @Part("user_id") userId: RequestBody,
//        @Part profile_image: MultipartBody.Part
//    ): Call<UploadResponse>

    @FormUrlEncoded
    @POST("doctor_hospital_address.php")
    fun saveDoctorHospitalAddress(
        @Field("user_id") userId: Int,
        @Field("state") state: String,
        @Field("district") district: String,
        @Field("division") division: String,
        @Field("mandal") mandal: String,
        @Field("gp") gp: String
    ): Call<ResponseBody>

    @POST("save_doctor_slots.php")
    fun submitDoctorSlots(@Body request: DoctorSlotRequest): Call<GenericResponse>

    @FormUrlEncoded
    @POST("search_clinics.php")
    fun searchClinics(
        @Field("state") state: String,
        @Field("district") district: String,
        @Field("block") block: String, // was division
        @Field("mandal") mandal: String,
        @Field("gp") gp: String,
        @Field("language") language: String
    ): Call<List<Clinic>>

    @FormUrlEncoded
    @POST("book_appointment.php")
    fun bookAppointment(
        @Field("doctor_name") doctorName: String,
        @Field("clinic_address") clinicAddress: String,
        @Field("purpose") purpose: String,
        @Field("date") date: String,
        @Field("time") time: String,
        @Field("patient_name") patientName: String,
        @Field("patient_phone") patientPhone: String
    ): Call<BookingResponse>

    @POST("book_clinic_appointment.php")
    fun bookAppointment(
        @Body appointmentRequest: ClinicAppointmentRequest
    ): Call<ClinicAppointmentResponse>




    @POST("get_clinic_appointments.php")
    fun getAppointmentsForDoctor(
        @Body request: DoctorNameRequest
    ): Call<ClinicAppointmentResponse>

    @Multipart
    @POST("update_patient_profile_pic.php")
    fun uploadProfileImage(
        @Part("user_id") userId: RequestBody,
        @Part profileImage: MultipartBody.Part
    ): Call<UploadResponse>


    @GET("get_patient_appointments.php")
    fun getAppointments(@Query("email") email: String): Call<AppointmentResponse>

    @Multipart
    @POST("update_doctor_profile_pic.php")
    fun uploadDoctorProfileImage(
        @Part doctorId: MultipartBody.Part,
        @Part profile_image: MultipartBody.Part
    ): Call<UploadResponse>
    @POST("update_appointment_status.php")
    fun updateAppointmentStatus(@Body request: UpdateAppointmentStatusRequest): Call<ClinicAppointmentResponse>

    @GET("get_Doctor_Slots.php") // your PHP file
    fun getDoctorSlots(@Query("user_id") userId: Int): Call<List<DoctorSlot>>


    // In your ApiService interface, add this method:
//    @FormUrlEncoded
//    @POST("update_appointment_rating.php")
//    fun updateAppointmentRating(
//        @Field("appointment_id") appointmentId: Int,
//        @Field("rating") rating: Float
//    ): Call<GenericResponse>
    // In your ApiService interface, update the method:

        @FormUrlEncoded
        @POST("update_appointment_rating.php")
        fun updateAppointmentRating(
            @Field("appointment_id") appointmentId: Int,
            @Field("rating") rating: Float,
            @Field("review") review: String
        ): Call<GenericResponse>

    @GET("get_reviews.php")
    fun getReviews(
        @Query("doctor_name") doctorName: String
    ): Call<ReviewsResponse>

}

