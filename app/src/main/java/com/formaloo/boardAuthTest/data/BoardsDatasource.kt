package com.formaloo.boardAuthTest.data


/**
 * Implementation of [BoardsService] interface
 */

class BoardsDatasource(private val service: BoardsService) {
    fun getSharedBoardDetail(shared_board_slug: String) =
        service.getSharedBoardDetail(shared_board_slug)

}
