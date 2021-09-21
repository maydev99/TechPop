package com.bombadu.techpop.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bombadu.techpop.R
import com.bombadu.techpop.local.NewsEntity
import com.bombadu.techpop.ui.detail.ArticleDetailActivity
import com.squareup.picasso.Picasso


class HomeAdapter : ListAdapter<NewsEntity, HomeAdapter.ItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.article_card, parent, false)
        )
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageView = itemView.findViewById<ImageView>(R.id.card_image_view)
        private val sourceTextView = itemView.findViewById<TextView>(R.id.card_source_text_view)
        private val authorTextView = itemView.findViewById<TextView>(R.id.card_author_text_view)
        private val descriptionTextView =
            itemView.findViewById<TextView>(R.id.card_description_text_view)

        @SuppressLint("SetTextI18n")
        fun bind(item: NewsEntity) = with(itemView) {
            if (item.urlToImage != "") {
                Picasso.get().load(item.urlToImage)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.gradient_fade_up)
                    .error(R.drawable.noimage).into(imageView)

            } else {
                Picasso.get().load(R.drawable.missingimage).into(imageView)
            }

            val author = item.author
            sourceTextView.text = item.source
            authorTextView.text = "by $author"
            if (author!!.contains("https")) {
                authorTextView.text = ""

            }
            descriptionTextView.text = item.title


            setOnClickListener {
                val intent = Intent(context, ArticleDetailActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable("article_key", item)
                intent.putExtras(bundle)
                context.startActivity(intent)

            }


        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<NewsEntity>() {

    override fun areItemsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean {
        return oldItem == newItem
    }
}