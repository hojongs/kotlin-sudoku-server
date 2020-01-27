package com.hojongs.entity

import com.google.common.annotations.VisibleForTesting
import com.hojongs.SUDOKU_SIZE
import com.hojongs.SudokuRow
import reactor.core.publisher.Flux

// TODO : initial block digit should be immutable
data class Sudoku(
    val sudokuId: Long? = null,
    private val blocks: Array<SudokuRow> = Array(SUDOKU_SIZE) {
        IntArray(SUDOKU_SIZE) { 0 }
    },
    private val indexRange: IntRange = 0 until SUDOKU_SIZE,
    private val digitRange: IntRange = 1..SUDOKU_SIZE
) {

    fun getRow(
        row_idx: Int
    ): SudokuRow = blocks[row_idx].clone()

    fun getRowFlux(): Flux<SudokuRow> = Flux
        .fromArray(blocks.clone())

    fun isEmptyBlock(
        blockPosition: BlockPosition
    ): Boolean = blocks[blockPosition.rowIdx][blockPosition.colIdx] == 0

    fun updateBlockDigit(
        blockPosition: BlockPosition,
        digit: Int
    ): Sudoku = blocks.clone()
        .let { blocksToUpdate ->
            digit.takeIf { digit in 0..9 }
                ?.run {
                    blocksToUpdate.apply {
                        this[blockPosition.rowIdx][blockPosition.colIdx] = digit
                    }
                }
                ?: throw IllegalArgumentException()
        }
        .let { blocksToUpdate ->
            copy(
                sudokuId = sudokuId,
                blocks = blocksToUpdate
            )
        }

    fun emptyBlockDigit(
        blockPosition: BlockPosition
    ): Sudoku = updateBlockDigit(
        blockPosition = blockPosition,
        digit = 0
    )

    fun validate(): Set<BlockPosition>? {
        val errorPosSet = HashSet<BlockPosition>()

        validateRows(errorPosSet = errorPosSet)
        validateCols(errorPosSet = errorPosSet)
        validate3x3(errorPosSet = errorPosSet)

        return errorPosSet.takeIf { it.count() != 0 }
    }

    @VisibleForTesting
    /* private */ fun validateRows(
        errorPosSet: MutableSet<BlockPosition>
    ) {
        indexRange.forEach { rowIdx ->
            val digitSet = digitRange.toMutableSet()

            indexRange.forEach { colIdx ->
                validateBlock(
                    blockPosition = BlockPosition(rowIdx, colIdx),
                    digitSet = digitSet,
                    errorPosSet = errorPosSet
                )
            }
        }
    }

    @VisibleForTesting
    /* private */ fun validateCols(
        errorPosSet: MutableSet<BlockPosition>
    ) {
        indexRange.forEach { colIdx ->
            val digitSet = digitRange.toMutableSet()

            indexRange.forEach { rowIdx ->
                validateBlock(
                    blockPosition = BlockPosition(rowIdx, colIdx),
                    digitSet = digitSet,
                    errorPosSet = errorPosSet
                )
            }
        }
    }

    @VisibleForTesting
    /* private */ fun validate3x3(
        errorPosSet: MutableSet<BlockPosition>
    ) {
        assert(SUDOKU_SIZE == 9)

        listOf(0, 3, 6).forEach { rowIdx ->
            listOf(0, 3, 6).forEach { colIdx ->
                val digitSet = digitRange.toMutableSet()

                (0..2).forEach { i ->
                    (0..2).forEach { k ->
                        validateBlock(
                            blockPosition = BlockPosition(rowIdx + i, colIdx + k),
                            digitSet = digitSet,
                            errorPosSet = errorPosSet
                        )
                    }
                }
            }
        }
    }

    @VisibleForTesting
    /* private */ fun validateBlock(
        blockPosition: BlockPosition,
        digitSet: MutableSet<Int>,
        errorPosSet: MutableSet<BlockPosition>
    ) {
        val rowIdx = blockPosition.rowIdx
        val colIdx = blockPosition.colIdx
        val digit = blocks[rowIdx][colIdx]

        if (digit == 0)
            return

        digitSet.takeIf { it.contains(digit) }
            ?.let { digitSet.remove(digit) }
            ?: errorPosSet.add(BlockPosition(rowIdx, colIdx))
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
