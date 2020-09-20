package com.example.healthdiary.model

import android.database.Cursor
import java.text.SimpleDateFormat
import java.util.*

class WaterTrackParameters {

    var date: String
    var currentIntake: Int
    var liquid: String

    constructor(currentIntake: Int, liquid: String) {
        this.date = SimpleDateFormat(DATE_FORMAT).format(Date())
        this.currentIntake = currentIntake
        this.liquid = liquid
    }

    constructor(date: String, currentIntake: Int, liquid: String) {
        this.date = date
        this.currentIntake = currentIntake
        this.liquid = liquid
    }

    companion object {
        const val MODEL_NAME = "WaterTracking"
        const val ID_COLUMN_NAME = "id"
        const val LIQUID_COLUMN_NAME = "liquid"
        const val DATE_COLUMN_NAME = "date"
        const val CURRENTINTAKE_COLUMN_NAME = "currentIntake"
        const val DATE_FORMAT = "dd.MM.yyyy"

        val createQuery
            get() = "CREATE TABLE $MODEL_NAME (" +
                    "$ID_COLUMN_NAME INTEGER PRIMARY KEY," +
                    "$DATE_COLUMN_NAME TEXT," +
                    "$CURRENTINTAKE_COLUMN_NAME INTEGER," +
                    "$LIQUID_COLUMN_NAME STRING" +
                    ")"

        val dropQuery get() = "DROP TABLE IF EXISTS $MODEL_NAME"

        val selectQuery get() = "SELECT * FROM $MODEL_NAME ORDER BY $ID_COLUMN_NAME"

        val selectLastQuery
            get() = "SELECT * FROM $MODEL_NAME " +
                    "WHERE $DATE_COLUMN_NAME = '${SimpleDateFormat(DATE_FORMAT).format(Date())}'"

        fun getUpdateQuery(waterTrackParameters: WaterTrackParameters) =
            "INSERT OR REPLACE INTO $MODEL_NAME ($CURRENTINTAKE_COLUMN_NAME, $DATE_COLUMN_NAME, $LIQUID_COLUMN_NAME) " +
                    "VALUES (${waterTrackParameters.currentIntake},'${waterTrackParameters.date}', '${waterTrackParameters.liquid}') "

        fun getWaterTrackParametersList(cursor: Cursor?): LinkedList<WaterTrackParameters> {
            val result: LinkedList<WaterTrackParameters> = LinkedList()
            if (cursor!!.moveToFirst()) {
                result.addFirst(getWaterTrackParameters(cursor))
                while (cursor.moveToNext()) {
                    result.addFirst(getWaterTrackParameters(cursor))
                }
                cursor.close()
            }
            return result
        }

        fun getWaterTrackParameters(cursor: Cursor): WaterTrackParameters {
            return WaterTrackParameters(
                cursor.getString(cursor.getColumnIndex(DATE_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(CURRENTINTAKE_COLUMN_NAME)).toInt(),
                cursor.getString(cursor.getColumnIndex(LIQUID_COLUMN_NAME))
            )
        }
    }
}