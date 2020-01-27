package com.hojongs.entity

import com.hojongs.SUDOKU_SIZE
import com.hojongs.SudokuRow
import reactor.core.publisher.Flux

class Sudoku(
    val sudokuId: Long? = null,
    private val blocks: Array<SudokuRow> = Array(SUDOKU_SIZE) {
        IntArray(SUDOKU_SIZE) { 0 }
    }
) {

    fun getRow(
        row_idx: Int
    ): SudokuRow = blocks[row_idx].clone()

    fun getRowFlux(): Flux<SudokuRow> = Flux
        .fromArray(blocks.clone())

    fun isEmptyBlock(
        row_idx: Int,
        col_idx: Int
    ): Boolean = blocks[row_idx][col_idx] == 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Sudoku

        if (!blocks.contentDeepEquals(other.blocks)) return false

        return true
    }

    override fun hashCode(): Int {
        return blocks.contentDeepHashCode()
    }
}
