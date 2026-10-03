# Al-Qur'an Ku — Aplikasi Mobile Android (Kotlin + Jetpack Compose)

Aplikasi Al-Qur'an digital mobile native Android yang dibangun menggunakan **Kotlin**, **Jetpack Compose**, **Material 3**, dan **Media3 ExoPlayer**. Seluruh styling, tema warna, font, dan fitur islami telah disempurnakan **100% identik dengan versi web Al-Qur'an Ku**.

---

## 🌟 Seluruh Fitur Lengkap Sesuai Versi Web

### 1. 📖 Al-Qur'an (114 Surah) & Terakhir Dibaca (`HomeScreen`)
- Hero Banner **Terakhir Dibaca (Last Read)** dengan tombol langsung lanjutkan bacaan.
- Pencarian cerdas nama surah, nomor surah, dan arti surah secara real-time.
- Filter Makkiyah & Madaniyah.
- Nomor surah berbingkai gradien biru & ungu khas Al-Qur'an Ku.
- Teks nama surah dalam tulisan Arab kaligrafi.

### 2. 📜 Detail Surah, Ayat & Tafsir Kemenag (`SurahDetailScreen`)
- **Tab Baca Ayat**: Teks ayat dengan font resmi **LPMQ Isep Misbah**, transliterasi latin, dan terjemahan bahasa Indonesia.
- **Tab Tafsir Kemenag**: Penjelasan dan tafsir lengkap per ayat dari Kementerian Agama RI (`equran.id/api/v2/tafsir`).
- **Pengaturan Ukuran Font**: Slider interaktif untuk memperbesar/memperkecil teks Arab sesuai kenyamanan mata (20sp - 38sp).
- **Murottal Full Surah & Audio Per Ayat**: Putar audio murottal langsung per ayat atau satu surah penuh.
- **Bookmark & Share**: Tandai ayat untuk disimpan ke daftar bookmark pribadi, dan bagikan teks ayat ke WhatsApp / media sosial.

### 3. ⏰ Jadwal Sholat 5 Waktu & Waktu Imsakiyah (`JadwalScreen`)
- Waktu sholat harian lengkap: **Imsak, Subuh, Terbit, Dhuha, Dzuhur, Ashar, Maghrib, Isya**.
- Pilihan kota besar di Indonesia (Jakarta, Surabaya, Bandung, Medan, Semarang, Makassar, Yogyakarta, dll.).
- Terintegrasi langsung dengan API Sholat `equran.id`.

### 4. 🎧 Murottal 30 Juz Audio Player (`MurottalScreen`)
- Pilihan 5 Qari ternama dunia:
  - **Misyari Rasyid Al-'Afasy**
  - **Abdullah Al-Juhany**
  - **Abdul Muhsin Al-Qasim**
  - **Abdurrahman As-Sudais**
  - **Ibrahim Al-Dossari**
- Kontrol pemutar lengkap: Play, Pause, Ganti Surah, dan Floating Mini-Player di bawah layar.

### 5. 📑 Daftar 30 Juz (`JuzScreen`)
- Pembagian 30 Juz lengkap dengan surah awal, surah akhir, rentang ayat, dan potongan ayat pembuka.
- Klik langsung membuka surah terkait.

### 6. ✨ 99 Asmaul Husna (`AsmaulHusnaScreen`)
- Desain **Checkerboard Alternating Gradient** (selang-seling Biru Neon `#1089FF` dan Ungu `#7B1FA2`) identik dengan web.
- Teks Arab, nama Latin, dan arti Bahasa Indonesia.
- Pencarian instan Asmaul Husna.

### 7. 🤲 Kumpulan Doa Harian (`DoaScreen`)
- Ratusan doa harian dari API `equran.id`.
- Kartu interaktif yang dapat di-*expand* untuk melihat tulisan Arab, transliterasi Latin, dan terjemahan.
- Fitur bagikan doa.

### 8. 📿 Dzikir Pagi & Petang + Tasbih Digital (`DzikirScreen`)
- Tab switch **Dzikir Pagi** dan **Dzikir Petang** sesuai sunnah.
- **Counter Tasbih Digital Interaktif**: Tombol hitung pengulangan (1x, 3x, dll.) dengan indikator centang hijau saat tuntas.

### 9. 📜 Tahlil & Yasin Lengkap (`TahlilScreen`)
- Susunan bacaan tahlil dan doa arwah lengkap dari Niyyat, Al-Fatihah, Surah Pendek, Tahlil inti, hingga Doa Penutup.

### 10. 📚 Hadits Arbain Nawawi (`HaditsScreen`)
- 42 Hadits pokok ajaran Islam karya Imam An-Nawawi lengkap dengan teks Arab dan terjemahan.
- Pencarian hadits berdasarkan nomor atau topik.

### 11. 🎮 Kuis Wawasan Al-Qur'an (`KuisScreen`)
- Kuis pilihan ganda edukatif seputar Al-Qur'an dan wawasan Islam.
- Indikator benar (hijau) & salah (merah) secara langsung disertai penjelasan detail.
- Perhitungan skor akhir.

### 12. 🔖 Bookmark & Riwayat Bacaan (`BookmarkScreen`)
- Halaman khusus daftar ayat-ayat yang telah dibookmark.
- Sekali klik langsung melompat ke surah dan ayat yang ditandai.

---

## 🚀 Cara Build Menjadi `.apk` via GitHub Actions

Workflow GitHub Actions sudah terpasang di:
`.github/workflows/build-apk.yml`

### Langkah-langkah:
1. Buat repository baru di [github.com/new](https://github.com/new) (misal: `quran-mobile-app`).
2. Jalankan perintah berikut di folder `quran-android`:
   ```bash
   cd "c:\Champion Ambassador Project Fase 2\Ammar\Project\Web\quran\quran-android"
   git init
   git add .
   git commit -m "feat: complete quran mobile app matching web version"
   git branch -M main
   git remote add origin https://github.com/USERNAME/quran-mobile-app.git
   git push -u origin main
   ```
3. Buka repository di browser, lalu buka tab **Actions**.
4. Tunggu workflow selesai (sekitar 2–3 menit hingga centang hijau).
5. Klik nama run tersebut, scroll ke bawah ke bagian **Artifacts**, lalu download **`QuranKu-Android-Debug-APK`**.
6. Ekstrak zip dan pasang file `.apk` di HP Android Anda.
