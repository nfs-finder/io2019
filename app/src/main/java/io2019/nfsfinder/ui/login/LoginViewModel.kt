package io2019.nfsfinder.ui.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Patterns
import io2019.nfsfinder.data.login.LoginRepository
import io2019.nfsfinder.data.login.Result

import io2019.nfsfinder.R
import io2019.nfsfinder.data.login.LoginRepositorySingleton
import io2019.nfsfinder.data.database.WrongCredentialsException

class LoginViewModel : ViewModel() {
    private val loginRepository: LoginRepository = LoginRepositorySingleton.getInstance().loginRepository

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        loginRepository.onResultChange = {
            val res = it!!

            if (res is Result.Success) {
                _loginResult.value = LoginResult(success = LoggedInUserView(displayName = res.data.displayName))
            } else if (res is Result.Error && res.exception is WrongCredentialsException) {
                _loginResult.value = LoginResult(error = R.string.wrong_credentials)
            } else {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }

        loginRepository.login(username, password)
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
