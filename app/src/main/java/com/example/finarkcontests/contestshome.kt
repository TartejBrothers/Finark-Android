package com.example.finarkcontests
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class Contest(
    val contestId: String,
    val contestName: String,
    val createdDate: String,
    val contestStatus: String
)

class contestshome : AppCompatActivity() {
    private lateinit var tableLayout: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contestshome)
        val signButton: ImageView = findViewById(R.id.imageView4)
        signButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Initialize TableLayout
        tableLayout = findViewById(R.id.tableLayout)

        // Fetch contests data from API
        fetchContests()
    }

    private fun fetchContests() {
        val service = RetrofitClient.apiService
        val call = service.getContests()

        call.enqueue(object : Callback<List<Contest>> {
            override fun onResponse(call: Call<List<Contest>>, response: Response<List<Contest>>) {
                if (response.isSuccessful) {
                    val contests = response.body()
                    contests?.let {
                        // Create rows dynamically and populate the table
                        val displayMetrics = DisplayMetrics()
                        windowManager.defaultDisplay.getMetrics(displayMetrics)
                        val screenWidth = displayMetrics.widthPixels
                        val quarterScreenWidth = screenWidth / 4

                        for (contest in it) {
                            val tableRow = TableRow(this@contestshome)
                            val layoutParams = TableRow.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                            )
                            tableRow.layoutParams = layoutParams
                            tableRow.setPadding(8, 8, 8, 8) // Add padding to the TableRow

                            val contestIdTextView = TextView(this@contestshome)
                            contestIdTextView.text = contest.contestId
                            contestIdTextView.layoutParams = TableRow.LayoutParams(
                                quarterScreenWidth, // Set width to 25% of screen width
                                TableRow.LayoutParams.WRAP_CONTENT
                            )
                            contestIdTextView.gravity = Gravity.CENTER
                            contestIdTextView.setPadding(8, 8, 8, 8)
                            contestIdTextView.setTextSize(11f) // Set text size

                            val contestNameTextView = TextView(this@contestshome)
                            contestNameTextView.text = contest.contestName
                            contestNameTextView.layoutParams = TableRow.LayoutParams(
                                quarterScreenWidth,
                                TableRow.LayoutParams.WRAP_CONTENT
                            )
                            contestNameTextView.gravity = Gravity.NO_GRAVITY
                            contestNameTextView.setPadding(8, 8, 8, 8)
                            contestNameTextView.setTextSize(11f)

                            val contestDateTextView = TextView(this@contestshome)
                            val dateOnly = contest.createdDate.split("T")[0]
                            contestDateTextView.text = dateOnly
                            contestDateTextView.layoutParams = TableRow.LayoutParams(
                                quarterScreenWidth,
                                TableRow.LayoutParams.WRAP_CONTENT
                            )
                            contestDateTextView.gravity = Gravity.CENTER
                            contestDateTextView.setPadding(8, 8, 8, 8)
                            contestDateTextView.setTextSize(11f) // Set text size

                            val contestStatusTextView = TextView(this@contestshome)
                            contestStatusTextView.text = contest.contestStatus
                            contestStatusTextView.layoutParams = TableRow.LayoutParams(
                                quarterScreenWidth,
                                TableRow.LayoutParams.WRAP_CONTENT
                            )
                            contestStatusTextView.gravity = Gravity.CENTER
                            contestStatusTextView.setPadding(8, 8, 8, 8)
                            contestStatusTextView.setTextSize(11f)

                            tableRow.addView(contestIdTextView)
                            tableRow.addView(contestNameTextView)
                            tableRow.addView(contestDateTextView)
                            tableRow.addView(contestStatusTextView)

                            tableLayout.addView(tableRow)
                        }
                    }
                } else {
                    Toast.makeText(applicationContext, "Failed to fetch contests", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Contest>>, t: Throwable) {
                Toast.makeText(applicationContext, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
