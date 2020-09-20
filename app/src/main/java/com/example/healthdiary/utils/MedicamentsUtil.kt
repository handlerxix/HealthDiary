package com.example.healthdiary.utils

import android.util.Log
import com.example.healthdiary.model.Medicament
import com.google.gson.internal.LinkedTreeMap

class MedicamentsUtil {

    private val client = JsonBinMedicamentsClient.create()

    fun getMedicaments(): List<Medicament> {
        try {
            val response = client.getMedicaments().execute()
            if (response.isSuccessful) {
                val body = response.body() as LinkedTreeMap<String, Any>
                val wrapper = body["medicaments"] as List<*>
                Log.d(this.javaClass.name, "$wrapper")
                return wrapper
                    .map { it as LinkedTreeMap<*, *> }
                    .map {
                        Medicament(
                            it["name"] as String,
                            it["price"] as String,
                            it["dosage"] as String,
                            it["manufacturer"] as String
                        )
                    }
                    .toList()
            }
        } catch (e: Exception) {
            Log.e(this.javaClass.name, "Something wrong happened while retrieving medicaments", e)
        }
        return emptyList()
    }

}