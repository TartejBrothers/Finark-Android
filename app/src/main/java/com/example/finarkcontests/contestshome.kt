package com.example.finarkcontests

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import android.app.DatePickerDialog
import android.view.LayoutInflater

import androidx.appcompat.app.AlertDialog

import java.text.SimpleDateFormat
import java.util.*

data class Contest(
    val contestId: String,
    val contestName: String,
    val createdDate: String,
    val contestStatus: String
)

class contestshome : AppCompatActivity() {
    private lateinit var tableLayout: TableLayout
    private lateinit var searchEditText: TextInputEditText
    private var contestsList: List<Contest> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contestshome)

        // Initialize views
        val signButton: ImageView = findViewById(R.id.imageView4)
        signButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        tableLayout = findViewById(R.id.tableLayout)
        searchEditText = findViewById(R.id.searchEditText)
        val filterButton: Button = findViewById(R.id.button3)

        // Fetch contests data from API
        fetchContests()

        // Add TextWatcher to the search EditText
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterContests(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Set filter button click listener
        filterButton.setOnClickListener {
            showFilterPopup()
        }
    }

    private fun fetchContests() {
        val service = RetrofitClient.apiService
        val call = service.getContests()

        call.enqueue(object : Callback<List<Contest>> {
            override fun onResponse(call: Call<List<Contest>>, response: Response<List<Contest>>) {
                if (response.isSuccessful) {
                    contestsList = response.body() ?: listOf()
                    displayContests(contestsList)
                } else {
                    Toast.makeText(applicationContext, "Failed to fetch contests", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Contest>>, t: Throwable) {
                Toast.makeText(applicationContext, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayContests(contests: List<Contest>) {
        tableLayout.removeAllViews()
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val quarterScreenWidth = screenWidth / 4

        // Table Header
        val headerRow = TableRow(this)
        val headerParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        headerRow.layoutParams = headerParams
        headerRow.setPadding(8, 8, 8, 8)

        val headers = listOf("Contest ID", "Contest Name", "Contest Date", "Contest Status")
        headers.forEach { headerText ->
            val headerTextView = TextView(this)
            headerTextView.text = headerText
            headerTextView.layoutParams = TableRow.LayoutParams(
                quarterScreenWidth,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            headerTextView.gravity = Gravity.CENTER
            headerTextView.setPadding(8, 8, 8, 8)
            headerTextView.setTextSize(11f)
            headerTextView.setTypeface(null, android.graphics.Typeface.BOLD)
            headerRow.addView(headerTextView)
        }

        tableLayout.addView(headerRow)

        for (contest in contests) {
            val tableRow = TableRow(this)
            val layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            tableRow.layoutParams = layoutParams
            tableRow.setPadding(8, 8, 8, 8)

            val contestIdTextView = TextView(this)
            contestIdTextView.text = contest.contestId
            contestIdTextView.layoutParams = TableRow.LayoutParams(
                quarterScreenWidth,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            contestIdTextView.gravity = Gravity.CENTER
            contestIdTextView.setPadding(8, 8, 8, 8)
            contestIdTextView.setTextSize(11f)

            val contestNameTextView = TextView(this)
            contestNameTextView.text = contest.contestName
            contestNameTextView.layoutParams = TableRow.LayoutParams(
                quarterScreenWidth,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            contestNameTextView.gravity = Gravity.CENTER
            contestNameTextView.setPadding(8, 8, 8, 8)
            contestNameTextView.setTextSize(11f)

            val contestDateTextView = TextView(this)
            val dateOnly = contest.createdDate.split("T")[0]
            contestDateTextView.text = dateOnly
            contestDateTextView.layoutParams = TableRow.LayoutParams(
                quarterScreenWidth,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            contestDateTextView.gravity = Gravity.CENTER
            contestDateTextView.setPadding(8, 8, 8, 8)
            contestDateTextView.setTextSize(11f)

            val contestStatusTextView = TextView(this)
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

    private fun filterContests(query: String) {
        val filteredList = contestsList.filter {
            it.contestName.contains(query, ignoreCase = true)
        }
        displayContests(filteredList)
    }

    private fun showFilterPopup() {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.activity_filter_popup, null)
        val statusRadioGroup: RadioGroup = view.findViewById(R.id.statusRadioGroup)
        val startDateEditText: EditText = view.findViewById(R.id.startDateEditText)
        val endDateEditText: EditText = view.findViewById(R.id.endDateEditText)
        val applyFilterButton: Button = view.findViewById(R.id.applyFilterButton)

        val startDateCalendar = Calendar.getInstance()
        val endDateCalendar = Calendar.getInstance()

        startDateEditText.setOnClickListener {
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                startDateCalendar.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                startDateEditText.setText(dateFormat.format(startDateCalendar.time))
            }, startDateCalendar.get(Calendar.YEAR), startDateCalendar.get(Calendar.MONTH),
                startDateCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        endDateEditText.setOnClickListener {
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                endDateCalendar.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                endDateEditText.setText(dateFormat.format(endDateCalendar.time))
            }, endDateCalendar.get(Calendar.YEAR), endDateCalendar.get(Calendar.MONTH),
                endDateCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .create()

        applyFilterButton.setOnClickListener {
            val selectedStatus = when (statusRadioGroup.checkedRadioButtonId) {
                R.id.activeRadioButton -> "active"
                R.id.endedRadioButton -> "ended"
                else -> null
            }

            val startDate = startDateEditText.text.toString()
            val endDate = endDateEditText.text.toString()

            filterContestsByStatusAndDate(selectedStatus, startDate, endDate)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun filterContestsByStatusAndDate(status: String?, startDate: String, endDate: String) {
        val filteredList = contestsList.filter {
            (status == null || it.contestStatus.equals(status, ignoreCase = true)) &&
                    (startDate.isEmpty() || it.createdDate.split("T")[0] >= startDate) &&
                    (endDate.isEmpty() || it.createdDate.split("T")[0] <= endDate)
        }
        displayContests(filteredList)
    }
}
