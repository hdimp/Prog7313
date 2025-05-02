package com.example.prog7313
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    //--------------------------------------------
    //
    //--------------------------------------------

    fun insertUser(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertUser(user)
        }
    }

    //--------------------------------------------
    //
    //--------------------------------------------

    fun getUserByUsername(username: String) = liveData(Dispatchers.IO) {
        emit(repository.getUserByUsername(username))
    }

}

//--------------------------------------------
//
//--------------------------------------------

class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}