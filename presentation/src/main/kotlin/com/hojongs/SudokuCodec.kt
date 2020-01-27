package com.hojongs

import com.google.protobuf.Empty
import com.hojongs.entity.BlockPosition
import com.hojongs.entity.BlockPositionProto
import com.hojongs.entity.Sudoku
import com.hojongs.entity.SudokuProto
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.toFlux

object SudokuCodec {

    fun encode(
        sudoku: Sudoku
    ): SudokuProto.Sudoku = sudoku
        .getRowFlux()
        .flatMap { Flux.fromArray(it.toTypedArray()) }
        .map(SudokuCodec::encodeSudokuBlock)
        .buffer(SUDOKU_SIZE)
        .map { sudokuBlocks ->
            SudokuProto.SudokuRow
                .newBuilder()
                .addAllSudokuBlocks(sudokuBlocks)
                .build()
        }
        .collectList()
        .map { sudokuRows ->
            SudokuProto.Sudoku
                .newBuilder()
                .addAllSudokuRows(sudokuRows)
                .build()
        }
        .subscribeOn(Schedulers.single())
        .block()!! // needs reactor support

    fun decode(
        protoSudoku: SudokuProto.Sudoku
    ): Sudoku = protoSudoku
        .sudokuRowsList
        .toFlux()
        .flatMap {
            it.sudokuBlocksList.toFlux()
        }
        .map { sudokuBlock ->
            sudokuBlock.digit
        }
        .buffer(SUDOKU_SIZE)
        .map { it.toIntArray() }
        .collectList()
        .map { it.toTypedArray() }
        .map {
            Sudoku(
                sudokuId = protoSudoku.sudokuId,
                blocks = it
            )
        }
        .block()!!

    fun encodeSudokuBlock(
        digit: Int
    ): SudokuProto.SudokuBlock = SudokuProto.SudokuBlock
        .newBuilder()
        .apply {
            when (digit) {
                0 -> this.empty = Empty.getDefaultInstance()
                else -> this.digit = digit
            }
        }
        .build() // needs reactor support

    fun decodeBlockPosition(
        blockPosition: BlockPositionProto.BlockPosition
    ): BlockPosition = BlockPosition(
        rowIdx = blockPosition.rowIdx,
        colIdx = blockPosition.colIdx
    )

    fun encodeBlockPosition(
        blockPosition: BlockPosition
    ): BlockPositionProto.BlockPosition = BlockPositionProto.BlockPosition
        .newBuilder()
        .setRowIdx(blockPosition.rowIdx)
        .setColIdx(blockPosition.colIdx)
        .build()
}
