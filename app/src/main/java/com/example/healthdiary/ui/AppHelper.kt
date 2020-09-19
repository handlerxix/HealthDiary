package com.example.healthdiary.ui

import kotlin.math.pow

const val BASE_WATER_RATE = 3000
const val SUMMER_WATER_COEFF = 1.3f
const val WINTER_WATER_COEFF = 0.8f
const val NORMAL_TEMP = 36.6f
const val NORMAL_TEMP_DELTA = 0.2f
const val NORMAL_PRESS = 140
const val NORMAL_PRESS_DELTA = 5
const val TEMP_WATER_MUL_COEFF = 1.3f
const val MALE_WATER_RATE = 34f
const val FEMALE_WATER_RATE = 31f

const val DEFICIT = 18.5f
const val NORMAL = 25f
const val EXCESS = 30f
const val FIRST_DEGREE_OBESITY = 35f
const val SECOND_DEGREE_OBESITY = 40f

enum class CodeValue {
    FINE_CODE,
    HIGH_TEMP,
    LOW_TEMP,
    HIGH_PRESS,
    LOW_PRESS,
    HIGH_WEIGHT_INDEX,
    LOW_WEIGHT_INDEX
}

fun String.cutWords(maxStrSize: Int) : String {
    if (this.count() <= maxStrSize){
        return this
    }
    for (i in this.lastIndex downTo 0){
        if (this[i] == ' ' && i < maxStrSize) {
            return this.substring(0, i)
        }
    }
    return this
}

fun String.cutWordsWithEnding(maxStrSize: Int) : String {
    val newStr = this.cutWords(maxStrSize)
    if (this != newStr){
        return "$newStr..."
    }
    return this
}

class AppHelper {
    companion object {
        fun getWeightProblemCode(massIndex: Float?): CodeValue? {
            return when {
                massIndex == null -> null
                massIndex < DEFICIT -> CodeValue.LOW_WEIGHT_INDEX
                massIndex > EXCESS -> CodeValue.HIGH_WEIGHT_INDEX
                else -> null
            }
        }


        fun getTempCode(temp: Float?): CodeValue? {
            return when {
                temp == null -> null
                temp < NORMAL_TEMP - NORMAL_TEMP_DELTA -> CodeValue.LOW_TEMP
                temp > NORMAL_TEMP + NORMAL_TEMP_DELTA -> CodeValue.HIGH_TEMP
                else -> null
            }
        }

        fun getPressCode(press: Float?): CodeValue? {
            return when {
                press == null -> null
                press < NORMAL_PRESS - NORMAL_PRESS_DELTA -> CodeValue.LOW_PRESS
                press > NORMAL_PRESS + NORMAL_PRESS_DELTA -> CodeValue.HIGH_PRESS
                else -> null
            }
        }

        fun getMassIndex(weight: Float?, height: Float?): Float? {
            if (weight == null || height == null){
                return null
            }
            return weight / (height / 100f).pow(2)
        }
    }
}