package com.company.indiafightscovid.view.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.indiafightscovid.R
import com.company.indiafightscovid.databinding.FragmentHomeBinding
import com.company.indiafightscovid.model.entities.AllIndiaData
import com.company.indiafightscovid.view.adapters.CovidCasesAdapter
import com.company.indiafightscovid.viewmodel.HomeViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class HomeFragment : Fragment(),View.OnClickListener {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvHomeCovidCases.layoutManager=LinearLayoutManager(requireActivity())
        val cityAdapter=CovidCasesAdapter(this)
        binding.rvHomeCovidCases.adapter=cityAdapter

        homeViewModel.citiesListAndData.observe(viewLifecycleOwner){
            cityAdapter.citiesList(it)
        }
        homeViewModel.allIndiaData.observe(viewLifecycleOwner){
            setAllIndiaCasesView(it)
        }
        homeViewModel.progressBarChecker.observe(viewLifecycleOwner){
            if(it==true){
                binding.progressBarHome.visibility=View.VISIBLE
            }else{
                binding.progressBarHome.visibility=View.GONE
            }
        }
        makeAllIndiaDataVisible()
        binding.rgCases.setOnCheckedChangeListener { _, checkedId ->
            if(checkedId== R.id.rb_cases_in_india){
                makeAllIndiaDataVisible()
            }else{
                homeViewModel.refreshCitiesDataFromFirestore()
                makeStateWiseDataVisible()
            }
        }
        binding.tvMinistryHelplineNumber.setOnClickListener(this)
        binding.tvSeniorCitizenHelplineNumber.setOnClickListener(this)
        binding.tvMentalHealthHelplineNumber.setOnClickListener(this)
        binding.tvAyushCounsellingNumber.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            binding.tvMinistryHelplineNumber->{
                callHelpline(binding.tvMinistryHelplineNumber.text.toString())
            }
            binding.tvSeniorCitizenHelplineNumber->{
                callHelpline(binding.tvSeniorCitizenHelplineNumber.text.toString())
            }
            binding.tvMentalHealthHelplineNumber->{
                callHelpline(binding.tvMentalHealthHelplineNumber.text.toString())
            }
            binding.tvAyushCounsellingNumber->{
                callHelpline(binding.tvAyushCounsellingNumber.text.toString())
            }
        }
    }

    private fun callHelpline(s: String) {
        Dexter.withContext(this.requireContext()).withPermission(Manifest.permission.CALL_PHONE).withListener(object :
            PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                try {
                    startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$s")))
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(this@HomeFragment.requireContext(),"You just denied the permission, we need the permission to call the helpline",Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?
            ) {
                showAlertDialogForPermissions("Call permission")
            }
        }).onSameThread().check()

    }

    private fun showAlertDialogForPermissions(permissionName:String) {
        val alertDialog=AlertDialog.Builder(this.requireContext())
        alertDialog.setMessage("We did not have $permissionName , you can enable permission in settings")
            .setNegativeButton("Cancel"){ dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .setPositiveButton("Go to settings"){ dialogInterface, _ ->
                gotoAppSettings()
                dialogInterface.dismiss()
            }.setCancelable(true)
            .show()
    }

    private fun gotoAppSettings() {
        try {
            val intent=Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri=Uri.fromParts("package", requireActivity().packageName,null)
            intent.data = uri
            startActivity(intent)
        }catch (e:Exception){
            Toast.makeText(requireContext(),"Some Error Occurred",Toast.LENGTH_SHORT).show()
        }

    }

    private fun setAllIndiaCasesView(it: AllIndiaData) {
        binding.tvAllIndiaTotalCases.text=it.totalCases
        binding.tvAllIndiaTotalRecovered.text=it.totalRecovered
        binding.tvAllIndiaTotalDeaths.text=it.totalDeaths
        binding.tvAllIndiaNewCases.text=it.newCases
        binding.tvAllIndiaNewRecovered.text=it.newRecovered
        binding.tvAllIndiaActiveCases.text=it.activeCases
    }

    private fun makeStateWiseDataVisible() {
        binding.clAllIndiaData.visibility=View.GONE
        binding.rvHomeCovidCases.visibility=View.VISIBLE
    }

    private fun makeAllIndiaDataVisible() {
        binding.clAllIndiaData.visibility=View.VISIBLE
        binding.rvHomeCovidCases.visibility=View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}