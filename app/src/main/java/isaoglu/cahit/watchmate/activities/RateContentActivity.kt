package isaoglu.cahit.watchmate.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.slider.Slider
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import isaoglu.cahit.watchmate.R
import isaoglu.cahit.watchmate.data.AppDatabase
import isaoglu.cahit.watchmate.data.Content
import kotlinx.coroutines.launch

class RateContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate_content)

        val etContentName = findViewById<EditText>(R.id.etContentName)
        val slider = findViewById<Slider>(R.id.sliderRating)
        val tvRating = findViewById<TextView>(R.id.tvRatingDisplay)
        val btnRate = findViewById<Button>(R.id.btnRate)

        slider.addOnChangeListener { _, value, _ ->
            tvRating.text = "Rating: ${String.format("%.1f", value)}"
        }

        btnRate.setOnClickListener {
            val contentName = etContentName.text.toString().trim()
            val rating = slider.value

            if (contentName.isEmpty()) {
                Toast.makeText(this, "Please enter a content name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newContent = Content(name = contentName, rating = rating)

            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(this@RateContentActivity)
                db.contentDao().insertContent(newContent)

                runOnUiThread {
                    Toast.makeText(
                        this@RateContentActivity,
                        "$contentName was rated ${String.format("%.1f", rating)}",
                        Toast.LENGTH_SHORT
                    ).show()
                    etContentName.text.clear()
                    slider.value = 5f
                    tvRating.text = "Rating: 5.0"
                }
            }
        }
    }
}
