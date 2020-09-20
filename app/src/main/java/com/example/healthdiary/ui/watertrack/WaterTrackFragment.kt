package com.example.healthdiary.ui.watertrack

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.healthdiary.R
import com.example.healthdiary.model.DBHelper
import com.example.healthdiary.model.WaterTrackParameters
import com.example.healthdiary.ui.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class WaterTrackFragment : Fragment() {
    private var paramsList = LinkedList<WaterTrackParameters>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_watertrack, container, false)

        val dbHandler = DBHelper(container!!.context)
        var sumIntake = dbHandler.getLastWaterTrackParameters().sumBy { it.currentIntake }
        val userTemp = dbHandler.getUserParameter(getString(R.string.temp))?.toFloat()
        val userWeight = dbHandler.getUserParameter(getString(R.string.weight))?.toFloat()
        val userSex = dbHandler.getUserParameter(getString(R.string.sex))
        val dailyRate = calculateDailyRate(userTemp, userWeight, userSex, container.context)

        val historyList: ListView = root.findViewById(R.id.historyList)
        paramsList = dbHandler.getWaterTrackParametersList()
        updateScreen(dailyRate, sumIntake, historyList, root)

        val radioGroup: RadioGroup = root.findViewById(R.id.radioGroup)
        val inputAmountText: EditText = root.findViewById(R.id.inputAmountText)
        val addButton: Button = root.findViewById(R.id.addButton)
        addButton.setOnClickListener {
            val addAmountStr = inputAmountText.text.toString()
            if (addAmountStr.isEmpty()) {
                Toast.makeText(container.context, "Введите количество!", Toast.LENGTH_LONG).show()
            } else {
                inputAmountText.setText("")
                val mult = when (radioGroup.checkedRadioButtonId) {
                    R.id.waterRadioButton -> 1f
                    R.id.coffeeRadioButton -> 0.5f
                    R.id.teaRadioButton -> 0.7f
                    else -> 0f
                }
                val liquid = when (radioGroup.checkedRadioButtonId) {
                    R.id.waterRadioButton -> getString(R.string.water)
                    R.id.coffeeRadioButton -> getString(R.string.coffee)
                    R.id.teaRadioButton -> getString(R.string.tea)
                    else -> getString(R.string.liquid)
                }
                val currentIntake = (addAmountStr.toInt() * mult).toInt()
                sumIntake += currentIntake
                val waterTrack = WaterTrackParameters(currentIntake, liquid)
                dbHandler.updateWaterTrackParameters(waterTrack)
                paramsList.addFirst(waterTrack)
                updateScreen(dailyRate, sumIntake, historyList, root)
            }
        }
        return root
    }

    private fun updateScreen(
        dailyRate: Int,
        currentIntake: Int,
        historyList: ListView,
        root: View
    ) {
        val normTextView: TextView = root.findViewById(R.id.normTextView)

        val progressPercent = (currentIntake.toFloat() / dailyRate * 100).toInt()
        val progressBar: ProgressBar = root.findViewById(R.id.progressBar)
        val adapter = ArrayWaterTrackAdapter(requireContext(), paramsList)
        progressBar.progress = progressPercent
        normTextView.text = getString(R.string.water_current, currentIntake, dailyRate)
        historyList.adapter = adapter
    }

    private fun calculateDailyRate(
        userTemp: Float?,
        userWeight: Float?,
        userSex: String?,
        context: Context
    ): Int {
        if (userTemp == null || userWeight == null || userSex == null) {
            Toast.makeText(context, getString(R.string.not_enough_data), Toast.LENGTH_LONG).show()
            return BASE_WATER_RATE
        }
        val month = SimpleDateFormat("mm").format(Date())
        val seasonMult = when (month.toInt()) {
            12, 1, 2 -> WINTER_WATER_COEFF
            6, 7, 8 -> SUMMER_WATER_COEFF
            else -> 1f
        }

        val tempMult = TEMP_WATER_MUL_COEFF * userTemp / NORMAL_TEMP
        val rateBySexAndWeight = if (userSex == getString(R.string.male)) {
            (MALE_WATER_RATE * userWeight).toInt()
        } else {
            (FEMALE_WATER_RATE * userWeight).toInt()
        }

        return (seasonMult * rateBySexAndWeight * tempMult).roundToInt()
    }
}