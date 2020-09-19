package com.example.healthdiary.ui.recommendations

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.fragment.app.ListFragment
import com.example.healthdiary.R
import com.example.healthdiary.model.DBHelper
import com.example.healthdiary.model.Recommendation
import com.example.healthdiary.model.UserParameter
import com.example.healthdiary.utils.RecommendationsUtil
import com.example.healthdiary.ui.CodeValue
import com.example.healthdiary.ui.AppHelper

class RecommendationsListFragment : ListFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val recommendationsService =
            RecommendationsUtil()
        val dbHelper = DBHelper(requireContext())
        val userParams = dbHelper.getUserParameterList()
        val userRecommendations = getUserRecommendations(recommendationsService, userParams)
        listAdapter = RecommendationListAdapter(requireContext(), userRecommendations)
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        val recommendation = l.getItemAtPosition(position) as Recommendation
        val intent = Intent(activity, RecommendationActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("recommendation", recommendation)
        intent.putExtra("bundle", bundle)
        requireContext().startActivity(intent)
    }

    private fun getUserRecommendations(
        util: RecommendationsUtil,
        userParameters: List<UserParameter>
    ): List<Recommendation> {
        val allRecommendationsList = util.getRecommendations()
        val userProblems = getUserProblemsList(userParameters)
        return allRecommendationsList.filter { recommendation ->
            userProblems.contains(
                recommendation.condition
            )
        }
    }

    private fun getUserProblemsList(userParams: List<UserParameter>): ArrayList<CodeValue> {
        val codesList = ArrayList<CodeValue>()
        var weight = 0f
        var height = 0f
        userParams.forEach { param ->
            when (param.name) {
                getString(R.string.weight) -> {
                    weight = param.value.toFloat()
                }
                getString(R.string.height) -> {
                    height = param.value.toFloat()
                }
                getString(R.string.temp) -> {
                    val tempCode = AppHelper.getTempCode(param.value.toFloat())
                    if (tempCode != null) {
                        codesList.add(tempCode)
                    }
                }
                getString(R.string.pressure) -> {
                    val pressCode = AppHelper.getPressCode(param.value.toFloat())
                    if (pressCode != null) {
                        codesList.add(pressCode)
                    }
                }
            }
        }
        val massIndex = AppHelper.getMassIndex(weight, height)
        val massIndexCode = AppHelper.getWeightProblemCode(massIndex)
        if (massIndexCode != null) {
            codesList.add(massIndexCode)
        }
        if (codesList.isEmpty()) {
            codesList.add(CodeValue.FINE_CODE)
        }
        return codesList
    }
}