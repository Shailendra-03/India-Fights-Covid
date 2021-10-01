package com.company.indiafightscovid.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.company.indiafightscovid.databinding.ItemVaccinationsBinding
import com.company.indiafightscovid.model.entities.CitiesContainer

class VaccinationsAdapter(private val fragment: Fragment):RecyclerView.Adapter<VaccinationsAdapter.ViewHolder>() {
    private var citiesList:ArrayList<CitiesContainer> = arrayListOf()

    class ViewHolder(view:ItemVaccinationsBinding):RecyclerView.ViewHolder(view.root) {
        val nameOfState=view.tvVaccinationsNameOfState
        val firstDoseNumber=view.tvTotalFirstDoseNumber
        val secondDoseNumber=view.tvTotalSecondDoseNumber
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=ItemVaccinationsBinding.inflate(LayoutInflater.from(fragment.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val citiesData=citiesList[position]
        holder.nameOfState.text=citiesData.id
        holder.firstDoseNumber.text=citiesData.cityData.totalFirstDose
        holder.secondDoseNumber.text=citiesData.cityData.totalSecondDose


    }

    override fun getItemCount(): Int {
        return citiesList.size
    }

    fun citiesList(list: ArrayList<CitiesContainer>){
        citiesList=list
        notifyDataSetChanged()
    }
}