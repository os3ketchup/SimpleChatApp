package uz.os3ketchup.navigationdrawer

import android.app.AlertDialog
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import uz.os3ketchup.navigationdrawer.Constants.mAuth
import uz.os3ketchup.navigationdrawer.adapter.MyViewPagerAdapter
import uz.os3ketchup.navigationdrawer.database.MyDatabase
import uz.os3ketchup.navigationdrawer.databinding.ActivityMainBinding
import uz.os3ketchup.navigationdrawer.models.Group
import uz.os3ketchup.navigationdrawer.models.SaveProfile


class MainActivity : AppCompatActivity() {
    private lateinit var list: ArrayList<SaveProfile>
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var myViewPagerAdapter: MyViewPagerAdapter
    private lateinit var myDatabase: MyDatabase
    lateinit var group: Group

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("groups")


        val username = intent.getStringExtra("username")
        val generatedUri = intent.getStringExtra("image_link")


        myDatabase = MyDatabase.getInstance(this)
        val profile = SaveProfile(user_name = firebaseAuth.currentUser?.uid?.substring(0..6))


        if (username != null) {
            val profile = SaveProfile(uid = 1, user_name = username, imageLink = generatedUri)
            myDatabase.profileDao().updateProfile(profile)
        }
        if (myDatabase.profileDao().getAllProfile().isEmpty()) {
            myDatabase.profileDao().addPerson(profile)
        }
        list = ArrayList()
        list.add(profile)
        val header = binding.navigationView.inflateHeaderView(R.layout.header_layout)
        val tvNumber = header.findViewById<TextView>(R.id.tv_number_profile)
        val tvName = header.findViewById<TextView>(R.id.tv_header_name)
        val imageHeader = header.findViewById<ImageView>(R.id.iv_header)

        tvNumber.text = mAuth.currentUser?.phoneNumber
        if (myDatabase.profileDao().getAllProfile().isNotEmpty()) {
            val name = myDatabase.profileDao().getAllProfile()[0].user_name
            val imageLink = myDatabase.profileDao().getAllProfile()[0].imageLink
            tvName.text = name
            Glide.with(this).load(imageLink).into(imageHeader)
        }

        binding.rvAdapter.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            // This method is triggered when there is any scrolling activity for the current page
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            // triggered when you select a new page
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        binding.fab.setOnClickListener {
                            val dialogBuilder = AlertDialog.Builder(this@MainActivity).create()
                            val dialogView = LayoutInflater.from(this@MainActivity)
                                .inflate(R.layout.dialog_chat, null, false)
                            dialogBuilder.setView(dialogView)
                            dialogBuilder.show()
                        }

                    }
                    1 -> {
                        binding.fab.setOnClickListener {
                            val dialogBuilder = AlertDialog.Builder(this@MainActivity).create()
                            val dialogView = LayoutInflater.from(this@MainActivity)
                                .inflate(R.layout.dialog_group, null, false)
                            val button_accept = dialogView.findViewById<Button>(R.id.btn_accept)!!
                            val et_group = dialogView.findViewById<EditText>(R.id.et_group)
                            button_accept.setOnClickListener {
                                val t = et_group.text.toString()
                                Toast.makeText(this@MainActivity, t, Toast.LENGTH_SHORT).show()

                                group = Group(name = t)
                                databaseReference.child(databaseReference.push().key!!).setValue(group)
                                dialogBuilder.cancel()
                            }
                            dialogBuilder.setView(dialogView)
                            dialogBuilder.show()

                        }


                    }
                }
            }

            // triggered when there is
            // scroll state will be changed
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })


/*
            when(currentFragment){
                "group"->{
                    binding.fab.setOnClickListener {
                        val dialogBuilder = AlertDialog.Builder(this).create()
                        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_group,null,false)
                        dialogBuilder.setView(dialogView)
                        dialogBuilder.show()
                    }
                }
                "chat"->{
                    binding.fab.setOnClickListener {
                        Toast.makeText(this, "chat fragment clicked", Toast.LENGTH_SHORT).show()
                    }
                }
            }*/





        myViewPagerAdapter = MyViewPagerAdapter(this, this)

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
                R.id.settings -> {
                    startActivity(Intent(this, Settings::class.java))
                }
                R.id.log_out -> {
                    mAuth.signOut()
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