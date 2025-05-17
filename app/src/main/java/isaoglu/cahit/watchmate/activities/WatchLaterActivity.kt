package isaoglu.cahit.watchmate.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import isaoglu.cahit.watchmate.R
import isaoglu.cahit.watchmate.data.AppDatabase
import isaoglu.cahit.watchmate.data.Content
import kotlinx.coroutines.launch

class WatchLaterActivity : AppCompatActivity() {

    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var contentList: List<Content>
    private val dao by lazy { AppDatabase.getDatabase(this).contentDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_list)

        val searchInput = findViewById<EditText>(R.id.searchInput)
        val spinner = findViewById<Spinner>(R.id.spinnerSort)
        val listView = findViewById<ListView>(R.id.listViewContents)

        val sortOptions = arrayOf("Name", "Date")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortOptions)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        searchInput.addTextChangedListener {
            loadContents(searchInput.text.toString(), spinner.selectedItem.toString())
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                loadContents(searchInput.text.toString(), sortOptions[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val content = contentList[position]
            val intent = Intent(this, UpdateContentActivity::class.java)
            intent.putExtra("contentId", content.id)
            intent.putExtra("onlyNameUpdate", true)
            startActivity(intent)
        }

        listView.setOnItemLongClickListener { _, _, position, _ ->
            val content = contentList[position]
            AlertDialog.Builder(this)
                .setTitle("Delete Content")
                .setMessage("Are you sure you want to delete \"${content.name}\"?")
                .setPositiveButton("Yes") { _, _ ->
                    lifecycleScope.launch {
                        dao.deleteContent(content)
                        runOnUiThread {
                            Toast.makeText(this@WatchLaterActivity, "\"${content.name}\" deleted", Toast.LENGTH_SHORT).show()
                            loadContents(searchInput.text.toString(), spinner.selectedItem.toString())
                        }
                    }
                }
                .setNegativeButton("No", null)
                .show()
            true
        }

        loadContents()
    }

    override fun onResume() {
        super.onResume()
        val search = findViewById<EditText>(R.id.searchInput).text.toString()
        val sort = findViewById<Spinner>(R.id.spinnerSort).selectedItem.toString()
        loadContents(search, sort)
    }

    private fun loadContents(search: String = "", sortBy: String = "Name") {
        lifecycleScope.launch {
            contentList = when {
                search.isNotEmpty() -> dao.searchWatchLaterContents(search)
                sortBy == "Date" -> dao.getWatchLaterSortedByDate()
                else -> dao.getWatchLaterSortedByName()
            }

            val names = contentList.map { it.name }

            runOnUiThread {
                val listView = findViewById<ListView>(R.id.listViewContents)
                adapter = ArrayAdapter(this@WatchLaterActivity, android.R.layout.simple_list_item_1, names)
                listView.adapter = adapter
            }
        }
    }
}
