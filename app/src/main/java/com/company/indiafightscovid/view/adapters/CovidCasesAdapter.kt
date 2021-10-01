package com.company.indiafightscovid.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.company.indiafightscovid.databinding.ItemCovidCasesLayoutBinding
import com.company.indiafightscovid.model.entities.CitiesContainer

class CovidCasesAdapter(private val fragment: Fragment):RecyclerView.Adapter<CovidCasesAdapter.ViewHolder>() {

    private var citiesList:ArrayList<CitiesContainer> = arrayListOf()

    class ViewHolder(view:ItemCovidCasesLayoutBinding):RecyclerView.ViewHolder(view.root) {
        val stateName=view.tvNameOfState
        val totalCases=view.tvTotalCasesNumber
        val totalRecovered=view.tvTotalRecoveredNumber
        val todayCases=view.tvTodayCasesNumber
        val todayRecovered=view.tvTodayRecoveredNumber
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding:ItemCovidCasesLayoutBinding= ItemCovidCasesLayoutBinding.inflate(LayoutInflater.from(fragment.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val citiesData=citiesList[position]
        holder.stateName.text=citiesData.id
        holder.totalCases.text=citiesData.cityData.totalCases
        holder.totalRecovered.text=citiesData.cityData.totalRecovered
        holder.todayCases.text=citiesData.cityData.todayCases
        holder.todayRecovered.text=citiesData.cityData.todayRecovered
    }

    override fun getItemCount(): Int {
        return citiesList.size
    }

    fun citiesList(list: ArrayList<CitiesContainer>){
        citiesList=list
        notifyDataSetChanged()
    }
}