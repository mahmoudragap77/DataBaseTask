package com.training.databasetask.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATA_BASE_NAME, null, DATA_BASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase?) {
            val sql = "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_NAME TEXT, " +
                    "$COLUMN_PHONE TEXT)"
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldversion: Int, newversion: Int) {
    db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        const val DATA_BASE_NAME = "MyDB"
        const val DATA_BASE_VERSION = 1
        const val TABLE_NAME = "contacts"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PHONE = "phone"
    }
}