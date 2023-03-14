package com.formaloo.boardAuthTest.data.model.detail

import com.formaloo.boardAuthTest.data.model.Board
import java.io.Serializable

data class BoardDetailData(
    var board: Board? = null
) : Serializable
