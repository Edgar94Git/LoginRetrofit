package com.example.loginretrofit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.loginretrofit.databinding.ActivityMainBinding
import com.example.loginretrofit.retrofit.LoginResponse
import com.example.loginretrofit.retrofit.LoginService
import com.example.loginretrofit.retrofit.UserInfo
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.swType.setOnCheckedChangeListener { button, checked ->
            button.text = if (checked) getString(R.string.main_type_login)
            else getString(R.string.main_type_register)

            mBinding.btnLogin.text = button.text
        }

        mBinding.btnLogin.setOnClickListener {
            login()
        }

        mBinding.btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun login() {

        val email = mBinding.etEmail.text.toString().trim()
        val password = mBinding.etPassword.text.toString().trim()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(LoginService::class.java)

        lifecycleScope.launch{
            try {
                val result = service.loginUser(UserInfo(email, password))
                updateUI("${Constants.TOKEN_PROPERTY}: ${result.token}")
            }
            catch (e: Exception){
                (e as? HttpException)?.let {
                    when(it.code()){
                        200 -> {
                            updateUI(getString(R.string.main_error_server))
                        }
                        else -> {
                            updateUI(getString(R.string.main_error_response))
                        }
                    }
                }
            }
        }

        /*service.login(UserInfo(email, password)).enqueue(
            object : Callback<LoginResponse>{
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    when(response.code()){
                        200 -> {
                            val result = response.body()
                            updateUI("${Constants.TOKEN_PROPERTY}: ${result?.token}")
                        }
                        400 -> {
                            updateUI(getString(R.string.main_error_server))
                        }
                        else -> {
                            updateUI(getString(R.string.main_error_response))
                        }
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("Retrofit", "Problemas con el servidor")
                }
            }
        )*/
    }

    private fun updateUI(result: String) {
        mBinding.tvResult.visibility = View.VISIBLE
        mBinding.tvResult.text = result
    }
}