package com.company.indiafightscovid.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.indiafightscovid.databinding.FragmentNewsBinding
import com.company.indiafightscovid.model.database.NewsRoomDatabase
import com.company.indiafightscovid.model.entities.NewsData
import com.company.indiafightscovid.model.repository.NewsDataRepository
import com.company.indiafightscovid.view.activities.NewsDetails
import com.company.indiafightscovid.view.adapters.NewsAdapter
import com.company.indiafightscovid.view.adapters.NewsAdapterInterface
import com.company.indiafightscovid.viewmodel.NewsViewModel

class NewsFragment : Fragment(), NewsAdapterInterface {

    private lateinit var newsViewModel: NewsViewModel
    private var _binding: FragmentNewsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val db = NewsRoomDatabase.getDatabase(this.requireContext())
        val repository = NewsDataRepository(db.newsDao())
        newsViewModel = ViewModelProvider(
            this,
            NewsViewModel.Factory(repository)
        ).get(NewsViewModel::class.java)


        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFragmentNews.layoutManager=LinearLayoutManager(requireActivity())
        val newsAdapter=NewsAdapter(this,this)
        binding.rvFragmentNews.adapter=newsAdapter

        newsViewModel.newsList.observe(viewLifecycleOwner){
                newsAdapter.newsList(it)
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onNewsClicked(position: Int, model: NewsData) {
        val intent=Intent(this.context,NewsDetails::class.java)
        intent.putExtra(NEWS_DETAILS,model)
        startActivity(intent)
    }

    companion object{
        private const val NEWS_DETAILS="news_details"
    }
}
