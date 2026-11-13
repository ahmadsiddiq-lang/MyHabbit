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

- [ ] 9.1 Select and Configure Chart Library
- [ ] 9.2 Implement Bar Chart
- [ ] 9.3 Implement Donut Chart
- [ ] 9.4 Implement Weekly Trend
- [ ] 9.5 Implement Monthly Trend

### Phase 10 — Google Authentication

- [ ] 10.1 Configure Google Authentication
- [ ] 10.2 Implement Google Sign-In
- [ ] 10.3 Implement User Profile
- [ ] 10.4 Implement Sign-Out
- [ ] 10.5 Handle Authentication Errors

### Phase 11 — JSON Backup

- [ ] 11.1 Define Backup JSON Schema
- [ ] 11.2 Implement Backup Models
- [ ] 11.3 Implement JSON Serializer
- [ ] 11.4 Implement JSON Validator
- [ ] 11.5 Implement Export
- [ ] 11.6 Implement Import
- [ ] 11.7 Implement Schema Versioning
- [ ] 11.8 Test Backup and Restore

### Phase 12 — Google Drive

- [ ] 12.1 Configure Google Cloud Project
- [ ] 12.2 Configure Google Drive API
- [ ] 12.3 Implement Drive Authorization
- [ ] 12.4 Implement Backup Upload
- [ ] 12.5 Implement Backup Download
- [ ] 12.6 Implement Restore from Drive
- [ ] 12.7 Implement Backup Status
- [ ] 12.8 Test Drive Integration

### Phase 13 — Final Polish

- [ ] 13.1 Implement Theme Settings
- [ ] 13.2 Implement Notifications
- [ ] 13.3 Improve Error Handling
- [ ] 13.4 Improve Accessibility
- [ ] 13.5 Performance Testing
- [ ] 13.6 Final Regression Testing

---

# 9. FEATURE COMPLETION RECORD

After completing each feature, add a record here.

## Feature Completion Record

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