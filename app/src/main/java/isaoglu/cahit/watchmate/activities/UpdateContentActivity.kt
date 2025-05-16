package isaoglu.cahit.watchmate.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.slider.Slider
import isaoglu.cahit.watchmate.R
import isaoglu.cahit.watchmate.data.AppDatabase
import isaoglu.cahit.watchmate.data.Content
import kotlinx.coroutines.launch

class UpdateContentActivity : AppCompatActivity() {

    private var contentId: Int = -1
    private lateinit var content: Content

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_content)

        contentId = intent.getIntExtra("contentId", -1)
        if (contentId == -1) {
            Toast.makeText(this, "Invalid content", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val etName = findViewById<EditText>(R.id.etContentName)
        val slider = findViewById<Slider>(R.id.sliderRating)
        val tvRating = findViewById<TextView>(R.id.tvRatingDisplay)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)

        val db = AppDatabase.getDatabase(this)
        val dao = db.contentDao()

        lifecycleScope.launch {
            content = dao.getContentById(contentId)
            runOnUiThread {
                etName.setText(content.name)
                slider.value = content.rating
                tvRating.text = "Rating: ${String.format("%.1f", content.rating)}"
            }
        }

        slider.addOnChangeListener { _, value, _ ->
            val rounded = String.format("%.1f", value)
            tvRating.text = "Rating: $rounded"
        }

        btnUpdate.setOnClickListener {
            val newName = etName.text.toString().trim()
            val newRating = slider.value
            if (newName.isEmpty()) {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                content.name = newName
                content.rating = newRating
                dao.updateContent(content)
                runOnUiThread {
                    Toast.makeText(this@UpdateContentActivity, "Content updated", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}
