package com.hojongs

import com.hojongs.repository.SudokuRepository
import com.hojongs.service.SudokuService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SudokuServiceProvider {

    @Bean
    fun sudokuRepository() = SudokuRepository()

    @Bean
    fun sudokuService(
        sudokuRepository: SudokuRepository
    ) = SudokuService(sudokuRepository)
}
