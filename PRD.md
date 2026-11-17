# PRD — MyHabbit

**Product Requirements Document**  
**Version:** 1.0  
**Status:** Draft / MVP  
**Platform:** Android  
**Product Type:** Habit & Activity Tracker

---

## 1. Product Overview

### 1.1 Product Name

MyHabbit

### 1.2 Product Description

MyHabbit adalah aplikasi Android sederhana untuk membantu pengguna membuat, menjalankan, mencatat, dan mengevaluasi kegiatan atau kebiasaan secara berkala.

Pengguna dapat membuat kegiatan dengan tiga kategori periode:

- Daily / Harian
- Weekly / Mingguan
- Monthly / Bulanan

Setiap kegiatan memiliki status:

- Pending / Belum dinilai
- Completed / Selesai
- Failed / Tidak dilakukan

Aplikasi menyimpan history setiap penilaian sehingga pengguna dapat melihat perkembangan dan konsistensi dari waktu ke waktu.

Fitur utama:

- Dashboard kegiatan
- Manajemen kegiatan
- History
- Summary
- Statistik
- Chart
- Profile
- Login menggunakan Google
- Backup data dalam format JSON
- Backup JSON ke Google Drive
- Restore data dari Google Drive

---

## 2. Product Goal

Tujuan utama MyHabbit:

> Membantu pengguna membangun kebiasaan dan mengevaluasi konsistensi diri dengan cara yang sederhana, cepat, dan mudah dipahami.

Alur utama:

```text
Buat kegiatan
    ↓
Lakukan kegiatan
    ↓
Pilih ✓ atau ✕
    ↓
Simpan history
    ↓
Hitung statistik
    ↓
Evaluasi
    ↓
Perbaiki kebiasaan
```

---

## 3. Product Principles

### 3.1 Simple

Pengguna tidak membutuhkan banyak langkah untuk mencatat aktivitas.

Idealnya:

```text
Buka aplikasi
→ lihat kegiatan
→ tekan ✓ atau ✕
→ selesai
```

### 3.2 Local First

Aplikasi harus tetap dapat digunakan tanpa koneksi internet untuk fungsi utama.

Internet hanya diperlukan untuk:

- Login Google
- Backup
- Restore
- Sinkronisasi jika fitur tersebut dikembangkan

### 3.3 Data Ownership

Data aktivitas adalah milik pengguna.

Pengguna harus dapat:

- Export data
- Backup data
- Restore data

### 3.4 History Tidak Boleh Hilang

Ketika periode baru dimulai, status kegiatan boleh di-reset tetapi history periode sebelumnya harus tetap tersimpan.

---

## 4. Target User

Target utama:

- Pengguna yang ingin membangun kebiasaan
- Pelajar
- Pekerja
- Programmer
- Pengguna yang ingin meningkatkan produktivitas
- Pengguna yang ingin mengevaluasi rutinitas pribadi

Contoh kegiatan:

- Bangun pagi
- Olahraga
- Membaca
- Belajar Bahasa Inggris
- Belajar programming
- Belajar Python
- Mengerjakan pekerjaan
- Membersihkan rumah
- Kegiatan rutin lainnya

---

# 5. Core Concept

MyHabbit membedakan antara **Habit Definition** dan **Habit Record**.

### 5.1 Habit Definition

Definisi kegiatan yang dibuat pengguna.

Contoh:

```text
Judul:
Belajar Bahasa Inggris

Kategori:
Daily

Deskripsi:
Belajar minimal 15 menit setiap hari
```

### 5.2 Habit Record

Catatan pelaksanaan kegiatan pada periode tertentu.

Contoh:

```text
Tanggal:
2026-10-31

Habit:
Belajar Bahasa Inggris

Status:
completed
```

Habit Definition tidak berubah setiap hari.

Habit Record dibuat untuk mencatat history.

---

# 6. Activity Categories

Aplikasi memiliki tiga kategori utama.

## 6.1 Daily

Kegiatan yang dievaluasi setiap hari.

Contoh:

- Belajar Bahasa Inggris
- Bangun pukul 04.00
- Membaca
- Belajar Python

Setiap hari memiliki record sendiri.

## 6.2 Weekly

Kegiatan yang dievaluasi berdasarkan minggu.

Contoh:

- Lari 3 kali seminggu
- Belajar React
- Membersihkan rumah

Periode minggu dimulai Senin dan berakhir Minggu.

Contoh:

```text
2026-10-26 sampai 2026-11-01
```

## 6.3 Monthly

Kegiatan yang dievaluasi berdasarkan bulan.

Contoh:

- Membaca 1 buku
- Menyelesaikan satu project
- Evaluasi pekerjaan

Periode mengikuti bulan kalender.

Contoh:

```text
2026-10-01 sampai 2026-10-31
```

---

# 7. Activity Status

Setiap activity record mempunyai tiga status.

## 7.1 Pending

Belum dinilai.

Value:

```text
pending
```

UI:

```text
○
```

## 7.2 Completed

Kegiatan berhasil dilakukan.

Value:

```text
completed
```

UI:

```text
✓
```

## 7.3 Failed

Kegiatan tidak dilakukan.

Value:

```text
failed
```

UI:

```text
✕
```

### Important Rule

Pending berbeda dengan Failed.

Contoh:

```text
Belajar Inggris     ○
Olahraga            ✕
Membaca             ✓
```

Artinya:

- Belajar Inggris belum dievaluasi
- Olahraga tidak dilakukan
- Membaca selesai

Pending tidak boleh dihitung sebagai Failed dalam statistik.

---

# 8. Main Features

MVP wajib mempunyai fitur berikut:

1. Dashboard
2. Create Activity
3. Edit Activity
4. Delete Activity
5. Daily Activity
6. Weekly Activity
7. Monthly Activity
8. Mark Completed
9. Mark Failed
10. Reset Period
11. History
12. Summary
13. Statistics
14. Profile
15. Local Storage

Fitur setelah MVP:

16. Google Login
17. JSON Export
18. JSON Import
19. Google Drive Backup
20. Google Drive Restore
21. Notification
22. Chart lanjutan

---

# 9. Dashboard

Dashboard adalah halaman utama.

Dashboard menampilkan kegiatan aktif untuk periode saat ini.

Contoh:

```text
MYHABBIT

31 October 2026

DAILY

○ Bangun pukul 04.00
○ Belajar Bahasa Inggris
✓ Belajar Python
✕ Olahraga

WEEKLY

✓ Lari 3 kali
○ Belajar React

MONTHLY

○ Membaca 1 buku
```

Dashboard harus:

- Menampilkan tanggal saat ini
- Menampilkan activity aktif
- Mengelompokkan activity berdasarkan kategori
- Menampilkan status
- Memungkinkan pengguna mengubah status
- Menampilkan progress sederhana

Contoh:

```text
Daily Progress

3 / 5

60%
```

---

# 10. Status Interaction

Pengguna dapat mengubah status activity.

Contoh:

```text
○
```

Dapat berubah menjadi:

```text
✓
```

atau:

```text
✕
```

Implementasi UI boleh menggunakan:

- Tombol ✓
- Tombol ✕
- Toggle
- Popup pilihan status
- Swipe action

Selama pengguna dapat dengan mudah memilih:

- Pending
- Completed
- Failed

### Implementasi terpilih (mockup UI.png #5)

Kartu di Beranda menampilkan SATU ikon status (○ / ✓ hijau / ✕ merah).
Ketuk ikon tersebut → bottom sheet "Pilih Status" muncul dengan opsi:

```text
Pilih Status

✓  Selesai          (Tandai sebagai selesai)
✕  Tidak Dilakukan  (Tandai sebagai gagal)
○  Belum dinilai    (Kembalikan ke belum dinilai)

[Batal]
```

---

# 11. Create Activity

Pengguna dapat membuat activity baru.

UI dapat menggunakan:

- Screen
- Modal
- Bottom Sheet
- Dialog

Form:

```text
Tambah Activity

Title
[________________]

Category
[ Daily ▼ ]

Description
[________________
 ________________]

[Cancel]       [Save]
```

---

# 12. Activity Fields

## 12.1 Title

Type:

```text
String
```

Required: Yes

Contoh:

```text
Belajar Bahasa Inggris
```

## 12.2 Category

Enum:

```text
daily
weekly
monthly
```

Required: Yes

## 12.3 Description

Type:

```text
String
```

Required: No

Contoh:

```text
Belajar minimal 15 menit.
```

---

# 13. Activity ID

Setiap activity harus mempunyai ID unik.

Contoh:

```text
habit_01HXYZ...
```

ID tidak boleh bergantung pada posisi index array.

---

# 14. Create Validation

Title:

- Tidak boleh kosong
- Minimal 1 karakter
- Maksimal 100 karakter

Description:

- Opsional
- Maksimal 500 karakter

Category:

- Wajib dipilih

---

# 15. Edit Activity

Pengguna dapat mengubah:

- Title
- Category
- Description

ID activity tidak berubah ketika diedit.

History lama tidak boleh hilang ketika activity diedit.

---

# 16. Delete Activity

Pengguna dapat menghapus activity.

Cara menghapus (interaction):

```text
Geser kartu activity ke KIRI (di Beranda atau daftar Activity)
→ tombol "Hapus" (merah) muncul di sisi kanan kartu
→ ketuk tombol tersebut
→ dialog konfirmasi muncul (lihat di bawah)
```

Beranda juga menyediakan link "Lihat semua activity" untuk membuka daftar lengkap.

Sebelum delete:

```text
Delete Activity?

This action cannot be undone.

[Cancel]
[Delete]
```

Default action:

```text
Cancel
```

### Recommended Delete Strategy

Activity sebaiknya menggunakan soft delete:

```text
deletedAt = timestamp
```

daripada langsung menghapus data secara permanen.

Dengan demikian history dapat tetap tersedia.

---

# 17. Period System

Sistem harus mempunyai konsep period.

## 17.1 Daily Period

Format:

```text
YYYY-MM-DD
```

Contoh:

```text
2026-10-31
```

## 17.2 Weekly Period

Format internal:

```text
YYYY-Www
```

Contoh:

```text
2026-W44
```

Minggu dimulai Senin.

## 17.3 Monthly Period

Format:

```text
YYYY-MM
```

Contoh:

```text
2026-10
```

---

# 18. Daily Reset

Daily activity mendapatkan record baru setiap tanggal.

Contoh:

```text
31 Oct
Belajar Inggris = completed

01 Nov
Belajar Inggris = pending
```

Record tanggal 31 Oktober tidak boleh diubah menjadi record tanggal 1 November.

Tidak boleh ada proses yang menghapus record lama ketika tanggal berubah.

---

# 19. Weekly Reset

Weekly activity menggunakan periode minggu.

Contoh:

```text
Week 44

Lari
completed
```

Ketika masuk:

```text
Week 45
```

activity mendapatkan periode baru.

History Week 44 tetap tersimpan.

---

# 20. Monthly Reset

Monthly activity menggunakan periode bulan.

Contoh:

```text
October 2026
```

Kemudian:

```text
November 2026
```

mendapatkan periode baru.

History Oktober tetap tersimpan.

---

# 21. History

History menampilkan aktivitas sebelumnya.

Contoh:

```text
Belajar Bahasa Inggris

31 Oct    ✓
30 Oct    ✓
29 Oct    ✕
28 Oct    ✓
27 Oct    ✓
```

Filter minimal:

- This Week
- This Month

Filter yang dapat ditambahkan:

- Today
- Previous Month
- Custom Range
- By Activity
- By Category

---

# 22. Summary

Summary adalah halaman evaluasi.

Summary dapat menampilkan:

- Total Activity Records
- Completed
- Failed
- Pending
- Evaluated
- Completion Rate
- Average

Contoh:

```text
SUMMARY

October 2026

Total evaluated : 100
Completed       : 78
Failed          : 22

Completion Rate
78%
```

---

# 23. Summary by Category

Summary dapat difilter:

```text
All
Daily
Weekly
Monthly
```

Contoh:

```text
DAILY

Belajar Inggris       90%
Belajar Python        85%
Olahraga              70%

WEEKLY

Lari                  80%
React                 75%

MONTHLY

Membaca               100%
```

---

# 24. Completion Rate

Formula:

```text
Completion Rate =
Completed /
(Completed + Failed)
× 100
```

Pending tidak masuk denominator.

Contoh:

```text
Completed = 8
Failed = 2
Pending = 5

Rate =
8 / (8 + 2) × 100

= 80%
```

Jika:

```text
Completed + Failed = 0
```

maka:

```text
Completion Rate = 0%
```

---

# 25. Daily Summary

Contoh:

```text
31 October 2026

Completed : 4
Failed    : 1
Pending   : 2

Progress:
80%
```

---

# 26. Weekly Summary

Contoh:

```text
Week 44

Completed : 20
Failed    : 5

Completion Rate:
80%
```

---

# 27. Monthly Summary

Contoh:

```text
October 2026

Completed : 90
Failed    : 30

Completion Rate:
75%
```

---

# 28. Statistics and Charts

MVP dapat menggunakan:

## 28.1 Bar Chart

Untuk membandingkan activity.

Contoh:

```text
Belajar Inggris  ██████████████████ 90%
Python           ████████████████   80%
Olahraga         ██████████████     70%
Membaca          ████████████       60%
```

## 28.2 Donut / Pie Chart

Untuk:

```text
Completed
Failed
Pending
```

Chart harus menggunakan data yang sama dengan summary dan tidak memiliki business logic sendiri.

---

# 29. Profile

Profile menampilkan:

```text
Name
Email
Profile Picture
```

Jika Google Login aktif, profile berasal dari Google Account.

Profile juga dapat menampilkan:

```text
Total Activities
Total Completed
Completion Rate
```

---

# 30. Settings

Settings minimal:

```text
Account
Backup
Restore
Theme
Notifications
About
```

MVP awal dapat hanya mempunyai:

```text
Account
About
```

---

# 31. Google Authentication

Login menggunakan Google Account.

Tujuan:

- Identifikasi pengguna
- Menghubungkan data dengan akun
- Akses Google Drive

Aplikasi tidak boleh menyimpan password Google.

Authentication harus menggunakan mekanisme resmi Google.

---

# 32. Google Drive Backup

Google Drive digunakan sebagai lokasi backup.

Struktur yang direkomendasikan:

```text
MyHabbit/
    myhabbit-backup.json
```

File backup berisi data aplikasi.

---

# 33. Backup Flow

Pengguna dapat memilih:

```text
Backup Now
```

Proses:

```text
Local Database
      ↓
Generate JSON
      ↓
Validate JSON
      ↓
Upload to Google Drive
```

Setelah berhasil:

```text
Backup successful

Last backup:
31 October 2026 22:15
```

Backup otomatis dapat menjadi fitur setelah MVP.

---

# 34. Restore Flow

Proses:

```text
Google Drive
      ↓
Download JSON
      ↓
Validate JSON
      ↓
Confirm
      ↓
Import
      ↓
Local Database
```

Sebelum restore:

```text
Warning

Restoring data will replace current data.

[Cancel]
[Continue]
```

Restore harus memvalidasi data sebelum mengganti database aktif.

---

# 34a. Reset Data

Layar Profil menyediakan card **Reset Data** (danger zone) dengan 2 aksi.

## 34a.1 Reset Ringkasan

Menghapus semua record status (habit_records) — history/chart/status mulai dari nol.

Activity, akun, dan pengaturan TETAP utuh.

Sebelum reset:

```text
Reset summary data?

All status history will be deleted. Activities are kept.
This action cannot be undone.

[Cancel]
[Reset]
```

## 34a.2 Hard Reset Semua Data

Menghapus SEMUA data habit (habits + records) — aplikasi kembali seperti baru.

Akun Google dan pengaturan (tema/notifikasi) tidak dihapus.

Sebelum reset:

```text
Reset ALL data?

All activities and history will be deleted.
This action cannot be undone.

[Cancel]
[Delete everything]
```

## 34a.3 Aturan

- Kedua reset wajib melewati dialog konfirmasi (default action: Cancel).
- Reset menghapus data secara PERMANEN — tidak seperti delete activity (soft delete).
- Setelah reset, chart/ringkasan otomatis sesuai karena dihitung dari habit_records.
- Card menampilkan peringatan "Backup dulu disarankan".
- Not "Never delete historical records" (§46) dianggap dikecualikan karena user memberi instruksi eksplisit via konfirmasi.

---

# 35. JSON Schema

JSON harus mempunyai version.

Contoh:

```json
{
  "schemaVersion": 1,
  "app": {
    "name": "MyHabbit",
    "version": "1.0.0"
  },
  "user": {
    "id": "google-user-id",
    "name": "Ahmad",
    "email": "user@example.com"
  },
  "habits": [],
  "records": [],
  "settings": {}
}
```

---

# 36. Habit JSON

Contoh:

```json
{
  "id": "habit-001",
  "title": "Belajar Bahasa Inggris",
  "category": "daily",
  "description": "Belajar selama 15 menit",
  "isActive": true,
  "createdAt": "2026-10-31T08:00:00Z",
  "updatedAt": "2026-10-31T08:00:00Z"
}
```

---

# 37. Record JSON

Contoh:

```json
{
  "id": "record-001",
  "habitId": "habit-001",
  "periodType": "daily",
  "periodKey": "2026-10-31",
  "status": "completed",
  "createdAt": "2026-10-31T21:00:00Z",
  "updatedAt": "2026-10-31T21:00:00Z"
}
```

---

# 38. JSON Enums

Status:

```text
pending
completed
failed
```

Category:

```text
daily
weekly
monthly
```

---

# 39. Database Architecture

JSON bukan database utama.

Database utama menggunakan Room / SQLite.

JSON digunakan untuk:

- Backup
- Restore
- Export
- Import

Arsitektur:

```text
                  ┌── Room Database
                  │
MyHabbit ─────────┤
                  │
                  └── JSON Backup
                           │
                           ↓
                     Google Drive
```

---

# 40. Local Database Tables

## 40.1 habits

Fields:

```text
id
title
category
description
isActive
createdAt
updatedAt
deletedAt
```

## 40.2 habit_records

Fields:

```text
id
habitId
periodType
periodKey
status
createdAt
updatedAt
```

## 40.3 user

Fields:

```text
id
googleId
name
email
photoUrl
createdAt
updatedAt
```

## 40.4 settings

Fields:

```text
id
theme
notificationsEnabled
lastBackupAt
```

---

# 41. Database Relationships

Relationship:

```text
User
  │
  └── Habits
         │
         └── Habit Records
```

One habit:

```text
1 Habit
   ↓
many Records
```

Example:

```text
Belajar Inggris
      │
      ├── 2026-10-27 ✓
      ├── 2026-10-28 ✓
      ├── 2026-10-29 ✕
      ├── 2026-10-30 ✓
      └── 2026-10-31 ✓
```

---

# 42. Recommended Android Architecture

Use:

```text
Presentation
     ↓
ViewModel
     ↓
Domain
     ↓
Repository
     ↓
Data
     ↓
Room
```

Google Drive:

```text
Repository
     │
     ├── LocalDataSource
     │       ↓
     │      Room
     │
     └── RemoteDataSource
             ↓
        Google Drive
```

---

# 43. Recommended Technology Stack

Recommended:

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- ViewModel
- Room
- Kotlin Coroutines
- Android Keystore / secure storage where needed
- Google Authentication
- Google Drive API

Chart library can be selected during the chart implementation phase.

---

# 44. Offline-First Behavior

The application must support:

- Open dashboard without internet
- Create activity without internet
- Edit activity without internet
- Change status without internet
- View history without internet
- View summary without internet

Internet is required for:

- Google Login
- Google Drive backup
- Google Drive restore

---

# 45. Error Handling

If Google Drive fails:

```text
Backup failed

Unable to connect to Google Drive.

Please check your internet connection.

[Retry]
```

If JSON is invalid:

```text
Invalid backup file.

The selected file is not a valid MyHabbit backup.
```

If restore fails:

```text
Restore failed.

Your current data has not been changed.
```

---

# 46. Data Integrity

Restore must not delete the existing database before the backup file is validated.

Process:

```text
Download
   ↓
Validate
   ↓
Parse
   ↓
Check schemaVersion
   ↓
Create temporary data
   ↓
Replace database
```

If any stage fails:

```text
Existing database remains unchanged.
```

---

# 47. Security

Requirements:

- Never store Google password
- Use OAuth
- Do not store OAuth tokens in plain text
- Use secure Android storage for credentials/tokens when required
- Request only necessary Google Drive permissions
- Treat backup JSON as private user data
- Do not log sensitive user data
- Do not expose access tokens in logs

---

# 48. Notification

Feature after MVP.

Example:

```text
Belajar Inggris
20:00
```

Notification:

```text
Sudah belajar Bahasa Inggris hari ini?
```

Notification must not automatically mark an activity as completed or failed.

---

# 49. Theme

Supported themes:

```text
Light
Dark
System Default
```

Default:

```text
System Default
```

---

# 50. Accessibility

UI must:

- Use readable text sizes
- Use sufficiently large touch targets
- Not rely only on color to represent status
- Show ✓ and ✕ clearly
- Provide content descriptions for icons
- Support system font scaling where practical

---

# 51. Performance

Requirements:

- Dashboard should open quickly
- Status updates should feel immediate
- Avoid unnecessary database queries
- History should support pagination if data becomes large
- Chart rendering should not block the main UI thread

---

# 52. Timezone

Date and period calculations must use the device's local timezone.

The application must not use UTC directly to determine the user's local day.

---

# 53. Week Definition

A week:

```text
Monday → Sunday
```

Example:

```text
Monday 2026-10-26
        ↓
Sunday 2026-11-01
```

---

# 54. Date Change

The application determines the active period from the current local date.

When the user opens the application on a new day, the application automatically uses the new period.

No manual reset action is required.

The system must not delete old records.

---

# 55. Important Design Decision

Do not implement reset as:

```text
delete old data
create new data
```

Instead use:

```text
Habit Definition
      +
Period
      +
Habit Record
```

This preserves history.

---

# 56. Example Daily Flow

User creates:

```text
Title:
Belajar Bahasa Inggris

Category:
Daily

Description:
Belajar 15 menit
```

System creates:

```text
habit-001
```

On 2026-10-31, system searches for:

```text
habit-001
+
periodKey = 2026-10-31
```

If no record exists:

```text
○
```

When user selects completed:

```text
record-001
status = completed
periodKey = 2026-10-31
```

On 2026-11-01:

```text
habit-001
+
periodKey = 2026-11-01
```

No previous record is reused.

UI becomes:

```text
○
```

History:

```text
31 Oct ✓
01 Nov ○
```

---

# 57. Example Weekly Flow

Activity:

```text
Lari 3 kali seminggu
```

Category:

```text
weekly
```

Current period:

```text
2026-W44
```

When completed:

```text
periodKey = 2026-W44
status = completed
```

When week changes:

```text
2026-W45
```

The application uses a new period.

History remains:

```text
Week 44 ✓
Week 45 ○
```

---

# 58. Example Monthly Flow

Activity:

```text
Baca 1 buku
```

Category:

```text
monthly
```

October period:

```text
2026-10
```

November period:

```text
2026-11
```

October records remain available in history.

---

# 59. Summary Logic

Summary uses records with:

```text
status = completed
```

or:

```text
status = failed
```

Pending is not included in completion rate.

Formula:

```text
evaluated = completed + failed

completionRate =
completed / evaluated * 100
```

If:

```text
evaluated = 0
```

then:

```text
completionRate = 0
```

---

# 60. Acceptance Criteria — Activity

Feature is complete when:

- User can create an activity
- Title is required
- Category is required
- Description is optional
- Activity appears on dashboard
- Activity can be edited
- Activity can be deleted
- Activity ID is unique

---

# 61. Acceptance Criteria — Status

Feature is complete when:

- Activity has pending state
- User can select completed
- User can select failed
- User can return to pending
- Status persists after app restart

---

# 62. Acceptance Criteria — Daily

Feature is complete when:

- Daily activity uses date-based periods
- Previous day's record remains stored
- Next day has a new record
- Previous status is not carried to the next day

---

# 63. Acceptance Criteria — Weekly

Feature is complete when:

- Week starts Monday
- Week ends Sunday
- Weekly activity uses a period key
- New week uses a new period
- Previous week history remains available

---

# 64. Acceptance Criteria — Monthly

Feature is complete when:

- Monthly activity uses calendar month
- New month uses a new period
- Previous month history remains available

---

# 65. Acceptance Criteria — Summary

Feature is complete when:

- Completed count is correct
- Failed count is correct
- Pending is not counted as evaluated
- Completion rate is correct
- Category filter works
- Summary does not modify data

---

# 66. Acceptance Criteria — Backup

Feature is complete when:

- Data can be converted to JSON
- JSON has schemaVersion
- JSON can be validated
- JSON can be stored
- JSON can be restored
- Invalid JSON is rejected
- Failed restore does not corrupt the existing database

---

# 67. Acceptance Criteria — Google Drive

Feature is complete when:

- User can sign in with Google
- Application requests required permissions
- Backup can be created or updated
- Backup is stored in Google Drive
- Backup can be downloaded
- Restore can be performed
- Network errors are handled

---

# 68. MVP Scope

MVP focuses on:

```text
1. Android project
2. Dashboard
3. Activity CRUD
4. Daily
5. Weekly
6. Monthly
7. Pending
8. Completed
9. Failed
10. History
11. Summary
12. Statistics
13. Room Database
```

MVP does not require:

```text
Google Login
Google Drive
Notification
Advanced Chart
Cloud Sync
Social Features
Gamification
```

---

# 69. Phase 2

After MVP is stable:

```text
Google Authentication
        ↓
Google Drive API
        ↓
JSON Export
        ↓
JSON Import
        ↓
Backup
        ↓
Restore
```

---

# 70. Phase 3

After backup is stable:

```text
Notification
Advanced Summary
Charts
Theme
Settings
Improved History
```

---

# 71. Phase 4 — Future Features

Possible future features:

- Multiple device sync
- Automatic backup
- Conflict resolution
- Recurring targets
- Streaks
- Achievements
- Android widget
- Calendar view
- CSV export
- PDF report
- Custom categories
- Archive
- Search

Future features must not break the MVP architecture.

---

# 72. Out of Scope

For the first version, do not implement:

- Social network
- Activity sharing
- Chat
- Leaderboard
- Subscription
- Advertisement
- Complex AI
- Public profile
- Web application
- iOS application

---

# 73. Success Metrics

The application is successful if a user can:

1. Create an activity in less than 30 seconds
2. Record an activity in one or two taps
3. See today's progress
4. See weekly progress
5. See monthly progress
6. View history
7. Understand consistency
8. Backup data
9. Restore data

---

# 74. Development Priority

## P0 — Critical

- Data Model
- Database
- Activity CRUD
- Status
- Period Engine
- History

## P1 — High

- Dashboard
- Summary
- Statistics

## P2 — Medium

- Google Login
- JSON Backup
- Google Drive

## P3 — Low

- Charts
- Notification
- Theme
- Advanced Features

---

# 75. Recommended Project Structure

```text
app/
├── data/
│   ├── local/
│   │   ├── dao/
│   │   ├── database/
│   │   └── entity/
│   │
│   ├── remote/
│   │   ├── auth/
│   │   └── drive/
│   │
│   └── repository/
│
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
│
├── ui/
│   ├── dashboard/
│   ├── activity/
│   ├── history/
│   ├── summary/
│   ├── profile/
│   └── settings/
│
├── navigation/
├── util/
└── MainActivity.kt
```

---

# 76. Development Rules for AI Coding Agent

AI coding agent must follow these rules.

### Rule 1 — Incremental Development

Do not implement the entire application at once.

Implement one phase at a time.

### Rule 2 — Database Changes

Before changing the database schema, explain:

- What changes
- Why it changes
- Which tables are affected
- Whether migration is required

### Rule 3 — Preserve History

Never delete historical records unless explicitly instructed.

### Rule 4 — JSON Is Not the Primary Database

Use Room/SQLite as the primary local database.

Use JSON for:

- Export
- Import
- Backup
- Restore

### Rule 5 — Separate Business Logic

Business logic must not be placed directly inside Compose UI components.

### Rule 6 — Repository Pattern

Use repository abstraction between UI/domain and data sources.

### Rule 7 — ViewModel

Use ViewModel for UI state and business-operation coordination.

### Rule 8 — Coroutines

Use Kotlin Coroutines for asynchronous operations.

### Rule 9 — Dependencies

Do not add dependencies unless necessary.

### Rule 10 — Acceptance Criteria

Every feature must be verified against its acceptance criteria.

### Rule 11 — Build After Changes

After significant implementation changes:

1. Build the project
2. Run tests
3. Fix compilation errors
4. Report the result

### Rule 12 — Do Not Guess

If a requirement is ambiguous, do not silently invent behavior.

Identify the ambiguity and propose the smallest reasonable solution.

---

# 77. AI Development Workflow

The AI coding agent should follow this workflow:

```text
1. Read the PRD
2. Identify the current development phase
3. Analyze existing project structure
4. Identify affected files
5. Explain implementation plan
6. Implement the requested feature
7. Build the application
8. Run tests
9. Fix errors
10. Verify acceptance criteria
11. Summarize changes
12. Wait for the next task
```

The AI must not modify unrelated features without explicit instruction.

---

# 78. Development Phases

## Phase 1 — Project Setup

Tasks:

- Create Android project
- Configure Kotlin
- Configure Jetpack Compose
- Configure Material 3
- Configure Navigation

Expected result:

```text
Application launches successfully.
```

---

## Phase 2 — Database

Tasks:

- Create Habit entity
- Create HabitRecord entity
- Create User entity
- Create Settings entity
- Create DAO
- Create Room database
- Configure database migrations

Expected result:

```text
Database works locally.
```

---

## Phase 3 — Activity Management

Implement:

```text
Create
Read
Update
Delete
```

Expected result:

```text
User can manage activities.
```

---

## Phase 4 — Period Engine

Implement:

```text
Daily
Weekly
Monthly
```

Expected result:

```text
Correct period is automatically determined.
```

---

## Phase 5 — Status

Implement:

```text
Pending
Completed
Failed
```

Expected result:

```text
User can evaluate activities.
```

---

## Phase 6 — History

Implement:

```text
History
Filtering
Period selection
```

---

## Phase 7 — Summary

Implement:

```text
Completed
Failed
Pending
Evaluated
Completion Rate
Average
```

---

## Phase 8 — Charts

Implement:

```text
Bar Chart
Donut Chart
```

Charts must use existing summary/statistics data.

---

## Phase 9 — Google Authentication

Implement:

```text
Google Sign In
Profile
Account
```

---

## Phase 10 — JSON

Implement:

```text
Export
Import
Validation
Schema Version
```

---

## Phase 11 — Google Drive

Implement:

```text
Upload Backup
Download Backup
Restore
```

---

## Phase 12 — Polish

Implement:

```text
Notification
Theme
Accessibility
Error Handling
Performance
```

---

# 79. Final Product Flow

```text
                    MyHabbit
                       │
          ┌────────────┼────────────┐
          ↓            ↓            ↓
       Dashboard     Summary      Profile
          │            │            │
          │            │            ├── Account
          │            │            ├── Backup
          │            │            ├── Restore
          │            │            └── Settings
          │            │
          ↓            ↓
      Activities     Statistics
          │
     ┌────┼────┐
     ↓    ↓    ↓
   Daily Weekly Monthly
     │
     ↓
Pending / Completed / Failed
     │
     ↓
   History
     │
     ↓
   Summary
```

---

# 80. Definition of Done

MyHabbit MVP dianggap selesai apabila:

- Aplikasi dapat dijalankan di Android
- User dapat membuat activity
- User dapat mengedit activity
- User dapat menghapus activity
- Activity dapat dikategorikan Daily/Weekly/Monthly
- User dapat memilih Completed
- User dapat memilih Failed
- Status Pending tersedia
- Daily period bekerja
- Weekly period bekerja
- Monthly period bekerja
- History tersimpan
- History tidak hilang ketika periode berubah
- Summary dapat dihitung
- Completion rate benar
- Aplikasi dapat digunakan offline
- Data tersimpan di local database
- Tidak ada crash pada alur utama

Setelah semua kondisi tersebut terpenuhi, project dapat dilanjutkan ke:

```text
Google Login
      ↓
JSON Backup
      ↓
Google Drive
      ↓
Restore
```

---

# 81. Product Vision

MyHabbit bukan sekadar aplikasi checklist.

Konsep jangka panjang:

```text
PLAN
 ↓
DO
 ↓
RECORD
 ↓
MEASURE
 ↓
REFLECT
 ↓
IMPROVE
```

Pengguna menentukan kegiatan.

Pengguna menjalankan kegiatan.

Pengguna mencatat hasilnya.

MyHabbit mengukur konsistensi.

Pengguna melihat kelemahan.

Pengguna memperbaiki kebiasaan.

Siklus tersebut menjadi inti dari MyHabbit.

---

# 82. UI Reference (UI.png)

File `UI.png` adalah referensi tampilan (mockup) MyHabbit.
Implementasi mengikuti referensi ini dengan beberapa penyesuaian.

## 82.1 Warna (Brand)

- Ungu utama: #5246C8 (terang) / #C3BBFF (gelap) — top bar, tombol utama
- Container lavender: #E4DFFF (kartu Progress/FAB terang)
- Status Selesai: hijau ✓ (#2E7D32 terang / #8BD98F gelap)
- Status Tidak Dilakukan: merah ✕ (#B3261E terang / #FFB4AB gelap)
- Dynamic color (Material You) TIDAK dipakai — brand konsisten dengan mockup

## 82.2 Splash Screen

Sesuai mockup #1: latar gradient ungu, logo kotak putih dengan ✓ ungu,
teks "MyHabbit" + tagline "Track • Plan • Achieve", spinner putih.
Ditampilkan ±1,6 detik saat aplikasi dibuka.

## 82.3 Ikon

- Top bar Beranda: LATAR UNGU menyatu dengan status bar, logo ✓ putih, judul & ikon putih (semua layar memakai AppTopBar yang sama)
- Status: lingkaran berisi ✓ hijau / ✕ merah / ○ kosong (simbol + warna)
- Bottom nav: Beranda (rumah), Ringkasan (grafik), Profil (orang)

## 82.4 Penyesuaian dari mockup

- Login: HANYA Google Sign-In (mockup punya "Masuk dengan Email" — tidak diimplementasi)
- Beranda: tab filter Harian/Mingguan/Bulanan (mockup #3) mengganti section berurutan —
  default tab Harian, tab terpilih berbentuk pill ungu terisi, yang lain outline;
  tab tanpa activity menampilkan pesan "Belum ada activity di kategori ini"
- Popup konfirmasi hapus/restore/reset memakai AlertDialog Material, tetap sesuai gaya aplikasi
