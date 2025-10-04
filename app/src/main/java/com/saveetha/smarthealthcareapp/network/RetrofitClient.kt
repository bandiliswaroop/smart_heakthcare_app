
    package com.saveetha.smarthealthcareapp.network

    import retrofit2.Retrofit
    import retrofit2.converter.gson.GsonConverterFactory


    object RetrofitClient {
        private const val BASE_URL = "http://192.168.24.116/smart_healthcare_app/"


        val instance: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

    }


