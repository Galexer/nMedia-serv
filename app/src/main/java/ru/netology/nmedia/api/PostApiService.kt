package ru.netology.nmedia.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.*
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://10.0.2.2:9999/api/slow/"

private val client = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .run{
        if(BuildConfig.DEBUG) {
            this.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })

        }else {
            this
        }
    }
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface PostApiService {

    @GET("posts")
    fun getPosts(): Call<List<Post>>

    @DELETE("posts/{id}")
    fun deletePostById(@Path("id") postId: Long): Call<Unit>

    @DELETE("posts/{id}/likes")
    fun dislikeById(@Path("id") postId: Long): Call<Post>

    @POST("posts/{id}/likes")
    fun likeById(@Path("id") postId: Long): Call<Post>

    @POST("posts")
    fun save(@Body post: Post): Call<Post>
}

object PostApi {
    val service: PostApiService by lazy {
        retrofit.create()
    }
}