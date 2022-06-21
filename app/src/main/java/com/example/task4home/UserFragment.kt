package com.example.task4home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.PopupMenu
import androidx.recyclerview.widget.GridLayoutManager
import com.example.task4home.adaptadores.UserShowAdapter
import com.example.task4home.databinding.FragmentUserBinding
import com.example.task4home.entidades.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase

class UserFragment : Fragment() {

    private lateinit var db : FirebaseFirestore
    private lateinit var userArrayList: ArrayList<User>
    private lateinit var userShowAdapter: UserShowAdapter
    private lateinit var auth: FirebaseAuth

    private var _binding:FragmentUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater)
        var view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        userArrayList = arrayListOf()

        userShowAdapter = UserShowAdapter(userArrayList, activity as Context)
        binding.recyclerViewUserShow.apply {
            layoutManager = GridLayoutManager(activity, 2)
            setHasFixedSize(true)
            adapter = userShowAdapter
        }

        calldb()

        binding.menuButtonUsers.setOnClickListener {
            showPopup(binding.menuButtonUsers)
        }
    }


    private fun calldb(){
        //Llamar al id del grupo
        val prefs = activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val groupID = prefs?.getString("group", "")

        db = FirebaseFirestore.getInstance()
        db.collection("users").whereEqualTo("group", groupID).addSnapshotListener(object :
            EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.i("Carencias en firestore", error.message.toString())
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        userArrayList.add(dc.document.toObject(User::class.java))
                    }
                }
                userShowAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun showPopup (view: View){
        val popup = PopupMenu(activity as Context,view)
        val inflater:  MenuInflater = popup.menuInflater
        val prefs = activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)?.edit()
        val intencion = Intent(context, LoginActivity::class.java)
        inflater.inflate(R.menu.options_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId){
                R.id.log_out -> {
                    prefs?.clear()
                    prefs?.apply()

                    startActivity(intencion)

                    auth.signOut()
                    activity?.finish()
                }
            }
            true
        }
        popup.show()
    }



}