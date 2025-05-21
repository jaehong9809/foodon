package com.swallaby.foodon.data.auth.remote.result

sealed class AuthFlowResult {
    object NavigateToMain : AuthFlowResult()
    object NavigateToSignUp : AuthFlowResult()
}