package com.example.finarkcontests

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class User(
    val phoneNumber: String,
    val password: String
)

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signUpButton: Button = findViewById(R.id.button2)
        signUpButton.setOnClickListener {
            val intent = Intent(this, signuppage::class.java)
            startActivity(intent)
        }

        val loginButton: Button = findViewById(R.id.button)
        loginButton.setOnClickListener {
            val phoneNumberEditText: EditText = findViewById(R.id.editTextNumber)
            val passwordEditText: EditText = findViewById(R.id.editTextPassword)

            val phoneNumber = phoneNumberEditText.text.toString()
            val password = passwordEditText.text.toString()

            fetchUsersAndCheckCredentials(phoneNumber, password)
        }
    }

    private fun fetchUsersAndCheckCredentials(phoneNumber: String, password: String) {
        val call: Call<List<User>> = RetrofitClient.apiService.getUsers()

        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val users = response.body()
                    if (users != null && checkCredentials(users, phoneNumber, password)) {
                        val intent = Intent(this@MainActivity, contestshome::class.java)
                        startActivity(intent)
                    } else {
                        showIncorrectDetailsDialog()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to fetch users", Toast.LENGTH_SHORT).show()
                    Log.e("API_ERROR", "Response code: ${response.code()}, Message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("API_ERROR", "Network error: ${t.message}")
            }
        })
    }

    private fun checkCredentials(users: List<User>, phoneNumber: String, password: String): Boolean {
        return users.any { it.phoneNumber == phoneNumber && it.password == password }
    }

    private fun showIncorrectDetailsDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Incorrect Details")
            setMessage("The phone number or password you entered is incorrect.")
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }
}
