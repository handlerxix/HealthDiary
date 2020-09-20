package com.example.healthdiary.utils

import com.google.gson.internal.LinkedTreeMap
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

const val secretKey = "\$2b\$10\$EMVkM3cmoWqdAh84AV8V0.zf0JFVNQpwZh2phFfumZrNCpAdEe2Z."
const val url = "b/5f61c25a302a837e9567419e"

interface JsonBinMedicamentsClient {

    @Headers("secret-key: $secretKey")
    @GET(url)
    fun getMedicaments(): Call<LinkedTreeMap<String, Any>>

    companion object {
        fun create(): JsonBinMedicamentsClient {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.jsonbin.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(JsonBinMedicamentsClient::class.java)
        }
    }
}