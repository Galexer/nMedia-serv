package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dto.Post
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit


class PostRepositoryImpl : PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAll(callBack: PostRepository.CallBack<List<Post>>) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callBack.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        callBack.onError(Exception(response.message))
                        return
                    }
                    val data: List<Post>? = response.body?.string()?.let {
                        gson.fromJson(it, typeToken.type)
                    }
                    data ?: run {
                        callBack.onError(Exception("Body is null"))
                        return
                    }

                    callBack.onSuccess(data)
                }
            })

    }

    override fun likeById(id: Long, callBack: PostRepository.CallBack<Long>) {
        val request: Request = Request.Builder()
            .post("".toRequestBody(jsonType))
            .url("${BASE_URL}/api/posts/$id/likes")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callBack.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    callBack.onSuccess(id)
                }
            })
    }

    override fun dislike(id: Long, callBack: PostRepository.CallBack<Long>) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/posts/$id/likes")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callBack.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    callBack.onSuccess(id)
                }
            })
    }

    override fun save(post: Post, callBack: PostRepository.CallBack<Post>) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callBack.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    callBack.onSuccess(post)
                }
            })
    }

    override fun removeById(id: Long, callBack: PostRepository.CallBack<Long>) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callBack.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    callBack.onSuccess(id)
                }
            })
    }
}
