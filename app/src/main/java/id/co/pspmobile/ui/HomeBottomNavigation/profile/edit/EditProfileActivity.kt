package id.co.pspmobile.ui.HomeBottomNavigation.profile.edit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.responses.checkcredential.CheckCredentialResponse
import id.co.pspmobile.data.network.responses.checkcredential.UserX
import id.co.pspmobile.data.network.responses.profile.UserResponse
import id.co.pspmobile.databinding.ActivityEditProfileBinding
import id.co.pspmobile.ui.Utils.hideKeyboard
import id.co.pspmobile.ui.Utils.hideLottieLoader
import id.co.pspmobile.ui.Utils.showLottieLoader
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST_CODE = 100
    private lateinit var selectedImageUri: Uri

    var isInputMode = false


    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel: EditProfileViewModel by viewModels()
    var currentEmail = ""
    var currentPhone = ""
    private lateinit var tempUserResponse : UserResponse
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

        currentEmail = viewModel.getUserInfo().user.email ?: ""
        currentPhone = viewModel.getUserInfo().user.phone ?: ""
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
            hideKeyboard()
            if (binding.edEditEmail.text.toString() != currentEmail || binding.edEditPhone.text.toString() != currentPhone) {
                val body = viewModel.getUserInfo()
                Log.d("EditProfileActivity", "body: $body")
                body.user.email = binding.edEditEmail.text.toString()
                body.user.phone = binding.edEditPhone.text.toString()
                updateProfile(binding.edEditEmail.text.toString(), binding.edEditPhone.text.toString())
                Log.d("EditProfileActivity", "body: $body")
            }else{
                finish()
            }
        }

        viewModel.updateProfileResponse.observe(this){
            when(it is Resource.Loading){
                true -> {
                    showLottieLoader(supportFragmentManager)
                }
                false -> {
                    hideLottieLoader(supportFragmentManager)
                }
            }
            if(it is Resource.Success){
                Snackbar.make(binding.root, "Profile updated", Snackbar.LENGTH_SHORT).show()
                viewModel.checkCredential()
            }else if(it is Resource.Failure){
                Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show()
            }
        }

        viewModel.checkCredentialResponse.observe(this){
            when(it is Resource.Loading){
                true -> {
                    showLottieLoader(supportFragmentManager)
                }
                false -> {
                    hideLottieLoader(supportFragmentManager)
                }
            }
            if(it is Resource.Success){
                viewModel.saveUserInfo(it.value)
                finish()
            }else if(it is Resource.Failure){
                Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show()
            }

        }

        viewModel.uploadImageResponse.observe(this){
            when(it is Resource.Loading){
                true -> {
                    showLottieLoader(supportFragmentManager)
                }
                false -> {
                    hideLottieLoader(supportFragmentManager)
                }
            }
            if(it is Resource.Success){
                updatePhotoProfile(it.value.name)
            }else if(it is Resource.Failure){
                Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show()
            }
        }

        viewModel.userResponse.observe(this){
            when(it is Resource.Loading){
                true -> {
                    showLottieLoader(supportFragmentManager)
                }
                false -> {
                    hideLottieLoader(supportFragmentManager)
                }
            }
            if(it is Resource.Success){
                tempUserResponse = it.value


                if(!it.value.email.isNullOrEmpty()){
                    currentEmail = it.value.email
                }

                if (!it.value.phone.isNullOrEmpty()){
                    currentPhone = it.value.phone
                }

                binding.edEditEmail.setText(currentEmail)
                binding.edEditPhone.setText(currentPhone)
                val url = "${viewModel.getBaseUrl()}/main_a/image/get/${it.value.photoUrl}/pas"
                Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.ic_profile_profile)
                    .into(binding.imgProfile)

            }else if(it is Resource.Failure){
                Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show()
            }
        }

        getUserUpdateProfile()
    }

    fun getUserUpdateProfile(){
        viewModel.getUserInfoEditProfile()
    }
//    getUserData: {"activeCompany":{"activeUntil":" ","adminManualTopUp":1000.0,"adminManualTopUpMerchant":1000.0,"adminManualTopUpSchool":2000.0,"autoBanksTrx":[],"banks":["BRI","BNI","MUAMALAT","IDN","BSI","BANKJATIM"],"cardSetting":{"limitChange":true,"limitDaily":10000.0,"limitMax":10000.0,"usedPin":true},"companyCode":"0153","customApps":true,"id":"5f83b9fa28f6ab6e77b810d4","laundry":true,"menus":[],"name":"DEMO TEKNOLOGI KARTU INDONESIA HEBAT TKI","solution":[],"suspensionStatus":"NOT_SUSPEND","topUpManual":true},"companies":[{"address":"SALATIGA KOTA 50722, -, -","companyCode":"0153","id":"5f83b9fa28f6ab6e77b810d4","name":"DEMO TEKNOLOGI KARTU INDONESIA HEBAT TKI"}],"firstLogin":false,"tags":["santri","siswa","cobapspinfo","bobtest","7a","jadwal_7a","seol"],"user":{"accounts":[{"accountNumber":"015300000219","active":true,"callerIdentities":[{"callerId":"170918","id":"615a6aa71a7f287bd616ef94","name":"ANAK BOB DEMO PROD","photoUrl":"2023-06-12-dmtbdggo-","tags":["siswa","bobtest","7a","jadwal_7a"],"title":"NIS"},{"callerId":"6352","id":"61aec330ce7e4b2d81b720df","name":"REZA ALFIAN DIMAS","photoUrl":"","tags":["santri","siswa","cobapspinfo"],"title":"NIS"},{"callerId":"99199","id":"61c532be4365aa324e9baba3","name":"SEOL","photoUrl":"2023-06-12-qpszwkcl-","tags":["seol"],"title":"NIS"}],"companyId":"5f83b9fa28f6ab6e77b810d4","id":"60e277d49f73a5459dab99b1","lastLogin":"2023-11-26 11:10:16","note":"","roles":["ROLE_USER"],"sourceOfFund":{"accountNumber":"","key":"","type":"KATALIS"},"transactionUnlimited":true,"vaNumbers":[{"bankId":"5d15bf7607821e31957cbd4f","bankName":"BNI","userName":"BOB","vaNumber":"8612015300000219"},{"bankId":"600a230e91d45b73bbae9ec5","bankName":"BANKJATIM","userName":"BOB DEMO PROD","vaNumber":"1604100000219"},{"bankId":"6103c94e9470534b4d5d0f07","bankName":"BSI","userName":"BOB DEMO PROD","vaNumber":"900523234567890"},{"bankId":"61e668396016ff1105983711","bankName":"XENDIT BRI","userName":"BOB DEMO PROD","vaNumber":"13281913780067890"},{"bankId":"62a181924f39422250f52f01","bankName":"MUAMALAT","userName":"BOB DEMO PROD","vaNumber":"7539020000006352"}]}],"address":"ISMOYO 26 ST.","banks":[],"dateOfBirth":"2021-01-12","email":"bob@test.com","firebase":{"serverKeyId":"645da9a1ac84fe51664bdf32","token":"e-ovBz5aQwWe4-RXmoy5U3:APA91bGF3FriKX66hxH44jYgHYfQM9phr82fvSZmqG831I2hdXTaCqcyT-L5-haG3S5PRKifMhvopav8KUeDln3H6gY3A6edonSzleXRrKXP2DQ51yq2lAAw9fhMFfnAFeWUtBDaWW3o"},"gender":"MALE","id":"60e277d49f73a5459dab99b4","maritalStatus":"SINGLE","name":"BOB DEMO PROD","nik":"","openfire":{"active":false,"id":"","password":""},"phone":"081225951789","photoUrl":"2023-11-23-biojhhad-","placeOfBirth":"KASUR","regDate":"2021-07-05 10:09:08","religion":"OTHER","socmedAccounts":[{"account":"","media":"facebook"},{"account":"","media":"instagram"},{"account":"","media":"twitter"}],"validationStatus":true}}
//    12:27:39.553  D  body: CheckCredentialResponse(activeCompany=ActiveCompany(activeUntil= , adminManualTopUp=1000.0, adminManualTopUpMerchant=1000.0, adminManualTopUpSchool=2000.0, autoBanksTrx=[], banks=[BRI, BNI, MUAMALAT, IDN, BSI, BANKJATIM], cardSetting=CardSetting(limitChange=true, limitDaily=10000.0, limitMax=10000.0, usedPin=true), companyCode=0153, customApps=true, id=5f83b9fa28f6ab6e77b810d4, laundry=true, menus=[], name=DEMO TEKNOLOGI KARTU INDONESIA HEBAT TKI, solution=[], suspensionStatus=NOT_SUSPEND, topUpManual=true), companies=[Company(address=SALATIGA KOTA 50722, -, -, companyCode=0153, id=5f83b9fa28f6ab6e77b810d4, name=DEMO TEKNOLOGI KARTU INDONESIA HEBAT TKI)], firstLogin=false, tags=[santri, siswa, cobapspinfo, bobtest, 7a, jadwal_7a, seol], user=UserX(accounts=[Account(accountNumber=015300000219, active=true, callerIdentities=[CallerIdentity(callerId=170918, id=615a6aa71a7f287bd616ef94, name=ANAK BOB DEMO PROD, photoUrl=2023-06-12-dmtbdggo-, tags=[siswa, bobtest, 7a, jadwal_7a], title=NIS), CallerIdentity(callerId=6352, id=61aec330ce7e4b2d81b720df, name=REZA ALFIAN DIMAS, photoUrl=, tags=[santri, siswa, cobapspinfo], title=NIS), CallerIdentity(callerId=99199, id=61c532be4365aa324e9baba3, name=SEOL, photoUrl=2023-06-12-qpszwkcl-, tags=[seol], title=NIS)], companyId=5f83b9fa28f6ab6e77b810d4, id=60e277d49f73a5459dab99b1, lastLogin=2023-11-26 11:10:16, note=, roles=[ROLE_USER], sourceOfFund=SourceOfFund(accountNumber=, key=, type=KATALIS), transactionUnlimited=true, vaNumbers=[VaNumber(bankId=5d15bf7607821e31957cbd4f, bankName=BNI, userName=BOB, vaNumber=8612015300000219), VaNumber(bankId=600a230e91d45b73bbae9ec5, bankName=BANKJATIM, userName=BOB DEMO PROD, vaNumber=1604100000219), VaNumber(bankId=6103c94e9470534b4d5d0f07, bankName=BSI, userName=BOB DEMO PROD, vaNumber=900523234567890), VaNumber(bankId=61e668396016ff1105983711, bankName=XENDIT BRI, userName=BOB DEMO PROD, vaNumber=13281913780067890), VaNumber(bankId=62a181924f39422250f52f01, bankName=MUAMALAT, userName=BOB DEMO PROD, vaNumber=7539020000006352)])], address=ISMOYO 26 ST., banks=[], dateOfBirth=2021-01-12, email=bob@test.com, firebase=Firebase(serverKeyId=645da9a1ac84fe51664bdf32, token=e-ovBz5aQwWe4-RXmoy5U3:APA91bGF3FriKX66hxH44jYgHYfQM9phr82fvSZmqG831I2hdXTaCqcyT-L5-haG3S5PRKifMhvopav8KUeDln3H6gY3A6edonSzleXRrKXP2DQ51yq2lAAw9fhMFfnAFeWUtBDaWW3o), gender=MALE, id=60e277d49f73a5459dab99b4, maritalStatus=SINGLE, name=BOB DEMO PROD, nik=, openfire=Openfire(active=false, id=, password=), phone=081225951789, photoUrl=2023-11-23-biojhhad-, placeOfBirth=KASUR, regDate=2021-07-05 10:09:08, religion=OTHER, socmedAccounts=[SocmedAccount(account=, media=facebook), SocmedAccount(account=, media=instagram), SocmedAccount(account=, media=twitter)], tags=null, validationStatus=true))
//    12:27:39.555  D  getUserData: {"activeCompany":{"activeUntil":" ","adminManualTopUp":1000.0,"adminManualTopUpMerchant":1000.0,"adminManualTopUpSchool":2000.0,"autoBanksTrx":[],"banks":["BRI","BNI","MUAMALAT","IDN","BSI","BANKJATIM"],"cardSetting":{"limitChange":true,"limitDaily":10000.0,"limitMax":10000.0,"usedPin":true},"companyCode":"0153","customApps":true,"id":"5f83b9fa28f6ab6e77b810d4","laundry":true,"menus":[],"name":"DEMO TEKNOLOGI KARTU INDONESIA HEBAT TKI","solution":[],"suspensionStatus":"NOT_SUSPEND","topUpManual":true},"companies":[{"address":"SALATIGA KOTA 50722, -, -","companyCode":"0153","id":"5f83b9fa28f6ab6e77b810d4","name":"DEMO TEKNOLOGI KARTU INDONESIA HEBAT TKI"}],"firstLogin":false,"tags":["santri","siswa","cobapspinfo","bobtest","7a","jadwal_7a","seol"],"user":{"accounts":[{"accountNumber":"015300000219","active":true,"callerIdentities":[{"callerId":"170918","id":"615a6aa71a7f287bd616ef94","name":"ANAK BOB DEMO PROD","photoUrl":"2023-06-12-dmtbdggo-","tags":["siswa","bobtest","7a","jadwal_7a"],"title":"NIS"},{"callerId":"6352","id":"61aec330ce7e4b2d81b720df","name":"REZA ALFIAN DIMAS","photoUrl":"","tags":["santri","siswa","cobapspinfo"],"title":"NIS"},{"callerId":"99199","id":"61c532be4365aa324e9baba3","name":"SEOL","photoUrl":"2023-06-12-qpszwkcl-","tags":["seol"],"title":"NIS"}],"companyId":"5f83b9fa28f6ab6e77b810d4","id":"60e277d49f73a5459dab99b1","lastLogin":"2023-11-26 11:10:16","note":"","roles":["ROLE_USER"],"sourceOfFund":{"accountNumber":"","key":"","type":"KATALIS"},"transactionUnlimited":true,"vaNumbers":[{"bankId":"5d15bf7607821e31957cbd4f","bankName":"BNI","userName":"BOB","vaNumber":"8612015300000219"},{"bankId":"600a230e91d45b73bbae9ec5","bankName":"BANKJATIM","userName":"BOB DEMO PROD","vaNumber":"1604100000219"},{"bankId":"6103c94e9470534b4d5d0f07","bankName":"BSI","userName":"BOB DEMO PROD","vaNumber":"900523234567890"},{"bankId":"61e668396016ff1105983711","bankName":"XENDIT BRI","userName":"BOB DEMO PROD","vaNumber":"13281913780067890"},{"bankId":"62a181924f39422250f52f01","bankName":"MUAMALAT","userName":"BOB DEMO PROD","vaNumber":"7539020000006352"}]}],"address":"ISMOYO 26 ST.","banks":[],"dateOfBirth":"2021-01-12","email":"bob@test.com","firebase":{"serverKeyId":"645da9a1ac84fe51664bdf32","token":"e-ovBz5aQwWe4-RXmoy5U3:APA91bGF3FriKX66hxH44jYgHYfQM9phr82fvSZmqG831I2hdXTaCqcyT-L5-haG3S5PRKifMhvopav8KUeDln3H6gY3A6edonSzleXRrKXP2DQ51yq2lAAw9fhMFfnAFeWUtBDaWW3o"},"gender":"MALE","id":"60e277d49f73a5459dab99b4","maritalStatus":"SINGLE","name":"BOB DEMO PROD","nik":"","openfire":{"active":false,"id":"","password":""},"phone":"081225951789","photoUrl":"2023-11-23-biojhhad-","placeOfBirth":"KASUR","regDate":"2021-07-05 10:09:08","religion":"OTHER","socmedAccounts":[{"account":"","media":"facebook"},{"account":"","media":"instagram"},{"account":"","media":"twitter"}],"validationStatus":true}}
    fun updateProfile(email: String, phone: String){
        try {
            val temp = tempUserResponse
            val newUserResponse = UserResponse(
                temp.accounts, temp.address, temp.banks, temp.dateOfBirth, email, temp.firebase,
                temp.gender, temp.id, temp.maritalStatus, temp.name, temp.nik, temp.openfire,
                phone, temp.photoUrl, temp.placeOfBirth, temp.regDate, temp.religion,
                temp.socmedAccounts, temp.tags, temp.validationStatus
            )
            viewModel.updateProfile(newUserResponse)
        }catch (e: Exception){
            Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show()
        }
    }
    fun updatePhotoProfile(photoUrl: String){
        try {
            val temp = tempUserResponse
            temp.photoUrl = photoUrl
            viewModel.updateProfile(temp)
        }catch (e: Exception){
            Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_REQUEST_CODE) {
            data?.data?.let { uri ->
                uploadFile(uri)
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }
    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }
    private fun createTempFile(bitmap: Bitmap): File? {
        val file = File(
            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            System.currentTimeMillis().toString() + "_image.jpg"
        )
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitmapdata = bos.toByteArray()
        //write the bytes in file
        try {
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }
    private fun uploadFile(uri: Uri) {
        lifecycleScope.launch {

            val bitmap = MediaStore.Images.Media.getBitmap(
                this@EditProfileActivity.contentResolver,
                uri
            )

            val postPath = getResizedBitmap(bitmap,500)
            val path = postPath?.let { x -> createTempFile(x) }
            val requestFile2 =
                RequestBody.create("image/jpeg".toMediaTypeOrNull(), path!!)
            val accProfileImage =
                MultipartBody.Part.createFormData("file", path.name, requestFile2)

            try {
                viewModel.uploadImage(accProfileImage)
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
            ) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
        ) {
            Log.d("EditProfileActivity", "checkPermission: not granted")
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PICK_IMAGE_REQUEST_CODE
            )
        } else {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ){
                Log.d("EditProfileActivity", "checkPermission: not granted")
                requestPermissions(
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    PICK_IMAGE_REQUEST_CODE
                )
            }else{
                Log.d("EditProfileActivity", "checkPermission: granted")
                // Permission already granted
                // You can proceed with image selection
                selectImage()
            }
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