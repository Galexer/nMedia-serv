package ru.netology.nmedia.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import kotlinx.coroutines.withContext
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.*
import ru.netology.nmedia.util.SingleLiveEvent

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    likes = 0,
    published = 0
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {
        _data.value = FeedModel(loading = true)
        repository.getAll(object : PostRepository.CallBack<List<Post>> {
            override fun onSuccess(data: List<Post>) {
                _data.postValue(FeedModel(posts = data, empty = data.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }

        })
    }

    fun save() {
        edited.value?.let {
            repository.save(it, object : PostRepository.CallBack<Post> {
                override fun onSuccess(data: Post) {
                    _postCreated.postValue(Unit)
                }

                override fun onError(e: Exception) {
                    return
                }
            })
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun likeById(id: Long) {
        val old = _data.value?.posts.orEmpty()
        val oldLike = _data.value?.posts?.find { it.id == id }

        if (oldLike != null) {
            if (!oldLike.likedByMe) {
                repository.likeById(id, object : PostRepository.CallBack<Post> {
                    override fun onSuccess(data: Post) {
                        _data.postValue(
                            _data.value?.copy(posts = _data.value?.posts.orEmpty()
                                .map {
                                    if (it.id == id) it.copy(
                                        likedByMe = !it.likedByMe, likes = it.likes + 1
                                    ) else it
                                })
                        )
                    }

                    override fun onError(e: Exception) {
                        _data.postValue(_data.value?.copy(posts = old))
                        _data.postValue(FeedModel(toastError = true))
                        loadPosts()
                    }

                })
            } else {
                repository.dislike(id, object : PostRepository.CallBack<Post> {
                    override fun onSuccess(data: Post) {
                        _data.postValue(
                            _data.value?.copy(posts = _data.value?.posts.orEmpty()
                                .map {
                                    if (it.id == id) it.copy(
                                        likedByMe = !it.likedByMe, likes = it.likes - 1
                                    ) else it
                                })
                        )
                    }

                    override fun onError(e: Exception) {
                        _data.postValue(_data.value?.copy(posts = old))
                        _data.postValue(FeedModel(toastError = true))
                        loadPosts()
                    }
                })
            }
        }
    }

    fun removeById(id: Long) {
        val old = _data.value?.posts.orEmpty()
        repository.removeById(id, object : PostRepository.CallBack<Unit> {
            override fun onSuccess(data: Unit) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .filter { it.id != id }
                    )
                )
            }

            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(posts = old))
                _data.postValue(FeedModel(toastError = true))
                loadPosts()
            }

        })
    }
}
