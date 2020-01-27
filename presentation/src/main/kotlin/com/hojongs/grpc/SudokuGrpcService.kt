package com.hojongs.grpc

import com.google.protobuf.Empty
import com.hojongs.SudokuCodec
import com.hojongs.service.SudokuService
import com.hojongs.entity.SudokuProto
import org.lognet.springboot.grpc.GRpcService
import reactor.core.publisher.Mono

@GRpcService
class SudokuGrpcService(
    private val sudokuService: SudokuService
) : ReactorSudokuServiceGrpc.SudokuServiceImplBase() {

    override fun getSudoku(
        request: Mono<GetSudokuRequest>
    ): Mono<SudokuProto.Sudoku> = request
        .flatMap { req ->
            sudokuService.getSudoku(
                sudokuId = req.sudokuId
            )
        }
        .map(SudokuCodec::encode)

    override fun insertBlockDigit(
        request: Mono<InsertBlockDigitRequest>
    ): Mono<Empty> = request
        .flatMap { req ->
            sudokuService.updateBlockDigit(
                sudokuId = req.sudokuId,
                blockPosition = SudokuCodec.decodeBlockPosition(req.blockPos),
                digit = req.digit
            )
        }
        .map { Empty.getDefaultInstance() }

    override fun emptyBlockDigit(
        request: Mono<EmptyBlockDigitRequest>
    ): Mono<Empty> = request
        .map { req ->
            sudokuService.emptyBlockDigit(
                sudokuId = req.sudokuId,
                blockPosition = SudokuCodec.decodeBlockPosition(req.blockPos)
            )
        }
        .map { Empty.getDefaultInstance() }

    override fun completeSudoku(
        request: Mono<SudokuProto.Sudoku>
    ): Mono<Empty> = request
        .map { sudoku ->
            sudokuService.completeSudoku(
                sudoku = SudokuCodec.decode(sudoku)
            )
        }
        .map { Empty.getDefaultInstance() }
}
