# ARCHITECTURE.md

# MyHabbit — System Architecture

**Version:** 1.0  
**Platform:** Android  
**Architecture Style:** Clean Architecture + MVVM  
**Primary Language:** Kotlin  
**UI Framework:** Jetpack Compose  
**Local Database:** Room (SQLite)  
**Backup Format:** JSON  
**Cloud Storage:** Google Drive API  
**Authentication:** Google Sign-In

---

# 1. Architecture Overview

## 1.1 Purpose

Dokumen ini menjelaskan arsitektur teknis aplikasi MyHabbit.

Dokumen ini menjadi pedoman bagi developer dan AI coding agent dalam membangun aplikasi agar:

- Struktur kode konsisten.
- Business logic terpisah dari UI.
- Database mudah dikelola.
- Data history tetap aman.
- Fitur dapat dikembangkan secara bertahap.
- Google Drive dapat diintegrasikan tanpa mengubah arsitektur utama.

## 1.2 Architecture Principles

MyHabbit menggunakan prinsip:

1. Clean Architecture
2. MVVM (Model-View-ViewModel)
3. Repository Pattern
4. Local First
5. Offline First
6. Separation of Concerns
7. Single Source of Truth
8. Dependency Injection

## 1.3 High-Level Architecture

```text
                    MyHabbit Android
                           |
                           v
                  PRESENTATION LAYER
                           |
                 Jetpack Compose UI
                           |
                       ViewModel
                           |
                           v
                    DOMAIN LAYER
                           |
                  Use Cases / Services
                           |
                 Domain Models
                           |
                           v
                      DATA LAYER
                           |
                       Repository
                           |
               +-----------+-----------+
               |                       |
               v                       v
         LOCAL DATA SOURCE       REMOTE DATA SOURCE
               |                       |
               v                       v
          Room Database          Google Drive API
               |                       |
               v                       v
          SQLite Data              JSON Backup
```

---

# 2. Technology Stack

Teknologi yang digunakan:

| Component | Technology |
|---|---|
| Programming Language | Kotlin |
| UI Framework | Jetpack Compose |
| Design System | Material 3 |
| Architecture | Clean Architecture + MVVM |
| Navigation | Navigation Compose |
| Local Database | Room |
| Database Engine | SQLite |
| Async Operations | Kotlin Coroutines |
| Reactive Data | Kotlin Flow |
| Dependency Injection | Hilt |
| Authentication | Google Sign-In |
| Cloud Storage | Google Drive API |
| Serialization | Kotlin Serialization |
| Preferences | DataStore |
| Charts | Compose-compatible chart library |
| Testing | JUnit, AndroidX Test |

AI coding agent harus memeriksa kompatibilitas versi dependency sebelum menambahkannya.

Gunakan dependency yang stabil dan saling kompatibel.

---

# 3. Application Layers

## 3.1 Presentation Layer

Tanggung jawab:

- Menampilkan UI.
- Menerima interaksi pengguna.
- Mengelola UI state.
- Menampilkan loading dan error.
- Mengirim perintah ke ViewModel.
- Mengamati perubahan data.

Teknologi:

- Jetpack Compose
- Material 3
- ViewModel
- StateFlow
- Navigation Compose

Presentation Layer tidak boleh mengakses DAO atau database secara langsung.

Alur:

```text
User Interaction
       |
       v
Compose Screen
       |
       v
ViewModel
       |
       v
Use Case
```

## 3.2 Domain Layer

Tanggung jawab:

- Menyimpan business logic.
- Menentukan aturan periode.
- Mengelola perubahan status.
- Menghitung summary.
- Menghitung completion rate.
- Menentukan record yang aktif.

Domain Layer tidak boleh bergantung pada Android UI atau Room.

Komponen:

```text
domain/
├── model/
├── repository/
├── usecase/
└── service/
```

## 3.3 Data Layer

Tanggung jawab:

- Membaca dan menyimpan data.
- Mengelola database.
- Mengubah Entity menjadi Domain Model.
- Mengelola backup JSON.
- Berkomunikasi dengan Google Drive.
- Mengelola proses import dan restore.

Komponen:

```text
data/
├── local/
├── remote/
├── repository/
├── mapper/
└── backup/
```

---

# 4. Project Structure

Struktur project yang direkomendasikan:

```text
com.myhabbit.app/
│
├── MyHabbitApp.kt
│
├── di/
│   ├── DatabaseModule.kt
│   ├── RepositoryModule.kt
│   └── AppModule.kt
│
├── core/
│   ├── common/
│   ├── constants/
│   ├── util/
│   ├── datetime/
│   ├── result/
│   └── error/
│
├── data/
│   │
│   ├── local/
│   │   ├── database/
│   │   │   └── MyHabbitDatabase.kt
│   │   │
│   │   ├── dao/
│   │   │   ├── HabitDao.kt
│   │   │   ├── HabitRecordDao.kt
│   │   │   ├── UserDao.kt
│   │   │   └── SettingsDao.kt
│   │   │
│   │   └── entity/
│   │       ├── HabitEntity.kt
│   │       ├── HabitRecordEntity.kt
│   │       ├── UserEntity.kt
│   │       └── SettingsEntity.kt
│   │
│   ├── remote/
│   │   ├── auth/
│   │   │   └── GoogleAuthManager.kt
│   │   │
│   │   └── drive/
│   │       └── GoogleDriveManager.kt
│   │
│   ├── backup/
│   │   ├── BackupSerializer.kt
│   │   ├── BackupValidator.kt
│   │   ├── BackupImporter.kt
│   │   └── BackupExporter.kt
│   │
│   ├── mapper/
│   │
│   └── repository/
│       ├── HabitRepositoryImpl.kt
│       ├── HabitRecordRepositoryImpl.kt
│       ├── SummaryRepositoryImpl.kt
│       ├── AuthRepositoryImpl.kt
│       └── BackupRepositoryImpl.kt
│
├── domain/
│   │
│   ├── model/
│   │   ├── Habit.kt
│   │   ├── HabitRecord.kt
│   │   ├── User.kt
│   │   ├── HabitCategory.kt
│   │   ├── HabitStatus.kt
│   │   └── Summary.kt
│   │
│   ├── repository/
│   │   ├── HabitRepository.kt
│   │   ├── HabitRecordRepository.kt
│   │   ├── SummaryRepository.kt
│   │   ├── AuthRepository.kt
│   │   └── BackupRepository.kt
│   │
│   ├── usecase/
│   │   ├── habit/
│   │   ├── record/
│   │   ├── summary/
│   │   ├── history/
│   │   └── backup/
│   │
│   └── service/
│       ├── PeriodCalculator.kt
│       └── CompletionCalculator.kt
│
├── presentation/
│   │
│   ├── navigation/
│   │   ├── AppNavigation.kt
│   │   └── Screen.kt
│   │
│   ├── components/
│   │   ├── HabitItem.kt
│   │   ├── StatusSelector.kt
│   │   ├── ProgressCard.kt
│   │   └── EmptyState.kt
│   │
│   ├── dashboard/
│   │   ├── DashboardScreen.kt
│   │   └── DashboardViewModel.kt
│   │
│   ├── activity/
│   │   ├── ActivityFormScreen.kt
│   │   └── ActivityViewModel.kt
│   │
│   ├── history/
│   │   ├── HistoryScreen.kt
│   │   └── HistoryViewModel.kt
│   │
│   ├── summary/
│   │   ├── SummaryScreen.kt
│   │   └── SummaryViewModel.kt
│   │
│   ├── profile/
│   │   ├── ProfileScreen.kt
│   │   └── ProfileViewModel.kt
│   │
│   ├── settings/
│   │   ├── SettingsScreen.kt
│   │   └── SettingsViewModel.kt
│   │
│   ├── backup/
│   │   ├── BackupScreen.kt
│   │   └── BackupViewModel.kt
│   │
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
│
└── MainActivity.kt
```

AI coding agent boleh menyesuaikan struktur ketika diperlukan, tetapi harus mempertahankan pemisahan tanggung jawab setiap layer.

---

# 5. Navigation Architecture

Aplikasi menggunakan Bottom Navigation.

Menu utama:

1. Beranda
2. Ringkasan
3. Profil

Struktur:

```text
                    MyHabbit
                       |
              +--------+--------+
              |        |        |
              v        v        v
           Beranda  Ringkasan  Profil
              |        |        |
              |        |        +-- Pengaturan
              |        |        +-- Backup
              |        |        +-- Restore
              |        |
              |        +-- Summary List
              |        +-- Summary Chart
              |
              +-- Daily
              +-- Weekly
              +-- Monthly
              +-- Add Activity
              +-- Edit Activity
              +-- History
```

Navigation menggunakan Navigation Compose.

Screen harus memiliki route yang jelas dan konsisten.

---

# 6. Domain Models

Domain Model tidak boleh bergantung pada Room Entity.

## 6.1 HabitCategory

```kotlin
enum class HabitCategory {
    DAILY,
    WEEKLY,
    MONTHLY
}
```

## 6.2 HabitStatus

```kotlin
enum class HabitStatus {
    PENDING,
    COMPLETED,
    FAILED
}
```

## 6.3 Habit

```kotlin
data class Habit(
    val id: String,
    val title: String,
    val category: HabitCategory,
    val description: String?,
    val isActive: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
    val deletedAt: Long?
)
```

## 6.4 HabitRecord

```kotlin
data class HabitRecord(
    val id: String,
    val habitId: String,
    val periodType: HabitCategory,
    val periodKey: String,
    val status: HabitStatus,
    val createdAt: Long,
    val updatedAt: Long
)
```

## 6.5 User

```kotlin
data class User(
    val id: String,
    val googleId: String?,
    val name: String,
    val email: String,
    val photoUrl: String?
)
```

## 6.6 Summary

```kotlin
data class Summary(
    val total: Int,
    val completed: Int,
    val failed: Int,
    val pending: Int,
    val evaluated: Int,
    val completionRate: Double
)
```

---

# 7. Database Architecture

## 7.1 Database

Gunakan Room Database dengan SQLite.

Nama database:

```text
myhabbit.db
```

Database menjadi sumber data utama aplikasi.

JSON hanya digunakan untuk proses export, import, backup, dan restore.

## 7.2 Database Tables

### Table: habits

| Field | Type | Description |
|---|---|---|
| id | TEXT | Primary key |
| title | TEXT | Activity title |
| category | TEXT | DAILY/WEEKLY/MONTHLY |
| description | TEXT | Optional description |
| isActive | INTEGER | Active state |
| createdAt | INTEGER | Creation timestamp |
| updatedAt | INTEGER | Update timestamp |
| deletedAt | INTEGER | Soft delete timestamp |

### Table: habit_records

| Field | Type | Description |
|---|---|---|
| id | TEXT | Primary key |
| habitId | TEXT | Foreign key |
| periodType | TEXT | DAILY/WEEKLY/MONTHLY |
| periodKey | TEXT | Unique period identifier |
| status | TEXT | PENDING/COMPLETED/FAILED |
| createdAt | INTEGER | Creation timestamp |
| updatedAt | INTEGER | Update timestamp |

### Table: users

| Field | Type | Description |
|---|---|---|
| id | TEXT | Primary key |
| googleId | TEXT | Google account identifier |
| name | TEXT | User name |
| email | TEXT | User email |
| photoUrl | TEXT | Profile image URL |

### Table: settings

| Field | Type | Description |
|---|---|---|
| id | INTEGER | Primary key |
| theme | TEXT | LIGHT/DARK/SYSTEM |
| notificationsEnabled | INTEGER | Notification state |
| lastBackupAt | INTEGER | Last successful backup |

## 7.3 Database Constraints

The following constraints must be implemented:

- `habits.id` must be unique.
- `habit_records.id` must be unique.
- `habit_records.habitId` must reference an existing habit.
- One habit can have only one record for the same period.
- Combination of `habitId` and `periodKey` must be unique.

Recommended unique index:

```text
UNIQUE(habitId, periodKey)
```

This prevents duplicate records for the same habit and period.

---

# 8. Period Management Architecture

Period management is one of the most important business logic components.

Implement it in:

```text
domain/service/PeriodCalculator.kt
```

The UI must not calculate period keys independently.

## 8.1 Daily Period

Format:

```text
YYYY-MM-DD
```

Example:

```text
2026-10-31
```

## 8.2 Weekly Period

Format:

```text
YYYY-Www
```

Example:

```text
2026-W44
```

Week starts Monday and ends Sunday.

Use ISO week-based date calculations.

## 8.3 Monthly Period

Format:

```text
YYYY-MM
```

Example:

```text
2026-10
```

## 8.4 PeriodCalculator Interface

```kotlin
interface PeriodCalculator {
    fun getCurrentPeriodKey(
        category: HabitCategory,
        date: LocalDate
    ): String

    fun getPeriodStartDate(
        category: HabitCategory,
        periodKey: String
    ): LocalDate

    fun getPeriodEndDate(
        category: HabitCategory,
        periodKey: String
    ): LocalDate
}
```

Implementation must use the user's device timezone.

## 8.5 Automatic Reset

Do not delete old records.

When a new period begins:

1. Determine the current period key.
2. Find the record for the selected habit and period.
3. If a record exists, display its status.
4. If no record exists, display Pending.
5. Save the record only when needed or when the user changes its status.

Example:

```text
October 31

habit-001
periodKey = 2026-10-31
status = COMPLETED
```

Next day:

```text
November 1

habit-001
periodKey = 2026-11-01
status = PENDING
```

The previous record remains unchanged.

---

# 9. Habit Record Management

HabitRecordRepository is responsible for managing activity evaluation records.

Required operations:

```kotlin
interface HabitRecordRepository {

    fun observeRecords(
        habitId: String
    ): Flow<List<HabitRecord>>

    suspend fun getRecord(
        habitId: String,
        periodKey: String
    ): HabitRecord?

    suspend fun saveRecord(
        record: HabitRecord
    )

    suspend fun updateStatus(
        habitId: String,
        periodKey: String,
        status: HabitStatus
    )

    suspend fun getRecordsByPeriod(
        periodKey: String
    ): List<HabitRecord>
}
```

Status changes must be persisted immediately.

The application must prevent duplicate records.

---

# 10. Activity Management Use Cases

Create the following use cases:

```text
CreateHabitUseCase
GetHabitsUseCase
GetActiveHabitsUseCase
UpdateHabitUseCase
DeleteHabitUseCase
GetHabitByIdUseCase
```

Responsibilities:

### CreateHabitUseCase

- Validate title.
- Validate category.
- Generate unique ID.
- Save activity.

### UpdateHabitUseCase

- Validate updated fields.
- Preserve original ID.
- Update timestamp.
- Preserve historical records.

### DeleteHabitUseCase

- Perform soft delete.
- Preserve history.
- Remove activity from active dashboard.

---

# 11. Status Management Use Cases

Create:

```text
UpdateHabitStatusUseCase
GetCurrentPeriodRecordsUseCase
GetHabitRecordUseCase
```

Status update flow:

```text
User selects status
        |
        v
ViewModel
        |
        v
UpdateHabitStatusUseCase
        |
        v
PeriodCalculator
        |
        v
HabitRecordRepository
        |
        v
Room Database
        |
        v
Flow emits updated data
        |
        v
Compose UI updates
```

Status must be one of:

```text
PENDING
COMPLETED
FAILED
```

The UI must allow users to change an existing status.

---

# 12. Summary and Statistics Architecture

All summary calculations must be implemented in Domain Layer.

Do not calculate summary directly inside Compose components.

Implement:

```text
CompletionCalculator
GetSummaryUseCase
GetSummaryByCategoryUseCase
GetHabitStatisticsUseCase
```

## 12.1 Completion Formula

```text
evaluated = completed + failed

completionRate =
    completed / evaluated * 100
```

Pending is excluded.

If evaluated is zero:

```text
completionRate = 0
```

## 12.2 Summary Data

Summary must contain:

- Total records
- Completed records
- Failed records
- Pending records
- Evaluated records
- Completion rate

## 12.3 Average Calculation

When calculating average completion rate across multiple periods, use:

```text
averageRate =
    totalCompleted /
    (totalCompleted + totalFailed)
    * 100
```

Do not average percentages from periods with different numbers of evaluated records unless the UI explicitly labels it as an unweighted average.

## 12.4 Chart Data

Charts must receive prepared data from the ViewModel.

Chart components must not query the database directly.

---

# 13. Repository Architecture

Repository interfaces belong in Domain Layer.

Implementations belong in Data Layer.

Example:

```text
domain/
└── repository/
    └── HabitRepository.kt

data/
└── repository/
    └── HabitRepositoryImpl.kt
```

Repositories:

```text
HabitRepository
HabitRecordRepository
SummaryRepository
AuthRepository
BackupRepository
```

Repository responsibilities:

### HabitRepository

- Create activity
- Update activity
- Delete activity
- Observe activities
- Retrieve activity

### HabitRecordRepository

- Create record
- Update record
- Retrieve records
- Observe records

### SummaryRepository

- Retrieve summary
- Retrieve summary by category
- Retrieve chart data

### AuthRepository

- Sign in
- Sign out
- Retrieve current account

### BackupRepository

- Generate JSON backup
- Validate backup
- Export backup
- Import backup
- Upload to Drive
- Download from Drive

---

# 14. Google Authentication Architecture

Google Authentication is optional for the local MVP.

The application must work without requiring login.

When authentication is implemented:

```text
Login Screen
     |
     v
Google Authentication
     |
     v
AuthRepository
     |
     v
Current User
     |
     v
Profile Screen
```

Important rules:

- Never store Google passwords.
- Use official Google authentication mechanisms.
- Do not put authentication logic inside Compose screens.
- Store only necessary user profile information.
- Keep authentication separate from Drive authorization.

Google Sign-In identifies the user.

Google Drive authorization grants permission to access Drive files.

These are related but separate concerns.

---

# 15. Google Drive Architecture

Google Drive is used for backup, not as the primary database.

Architecture:

```text
                 MyHabbit
                     |
                     v
               BackupRepository
                     |
             BackupExporter
                     |
                     v
                JSON File
                     |
                     v
              GoogleDriveManager
                     |
                     v
                Google Drive
```

## 15.1 Backup File

Recommended file:

```text
myhabbit-backup.json
```

Recommended folder:

```text
MyHabbit/
    myhabbit-backup.json
```

Use the Google Drive API with the minimum required permission, preferably `drive.file` when compatible with the selected file-access flow.

The application should create or update only files it is authorized to manage.

## 15.2 Backup Process

```text
Room Database
      |
      v
Read all required data
      |
      v
Convert to Backup Model
      |
      v
Serialize JSON
      |
      v
Validate JSON
      |
      v
Upload to Google Drive
      |
      v
Update lastBackupAt
```

## 15.3 Restore Process

```text
Google Drive
      |
      v
Download JSON
      |
      v
Parse JSON
      |
      v
Validate Schema
      |
      v
Validate Data Integrity
      |
      v
User Confirmation
      |
      v
Database Transaction
      |
      v
Update Local Database
```

Restore must use a Room database transaction.

If restore fails, the existing database must remain unchanged.

## 15.4 Backup Strategy

Initial version:

- Manual backup
- Manual restore
- One primary backup file

Future version:

- Automatic backup
- Backup history
- Multiple backup versions
- Backup reminders

## 15.5 Backup Is Not Sync

Google Drive backup is not real-time synchronization.

For MVP:

```text
Android Device
      |
      v
Local Room Database
      |
      v
JSON Backup
      |
      v
Google Drive
```

If multi-device synchronization is introduced later, a separate synchronization and conflict-resolution architecture will be required.

---

# 16. JSON Serialization Architecture

Use Kotlin Serialization.

Create:

```text
data/backup/
├── BackupSerializer.kt
├── BackupValidator.kt
├── BackupImporter.kt
└── BackupExporter.kt
```

## 16.1 Backup Structure

```json
{
  "schemaVersion": 1,
  "app": {
    "name": "MyHabbit",
    "version": "1.0.0"
  },
  "exportedAt": "2026-10-31T22:00:00Z",
  "user": {
    "id": "user-001",
    "name": "Ahmad",
    "email": "user@example.com"
  },
  "habits": [],
  "records": [],
  "settings": {}
}
```

## 16.2 Versioning

Every backup must contain:

```text
schemaVersion
```

When the JSON structure changes:

- Increase schemaVersion when backward compatibility requires a new schema.
- Implement migrations for supported older backup versions.
- Reject unsupported future schema versions.
- Never silently ignore incompatible critical fields.

## 16.3 Export Requirements

Export must include:

- Active habits
- Archived or soft-deleted habits
- Historical records
- User profile data that is safe to export
- Application settings

Do not export authentication tokens or secrets.

## 16.4 Import Requirements

Import must:

1. Parse JSON.
2. Validate schemaVersion.
3. Validate required fields.
4. Validate unique IDs.
5. Validate habit references in records.
6. Validate enum values.
7. Reject malformed records.
8. Apply data in a database transaction.

---

# 17. Data Mapping

Room Entity and Domain Model must be separated.

Example:

```text
HabitEntity
    |
    v
HabitMapper
    |
    v
Habit
```

For saving:

```text
Habit
    |
    v
HabitMapper
    |
    v
HabitEntity
```

This prevents database implementation details from leaking into Domain Layer.

---

# 18. UI State Architecture

Each screen uses a dedicated ViewModel.

Example:

```kotlin
data class DashboardUiState(
    val isLoading: Boolean = false,
    val selectedCategory: HabitCategory = HabitCategory.DAILY,
    val habits: List<Habit> = emptyList(),
    val summary: Summary? = null,
    val error: String? = null
)
```

ViewModel exposes state using StateFlow.

Example:

```kotlin
class DashboardViewModel(
    private val getHabitsUseCase: GetActiveHabitsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        DashboardUiState()
    )

    val uiState: StateFlow<DashboardUiState> =
        _uiState.asStateFlow()
}
```

Compose observes the state using lifecycle-aware collection.

Do not store persistent application data only in Compose state.

---

# 19. Error Handling

Use a consistent error-handling strategy.

Recommended result structure:

```kotlin
sealed interface AppResult<out T> {

    data class Success<T>(
        val data: T
    ) : AppResult<T>

    data class Error(
        val message: String
    ) : AppResult<Nothing>
}
```

Handle errors for:

- Database failures
- Invalid activity input
- Authentication failures
- Network failures
- Google Drive failures
- Invalid JSON
- Unsupported backup versions
- Restore failures

Errors must be presented clearly to users.

Sensitive technical details must not be exposed in normal UI messages.

---

# 20. Offline-First Strategy

The local database is the source of truth.

All core operations must work offline:

- Dashboard
- Create activity
- Edit activity
- Delete activity
- Change status
- History
- Summary
- Statistics

Internet is only required for:

- Google authentication
- Google Drive backup
- Google Drive restore

Do not make dashboard loading dependent on Google Drive availability.

---

# 21. Security Architecture

Security requirements:

- Never store Google passwords.
- Never store access tokens in plain text.
- Use secure storage for credentials when needed.
- Request minimum necessary Google Drive scopes.
- Do not log tokens or private activity data.
- Validate JSON before importing.
- Use database transactions for restore.
- Avoid exposing personal data through error logs.

Backup JSON contains personal activity history and must be treated as private data.

---

# 22. Time and Date Architecture

All period calculations must use the user's local timezone.

Recommended APIs:

```kotlin
java.time.LocalDate
java.time.LocalDateTime
java.time.YearMonth
java.time.ZoneId
```

Do not use UTC date directly to determine the active daily period.

Store timestamps consistently, preferably as UTC epoch milliseconds.

Use local timezone when calculating:

- Current day
- Current week
- Current month
- Period boundaries

Use ISO week calculations for weekly periods.

Test date changes around:

- Midnight
- Month boundaries
- Year boundaries
- Week boundaries
- Leap years

---

# 23. Dependency Injection

Use Hilt for dependency injection.

Provide dependencies for:

- Room database
- DAOs
- Repositories
- Use cases
- Google authentication manager
- Google Drive manager
- Backup services

Avoid manually constructing dependencies throughout the UI.

---

# 24. Testing Architecture

Testing must be performed at multiple levels.

## 24.1 Unit Tests

Test:

- PeriodCalculator
- CompletionCalculator
- Habit validation
- Status transitions
- Summary calculations
- JSON validation
- Backup serialization

## 24.2 Database Tests

Test:

- Create habit
- Update habit
- Soft delete habit
- Create record
- Update status
- Unique period constraint
- History retrieval
- Database transaction rollback

## 24.3 ViewModel Tests

Test:

- Dashboard state
- Loading state
- Error state
- Status update
- Summary state
- Backup state

## 24.4 UI Tests

Test:

- Navigation
- Activity form
- Activity list
- Status selection
- Summary display
- Backup and restore interaction

---

# 25. Important Business Rules

These rules are mandatory.

1. Room is the primary local database.
2. JSON is only a backup/import/export format.
3. Daily, Weekly, and Monthly activities use different period keys.
4. Week starts Monday and ends Sunday.
5. Period changes never delete previous records.
6. Pending is different from Failed.
7. Pending is excluded from completion rate.
8. Each habit has at most one record per period.
9. Historical records must survive activity edits.
10. Deleted activities must not appear in the active dashboard.
11. Summary calculations belong in Domain Layer.
12. Google Drive is not the primary database.
13. Backup and restore must not corrupt existing data.
14. Authentication and Drive authorization must be separated.
15. Core features must work without internet.

---

# 26. Development Phases

Development must be incremental.

## Phase 1 — Project Foundation

Implement:

- Android project
- Kotlin
- Jetpack Compose
- Material 3
- Navigation
- Hilt
- Base theme

Expected result:

Application launches successfully.

## Phase 2 — Database Foundation

Implement:

- HabitEntity
- HabitRecordEntity
- Room Database
- DAO
- Repository interfaces
- Repository implementations

Expected result:

Local database works.

## Phase 3 — Activity Management

Implement:

- Create activity
- View activity
- Edit activity
- Delete activity
- Category selection

Expected result:

Users can manage activities.

## Phase 4 — Period Engine

Implement:

- Daily period
- Weekly period
- Monthly period
- PeriodCalculator
- Automatic current-period lookup

Expected result:

Correct period is determined automatically.

## Phase 5 — Status Management

Implement:

- Pending
- Completed
- Failed
- Status persistence
- Status update

Expected result:

Users can evaluate activities.

## Phase 6 — Dashboard

Implement:

- Daily list
- Weekly list
- Monthly list
- Current period progress
- Add activity action

Expected result:

Main dashboard works.

## Phase 7 — History

Implement:

- History list
- Date filtering
- Period filtering
- Activity history

Expected result:

Users can review previous records.

## Phase 8 — Summary and Statistics

Implement:

- Completed count
- Failed count
- Pending count
- Completion rate
- Category summary
- Summary filters

Expected result:

Statistics are calculated correctly.

## Phase 9 — Charts

Implement:

- Bar chart
- Donut chart
- Weekly trend
- Monthly trend

Expected result:

Users can visualize progress.

## Phase 10 — Google Authentication

Implement:

- Google sign-in
- User profile
- Sign-out

Expected result:

User can connect a Google account.

## Phase 11 — JSON Backup

Implement:

- JSON serializer
- JSON validator
- Export
- Import
- Schema versioning

Expected result:

Data can be backed up and restored locally.

## Phase 12 — Google Drive

Implement:

- Drive authorization
- Upload backup
- Download backup
- Restore from Drive
- Backup status

Expected result:

Users can store and retrieve backup files from Google Drive.

## Phase 13 — Final Polish

Implement:

- Notifications
- Theme settings
- Accessibility
- Error handling
- Performance improvements

Expected result:

Application is ready for personal use.

---

# 27. AI Coding Agent Rules

AI coding agents must follow these rules.

## Rule 1 — Read Documentation First

Before implementing a feature, read:

- PRD.md
- ARCHITECTURE.md

If additional project documentation exists, read relevant files too.

## Rule 2 — Respect Architecture

Do not:

- Put business logic in Compose screens.
- Access DAO directly from ViewModel.
- Access Google Drive directly from UI.
- Put database queries in chart components.
- Mix backup serialization with UI logic.

## Rule 3 — Work Incrementally

Implement only the requested phase or task.

Do not build all features at once.

## Rule 4 — Explain Changes

Before significant changes, explain:

- Files affected
- Implementation approach
- Dependencies required
- Database changes
- Potential risks

## Rule 5 — Preserve Existing Data

Never delete historical data without explicit instruction.

## Rule 6 — Database Migration

Any database schema change must include an appropriate Room migration.

Do not use destructive migration as a shortcut for production data.

## Rule 7 — Build and Test

After implementation:

1. Build the application.
2. Run relevant tests.
3. Fix compilation errors.
4. Verify acceptance criteria.
5. Report remaining issues.

## Rule 8 — Avoid Unnecessary Dependencies

Use existing project dependencies whenever possible.

## Rule 9 — Do Not Invent Requirements

If a requirement is ambiguous:

1. Identify the ambiguity.
2. Explain its impact.
3. Propose a reasonable solution.
4. Wait for confirmation if the decision affects data structure or important business behavior.

## Rule 10 — Keep Documentation Updated

When architecture or implementation changes significantly, update the relevant documentation.

---

# 28. Definition of Architecture Complete

The architecture foundation is considered complete when:

- Project structure is established.
- Clean Architecture layers are separated.
- MVVM is implemented.
- Room database is configured.
- Repository pattern is working.
- Dependency injection is configured.
- Period calculation is centralized.
- Domain logic is independent of UI.
- Unit tests cover critical business rules.
- The application can run offline.

Google Drive integration is not required for architecture foundation completion.

---

# 29. Final Architecture Summary

```text
                    MyHabbit Android
                           |
                           v
                  PRESENTATION LAYER
                           |
                  Jetpack Compose UI
                           |
                       ViewModel
                           |
                           v
                     DOMAIN LAYER
                           |
                  Use Cases / Services
                           |
                  Period Calculator
                  Completion Calculator
                           |
                           v
                      DATA LAYER
                           |
                       Repository
                           |
               +-----------+-----------+
               |                       |
               v                       v
         LOCAL DATA SOURCE       REMOTE DATA SOURCE
               |                       |
               v                       v
          Room Database          Google Drive API
               |                       |
               v                       v
             SQLite                JSON Backup
```

**Core architectural decision:**

Room Database is the primary source of truth. Google Drive is used for backup and restore. JSON is the portable data format. The application must remain fully usable for its core features without an internet connection.

This architecture must be followed throughout MyHabbit development unless a documented architectural decision explicitly changes it.