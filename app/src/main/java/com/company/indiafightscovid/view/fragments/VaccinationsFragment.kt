package com.company.indiafightscovid.view.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.indiafightscovid.databinding.VaccinationsFragmentBinding
import com.company.indiafightscovid.model.entities.AllIndiaData
import com.company.indiafightscovid.view.adapters.VaccinationsAdapter
import com.company.indiafightscovid.viewmodel.VaccinationsViewModel

class VaccinationsFragment : Fragment() {

    private lateinit var vaccinationsViewModel: VaccinationsViewModel
    private var _binding: VaccinationsFragmentBinding?  = null
    private lateinit var vaccinationsAdapter:VaccinationsAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vaccinationsViewModel =
            ViewModelProvider(this).get(VaccinationsViewModel::class.java)

        _binding = VaccinationsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvVaccinations.layoutManager=LinearLayoutManager(requireActivity())
        vaccinationsAdapter=VaccinationsAdapter(this)
        binding.rvVaccinations.adapter=vaccinationsAdapter

        vaccinationsViewModel.citiesListAndData.observe(viewLifecycleOwner){
            vaccinationsAdapter.citiesList(it)
        }

        vaccinationsViewModel.allIndiaData.observe(viewLifecycleOwner){
            setAllIndiaVaccinationView(it)
        }

        vaccinationsViewModel.progressBarChecker.observe(viewLifecycleOwner){
            if(it==true){
                binding.progressBarVaccinations.visibility=View.VISIBLE
            }else{
                binding.progressBarVaccinations.visibility=View.GONE
            }
        }
    }

    private fun setAllIndiaVaccinationView(it: AllIndiaData) {
        binding.tvAllIndiaFirstDose.text=it.totalFirstDose
        binding.tvAllIndiaSecondDose.text=it.totalSecondDose
        binding.tvAllIndiaTotalDosesYesterday.text=it.todayDosesGiven
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}