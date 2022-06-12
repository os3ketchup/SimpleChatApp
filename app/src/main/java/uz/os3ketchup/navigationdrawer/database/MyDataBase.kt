package uz.os3ketchup.navigationdrawer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.os3ketchup.navigationdrawer.dao.ProfileDao
import uz.os3ketchup.navigationdrawer.models.SaveProfile

@Database(
    entities = [SaveProfile::class],
    version = 4
)

abstract class MyDatabase: RoomDatabase() {
    abstract fun profileDao():ProfileDao

    companion object{
        private var instance:MyDatabase?=null

        @Synchronized
        fun getInstance(context: Context):MyDatabase{
            if (instance==null){
                instance = Room.databaseBuilder(context,MyDatabase::class.java,"wordDb")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}