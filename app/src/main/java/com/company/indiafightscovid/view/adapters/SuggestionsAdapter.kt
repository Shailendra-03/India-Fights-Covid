package com.company.indiafightscovid.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.company.indiafightscovid.R
import com.company.indiafightscovid.utils.TimeToTimeAgo
import com.company.indiafightscovid.databinding.ItemSuggestionsBinding
import com.company.indiafightscovid.model.entities.Suggestions
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SuggestionsAdapter(val fragment: Fragment, options: FirestoreRecyclerOptions<Suggestions>,
                         private val listener:SuggestionAdapterInterface):
    FirestoreRecyclerAdapter<Suggestions, SuggestionsAdapter.ViewHolder>(options) {
    class ViewHolder(view:ItemSuggestionsBinding):RecyclerView.ViewHolder(view.root) {
        val userImage=view.ivUserImage
        val userName=view.tvName
        val postTime=view.tvCreatedAt
        val suggestionsText=view.tvSuggestionsText
        val likedImage=view.ivLiked
        val likedCount=view.tvLikedCount
        val dislikedImage=view.ivDisliked
        val dislikedCount=view.tvDislikedCount
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Suggestions) {
        Glide.with(fragment).load(model.user.userImageUrl).error(R.drawable.ic_unknown_person_image).circleCrop().into(holder.userImage)
        holder.userName.text=model.user.userName
        holder.suggestionsText.text=model.text
        holder.likedCount.text=model.likedBy.size.toString()
        holder.dislikedCount.text=model.dislikedBy.size.toString()
        holder.postTime.text= TimeToTimeAgo().getTimeAgo(model.createdAt)
        val auth=Firebase.auth
        val currentUserId=auth.currentUser!!.uid

        holder.likedImage.setOnClickListener {
            listener.onLikeClicked(snapshots.getSnapshot(holder.absoluteAdapterPosition).id)
        }
        holder.dislikedImage.setOnClickListener {
            listener.onDislikedClicked(snapshots.getSnapshot(holder.absoluteAdapterPosition).id)
        }

        val isLiked=model.likedBy.contains(currentUserId)
        val isDisliked= model.dislikedBy.contains(currentUserId)
        if(isLiked){
            holder.likedImage.setImageDrawable(ContextCompat.getDrawable(holder.likedImage.context,R.drawable.ic_liked))
        }else{
            holder.likedImage.setImageDrawable(ContextCompat.getDrawable(holder.likedImage.context,R.drawable.ic_not_liked))
        }

        if(isDisliked){
            holder.dislikedImage.setImageDrawable(ContextCompat.getDrawable(holder.dislikedImage.context,R.drawable.ic_disliked))
        }else{
            holder.dislikedImage.setImageDrawable(ContextCompat.getDrawable(holder.dislikedImage.context,R.drawable.ic_not_disliked))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=ItemSuggestionsBinding.inflate(LayoutInflater.from(fragment.context),parent,false)
        return ViewHolder(binding)
    }
}

interface SuggestionAdapterInterface{
    fun onLikeClicked(suggestionsId:String)
    fun onDislikedClicked(suggestionsId: String)
}