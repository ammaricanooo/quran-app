# Al-Qur'an Ku — Aplikasi Mobile Android (Kotlin + Jetpack Compose)

Aplikasi Al-Qur'an digital mobile native Android yang dibangun menggunakan **Kotlin**, **Jetpack Compose**, **Material 3**, **Firebase Auth & Firestore**, dan **Media3 ExoPlayer**. Seluruh styling, tema warna, font kaligrafi, animasi transisi, dan fitur islami telah disempurnakan **100% identik dan selengkap versi web Al-Qur'an Ku**.

---

## 🌟 Seluruh Fitur Lengkap Sesuai Versi Web

### 1. 📖 Al-Qur'an (114 Surah) & Terakhir Dibaca (`HomeScreen`)
- Hero Banner **Terakhir Dibaca (Last Read)** dengan tombol langsung lanjutkan bacaan dan sinkronisasi cloud.
- Pencarian cerdas nama surah, nomor surah, dan arti surah secara real-time.
- **Filter Animated Sliding Indicator Tabs** ("Semua", "Mekah", "Madinah") dengan animasi pergeseran yang halus dan fluid.
- Nomor surah berbingkai gradien biru & ungu khas Al-Qur'an Ku.
- Teks nama surah dalam tulisan Arab kaligrafi.

### 2. 📜 Detail Surah, Ayat & Tafsir Kemenag (`SurahDetailScreen`)
- **Tab Baca Ayat**: Teks ayat dengan font resmi **LPMQ Isep Misbah**, transliterasi latin, dan terjemahan bahasa Indonesia.
- **Tab Tafsir Kemenag**: Penjelasan dan tafsir lengkap per ayat dari Kementerian Agama RI (`equran.id/api/v2/tafsir`).
- **Pengaturan Ukuran Font**: Slider interaktif untuk memperbesar/memperkecil teks Arab sesuai kenyamanan mata (20sp - 38sp).
- **Murottal Full Surah & Audio Per Ayat**: Putar audio murottal langsung per ayat atau satu surah penuh.
- **Bookmark & Share**: Tandai ayat untuk disimpan ke cloud Firebase / lokal, dan bagikan teks ayat ke WhatsApp / media sosial.

### 3. 👤 Profil & Google Sign-In (`ProfilScreen`)
- **Autentikasi Firebase & Google Sign-In** sinkron dengan versi web.
- Ringkasan aktivitas tilawah, statistik hafalan, dan progres bacaan.
- Tab **Bookmark Kategori** (Semua, Surah, Ayat, Hadits, Doa, Asmaul Husna).
- Edit nama profil dan avatar profil otomatis dari akun Google.
- Progres hafalan 99 Asmaul Husna tersinkronisasi ke Firestore.

### 4. 🤲 Doa Rabithah (`RobithohScreen`)
- Rangkaian wirid doa penjalin hati dari Al-Ma'tsurat Sugro karya Imam Hasan Al-Banna.
- Teks Arab berharakat, transliterasi Latin, arti Indonesia, dan penjelasan faedah.
- Counter pengulangan zikir interaktif dan tombol reset.

### 5. 🎙️ Materi Kultum 7 Menit (`KultumScreen`)
- Kumpulan materi tausiyah terstruktur lengkap: Muqaddimah Arab, Pembukaan, Poin Inti, Dalil Al-Qur'an, Dalil Hadits Shahih, Kesimpulan, dan Doa Penutup Majelis.
- Filter kategori: Ramadhan & Puasa, Sholat & Ibadah, Akhlak & Adab, Sabar & Syukur, Sedekah & Rezeki, dll.
- Fitur pencarian tema dan tombol bagikan naskah ceramah ke media sosial.

### 6. 📰 Artikel Islami (`ArtikelScreen`)
- Kajian Islam terpercaya: Tafsir Al-Qur'an, Syariah & Fiqih, Tasawuf, Kisah Hikmah, dan Sirah Nabawiyah.
- Desain visual modern dengan banner gambar, chip kategori, tanggal rilis, dan pembaca artikel interaktif.

### 7. 📖 Kumpulan Kitab Maulid Nabi ﷺ (`MaulidScreen`)
- Kitab-kitab maulid populer: Maulid Diba'i, Maulid Barzanji, Maulid Simtuddurar, Maulid Burdah, dan Dhiya'ul Lami'.
- Navigasi bertingkat: Pilih Kitab → Pilih Bab/Fasal → Bacaan bait-bait syair pujian lengkap dengan terjemahan.

### 8. ⏰ Jadwal Sholat 5 Waktu & Waktu Imsakiyah (`JadwalScreen`)
- Waktu sholat harian lengkap: **Imsak, Subuh, Terbit, Dhuha, Dzuhur, Ashar, Maghrib, Isya**.
- Pilihan kota besar di seluruh Indonesia.
- Countdown waktu sholat berikutnya secara dinamis.

### 9. 🎧 Murottal 30 Juz Audio Player (`MurottalScreen`)
- Pilihan 5 Qari ternama dunia:
  - **Misyari Rasyid Al-'Afasy**
  - **Abdullah Al-Juhany**
  - **Abdul Muhsin Al-Qasim**
  - **Abdurrahman As-Sudais**
  - **Ibrahim Al-Dossari**
- Kontrol pemutar lengkap: Play, Pause, Ganti Surah, dan Floating Mini-Player di bawah layar.

### 10. 📑 Daftar 30 Juz (`JuzScreen`)
- Pembagian 30 Juz lengkap dengan surah awal, surah akhir, rentang ayat, dan potongan ayat pembuka.

### 11. ✨ 99 Asmaul Husna (`AsmaulHusnaScreen`)
- Desain **Checkerboard Alternating Gradient** (selang-seling Biru Neon `#1089FF` dan Ungu `#7B1FA2`) identik dengan web.
- Teks Arab, nama Latin, dan arti Bahasa Indonesia dengan tombol tandai hafalan.

### 12. 🤲 Kumpulan Doa Harian (`DoaScreen`)
- Ratusan doa harian dari API `equran.id` lengkap dengan teks Arab, Latin, dan terjemahan.

### 13. 📿 Dzikir Pagi & Petang + Tasbih Digital (`DzikirScreen`)
- Tab switch **Dzikir Pagi** dan **Dzikir Petang** sesuai sunnah.
- Counter Tasbih Digital Interaktif dengan getaran haptik dan centang hijau saat tuntas.

### 14. 📜 Tahlil & Yasin Lengkap (`TahlilScreen`)
- Susunan bacaan tahlil dan doa arwah lengkap dari Niyyat, Al-Fatihah, Surah Pendek, Tahlil inti, hingga Doa Penutup.

### 15. 📚 Hadits Arbain Nawawi (`HaditsScreen`)
- 42 Hadits pokok ajaran Islam karya Imam An-Nawawi lengkap dengan teks Arab dan terjemahan.

### 16. 🎮 Kuis Wawasan Al-Qur'an (`KuisScreen`)
- Kuis pilihan ganda edukatif seputar Al-Qur'an dan wawasan Islam.

### 17. 🧭 Arah Kiblat
- Terintegrasi langsung dengan Qibla Finder resmi Google.

### 18. ⚙️ Pengaturan Tampilan (`SettingsBottomSheet`)
- Kustomisasi ukuran huruf Arab, ukuran terjemahan, serta toggle tampilkan Latin dan Terjemahan dengan pratinjau live.

---

## 🚀 Cara Build Menjadi `.apk` via GitHub Actions

Workflow GitHub Actions sudah terpasang di:
`.github/workflows/build-apk.yml`

### Langkah-langkah Push & Build Otomatis:
1. Jalankan perintah berikut di folder `quran-android`:
   ```bash
   cd "c:\Champion Ambassador Project Fase 2\Ammar\Project\Web\quran\quran-android"
   git add .
   git commit -m "feat: complete android app upgrade with all web features, animations and firebase login"
   git push origin main
   ```
2. Buka repository Anda di GitHub, lalu buka tab **Actions**.
3. Workflow **Build Quran Android APK** akan berjalan secara otomatis.
4. Tunggu workflow selesai (sekitar 2–3 menit hingga centang hijau ✅).
5. Klik pada riwayat run tersebut, scroll ke bawah ke bagian **Artifacts**, lalu unduh **`QuranKu-Android-Debug-APK`**.
6. Ekstrak zip dan pasang file `.apk` di HP Android Anda.
