package isaoglu.cahit.watchmate.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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
        val seekBar = findViewById<SeekBar>(R.id.seekBarRating)
        val tvRating = findViewById<TextView>(R.id.tvRating)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)

        val db = AppDatabase.getDatabase(this)
        val dao = db.contentDao()

        lifecycleScope.launch {
            content = dao.getContentById(contentId)
            runOnUiThread {
                etName.setText(content.name)
                seekBar.progress = (content.rating * 10).toInt()
                tvRating.text = "Rating: ${String.format("%.1f", content.rating)}"
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                val rating = progress / 10.0f
                val roundedRating = (rating * 2).toInt() / 2.0f
                tvRating.text = "Rating: $roundedRating"
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        btnUpdate.setOnClickListener {
            val newName = etName.text.toString().trim()
            val newRating = (seekBar.progress / 10.0f)
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