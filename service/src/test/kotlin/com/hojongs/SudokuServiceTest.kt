package com.hojongs

import com.hojongs.entity.BlockPosition
import com.hojongs.entity.Sudoku
import com.hojongs.repository.SudokuRepository
import com.hojongs.service.SudokuService
import io.kotlintest.shouldBe
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
        every { sudokuRepository.updateBlockDigit(any(), any(), any()) } returns Mono.empty()

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
    fun `test validateRows()`() {
        val sudoku = Sudoku(
            sudokuId = 1L,
            blocks = arrayOf(
                arrayOf(1, 1, 0, 0, 0, 0, 4, 3, 0).toIntArray(),
                arrayOf(7, 0, 0, 0, 0, 0, 0, 0, 0).toIntArray(),
                arrayOf(0, 0, 0, 2, 5, 4, 9, 0, 0).toIntArray(),
                arrayOf(1, 7, 0, 0, 4, 0, 2, 0, 6).toIntArray(),
                arrayOf(0, 0, 0, 0, 9, 0, 0, 0, 3).toIntArray(),
                arrayOf(0, 0, 3, 0, 0, 6, 0, 8, 0).toIntArray(),
                arrayOf(0, 0, 1, 4, 7, 0, 0, 6, 0).toIntArray(),
                arrayOf(0, 0, 0, 5, 0, 8, 1, 2, 0).toIntArray(),
                arrayOf(0, 9, 0, 0, 6, 0, 3, 0, 4).toIntArray()
            )
        )

        val errorPosSet = HashSet<BlockPosition>()
        sudoku.validateRows(errorPosSet)
        println(errorPosSet)
        errorPosSet.containsAll(
            listOf(
                BlockPosition(0, 0),
                BlockPosition(0, 1)
            )
        ) shouldBe true
    }

    @Test
    fun `test validateSudoku()`() {
        val sudoku = Sudoku(
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
        )

        StepVerifier
            .create(
                sudokuService
                    .validateSudoku(sudoku = sudoku)
            )
            .verifyComplete()

        val sudoku2 = sudoku.updateBlockDigit(
            blockPosition = BlockPosition(0, 0),
            digit = 7
        )

        StepVerifier
            .create(
                sudokuService
                    .validateSudoku(sudoku = sudoku2)
            )
            .assertNext { errorPosSet ->
                errorPosSet.size shouldBe 2
                println(errorPosSet)
                errorPosSet.contains(BlockPosition(1, 0)) shouldBe true
                errorPosSet.contains(BlockPosition(1, 1)) shouldBe true
            }
            .verifyComplete()
    }

    @Test
    fun `test completeSudoku()`() {
        val sudoku = Sudoku()

        every { sudokuRepository.complete(any()) } returns sudoku.toMono()
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
