package com.example.healthdiary.ui.medicaments

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.TextView
import com.example.healthdiary.R
import com.example.healthdiary.model.Medicament
import com.example.healthdiary.utils.AbstractListAdapter
import com.example.healthdiary.ui.cutWordsWithEnding

class MedicamentsListAdapter(context: Context, objects: List<Medicament>) :
    AbstractListAdapter<Medicament>(context, R.layout.item_list_element, objects) {

    @SuppressLint("SetTextI18n")
    override fun createViewFromResource(v: View, position: Int): View {
        val title: TextView = v.findViewById(R.id.recommendation_title)
        val descr: TextView = v.findViewById(R.id.recommendation_description)
        val med = getItem(position)!!
        title.text = med.name.cutWordsWithEnding(30)
        descr.text = "${med.manufacturer}, ${med.price}".cutWordsWithEnding(80)
        return v
    }
}