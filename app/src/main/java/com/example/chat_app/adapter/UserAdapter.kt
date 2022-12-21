package com.example.chat_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chat_app.R
import com.example.chat_app.User
import com.example.chat_app.databinding.ItemUserBinding

class UserAdapter(var list: ArrayList<User>, var myListener: MyListener) :
    RecyclerView.Adapter<UserAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(user: User, position: Int) {
            val bind = ItemUserBinding.bind(itemView)
            bind.tvName.text = user.name
            Glide.with(itemView.context).load(user.imgUser).into(bind.ivProfile)
            bind.root.setOnClickListener {
                myListener.onClickListener(user, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface MyListener {
        fun onClickListener(user: User, position: Int)
    }
}