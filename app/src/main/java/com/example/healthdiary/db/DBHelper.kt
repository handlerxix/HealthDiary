package com.example.healthdiary.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*
import kotlin.collections.ArrayList

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory? = null) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    constructor(context: Context) : this(context, null)

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(UserParameter.createQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(UserParameter.dropQuery)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(UserParameter.dropQuery)
        onCreate(db)
    }

    fun getUserParameterList(): ArrayList<UserParameter> {
        val db = this.readableDatabase
        val cursor = db.rawQuery(UserParameter.selectQuery, null)
        val result = UserParameter.getUserParametersList(cursor)
        cursor.close()
        return result
    }

    fun getUserParameter(name: String): String? {
        val db = this.readableDatabase
        val cursor = db.rawQuery(UserParameter.getParameterQuery(name), null)
        val result = if (cursor!!.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(UserParameter.VALUE_COLUMN_NAME))
        } else {
            null
        }
        cursor.close()
        return result
    }

    fun updateUserParameter(userParameter: UserParameter) {
        val db = this.readableDatabase
        db.execSQL(UserParameter.getUpdateQuery(userParameter))
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "healthDiary.db"
    }
}