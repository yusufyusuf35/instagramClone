package com.deliysuf.kotlin_43clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.deliysuf.kotlin_43clone.databinding.ActivityFeedBinding
import com.deliysuf.kotlin_43clone.model.Model
import com.deliysuf.kotlin_43clone.recyclerViewAdaoter.adapterViewClass
import com.google.firebase.Timestamp.now
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Timestamp
import java.time.Instant.now
import java.time.LocalDateTime.now

class feedActivity : AppCompatActivity() {
    private lateinit var binding:ActivityFeedBinding
    private lateinit var Auth:FirebaseAuth
    private lateinit var firebases:FirebaseFirestore
    private lateinit var fireArrayList:ArrayList<Model>
    private lateinit var adapters:adapterViewClass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFeedBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        fireArrayList=ArrayList<Model>()
        Auth= FirebaseAuth.getInstance()
        firebases=Firebase.firestore
        adapters= adapterViewClass(fireArrayList)
        binding.recyclerView.layoutManager=LinearLayoutManager(this)
        binding.recyclerView.adapter=adapters
        naber()




       }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater= MenuInflater(this)
        inflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.post) {
            val intent= Intent(this,UploadActivity::class.java)
            startActivity(intent)
        }
        else{
            Auth.signOut()
            val intent=Intent(this,MainActivity::class.java)
             startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    fun naber(){
        firebases.collection("Media").addSnapshotListener { value, error ->
            if(error!=null){
                Toast.makeText(this,error
                    .localizedMessage,Toast.LENGTH_LONG).show()
            }
            if (value != null){
                val docments= value.documents
                for(document in docments){
                    val comment=document.get("Comment") as String
                    val email=document.get("email") as String
                    val uri=document.get("imageuri") as String
                    println(comment)
                    val model=Model(email,comment,uri)


                    fireArrayList.add(model)

                }
                adapters.notifyDataSetChanged()

            }


        }

    }
}