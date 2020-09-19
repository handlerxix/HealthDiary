package com.example.healthdiary.model

import com.example.healthdiary.ui.CodeValue
import java.io.Serializable

data class Recommendation(
    val title: String,
    val text: String,
    val condition: CodeValue
) : Serializable