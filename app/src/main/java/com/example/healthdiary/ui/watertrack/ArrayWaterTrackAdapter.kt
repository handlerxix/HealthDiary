package com.example.healthdiary.ui.watertrack

import android.content.Context
import android.view.View
import android.widget.TextView
import com.example.healthdiary.R
import com.example.healthdiary.model.WaterTrackParameters
import com.example.healthdiary.utils.AbstractListAdapter

class ArrayWaterTrackAdapter(context: Context, objects: List<WaterTrackParameters>) :
    AbstractListAdapter<WaterTrackParameters>(context, R.layout.item_list_element, objects) {

    override fun createViewFromResource(v: View, position: Int): View {
        val title : TextView = v.findViewById(R.id.recommendation_title)
        val description : TextView = v.findViewById(R.id.recommendation_description)
        val param = getItem(position)!!
        title.text = param.liquid
        description.text = "Выпито: ${param.currentIntake}ml, Дата: ${param.date}"
        return v
    }
}