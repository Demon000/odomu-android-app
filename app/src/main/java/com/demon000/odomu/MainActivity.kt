package com.demon000.odomu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demon000.odomu.dependencies.DependencyLocator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DependencyLocator.context = this.applicationContext

        startActivity(Intent(this, LoginActivity::class.java))

        finish()
    }
}
