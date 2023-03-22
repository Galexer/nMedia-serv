package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(callBack: CallBack<List<Post>>)
    fun likeById(callBack: CallBack<Long>, id: Long)
    fun dislike(callBack: CallBack<Long>, id: Long)
    fun save(callBack: CallBack<Post>, post: Post)
    fun removeById(callBack: CallBack<Long>, id: Long)

    interface CallBack <T> {
        fun onSuccess (data: T)
        fun onError (e: Exception)
    }
}
