package com.demon000.odomu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.demon000.odomu.dependencies.DependencyLocator
import com.demon000.odomu.models.Area
import com.demon000.odomu.models.AreaAddData
import com.demon000.odomu.models.AreaUpdateData
import com.demon000.odomu.utils.Constants
import kotlinx.android.synthetic.main.activity_area_details.topAppBar
import kotlinx.android.synthetic.main.activity_area_edit.*
import java.lang.Float.parseFloat
import java.lang.NumberFormatException

class AreaEditActivity : AppCompatActivity() {
    private var areaId: String? = null
    private var area: Area? = null
    private var areaCategories = HashMap<Number, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_area_edit)

        areaId = intent.getStringExtra(Constants.AREA_ID_EXTRA_NAME)

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.saveArea -> {
                    onSaveAreaMenuItemClick()
                }
                else -> false
            }
        }
    }

    override fun onStart() {
        super.onStart()

        loadAreaCategories()
        loadArea()
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
        if (areaId == null) {
            return
        }

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

    fun getAreaCategoryText(area: Area): String {
        return areaCategories.getOrDefault(area.category, "")
    }

    fun onAreaCategoriesLoad() {
        val items = ArrayList(areaCategories.values)
        val adapter = ArrayAdapter(this, R.layout.list_item, items)
        categoryFieldText.setAdapter(adapter)

        if (area == null) {
            return
        }

        categoryField.editText?.setText(getAreaCategoryText(area!!))
    }

    fun onAreaLoad() {
        if (area == null) {
            return
        }

        topAppBar.title = "Updating area " + area!!.name
        nameField.editText?.setText(area!!.name)
        categoryField.editText?.setText(getAreaCategoryText(area!!))
        locationField.editText?.setText(area!!.location)
        locationLatField.editText?.setText(area!!.locationPoint[0].toString())
        locationLonField.editText?.setText(area!!.locationPoint[1].toString())
    }

    fun checkFields(): AreaAddData? {
        val name = nameField.editText?.text.toString()
        if (name.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            return null
        }

        val category = categoryField.editText?.text.toString()
        if (category.isEmpty()) {
            Toast.makeText(this, "Category cannot be empty", Toast.LENGTH_SHORT).show()
            return null
        }

        val location = locationField.editText?.text.toString()
        if (location.isEmpty()) {
            Toast.makeText(this, "Location cannot be empty", Toast.LENGTH_SHORT).show()
            return null
        }

        val locationPointLat: Float
        try {
            locationPointLat = parseFloat(locationLatField.editText?.text.toString())
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Location latitude is invalid", Toast.LENGTH_SHORT).show()
            return null
        }

        val locationPointLon: Float
        try {
            locationPointLon = parseFloat(locationLonField.editText?.text.toString())
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Location longitude is invalid", Toast.LENGTH_SHORT).show()
            return null
        }

        val locationPoint = ArrayList<Float>()
        locationPoint.add(locationPointLat)
        locationPoint.add(locationPointLon)

        return AreaAddData(name, category, location, locationPoint)
    }

    fun onSaveAreaMenuItemClick(): Boolean {
        val data: AreaAddData = checkFields() ?: return true
        val call: MutableLiveData<Area?>?

        call = if (area == null) {
            DependencyLocator.areaService.addArea(data)
        } else {
            val updateData = AreaUpdateData(data.name, data.category, data.location,
                data.locationPoint, area!!.updatedAtTimestamp)
            DependencyLocator.areaService.updateArea(areaId!!, updateData)
        }

        call.observe(this) { area ->
            if (area == null) {
                Toast.makeText(this, "Failed to save / update area", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Area save / update success", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, AreaDetailsActivity::class.java)
                intent.putExtra(Constants.AREA_ID_EXTRA_NAME, area.id)
                startActivity(intent)
                finish()
            }
        }

        return true
    }
}
