package com.example.finarkcontests

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.TableLayout
import android.widget.TableRow

data class Contest(
    val contestId: String,
    val contestName: String,
    val createdDate: String,
    val contestStatus: String
)

class ContestAdapter : RecyclerView.Adapter<ContestAdapter.ContestViewHolder>() {

    private var contests: List<Contest> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_contestshome, parent, false)
        return ContestViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContestViewHolder, position: Int) {
        val contest = contests[position]
        holder.bind(contest)
    }

    override fun getItemCount(): Int {
        return contests.size
    }

    fun submitList(contests: List<Contest>) {
        this.contests = contests
        notifyDataSetChanged()
    }

    inner class ContestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(contest: Contest) {
            // No need to bind views here as we're not using RecyclerView
        }
    }
}

class contestshome : AppCompatActivity() {
    private lateinit var contestAdapter: ContestAdapter
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

                            val contestIdTextView = TextView(this@contestshome)
                            contestIdTextView.text = contest.contestId
                            contestIdTextView.layoutParams = TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                            )

                            val contestStatusTextView = TextView(this@contestshome)
                            contestStatusTextView.text = contest.contestStatus
                            contestStatusTextView.layoutParams = TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                            )

                            tableRow.addView(contestNameTextView)
                            tableRow.addView(contestIdTextView)
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
