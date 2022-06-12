package uz.os3ketchup.navigationdrawer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import uz.os3ketchup.navigationdrawer.databinding.ItemFromBinding
import uz.os3ketchup.navigationdrawer.databinding.ItemToBinding
import uz.os3ketchup.navigationdrawer.models.Message

class GroupMessageAdapter(var list: List<Message>, var firebaseAuth: FirebaseAuth, var toUID:String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class FromVH(var itemFromBinding: ItemFromBinding) :
        RecyclerView.ViewHolder(itemFromBinding.root) {
        fun onBind(message: Message) {
            itemFromBinding.tvMessage.text = message.text
        }
    }

    inner class ToVH(var itemToBinding: ItemToBinding) :
        RecyclerView.ViewHolder(itemToBinding.root) {
        fun onBind(message: Message) {
            itemToBinding.tvMessage.text = message.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            FromVH(ItemFromBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            ToVH(ItemToBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 1) {
            val fromVH = holder as FromVH
            fromVH.onBind(list[position])
        } else {
            (holder as ToVH).onBind(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun getItemViewType(position: Int): Int {
        firebaseAuth = Firebase.auth
        val uid = firebaseAuth.currentUser?.uid

        return if (list[position].fromUID == uid) {
            1
        } else if(list[position].toUID==toUID) {
            2
        }else{
            3
        }


    }


}