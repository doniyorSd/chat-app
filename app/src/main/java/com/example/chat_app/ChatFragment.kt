package com.example.chat_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chat_app.adapter.MessageAdapter
import com.example.chat_app.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Date

class ChatFragment : Fragment() {
    lateinit var binding: FragmentChatBinding
    private lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference
    lateinit var adapter: MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        binding = FragmentChatBinding.bind(view)

        auth = FirebaseAuth.getInstance()
        database = Firebase.database
        myRef = database.getReference("messages")
        val currentUser = auth.currentUser

        val selectedUser = arguments?.getSerializable("user") as User

        myRef.child(currentUser?.uid!!).child(selectedUser.id!!)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<Message>()
                    val children = snapshot.children
                    children.forEach {
                        val mess = it.getValue(Message::class.java)
                        list.add(mess!!)
                    }
                    adapter = MessageAdapter(list, currentUser.uid)
                    binding.rv.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

        binding.sendButton.setOnClickListener {
            val mess = binding.etMessage.text.toString()

            val sdf = SimpleDateFormat("hh:mm")
            val currentDate = sdf.format(Date())

            val message = Message(mess, currentUser?.uid!!, selectedUser.id!!, currentDate)
            val key = myRef.push().key

            myRef.child(currentUser.uid).child(selectedUser.id!!).child(key!!).setValue(message)
            myRef.child(selectedUser.id!!).child(currentUser.uid).child(key).setValue(message)
            binding.etMessage.text.clear()
        }

        return view
    }
}