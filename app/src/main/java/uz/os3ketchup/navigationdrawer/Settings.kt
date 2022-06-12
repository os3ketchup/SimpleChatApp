package uz.os3ketchup.navigationdrawer

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import uz.os3ketchup.navigationdrawer.database.MyDatabase
import uz.os3ketchup.navigationdrawer.databinding.ActivitySettingsBinding
import java.io.File
import java.io.IOException
import java.util.*

class Settings : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var filePath: Uri
    private lateinit var database: MyDatabase
    private lateinit var path: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        database = MyDatabase.getInstance(this)
        if (database.profileDao().getAllProfile().isNotEmpty())
        selectDefaultView()

        binding.ivAddSettings.setOnClickListener {
            selectImage()

        }


        binding.topAppBar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.topAppBar.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.check -> {
                    if (binding.etFirstNameMain.text.toString().trim().isEmpty()) {
                        binding.etFirstName.error = "Please fill the gaps"
                    } else {
                        val name = binding.etFirstNameMain.text.toString()
                        val lastName = binding.etLastNameMain.text.toString()
                        val fullName = "$name $lastName"
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("username", fullName)
                        intent.putExtra("image_link", path)
                        startActivity(intent)
                        finish()
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }


        val window = this.window
        setWindow(window)

    }

    private fun selectDefaultView() {
        database = MyDatabase.getInstance(this)
        if (database.profileDao().getAllProfile().isNotEmpty()){
            val imagelink = database.profileDao().getAllProfile()[0].imageLink!!
            val fullName = database.profileDao().getAllProfile()[0].user_name
            val myName = fullName?.split(" ")
            val firstName = myName?.get(0)
            var lastName = ""
            if (myName?.size==2){
                 lastName = myName[1]
            }

            Glide.with(this).load(imagelink).into(binding.ivAddSettings)
            binding.etFirstNameMain.setText(firstName)
            binding.etLastNameMain.setText(lastName)
        }

    }


    private fun uploadImage() {
        //code for showing progressDialog while uploading
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading")
        progressDialog.show()

        //Defining the child of storageReference
        val ref = storageReference.child("images/profile.jpg")

        val storageRef = Firebase.storage.reference
        storageRef.child("images/profile.jpg").downloadUrl
            .addOnSuccessListener { url ->
                // do whatever with your url
                path = url.toString()
            }
            .addOnFailureListener {

            }






        ref.putFile(filePath).addOnSuccessListener {
            progressDialog.dismiss()
            Toast.makeText(this, "image uploaded", Toast.LENGTH_SHORT).show()

        }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
            .addOnProgressListener {
                val progress = (100.0 * it.bytesTransferred / it.totalByteCount)
                progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
            }
            /* .continueWithTask { task ->
                 if (task.isSuccessful) {
                     throw task.exception!!
                 }
                 storageReference.downloadUrl
             }*/.addOnCompleteListener {
                if (it.isSuccessful) {


                   //
                } else {
                    Toast.makeText(this, "cant download uri", Toast.LENGTH_SHORT).show()
                }
            }

    }


    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select image from here..."), 1)

    }


    private fun setWindow(window: Window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data!!
            uploadImage()
            //uri

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                binding.ivAddSettings.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


}