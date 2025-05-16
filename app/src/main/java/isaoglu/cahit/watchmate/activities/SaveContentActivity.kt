package isaoglu.cahit.watchmate.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import isaoglu.cahit.watchmate.R
import isaoglu.cahit.watchmate.data.AppDatabase
import isaoglu.cahit.watchmate.data.Content
import kotlinx.coroutines.launch

class SaveContentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_content)

        val etContentName = findViewById<EditText>(R.id.etContentName)
        val btnSave = findViewById<Button>(R.id.btnSave)

        val db = AppDatabase.getDatabase(this)
        val contentDao = db.contentDao()

        btnSave.setOnClickListener {
            val contentName = etContentName.text.toString().trim()

            if (contentName.isNotEmpty()) {
                val newContent = Content(name = contentName, rating = 0.0f)

                lifecycleScope.launch {
                    contentDao.insertContent(newContent)
                    runOnUiThread {
                        Toast.makeText(this@SaveContentActivity, "$contentName saved successfully", Toast.LENGTH_SHORT).show()
                        etContentName.text.clear()
                    }
                }

            } else {
                Toast.makeText(this, "Please enter a content name", Toast.LENGTH_SHORT).show()
            }
        }
    }
}