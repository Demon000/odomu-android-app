package com.demon000.odomu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import com.demon000.odomu.dependencies.DependencyLocator
import com.demon000.odomu.models.User
import com.demon000.odomu.models.UserLoginData
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener {
            val usernameText = usernameField.editText?.text.toString()
            val passwordText = passwordField.editText?.text.toString()

            if (usernameText.isEmpty()) {
                Toast.makeText(this, "Username cannot be empty", LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (passwordText.isEmpty()) {
                Toast.makeText(this, "Password cannot be empty", LENGTH_SHORT).show()
                return@setOnClickListener
            }

            DependencyLocator.userService.loginUser(UserLoginData(usernameText, passwordText))
                .observe(this) { user ->
                    if (user == null) {
                        Toast.makeText(this, "Failed to log in", LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Logged in", LENGTH_SHORT).show()
                        startActivity(Intent(this, AreasActivity::class.java))
                        finish()
                    }
                }
        }
    }
}
