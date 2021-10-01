package com.company.indiafightscovid.view.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.company.indiafightscovid.R
import com.company.indiafightscovid.databinding.ActivityNewsDetailsBinding
import com.company.indiafightscovid.model.entities.NewsData

class NewsDetails : AppCompatActivity() {
    private lateinit var binding : ActivityNewsDetailsBinding
    private lateinit var news:NewsData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityNewsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra("news_details")){
            news=intent.getSerializableExtra("news_details") as NewsData
        }
        Glide.with(this).load(news.imageSource).error(R.drawable.ic_baseline_broken_image_24).into(binding.ivNewsDetailImage)
        binding.tvNewsDetailTitle.text=news.title
        binding.tvNewsDetailContent.text=news.content
        binding.tvPublishedAt.text=news.publishedAt
        binding.tvNewsDetailDescription.text=news.description

        binding.ivBackButton.setOnClickListener {
            finish()
        }
        binding.btnViewOnWeb.setOnClickListener {
            val url=news.url
            try{
                val intent=Intent(Intent.ACTION_VIEW)
                intent.data= Uri.parse(url)
                startActivity(intent)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}