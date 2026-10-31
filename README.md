# MyHabbit

Aplikasi Android sederhana untuk melacak kebiasaan dan aktivitas secara berkala. Dibangun dengan Kotlin, Jetpack Compose, dan arsitektur Clean Architecture + MVVM.

## Fitur Utama

- **CRUD Aktivitas** - Buat, edit, dan hapus kegiatan
- **Periode Berbeda** - Dukungan harian, mingguan, dan bulanan
- **Status Tracking** - Tandai kegiatan sebagai Pending, Completed, atau Failed
- **Dashboard** - Tampilan kegiatan aktif untuk periode saat ini
- **Riwayat** - Lihat catatan pelaksanaan kegiatan sebelumnya
- **Ringkasan & Statistik** - Evaluasi konsistensi dan tingkat penyelesaian
- **Grafik** - Visualisasi progres kegiatan
- **Offline-First** - Dapat digunakan tanpa koneksi internet untuk fungsi inti
- **Backup & Restore** - Backup data ke Google Drive (opsional)

## Tech Stack

| Komponen | Teknologi |
|---|---|
| Bahasa | Kotlin |
| UI Framework | Jetpack Compose |
| Design System | Material 3 |
| Arsitektur | Clean Architecture + MVVM |
| Navigation | Navigation Compose |
| Dependency Injection | Hilt |
| Database Lokal | Room (SQLite) |
| Async | Kotlin Coroutines & Flow |
| Autentikasi | Google Sign-In (opsional) |
| Cloud Storage | Google Drive API (opsional) |

## Struktur Proyek

```
com.myhabbit.app/
├── core/           # Utilitas, konstanta, dan helper
├── data/           # Data layer (Room, Repository impl, Backup)
├── di/             # Dependency Injection modules
├── domain/         # Business logic (Model, UseCase, Repository interface)
├── presentation/   # UI layer (Screen, ViewModel, Navigation)
└── ui/theme/       # Tema aplikasi
```

## Cara Menjalankan

1. Clone repository ini
2. Buka project di Android Studio
3. Sync Gradle
4. Jalankan aplikasi di emulator atau device

```bash
./gradlew assembleDebug
```

## Dokumentasi

- [PRD.md](PRD.md) - Product Requirements Document
- [ARCHITECTURE.md](ARCHITECTURE.md) - Arsitektur teknis
- [PROMPT.md](PROMPT.md) - Panduan pengembangan AI
- [GIT_WORKFLOW.md](GIT_WORKFLOW.md) - Workflow Git & GitHub

## Lisensi

Proyek ini dikembangkan untuk penggunaan pribadi.
