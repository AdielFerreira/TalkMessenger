package br.com.talkmessenger.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import br.com.talkmessenger.MainActivity
import br.com.talkmessenger.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask



class SignUpActivity : AppCompatActivity() {

    companion object{
        private val PERMISSION_CODE_IMAGE_PICK = 1001
        private val IMAGE_PICK_CODE = 1000
    }

    val useImageView by lazy {
        findViewById<ShapeableImageView>(R.id.userImgView)
    }

    lateinit var auth:FirebaseAuth
    lateinit var downloadUrl:String

    val database by lazy {
        FirebaseFirestore.getInstance()
    }

    val continueBtn by lazy {
        findViewById<Button>(R.id.nextBtn)
    }
    val nameLbl by lazy {
        findViewById<EditText>(R.id.nameEt)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = Firebase.auth


        useImageView.setOnClickListener{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permission,PERMISSION_CODE_IMAGE_PICK)
                }else{
                    openGallery()
                }
            }else{
                openGallery()
            }
        }


        continueBtn.setOnClickListener{
            if(!::downloadUrl.isInitialized){
                Toast.makeText(this,"Image cannot be empty",Toast.LENGTH_SHORT).show()
            }else if(nameLbl.text.isEmpty()){
                Toast.makeText(this,"Nome cannot be empty",Toast.LENGTH_SHORT).show()
            }else{
                val user = User(nameLbl.text.toString(),downloadUrl,auth.uid!!)
                database.collection("users").document(auth.uid!!).set(user).addOnSuccessListener {
                    //TRATAR SUCESSO
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }.addOnFailureListener{
                    //TRATAR FALHA
                }
            }
        }
    }

    override fun onBackPressed() {

    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun uploadImage(image: Uri) {
        continueBtn.isEnabled = false
        val ref = FirebaseStorage.getInstance().reference.child("uploads/${auth.uid.toString()}")
        val uploadTask = ref.putFile(image)
        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                //Handle error
                Log.e("Error uploading", task.exception.toString())
            }
            return@Continuation ref.downloadUrl
        }).addOnCompleteListener { task ->
            continueBtn.isEnabled = true
            if (task.isSuccessful) {
                Log.e("Done uploading", task.result.toString())
                downloadUrl = task.result.toString()
            }
        }.addOnFailureListener {
            continueBtn.isEnabled = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            data?.data?.let { image->
                useImageView.setImageURI(image)
                uploadImage(image)
                Toast.makeText(this,"IMAGEM UPADA",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {

        when(requestCode){
            PERMISSION_CODE_IMAGE_PICK ->{
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openGallery()
                }else{
                    //Toast.makeText(this,"Permissao Negada!",Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}