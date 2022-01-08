package com.taskphase.git.view

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.taskphase.git.R
import com.taskphase.git.adapters.BranchesAdapter
import com.taskphase.git.databinding.ActivityDetailsBinding
import com.taskphase.git.models.Branch
import com.taskphase.git.models.Repository
import com.taskphase.git.network.Retrofit
import com.taskphase.git.utils.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailsActivity : AppCompatActivity() {

    private val gson = Gson()
    private val reposType = object : TypeToken<Repository>(){}.type
    private val listReposType = object : TypeToken<List<Repository>>(){}.type


    private lateinit var binding: ActivityDetailsBinding
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)
        userPreferences = UserPreferences(this)

        binding.imgBack.setOnClickListener {
            finish()
        }

        val repo = intent.getStringExtra("REPO")
        repo?.let{
            val repository: Repository = gson.fromJson(it, reposType)
            binding.txtTitle.text = repository.name
            binding.txtDes.text = repository.description

            binding.imgEye.setOnClickListener {
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(repository.html_url)
                startActivity(openURL)
            }

            setUpBranchesAndIssueCount(repository)
        }
    }

    private fun setUpBranchesAndIssueCount(repository: Repository) {
        binding.txtBranches.setOnClickListener {
            if (repository.name == "jquery"){
                startActivity(Intent(this, CommitsActivity::class.java))
            }else{
                Toast.makeText(this, "No branches were created!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.imgDelete.setOnClickListener {
            lifecycleScope.launch{
                deleteRepo(repository)
            }
        }

        if (repository.name == "jquery"){
            CoroutineScope(Dispatchers.IO).launch {
                val response = Retrofit.api().getIssues()
                if (response.isSuccessful){
                    runOnUiThread{
                        binding.txtIssues.text = "Issues ( ${response.body()?.size ?: 0} )"
                    }
                }

                val branches = Retrofit.api().getBranches()
                if (branches.isSuccessful && !branches.body().isNullOrEmpty()){
                    runOnUiThread {
                        val adapter = BranchesAdapter(branches.body()!!) {
                            val intent = Intent(this@DetailsActivity, CommitsActivity::class.java)
                            intent.apply {
                                putExtra("BRANCH", it)
                            }
                            startActivity(intent)
                        }

                        binding.recBranches.apply {
                            this.adapter = adapter
                            layoutManager = LinearLayoutManager(this@DetailsActivity)
                        }
                    }
                }
            }

        }else{
            val adapter = BranchesAdapter(listOf(Branch("Main"))) {
                Toast.makeText(this@DetailsActivity, "No Commits", Toast.LENGTH_SHORT).show()
            }

            binding.recBranches.apply {
                this.adapter = adapter
                layoutManager = LinearLayoutManager(this@DetailsActivity)
            }
        }
    }

    private suspend fun deleteRepo(repository: Repository) {
        userPreferences.getRepoList.collect { stringList ->
            val list = gson.fromJson(stringList, listReposType) as ArrayList<Repository>

            Log.i(TAG, "deleteRepo: >>>>>>>>>>>>${list.contains(repository)}")
            list.remove(repository)

            //Change Later!
            userPreferences.saveRepoList(gson.toJson(list.distinct()))
            startActivity(Intent(this@DetailsActivity, MainActivity::class.java))
        }
    }
}