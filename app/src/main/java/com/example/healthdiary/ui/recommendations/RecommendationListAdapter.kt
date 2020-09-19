package com.example.healthdiary.ui.recommendations

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.healthdiary.R
import com.example.healthdiary.model.Recommendation
import com.example.healthdiary.ui.cutWordsWithEnding

class RecommendationListAdapter(context: Context, objects: List<Recommendation>) :
    ArrayAdapter<Recommendation>(context, R.layout.item_list_element, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return if (convertView != null) {
            convertView
        } else {
            Log.d(javaClass.toString(), "getView")
            val v =
                LayoutInflater
                    .from(this.context)
                    .inflate(R.layout.item_list_element, parent, false)
            createViewFromResource(v, position)
        }
    }

    @SuppressLint("SetTextI18n")
    fun createViewFromResource(v: View, position: Int): View {
        val title: TextView = v.findViewById(R.id.recommendation_title)
        val descr: TextView = v.findViewById(R.id.recommendation_description)
        val recommendation = getItem(position)!!
        title.text = recommendation.title.cutWordsWithEnding(30)
        descr.text = recommendation.text.cutWordsWithEnding(80)
        return v
    }
}