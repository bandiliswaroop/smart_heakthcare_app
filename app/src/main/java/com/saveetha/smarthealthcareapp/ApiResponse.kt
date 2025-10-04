import com.google.gson.annotations.SerializedName

data class ApiResponse(
    val success: Boolean,
    val message: String? = null,
    val error:   String? = null,
    val status: String,
    @SerializedName("request_id")        // ← maps snake_case → camelCase
    val request_id: String? = null       //   keep this name exactly
)

//
//data class ApiResponse(
//    val success: Boolean,
//    val message: String? = null,
//    val error: String? = null,
//    @SerializedName("request_id")
//    val request_id: String? = null
//)
