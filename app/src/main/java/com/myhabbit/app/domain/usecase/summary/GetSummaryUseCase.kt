package com.myhabbit.app.domain.usecase.summary

import com.myhabbit.app.domain.model.HabitCategory
import com.myhabbit.app.domain.model.Summary
import com.myhabbit.app.domain.service.CompletionCalculator
import com.myhabbit.app.data.local.dao.HabitRecordDao
import com.myhabbit.app.data.mapper.toDomain
import javax.inject.Inject

/**
 * Summary seluruh record, atau difilter per kategori
 * (ARCHITECTURE.md §12 GetSummaryUseCase).
 */
class GetSummaryUseCase @Inject constructor(
    private val habitRecordDao: HabitRecordDao
) {

    suspend operator fun invoke(
        category: HabitCategory? = null
    ): Summary {
        val records = habitRecordDao.getAllRecords()
            .map { it.toDomain() }
            .filter { category == null || it.periodType == category }
        return CompletionCalculator.calculate(records)
    }
}
