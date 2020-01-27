package com.hojongs.service

import com.hojongs.entity.BlockPosition
import com.hojongs.entity.Sudoku
import com.hojongs.repository.SudokuRepository
import reactor.core.publisher.Mono

class SudokuService(
    private val sudokuRepository: SudokuRepository
) {

    fun getSudoku(
        sudokuId: Long
    ): Mono<Sudoku> = sudokuRepository.get(
        sudokuId = sudokuId
    )

    fun updateBlockDigit(
        sudokuId: Long,
        blockPosition: BlockPosition,
        digit: Int
    ): Mono<Unit> = sudokuRepository.updateBlockDigit(
        sudokuId = sudokuId,
        blockPosition = blockPosition,
        digit = digit
    )

    fun emptyBlockDigit(
        sudokuId: Long,
        blockPosition: BlockPosition
    ): Mono<Unit> = sudokuRepository.updateBlockDigit(
        sudokuId = sudokuId,
        blockPosition = blockPosition,
        digit = 0
    )

    fun completeSudoku(
        sudoku: Sudoku
    ): Mono<Sudoku> = sudokuRepository.complete(
        sudoku = sudoku
    )
}
