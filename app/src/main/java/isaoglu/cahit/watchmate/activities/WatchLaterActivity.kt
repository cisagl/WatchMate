package isaoglu.cahit.watchmate.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import isaoglu.cahit.watchmate.R
import isaoglu.cahit.watchmate.data.AppDatabase
import isaoglu.cahit.watchmate.data.Content
import kotlinx.coroutines.launch

class WatchLaterActivity : AppCompatActivity() {

    private lateinit var dao: AppDatabase
    private lateinit var adapter: ArrayAdapter<String>
    private var contentList = listOf<Content>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_list)

        val listView = findViewById<ListView>(R.id.listViewContents)
        dao = AppDatabase.getDatabase(this)

        loadContents()

        listView.setOnItemLongClickListener { _, _, position, _ ->
            val content = contentList[position]
            AlertDialog.Builder(this)
                .setTitle("Delete Content")
                .setMessage("Are you sure you want to delete \"${content.name}\"?")
                .setPositiveButton("Yes") { _, _ ->
                    lifecycleScope.launch {
                        dao.contentDao().deleteContent(content)
                        runOnUiThread {
                            Toast.makeText(this@WatchLaterActivity, "\"${content.name}\" deleted", Toast.LENGTH_SHORT).show()
                            loadContents()
                        }
                    }
                }
                .setNegativeButton("No", null)
                .show()
            true
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val content = contentList[position]
            val intent = Intent(this, UpdateContentActivity::class.java)
            intent.putExtra("contentId", content.id)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadContents()
    }

    private fun loadContents() {
        lifecycleScope.launch {
            contentList = dao.contentDao().getWatchLater()
            val names = contentList.map { it.name }
            runOnUiThread {
                val listView = findViewById<ListView>(R.id.listViewContents)
                adapter = ArrayAdapter(this@WatchLaterActivity, android.R.layout.simple_list_item_1, names)
                listView.adapter = adapter
            }
        }
    }
}