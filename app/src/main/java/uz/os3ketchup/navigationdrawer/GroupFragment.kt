package uz.os3ketchup.navigationdrawer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import uz.os3ketchup.navigationdrawer.adapter.GroupAdapter
import uz.os3ketchup.navigationdrawer.adapter.UserAdapter
import uz.os3ketchup.navigationdrawer.databinding.FragmentGroupBinding
import uz.os3ketchup.navigationdrawer.models.Group


class GroupFragment : Fragment() {
    lateinit var binding: FragmentGroupBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    lateinit var group: Group

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentGroupBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*val text = arguments?.getString("key")*/

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("groups")

        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Group>()
                for (child in snapshot.children){
                    val group = child.getValue(Group::class.java)
                    if (group?.name!!.isNotEmpty()){
                        list.add(group)
                    }
                }
                val groupAdapter = GroupAdapter(requireContext(), list)
                binding.rvGroup.adapter = groupAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

}