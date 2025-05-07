package com.swallaby.foodon.presentation.signup.viewmodel

import com.swallaby.foodon.core.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(

) : BaseViewModel<SignUpUiState>(SignUpUiState()) {

}