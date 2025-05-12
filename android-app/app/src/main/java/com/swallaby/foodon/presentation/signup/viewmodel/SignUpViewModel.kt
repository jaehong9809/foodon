package com.swallaby.foodon.presentation.signup.viewmodel

import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.presentation.calendar.viewmodel.CalendarUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(

) : BaseViewModel<SignUpUiState>(SignUpUiState()) {

    fun updateState(block: (SignUpUiState) -> SignUpUiState) {
        _uiState.update(block)
    }

    fun selectGender(gender: String) {
        updateState { it.copy(selectedGender = gender) }
    }

}