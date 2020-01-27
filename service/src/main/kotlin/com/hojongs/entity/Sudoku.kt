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

    fun validate(): List<BlockPosition> = listOf(
        *validateRows().toTypedArray(),
        *validateCols().toTypedArray(),
        *validate3x3().toTypedArray()
    )

    private fun validateRows(): List<BlockPosition> {
        val blockPositions = ArrayList<BlockPosition>()

        for (row_idx in 0 until SUDOKU_SIZE) {
            val digitSet = (0 until SUDOKU_SIZE).toMutableSet()

            for (col_idx in 0 until SUDOKU_SIZE) {
                val digit = blocks[row_idx][col_idx]

                digitSet.takeIf { it.contains(digit) }
                    ?.let { digitSet.remove(digit) }
                    ?: blockPositions.add(BlockPosition(row_idx, col_idx))
            }
        }

        return blockPositions
    }

    private fun validateCols(): List<BlockPosition> {
        val blockPositions = ArrayList<BlockPosition>()

        for (col_idx in 0 until SUDOKU_SIZE) {
            val digitSet = (0 until SUDOKU_SIZE).toMutableSet()

            for (row_idx in 0 until SUDOKU_SIZE) {
                val digit = blocks[row_idx][col_idx]

                digitSet.takeIf { it.contains(digit) }
                    ?.let { digitSet.remove(digit) }
                    ?: blockPositions.add(BlockPosition(row_idx, col_idx))
            }
        }

        return blockPositions
    }

    private fun validate3x3(): List<BlockPosition> {
        assert(SUDOKU_SIZE == 9)

        val blockPositions = ArrayList<BlockPosition>()

        for (row_idx in listOf(0, 3, 6)) {
            for (col_idx in listOf(0, 3, 6)) {
                val digitSet = (0 until SUDOKU_SIZE).toMutableSet()

                for (i in 0 until 3) {
                    for (k in 0 until 3) {
                        val digit = blocks[row_idx + i][col_idx + k]

                        digitSet.takeIf { it.contains(digit) }
                            ?.let { digitSet.remove(digit) }
                            ?: blockPositions.add(BlockPosition(row_idx, col_idx))
                    }
                }
            }
        }

        return blockPositions
    }

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
