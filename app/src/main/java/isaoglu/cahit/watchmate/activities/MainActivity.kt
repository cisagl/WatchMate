package isaoglu.cahit.watchmate.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import isaoglu.cahit.watchmate.R

class MainActivity : AppCompatActivity() {

    private fun toggleVisibility(view: View) {
        if (view.visibility == View.GONE) {
            view.visibility = View.VISIBLE
            val anim = AlphaAnimation(0.0f, 1.0f)
            anim.duration = 300
            view.startAnimation(anim)
        } else {
            view.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnBrowse = findViewById<Button>(R.id.btnBrowseContents)
        val layoutBrowse = findViewById<LinearLayout>(R.id.layoutBrowseOptions)
        val btnWatchLater = findViewById<Button>(R.id.btnWatchLater)
        val btnRatedList = findViewById<Button>(R.id.btnRatedList)

        val btnAdd = findViewById<Button>(R.id.btnAddContent)
        val layoutAdd = findViewById<LinearLayout>(R.id.layoutAddOptions)
        val btnSave = findViewById<Button>(R.id.btnSaveContent)
        val btnRate = findViewById<Button>(R.id.btnRateContent)

        btnBrowse.setOnClickListener { toggleVisibility(layoutBrowse) }
        btnAdd.setOnClickListener { toggleVisibility(layoutAdd) }

        btnWatchLater.setOnClickListener {
            startActivity(Intent(this, WatchLaterActivity::class.java))
        }

        btnRatedList.setOnClickListener {
            startActivity(Intent(this, RatedContentActivity::class.java))
        }

        btnSave.setOnClickListener {
            startActivity(Intent(this, SaveContentActivity::class.java))
        }

        btnRate.setOnClickListener {
            startActivity(Intent(this, RateContentActivity::class.java))
        }
    }
}
