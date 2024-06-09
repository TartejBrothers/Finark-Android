package com.example.finarkcontests

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/user")
    fun createUser(@Body userData: UserData): Call<UserResponse>
}

data class UserData(
    val name: String,
    val phoneNumber: String,
    val password: String
)

data class UserResponse(
    val success: Boolean,
    val message: String?
)

object RetrofitClient {
    private const val BASE_URL = "https://finark-backend.vercel.app/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}

class signuppage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signuppage)

        val nameEditText: EditText = findViewById(R.id.editTextName)
        val phoneNumberEditText: EditText = findViewById(R.id.editTextPhoneNumber)
        val passwordEditText: EditText = findViewById(R.id.editTextPassword)

        val signupButton: Button = findViewById(R.id.button)
        signupButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val phoneNumber = phoneNumberEditText.text.toString()
            val password = passwordEditText.text.toString()

            val userData = UserData(name, phoneNumber, password)
            Log.i("signuppage", userData.toString())

            RetrofitClient.apiService.createUser(userData).enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful) {
                        val userResponse = response.body()
                        print(userResponse)
                        if (userResponse != null && userResponse.success) {
                            Toast.makeText(applicationContext, "User created successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@signuppage, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(applicationContext, " ${userResponse?.message}", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@signuppage, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Toast.makeText(applicationContext, "Failed to create user", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
