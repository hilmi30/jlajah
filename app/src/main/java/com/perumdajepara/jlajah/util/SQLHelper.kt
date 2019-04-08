package com.perumdajepara.jlajah.util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import hinl.kotlin.database.helper.SQLiteDatabaseHelper

class SQLHelper(context: Context): SQLiteDatabaseHelper(context, ConstantVariable.dbName, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        //
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //
    }
}

// init table
// val dataBase = DataBase(context)
// database.createTable(Example::class)

// insert row
// val dataBase = DataBase(context)
// val exampleObject = Example(columnOne = "One", columnTwo = 1, columnThree = Date())
// dataBase.insert(exampleObject)

// read row
// val dataBase = DataBase(context)
// val exampleList = dataBase.get(Example::class)

// for more selection
// val dataBase = DataBase(context)
// val exampleList = dataBase.get(Example::class) {
//     between("Id", 1, 20)
//     eq("columnOne", "One")
//     orderBy(key = "Id", order = ISelectionOperator.Order.Descending)
// }

// update row
// val dataBase = DataBase(context)
// originExampleObj.columnOne = "Two"
// dataBase.update(originExampleObj)

// delete row
// val dataBase = DataBase(context)
// dataBase.delete(deleteExampleObj)

// or
// val dataBase = DataBase(context)
// dataBase.delete(Example::class) {
//     between("Id", 1, 20)
//     eq("columnOne", "One")
// }

// If you want to Delete All Row
// dataBase.delete(Example::class)

// count
// val dataBase = DataBase(context)
// val count = dataBase.count(Example::class)

// for more selection
// val dataBase = DataBase(context)
// val count = dataBase.count(Example::class) {
//     between("Id", 1, 20)
//     eq("columnOne", "One")
// }

// selection cause
// val exampleList = dataBase.get(Example::class) {
//     between("Id", 1, 20)
//     eq("columnOne", "One")
//     orderBy(key = "Id", order = ISelectionOperator.Order.Descending)
// }

