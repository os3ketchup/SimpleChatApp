package uz.os3ketchup.navigationdrawer

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.R
import com.google.firebase.auth.ktx.oAuthCredential
import uz.os3ketchup.navigationdrawer.Constants.PHONE_KEY
import uz.os3ketchup.navigationdrawer.Constants.mSMSCode
import uz.os3ketchup.navigationdrawer.Constants.mVerificationId
import uz.os3ketchup.navigationdrawer.databinding.ActivityVerificationBinding
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class VerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerificationBinding
    private lateinit var phoneNumber: String

    private var etCode = ""


    private val TAG = "VerificationActivity"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        phoneNumber = intent.getStringExtra(PHONE_KEY).toString()
        binding.tvReceiveNumber.text = "Please enter the OTP received in         $phoneNumber"

        sendCode()
        with(binding) {
            progressCircular.visibility = View.VISIBLE

        }



        binding.btnVerify.setOnClickListener {

            with(binding){
                etCode = et1.text.toString()+et2.text.toString()+et3.text.toString()+et4.text.toString()+et5.text.toString()+et6.text.toString()
            }

            if (etCode.isNotEmpty()){
                val credential = PhoneAuthProvider.getCredential(mVerificationId!!, etCode)
                signInWithPhoneAuthCredential(credential)
            }
        }






    }


    private fun sendCode() {
        val options = PhoneAuthOptions.newBuilder(Constants.mAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)

            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.


            /*val intent = Intent(this@VerificationActivity, MainActivity::class.java)
            startActivity(intent)*/
            signInWithPhoneAuthCredential(credential)

        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.


            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.



            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId
            /* resendToken = token*/
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        Constants.mAuth.signInWithCredential(credential).addOnSuccessListener {

        }
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    binding.progressCircular.visibility = View.INVISIBLE
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                  /*  receivedCode = credential.smsCode!!
                    with(binding){
                        et1.setText(receivedCode[0].toString())
                        et2.setText(receivedCode[1].toString())
                        et3.setText(receivedCode[2].toString())
                        et4.setText(receivedCode[3].toString())
                        et5.setText(receivedCode[4].toString())
                        et6.setText(receivedCode[5].toString())
                    }*/

                    Toast.makeText(this, "  ${credential.smsCode}", Toast.LENGTH_SHORT).show()
                    val user = task.result?.user

                    Constants.userPhoneNumber = user?.phoneNumber!!

                    Log.d(TAG, "signInWithPhoneAuthCredential: ${credential.smsCode}")

                } else {
                    // Sign in failed, display a message and update the UI

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
}