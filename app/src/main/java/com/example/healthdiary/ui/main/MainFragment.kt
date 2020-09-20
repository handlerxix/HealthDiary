package com.example.healthdiary.ui.main

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.healthdiary.R
import com.example.healthdiary.model.DBHelper
import com.example.healthdiary.model.UserParameter
import com.example.healthdiary.ui.*
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {
    class UserParams {
        var weight: Float? = null
        var height: Float? = null
        var temp: Float? = null
        var press: Float? = null
    }

    //EditFields
    lateinit var inputWeight: EditText
    lateinit var inputHeight: EditText
    lateinit var inputTemp: EditText
    lateinit var sexSpinner: Spinner
    lateinit var inputPressure: EditText

    //Buttons
    lateinit var chooseDateButton: Button
    lateinit var saveButton: Button
    lateinit var butRec: ImageButton
    lateinit var butMeds: ImageButton
    lateinit var butWater: ImageButton

    //TextFields
    lateinit var inputAgeHint: TextView

    //Values
    val calendar = Calendar.getInstance()
    var birthDate = ""
    var birthYear = calendar.get(Calendar.YEAR)
    var birthMonth = calendar.get(Calendar.MONTH)
    var birthDay = calendar.get(Calendar.DAY_OF_MONTH)
    var params = UserParams()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)

        inputWeight = root.findViewById(R.id.input_weight)
        inputHeight = root.findViewById(R.id.input_height)
        inputTemp = root.findViewById(R.id.input_temp)
        sexSpinner = root.findViewById(R.id.sex_spinner)
        inputPressure = root.findViewById(R.id.input_pressure)

        butRec = root.findViewById(R.id.imageButtonRecomend)
        butMeds = root.findViewById(R.id.imageButtonMeds)
        butWater = root.findViewById(R.id.imageButtonWater)

        val navController = requireActivity().findNavController(R.id.nav_host_fragment)
        Navigation.setViewNavController(butRec, navController)
        Navigation.setViewNavController(butMeds, navController)
        Navigation.setViewNavController(butWater, navController)

        val dateFormat = SimpleDateFormat(getString(R.string.date_format), Locale(getString(R.string.ru)))

        inputAgeHint = root.findViewById(R.id.input_age)

        val dbHelper = DBHelper(container!!.context, null)
        initUserParameters(container, dbHelper)
        updateHints(root)

        chooseDateButton = root.findViewById(R.id.button_choose_date)
        chooseDateButton.setOnClickListener {
            val dpd = DatePickerDialog(
                container.context,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    birthDate =
                        dateFormat.format(dateFormat.parse("$dayOfMonth.${monthOfYear + 1}.$year")!!)

                    birthYear = year
                    birthMonth = monthOfYear
                    birthDay = dayOfMonth
                    inputAgeHint.text = birthDate
                },
                birthYear,
                birthMonth,
                birthDay
            )

            dpd.show()
        }

        saveButton = root.findViewById(R.id.button_save)
        saveButton.setOnClickListener {
            val weight = inputWeight.text.toString()
            val height = inputHeight.text.toString()
            val temp = inputTemp.text.toString()
            val pressure = inputPressure.text.toString()
            if (weight.isEmpty() || height.isEmpty() || temp.isEmpty() || pressure.isEmpty() || birthDate.isEmpty()) {
                Toast.makeText(
                    container.context,
                    getString(R.string.fill_empty_imputs),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                dbHelper.updateUserParameter(
                    UserParameter(
                        getString(R.string.birth_date_key),
                        birthDate
                    )
                )
                dbHelper.updateUserParameter(
                    UserParameter(
                        getString(R.string.sex),
                        sexSpinner.selectedItem.toString()
                    )
                )
                dbHelper.updateUserParameter(
                    UserParameter(
                        getString(R.string.weight),
                        weight
                    )
                )
                params.weight = weight.toFloat()
                dbHelper.updateUserParameter(
                    UserParameter(
                        getString(R.string.height),
                        height
                    )
                )
                params.height = height.toFloat()
                dbHelper.updateUserParameter(
                    UserParameter(
                        getString(R.string.temp),
                        temp
                    )
                )
                params.temp = temp.toFloat()
                dbHelper.updateUserParameter(
                    UserParameter(
                        getString(R.string.pressure),
                        pressure
                    )
                )
                params.press=pressure.toFloat()
                Toast.makeText(
                    container.context,
                    getString(R.string.values_saved),
                    Toast.LENGTH_LONG
                ).show()
                updateHints(root)
            }
        }

        butRec.setOnClickListener {
            navController.navigate(R.id.nav_recommendations)
        }
        butMeds.setOnClickListener {
            navController.navigate(R.id.nav_medicaments)
        }
        butWater.setOnClickListener {
            navController.navigate(R.id.nav_watertrack)
        }
        return root
    }

    private fun initUserParameters(container: ViewGroup, dbHelper: DBHelper){
        val userParameterList = dbHelper.getUserParameterList()
        for (userParameter in userParameterList) {
            when (userParameter.name) {
                getString(R.string.birth_date_key) -> {
                    birthDate = userParameter.value
                    inputAgeHint.text = getString(R.string.your_age, birthDate)
                    val dateParams = birthDate.split(getString(R.string.dot)).toTypedArray()
                    birthDay = dateParams[0].toInt()
                    birthMonth = dateParams[1].toInt()
                    birthYear = dateParams[2].toInt()
                }
                getString(R.string.sex) -> {
                    val adapter = ArrayAdapter(
                        container.context,
                        R.layout.fragment_main,
                        container.context.resources.getStringArray(R.array.sex_list)
                    )
                    sexSpinner.setSelection(adapter.getPosition(userParameter.value))
                }
                getString(R.string.weight) -> {
                    inputWeight.setText(userParameter.value)
                    params.weight = userParameter.value.toFloat()
                }
                getString(R.string.height) -> {
                    inputHeight.setText(userParameter.value)
                    params.height = userParameter.value.toFloat()
                }
                getString(R.string.temp) -> {
                    inputTemp.setText(userParameter.value)
                    params.temp = userParameter.value.toFloat()
                }
                getString(R.string.pressure) -> {
                    inputPressure.setText(userParameter.value)
                    params.press = userParameter.value.toFloat()
                }
            }
        }
    }

    private fun updateHints(root: View) {
        val inputMassIndexHint: TextView = root.findViewById(R.id.mass_index_hint)
        val inputTempHint: TextView = root.findViewById(R.id.temp_hint)
        val inputPressureHint: TextView = root.findViewById(R.id.pressure_hint)

        when (AppHelper.getTempCode(params.temp)) {
            CodeValue.HIGH_TEMP -> {
                inputTempHint.text = getString(R.string.high_temp)
                inputTempHint.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            }
            CodeValue.LOW_TEMP -> {
                inputTempHint.text = getString(R.string.low_temp)
                inputTempHint.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            }
            else -> {
                inputTempHint.text = getString(R.string.norm_temp)
                inputTempHint.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }
        }

        when (AppHelper.getPressCode(params.press)) {
            CodeValue.HIGH_PRESS -> {
                inputPressureHint.text = getString(R.string.high_press)
                inputPressureHint.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
            }
            CodeValue.LOW_PRESS -> {
                inputPressureHint.text = getString(R.string.low_press)
                inputPressureHint.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
            }
            else -> {
                inputPressureHint.text = getString(R.string.norm_press)
                inputPressureHint.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
            }
        }

        val massIndex = AppHelper.getMassIndex(params.weight, params.height)
        inputMassIndexHint.text = getWeightIndexHint(massIndex)

        val color = when (AppHelper.getWeightProblemCode(massIndex)) {
            CodeValue.HIGH_WEIGHT_INDEX, CodeValue.LOW_WEIGHT_INDEX ->
                ContextCompat.getColor(
                    requireContext(),
                    R.color.red
                )
            else ->
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
        }
        inputMassIndexHint.setTextColor(color)
    }

    private fun getWeightIndexHint(massIndex: Float?): String {
        return when {
            massIndex == null -> getString(R.string.mass_index_normal_hint)
            massIndex < DEFICIT -> getString(R.string.mass_index_deficit_hint)
            massIndex < NORMAL -> getString(R.string.mass_index_normal_hint)
            massIndex < EXCESS -> getString(R.string.mass_index_excess_hint)
            massIndex < FIRST_DEGREE_OBESITY -> getString(R.string.mass_index_first_degree_hint)
            massIndex < SECOND_DEGREE_OBESITY -> getString(R.string.mass_index_second_degree_hint)
            else -> getString(R.string.mass_index_third_degree_hint)
        }
    }
}