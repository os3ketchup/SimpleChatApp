package uz.os3ketchup.navigationdrawer

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import uz.os3ketchup.navigationdrawer.Constants.mAuth
import uz.os3ketchup.navigationdrawer.Constants.username
import uz.os3ketchup.navigationdrawer.adapter.MyViewPagerAdapter
import uz.os3ketchup.navigationdrawer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var myViewPagerAdapter: MyViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val user_name = intent.getStringExtra("username")



        val header = binding.navigationView.inflateHeaderView(R.layout.header_layout)
        val tvNumber = header.findViewById<TextView>(R.id.tv_number_profile)
        val tvName = header.findViewById<TextView>(R.id.tv_header_name)

        tvNumber.text = mAuth.currentUser?.phoneNumber
        if (user_name!=null){
            tvName.text = user_name
        }

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("users")

        myViewPagerAdapter = MyViewPagerAdapter(this)
        binding.rvAdapter.adapter = myViewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.rvAdapter) { tab, index ->
            run {
                when (index) {
                    0 -> {
                        /*tab.icon = ContextCompat.getDrawable(this, R.drawable.ic_person)*/
                        tab.text = "Chats"
                    }
                    1 -> {
                        /*tab.icon = ContextCompat.getDrawable(this, R.drawable.ic_groups)*/
                        tab.text = "Groups"
                    }
                    else -> {
                        throw Resources.NotFoundException("dsasd")
                    }
                }
            }
        }.attach()








        binding.topAppBar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item selected

            when (menuItem.itemId) {
                R.id.settings->{
                        startActivity(Intent(this,Settings::class.java))
                }


                R.id.log_out -> {
                    Constants.mAuth.signOut()
                    if (mAuth.currentUser == null) {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish() // destroy login so user can't come back with back button
                    }
                }
            }

            menuItem.isChecked = true
            binding.drawerLayout.close()
            true
        }


    }


}