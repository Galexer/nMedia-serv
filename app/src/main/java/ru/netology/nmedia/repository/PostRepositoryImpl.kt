package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.dto.Post
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit
import retrofit2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PostRepositoryImpl : PostRepository {

    override fun getAll(callBack: PostRepository.CallBack<List<Post>>) {
        PostApi.service.getPosts()
            .enqueue(object : Callback<List<Post>>{
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if(!response.isSuccessful){
                        callBack.onError(RuntimeException(response.message()))
                        return
                    }

                    callBack.onSuccess(response.body() ?: throw RuntimeException("Bode is null"))
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    callBack.onError(RuntimeException(t))
                }
            })

    }

    override fun likeById(id: Long, callBack: PostRepository.CallBack<Post>) {
        PostApi.service.likeById(id)
            .enqueue(object : Callback<Post>{
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if(!response.isSuccessful){
                        callBack.onError(RuntimeException(response.message()))
                        return
                    }

                    callBack.onSuccess(response.body() ?: throw RuntimeException("Bode is null"))
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callBack.onError(RuntimeException(t))
                }

            })
    }

    override fun dislike(id: Long, callBack: PostRepository.CallBack<Post>) {
        PostApi.service.dislikeById(id)
            .enqueue(object : Callback<Post>{
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if(!response.isSuccessful){
                        callBack.onError(RuntimeException(response.message()))
                        return
                    }

                    callBack.onSuccess(response.body() ?: throw RuntimeException("Bode is null"))
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callBack.onError(RuntimeException(t))
                }

            })
    }

    override fun save(post: Post, callBack: PostRepository.CallBack<Post>) {
        PostApi.service.save(post)
            .enqueue(object : Callback<Post>{
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if(!response.isSuccessful){
                        callBack.onError(RuntimeException(response.message()))
                        return
                    }

                    callBack.onSuccess(response.body() ?: throw RuntimeException("Bode is null"))
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callBack.onError(RuntimeException(t))
                }

            })
    }

    override fun removeById(id: Long, callBack: PostRepository.CallBack<Unit>) {
        PostApi.service.deletePostById(id)
            .enqueue(object : Callback<Unit>{
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if(!response.isSuccessful){
                        callBack.onError(RuntimeException(response.message()))
                        return
                    }

                    callBack.onSuccess(response.body() ?: throw RuntimeException("Bode is null"))
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callBack.onError(RuntimeException(t))
                }

            })
    }
}
