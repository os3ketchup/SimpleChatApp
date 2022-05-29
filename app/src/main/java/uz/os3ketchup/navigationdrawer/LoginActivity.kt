package uz.os3ketchup.navigationdrawer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import uz.os3ketchup.navigationdrawer.Constants.PHONE_KEY
import uz.os3ketchup.navigationdrawer.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    /*lateinit var mAuth:FirebaseAuth*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
       /* mAuth = Firebase.auth
        Constants.mAuth = mAuth
        val user =  Constants.mAuth.currentUser


        if (user!=null) {
            // start activity
                startActivity(Intent(this,MainActivity::class.java))
            finish(); // destroy login so user can't come back with back button
        }*/

        binding.btnSend.setOnClickListener {

            if (binding.etPhoneNum.text.isNotEmpty()){
                val intent = Intent(this,VerificationActivity::class.java)
                intent.putExtra(PHONE_KEY,binding.etPhoneNum.text.toString())

                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "Please fill the gaps", Toast.LENGTH_SHORT).show()
            }

        }


    }
}