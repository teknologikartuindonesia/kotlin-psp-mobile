package id.co.pspmobile.ui.HomeBottomNavigation.message

import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import id.co.pspmobile.databinding.ActivityNotificationMessageDetailBinding
import id.co.pspmobile.data.network.responses.activebroadcast.Content
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date


class NotificationMessageDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationMessageDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotificationMessageDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent?.extras
        val c = intent?.getString("content")
        val content = c?.let { Gson().fromJson(it, Content::class.java) }

        Log.d("NotificationMessageDetailActivity", "onCreate: $content")

        binding.txtNotificationMessageTitle.text = content?.title
        // 2023-11-23T14:08:11.801000 to "dd-MM-yyyy HH:mm"
        val inputDateString = content?.process

        // Parse the input date string
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        val date: Date
        try {
            date = inputFormat.parse(inputDateString)!!
            // Format the date to the desired format
            val outputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
            val outputDateString = outputFormat.format(date)

            binding.txtNotificationMessageDate.text = outputDateString
        } catch (e: ParseException) {
            e.printStackTrace()
            binding.txtNotificationMessageDate.text = inputDateString
        }

        if (content?.content?.isNotEmpty() == true) {
            val replace = content.message.replace("*****", content.content)
            binding.txtNotificationMessageDescription.text = Html.fromHtml(replace)
        } else {
            binding.txtNotificationMessageDescription.text = content?.message
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

    }

}