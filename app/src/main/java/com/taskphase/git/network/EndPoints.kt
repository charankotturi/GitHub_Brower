package com.taskphase.git.network

import com.taskphase.git.models.Branch
import com.taskphase.git.models.Commits
import com.taskphase.git.models.Issue
import com.taskphase.git.models.Repository
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EndPoints {

    @GET("jquery")
    suspend fun getRepo(): Response<Repository>

    @GET("jquery/branches")
    suspend fun getBranches(): Response<List<Branch>>

    @GET("jquery/commits?")
    suspend fun getCommits(
        @Query("sha")
        branch: String
    ): Response<List<Commits>>

    @GET("jquery/issues?state=open")
    suspend fun getIssues(): Response<List<Issue>>

}