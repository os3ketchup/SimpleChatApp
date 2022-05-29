package uz.os3ketchup.navigationdrawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import uz.os3ketchup.navigationdrawer.adapter.UserAdapter
import uz.os3ketchup.navigationdrawer.databinding.FragmentChatBinding
import uz.os3ketchup.navigationdrawer.models.User


class ChatFragment : Fragment() {
    lateinit var binding: FragmentChatBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("users")

        val user = User(Constants.mAuth.uid!!)


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
                val userAdapter = UserAdapter(requireContext(), list)
                binding.rvChat.adapter = userAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}