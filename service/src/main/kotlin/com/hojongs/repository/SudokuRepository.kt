package com.hojongs.repository

import com.hojongs.entity.BlockPosition
import com.hojongs.entity.Sudoku
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

class SudokuRepository {

    fun get(
        sudokuId: Long
    ): Mono<Sudoku> = when (sudokuId) {
        1L -> Sudoku(
            sudokuId = 1L,
            blocks = arrayOf(
                arrayOf(0, 1, 0, 0, 0, 0, 4, 3, 0).toIntArray(),
                arrayOf(7, 0, 0, 0, 0, 0, 0, 0, 0).toIntArray(),
                arrayOf(0, 0, 0, 2, 5, 4, 9, 0, 0).toIntArray(),
                arrayOf(1, 7, 0, 0, 4, 0, 2, 0, 6).toIntArray(),
                arrayOf(0, 0, 0, 0, 9, 0, 0, 0, 3).toIntArray(),
                arrayOf(0, 0, 3, 0, 0, 6, 0, 8, 0).toIntArray(),
                arrayOf(0, 0, 1, 4, 7, 0, 0, 6, 0).toIntArray(),
                arrayOf(0, 0, 0, 5, 0, 8, 1, 2, 0).toIntArray(),
                arrayOf(0, 9, 0, 0, 6, 0, 3, 0, 4).toIntArray()
            )
        ).toMono()
        else -> Mono.error(IllegalArgumentException())
    }

    fun updateBlockDigit(
        sudokuId: Long,
        blockPosition: BlockPosition,
        digit: Int
    ): Mono<Unit> = Mono.empty() // TODO : exposed dependency

    fun complete(
        sudoku: Sudoku
    ): Mono<Sudoku> = Mono.empty() // TODO ; exposed dependency
}
