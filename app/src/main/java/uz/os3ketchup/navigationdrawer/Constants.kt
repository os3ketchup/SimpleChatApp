package uz.os3ketchup.navigationdrawer

import com.google.firebase.auth.FirebaseAuth

object Constants {
    const val PHONE_KEY = "KEY"
    const val USER_UID_KEY = "UID"
    lateinit var mAuth: FirebaseAuth
    var mVerificationId:String? = null
    var mSMSCode:String? = null
    var userPhoneNumber = ""
}