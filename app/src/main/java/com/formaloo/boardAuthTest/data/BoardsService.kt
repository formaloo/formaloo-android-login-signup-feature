package com.formaloo.boardAuthTest.data

import com.formaloo.boardAuthTest.data.model.detail.BoardDetailRes
import retrofit2.Call
import retrofit2.http.*

interface BoardsService {
    companion object {
        private const val SHARED_BOARD = "v4/shared-boards/{board_share_address}/"
    }

    @GET(SHARED_BOARD)
    fun getSharedBoardDetail(
        @Path("board_share_address") board_slug: String
    ): Call<BoardDetailRes>


}
