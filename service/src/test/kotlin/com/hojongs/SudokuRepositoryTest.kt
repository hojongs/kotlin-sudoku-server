package com.hojongs

import com.hojongs.entity.BlockPosition
import com.hojongs.entity.Sudoku
import com.hojongs.repository.SudokuRepository
import io.mockk.spyk
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier

class SudokuRepositoryTest {
    private val sudokuRepository = spyk(SudokuRepository())

    @Test
    fun `test get()`() {
        val sudoku = Sudoku()

        StepVerifier
            .create(
                sudokuRepository.get(
                    sudokuId = 1
                )
            )
            .expectNext(sudoku)
            .verifyComplete()
    }

    @Test
    fun `test updateBlockDigit()`() {
        StepVerifier
            .create(
                sudokuRepository.updateBlockDigit(
                    sudokuId = 1,
                    blockPosition = BlockPosition(0, 0),
                    digit = 1
                )
            )
            .verifyComplete()
    }

    @Test
    fun `test complete()`() {
        val sudoku = Sudoku()

        StepVerifier
            .create(
                sudokuRepository.complete(
                    sudoku = sudoku
                )
            )
            .expectNext(sudoku)
            .verifyComplete()
    }
}
