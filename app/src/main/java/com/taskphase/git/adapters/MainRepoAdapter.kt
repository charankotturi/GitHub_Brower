package com.taskphase.git.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.taskphase.git.R
import com.taskphase.git.databinding.RvRepoItemBinding
import com.taskphase.git.models.Repository

class MainRepoAdapter(
    private val list: List<Repository>,
    private val launchShareIntent: (url: String) -> Unit,
    private val onItemClick: (repo: Repository) -> Unit,
) : RecyclerView.Adapter<MainRepoAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: RvRepoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = list[position]

            binding.txtTitle.text = item.name
            binding.txtDes.text = item.description
            binding.parent.setOnClickListener {
                onItemClick(item)
            }
            binding.imgShare.setOnClickListener {
                launchShareIntent(item.html_url)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.rv_repo_item, parent, false))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = list.size
}