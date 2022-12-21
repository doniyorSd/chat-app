package com.example.chat_app

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.chat_app.adapter.UserAdapter
import com.example.chat_app.databinding.FragmentUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserFragment : Fragment() {
    lateinit var binding: FragmentUserBinding
    lateinit var adapter: UserAdapter
    private lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference
    lateinit var list: ArrayList<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        binding = FragmentUserBinding.bind(view)

        auth = FirebaseAuth.getInstance()
        list = ArrayList()
        database = Firebase.database
        myRef = database.getReference("users")
        val currentUser = auth.currentUser

        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                val children = snapshot.children
                children.forEach {
                    val user = it.getValue(User::class.java)
                    if (currentUser?.uid != user?.id) {
                        list.add(user!!)
                    }
                    adapter = UserAdapter(list, object : UserAdapter.MyListener {
                        override fun onClickListener(user: User, position: Int) {
                            val bundle = Bundle()
                            bundle.putSerializable("user", user)
                            findNavController().navigate(R.id.chatFragment, bundle)
                        }
                    })
                    binding.rv.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        return view
    }
}