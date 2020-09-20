package com.example.healthdiary.ui.medicaments

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.ListFragment
import com.example.healthdiary.model.DBHelper
import com.example.healthdiary.model.Medicament
import com.example.healthdiary.utils.MedicamentsUtil

class MedicamentsListFragment : ListFragment() {

    private val medicamentsService =
        MedicamentsUtil()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AsyncTask.execute {
            val result = getMedicaments()
            requireActivity().runOnUiThread {
                listAdapter = MedicamentsListAdapter(requireContext(), result)
            }
        }
        listAdapter = MedicamentsListAdapter(requireContext(), emptyList())
    }

    private fun getMedicaments(): List<Medicament> {
        val medicaments = medicamentsService.getMedicaments()
        val dbHelper = DBHelper(requireContext())
        if (medicaments.isEmpty()) {
            activity?.runOnUiThread{
                Toast.makeText(
                    requireContext(),
                    "Medicaments were not found in internet",
                    Toast.LENGTH_LONG
                ).show()
            }
            return dbHelper.getMedicaments()
        } else {
            medicaments.forEach { dbHelper.saveMedicament(it) }
        }
        return medicaments
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        val medicament = l.getItemAtPosition(position) as Medicament
        val intent = Intent(activity, MedicamentActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("medicament", medicament)
        intent.putExtra("bundle", bundle)
        requireContext().startActivity(intent)
    }
}