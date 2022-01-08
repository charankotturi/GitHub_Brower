package com.taskphase.git.view

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.taskphase.git.R
import com.taskphase.git.adapters.MainRepoAdapter
import com.taskphase.git.databinding.ActivityMainBinding
import com.taskphase.git.models.Repository
import com.taskphase.git.network.Retrofit
import com.taskphase.git.utils.Resource
import com.taskphase.git.utils.UserPreferences
import com.taskphase.git.viewmodels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val liveRepo = MutableLiveData<Resource<List<Repository>>>()
    private val gson = Gson()
    private val reposType = object : TypeToken<List<Repository>>(){}.type

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        userPreferences = UserPreferences(this)

        binding.btnAddRepo.setOnClickListener {
            goToAddRepoPage()
        }

        binding.imgAddRepo.setOnClickListener {
            goToAddRepoPage()
        }

        binding.imgRefresh.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                fetchFromNetwork()?.let { it1 -> setData(it1) }
            }
        }

        userPreferences.getRepoList.asLiveData().observe(this) {
            if (it != "") {
                val list = gson.fromJson(it, reposType) as ArrayList<Repository>
                Log.i(TAG, "onCreate: hello its me>>>>>>>>....>")
                liveRepo.value = Resource.Success(list)
            } else {
                liveRepo.value = Resource.Success(emptyList())
            }
        }

        liveRepo.observe(this){ list ->
            when(list){
                is Resource.Error -> {
                    // Show Error!
                    Log.i(TAG, "onCreate: >>>>>>>>>>>>error")
                }
                is Resource.Loading -> {
                    // Show Loading!
                    Log.i(TAG, "onCreate: >>>>>>>>>>>>loading")

                }
                is Resource.Success -> {
                    Log.i(TAG, "onCreate: >>>>>>>>>>>>null")
                    if (list.data == null) return@observe
                    Log.i(TAG, "onCreate: >>>>>>>>>>>>${list.data.toString()}")

                    if (list.data.isEmpty()){
                        binding.emptyRepo.visibility = View.VISIBLE
                        binding.nonEmptyRepo.visibility = View.GONE
                        return@observe
                    }

                    binding.emptyRepo.visibility = View.GONE
                    binding.nonEmptyRepo.visibility = View.VISIBLE

                    val adapter = MainRepoAdapter(list.data!!, { url ->
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, url)
                            type = "text/plain"
                        }

                        val shareIntent = Intent.createChooser(sendIntent, null)
                        startActivity(shareIntent)
                    }, { repo ->
                        val intent = Intent(this@MainActivity, DetailsActivity::class.java).apply {
                            putExtra("REPO", gson.toJson(repo))
                        }
                        startActivity(intent)
                    })

                    binding.recRepos.apply {
                        this.adapter = adapter
                        this.layoutManager = LinearLayoutManager(this@MainActivity)
                    }
                }
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
        }
    }

    private suspend fun fetchFromNetwork(): Repository? {
        val response = Retrofit.api().getRepo()

        if (response.isSuccessful){
            return response.body()
        }

        return null
    }

    private fun goToAddRepoPage() {
        startActivity(Intent(this, AddRepoActivity::class.java))
    }

}