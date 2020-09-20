package com.example.healthdiary.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlin.collections.ArrayList

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory? = null) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    constructor(context: Context) : this(context, null)

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(UserParameter.createQuery)
        db.execSQL(Medicament.createQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(UserParameter.dropQuery)
        db.execSQL(Medicament.dropQuery)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(UserParameter.dropQuery)
        db.execSQL(Medicament.dropQuery)
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

    fun getMedicaments(): List<Medicament> {
        val db = this.readableDatabase
        val cursor = db.rawQuery(Medicament.selectQuery, null)
        return Medicament.getList(cursor)
    }

    fun saveMedicament(medicament: Medicament) {
        val db = this.writableDatabase
        db.execSQL(Medicament.getUpdateQuery(medicament))
        db.close()
    }

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "healthDiary.db"
    }
}