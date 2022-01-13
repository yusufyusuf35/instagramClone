package com.deliysuf.kotlin_43clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.deliysuf.kotlin_43clone.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var Auth:FirebaseAuth
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        Auth= FirebaseAuth.getInstance()
        val currentUser=Auth.currentUser
        if(currentUser!=null) {
            val intent = Intent(this, feedActivity::class.java)
        }
    }
   fun singUp(view: View){
       val email=binding.EmailText.text.toString()
       val password=binding.passwordText.text.toString()

       Auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
           val intent= Intent(MainActivity@this,feedActivity::class.java)
           startActivity(intent)
       }.addOnFailureListener{
           Toast.makeText(MainActivity@this,it.localizedMessage,Toast.LENGTH_LONG).show()
       }
   }

    fun singIn(view:View){
        val email=binding.EmailText.text.toString()
        val password=binding.passwordText.text.toString()
        Auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            val intent=Intent(this,feedActivity::class.java)
            startActivity(intent)
        }.addOnFailureListener{
            Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
        }

    }


}