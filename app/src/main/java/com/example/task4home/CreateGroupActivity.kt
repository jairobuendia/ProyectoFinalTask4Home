package com.example.task4home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task4home.adaptadores.UserCreateGroupAdapter
import com.example.task4home.databinding.ActivityCreateGroupBinding
import com.example.task4home.entidades.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*
import kotlin.collections.ArrayList

class CreateGroupActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateGroupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    //variables para recycler
    private lateinit var userList: ArrayList<User>
    private lateinit var userCreateGroupAdapter: UserCreateGroupAdapter
    private var groupCount: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityCreateGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var email = intent.extras?.get("email").toString()

        userList = arrayListOf()
        userCreateGroupAdapter = UserCreateGroupAdapter(userList, this, email)

        binding.recyclerViewUserGroup.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(false)
            adapter = userCreateGroupAdapter
        }

        calldb()

        binding.botonCrearGrupo.setOnClickListener {
            var nombreGrupo = binding.nameGroupText.text.toString().trim()
            var groupID = UUID.randomUUID().toString()
            userList.forEach {
                if (email == it.email) {
                    it.group = groupID
                    it.groupAdmin = true
                    it.onGroup = true
                }
                if (it.onGroup == true) {
                    it.group = groupID
                    groupCount++
                }
            }
            //Log.i("test2" , "${groupCount}")
            if (groupCount > 1) {
                if (nombreGrupo != "") {
                    db.collection("groups").document(groupID).set(
                        hashMapOf(
                            "groupID" to groupID,
                            "name" to nombreGrupo,
                        )
                    ).addOnSuccessListener {
                        var bach = FirebaseFirestore.getInstance().batch()
                        userList.forEach {
                            if (it.onGroup == true) {
                                var ref = db.collection("users").document(it.email)
                                bach.set(ref, it)
                            }
                        }
                        bach.commit()
                        saveGroupID(groupID)
                        val intencion = Intent(this, MainActivity::class.java)
                        startActivity(intencion)
                        finish()
                    }
                } else {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.nameGroup),
                        Snackbar.LENGTH_LONG
                    ).show()
                    groupCount = 0
                }
            } else {
                Snackbar.make(
                    binding.root,
                    getString(R.string.minimunGroup),
                    Snackbar.LENGTH_LONG
                ).show()
                groupCount = 0
            }

        }

        binding.botonCancelarGrupo.setOnClickListener {
            finish()
        }


    }


    private fun calldb() {
        db = FirebaseFirestore.getInstance()
        db.collection("users").whereEqualTo("onGroup", false)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        //Log.i("Carencias en firestore", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            userList.add(dc.document.toObject(User::class.java))
                        }
                    }
                    userCreateGroupAdapter.notifyDataSetChanged()
                }
            })
    }

    override fun onBackPressed() {}

    private fun saveGroupID(groupId : String){
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("group", groupId)
        prefs.apply()
    }


}