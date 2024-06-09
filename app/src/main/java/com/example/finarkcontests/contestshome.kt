package com.example.finarkcontests

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.view.Gravity

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
                        for (contest in it) {
                            val tableRow = TableRow(this@contestshome)
                            val layoutParams = TableRow.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                            )
                            tableRow.layoutParams = layoutParams

                            val contestNameTextView = TextView(this@contestshome)
                            contestNameTextView.text = contest.contestName
                            contestNameTextView.layoutParams = TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                            )
                            contestNameTextView.gravity = Gravity.CENTER

                            val contestIdTextView = TextView(this@contestshome)
                            contestIdTextView.text = contest.contestId
                            contestIdTextView.layoutParams = TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,

                                TableRow.LayoutParams.WRAP_CONTENT
                            )
                            contestIdTextView.gravity = Gravity.CENTER

                            val contestDateTextView = TextView(this@contestshome)
                            val dateOnly = contest.createdDate.split("T")[0]
                            contestDateTextView.text = dateOnly
                            contestDateTextView.layoutParams = TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                            )
                            contestDateTextView.gravity = Gravity.CENTER

                            val contestStatusTextView = TextView(this@contestshome)
                            contestStatusTextView.text = contest.contestStatus
                            contestStatusTextView.layoutParams = TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                            )
                            contestStatusTextView.gravity = Gravity.CENTER

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
