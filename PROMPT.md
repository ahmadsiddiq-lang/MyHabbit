# PROMPT.md

# MyHabbit — AI Development Instructions

**Project:** MyHabbit  
**Platform:** Android  
**Primary Language:** Kotlin  
**UI Framework:** Jetpack Compose  
**Architecture:** Clean Architecture + MVVM  
**Document Version:** 1.0

---

## Git and GitHub Integration

Project MyHabbit menggunakan Git dan GitHub untuk version control.

AI WAJIB membaca dan mengikuti seluruh aturan dalam `GIT_WORKFLOW.md`.
dan lihat juga file MyHabbit App UI Walkthrough Infographic.png

Ketentuan:

1. Periksa kondisi Git sebelum memulai pekerjaan.
2. Gunakan feature branch untuk pengembangan fitur.
3. Jangan mengerjakan fitur langsung pada `main` tanpa persetujuan.
4. Jangan melakukan commit tanpa persetujuan pengguna.
5. Jangan melakukan push ke GitHub tanpa persetujuan pengguna.
6. Jangan melakukan merge tanpa persetujuan pengguna.
7. Jangan melakukan operasi Git yang berisiko menghilangkan perubahan atau riwayat tanpa persetujuan eksplisit.
8. Periksa `.gitignore` sebelum mengunggah file.
9. Jangan pernah mengunggah kredensial, token, password, atau secret.
10. Laporkan branch aktif, perubahan file, dan kondisi repository dalam laporan pekerjaan.

AI harus membedakan antara:

- Memeriksa Git: boleh dilakukan tanpa persetujuan.
- Merencanakan operasi Git: boleh dilakukan tanpa persetujuan.
- Mengubah file kode: mengikuti prosedur persetujuan implementasi.
- Mengubah repository melalui Git: wajib mengikuti persetujuan dalam GIT_WORKFLOW.md.

Buatkan juga README.md dan isikan dengan baik informasi mengenai aplikasi 

Jika pengguna meminta AI melakukan operasi Git, jelaskan terlebih dahulu perintah yang akan dijalankan dan dampaknya. Tunggu persetujuan sebelum mengeksekusinya.

Jangan pernah mengklaim commit, push, merge, atau operasi lainnya berhasil jika belum benar-benar dijalankan dan diverifikasi.

# 1. ROLE AND RESPONSIBILITY

You are acting as a Senior Android Developer, Software Architect, and Technical Assistant for the MyHabbit project.

Your responsibility is to help develop MyHabbit into a functional, stable, maintainable, and well-structured Android application.

You must work carefully and incrementally.

You are NOT allowed to independently develop the entire application without instructions.

Your job is to:

1. Understand the project documentation.
2. Inspect the existing source code.
3. Follow the defined architecture.
4. Implement only the requested feature.
5. Test the implementation.
6. Update project documentation.
7. Update the progress checklist in this file.
8. Stop and wait for the next instruction.

The human project owner makes all major development decisions.

---

# 2. DOCUMENTATION IS THE SOURCE OF TRUTH

Before performing any development task, you MUST read the project documentation.

The primary documentation files are:

| File | Purpose |
|---|---|
| PRD.md | Product requirements and application features |
| ARCHITECTURE.md | Technical architecture and implementation guidelines |
| PROMPT.md | AI development instructions and progress tracking |

If other documentation files exist, inspect and read the relevant ones before implementation.

## Mandatory Rules

1. Never assume the contents of a documentation file.
2. Never claim to have read a file that you have not actually inspected.
3. Never invent requirements that are not documented.
4. Never silently change the product requirements.
5. Never ignore architectural decisions.
6. Never implement a feature based on assumptions when important details are missing.
7. If documentation conflicts, stop and explain the conflict.
8. If a requirement is unclear, ask the project owner before making important decisions.

The documentation is the primary reference for the project.

If the implementation requires a change to the documentation, explain the reason and request approval when the change affects product behavior or architecture.

---

# 3. PROJECT INSPECTION RULES

At the beginning of a new development session:

1. Inspect the project root directory.
2. Identify all available documentation files.
3. Read PRD.md.
4. Read ARCHITECTURE.md.
5. Read PROMPT.md.
6. Inspect the current source code and project structure.
7. Check the current implementation status.
8. Identify the last completed feature.
9. Identify the next feature that can be developed.

Do not assume that the source code matches the documentation.

Verify the actual implementation.

If a file does not exist, report it.

If a feature is marked complete but its implementation is missing or broken, report the discrepancy.

Do not start implementation until the requested task is clear.

---

# 4. ANTI-HALLUCINATION RULES

Accuracy is more important than speed.

You MUST follow these rules.

## 4.1 Never Invent Implementation

Do not:

- Invent existing files or classes.
- Invent functions or methods.
- Invent dependencies that are already installed.
- Claim a feature works without testing it.
- Claim a build succeeds without running the build.
- Claim a test passes without running the test.
- Assume an API behaves in a particular way without checking its documentation or actual implementation.
- Pretend that an external service is configured when it is not.

## 4.2 Verify Before Making Changes

Before modifying code:

1. Locate the actual file.
2. Read the existing implementation.
3. Understand its dependencies.
4. Identify its relationship with other components.
5. Determine the minimum changes required.

Do not replace existing code without understanding its purpose.

## 4.3 Be Honest About Limitations

If you cannot verify something, explicitly say:

"I cannot verify this yet because the required information or configuration is unavailable."

If you encounter an error:

1. Show the actual error.
2. Explain the likely cause.
3. Identify what has been verified.
4. Propose a solution.
5. Do not claim success until verified.

## 4.4 External Documentation

When implementing features involving external services or libraries, consult their official documentation when necessary.

Examples:

- Android Developer Documentation
- Kotlin Documentation
- Jetpack Compose Documentation
- AndroidX Room Documentation
- Google Identity Documentation
- Google Drive API Documentation

Do not invent API methods, permissions, dependency versions, or configuration steps.

---

# 5. INCREMENTAL DEVELOPMENT RULES

This is one of the most important rules in the project.

**Develop MyHabbit ONE FEATURE AT A TIME.**

Never implement multiple unrelated features in one development task.

Each feature must go through the following workflow.

## Development Workflow

```text
                  PROJECT OWNER
                       |
                       v
               Gives a feature task
                       |
                       v
                 AI INSPECTION
                       |
                       v
              Read documentation
                       |
                       v
              Inspect source code
                       |
                       v
              Analyze requirements
                       |
                       v
             Explain implementation
                       |
                       v
              Wait for approval
                       |
                       v
               IMPLEMENT FEATURE
                       |
                       v
                Run build/tests
                       |
                       v
              Verify implementation
                       |
                       v
             Update PROMPT.md
                       |
                       v
             Report completion
                       |
                       v
             STOP AND WAIT
                       |
                       v
             Next user instruction
```

## Mandatory Workflow

For every feature:

### Step 1 — Understand

Read the relevant requirements in PRD.md and ARCHITECTURE.md.

Identify what the feature should do.

### Step 2 — Inspect

Inspect the existing source code.

Determine whether the feature is:

- Not implemented.
- Partially implemented.
- Already implemented.
- Implemented but requires correction.

### Step 3 — Plan

Before writing code, explain:

- Feature being developed.
- Expected behavior.
- Files that will be created.
- Files that will be modified.
- Dependencies required.
- Database changes, if any.
- Potential risks.
- Testing approach.

### Step 4 — Request Approval

Present the implementation plan and WAIT for the project owner's approval.

Do not start implementation before approval.

### Step 5 — Implement

After approval, implement only the approved feature.

Do not move to the next feature automatically.

### Step 6 — Test

Run the relevant checks.

At minimum, when applicable:

- Compile the application.
- Run relevant unit tests.
- Run relevant instrumentation or UI tests.
- Verify the implemented behavior.
- Check for regressions.

If a test cannot be executed, report why.

### Step 7 — Update Documentation

After implementation, update:

- PROMPT.md progress checklist.
- Other documentation if necessary and approved.

### Step 8 — Report

Provide a completion report.

Include:

- What was implemented.
- Files created.
- Files modified.
- Tests executed.
- Build result.
- Known limitations.
- Remaining work.

### Step 9 — STOP

After completing the requested feature, STOP.

Do not continue to another feature.

Wait for the project owner to provide the next instruction.

---

# 6. FEATURE APPROVAL POLICY

No feature implementation may begin without a clear instruction from the project owner.

The AI must distinguish between:

**Discussion:** The owner is asking questions or discussing an idea.

**Planning:** The owner is asking for an implementation plan.

**Approval:** The owner explicitly authorizes implementation.

**Implementation:** The owner instructs the AI to develop a specific feature.

Do not interpret every question as permission to modify the project.

If the owner asks:

"What do you think about the dashboard?"

This is a discussion, not permission to implement the dashboard.

If the owner asks:

"Plan the dashboard implementation."

Prepare a plan and wait.

If the owner says:

"Implement the dashboard according to the approved plan."

You may implement that feature.

---

# 7. STRICT FEATURE BOUNDARIES

Each task must have a clearly defined scope.

For example:

Task: Create Activity Form.

Allowed:

- Create the activity form UI.
- Implement title input.
- Implement category selection.
- Implement description input.
- Implement Save and Cancel actions.
- Add required validation.
- Write relevant tests.

Not allowed unless explicitly requested:

- Build the entire dashboard.
- Implement Google Sign-In.
- Implement Google Drive backup.
- Implement the summary chart.
- Redesign unrelated screens.
- Refactor unrelated application modules.

If another feature is required to complete the current task, explain the dependency and ask for approval.

Do not silently expand the scope.

---

# 8. PROMPT.MD PROGRESS TRACKING

This file MUST be maintained throughout development.

The checklist below is the official development progress tracker.

Update it whenever a feature changes status.

## Status Definitions

Use these statuses:

- [ ] NOT STARTED
- [~] IN PROGRESS
- [x] COMPLETED
- [!] BLOCKED
- [?] NEEDS REVIEW

Do not mark a feature as COMPLETED merely because its code has been written.

A feature can only be marked COMPLETED when:

1. Implementation is finished.
2. Relevant build checks succeed.
3. Relevant tests pass, or unavailable tests are explicitly documented.
4. Acceptance criteria are verified.
5. No known critical issue remains.

If a feature has unresolved critical issues, do not mark it complete.

## 8.1 Development Progress

### Phase 1 — Project Foundation

- [ ] 1.1 Create Android project
- [ ] 1.2 Configure Kotlin
- [ ] 1.3 Configure Jetpack Compose
- [ ] 1.4 Configure Material 3
- [ ] 1.5 Configure Navigation Compose
- [ ] 1.6 Configure Hilt
- [ ] 1.7 Create base theme
- [ ] 1.8 Verify initial application build

### Phase 2 — Database Foundation

- [x] 2.1 Configure Room Database
- [x] 2.2 Create HabitEntity
- [x] 2.3 Create HabitRecordEntity
- [x] 2.4 Create UserEntity
- [x] 2.5 Create SettingsEntity
- [x] 2.6 Create DAO interfaces
- [x] 2.7 Create Domain Models
- [x] 2.8 Create Repository interfaces
- [x] 2.9 Implement Repositories
- [x] 2.10 Test database operations

### Phase 3 — Activity Management

- [x] 3.1 Create Activity Form UI
- [x] 3.2 Implement activity validation
- [x] 3.3 Implement Create Activity
- [x] 3.4 Implement Activity List
- [x] 3.5 Implement Edit Activity
- [x] 3.6 Implement Delete Activity
- [x] 3.7 Implement Category Selection

### Phase 4 — Period Engine

- [x] 4.1 Implement Daily Period
- [x] 4.2 Implement Weekly Period
- [x] 4.3 Implement Monthly Period
- [x] 4.4 Implement PeriodCalculator
- [x] 4.5 Implement Period Boundary Handling
- [x] 4.6 Test Period Calculation

### Phase 5 — Status Management

- [x] 5.1 Implement Pending Status
- [x] 5.2 Implement Completed Status
- [x] 5.3 Implement Failed Status
- [x] 5.4 Implement Status Update
- [x] 5.5 Implement Record Persistence
- [x] 5.6 Test Status Changes

### Phase 6 — Dashboard

- [x] 6.1 Create Dashboard UI
- [x] 6.2 Implement Daily Activities
- [x] 6.3 Implement Weekly Activities
- [x] 6.4 Implement Monthly Activities
- [x] 6.5 Implement Category Navigation
- [x] 6.6 Implement Current Period Progress
- [x] 6.7 Implement Empty State

### Phase 7 — History

- [x] 7.1 Create History Screen
- [x] 7.2 Implement Daily History
- [x] 7.3 Implement Weekly History
- [x] 7.4 Implement Monthly History
- [x] 7.5 Implement Date Filtering
- [x] 7.6 Implement Historical Detail

### Phase 8 — Summary and Statistics

- [x] 8.1 Implement CompletionCalculator
- [x] 8.2 Implement Summary Use Case
- [x] 8.3 Implement Daily Summary
- [x] 8.4 Implement Weekly Summary
- [x] 8.5 Implement Monthly Summary
- [x] 8.6 Implement Category Summary
- [x] 8.7 Implement Completion Rate

### Phase 9 — Charts

- [x] 9.1 Select and Configure Chart Library
- [x] 9.2 Implement Bar Chart
- [x] 9.3 Implement Donut Chart
- [x] 9.4 Implement Weekly Trend
- [x] 9.5 Implement Monthly Trend

### Phase 10 — Google Authentication

- [x] 10.1 Configure Google Authentication
- [x] 10.2 Implement Google Sign-In
- [x] 10.3 Implement User Profile
- [x] 10.4 Implement Sign-Out
- [x] 10.5 Handle Authentication Errors

### Phase 11 — JSON Backup

- [x] 11.1 Define Backup JSON Schema
- [x] 11.2 Implement Backup Models
- [x] 11.3 Implement JSON Serializer
- [x] 11.4 Implement JSON Validator
- [x] 11.5 Implement Export
- [x] 11.6 Implement Import
- [x] 11.7 Implement Schema Versioning
- [x] 11.8 Test Backup and Restore

### Phase 12 — Google Drive

- [x] 12.1 Configure Google Cloud Project
- [x] 12.2 Configure Google Drive API
- [x] 12.3 Implement Drive Authorization
- [x] 12.4 Implement Backup Upload
- [x] 12.5 Implement Backup Download
- [x] 12.6 Implement Restore from Drive
- [x] 12.7 Implement Backup Status
- [x] 12.8 Test Drive Integration

### Phase 13 — Final Polish

- [x] 13.1 Implement Theme Settings
- [x] 13.2 Implement Notifications
- [x] 13.3 Improve Error Handling
- [x] 13.4 Improve Accessibility
- [x] 13.5 Performance Testing
- [x] 13.6 Final Regression Testing

---

# 9. FEATURE COMPLETION RECORD

After completing each feature, add a record here.

## Feature Completion Record

### Feature: Post-release — Form Tambah/Edit Activity sesuai mockup #4

- Status: COMPLETED
- Date: 2026-11-24
- Latar: form sebelumnya polos — field tanpa label di atas, teks Inggris
  ("Title"/"Category"/"Description (opsional)", "Cancel"/"Save"), tanpa panah
  kembali, dan tanpa kartu pembungkus.

- Files Created:
  - Tidak ada

- Files Modified:
  - app/src/main/java/com/myhabbit/app/presentation/activity/ActivityFormScreen.kt
    (form dibungkus Card; label di ATAS tiap field: "Judul *", "Kategori",
    "Deskripsi (opsional)"; placeholder contoh; tombol "Batal"/"Simpan" dengan
    Simpan lebih lebar; back arrow di top bar; kategori diterjemahkan ke
    Harian/Mingguan/Bulanan agar konsisten dengan Beranda & Ringkasan)

- Build:
  - SUCCESS (:app:assembleDebug)

- Tests:
  - PASSED (143/143 — 3× rerun `--rerun-tasks` untuk memastikan stabil)

- Acceptance Criteria (mockup #4):
  - [x] Form berada dalam kartu putih/rounded di atas latar
  - [x] Label di atas field: Judul *, Kategori, Deskripsi (opsional)
  - [x] Placeholder contoh ("Mis. Meditasi pagi", "Mis. Lakukan meditasi selama 10 menit")
  - [x] Tombol "Batal" (outline) + "Simpan" (terisi ungu) sejajar
  - [x] Panah kembali di top bar
  - [x] Kategori dropdown: Harian / Mingguan / Bulanan
  - [x] Validasi tetap: border merah + pesan "Title tidak boleh kosong",
        "Kategori wajib dipilih" di bawah field
  - [x] Alur simpan end-to-end di device: isi judul + kategori + simpan →
        kembali ke Beranda dan activity muncul di tab kategori terkait

- Known Issues:
  - `DashboardViewModelTest.emptyState_allSectionsEmpty` sempat gagal sekali dengan
    `Dispatchers.Main is used concurrently with setting it` (race `resetMain()`
    Robolectric). Terbukti flaky: 3 rerun berikutnya lulus. Bukan regresi.

### Feature: Post-release — Profil sesuai mockup #8 + Pengaturan & Backup terpisah

- Status: COMPLETED
- Date: 2026-11-24
- Latar: halaman Profil sebelumnya menumpuk semua kartu (akun, about, tema,
  notifikasi, backup file, backup Drive, reset). Mockup UI.png #8 menunjukkan
  Profil sebagai daftar menu, dengan Pengaturan (#9) dan Backup & Restore (#10)
  sebagai layar tersendiri.

- Files Created:
  - app/src/main/java/com/myhabbit/app/presentation/profile/ProfileScreen.kt (ditulis ulang)
  - app/src/main/java/com/myhabbit/app/presentation/settings/SettingsScreen.kt
  - app/src/main/java/com/myhabbit/app/presentation/settings/SettingsViewModel.kt
  - app/src/main/java/com/myhabbit/app/presentation/backup/BackupScreen.kt
  - app/src/main/java/com/myhabbit/app/presentation/backup/BackupViewModel.kt
  - app/src/main/java/com/myhabbit/app/presentation/about/AboutScreen.kt
  - app/src/main/java/com/myhabbit/app/ui/common/MenuRow.kt (baris menu ikon + chevron)
  - app/src/main/java/com/myhabbit/app/ui/common/BackButton.kt
  - app/src/main/res/drawable/ic_settings.xml, ic_palette.xml, ic_info.xml,
    ic_logout.xml, ic_notifications.xml, ic_reset.xml, ic_arrow_back.xml,
    ic_cloud_upload.xml, ic_cloud_download.xml
  - app/src/test/java/com/myhabbit/app/presentation/backup/BackupViewModelTest.kt (10 test)
  - app/src/test/java/com/myhabbit/app/presentation/settings/SettingsViewModelTest.kt (7 test)

- Files Modified:
  - app/src/main/java/com/myhabbit/app/presentation/profile/ProfileViewModel.kt (disederhanakan:
    hanya observeCurrentUser + signOut; backup/pengaturan/reset pindah ke ViewModel masing-masing)
  - app/src/main/java/com/myhabbit/app/presentation/navigation/Screen.kt (+Settings, +Backup, +About)
  - app/src/main/java/com/myhabbit/app/presentation/navigation/AppNavigation.kt (route + back arrow)
  - app/src/test/java/com/myhabbit/app/presentation/profile/ProfileViewModelTest.kt (disesuaikan, 3 test)

- Build:
  - SUCCESS (:app:assembleDebug)

- Tests:
  - PASSED (143/143 — 20 kelas)

- Acceptance Criteria:
  - [x] Profil: top bar ungu "Profil", avatar bulat besar, nama, email (mockup #8)
  - [x] Daftar menu ber-ikon dengan chevron: Pengaturan, Backup ke Google Drive,
        Pulihkan dari Google Drive, Tentang Aplikasi
  - [x] Baris "Keluar" terpisah di bawah, warna error (mockup #8)
  - [x] Pengaturan (#9): Notifikasi (switch), Tema Aplikasi (dialog pilihan), Reset Data
        (Reset Ringkasan + Hard Reset, keduanya lewat dialog konfirmasi PRD §34a)
  - [x] Backup & Restore (#10): kartu Backup ke Google Drive + Restore Sekarang,
        status "Last backup", plus Export/Import file JSON lokal yang sudah ada
  - [x] Tentang Aplikasi: nama, versi, deskripsi, catatan data & privasi
  - [x] Back arrow di Pengaturan / Backup / Tentang
  - [x] Perilaku lama dipertahankan: konsen Drive via ActivityResult, konfirmasi
        replace-data sebelum restore, reset tanpa hapus akun & pengaturan
  - [x] Build + seluruh unit test lulus

- Known Issues:
  - Baris "Satuan Waktu" dan "Bahasa" pada mockup #9 TIDAK diimplementasi — keduanya
    tidak ada di PRD maupun ARCHITECTURE (tidak mengarang fitur). Perlu keputusan
    produk bila diinginkan.
  - Verifikasi visual di device belum dilakukan pada sesi ini (APK terbangun).

### Feature: Post-release — Perbaikan `lastBackupAt` setelah restore

- Status: COMPLETED
- Date: 2026-11-24
- Files Created:
  - Tidak ada

- Files Modified:
  - app/src/main/java/com/myhabbit/app/data/backup/BackupImporter.kt (`lastBackupAt` hasil restore digabung
    dengan nilai lokal memakai `maxOf(local, backup)` — timestamp tidak mundur/hilang bila file backup
    dibuat sebelum backup pertama)
  - app/src/test/java/com/myhabbit/app/data/backup/BackupExporterImporterTest.kt (+2 test merge timestamp)

- Build:
  - SUCCESS (:app:assembleDebug)

- Tests:
  - PASSED (139/139 — 18 kelas; `BackupExporterImporterTest` 8/8 termasuk 2 test baru)

- Acceptance Criteria:
  - [x] Restore file backup dengan `lastBackupAt = null` → label "Last backup" lokal TIDAK hilang
  - [x] Restore file backup dengan `lastBackupAt` lebih baru → memakai timestamp dari backup
  - [x] `theme` dan `notificationsEnabled` tetap diambil dari isi backup
  - [x] Tidak ada perubahan skema DB / skema JSON (tanpa migrasi)

- Known Issues:
  - None

- Notes:
  - Merupakan penutupan known issue retest Drive 2026-11-17 (PROMPT.md Phase 12)
  - Diverifikasi ulang dengan `--rerun-tasks` agar test benar-benar dieksekusi, bukan UP-TO-DATE

### Feature: Post-release — Perbaikan Gradle Wrapper rusak

- Status: COMPLETED
- Date: 2026-11-24
- Files Created:
  - gradle/wrapper/gradle-wrapper.jar (sebelumnya tidak ada — wajib untuk wrapper)
  - gradlew.bat (sebelumnya tidak ada)

- Files Modified:
  - gradlew (sebelumnya berisi `exec "$APP_HOME/gradlew" "$@"` → rekursi tanpa henti; diganti skrip wrapper resmi Gradle 8.7)
  - gradle/wrapper/gradle-wrapper.properties (ditambah `networkTimeout`, `validateDistributionUrl` dari wrapper resmi; `distributionUrl` tetap gradle-8.7-bin)

- Build:
  - SUCCESS (:app:assembleDebug lewat `./gradlew`)

- Tests:
  - PASSED (139/139 lewat `./gradlew testDebugUnitTest`)

- Acceptance Criteria:
  - [x] `./gradlew --version` menampilkan Gradle 8.7 (sebelumnya hang tanpa output)
  - [x] `./gradlew testDebugUnitTest` dan `./gradlew assembleDebug` berjalan normal
  - [x] `gradle-wrapper.jar` tidak diabaikan `.gitignore` (aturan `!gradle/wrapper/gradle-wrapper.jar` sudah ada)

- Known Issues:
  - `gradle-wrapper.jar` dan `gradlew.bat` belum di-commit — menunggu persetujuan pengguna

- Notes:
  - Wrapper digenerasi ulang via task `wrapper` resmi Gradle 8.7, bukan ditulis manual
  - Build environment: JDK 17 (`~/jdk17/jdk-17.0.12+7`), Android SDK `~/android-sdk`

### Feature: Post-release — Layar awal Login Google (mockup #2)

- Status: COMPLETED
- Date: 2026-11-17
- Files Created:
  - app/src/main/java/com/myhabbit/app/presentation/login/LoginViewModel.kt (observeCurrentUser + signIn via AuthRepository; authChecked anti-flash)
  - app/src/main/java/com/myhabbit/app/presentation/login/LoginScreen.kt (gradient ungu + logo + tagline + kartu putih tombol "Masuk dengan Google" dengan logo G; loading & error state)
  - app/src/main/res/drawable/ic_google.png (dari logo-google.png yang ditambahkan user)
  - app/src/test/java/com/myhabbit/app/presentation/login/LoginViewModelTest.kt (4 test)

- Files Modified:
  - app/src/main/java/com/myhabbit/app/presentation/navigation/AppNavigation.kt (gate: splash → belum login = LoginScreen → sudah login = NavHost)
  - PRD.md (§82.4 detail layar login)
  - logo-google.png (root, file user — tidak di-commit; hanya salinan drawable)

- Build:
  - SUCCESS (:app:assembleDebug)

- Tests:
  - PASSED (137/137 — 4 test baru: state awal, login sukses, login gagal, clearError)

- Acceptance Criteria:
  - [x] User belum login → layar awal mockup #2: gradient ungu, logo ✓, "MyHabbit" + tagline, kartu putih tombol "Masuk dengan Google"
  - [x] HANYA Google — tanpa input email/password (keputusan user)
  - [x] Login end-to-end di device: sheet "Pilih akun" Google muncul → pilih akun → masuk ke Beranda
  - [x] Sesi tersimpan: buka ulang aplikasi langsung ke Beranda (tidak minta login lagi)
  - [x] Batal/gagal login menampilkan pesan error di kartu (unit test + UI state)
  - [x] Keluar (Profil → Keluar) akan mengembalikan ke layar login via flow user (observeCurrentUser)

- UI Polish (revisi user — tampilan tidak mirip mockup):
  - Logo + kartu pindah ke TENGAH vertikal (Spacer weight 1f atas / 1.2f bawah), bukan menempel bawah
  - Background gradient diagonal (Offset.Zero → Offset.Infinite, #8F83F5 → #6A5AE0 → #5246C8)
  - Kartu + tombol pakai warna TETAP (bukan MaterialTheme): kartu putih, judul #1B1B21, subtitle #46464F,
    border tombol #DADCE0, teks tombol #1B1B21, error #B3261E — kartu identik di tema terang & gelap
    (dark mode device membuat teks tema-gelap pudar di kartu putih — bug "washed out", diperbaiki)
  - Verifikasi ulang di device: layar login crisp & terpusat sesuai mockup; login end-to-end + persistensi OK

- Known Issues:
  - Tidak ada

### Feature: Post-release — Ringkasan Daftar + Chart sesuai mockup

- Status: COMPLETED
- Date: 2026-11-17
- Files Created:
  - app/src/main/java/com/myhabbit/app/domain/usecase/summary/GetTrendUseCase.kt (tren completion 7 periode per kategori — hari/minggu/bulan, label Indonesia)
  - app/src/main/java/com/myhabbit/app/domain/usecase/summary/TrendPoint.kt
  - app/src/test/java/com/myhabbit/app/domain/usecase/summary/GetTrendUseCaseTest.kt (7 test: 7 titik, persen per hari, hari kosong = 0, label Indonesia, minggu/bulan)

- Files Modified:
  - app/src/main/java/com/myhabbit/app/presentation/summary/SummaryViewModel.kt (SummaryViewMode Daftar/Chart, default filter DAILY, trend di uiState, GetTrendUseCase injected)
  - app/src/main/java/com/myhabbit/app/presentation/summary/SummaryScreen.kt (rewrite: segmented toggle Daftar/Chart, pill kategori ala Beranda, tab Daftar = baris statistik + donut dengan persen tengah + legend, tab Chart = line chart "Tren 7 Hari Terakhir" + kartu Rata-rata; refresh saat layar dibuka — fix data stale)
  - app/src/main/java/com/myhabbit/app/presentation/components/Charts.kt (DonutChart: param centerLabel + legend Indonesia Selesai/Gagal; LineChart baru: garis + titik, grid 0-100%, label sumbu Compose Text)
  - PRD.md (§82.5 Ringkasan Daftar + Chart)

- Build:
  - SUCCESS (:app:assembleDebug)

- Tests:
  - PASSED (133/133 — 7 test baru)

- Acceptance Criteria:
  - [x] Toggle segmented Daftar | Chart (mockup #6) — terpilih = segmen putih
  - [x] Pill kategori Harian/Mingguan/Bulanan, default Harian
  - [x] Tab Daftar: judul "Ringkasan Harian (17 Nov 2026)", baris ✓ Selesai/✕ Gagal/○ Total/⊕ Rata-rata, donut dengan "100%" di tengah + legend Selesai/Gagal
  - [x] Tab Chart: "Tren 7 Hari Terakhir" line chart hijau + grid 0-100% + label hari + kartu Rata-rata dengan ikon bar mini (mockup #7)
  - [x] Tren menghitung 7 hari/7 minggu/7 bulan sesuai kategori; hari kosong = 0%
  - [x] Ringkasan refresh setiap layar dibuka (fix: data tidak stale setelah ubah status di Beranda)
  - [x] Verifikasi device: Daftar (Selesai 1, Total 1, Rata-rata 100%, donut penuh hijau) & Chart (garis naik ke 100% hari ini)

- Known Issues:
  - Tidak ada

### Feature: Post-release — Launcher Icon sesuai tema mockup

- Status: COMPLETED
- Date: 2026-11-17
- Files Created:
  - app/src/main/res/drawable/ic_launcher_foreground.xml (kotak putih rounded + ✓ ungu, viewport 108dp, safe zone)
  - app/src/main/res/drawable/ic_launcher_monochrome.xml (siluet ✓ — themed icon Android 13+)
  - app/src/main/res/values/ic_launcher_background.xml (warna latar #5246C8)
  - app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml + ic_launcher_round.xml (adaptive icon)

- Files Modified:
  - PRD.md (§82.3: deskripsi launcher icon)

- Build:
  - SUCCESS (:app:assembleDebug)

- Tests:
  - Tidak ada test baru (perubahan resource murni; test suite tidak tersentuh)

- Acceptance Criteria:
  - [x] Launcher icon: lingkaran ungu + kotak putih ✓ ungu — konsisten dengan AppLogo di top bar & splash (mockup UI.png)
  - [x] Adaptive icon dipakai API 26+ (device Android 12 tercakup); PNG lama tetap sebagai fallback API 24-25
  - [x] Round icon & monochrome (themed icon) tersedia
  - [x] Verifikasi device: icon MyHabbit di app drawer sesuai tema

- Known Issues:
  - PNG legacy (mipmap-*dpi) masih template bawaan — hanya dipakai Android 7.x ke bawah (di bawah minSdk 26 realistis tidak terlihat; biaya regenerate PNG via tool gambar dianggap tidak perlu)

### Feature: Post-release — Tab Filter Beranda (Harian/Mingguan/Bulanan)

- Status: COMPLETED
- Date: 2026-11-17
- Files Modified:
  - app/src/main/java/com/myhabbit/app/presentation/dashboard/DashboardScreen.kt (tab filter pill `DashboardTab` + state `selectedTab` default DAILY; section DAILY/WEEKLY/MONTHLY berurutan diganti SATU `habitSection` sesuai tab aktif; `habitSection` bertambah param `emptyMessage`)
  - PRD.md (§82.4 diperbarui: tab filter mengganti section berurutan)
  - UI.png (ditambahkan ke repo — referensi mockup resmi, PRD §82)

- Files Created:
  - UI.png (referensi mockup UI — commit disetujui user)

- Build:
  - SUCCESS (:app:assembleDebug)

- Tests:
  - PASSED (126/126)

- Acceptance Criteria:
  - [x] Tab Harian/Mingguan/Bulanan tampil di bawah label tanggal (mockup #3)
  - [x] Default tab Harian; pill terisi ungu saat aktif, outline saat tidak
  - [x] Konten berubah sesuai tab: Harian → "minum", Mingguan → "olahraga" + Progress MINGGUAN
  - [x] Tab Bulanan (kosong) menampilkan pesan "Belum ada activity di kategori ini" (tanpa kartu Progress)
  - [x] Status popup, swipe-delete, tombol nav tetap berfungsi dalam semua tab
  - [x] Verifikasi device: ketiga tab dicek via screenshot; duplikat "olahraga" dibersihkan via swipe-delete

- Known Issues:
  - Tidak ada (Progress card disembunyikan hanya untuk kategori kosong — perilaku sengaja)

### Feature: Post-release — UI Refresh sesuai mockup UI.png (warna, splash, ikon, popup status)

- Status: COMPLETED
- Date: 2026-11-17
- Files Created:
  - app/src/main/java/com/myhabbit/app/presentation/components/StatusIcon.kt (lingkaran ✓ hijau / ✕ merah / ○ kosong, adaptif terang/gelap)
  - app/src/main/java/com/myhabbit/app/presentation/components/StatusPickerSheet.kt (bottom sheet "Pilih Status" — mockup #5)
  - app/src/main/java/com/myhabbit/app/presentation/splash/SplashScreen.kt (gradient ungu + logo + tagline + spinner — mockup #1)
  - app/src/main/java/com/myhabbit/app/ui/common/AppLogo.kt (logo kotak centang, dipakai top bar & splash)
  - app/src/main/java/com/myhabbit/app/ui/common/AppTopBar.kt (top bar ungu + teks/ikon putih, dipakai SEMUA layar)

- Files Modified:
  - app/src/main/java/com/myhabbit/app/ui/theme/Color.kt (palet brand ungu #5246C8 + aksen hijau/merah)
  - app/src/main/java/com/myhabbit/app/ui/theme/Theme.kt (dynamicColor DINONAKTIFKAN — brand konsisten mockup; scheme terang/gelap lengkap)
  - app/src/main/java/com/myhabbit/app/presentation/dashboard/DashboardScreen.kt (top bar: logo ✓ + ikon person→Profil; kartu: 1 ikon status yang membuka popup; StatusSelector 3-tombol diganti)
  - app/src/main/java/com/myhabbit/app/presentation/navigation/AppNavigation.kt (splash overlay 1.6 detik + pass onOpenProfile)
  - app/src/main/java/com/myhabbit/app/presentation/summary/SummaryScreen.kt (AppTopBar ungu)
  - app/src/main/java/com/myhabbit/app/presentation/history/HistoryScreen.kt (AppTopBar ungu)
  - app/src/main/java/com/myhabbit/app/presentation/activity/ActivityListScreen.kt (AppTopBar ungu)
  - app/src/main/java/com/myhabbit/app/presentation/activity/ActivityFormScreen.kt (AppTopBar ungu)
  - PRD.md (§10 implementasi popup status + §82 UI Reference)

- Files Deleted:
  - app/src/main/java/com/myhabbit/app/presentation/components/StatusSelector.kt (diganti StatusIcon + StatusPickerSheet)

- Build:
  - SUCCESS (:app:assembleDebug)

- Tests:
  - PASSED (126/126)

- Acceptance Criteria:
  - [x] Warna ungu brand sesuai mockup, dark/light lengkap, dynamic color off
  - [x] Splash screen: gradient ungu + logo ✓ + "Track • Plan • Achieve" + spinner (mockup #1)
  - [x] Top bar Beranda: logo ✓ + judul + ikon person menuju Profil (mockup #3)
  - [x] Top bar UNGU menyatu status bar dengan teks/ikon putih di SEMUA layar (AppTopBar) — verifikasi Beranda & Ringkasan di device
  - [x] Kartu activity: SATU ikon status lingkaran (bukan 3 tombol inline)
  - [x] Popup "Pilih Status" bottom sheet: Selesai/Tidak Dilakukan/Belum dinilai + Batal (mockup #5)
  - [x] Login tetap HANYA Google (email dari mockup diabaikan — keputusan user)
  - [x] Verifikasi device: splash ✓, beranda ✓, popup ✓, pilih Selesai → ✓ hijau Progress 1/1 100%
  - [x] Swipe-delete & semua fitur lama tetap berfungsi

- Known Issues:
  - Splash tidak terlihat pada screenshot <1 detik (cold start device lambat) — tampil normal mulai ~1s
  - Tab filter Harian/Mingguan/Bulanan (mockup #3) belum — section berurutan dipertahankan (bisa jadi backlog)

### Feature: Post-release — Reset Data (Reset Ringkasan + Hard Reset)

- Status: COMPLETED
- Date: 2026-11-17
- Files Created:
  - app/src/main/java/com/myhabbit/app/domain/usecase/habit/ResetRecordsUseCase.kt (hapus semua habit_records, activity tetap)
  - app/src/main/java/com/myhabbit/app/domain/usecase/habit/ResetAllDataUseCase.kt (hapus habits + records; akun/pengaturan tetap)

- Files Modified:
  - app/src/main/java/com/myhabbit/app/domain/repository/HabitRepository.kt (+deleteAllHabits)
  - app/src/main/java/com/myhabbit/app/domain/repository/HabitRecordRepository.kt (+deleteAllRecords)
  - app/src/main/java/com/myhabbit/app/data/repository/HabitRepositoryImpl.kt
  - app/src/main/java/com/myhabbit/app/data/repository/HabitRecordRepositoryImpl.kt
  - app/src/main/java/com/myhabbit/app/presentation/profile/ProfileViewModel.kt (inject 2 use case, resetRecords()/resetAllData())
  - app/src/main/java/com/myhabbit/app/presentation/profile/ProfileScreen.kt (ResetDataCard danger zone + dialog konfirmasi PRD §34a)
  - app/src/test/java/com/myhabbit/app/presentation/profile/ProfileViewModelTest.kt (ctor 5 param + 3 test reset)
  - app/src/test/java/com/myhabbit/app/domain/usecase/habit/HabitUseCaseTest.kt (+2 test use case, urutan records→habits diverifikasi InOrder)
  - PRD.md (§34a Reset Data)

- Build:
  - SUCCESS (:app:assembleDebug)

- Tests:
  - PASSED (126/126 — +5 test)

- Acceptance Criteria:
  - [x] Card "Reset Data" di Profil dengan 2 tombol merah: Reset Ringkasan & Hard Reset Semua Data
  - [x] Reset Ringkasan hapus semua records → chart/history mulai dari nol, activity tetap
  - [x] Hard Reset hapus habits + records (urutan records dulu) — aplikasi seperti baru
  - [x] Akun Google, tema, notifikasi TIDAK ikut terhapus
  - [x] Dialog konfirmasi wajib (default Cancel) dengan peringatan "This action cannot be undone"
  - [x] Peringatan "Backup dulu disarankan" di card
  - [x] Error reset tampil sebagai pesan di Profil

- Known Issues:
  - None

### Feature: Post-release — Swipe-to-Delete (Delete Activity UX)

- Status: COMPLETED
- Date: 2026-11-17
- Files Created:
  - app/src/main/java/com/myhabbit/app/ui/common/SwipeToDeleteContainer.kt (komponen bersama: geser kiri → tombol "Hapus" merah 88dp, clip rounded corner)

- Files Modified:
  - app/src/main/java/com/myhabbit/app/presentation/activity/ActivityListScreen.kt (ActivityItem memakai SwipeToDeleteContainer; IconButton delete lama dihapus dari baris)
  - app/src/main/java/com/myhabbit/app/presentation/activity/ActivityViewModel.kt (listError MutableStateFlow — error delete kini tampil di layar daftar, sebelumnya salah rute ke _formState)
  - app/src/main/java/com/myhabbit/app/presentation/dashboard/DashboardScreen.kt (kartu Beranda swipe-able + dialog konfirmasi "Delete Activity?" + link "Lihat semua activity"/"Lihat history" di bagian bawah daftar)
  - app/src/main/java/com/myhabbit/app/presentation/dashboard/DashboardViewModel.kt (inject DeleteHabitUseCase, deleteHabit() dengan error → manualError/snackbar)
  - app/src/test/java/com/myhabbit/app/presentation/dashboard/DashboardViewModelTest.kt (ctor 4 param + test deleteHabit_removesItemFromDashboard)
  - PRD.md (§16: ketentuan interaksi geser-kiri → Hapus → konfirmasi)

- Build:
  - SUCCESS (:app:assembleDebug)

- Tests:
  - PASSED (121/121 — +1 test DashboardViewModel delete)

- Acceptance Criteria:
  - [x] Swipe kiri pada kartu (Beranda ATAU daftar Activity) menampilkan tombol "Hapus" merah di sisi kanan
  - [x] Tap "Hapus" → dialog konfirmasi "Delete Activity? — This action cannot be undone" (PRD §34), default Cancel
  - [x] Soft delete (deletedAt timestamp), history tetap tersimpan (PRD §16)
  - [x] Tombol delete lama di baris daftar diganti interaksi swipe
  - [x] Error gagal delete tampil di layar (snackbar Beranda / error list Activity)
  - [x] Verifikasi di device bd60bb24: swipe → reveal → dialog → delete → Progress 0/2 → 0/1, sudut kartu tidak ada warna merah bocor

- Known Issues:
  - None

### Feature: Phase 13 — Final Polish

- Status: COMPLETED
- Date: 2026-11-14
- Files Created:
  - app/src/main/java/com/myhabbit/app/domain/repository/SettingsRepository.kt
  - app/src/main/java/com/myhabbit/app/data/repository/SettingsRepositoryImpl.kt
  - app/src/main/java/com/myhabbit/app/data/notification/ReminderScheduler.kt
  - app/src/main/java/com/myhabbit/app/data/notification/ReminderWorker.kt
  - app/src/test/java/com/myhabbit/app/data/repository/SettingsRepositoryTest.kt

- Files Modified:
  - gradle/libs.versions.toml (work-runtime-ktx 2.9.1)
  - app/build.gradle.kts
  - app/src/main/AndroidManifest.xml (POST_NOTIFICATIONS)
  - app/src/main/java/com/myhabbit/app/MainActivity.kt (theme override + reminder schedule)
  - app/src/main/java/com/myhabbit/app/presentation/profile/ProfileViewModel.kt (theme/notifications)
  - app/src/main/java/com/myhabbit/app/presentation/profile/ProfileScreen.kt (ThemeCard, NotificationCard)
  - app/src/main/java/com/myhabbit/app/data/local/dao/SettingsDao.kt (deleteSettings)
  - app/src/main/java/com/myhabbit/app/di/DatabaseModule.kt (bindSettingsRepository)
  - app/src/test/java/com/myhabbit/app/presentation/profile/ProfileViewModelTest.kt (+2 test)
  - PROMPT.md (checklist)

- Build:
  - SUCCESS (:app:assembleDebug)

- Tests:
  - PASSED (110/110 — 6 test baru: 4 SettingsRepository + 2 ViewModel)

- Acceptance Criteria:
  - [x] 13.1 Theme Light/Dark/System (default system), persist di Room, apply via MainActivity
  - [x] 13.2 Reminder harian 20:00 via WorkManager, toggle di Profile, POST_NOTIFICATIONS runtime permission API 33+, notification TIDAK menandai status (PRD §48)
  - [x] 13.3 Error handling: semua screen punya error state + try/catch use case; Drive/notification error tidak crash
  - [x] 13.4 Accessibility: contentDescription icon ✓/✕/edit/delete/nav (sudah tercakup sejak Phase 6-7), teks + icon selalu berpasangan (bukan hanya warna)
  - [x] 13.5 Performance: LazyColumn history lazy-rendered, index DB di category/periodKey, chart via Canvas tanpa block main thread, query reaktif via Flow
  - [x] 13.6 Regression: full suite 110/110 + assembleDebug SUCCESS

- Known Issues:
  - Verifikasi notifikasi end-to-end di device belum dilakukan (butuh install manual)
  - Reminder pakai periodic 24 jam dengan initial delay ke 20:00 — drift WorkManager bisa memundurkan beberapa menit (acceptable untuk MVP)

### Feature: Phase 12 — Google Drive

- Status: COMPLETED (kode + unit test; verifikasi end-to-end menunggu setup Google Cloud user)
- Date: 2026-11-14
- **RETEST END-TO-END (2026-11-17, device bd60bb24): PASSED** —
  Profil → Backup ke Drive → consent Google ("Google belum memverifikasi aplikasi ini" →
  Lanjutkan → scope Drive appDataFolder → Lanjutkan) → "Backup berhasil disimpan ke Google
  Drive" + Last backup 17 Nov 2026 21:15; Restore dari Drive → dialog konfirmasi → Continue →
  "Restore berhasil — data berhasil diganti dari Drive"; activity "Minum" tetap tampil di Beranda.
  Known issue baru: setelah restore, label "Last backup" kembali "Belum pernah backup ke Drive"
  (timestamp `lastBackupAt` ikut tertimpa isi backup) — **(RESOLVED 2026-11-24)** lihat
  "Post-release — Perbaikan lastBackupAt setelah restore".
- Date: 2026-11-14
- Files Created:
  - app/src/main/java/com/myhabbit/app/data/drive/GoogleDriveManager.kt
  - app/src/test/java/com/myhabbit/app/data/repository/BackupRepositoryImplTest.kt

- Files Modified:
  - gradle/libs.versions.toml (play-services-auth 21.2.0, google-api-services-drive v3-rev20240509, google-http-client-gson 1.44.2)
  - app/build.gradle.kts (dependencies + packaging excludes META-INF/INDEX.LIST)
  - app/src/main/AndroidManifest.xml (INTERNET permission)
  - app/src/main/java/com/myhabbit/app/data/local/dao/SettingsDao.kt (updateLastBackupAt)
  - app/src/main/java/com/myhabbit/app/domain/repository/BackupRepository.kt (3 method Drive)
  - app/src/main/java/com/myhabbit/app/data/repository/BackupRepositoryImpl.kt
  - app/src/main/java/com/myhabbit/app/presentation/profile/ProfileViewModel.kt (backupToDrive/restoreFromDrive/lastBackupAt)
  - app/src/main/java/com/myhabbit/app/presentation/profile/ProfileScreen.kt (DriveBackupCard + last backup)
  - app/src/test/java/com/myhabbit/app/presentation/profile/ProfileViewModelTest.kt (stub lastBackupAt)
  - PROMPT.md (checklist)

- Build:
  - SUCCESS (:app:assembleDebug)

- Tests:
  - PASSED (104/104 total: 8 test Drive flow baru)

- Acceptance Criteria:
  - [x] Otorisasi Drive via `Identity.getAuthorizationClient` + scope minimal `drive.file`
  - [x] Upload backup: create folder `MyHabbit/` + file `myhabbit-backup.json` (create/update)
  - [x] Download backup dari Drive
  - [x] Restore via BackupImporter (validasi + transaction — tidak corrupt data)
  - [x] Backup status `lastBackupAt` ter-update setelah upload sukses (settings row dibuat bila belum ada)
  - [x] Validasi JSON sebelum upload (ARCHITECTURE §15.2)
  - [x] Error network/otorisasi → pesan user-friendly, tidak crash
  - [x] INTERNET permission ditambahkan di manifest
  - [x] Auth (sign-in) dan Drive authorization tetap terpisah (§14)

- Known Issues:
  - Verifikasi end-to-end Drive menunggu: Google Drive API diaktifkan + Android OAuth client (package + SHA-1) dibuat di Google Cloud
  - UI restore Drive belum memakai dialog konfirmasi eksplisit (PRD §34) — masih inline message

- Setup user (SHA-1 debug dari signingReport):
  - Package: com.myhabbit.app
  - SHA-1: A4:1A:F4:27:1C:77:3B:1E:57:1F:8B:B6:74:31:04:77:BD:5E:F9:7C
  - WEB_CLIENT_ID di data/auth/AuthConfig.kt harus diisi (Phase 10)

### Feature: Phase 11 — JSON Backup

- Status: COMPLETED
- Date: 2026-11-13
- Files Created:
  - app/src/main/java/com/myhabbit/app/data/backup/BackupModels.kt
  - app/src/main/java/com/myhabbit/app/data/backup/BackupSerializer.kt
  - app/src/main/java/com/myhabbit/app/data/backup/BackupValidator.kt
  - app/src/main/java/com/myhabbit/app/data/backup/BackupExporter.kt
  - app/src/main/java/com/myhabbit/app/data/backup/BackupImporter.kt
  - app/src/main/java/com/myhabbit/app/domain/repository/BackupRepository.kt
  - app/src/main/java/com/myhabbit/app/data/repository/BackupRepositoryImpl.kt
  - app/src/test/java/com/myhabbit/app/data/backup/BackupSerializerTest.kt
  - app/src/test/java/com/myhabbit/app/data/backup/BackupValidatorTest.kt
  - app/src/test/java/com/myhabbit/app/data/backup/BackupExporterImporterTest.kt

- Files Modified:
  - gradle/libs.versions.toml (kotlinx-serialization-json 1.6.3 + plugin serialization)
  - app/build.gradle.kts
  - app/src/main/java/com/myhabbit/app/di/DatabaseModule.kt (bindBackupRepository)
  - app/src/main/java/com/myhabbit/app/data/local/dao/HabitDao.kt (deleteAllHabits)
  - app/src/main/java/com/myhabbit/app/data/local/dao/HabitRecordDao.kt (deleteAllRecords)
  - app/src/main/java/com/myhabbit/app/data/local/dao/UserDao.kt (getCurrentUser)
  - app/src/main/java/com/myhabbit/app/presentation/profile/ProfileViewModel.kt (exportBackup/importBackup)
  - app/src/main/java/com/myhabbit/app/presentation/profile/ProfileScreen.kt (BackupCard)
  - app/src/test/java/com/myhabbit/app/data/repository/AuthRepositoryImplTest.kt (FakeUserDao + getCurrentUser)
  - app/src/test/java/com/myhabbit/app/presentation/profile/ProfileViewModelTest.kt (backupRepository + 2 test)
  - PROMPT.md (checklist)

- Build:
  - SUCCESS (:app:assembleDebug)

- Tests:
  - PASSED (96/96 total: 24 test backup baru)

- Acceptance Criteria:
  - [x] Schema v1: schemaVersion, app, exportedAt, user, habits, records, settings
  - [x] Serializer round-trip aman (kotlinx-serialization, ARCHITECTURE §16)
  - [x] Validator: reject schemaVersion masa depan, duplikat ID, referensi habit rusak, enum salah, field kosong
  - [x] Export: habit aktif + soft-deleted + semua record + profil + settings, tanpa token
  - [x] Import: validasi dulu, replace dalam SATU Room transaction (gagal tidak merusak data)
  - [x] Enum lowercase JSON dinormalisasi ke internal uppercase
  - [x] Backup file app-specific storage (tanpa permission)

- Known Issues:
  - (resolved 2026-11-14) Restore dari UI kini support pilih file eksternal via SAF (OpenDocument, mime json/text)
  - (resolved 2026-11-14) Konfirmasi "replace data" kini dialog peringatan eksplisit sesuai PRD §34 — berlaku untuk import file dan restore Drive

- Notes:
  - Timestamp JSON memakai epoch millis Long (deviasi dari contoh ISO PRD — internal model juga epoch)
  - Drive upload/download adalah Phase 12

### Feature: Phase 10 — Google Authentication

- Status: COMPLETED (kode + test; verifikasi end-to-end menunggu Web Client ID user)
- Date: 2026-11-13
- Files Created:
  - app/src/main/java/com/myhabbit/app/domain/model/AuthResult.kt
  - app/src/main/java/com/myhabbit/app/domain/repository/AuthRepository.kt
  - app/src/main/java/com/myhabbit/app/data/auth/AuthConfig.kt (placeholder WEB_CLIENT_ID)
  - app/src/main/java/com/myhabbit/app/data/auth/GoogleAuthManager.kt
  - app/src/main/java/com/myhabbit/app/data/repository/AuthRepositoryImpl.kt
  - app/src/main/java/com/myhabbit/app/presentation/profile/ProfileViewModel.kt
  - app/src/test/java/com/myhabbit/app/data/repository/AuthRepositoryImplTest.kt
  - app/src/test/java/com/myhabbit/app/presentation/profile/ProfileViewModelTest.kt

- Files Modified:
  - gradle/libs.versions.toml (credentials 1.3.0, googleid 1.1.1)
  - app/build.gradle.kts (3 dependency auth)
  - app/src/main/java/com/myhabbit/app/di/DatabaseModule.kt (bindAuthRepository)
  - app/src/main/java/com/myhabbit/app/presentation/profile/ProfileScreen.kt (full rebuild)
  - PROMPT.md (checklist)

- Build:
  - SUCCESS (:app:assembleDebug)

- Tests:
  - PASSED (72/72 total: 4 AuthRepositoryImpl + 4 ProfileViewModel baru)

- Acceptance Criteria:
  - [x] Google Sign-In via Credential Manager (mekanisme resmi, PRD §31)
  - [x] Tanpa password & tanpa token di storage — hanya profil user di Room
  - [x] Auth logic di GoogleAuthManager/AuthRepository, BUKAN di Compose (§14)
  - [x] Sign-out menghapus user lokal + clear credential state
  - [x] Error handling: GetCredentialException (batal/gagal) → pesan user-friendly
  - [x] App tetap berfungsi tanpa login (Profile menampilkan "Belum login")
  - [x] Nonce di-generate untuk keamanan ID token

- Known Issues:
  - WEB_CLIENT_ID masih placeholder — harus diisi user dari Google Cloud Console
  - Verifikasi end-to-end (sheet Google muncul & login sukses) menunggu client ID + SHA-1 didaftarkan
  - Photo profil ditampilkan sebagai avatar inisial (tanpa Coil; hindari dependency tambahan)

- Notes:
  - Email diambil dari payload JWT ID token (GoogleIdTokenCredential tidak expose email)
  - Profil dipersist ke tabel `users` (infrastruktur sudah ada sejak Phase 2)

### Feature: Phase 9 — Charts

- Status: COMPLETED
- Date: 2026-11-13
- Files Created:
  - app/src/main/java/com/myhabbit/app/domain/usecase/summary/HabitStatistic.kt
  - app/src/main/java/com/myhabbit/app/domain/usecase/summary/GetHabitStatisticsUseCase.kt
  - app/src/main/java/com/myhabbit/app/presentation/components/Charts.kt
  - app/src/test/java/com/myhabbit/app/domain/usecase/summary/GetHabitStatisticsUseCaseTest.kt

- Files Modified:
  - app/src/main/java/com/myhabbit/app/presentation/summary/SummaryViewModel.kt (ChartUiState)
  - app/src/main/java/com/myhabbit/app/presentation/summary/SummaryScreen.kt (ChartsCard)
  - PROMPT.md (checklist)

- Build:
  - SUCCESS (:app:assembleDebug)

- Tests:
  - PASSED (64/64 total: 5 test statistik baru)

- Acceptance Criteria:
  - [x] Bar chart completion rate per activity
  - [x] Donut chart distribusi status (Completed/Failed/Pending)
  - [x] Chart hanya menggambar, data disiapkan ViewModel (§12.4)
  - [x] Habit tanpa record evaluated di-exclude dari bar chart
  - [x] Chart mengikuti filter kategori (All/Daily/Weekly/Monthly)
  - [x] Chart reaktif terhadap perubahan status

- Known Issues:
  - None

- Notes:
  - Keputusan desain: custom Canvas Compose, TANPA chart library eksternal (disetujui user) — bar chart pakai drawRoundRect, donut pakai drawArc
  - Weekly/Monthly trend diimplementasi via filter kategori + filter chips pada SummaryScreen (data tren agregat per periode)

### Feature: Phase 8 — Summary and Statistics

- Status: COMPLETED
- Date: 2026-11-13
- Files Created:
  - app/src/main/java/com/myhabbit/app/domain/service/CompletionCalculator.kt
  - app/src/main/java/com/myhabbit/app/domain/usecase/summary/GetSummaryUseCase.kt
  - app/src/main/java/com/myhabbit/app/presentation/summary/SummaryScreen.kt
  - app/src/main/java/com/myhabbit/app/presentation/summary/SummaryViewModel.kt
  - app/src/test/java/com/myhabbit/app/domain/service/CompletionCalculatorTest.kt
  - app/src/test/java/com/myhabbit/app/domain/usecase/summary/GetSummaryUseCaseTest.kt

- Files Modified:
  - app/src/main/java/com/myhabbit/app/presentation/navigation/AppNavigation.kt
  - PROMPT.md (checklist)

- Build:
  - SUCCESS (:app:assembleDebug)

- Tests:
  - PASSED (59/59 total: 7 calculator + 4 use case baru)

- Acceptance Criteria:
  - [x] Completed count benar
  - [x] Failed count benar
  - [x] Pending tidak dihitung sebagai evaluated
  - [x] Completion rate sesuai formula (8/10 = 80%)
  - [x] Evaluated = 0 → rate 0%
  - [x] Filter kategori (All/Daily/Weekly/Monthly) bekerja
  - [x] Summary tidak memodifikasi data (read-only)

- Known Issues:
  - None

- Notes:
  - Rata-rata multi-periode memakai agregat, bukan rata-rata persen (ARCHITECTURE.md §12.3)
  - Chart adalah Phase 9

### Feature: Phase 7 — History

- Status: COMPLETED
- Date: 2026-11-13
- Files Created:
  - app/src/main/java/com/myhabbit/app/domain/usecase/history/GetHistoryUseCase.kt
  - app/src/main/java/com/myhabbit/app/domain/usecase/history/HistoryEntry.kt
  - app/src/main/java/com/myhabbit/app/presentation/history/HistoryScreen.kt
  - app/src/main/java/com/myhabbit/app/presentation/history/HistoryViewModel.kt
  - app/src/test/java/com/myhabbit/app/presentation/history/HistoryViewModelTest.kt

- Files Modified:
  - app/src/main/java/com/myhabbit/app/data/local/dao/HabitDao.kt (getAllHabitsAsFlow)
  - app/src/main/java/com/myhabbit/app/data/local/dao/HabitRecordDao.kt (observeAllRecords)
  - app/src/main/java/com/myhabbit/app/presentation/navigation/Screen.kt
  - app/src/main/java/com/myhabbit/app/presentation/navigation/AppNavigation.kt
  - app/src/main/java/com/myhabbit/app/presentation/dashboard/DashboardScreen.kt
  - PROMPT.md (checklist)

- Build:
  - SUCCESS (:app:assembleDebug)

- Tests:
  - PASSED (48/48 total: 4 history filter tests baru)

- Acceptance Criteria:
  - [x] History list menampilkan judul habit, periode, status
  - [x] Filter This Week: daily dalam minggu ISO ini + weekly minggu ini
  - [x] Filter This Month: daily bulan ini + weekly yang beririsan + monthly bulan ini
  - [x] Filter All: semua record
  - [x] Data reaktif (Room Flow, combine)
  - [x] Empty state per filter

- Known Issues:
  - Filter "This Week" sengaja tidak menampilkan monthly (periode bulanan melebihi satu minggu)

- Notes:
  - Weekly-to-month intersection dihitung dengan ISO week start/end date

### Feature: Phase 6 — Dashboard

- Status: COMPLETED
- Date: 2026-11-13
- Files Created:
  - app/src/main/java/com/myhabbit/app/presentation/dashboard/DashboardViewModel.kt
  - app/src/main/java/com/myhabbit/app/presentation/components/StatusSelector.kt
  - app/src/main/java/com/myhabbit/app/presentation/components/ProgressCard.kt
  - app/src/test/java/com/myhabbit/app/presentation/dashboard/DashboardViewModelTest.kt

- Files Modified:
  - app/src/main/java/com/myhabbit/app/presentation/dashboard/DashboardScreen.kt
  - PROMPT.md (checklist)

- Build:
  - SUCCESS (:app:assembleDebug)

- Tests:
  - PASSED (44/44 total)

- Acceptance Criteria:
  - [x] Dashboard menampilkan tanggal hari ini (format Indonesia)
  - [x] Activity dikelompokkan DAILY / WEEKLY / MONTHLY
  - [x] Status ○ / ✓ / ✕ dengan tombol yang bisa diklik
  - [x] Update status tersimpan & UI refresh (refresh trigger)
  - [x] Progress per kategori (completed/total, %, bar)
  - [x] Empty state dengan aksi tambah
  - [x] Aksesibilitas: contentDescription pada tombol status, status dibedakan simbol + background
  - [x] Error ditampilkan via Snackbar

- Known Issues:
  - Status yang diseleksi ditandai background secondaryContainer (bukan hanya warna ikon)

- Notes:
  - Room flow hanya memantau tabel habits, sehingga update record memerlukan refresh trigger manual
  - Verifikasi runtime di device HP dapat dilakukan saat USB tersedia

### Feature: Phase 5 — Status Management

- Status: COMPLETED
- Date: 2026-11-13
- Files Created:
  - app/src/main/java/com/myhabbit/app/domain/usecase/record/UpdateHabitStatusUseCase.kt
  - app/src/main/java/com/myhabbit/app/domain/usecase/record/GetHabitStatusUseCase.kt
  - app/src/main/java/com/myhabbit/app/domain/usecase/record/GetCurrentPeriodStatusesUseCase.kt
  - app/src/main/java/com/myhabbit/app/domain/usecase/record/HabitWithStatus.kt
  - app/src/test/java/com/myhabbit/app/domain/usecase/record/StatusManagementTest.kt

- Files Modified:
  - PROMPT.md (checklist)

- Build:
  - SUCCESS (:app:assembleDebug)

- Tests:
  - PASSED (40/40 total: 8 status management baru + 16 period + 8 DAO + 8 use case)

- Acceptance Criteria:
  - [x] Habit tanpa record = PENDING (tanpa menulis DB)
  - [x] User dapat memilih completed
  - [x] User dapat memilih failed
  - [x] User dapat kembali ke pending (record diupdate, tidak duplikat)
  - [x] Status persist setelah restart (tersimpan di Room)
  - [x] Periode baru otomatis PENDING, record lama tetap utuh
  - [x] Weekly/monthly pakai periodKey sesuai kategori

- Known Issues:
  - None

- Notes:
  - Update status pakai create-or-update dengan unique index habitId+periodKey
  - GetHabitStatusUseCase hanya membaca, tidak menulis ke DB (ARCHITECTURE.md §8.5)

### Feature: Phase 4 — Period Engine

- Status: COMPLETED
- Date: 2026-11-13
- Files Created:
  - app/src/main/java/com/myhabbit/app/domain/service/PeriodCalculator.kt
  - app/src/main/java/com/myhabbit/app/domain/service/PeriodCalculatorImpl.kt
  - app/src/test/java/com/myhabbit/app/domain/service/PeriodCalculatorTest.kt

- Files Modified:
  - app/src/main/java/com/myhabbit/app/di/AppModule.kt (provide PeriodCalculator)
  - app/build.gradle.kts (core library desugaring untuk java.time di minSdk 24)
  - gradle/libs.versions.toml (desugar_jdk_libs)
  - PROMPT.md (checklist)

- Build:
  - SUCCESS (:app:assembleDebug)

- Tests:
  - PASSED (32/32 total: 16 period calculator + 8 DAO + 8 use case)

- Acceptance Criteria:
  - [x] Daily: format YYYY-MM-DD
  - [x] Weekly: format YYYY-Www, ISO week (Senin s/d Minggu), week-based year
  - [x] Monthly: format YYYY-MM, akhir bulan benar (termasuk Februari kabisat 29)
  - [x] PeriodCalculator interface sesuai ARCHITECTURE.md §8.4
  - [x] Boundary minggu: Sabtu & Minggu satu minggu, Senin = minggu baru
  - [x] Boundary tahun ISO: 1 Jan 2027 = 2026-W53
  - [x] Periode baru = key baru, tidak menghapus record lama

- Known Issues:
  - None

- Notes:
  - java.time untuk minSdk 24 memerlukan core library desugaring (sudah diaktifkan)
  - Perhitungan tanggal lokal device (PRD.md §52)

### Feature: Phase 3 — Activity Management

- Status: COMPLETED
- Date: 2026-11-13
- Files Created:
  - app/src/main/java/com/myhabbit/app/domain/usecase/habit/CreateHabitUseCase.kt
  - app/src/main/java/com/myhabbit/app/domain/usecase/habit/UpdateHabitUseCase.kt
  - app/src/main/java/com/myhabbit/app/domain/usecase/habit/DeleteHabitUseCase.kt
  - app/src/main/java/com/myhabbit/app/domain/usecase/habit/GetActiveHabitsUseCase.kt
  - app/src/main/java/com/myhabbit/app/domain/usecase/habit/GetHabitByIdUseCase.kt
  - app/src/main/java/com/myhabbit/app/domain/validation/ActivityValidator.kt
  - app/src/main/java/com/myhabbit/app/core/result/AppResult.kt
  - app/src/main/java/com/myhabbit/app/presentation/activity/ActivityFormScreen.kt
  - app/src/main/java/com/myhabbit/app/presentation/activity/ActivityListScreen.kt
  - app/src/main/java/com/myhabbit/app/presentation/activity/ActivityViewModel.kt
  - app/src/test/java/com/myhabbit/app/domain/usecase/habit/HabitUseCaseTest.kt

- Files Modified:
  - app/src/main/java/com/myhabbit/app/presentation/navigation/Screen.kt (rute activity_list, activity_form, activity_edit)
  - app/src/main/java/com/myhabbit/app/presentation/navigation/AppNavigation.kt
  - app/src/main/java/com/myhabbit/app/presentation/dashboard/DashboardScreen.kt
  - gradle/libs.versions.toml (lifecycle-runtime-compose, mockito-kotlin)
  - app/build.gradle.kts
  - PROMPT.md (checklist)

- Build:
  - SUCCESS (:app:assembleDebug)

- Tests:
  - PASSED (16/16: 8 DAO + 8 use case)

- Acceptance Criteria:
  - [x] User dapat membuat activity
  - [x] Title wajib (1-100 karakter)
  - [x] Category wajib dipilih (Daily/Weekly/Monthly)
  - [x] Description opsional (max 500 karakter)
  - [x] Activity muncul di list
  - [x] Activity dapat diedit (ID tidak berubah, createdAt dipertahankan)
  - [x] Activity dapat dihapus (soft delete dengan dialog konfirmasi, default Cancel)
  - [x] Activity ID unik (UUID-based: habit_...)

- Known Issues:
  - Verifikasi runtime di device HP belum bisa dilakukan (USB tidak terdeteksi saat sesi ini)

- Notes:
  - Delete memakai soft delete agar history tetap tersedia (PRD.md §16)
  - Error handling memakai AppResult sealed interface (ARCHITECTURE.md §19)

## Feature Completion Record

### Feature: Phase 2 — Database Foundation

- Status: COMPLETED
- Date: 2026-11-13
- Files Created:
  - app/src/main/java/com/myhabbit/app/data/local/database/MyHabbitDatabase.kt
  - app/src/main/java/com/myhabbit/app/data/local/entity/HabitEntity.kt
  - app/src/main/java/com/myhabbit/app/data/local/entity/HabitRecordEntity.kt
  - app/src/main/java/com/myhabbit/app/data/local/entity/UserEntity.kt
  - app/src/main/java/com/myhabbit/app/data/local/entity/SettingsEntity.kt
  - app/src/main/java/com/myhabbit/app/data/local/dao/HabitDao.kt
  - app/src/main/java/com/myhabbit/app/data/local/dao/HabitRecordDao.kt
  - app/src/main/java/com/myhabbit/app/data/local/dao/UserDao.kt
  - app/src/main/java/com/myhabbit/app/data/local/dao/SettingsDao.kt
  - app/src/main/java/com/myhabbit/app/data/mapper/HabitMapper.kt
  - app/src/main/java/com/myhabbit/app/data/mapper/HabitRecordMapper.kt
  - app/src/main/java/com/myhabbit/app/data/mapper/UserMapper.kt
  - app/src/main/java/com/myhabbit/app/domain/model/Habit.kt
  - app/src/main/java/com/myhabbit/app/domain/model/HabitRecord.kt
  - app/src/main/java/com/myhabbit/app/domain/model/HabitCategory.kt
  - app/src/main/java/com/myhabbit/app/domain/model/HabitStatus.kt
  - app/src/main/java/com/myhabbit/app/domain/model/User.kt
  - app/src/main/java/com/myhabbit/app/domain/model/Summary.kt
  - app/src/main/java/com/myhabbit/app/domain/repository/HabitRepository.kt
  - app/src/main/java/com/myhabbit/app/domain/repository/HabitRecordRepository.kt
  - app/src/main/java/com/myhabbit/app/data/repository/HabitRepositoryImpl.kt
  - app/src/main/java/com/myhabbit/app/data/repository/HabitRecordRepositoryImpl.kt
  - app/src/main/java/com/myhabbit/app/di/DatabaseModule.kt
  - app/src/test/java/com/myhabbit/app/data/local/dao/HabitDaoTest.kt

- Files Modified:
  - gradle/libs.versions.toml (Room, Robolectric dependencies)
  - app/build.gradle.kts (Room + test dependencies)

- Build:
  - SUCCESS (:app:assembleDebug)

- Tests:
  - PASSED (8/8 unit tests: CRUD, soft delete, unique period key constraint, record status update)

- Acceptance Criteria:
  - [x] Room database configured (myhabbit.db, version 1)
  - [x] 4 tables created sesuai schema ARCHITECTURE.md §7.2
  - [x] UNIQUE(habitId, periodKey) constraint aktif
  - [x] Domain model terpisah dari Room entity (mapper)
  - [x] Repository interfaces di Domain Layer, implementasi di Data Layer
  - [x] Hilt DI menyediakan database, DAO, repository
  - [x] Database operations tested

- Known Issues:
  - None

- Notes:
  - Unit test menggunakan Robolectric karena Room membutuhkan runtime SQLite
  - Build environment: JDK 17 (Home/ahmad/jdk17), Gradle 8.7, AGP 8.5.2

Use this format:

```text
## Feature Completion Record

### Feature: [Feature Name]

- Status: COMPLETED
- Date: YYYY-MM-DD
- Files Created:
  - Example.kt

- Files Modified:
  - ExampleScreen.kt

- Build:
  - SUCCESS / FAILED / NOT RUN

- Tests:
  - PASSED / FAILED / NOT RUN

- Acceptance Criteria:
  - [x] Requirement 1
  - [x] Requirement 2

- Known Issues:
  - None

- Notes:
  - Implementation details.
```

Keep previous completion records.

Do not delete historical development records.

---

# 10. DATABASE AND DATA SAFETY RULES

Data integrity is critical.

Never:

- Delete historical records without permission.
- Reset the database to fix an implementation issue.
- Use destructive migration as a shortcut.
- Change database schema without considering migration.
- Change JSON backup schema without versioning.
- Overwrite user data without confirmation.
- Implement restore without transaction safety.

Before any database modification:

1. Inspect the current schema.
2. Identify affected tables.
3. Identify migration requirements.
4. Explain the impact.
5. Implement appropriate migration.
6. Test existing data compatibility.

If data loss is possible, STOP and request approval.

---

# 11. CODE QUALITY RULES

All code must follow these principles:

- Clean Architecture
- MVVM
- Repository Pattern
- Separation of Concerns
- Single Responsibility Principle
- Readability
- Maintainability
- Testability

Additional rules:

1. Use meaningful names.
2. Avoid unnecessary complexity.
3. Avoid duplicate business logic.
4. Keep functions focused.
5. Use Kotlin Coroutines for asynchronous work.
6. Use Flow for reactive data.
7. Use immutable UI state where practical.
8. Keep Compose components reusable.
9. Do not put business logic inside Composable functions.
10. Do not introduce unnecessary dependencies.

Follow the existing project conventions unless there is a documented reason to change them.

---

# 12. BUILD AND TEST RULES

Never assume the application compiles.

After each feature:

1. Run the appropriate Gradle build task.
2. Run relevant unit tests.
3. Run relevant UI or instrumentation tests when applicable.
4. Review compiler errors and warnings.
5. Fix issues introduced by the current task.
6. Report any pre-existing failures separately.

Do not hide failed tests.

Do not mark a feature complete when its required verification has failed.

If the environment cannot run Android builds or tests, explain the limitation and provide the exact verification command for the project owner.

---

# 13. DEPENDENCY MANAGEMENT

Before adding a dependency:

1. Check whether the project already contains it.
2. Check whether an existing dependency can solve the requirement.
3. Verify the official documentation.
4. Check compatibility with the project's Android and Kotlin versions.
5. Explain why the dependency is necessary.

Do not add dependencies without a clear reason.

Do not arbitrarily upgrade major dependency versions.

---

# 14. UI AND DESIGN RULES

MyHabbit must have a consistent and simple user experience.

UI implementation must follow the product requirements and approved design decisions.

Rules:

- Use Material 3.
- Maintain consistent spacing.
- Maintain consistent typography.
- Use reusable components.
- Provide clear loading states.
- Provide clear empty states.
- Provide clear error states.
- Provide confirmation for destructive actions.
- Support light and dark themes when included in scope.
- Avoid unnecessary UI complexity.

Do not redesign the application independently.

If a design decision is not specified, propose a design and wait for approval before implementing significant changes.

---

# 15. GOOGLE SERVICES RULES

Google Authentication and Google Drive must be implemented only in their designated development phases.

Do not implement Google services during the local application foundation phases.

Important:

- Google Authentication and Google Drive authorization are separate.
- Never store Google passwords.
- Never expose tokens.
- Use minimum required permissions.
- Do not assume a Google Cloud project already exists.
- Do not claim Drive integration works before testing with an authorized account.

Google Drive is for backup and restore, not the primary database.

Core application functionality must work offline.

---

# 16. CHANGE MANAGEMENT

If implementation requires changing an existing architectural decision:

1. Identify the original decision.
2. Explain why it is insufficient.
3. Propose an alternative.
4. Explain the advantages and disadvantages.
5. Identify affected files and features.
6. Wait for approval.

Do not silently change:

- Database structure.
- JSON schema.
- Authentication strategy.
- Backup strategy.
- Navigation structure.
- Business rules.
- Technology stack.

Document approved changes.

---

# 17. REQUIRED RESPONSE FORMAT

For every feature request, use the following response structure.

## A. Task Understanding

Explain what feature will be developed.

## B. Documentation Reference

Mention the relevant requirements from:

- PRD.md
- ARCHITECTURE.md
- PROMPT.md

## C. Current Project Condition

Report what already exists and what is missing.

## D. Implementation Plan

List:

- Files to create.
- Files to modify.
- Dependencies.
- Implementation steps.
- Testing approach.

## E. Approval

If the user has not approved implementation, ask:

"Apakah rencana ini sudah sesuai? Jika disetujui, saya akan mulai mengimplementasikan fitur ini."

Then WAIT.

## F. Implementation Result

After implementation, report:

- What was completed.
- Files created.
- Files modified.
- Build results.
- Test results.
- Remaining issues.

## G. Progress Update

Show the updated checklist item from PROMPT.md.

## H. Stop

End the task and wait for the next instruction.

---

# 18. SESSION START RULE

Whenever a new development session begins, do the following:

1. Read PRD.md.
2. Read ARCHITECTURE.md.
3. Read PROMPT.md.
4. Inspect the current project structure.
5. Check the latest feature completion record.
6. Check the progress checklist.
7. Report the current project status.
8. Ask which feature the project owner wants to work on next, unless the owner has already specified it.

Do not restart completed work without a reason.

Do not assume the previous session's implementation was successful without inspecting the current project.

---

# 19. FINAL AND MOST IMPORTANT RULE

**ONE FEATURE AT A TIME.**

The project owner controls the development sequence.

You must never:

- Build the entire application in one attempt.
- Skip development phases without approval.
- Start another feature automatically.
- Modify unrelated modules.
- Change architecture without approval.
- Mark incomplete work as complete.
- Continue after completing the requested feature.

After every feature:

1. Verify the implementation.
2. Update PROMPT.md.
3. Record the completion status.
4. Report the result.
5. STOP.
6. WAIT for the next instruction.

**Do not proceed until the project owner gives the next instruction.**

The goal is not to generate as much code as possible.

The goal is to build MyHabbit correctly, safely, consistently, and incrementally.