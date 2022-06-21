package com.example.task4home

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.task4home.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.robertlevonyan.components.picker.*
import java.util.*


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private var selectedPhotoUri: Uri? = null
    private val db = FirebaseFirestore.getInstance()
    val storage = Firebase.storage


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Boton de la camara

        binding.photoButton.setOnClickListener { lanzarCamara() }

        binding.buttonR.setOnClickListener { Registro(it) }
    }


    private fun lanzarCamara() {
        pickerDialog {
            setTitle(R.string.select_from)
            setTitleTextSize(22f)
            setTitleTextBold(true)
            setTitleGravity(Gravity.START)
            setItems(
                setOf(
                    ItemModel(
                        ItemType.Camera,
                        backgroundType = ShapeType.TYPE_ROUNDED_SQUARE,
                        itemBackgroundColor = Color.rgb(37, 150, 190)
                    ),
                    ItemModel(
                        ItemType.ImageGallery(MimeType.Image.All),
                        backgroundType = ShapeType.TYPE_ROUNDED_SQUARE,
                        itemBackgroundColor = Color.rgb(37, 150, 190)
                    ),
                )
            )

            setListType(PickerDialog.ListType.TYPE_LIST)
        }.setPickerCloseListener { type, uris ->
            when (type) {
                ItemType.Camera -> setChoosenImg(uris.first())
                is ItemType.ImageGallery -> {
                    setChoosenImg(uris.first())
                }
            }
        }.show()
    }

    private fun setChoosenImg(uri: Uri) {
        selectedPhotoUri = uri
        binding.ImageViuw.setImageURI(selectedPhotoUri)
    }

    private fun uploadImageToFirebaseStorage(
        view: View,
        name: String,
        password: String,
        email: String,
        username: String
    ) {
        showAlert(getString(R.string.creatingUser))
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    db.collection("users").document(email).set(
                        hashMapOf(
                            "email" to email,
                            "fullname" to name,
                            "username" to username,
                            "onGroup" to false,
                            "group" to "",
                            "groupAdmin" to false,
                            "imgUrl" to it.toString()
                        )
                    )
                    finish()
                }
            }

    }

    fun Registro(view: View) {
        var email = binding.emailRText.text.toString().trim()
        var password = binding.passwordRText.text.toString().trim()
        var name = binding.nameRText.text.toString().trim()
        var username = binding.usernameRText.text.toString().trim()

        auth = Firebase.auth
        if (email != "" && password != "" && name != "" && username != "") {
            if (password.length >= 6) {
                if (selectedPhotoUri != null) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) {
                            if (it.isSuccessful) {
                                setResult(RESULT_OK)
                                uploadImageToFirebaseStorage(view, name, password, email, username)
                            } else {
                                Snackbar.make(
                                    view,
                                    getString(R.string.failedregister),
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                } else {
                    Snackbar.make(
                        view,
                        getString(R.string.selectImageWarn),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            } else {
                Snackbar.make(
                    view,
                    getString(R.string.passwordregister),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        } else {
            Snackbar.make(
                view,
                getString(R.string.failedregister_fields),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
        val dialog = builder.create()
        dialog.show()
    }
}
