package com.bombadu.techpop.ui.saved

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bombadu.techpop.R
import com.bombadu.techpop.local.SavedEntity
import com.bombadu.techpop.util.Utils
import com.squareup.picasso.Picasso

class SavedAdapter2(
    val mItemClickListener: ItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<SavedEntity>() {

        override fun areItemsTheSame(oldItem: SavedEntity, newItem: SavedEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SavedEntity, newItem: SavedEntity): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, diffCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return SavedHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.saved_article_item,
                parent,
                false
            ),
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SavedHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    fun getItemAt(position: Int): SavedEntity {
        return differ.currentList[position]

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<SavedEntity>) {
        differ.submitList(list)
    }

    inner class SavedHolder(
        itemView: View,

        ) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                mItemClickListener.onItemClick(adapterPosition)

            }
        }

        fun bind(item: SavedEntity) = with(itemView) {
            itemView.setOnClickListener {
                mItemClickListener.onItemClick(adapterPosition)
            }

            val imageView = findViewById<ImageView>(R.id.saved_image_view)
            val titleTextView = findViewById<TextView>(R.id.saved_title_text_view)
            val dateTextView = findViewById<TextView>(R.id.saved_date_text_view)

            if (item.imageUrl != "") {
                Picasso.get().load(item.imageUrl)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.missingimage).into(imageView)

            } else {
                Picasso.get().load(R.drawable.missingimage).into(imageView)
            }

            titleTextView.text = item.title
            val timestamp = item.savedTimeStamp
            dateTextView.text = Utils.convertTimestampToDate(timestamp.toString())
        }
    }
}



