package uz.os3ketchup.navigationdrawer.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import uz.os3ketchup.navigationdrawer.Constants.GROUP_NAME_KEY
import uz.os3ketchup.navigationdrawer.GroupActivity
import uz.os3ketchup.navigationdrawer.databinding.ItemRvBinding
import uz.os3ketchup.navigationdrawer.models.Group

class GroupAdapter(var context: Context ,private var list: List<Group>): RecyclerView.Adapter<GroupAdapter.VH>() {

    inner class VH(private var itemRV: ItemRvBinding):RecyclerView.ViewHolder(itemRV.root){
        fun onBind(group: Group,position: Int){
            itemRV.tvMessage.text = group.name
            itemRV.root.setOnClickListener {
                val intent = Intent(context,GroupActivity::class.java)
                intent.putExtra(GROUP_NAME_KEY,position)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemRvBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position],position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}