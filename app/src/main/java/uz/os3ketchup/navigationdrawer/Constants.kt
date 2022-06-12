package uz.os3ketchup.navigationdrawer

import com.google.firebase.auth.FirebaseAuth

object Constants {
    const val PHONE_KEY = "KEY"
    const val USER_UID_KEY = "UID"
    const val GROUP_NAME_KEY = "GROUP_NAME"
    lateinit var mAuth: FirebaseAuth
    var mVerificationId:String? = null
    var mSMSCode:String? = null
    var username:String? = null
    var userPhoneNumber = ""
}