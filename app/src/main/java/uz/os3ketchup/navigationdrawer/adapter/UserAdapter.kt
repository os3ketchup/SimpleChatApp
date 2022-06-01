package uz.os3ketchup.navigationdrawer.adapter

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.provider.SyncStateContract
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import uz.os3ketchup.navigationdrawer.ChatActivity
import uz.os3ketchup.navigationdrawer.Constants
import uz.os3ketchup.navigationdrawer.Constants.USER_UID_KEY
import uz.os3ketchup.navigationdrawer.Constants.mAuth
import uz.os3ketchup.navigationdrawer.Constants.username
import uz.os3ketchup.navigationdrawer.databinding.ItemRvBinding
import uz.os3ketchup.navigationdrawer.models.User
import java.util.*
import kotlin.random.Random

class UserAdapter(var context: Context ,var list: List<User>) : RecyclerView.Adapter<UserAdapter.VH>() {

    inner class VH(private var itemRV: ItemRvBinding) : RecyclerView.ViewHolder(itemRV.root) {
        fun onBind(user: User) {
            val cut = user.UId.subSequence(3..9).toString().lowercase(Locale.getDefault())
            val whole =  user.UId
            /*user.name = cut*/
            itemRV.tvMessage.text = user.name

            itemRV.root.setOnClickListener {
                val intent =  Intent(context,ChatActivity::class.java)
                intent.putExtra(USER_UID_KEY,whole)
                context. startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}