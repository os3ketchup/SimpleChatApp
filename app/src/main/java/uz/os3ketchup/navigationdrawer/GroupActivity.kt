package uz.os3ketchup.navigationdrawer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.view.Window
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import uz.os3ketchup.navigationdrawer.Constants.GROUP_NAME_KEY
import uz.os3ketchup.navigationdrawer.Constants.USER_UID_KEY
import uz.os3ketchup.navigationdrawer.adapter.GroupAdapter
import uz.os3ketchup.navigationdrawer.adapter.MessageAdapter
import uz.os3ketchup.navigationdrawer.databinding.ActivityGroupBinding
import uz.os3ketchup.navigationdrawer.models.Group


class GroupActivity : AppCompatActivity() {
    lateinit var binding: ActivityGroupBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    lateinit var list: ArrayList<Group>
    lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        setWindow( window)

        firebaseAuth = Firebase.auth
        val curUID =  firebaseAuth.currentUser?.uid!!
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("gr_messages")
        val positionList = intent.getIntExtra(GROUP_NAME_KEY,0)

        val groupReference = firebaseDatabase.getReference("groups")
        groupReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                 list = ArrayList<Group>()
                for (child in snapshot.children){
                    val group = child.getValue(Group::class.java)
                    if (group?.name!!.isNotEmpty()){
                        list.add(group)
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })



        binding.btnSend.setOnClickListener {
            val messages = uz.os3ketchup.navigationdrawer.models.Message(binding.etMessage.text.toString(),curUID,list[positionList].name)
            databaseReference.child(databaseReference.push().key!!).setValue(messages)
            binding.etMessage.text.clear()
        }

        databaseReference.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val messageList = ArrayList<uz.os3ketchup.navigationdrawer.models.Message>()
                for (child in snapshot.children){
                    val message = child.getValue(uz.os3ketchup.navigationdrawer.models.Message::class.java)
                    if (message!=null&&list[positionList].name==message.toUID)
                    messageList.add(message)

                }
                val groupMessageAdapter = MessageAdapter(messageList,firebaseAuth,"groupName")
                binding.rvMessage.adapter = groupMessageAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


    }

    private fun setWindow(window: Window?) {
        window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}