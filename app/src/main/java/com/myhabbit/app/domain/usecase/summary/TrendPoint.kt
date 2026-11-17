package com.myhabbit.app.domain.usecase.summary

/**
 * Satu titik data tren (PRD.md §28 / mockup UI.png #7):
 * label sumbu-x (hari/minggu/bulan) + persentase completion 0..100.
 */
data class TrendPoint(
    val label: String,
    val percentage: Double
)
