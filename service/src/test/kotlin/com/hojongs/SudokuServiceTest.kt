package com.hojongs

import com.hojongs.entity.BlockPosition
import com.hojongs.entity.Sudoku
import com.hojongs.repository.SudokuRepository
import com.hojongs.service.SudokuService
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import reactor.test.StepVerifier

class SudokuServiceTest {
    private val sudokuRepository = mockk<SudokuRepository>()
    private val sudokuService = spyk(SudokuService(sudokuRepository))

    @Test
    fun `test getSudoku()`() {
        val sudoku = Sudoku()
        every { sudokuRepository.get(any()) } returns sudoku.toMono()

        StepVerifier
            .create(
                sudokuService
                    .getSudoku(
                        sudokuId = 1
                    )
            )
            .expectNext(sudoku)
            .verifyComplete()
    }

    @Test
    fun `test updateBlockDigit()`() {
        every { sudokuRepository.updateBlockDigit(any(), any(), any()) } returns Mono.empty()

        StepVerifier
            .create(
                sudokuService
                    .updateBlockDigit(
                        sudokuId = 1,
                        blockPosition = BlockPosition(0, 0),
                        digit = 1
                )
            )
            .verifyComplete()
    }

    @Test
    fun `test emptyBlockDigit()`() {
        every { sudokuRepository.updateBlockDigit(any(), any(), any())} returns Mono.empty()

        StepVerifier
            .create(
                sudokuService.emptyBlockDigit(
                    sudokuId = 1,
                    blockPosition = BlockPosition(0, 0)
                )
            )
            .verifyComplete()
    }

    @Test
    fun `test completeSudoku()`() {
        val sudoku = Sudoku()

        every { sudokuRepository.complete(any())} returns sudoku.toMono()
        StepVerifier
            .create(
                sudokuService.completeSudoku(
                    sudoku = sudoku
                )
            )
            .expectNext(sudoku)
            .verifyComplete()
    }
}
