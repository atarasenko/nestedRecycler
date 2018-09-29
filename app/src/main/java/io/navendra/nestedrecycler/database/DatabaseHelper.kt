package io.navendra.nestedrecycler.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import io.navendra.nestedrecycler.application.Application
import io.navendra.nestedrecycler.models.ChildModel
import io.navendra.nestedrecycler.models.ParentModel

object DatabaseHelper: SQLiteOpenHelper(Application.sharedInstance, "departments", null, 1) {

    private const val DEPARTMENTS = "departments"
    private const val ITEMS = "items"
    private const val ID = "_id"
    private const val TITLE = "title"
    private const val IMAGE = "image"
    private const val DEPARTMENT = "department"

    override fun onCreate(db: SQLiteDatabase?) {
        if (db != null) {
            db.execSQL("CREATE TABLE IF NOT EXISTS $DEPARTMENTS (_id INTEGER PRIMARY KEY AUTOINCREMENT, $TITLE)")
            db.execSQL("CREATE TABLE IF NOT EXISTS $ITEMS (_id INTEGER PRIMARY KEY AUTOINCREMENT, $DEPARTMENT, $TITLE, $IMAGE)")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // TODO
    }

    fun addDepartment(department: ParentModel){
        writableDatabase.beginTransaction()
        try{
            val departmentId = writableDatabase.insert(DEPARTMENTS, null, toContentValues(department))
            for (item in department.children){
                writableDatabase.insert(ITEMS, null, toContentValues(departmentId, item))
            }
            writableDatabase.setTransactionSuccessful()
        } catch (e: Exception){
            e.printStackTrace()
        } finally {
            writableDatabase.endTransaction()
        }
    }

    fun getDepartments(): List<ParentModel>{
        val cursor = writableDatabase.query(DEPARTMENTS, arrayOf(ID, TITLE), null, null,
                null, null, ID)
        val list = mutableListOf<ParentModel>()
        if (cursor != null) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast){
                val departmentId = cursor.getLong(cursor.getColumnIndex(ID))
                val items = getItems(departmentId)
                val d = ParentModel(cursor.getString(cursor.getColumnIndex(TITLE)), items)
                list.add(d)
                cursor.moveToNext()
            }
            cursor.close()
        }

        return list

    }

    fun getItems(departmentId: Long): List<ChildModel>{
        val cursor = writableDatabase.query(ITEMS, arrayOf(ID, IMAGE, TITLE),
                "$DEPARTMENT=$departmentId",null,null, null, ID)
        val list = mutableListOf<ChildModel>()
        if (cursor != null){
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val item = ChildModel(cursor.getInt(cursor.getColumnIndex(IMAGE)),
                        cursor.getString(cursor.getColumnIndex(TITLE)))
                list.add(item)
                cursor.moveToNext()
            }
            cursor.close()
        }
        return list
    }

    // private

    private fun toContentValues(department: ParentModel): ContentValues{
        val cv = ContentValues()
        cv.put(TITLE, department.title)
        return cv
    }

    private fun toContentValues(departmentId: Long, item: ChildModel): ContentValues{
        val cv = ContentValues()
        cv.put(TITLE, item.title)
        cv.put(IMAGE, item.image)
        cv.put(DEPARTMENT, departmentId)
        return cv
    }



}