package isaoglu.cahit.watchmate.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.slider.Slider
import androidx.appcompat.app.AppCompatActivity
import isaoglu.cahit.watchmate.data.AppDatabase
import isaoglu.cahit.watchmate.data.Content
import androidx.lifecycle.lifecycleScope
import isaoglu.cahit.watchmate.R
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
            val formatted = String.format("%.1f", value)
            tvRating.text = "Rating: $formatted"
        }

        btnRate.setOnClickListener {
            val contentName = etContentName.text.toString().trim()
            val rating = slider.value

            if (contentName.isNotEmpty()) {
                val newContent = Content(name = contentName, rating = rating)

                lifecycleScope.launch {
                    val db = AppDatabase.getDatabase(this@RateContentActivity)
                    db.contentDao().insertContent(newContent)

                    runOnUiThread {
                        val formattedRating = String.format("%.1f", rating)
                        Toast.makeText(this@RateContentActivity, "$contentName was rated $formattedRating", Toast.LENGTH_SHORT).show()
                        etContentName.text.clear()
                        slider.value = 5f
                        tvRating.text = "Rating: 5.0"
                    }
                }
            } else {
                Toast.makeText(this, "Please enter a content name", Toast.LENGTH_SHORT).show()
            }
        }

    }
}