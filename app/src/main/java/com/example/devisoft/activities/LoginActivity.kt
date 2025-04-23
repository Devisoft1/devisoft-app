package com.example.devisoft.activities  // Updated package name

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.devisoft.R  // Reference to new R file
import com.example.devisoft.utils.PrefManager  // Updated reference to new package
import com.example.devisoft.models.LoginResponse  // Updated reference to new package
import com.example.devisoft.network.ApiService  // Updated reference to new package
import com.example.devisoft.utils.Constants  // Updated reference to new package
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java


class  LoginActivity : AppCompatActivity() {

    private lateinit var edtUsername: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnInstagram: Button  // Add Instagram button
    private lateinit var btnFacebook: Button  // Add Facebook button
    private lateinit var btnDeviSoft: Button  // Add DeviSoft button
    private lateinit var btnForgetPassword: Button  // Add DeviSoft button

    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtUsername = findViewById(R.id.edtUsername)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnInstagram = findViewById(R.id.btnInstagram)  // Initialize Instagram button
        btnFacebook = findViewById(R.id.btnFacebook)  // Initialize Facebook button
        btnDeviSoft = findViewById(R.id.btnDevisoft)  // Initialize DeviSoft button

        prefManager = PrefManager(this)

        // Login button click event
        btnLogin.setOnClickListener {
            val username = edtUsername.text.toString()
            val password = edtPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            } else {
                // Call the login API
                loginUser(username, password)
            }
        }

        // Instagram button click event
        btnInstagram.setOnClickListener {
            openInstagram()  // Open Instagram URL
        }

        // Facebook button click event
        btnFacebook.setOnClickListener {
            openFacebook()  // Open Facebook URL
        }

        // DeviSoft button click event
        btnDeviSoft.setOnClickListener {
            openDeviSoft()  // Open DeviSoft URL
        }
    }

    private fun loginUser(username: String, password: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)  // Use the constant for the base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // Make the login API request
        val call = apiService.login(username, password)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!

                    // Save the login response data in SharedPreferences
                    prefManager.saveUserData(
                        loginResponse.accessToken,
                        loginResponse.data.id,
                        loginResponse.data.companyName
                    )

                    // Navigate to the Year Selection Page  Activity
                    val intent = Intent(this@LoginActivity, YearSelectionActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
                t.printStackTrace()
            }

        })
    }

    // Function to open Instagram URL
    private fun openInstagram() {
        val instagramUrl = "https://www.instagram.com/devisoft/"  // Instagram URL
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl))
        startActivity(intent)  // Launch Instagram URL
    }

    // Function to open Facebook URL
    private fun openFacebook() {
        val facebookUrl = "https://www.facebook.com/devisoft.devisoft/"  // Facebook URL
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl))
        startActivity(intent)  // Launch Facebook URL
    }

    // Function to open DeviSoft URL
    private fun openDeviSoft() {
        val devisoftUrl = "https://devisoft.co.in/"  // DeviSoft URL
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(devisoftUrl))
        startActivity(intent)  // Launch DeviSoft URL
    }
}
