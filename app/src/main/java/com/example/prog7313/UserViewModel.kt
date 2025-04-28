package com.example.prog7313
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    //Insert new User
    fun insertUser(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertUser(user)
        }
    }

    //Get a user by username
    fun getUserByUsername(username: String) = liveData(Dispatchers.IO) {
        emit(repository.getUserByUsername(username))
    }

    // Get all users
    fun getAllUsers() = liveData(Dispatchers.IO) {
        emit(repository.getAllUsers())
    }
}

class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}