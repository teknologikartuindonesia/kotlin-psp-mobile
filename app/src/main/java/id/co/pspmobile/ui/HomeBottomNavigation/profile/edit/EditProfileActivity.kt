package id.co.pspmobile.ui.HomeBottomNavigation.profile.edit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.databinding.ActivityEditProfileBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST_CODE = 100
    private lateinit var selectedImageUri: Uri


    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel: EditProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.imgProfile.setOnClickListener {
            checkPermission()
        }

    }


    fun editPhoto(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PICK_IMAGE_REQUEST_CODE
                )
            } else {
                // Permission already granted
                // You can proceed with image selection
                selectImage()
            }
        } else {
            // Runtime permissions not needed for lower Android versions
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
        // Implement your image selection logic here
        // You can use an image picker library or Intent.ACTION_PICK
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    private fun uploadImage() {
        val file = getFileFromUri(selectedImageUri)

        val requestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)

        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestBody)

        viewModel.uploadImage(imagePart)
    }

    private fun getFileFromUri(uri: Uri): File {
        // Implement logic to get the file from the Uri
        // This can vary depending on the Android version and the image picker used
        // You may need to use a ContentResolver and a query to get the real path
        // from the Uri
        return File(uri.path!!)
    }
}