package uz.os3ketchup.navigationdrawer.dao

import androidx.room.*
import uz.os3ketchup.navigationdrawer.models.SaveProfile

@Dao
interface ProfileDao {
    @Transaction
    @Query("select * from SaveProfile")
    fun getAllProfile():List<SaveProfile>

    @Insert
    fun addPerson(saveProfile: SaveProfile)

    @Update
    fun updateProfile(saveProfile: SaveProfile)

    @Query("select * from SaveProfile")
    fun getAllWord():List<SaveProfile>
}