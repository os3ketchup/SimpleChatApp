package uz.os3ketchup.navigationdrawer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import uz.os3ketchup.navigationdrawer.databinding.ActivitySettingsBinding
import uz.os3ketchup.navigationdrawer.models.User

class Settings : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topAppBar.setNavigationOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        binding.topAppBar.setOnMenuItemClickListener {
            menu->
            when(menu.itemId){
                R.id.check->{
                 val name =    binding.etFirstNameMain.text.toString()
                   val lastName = binding.etLastNameMain.text.toString()
                    val fullName = "$name $lastName"
                    val user = User(name=fullName)
                    val intent = Intent(this,MainActivity::class.java)
                    intent.putExtra("username",user.name)
                    startActivity(intent)
                    finish()
                    true
                }else->{
                    false
                }
            }
        }



        val window = this.window
        setWindow( window)

    }







    private fun setWindow(window: Window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}