package uz.os3ketchup.navigationdrawer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import uz.os3ketchup.navigationdrawer.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {


    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mAuth = Firebase.auth
        Constants.mAuth = mAuth
        val user = Constants.mAuth.currentUser

        if (user!=null) {
            // start activity
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }else{
          val   handler = Handler()
            handler.postDelayed({
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            },2000)

        }

    }
}