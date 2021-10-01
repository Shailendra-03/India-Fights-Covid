package com.company.indiafightscovid.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.company.indiafightscovid.R
import com.company.indiafightscovid.databinding.ItemNewsLayoutBinding
import com.company.indiafightscovid.model.entities.NewsData

class NewsAdapter(private val fragment: Fragment,private val listener:NewsAdapterInterface):RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    private var newsData:List<NewsData> = listOf()

    class ViewHolder(view:ItemNewsLayoutBinding):RecyclerView.ViewHolder(view.root) {
        val  ivNewsImage=view.ivNewsImage
        val tvTitle=view.tvNewsTitle
        val tvDescription=view.tvNewsDescription
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding:ItemNewsLayoutBinding= ItemNewsLayoutBinding.inflate(LayoutInflater.from(fragment.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news=newsData[position]
        Glide.with(fragment)
            .load(news.imageSource)
            .centerCrop()
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(holder.ivNewsImage)

        holder.tvTitle.text=news.title
        holder.tvDescription.text=news.description

        holder.itemView.setOnClickListener {
            listener.onNewsClicked(position,newsData[position])
        }
    }

    override fun getItemCount(): Int {
        return newsData.size
    }

    fun newsList(list: List<NewsData>){
        newsData=list
        notifyDataSetChanged()
    }
}

interface NewsAdapterInterface{
    fun onNewsClicked(position:Int,model:NewsData)
}