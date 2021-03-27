package com.pedrogomez.hnmob.models.result

sealed class Result{
    data class Success(val finished: Boolean):Result()
    data class LoadingNewContent(val status: Boolean):Result()
    data class LoadingMoreContent(val status: Boolean):Result()
    sealed class Error(val errorMsg: String):Result() {
        class RecoverableError(errorMsg: String) : Error(errorMsg)
        class NonRecoverableError(errorMsg: String) :
            Error(errorMsg)
    }
}