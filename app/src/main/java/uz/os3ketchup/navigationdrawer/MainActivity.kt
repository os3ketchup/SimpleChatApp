package uz.os3ketchup.navigationdrawer

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import uz.os3ketchup.navigationdrawer.adapter.MyViewPagerAdapter
import uz.os3ketchup.navigationdrawer.adapter.UserAdapter
import uz.os3ketchup.navigationdrawer.databinding.ActivityMainBinding
import uz.os3ketchup.navigationdrawer.models.User

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var myViewPagerAdapter: MyViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val header = binding.navigationView.inflateHeaderView(R.layout.header_layout)
        val tvNumber = header.findViewById<TextView>(R.id.tv_number_profile)
        tvNumber.text = Constants.mAuth.currentUser?.phoneNumber

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

/*        val user = User(Constants.mAuth.uid!!)


          *//*  databaseReference.child(databaseReference.push().key!!).setValue(user)*//*
            databaseReference.child(Constants.mAuth.uid!!).setValue(user)



        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<User>()
                for (child in snapshot.children) {
                    val user = child.getValue(User::class.java)
                    if (user?.UId != Constants.mAuth.currentUser?.uid) {
                        if (user != null) {
                            list.add(user)
                        }
                    }
                }
                val userAdapter = UserAdapter(this@MainActivity,list)
                binding.rvAdapter.adapter = userAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })*/







        binding.topAppBar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item selected

            when (menuItem.itemId) {
                R.id.log_out -> {
                    Constants.mAuth.signOut()
                    if (Constants.mAuth.currentUser == null) {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish(); // destroy login so user can't come back with back button
                    }
                }
            }

            menuItem.isChecked = true
            binding.drawerLayout.close()
            true
        }


    }


}