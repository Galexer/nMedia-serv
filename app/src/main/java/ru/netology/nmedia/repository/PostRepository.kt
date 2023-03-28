package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(callBack: CallBack<List<Post>>)
    fun likeById(id: Long, callBack: CallBack<Long>)
    fun dislike(id: Long, callBack: CallBack<Long>)
    fun save(post: Post, callBack: CallBack<Post>)
    fun removeById(id: Long, callBack: CallBack<Long>)

    interface CallBack <T> {
        fun onSuccess (data: T)
        fun onError (e: Exception)
    }
}
