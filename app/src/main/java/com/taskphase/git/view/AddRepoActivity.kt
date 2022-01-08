package com.taskphase.git.view

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.taskphase.git.R
import com.taskphase.git.databinding.ActivityAddRepoBinding
import com.taskphase.git.models.Repository
import com.taskphase.git.network.Retrofit
import com.taskphase.git.utils.Resource
import com.taskphase.git.utils.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.random.Random

class AddRepoActivity : AppCompatActivity() {

    private val gson = Gson()
    private val reposType = object : TypeToken<List<Repository>>(){}.type

    private lateinit var binding: ActivityAddRepoBinding
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_repo)
        userPreferences = UserPreferences(this)

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.btnSubmitRepo.setOnClickListener {
            val repo_name = binding.etRepoName.text.toString()
            val owner = binding.etOwner.text.toString()

            if (owner.isEmpty()) {
                toast("Enter Owner Name")
                return@setOnClickListener }
            if (repo_name.isEmpty()) {
                toast("Enter Repo Name*")
                return@setOnClickListener }

            val newRepo = Repository("This repo is owned by $owner", "https://github.com/jquery/$repo_name", (999..1000).random(), repo_name)

            lifecycleScope.launch {
                setData(newRepo)
            }
        }
    }

    private suspend fun setData(repository: Repository) {
        var list = ArrayList<Repository>()

        userPreferences.getRepoList.collect {
            Log.i(TAG, "setData: >>>>>>>$it")
            if (it != "") {
                list = gson.fromJson(it, reposType) as ArrayList<Repository>
            }

            list.add(repository)
            userPreferences.saveRepoList(gson.toJson(list.distinct()))
            startActivity(Intent(this@AddRepoActivity, MainActivity::class.java))
        }
    }

    private fun toast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
}