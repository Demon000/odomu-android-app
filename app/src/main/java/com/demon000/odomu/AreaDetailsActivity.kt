package com.demon000.odomu

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import com.demon000.odomu.dependencies.DependencyLocator
import com.demon000.odomu.models.Area
import com.demon000.odomu.services.SocketEvent
import com.demon000.odomu.utils.Constants.Companion.AREA_ID_EXTRA_NAME
import kotlinx.android.synthetic.main.activity_area_details.*

class AreaDetailsActivity : AppCompatActivity() {
    private var areaId: String? = null
    private var area: Area? = null
    private var areaCategories = HashMap<Number, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_area_details)

        areaId = intent.getStringExtra(AREA_ID_EXTRA_NAME)
        if (areaId == null) {
            Toast.makeText(this, "Area id missing", Toast.LENGTH_SHORT).show()
            return
        }

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.updateArea -> {
                    onUpdateAreaMenuItemClick()
                }
                R.id.deleteArea -> {
                    onDeleteAreaMenuItemClick()
                }
                else -> false
            }
        }

        DependencyLocator.notificationService.socket.on("area-updated") {
            onAreaUpdate()
        }
    }

    fun onAreaUpdate() {
        runOnUiThread {
            loadArea()
        }
    }

    override fun onStart() {
        super.onStart()

        loadAreaCategories()
        loadArea()
    }

    fun getAreaImage(area: Area): Bitmap? {
        if (area.image == null) {
            return null
        }

        val decodedBytes: ByteArray = Base64.decode(area.image, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    fun getAreaOwnerName(area: Area): String {
        return area.owner.firstName + " " + area.owner.lastName
    }


    fun getAreaLocationPoint(area: Area): String {
        return area.locationPoint[0].toString() + ", " + area.locationPoint[1].toString()
    }

    fun getAreaCategoryText(area: Area): String {
        return areaCategories.getOrDefault(area.category, "")
    }

    fun loadAreaCategories() {
        DependencyLocator.areaService.getAreaCategories().observe(this) { areaCategories ->
            if (areaCategories == null) {
                Toast.makeText(this, "Failed to get area categories", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Area categories get success", Toast.LENGTH_SHORT).show()
                this.areaCategories.putAll(areaCategories)
                onAreaCategoriesLoad()
            }
        }
    }

    fun loadArea() {
        DependencyLocator.areaService.getArea(areaId!!).observe(this) { area ->
            if (area == null) {
                Toast.makeText(this, "Failed to get area", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Area get success", Toast.LENGTH_SHORT).show()
                this.area = area
                onAreaLoad()
            }
        }
    }

    fun onAreaCategoriesLoad() {
        if (area == null) {
            return
        }

        areaCategoryText.text = getAreaCategoryText(area!!)
    }

    fun onAreaLoad() {
        if (area == null) {
            return
        }

        val bitmap = getAreaImage(area!!)
        if (bitmap == null) {
            areaImageView.visibility = View.GONE
        } else {
            areaImageView.setImageBitmap(getAreaImage(area!!))
        }

        topAppBar.title = area!!.name
        areaIdText.text = area!!.id
        areaLocation.text = area!!.location
        areaOwnerName.text = getAreaOwnerName(area!!)
        areaLocationPoint.text = getAreaLocationPoint(area!!)
        areaCategoryText.text = getAreaCategoryText(area!!)
    }

    fun onUpdateAreaMenuItemClick(): Boolean {
        if (area == null) {
            Toast.makeText(this, "Area invalid", Toast.LENGTH_SHORT).show()
            return false
        }

        val intent = Intent(this, AreaEditActivity::class.java)
        intent.putExtra(AREA_ID_EXTRA_NAME, area!!.id)
        startActivity(intent)

        return true
    }

    fun onDeleteAreaMenuItemClick(): Boolean {
        if (area == null) {
            Toast.makeText(this, "Area invalid", Toast.LENGTH_SHORT).show()
            return false
        }

        DependencyLocator.areaService.deleteArea(area!!.id).observe(this) { unit ->
            if (unit == null) {
                Toast.makeText(this, "Failed to delete area", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Area delete failed", Toast.LENGTH_SHORT).show()
            }
        }

        startActivity(Intent(this, AreasActivity::class.java))
        finish()

        return true
    }
}
