package com.moustafa.nyclient.ui.articleslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moustafa.nyclient.R
import com.moustafa.nyclient.model.NYArticle
import kotlinx.android.synthetic.main.item_articles_list.view.*

/**
 * @author moustafasamhoury
 * created on Tuesday, 22 Oct, 2019
 */

class ArticlesListAdapter(private val onRowClicked: ((View, Int) -> Any)? = null) :
    ListAdapter<NYArticle, ArticlesListAdapter.ArticleViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<NYArticle>() {
            override fun areItemsTheSame(oldItem: NYArticle, newItem: NYArticle): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: NYArticle, newItem: NYArticle): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder =
        ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_articles_list, parent, false
            ), onRowClicked
        )

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) =
        holder.bind(getItem(position))

    class ArticleViewHolder(
        view: View,
        private val onRowClicked: ((View, Int) -> Any)? = null
    ) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener {
                onRowClicked?.invoke(it, adapterPosition)
            }
        }

        fun bind(item: NYArticle) {
            itemView.textViewArticleName.text = item.id
        }
    }
}
