package com.example.myapplication.adapter


import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.Countitem
import kotlinx.android.synthetic.main.item.view.*


class ArticlesAdapter: RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder>() {

    inner class ArticlesViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    private val diffCallback = object : DiffUtil.ItemCallback<Countitem>(){
        override fun areItemsTheSame(oldItem: Countitem, newItem: Countitem): Boolean {
            return oldItem.word == newItem.word
        }

        override fun areContentsTheSame(oldItem: Countitem, newItem: Countitem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Countitem>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        return ArticlesViewHolder(
                LayoutInflater.from(
                        parent.context
                ).inflate(
                        R.layout.item,
                        parent,
                        false
                )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {

        val item = differ.currentList[position]
        var i=0

        holder.itemView.apply {
            name.text = "${item.word.toString()}"
            count.text="${item.count}"
        }
    }
}