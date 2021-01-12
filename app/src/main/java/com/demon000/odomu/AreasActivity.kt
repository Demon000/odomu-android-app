package com.demon000.odomu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.demon000.odomu.dependencies.DependencyLocator
import com.demon000.odomu.models.Area
import com.demon000.odomu.services.SocketEvent
import com.demon000.odomu.utils.Constants.Companion.AREA_ID_EXTRA_NAME
import kotlinx.android.synthetic.main.activity_areas.*
import kotlinx.android.synthetic.main.activity_areas.topAppBar


class AreasActivity : AppCompatActivity() {
    private var areaCategories = HashMap<Number, String>()
    private var areas = ArrayList<Area>()
    private var adapter = AreasAdapter(areas, areaCategories)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_areas)


        val layoutManager = LinearLayoutManager(this)
        areasRecyclerView.layoutManager = layoutManager
        areasRecyclerView.adapter = adapter

        adapter.cardClickObserver.observe(this) { areaId ->
            val intent = Intent(this, AreaDetailsActivity::class.java)
            intent.putExtra(AREA_ID_EXTRA_NAME, areaId)
            startActivity(intent)
        }

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logout -> {
                    onLogoutMenuItemClick()
                }
                R.id.addArea -> {
                    onAddAreaMenuItemClick()
                }
                else -> false
            }
        }

        DependencyLocator.notificationService.socket.on("area-added") {
            onAreasChange()
        }

        DependencyLocator.notificationService.socket.on("area-updated") {
            onAreasChange()
        }

        DependencyLocator.notificationService.socket.on("area-deleted") {
            onAreasChange()
        }
    }

    fun onAreasChange() {
        runOnUiThread {
            loadAreas()
        }
    }

    override fun onStart() {
        super.onStart()

        loadAreaCategories()
        loadAreas()
    }

    fun loadAreas() {
        DependencyLocator.areaService.getAreas().observe(this) { areas ->
            if (areas == null) {
                Toast.makeText(this, "Failed to get areas", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Areas get success", Toast.LENGTH_SHORT).show()
                this.areas.clear()
                this.areas.addAll(areas)
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun loadAreaCategories() {
        DependencyLocator.areaService.getAreaCategories().observe(this) { areaCategories ->
            if (areaCategories == null) {
                Toast.makeText(this, "Failed to get area categories", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Area categories get success", Toast.LENGTH_SHORT).show()
                this.areaCategories.putAll(areaCategories)
                adapter.notifyDataSetChanged()

            }
        }
    }

    fun onAddAreaMenuItemClick(): Boolean {
        startActivity(Intent(this, AreaEditActivity::class.java))
        return true
    }

    fun onLogoutMenuItemClick(): Boolean {
        DependencyLocator.userService.logoutUser()
        startActivity(Intent(this, LoginActivity::class.java))
        return true
    }
}
