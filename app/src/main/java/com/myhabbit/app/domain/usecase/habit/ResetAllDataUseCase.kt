package com.myhabbit.app.domain.usecase.habit

import com.myhabbit.app.core.result.AppResult
import com.myhabbit.app.domain.repository.HabitRecordRepository
import com.myhabbit.app.domain.repository.HabitRepository
import javax.inject.Inject

/**
 * "Hard Reset": hapus SEMUA data habit (habits + records) — aplikasi
 * kembali seperti baru (permintaan user — mulai semuanya dari awal).
 * Akun Google & pengaturan (tema/notifikasi) TIDAK dihapus.
 * Dipanggil hanya SETELAH konfirmasi user (PRD.md §34).
 */
class ResetAllDataUseCase @Inject constructor(
    private val habitRepository: HabitRepository,
    private val recordRepository: HabitRecordRepository
) {

    suspend operator fun invoke(): AppResult<Unit> {
        return try {
            // Records dulu agar FK/relasi tidak yatim saat habits terhapus.
            recordRepository.deleteAllRecords()
            habitRepository.deleteAllHabits()
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error("Gagal reset semua data: ${e.message}")
        }
    }
}
