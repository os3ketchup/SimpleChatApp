package uz.os3ketchup.navigationdrawer.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.os3ketchup.navigationdrawer.Constants

@Entity
data class SaveProfile(
    @PrimaryKey(autoGenerate = true)
     var uid: Int? = null,

    var user_name: String? = null,

    var imageLink:String? = ""



)