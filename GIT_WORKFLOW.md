# GIT_WORKFLOW.md

# MyHabbit — Git & GitHub Workflow

# repository
- https://github.com/ahmadsiddiq-lang/MyHabbit.git

## 1. Tujuan

Dokumen ini menetapkan aturan penggunaan Git dan GitHub selama pengembangan aplikasi MyHabbit.

Tujuannya adalah:

- Menjaga riwayat perubahan kode.
- Mempermudah pelacakan kesalahan.
- Memisahkan pengembangan fitur dari kode utama.
- Memungkinkan pemulihan kode ketika terjadi kesalahan.
- Menjaga project tetap terorganisasi.
- Memastikan AI tidak melakukan operasi Git yang berisiko tanpa persetujuan pengguna.

Dokumen ini wajib dibaca dan dipatuhi oleh AI maupun developer yang bekerja pada project MyHabbit.

---

## 2. Repository

Project MyHabbit menggunakan Git sebagai version control dan GitHub sebagai remote repository.

Ketentuan:

1. Repository GitHub menjadi tempat penyimpanan kode sumber project.
2. Branch `main` merupakan branch utama.
3. Seluruh perubahan harus memiliki riwayat commit.
4. Jangan menyimpan file hasil build atau file konfigurasi lokal yang bersifat sensitif.
5. Jangan mengunggah kredensial, token, password, atau secret key ke repository.

Nama repository yang disarankan:

`MyHabbit`

---

## 3. Struktur Branch

Gunakan strategi branch sederhana dengan `main` sebagai branch utama dan branch khusus untuk pengembangan fitur.

### 3.1 Main Branch

`main`

Ketentuan:

- Berisi kode yang sudah stabil.
- Tidak digunakan untuk eksperimen.
- Tidak boleh diubah secara langsung oleh AI tanpa persetujuan pengguna.
- Perubahan harus melalui proses review dan persetujuan.

### 3.2 Feature Branch

Format:

`feature/nama-fitur`

Contoh:

- `feature/project-foundation`
- `feature/activity-management`
- `feature/period-calculator`
- `feature/status-management`
- `feature/dashboard`
- `feature/activity-history`
- `feature/summary-statistics`
- `feature/google-authentication`
- `feature/json-backup`

Setiap fitur baru harus dikembangkan melalui branch tersendiri.

### 3.3 Bugfix Branch

Format:

`fix/nama-perbaikan`

Contoh:

- `fix/activity-status`
- `fix/database-migration`
- `fix/period-calculation`

### 3.4 Documentation Branch

Format:

`docs/nama-dokumentasi`

Contoh:

- `docs/update-readme`
- `docs/update-architecture`

---

## 4. Commit Convention

Gunakan format Conventional Commits.

Format:

`type: description`

Jenis commit yang digunakan:

| Type | Keterangan |
|---|---|
| feat | Menambahkan fitur baru |
| fix | Memperbaiki bug |
| docs | Mengubah dokumentasi |
| refactor | Merapikan kode tanpa mengubah fungsi |
| test | Menambahkan atau memperbaiki pengujian |
| style | Perubahan format atau tampilan kode |
| chore | Pemeliharaan project atau konfigurasi |
| perf | Meningkatkan performa aplikasi |
| build | Perubahan konfigurasi build |

Contoh:

```bash
git commit -m "feat: add activity management"
git commit -m "fix: correct weekly period calculation"
git commit -m "docs: update architecture documentation"
git commit -m "test: add period calculator tests"
git commit -m "refactor: improve habit repository"
```

Ketentuan commit:

1. Gunakan deskripsi yang singkat dan jelas.
2. Satu commit sebaiknya mewakili satu perubahan yang logis.
3. Hindari commit dengan pesan seperti `update`, `fix`, atau `changes` tanpa penjelasan.
4. Jangan menggabungkan perubahan yang tidak berkaitan dalam satu commit.
5. Jangan membuat commit jika build atau pengujian belum dijalankan, kecuali ada alasan yang dijelaskan secara eksplisit.

---

## 5. Development Workflow

Setiap pengembangan fitur wajib mengikuti alur berikut.

### Step 1 — Periksa Kondisi Repository

Sebelum melakukan perubahan:

```bash
git status
git branch
git log -5 --oneline
```

Tujuan:

- Memastikan branch yang sedang aktif.
- Memeriksa perubahan yang belum di-commit.
- Melihat riwayat commit terakhir.
- Menghindari konflik dengan pekerjaan sebelumnya.

AI tidak boleh mengabaikan perubahan lokal yang belum di-commit.

### Step 2 — Sinkronisasi Main

Jika tidak ada perubahan lokal yang belum diselesaikan:

```bash
git checkout main
git pull origin main
```

Jika terdapat perubahan lokal, jangan melakukan checkout atau pull yang berpotensi menimpa pekerjaan pengguna. Laporkan kondisinya terlebih dahulu.

### Step 3 — Buat Feature Branch

Contoh:

```bash
git checkout -b feature/activity-management
```

Gunakan satu branch untuk satu fitur atau satu kelompok pekerjaan yang saling berkaitan.

### Step 4 — Implementasi Fitur

AI wajib:

1. Membaca PRD.md.
2. Membaca ARCHITECTURE.md.
3. Membaca PROMPT.md.
4. Membaca GIT_WORKFLOW.md.
5. Memeriksa kode yang sudah ada.
6. Menyampaikan rencana implementasi.
7. Menunggu persetujuan pengguna.
8. Mengimplementasikan hanya fitur yang disetujui.

### Step 5 — Build dan Testing

Setelah implementasi:

- Jalankan build project.
- Jalankan unit test yang relevan.
- Periksa error dan warning penting.
- Pastikan fitur memenuhi acceptance criteria.
- Periksa perubahan file dengan `git diff`.

Contoh:

```bash
git diff
git status
```

Untuk build Android menggunakan Gradle Wrapper:

```bash
./gradlew assembleDebug
```

Untuk menjalankan unit test:

```bash
./gradlew test
```

AI harus melaporkan hasil sebenarnya. Jangan pernah mengklaim build atau test berhasil jika perintah tersebut belum dijalankan.

### Step 6 — Commit

Commit hanya dilakukan setelah pengguna menyetujui commit tersebut.

Contoh:

```bash
git add .
git commit -m "feat: add activity management"
```

Sebelum menjalankan `git add .`, periksa terlebih dahulu file yang akan dimasukkan.

Utamakan menambahkan file secara spesifik apabila terdapat file yang tidak seharusnya masuk repository.

### Step 7 — Push ke GitHub

Push hanya dilakukan setelah mendapatkan persetujuan pengguna.

Contoh:

```bash
git push -u origin feature/activity-management
```

Jangan pernah melakukan push otomatis tanpa persetujuan.

### Step 8 — Review dan Merge

Setelah fitur selesai:

1. Periksa perubahan pada feature branch.
2. Pastikan build dan test berhasil.
3. Laporkan hasil implementasi kepada pengguna.
4. Tunggu persetujuan pengguna untuk merge.
5. Merge dapat dilakukan melalui Pull Request GitHub atau secara lokal setelah disetujui.

Contoh merge lokal:

```bash
git checkout main
git pull origin main
git merge --no-ff feature/activity-management
```

Setelah merge, push branch `main` hanya jika pengguna menyetujuinya.

---

## 6. Aturan Khusus untuk AI

AI wajib mengikuti aturan berikut ketika bekerja dengan Git.

### 6.1 Operasi yang Boleh Dilakukan Setelah Pemeriksaan

AI boleh menjalankan perintah pemeriksaan yang tidak mengubah repository, seperti:

```bash
git status
git branch
git log
git diff
git remote -v
git show
```

AI juga boleh memeriksa isi repository dan mengidentifikasi perubahan yang diperlukan.

### 6.2 Operasi yang Memerlukan Persetujuan

AI wajib meminta persetujuan sebelum menjalankan:

- `git add`
- `git commit`
- `git push`
- `git merge`
- `git rebase`
- `git cherry-pick`
- `git branch -d`
- `git branch -D`
- `git reset`
- `git stash pop`
- Operasi lain yang mengubah riwayat atau kondisi repository.

Persetujuan harus jelas dan spesifik terhadap operasi yang akan dilakukan.

### 6.3 Operasi yang Dilarang Tanpa Instruksi Eksplisit

AI tidak boleh:

- Melakukan `git push --force`.
- Menghapus branch utama.
- Menghapus riwayat commit.
- Menggunakan `git reset --hard`.
- Menimpa perubahan lokal pengguna.
- Menghapus file yang belum di-commit.
- Mengubah remote repository tanpa persetujuan.
- Mengunggah file rahasia atau kredensial.

Jika diperlukan tindakan berisiko, jelaskan alasan, dampak, dan alternatifnya terlebih dahulu.

### 6.4 Tidak Mengklaim Operasi Berhasil Tanpa Bukti

AI hanya boleh menyatakan bahwa commit, push, merge, atau operasi Git lainnya berhasil jika perintah benar-benar dijalankan dan hasilnya telah diverifikasi.

Jika AI tidak memiliki akses terminal atau tidak dapat menjalankan Git, AI harus memberikan perintah kepada pengguna dan menjelaskan bahwa operasi belum dilakukan.

---

## 7. .gitignore

Project MyHabbit wajib memiliki file `.gitignore` untuk mencegah file yang tidak diperlukan atau bersifat lokal masuk ke repository.

File yang harus dikecualikan antara lain:

- File hasil build.
- Folder cache.
- File konfigurasi lokal Android Studio.
- File konfigurasi lokal SDK.
- File environment yang mengandung secret.
- File kredensial dan private key.
- File sementara sistem operasi.

Contoh pola yang perlu diperhatikan:

```gitignore
# Gradle
.gradle/
build/
**/build/

# Android
local.properties
captures/
.externalNativeBuild/
.cxx/

# Android Studio / IntelliJ
.idea/
*.iml

# Kotlin
.kotlin/

# OS
.DS_Store
Thumbs.db

# Environment and secrets
.env
.env.*
!.env.example
*.jks
*.keystore
secrets.properties

# Temporary files
*.tmp
*.log
```

Catatan:

1. Periksa kembali kebutuhan setiap pola sebelum menggunakannya.
2. Jangan mengabaikan seluruh file konfigurasi tanpa alasan.
3. File konfigurasi contoh yang tidak mengandung secret boleh disimpan.
4. Jangan pernah memasukkan password, token, OAuth client secret, atau private key ke Git.
5. Jika file rahasia pernah terlanjur diunggah, menghapus file dari commit terbaru saja tidak cukup. Secret tersebut harus segera dirotasi dan riwayat repository perlu ditangani.

---

## 8. Versioning

Gunakan Semantic Versioning untuk versi aplikasi.

Format:

`MAJOR.MINOR.PATCH`

Contoh:

- `0.1.0` — versi pengembangan awal.
- `0.2.0` — penambahan fitur.
- `0.2.1` — perbaikan bug.
- `1.0.0` — rilis stabil pertama.

Ketentuan:

- MAJOR berubah ketika terdapat perubahan besar yang tidak kompatibel.
- MINOR berubah ketika ada fitur baru yang kompatibel.
- PATCH berubah ketika ada perbaikan bug yang kompatibel.

Versi rilis dapat ditandai menggunakan Git tag.

Contoh:

```bash
git tag -a v0.1.0 -m "MyHabbit initial development release"
git push origin v0.1.0
```

Pembuatan dan pengiriman tag memerlukan persetujuan pengguna.

---

## 9. Dokumentasi dan Sinkronisasi Progress

Setiap fitur yang dikembangkan harus memiliki catatan progres.

AI wajib:

1. Memperbarui checklist pada PROMPT.md sesuai kondisi sebenarnya.
2. Memperbarui dokumentasi teknis jika ada perubahan arsitektur.
3. Mencatat file yang dibuat atau diubah.
4. Mencatat hasil build dan testing.
5. Mencatat branch yang digunakan.
6. Mencatat commit hash jika commit telah dilakukan.
7. Tidak menandai fitur selesai sebelum acceptance criteria terpenuhi.

Status fitur:

| Status | Arti |
|---|---|
| [ ] | Belum dikerjakan |
| [~] | Sedang dikerjakan |
| [x] | Selesai dan telah diverifikasi |
| [!] | Terhambat |
| [?] | Perlu pemeriksaan atau review |

---

## 10. Database dan Keamanan Data

MyHabbit menggunakan Room sebagai database lokal.

Karena itu, perubahan yang berkaitan dengan database harus dilakukan secara hati-hati.

Ketentuan:

- Jangan menghapus database pengguna untuk menyelesaikan bug.
- Jangan mengubah skema database tanpa memeriksa kebutuhan migrasi.
- Jangan menghapus migration yang sudah digunakan.
- Jangan menggunakan destructive migration tanpa persetujuan eksplisit.
- Pastikan perubahan database memiliki pengujian yang relevan.
- Jangan memasukkan database pengguna atau data pribadi ke repository.

Backup JSON dan database lokal tidak boleh berisi data pengguna sungguhan ketika digunakan sebagai file contoh di GitHub.

---

## 11. GitHub Pull Request

Pull Request digunakan untuk meninjau perubahan sebelum digabungkan ke `main`.

Informasi yang perlu disertakan:

- Judul fitur atau perbaikan.
- Deskripsi perubahan.
- Daftar file penting yang diubah.
- Hasil build.
- Hasil testing.
- Screenshot jika terdapat perubahan UI.
- Catatan risiko atau keterbatasan.

Pull Request harus diperiksa dan disetujui pengguna sebelum merge.

---

## 12. Aturan Ketika Melanjutkan Sesi Pengembangan

Pada setiap sesi baru, AI wajib:

1. Membaca seluruh dokumentasi utama.
2. Memeriksa branch aktif.
3. Memeriksa `git status`.
4. Memeriksa commit terakhir.
5. Memeriksa perubahan yang belum di-commit.
6. Memahami kondisi repository saat ini.
7. Memastikan pekerjaan sebelumnya tidak tertimpa.
8. Melaporkan kondisi repository sebelum mengerjakan fitur berikutnya.

AI tidak boleh berasumsi bahwa fitur sudah selesai hanya karena checklist menyatakan selesai. Kondisi kode harus diperiksa.

---

## 13. Format Laporan Git

Setiap kali menyelesaikan pekerjaan, AI wajib melaporkan:

### Git Status

- Branch aktif:
- Working tree:
- File berubah:
- File baru:
- File dihapus:

### Implementation

- Fitur:
- Hasil implementasi:
- Build:
- Testing:

### Git Operations

- Commit:
- Commit hash:
- Push:
- Pull Request:
- Merge:

### Next Step

- Pekerjaan berikutnya:
- Persetujuan yang dibutuhkan:

Jika suatu operasi belum dilakukan, tuliskan `Belum dilakukan`.

---

## 14. Prinsip Utama

Seluruh aktivitas Git pada project MyHabbit harus mengikuti prinsip berikut:

1. Jangan mengorbankan keamanan kode demi kecepatan.
2. Jangan menghilangkan perubahan lokal pengguna.
3. Jangan mengubah riwayat Git tanpa persetujuan.
4. Jangan melakukan commit atau push tanpa persetujuan.
5. Satu fitur dikembangkan secara terpisah.
6. Setiap perubahan harus dapat ditelusuri.
7. Setiap fitur harus diuji sebelum dianggap selesai.
8. `main` harus dijaga agar tetap stabil.
9. Dokumentasi harus selalu sesuai dengan kondisi project sebenarnya.
10. Pengguna tetap menjadi pihak yang mengambil keputusan akhir.

**PRINSIP PALING PENTING: AI BOLEH MEMERIKSA DAN MERENCANAKAN OPERASI GIT, TETAPI SETIAP PERUBAHAN RIWAYAT REPOSITORY, COMMIT, PUSH, MERGE, DAN OPERASI BERISIKO HARUS MENUNGGU PERSETUJUAN PENGGUNA.**