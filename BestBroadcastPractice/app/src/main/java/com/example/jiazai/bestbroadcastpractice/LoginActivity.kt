package com.example.jiazai.bestbroadcastpractice

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by jiazai on 17-11-27.
 */
class LoginActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        accountEdit = account
        passwordEdit = password
        loginButtion = login
        loginButtion.setOnClickListener {
            val account = accountEdit.text.toString()
            val password = passwordEdit.text.toString()
            if (account.equals("admin") && password.equals("123456")) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "account or password is invalid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private lateinit var accountEdit:EditText
    private lateinit var passwordEdit:EditText
    private lateinit var loginButtion:Button
}