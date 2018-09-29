package io.navendra.nestedrecycler.application

import android.app.Application
import android.provider.CalendarContract
import io.navendra.nestedrecycler.R
import io.navendra.nestedrecycler.database.DatabaseHelper
import io.navendra.nestedrecycler.models.ChildModel
import io.navendra.nestedrecycler.models.ParentModel

class Application: Application() {

    companion object {
        lateinit var sharedInstance: Application
    }

    override fun onCreate() {
        super.onCreate()
        sharedInstance = this

        val list = DatabaseHelper.getDepartments()
        if (list.isEmpty()){
            // populate with test data
            DatabaseHelper.addDepartment(ParentModel("Department1",
                    arrayListOf(
                            ChildModel(R.drawable.aviator, "Child1.1"),
                            ChildModel(R.drawable.aviator, "Child1.2"),
                            ChildModel(R.drawable.aviator, "Child1.3")
                    )))
            DatabaseHelper.addDepartment(ParentModel("Department2",
                    arrayListOf(
                            ChildModel(R.drawable.aviator, "Child2.1"),
                            ChildModel(R.drawable.aviator, "Child2.2")
                    )))
            DatabaseHelper.addDepartment(ParentModel("Department3",
                    arrayListOf(
                            ChildModel(R.drawable.aviator, "Child3.1")
                    )))
            DatabaseHelper.addDepartment(ParentModel("Department4",
                    arrayListOf(
                            ChildModel(R.drawable.aviator, "Child4.1"),
                            ChildModel(R.drawable.aviator, "Child4.2"),
                            ChildModel(R.drawable.aviator, "Child4.3")
                    )))
        }
    }


}