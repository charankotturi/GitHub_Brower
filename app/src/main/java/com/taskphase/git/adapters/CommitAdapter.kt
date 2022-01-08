package com.taskphase.git.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.taskphase.git.R
import com.taskphase.git.databinding.RvCommitItemBinding
import com.taskphase.git.models.Commits

class CommitAdapter(
    private val list: List<Commits>
) : RecyclerView.Adapter<CommitAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: RvCommitItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = list[position]

            binding.txtDate.text = item.commit.committer.date
            binding.txtCommitId.text = item.author.id.toString()
            binding.txtDescription.text = item.commit.message
            binding.txtUserName.text = item.commit.committer.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
     = ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.rv_commit_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = list.size
}