package uz.os3ketchup.navigationdrawer

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import uz.os3ketchup.navigationdrawer.Constants.USER_UID_KEY
import uz.os3ketchup.navigationdrawer.adapter.MessageAdapter
import uz.os3ketchup.navigationdrawer.adapter.UserAdapter
import uz.os3ketchup.navigationdrawer.databinding.ActivityChatBinding
import uz.os3ketchup.navigationdrawer.models.Message
import uz.os3ketchup.navigationdrawer.models.User

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val window = this.window
        setWindow( window)
        firebaseAuth = Firebase.auth

        binding.topAppBar.setNavigationOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
         val cuid =    firebaseAuth.currentUser?.uid
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("messages")
        val toUID = intent.getStringExtra(USER_UID_KEY)!!

        binding.btnSend.setOnClickListener {
            val messages = Message(binding.etMessage.text.toString(),cuid!!,toUID)
            databaseReference.child(databaseReference.push().key!!).setValue(messages)
            binding.etMessage.text.clear()
        }

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messageList = ArrayList<Message>()

                for (child in snapshot.children) {
                    val message = child.getValue(Message::class.java)
                    if (message!=null && ((message.fromUID == cuid && message.toUID == toUID) || (message.fromUID==toUID && message.toUID==cuid)))
                    messageList.add(message)



                }
                val messageAdapter = MessageAdapter( messageList,firebaseAuth,toUID)
                binding.rvMessage.scrollToPosition(messageList.count()-1)
                binding.rvMessage.adapter = messageAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


        /*list = ArrayList()
        list.add(Message("Assalomu alaykum","1","2"))
        list.add(Message("Vaaleykum assalom","2","1"))
        list.add(Message("Yaxshimisiz","1","2"))
        list.add(Message("Rahmat yaxshi","2","1"))
        list.add(Message("Rahmat yaxshi","3","1"))
        binding.rvMessage.adapter = MessageAdapter(list)*/
    }

    private fun setWindow(window: Window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}