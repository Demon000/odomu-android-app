package com.demon000.odomu

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.demon000.odomu.models.Area
import com.google.android.material.card.MaterialCardView


class AreasAdapter(
    private val areas: ArrayList<Area>,
    private val areaCategories: HashMap<Number, String>,
) : RecyclerView.Adapter<AreasAdapter.ViewHolder>() {
    val cardClickObserver = MutableLiveData<String>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val areaCard: MaterialCardView = view.findViewById(R.id.areaCard)
        val areaImageView: ImageView = view.findViewById(R.id.areaImageView)
        val areaNameText: TextView = view.findViewById(R.id.areaNameText)
        val areaCategoryText: TextView = view.findViewById(R.id.areaCategoryText)
        var areaId: String? = null

        init {
            areaCard.setOnClickListener {
                this@AreasAdapter.cardClickObserver.postValue(areaId)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.area_item, viewGroup, false)

        return ViewHolder(view)
    }

    fun getArea(position: Int): Area {
        return areas[position];
    }

    fun getAreaCategoryText(area: Area): String {
        return areaCategories.getOrDefault(area.category, "")
    }

    fun getAreaImage(area: Area): Bitmap? {
        if (area.image == null) {
            return null
        }

        val decodedBytes: ByteArray = Base64.decode(area.image, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val area = getArea(position)

        val bitmap = getAreaImage(area)
        if (bitmap == null) {
            viewHolder.areaImageView.visibility = GONE
        } else {
            viewHolder.areaImageView.setImageBitmap(getAreaImage(area))
        }

        viewHolder.areaNameText.text = area.name
        viewHolder.areaCategoryText.text = getAreaCategoryText(area)
        viewHolder.areaId = area.id
    }

    override fun getItemCount(): Int {
        return areas.size;
    }
}
