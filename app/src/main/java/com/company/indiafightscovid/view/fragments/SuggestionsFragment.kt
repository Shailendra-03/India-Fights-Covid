package com.company.indiafightscovid.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.indiafightscovid.databinding.FragmentSuggestionsBinding
import com.company.indiafightscovid.model.entities.Suggestions
import com.company.indiafightscovid.model.daos.SuggestionsDao
import com.company.indiafightscovid.view.adapters.SuggestionAdapterInterface
import com.company.indiafightscovid.view.adapters.SuggestionsAdapter
import com.company.indiafightscovid.viewmodel.SuggestionsViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query

class SuggestionsFragment: Fragment(), SuggestionAdapterInterface {

    private lateinit var suggestionsViewModel:SuggestionsViewModel
    private  lateinit var binding:FragmentSuggestionsBinding
    private lateinit var suggestionsDao:SuggestionsDao
    private lateinit var suggestionsAdapter:SuggestionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        suggestionsViewModel=ViewModelProvider(this).get(SuggestionsViewModel::class.java)
        binding= FragmentSuggestionsBinding.inflate(inflater,container,false)

        binding.btnPost.setOnClickListener {
            val suggestions=binding.etSuggestions.text.toString().trim()
            if(suggestions.isNotEmpty()&&suggestions.length>=20){
                suggestionsViewModel.addPost(suggestions)
                binding.etSuggestions.setText("")
            }else{
                Toast.makeText(this.requireContext(),"Please enter in more than 20 words",Toast.LENGTH_SHORT).show()
            }
        }
        suggestionsDao= SuggestionsDao()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val suggestionCollection=suggestionsDao.suggestionsCollection
        val query=suggestionCollection.orderBy("createdAt", Query.Direction.DESCENDING).limit(30)
        val recyclerViewOptions= FirestoreRecyclerOptions.Builder<Suggestions>().setQuery(query,
            Suggestions::class.java).build()
        suggestionsAdapter=SuggestionsAdapter(this,recyclerViewOptions,this)
        binding.rvSuggestions.layoutManager=LinearLayoutManager(requireActivity())
        binding.rvSuggestions.adapter=suggestionsAdapter
    }

    override fun onStart() {
        super.onStart()
        suggestionsAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        suggestionsAdapter.stopListening()
    }

    override fun onLikeClicked(suggestionsId: String) {
        suggestionsViewModel.updateSuggestionsLikes(suggestionsId)
    }

    override fun onDislikedClicked(suggestionsId: String) {
        suggestionsViewModel.updateSuggestionsDislikes(suggestionsId)
    }

}