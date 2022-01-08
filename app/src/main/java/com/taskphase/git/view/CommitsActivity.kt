package com.taskphase.git.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.taskphase.git.R
import com.taskphase.git.adapters.CommitAdapter
import com.taskphase.git.databinding.ActivityCommitsBinding
import com.taskphase.git.network.Retrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class CommitsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommitsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_commits)
        intent.getStringExtra("BRANCH")?.let {
            binding.materialToolbar.subtitle = it
            setSupportActionBar(binding.materialToolbar)

            fetchCommits(it)
        }
    }

    private fun fetchCommits(branchName: String) {
        CoroutineScope(IO).launch {
            val response = Retrofit.api().getCommits(branchName)
            if (response.isSuccessful){
                runOnUiThread {
                   response.body()?.let {
                       val adapter = CommitAdapter(response.body()!!)

                       binding.recCommits.apply {
                           layoutManager = LinearLayoutManager(this@CommitsActivity)
                           this.adapter = adapter
                       }
                   }
                }
            }
        }
    }
}