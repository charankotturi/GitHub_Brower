package com.taskphase.git.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.taskphase.git.R
import com.taskphase.git.databinding.RvBranchItemBinding
import com.taskphase.git.models.Branch

class BranchesAdapter(
    val list: List<Branch>,
    private val onClick: (branch: String) -> Unit,
) : RecyclerView.Adapter<BranchesAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: RvBranchItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = list[position]

            binding.txtBranchName.setOnClickListener {
                onClick(item.name)
            }

            binding.txtBranchName.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
     = ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.rv_branch_item, parent , false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = list.size
}