package com.formaloo.boardAuthTest


import androidx.lifecycle.viewModelScope
import com.formaloo.boardAuthTest.data.BoardRepo
import com.formaloo.boardAuthTest.data.model.Board
import com.formaloo.boardAuthTest.data.model.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber


/**
 * UI state for the Home screen
 */
data class BoardUiState(
    val board: Board? = null,
    val loading: Boolean = false,
    val errorMessages: List<String> = emptyList()
) {
    /**
     * True if this represents a first load
     */
    val initialLoad: Boolean
        get() = errorMessages.isEmpty() && loading
}


class BoardViewModel(private val repository: BoardRepo) : BaseViewModel() {

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(BoardUiState(loading = true))
    val uiState: StateFlow<BoardUiState> = _uiState.asStateFlow()

    fun refreshBoard(boardAddress: String) {

        // Ui state is refreshing
        _uiState.update { it.copy(loading = true) }


        viewModelScope.launch {


            val result = repository.getBoard(boardAddress)
            Timber.e("Board result $result")

            _uiState.update {
                Timber.e("Board $it")
                when (result) {
                    is Result.Success -> {
                        val board = result.data.data?.board

                        it.copy(
                            board = board,
                            loading = false
                        )
                    }

                    is Result.Error -> {
                        val errorMessages = it.errorMessages
                        Timber.e("Board errorMessages $errorMessages")

                        it.copy(errorMessages = errorMessages, loading = false)
                    }

                }
            }
        }
    }


}
