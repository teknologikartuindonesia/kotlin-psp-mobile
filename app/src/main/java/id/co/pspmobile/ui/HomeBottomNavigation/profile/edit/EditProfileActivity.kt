package id.co.pspmobile.ui.HomeBottomNavigation.profile.edit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.ActivityEditProfileBinding
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST_CODE = 100
    private lateinit var selectedImageUri: Uri

    var isInputMode = false


    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel: EditProfileViewModel by viewModels()
    var currentEmail = ""
    var currentPhone = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnUploadImage.setOnClickListener {
            Log.d("EditProfileActivity", "btnUpload: clicked")
            checkPermission()
        }

        currentEmail = viewModel.getUserInfo().user.email
        currentPhone = viewModel.getUserInfo().user.phone
        binding.edEditEmail.setText(currentEmail)
        binding.edEditPhone.setText(currentPhone)

        binding.btnEditProfile.setOnClickListener {
            if (isInputMode) {
                binding.btnEditProfile.text = "Edit Profile"
                binding.btnUploadImage.visibility = View.GONE
                binding.btnSaveProfile.visibility = View.GONE
                binding.btnEditProfile.visibility = View.VISIBLE
                binding.edEditEmail.isEnabled = false
                binding.edEditPhone.isEnabled = false
                binding.llEmail.background = resources.getDrawable(R.drawable.input_bg_inactive)
                binding.llPhone.background = resources.getDrawable(R.drawable.input_bg_inactive)
                isInputMode = false
            } else {
                binding.btnEditProfile.text = "Cancel"
                binding.btnUploadImage.visibility = View.VISIBLE
                binding.btnSaveProfile.visibility = View.VISIBLE
                binding.btnEditProfile.visibility = View.GONE
                binding.edEditEmail.isEnabled = true
                binding.edEditPhone.isEnabled = true
                binding.llEmail.background = resources.getDrawable(R.drawable.input_bg)
                binding.llPhone.background = resources.getDrawable(R.drawable.input_bg)
                isInputMode = true
            }
        }

        binding.btnSaveProfile.setOnClickListener {
            if (binding.edEditEmail.text.toString() != currentEmail || binding.edEditPhone.text.toString() != currentPhone) {
                val body = viewModel.getUserInfo()
                Log.d("EditProfileActivity", "body: $body")
                body.user.email = binding.edEditEmail.text.toString()
                body.user.phone = binding.edEditPhone.text.toString()
                Log.d("EditProfileActivity", "body: $body")
                viewModel.updateProfile(body)
            }else{
                finish()
            }
        }

        viewModel.updateProfileResponse.observe(this){
            if(it is Resource.Success){
                Snackbar.make(binding.root, "Profile updated", Snackbar.LENGTH_SHORT).show()
                viewModel.checkCredential()
            }else if(it is Resource.Failure){
                Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show()
            }
        }

        viewModel.checkCredentialResponse.observe(this){
            if(it is Resource.Success){
                viewModel.saveUserInfo(it.value)
                finish()
            }else if(it is Resource.Failure){
                Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show()
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_REQUEST_CODE) {
            data?.data?.let { uri ->
                uploadFile(uri)
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }
    private fun uploadFile(uri: Uri) {
        lifecycleScope.launch {
            Log.d("MyActivity", "on start upload file")
            Log.d("MyActivity", "uri: $uri")
            val stream = contentResolver.openInputStream(uri) ?: return@launch
            Log.d("MyActivity", "stream: $stream")
            val request = RequestBody.create("image/*".toMediaTypeOrNull(), stream.readBytes()) // read all bytes using kotlin extension
            Log.d("MyActivity", "request: $request")
            val filePart = MultipartBody.Part.createFormData(
                "file",
                "test.jpg",
                request
            )
            Log.d("MyActivity", "filePart: $filePart")
            try {
                viewModel.uploadImage(filePart)
            }
            catch (e: Exception) {
                Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show()
                return@launch
            }
            Log.d("MyActivity", "on finish upload file")
        }
    }

    fun editPhoto(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    private fun checkPermission() {
        Log.d("EditProfileActivity", "checkPermission: called")
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("EditProfileActivity", "checkPermission: not granted")
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PICK_IMAGE_REQUEST_CODE
            )
        } else {
            Log.d("EditProfileActivity", "checkPermission: granted")
            // Permission already granted
            // You can proceed with image selection
            selectImage()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PICK_IMAGE_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission granted
            selectImage()
        } else {
            // Permission denied
            // Handle accordingly, e.g., show a message to the user
        }
    }

    private fun selectImage() {
        Log.d("EditProfileActivity", "selectImage: called")
        // Implement your image selection logic here
        // You can use an image picker library or Intent.ACTION_PICK
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"
        startActivityForResult(pickIntent, PICK_IMAGE_REQUEST_CODE)
//        val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

//    private fun uploadImage() {
//        Log.d("EditProfileActivity", "uploadImage: called")
//        val file = getFileFromUri(selectedImageUri)
//        Log.d("EditProfileActivity", "uploadImage: $file")
//
//        val requestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
//        Log.d("EditProfileActivity", "uploadImage: $requestBody")
//
//        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestBody)
//        Log.d("EditProfileActivity", "uploadImage: $imagePart")
//
//        viewModel.uploadImage(imagePart)
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
//            selectedImageUri = data.data!!
//            Log.d("EditProfileActivity", "onActivityResult: $data")
////            uploadImage()
//        }
//    }

//    private fun getFileFromUri(uri: Uri): File {
//        // Implement logic to get the file from the Uri
//        // This can vary depending on the Android version and the image picker used
//        // You may need to use a ContentResolver and a query to get the real path
//        // from the Uri
//        Log.d("EditProfileActivity", "getFileFromUri: called ${uri.path}")
//        return File(uri.path!!)
//    }
}