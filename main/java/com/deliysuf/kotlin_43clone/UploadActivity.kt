package com.deliysuf.kotlin_43clone

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.MemoryFile
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.R
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.deliysuf.kotlin_43clone.databinding.ActivityUploadBinding
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.internal.StorageReferenceUri
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.lang.Exception
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant.now
import java.time.LocalDate.now
import java.time.YearMonth.now
import java.util.*
import kotlin.collections.HashMap

class UploadActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUploadBinding
    lateinit var uploadTask: UploadTask
    lateinit var Auth:FirebaseAuth
    lateinit var uri:Uri
    lateinit var storageRef:FirebaseStorage
    lateinit var Firebasestorage:FirebaseFirestore
    lateinit var activityRequest:ActivityResultLauncher<Intent>
    lateinit var permissionRequest:ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUploadBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
        requestLauncher()
        Auth= Firebase.auth
        storageRef=Firebase.storage
        Firebasestorage=Firebase.firestore


    }
    fun selectedImage(view: View){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(UploadActivity@this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(binding.root,"if you want to load something you should give permission",Snackbar.LENGTH_LONG).setAction("OK",
                    View.OnClickListener {
                        permissionRequest.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    })


            }else{
                permissionRequest.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
                    }
        else {

            val intent= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityRequest.launch(intent)

        }
    }
    fun requestLauncher(){
        activityRequest=registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                try{
                if(it.resultCode== RESULT_OK){
                    if (it != null) {
                        val intent=it.data
                        uri=intent!!.data!!
                        binding.uploadImage.setImageURI(uri)


                        }



                }}catch (e:Exception){
                    e.printStackTrace()
                    Toast.makeText(this,e.localizedMessage,Toast.LENGTH_LONG).show()
                }
            })
        permissionRequest=registerForActivityResult(ActivityResultContracts.RequestPermission(),
            ActivityResultCallback {
                if(it==true){

                    val intent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityRequest.launch(intent)

                }
                else{
                    Toast.makeText(UploadActivity@this,"You shold give the permission",Toast.LENGTH_LONG).show()
                }


            })


    }
    fun upload(view:View){
        val userId=UUID.randomUUID()
        val imageName="$userId.jpg"
        val reference=storageRef.reference
        val image=reference.child("images").child(imageName)
        if(uri!=null){
        uploadTask=image.putFile(uri)
        uploadTask.addOnSuccessListener {
            val uploadFire=storageRef.reference.child("images").child(imageName)
           uploadFire.downloadUrl.addOnSuccessListener {
               val imageUri=it.toString()
               if(Auth.currentUser!=null){
                   val timestamp=SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                   val postMap= hashMapOf<String,Any>()
                   postMap.put("Comment",binding.commentText.text.toString())
                   postMap.put("email", Auth.currentUser!!.email!!)
                   postMap.put("imageuri",imageUri)
                   postMap.put("Date",timestamp)

                   Firebasestorage.
                   collection("Media").
                   add(postMap).addOnSuccessListener {
                       finish()

                   }.addOnFailureListener{
                       Toast.makeText(UploadActivity@this
                           ,it.localizedMessage,Toast.LENGTH_LONG).show()
                   }
               }
           }
        }.addOnFailureListener {
            Toast.makeText(UploadActivity@ this, it.localizedMessage, Toast.LENGTH_LONG).show()
        }

        }
    }
}