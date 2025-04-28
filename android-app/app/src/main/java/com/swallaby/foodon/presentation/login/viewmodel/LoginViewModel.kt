package com.swallaby.foodon.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import com.swallaby.foodon.domain.auth.usecase.LoginWithKakaoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWithKakaoUseCase: LoginWithKakaoUseCase
) : ViewModel() {

}