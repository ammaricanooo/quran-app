package com.ammaricano.quran.ui.screens.kultum

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ammaricano.quran.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class KultumUiState(
    val categories: List<String> = listOf(
        "Semua",
        "Ramadhan & Puasa",
        "Sholat & Ibadah",
        "Akhlak & Adab",
        "Sabar & Syukur",
        "Sedekah & Rezeki",
        "Muhasabah & Kematian",
        "Keluarga & Silaturahmi"
    ),
    val selectedCategory: String = "Semua",
    val searchQuery: String = "",
    val items: List<KultumItem> = emptyList(),
    val selectedKultum: KultumItem? = null
)

class KultumViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(KultumUiState())
    val uiState: StateFlow<KultumUiState> = _uiState.asStateFlow()

    private val allKultum = listOf(
        KultumItem(
            id = "meraih-derajat-takwa-di-bulan-ramadhan",
            title = "Meraih Derajat Takwa di Bulan Suci Ramadhan",
            category = "Ramadhan & Puasa",
            duration = "5 - 7 Menit",
            summary = "Puasa bukan sekadar menahan lapar dan dahaga, melainkan madrasah ruhiyah untuk menggembleng diri mencapai derajat muttaqin.",
            muqaddimah = KultumMuqaddimah(
                arab = "الْحَمْدُ لِلَّهِ الَّذِي هَدَانَا لِهَٰذَا وَمَا كُنَّا لِنَهْتَدِيَ لَوْلَا أَنْ هَدَانَا اللَّهُ، وَالصَّلَاةُ وَالسَّلَامُ عَلَى نَبِيِّنَا مُحَمَّدٍ وَعَلَى آلِهِ وَصَحْبِهِ أَجْمَعِينَ. أَمَّا بَعْدُ.",
                latin = "Alhamdulillahi-lladzi hadana lihadza wama kunna linahtadiya lawla an hadanallah, wash-shalatu was-salamu 'ala nabiyyina Muhammadin wa 'ala alihi wa shahbihi ajma'in. Amma ba'du.",
                arti = "Segala puji bagi Allah yang telah menunjukkan kami kepada kebaikan ini. Shalawat dan salam semoga tercurah kepada Nabi kita Muhammad, segenap keluarga dan sahabatnya."
            ),
            pembukaan = "Kaum muslimin dan muslimat jamaah yang dirahmati Allah, marilah kita senantiasa memanjatkan puji syukur ke hadirat Allah SWT atas nikmat iman, Islam, dan kesempatan menikmati bulan suci Ramadhan.",
            points = listOf(
                KultumPoint(
                    heading = "1. Tujuan Utama Pensyariatan Puasa",
                    body = "Allah SWT tidak membutuhkan rasa lapar dan haus kita semata. Tujuan hakiki puasa difirmankan secara gamblang di akhir Surat Al-Baqarah ayat 183: la'allakum tattaquun (agar kalian bertakwa)."
                ),
                KultumPoint(
                    heading = "2. Nilai Muraqabatullah dalam Ibadah Puasa",
                    body = "Ibadah puasa adalah rahasia antara seorang hamba dengan Tuhannya. Di tempat yang sunyi tanpa ada manusia lain yang melihat, kita tetap tidak makan dan minum karena meyakini Allah Maha Melihat."
                ),
                KultumPoint(
                    heading = "3. Melatih Pengendalian Hawa Nafsu",
                    body = "Selama berpuasa, kita dilatih menahan hal-hal yang asalnya halal pada siang hari. Jika terhadap yang halal saja kita mampu menahan diri, semestinya kita jauh lebih mampu menahan diri dari yang haram selamanya."
                )
            ),
            dalilQuran = KultumDalilQuran(
                ayatArab = "يٰٓاَيُّهَا الَّذِيْنَ اٰمَنُوْا كُتِبَ عَلَيْكُمُ الصِّيَامُ كَمَا كُتِبَ عَلَى الَّذِيْنَ مِنْ قَبْلِكُمْ لَعَلَّكُمْ تَتَّقُوْنَۙ",
                latin = "Yā ayyuhalladzīna āmanū kutiba 'alaikumuṣ-ṣiyāmu kamā kutiba 'alallażīna min qablikum la'allakum tattaqūn.",
                arti = "Wahai orang-orang yang beriman! Diwajibkan atas kamu berpuasa sebagaimana diwajibkan atas orang sebelum kamu agar kamu bertakwa.",
                surah = "Al-Baqarah",
                nomorAyat = "183"
            ),
            dalilHadits = KultumDalilHadits(
                haditsArab = "رُبَّ صَائِمٍ لَيْسَ لَهُ مِنْ صِيَامِهِ إِلَّا الْجُوعُ ، وَرُبَّ قَائِمٍ لَيْسَ لَهُ مِنْ قِيَامِهِ إِلَّا السَّهَرُ",
                arti = "Betapa banyak orang yang berpuasa tidak mendapatkan apapun dari puasanya kecuali rasa lapar, dan betapa banyak orang shalat malam tidak mendapatkan apapun kecuali begadang semata.",
                perawi = "HR. Ibnu Majah no. 1690"
            ),
            kesimpulan = listOf(
                "Tolak ukur keberhasilan puasa adalah meningkatnya ketakwaan dan kepekaan hati setelah Ramadhan.",
                "Jaga lisan, pandangan, dan perbuatan dari hal sia-sia agar pahala puasa tidak gugur.",
                "Jadikan Ramadhan sebagai madrasah pembentuk kepribadian muslim sejati."
            ),
            doaPenutup = KultumDoaPenutup(
                arab = "اللَّهُمَّ أَعِنَّا عَلَى صِيَامِهِ وَقِيَامِهِ وَتِلَاوَةِ كِتَابِكَ، وَاجْعَلْنَا فِيهِ مِنَ الْمَقْبُولِينَ. رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ.",
                latin = "Allahumma a'inna 'ala shiyamihi wa qiyamihi wa tilawati kitabika, waj'alna fiihi minal maqbuuliin. Rabbana aatina fid-dunya hasanah wa fil aakhirati hasanah wa qina 'adzaban-naar.",
                arti = "Ya Allah, bantulah kami untuk berpuasa, shalat malam, dan membaca kitab-Mu. Jadikanlah kami termasuk orang-orang yang diterima amalnya. Berikanlah kami kebaikan dunia dan akhirat."
            )
        ),
        KultumItem(
            id = "keutamaan-sholat-berjamaah-tepat-waktu",
            title = "Keutamaan Sholat Berjamaah Tepat Waktu",
            category = "Sholat & Ibadah",
            duration = "6 Menit",
            summary = "Menjaga sholat fardhu di awal waktu secara berjamaah di masjid memiliki keutamaan 27 derajat dan perlindungan dari sifat munafik.",
            muqaddimah = KultumMuqaddimah(
                arab = "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ، وَالصَّلَاةُ وَالسَّلَامُ عَلَى أَشْرَفِ الأَنْبِيَاءِ وَالْمُرْسَلِينَ نَبِيِّنَا مُحَمَّدٍ وَعَلَى آلِهِ وَصَحْبِهِ أَجْمَعِينَ. أَمَّا بَعْدُ.",
                latin = "Alhamdulillahi Rabbil 'alamin, wash-shalatu was-salamu 'ala asyrafil anbiya'i wal mursalin, nabiyyina Muhammadin wa 'ala alihi wa shahbihi ajma'in. Amma ba'du.",
                arti = "Segala puji bagi Allah Rabb semesta alam, shalawat dan salam atas semulia-mulia Nabi dan Rasul, Nabi Muhammad SAW serta seluruh keluarga dan sahabatnya."
            ),
            pembukaan = "Sholat adalah tiang agama. Barang siapa mendirikannya maka ia telah menegakkan agama, dan barang siapa meninggalkannya maka ia meruntuhkan agama.",
            points = listOf(
                KultumPoint(
                    heading = "1. Amalan Paling Dicintai Allah",
                    body = "Saat Rasulullah ﷺ ditanya amalan apa yang paling dicintai Allah, beliau menjawab: 'Sholat pada waktunya.' Ini menunjukkan prioritas utama setiap muslim."
                ),
                KultumPoint(
                    heading = "2. Lipatan Pahala 27 Derajat",
                    body = "Sholat berjamaah melipatgandakan pahala hingga dua puluh tujuh kali lipat dibandingkan sholat sendirian di rumah."
                ),
                KultumPoint(
                    heading = "3. Membangun Ukhuwah dan Disiplin",
                    body = "Berdiri sejajar dalam satu shaf menghapus sekat status sosial dan menumbuhkan rasa persaudaraan serta kedisiplinan yang tinggi."
                )
            ),
            dalilQuran = KultumDalilQuran(
                ayatArab = "حَافِظُوْا عَلَى الصَّلَوٰتِ وَالصَّلٰوةِ الْوُسْطٰى وَقُوْمُوْا لِلّٰهِ قٰنِتِيْنَ",
                latin = "Ḥāfiẓū 'alaṣ-ṣalawāti waṣ-ṣalātil-wusṭā wa qūmū lillāhi qānitīn.",
                arti = "Peliharalah semua shalat dan shalat wustha (Ashar). Dan berdirilah untuk Allah (dalam shalatmu) dengan khusyuk.",
                surah = "Al-Baqarah",
                nomorAyat = "238"
            ),
            dalilHadits = KultumDalilHadits(
                haditsArab = "صَلاَةُ الْجَمَاعَةِ أَفْضَلُ مِنْ صَلاَةِ الْفَذِّ بِسَبْعٍ وَعِشْرِينَ دَرَجَةً",
                arti = "Sholat berjamaah lebih utama daripada sholat sendirian dengan dua puluh tujuh derajat.",
                perawi = "HR. Bukhari no. 645 & Muslim no. 650"
            ),
            kesimpulan = listOf(
                "Jadikan panggilan adzan sebagai pengingat untuk segera melangkah ke masjid.",
                "Sholat berjamaah adalah sarana pelebur dosa dan peninggi derajat kemuliaan di sisi Allah."
            ),
            doaPenutup = KultumDoaPenutup(
                arab = "رَبِّ اجْعَلْنِيْ مُقِيْمَ الصَّلٰوةِ وَمِنْ ذُرِّيَّتِيْ رَبَّنَا وَتَقَبَّلْ دُعَآءِ.",
                latin = "Rabbij'alni muqiimaṣ-ṣalāti wa min żurriyyati rabbanā wa taqabbal du'ā'.",
                arti = "Ya Tuhanku, jadikanlah aku dan anak cucuku orang yang tetap melaksanakan shalat, ya Tuhan kami, perkenankanlah doaku."
            )
        ),
        KultumItem(
            id = "kekuatan-sabar-dan-syukur",
            title = "Kekuatan Sabar dan Syukur dalam Kehidupan",
            category = "Sabar & Syukur",
            duration = "5 Menit",
            summary = "Dua sayap keimanan seorang mukmin adalah sabar saat diuji dan bersyukur saat diberi nikmat.",
            muqaddimah = KultumMuqaddimah(
                arab = "الْحَمْدُ لِلَّهِ الشَّكُورِ الصَّبُورِ، الَّذِي جَعَلَ الصَّبْرَ ضِيَاءً، وَالشُّكْرَ نَمَاءً، وَالصَّلَاةُ وَالسَّلَامُ عَلَى خَيْرِ مَنْ صَبَرَ وَشَكَرَ، سَيِّدِنَا مُحَمَّدٍ وَعَلَى آلِهِ وَصَحْبِهِ. أَمَّا بَعْدُ.",
                latin = "Alhamdulillahi Asy-Syakuur Ash-Shabuur, alladzi ja'alash-shabra dhiyaa-an, wasy-syukra namaa-an, wash-shalatu was-salamu 'ala khayri man shabara wa syakar. Amma ba'du.",
                arti = "Segala puji bagi Allah Yang Maha Mensyukuri lagi Maha Penyabar. Shalawat dan salam atas sebaik-baik hamba yang bersabar dan bersyukur, Nabi Muhammad SAW."
            ),
            pembukaan = "Hidup manusia berputar di antara dua keadaan: kelapangan nikmat atau kesempitan ujian. Mukmin yang cerdas adalah yang mampu menyikapinya dengan sabar dan syukur.",
            points = listOf(
                KultumPoint(
                    heading = "1. Sabar Saat Tertimpa Musibah",
                    body = "Sabar bukan berarti pasrah tanpa ikhtiar, melainkan menahan lisan dari mengeluh, menahan hati dari berprasangka buruk kepada takdir Allah."
                ),
                KultumPoint(
                    heading = "2. Syukur Membuka Pintu Tambahan Nikmat",
                    body = "Nikmat yang disyukuri akan bertambah dan berkah. Syukur dilakukan dengan lisan memuji Allah, hati mengakui karunia-Nya, dan anggota badan beramal saleh."
                )
            ),
            dalilQuran = KultumDalilQuran(
                ayatArab = "وَإِذْ تَأَذَّنَ رَبُّكُمْ لَئِنْ شَكَرْتُمْ لَأَزِيدَنَّكُمْ ۖ وَلَئِنْ كَفَرْتُمْ إِنَّ عَذَابِي لَشَدِيدٌ",
                latin = "Wa iz ta'ażżana rabbukum la'in syakartum la'azīdannakum wa la'in kafartum inna 'ażābī lasyadīd.",
                arti = "Dan (ingatlah) ketika Tuhanmu memaklumkan: 'Sesungguhnya jika kamu bersyukur, niscaya Aku akan menambah (nikmat) kepadamu, tetapi jika kamu mengingkari (nikmat-Ku), maka pasti azab-Ku sangat berat.'",
                surah = "Ibrahim",
                nomorAyat = "7"
            ),
            dalilHadits = KultumDalilHadits(
                haditsArab = "عَجَبًا لأَمْرِ الْمُؤْمِنِ إِنَّ أَمْرَهُ كُلَّهُ خَيْرٌ ... إِنْ أَصَابَتْهُ سَرَّاءُ شَكَرَ فَكَانَ خَيْرًا لَهُ ، وَإِنْ أَصَابَتْهُ ضَرَّاءُ صَبَرَ فَكَانَ خَيْرًا لَهُ",
                arti = "Sungguh menakjubkan urusan seorang mukmin, sesungguhnya semua perkaranya adalah baik... Jika ia mendapat kesenangan ia bersyukur, maka itu baik baginya. Dan jika ditimpa kesulitan ia bersabar, maka itu pun baik baginya.",
                perawi = "HR. Muslim no. 2999"
            ),
            kesimpulan = listOf(
                "Sabar dan syukur adalah kunci kedamaian hati yang tidak tergoyahkan oleh pasang surut dunia.",
                "Tidak ada kerugian bagi seorang mukmin selama ia memegang teguh kedua prinsip ini."
            ),
            doaPenutup = KultumDoaPenutup(
                arab = "رَبِّ أَوْزِعْنِي أَنْ أَشْكُرَ نِعْمَتَكَ الَّتِي أَنْعَمْتَ عَلَيَّ وَعَلَىٰ وَالِدَيَّ وَأَنْ أَعْمَلَ صَالِحًا تَرْضَاهُ وَأَدْخِلْنِي بِرَحْمَتِكَ فِي عِبَادِكَ الصَّالِحِينَ.",
                latin = "Rabbi awzi'nii an asykura ni'matakal-latii an'amta 'alayya wa 'ala waalidayya wa an a'mala shaalihan tardhaahu wa adkhilnii birahmatika fii 'ibaadikas-shaalihiin.",
                arti = "Ya Tuhanku, anugerahkanlah kepadaku ilham untuk tetap mensyukuri nikmat-Mu yang telah Engkau anugerahkan kepadaku dan kepada kedua orang tuaku dan agar aku mengerjakan kebajikan yang Engkau ridhai."
            )
        ),
        KultumItem(
            id = "keutamaan-sedekah-pembuka-rezeki",
            title = "Sedekah: Pembersih Jiwa dan Pembuka Pintu Rezeki",
            category = "Sedekah & Rezeki",
            duration = "5 Menit",
            summary = "Sedekah tidak pernah mengurangi harta, justru melipatgandakan keberkahan, menolak bala, dan memadamkan murka Allah.",
            muqaddimah = KultumMuqaddimah(
                arab = "الْحَمْدُ لِلَّهِ الْوَاسِعِ الْكَرِيمِ، الَّذِي يُضَاعِفُ لِمَنْ يَشَاءُ وَهُوَ الْعَلِيمُ الْحَكِيمُ. وَالصَّلَاةُ وَالسَّلَامُ عَلَى نَبِيِّ الرَّحْمَةِ وَالْجُودِ مُحَمَّدٍ وَعَلَى آلِهِ وَصَحْبِهِ أَجْمَعِينَ.",
                latin = "Alhamdulillahi-l waasi'il kariim, alladzi yudhaa'ifu liman yasyaa-u wahuwal 'aliimul hakiim. Wash-shalatu was-salamu 'ala nabiyyir-rahmati wal juudi Muhammadin wa 'ala alihi wa shahbihi ajma'in.",
                arti = "Segala puji bagi Allah Yang Maha Luas karunia-Nya lagi Maha Mulia. Shalawat dan salam atas Nabi penebar rahmat dan kedermawanan, Nabi Muhammad SAW."
            ),
            pembukaan = "Islam mengajarkan bahwa rezeki yang hakiki bukanlah apa yang kita simpan dan habiskan sendiri, melainkan apa yang kita nafkahkan di jalan Allah SWT.",
            points = listOf(
                KultumPoint(
                    heading = "1. Sedekah Tidak Mengurangi Harta",
                    body = "Nabi ﷺ bersumpah bahwa sedekah tidak akan mengurangi harta. Meskipun secara hitungan matematis berkurang, Allah menggantinya dengan keberkahan yang berlipat ganda."
                ),
                KultumPoint(
                    heading = "2. Menolak Marabahaya dan Penyakit",
                    body = "Obatilah orang sakit di antara kalian dengan sedekah. Sedekah memiliki daya tolak terhadap bala bencana dan penyakit hati maupun fisik."
                )
            ),
            dalilQuran = KultumDalilQuran(
                ayatArab = "مَثَلُ الَّذِيْنَ يُنْفِقُوْنَ اَمْوَالَهُمْ فِيْ سَبِيْلِ اللّٰهِ كَمَثَلِ حَبَّةٍ اَنْۢبَتَتْ سَبْعَ سَنَابِلَ فِيْ كُلِّ سُنْۢبُلَةٍ مِّائَةُ حَبَّةٍ ۗ وَاللّٰهُ يُضٰعِفُ لِمَنْ يَّشَآءُ",
                latin = "Maṡalullażīna yunfiqūna amwālahum fī sabīlillāhi kamaṡali ḥabbatin ambatat sab'a sanābila fī kulli sumbulatim mi'atu ḥabbah, wallāhu yuḍā'ifu limay yasyā'.",
                arti = "Perumpamaan orang yang menginfakkan hartanya di jalan Allah seperti sebutir biji yang menumbuhkan tujuh tangkai, pada setiap tangkai ada seratus biji. Allah melipatgandakan bagi siapa yang Dia kehendaki.",
                surah = "Al-Baqarah",
                nomorAyat = "261"
            ),
            dalilHadits = KultumDalilHadits(
                haditsArab = "مَا نَقَصَتْ صَدَقَةٌ مِنْ مَالٍ ، وَمَا زَادَ اللَّهُ عَبْدًا بِعَفْوٍ إِلَّا عِزًّا",
                arti = "Sedekah itu tidak akan mengurangi harta, dan tidaklah Allah menambah bagi seorang hamba karena sifat pemaafnya melainkan kemuliaan.",
                perawi = "HR. Muslim no. 2588"
            ),
            kesimpulan = listOf(
                "Jangan tunda bersedekah menunggu lapang, bersedekahlah di kala lapang maupun sempit.",
                "Ikhlaskan niat semata mencari ridha Allah tanpa mengharap pujian manusia."
            ),
            doaPenutup = KultumDoaPenutup(
                arab = "اللَّهُمَّ أَعْطِ مُنْفِقًا خَلَفًا، وَأَعْطِ مُمْسِكًا تَلَفًا. رَبَّنَا تَقَبَّلْ مِنَّا إِنَّكَ أَنْتَ السَّمِيعُ الْعَلِيمُ.",
                latin = "Allahumma a'thi munfiqan khalafan, wa a'thi mumsikan talafan. Rabbana taqabbal minna innaka Antas-Samii'ul 'Aliim.",
                arti = "Ya Allah, berikanlah ganti bagi orang yang berinfak, dan berikanlah kehancuran bagi orang yang menahan hartanya. Ya Tuhan kami, terimalah amal kami, sesungguhnya Engkau Maha Mendengar lagi Maha Mengetahui."
            )
        )
    )

    init {
        filterItems()
    }

    fun setCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        filterItems()
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        filterItems()
    }

    fun selectKultum(kultum: KultumItem?) {
        _uiState.value = _uiState.value.copy(selectedKultum = kultum)
    }

    private fun filterItems() {
        val cat = _uiState.value.selectedCategory
        val query = _uiState.value.searchQuery.trim().lowercase()

        val filtered = allKultum.filter { item ->
            val matchCat = cat == "Semua" || item.category.equals(cat, ignoreCase = true)
            val matchQuery = query.isEmpty() ||
                item.title.lowercase().contains(query) ||
                item.summary.lowercase().contains(query) ||
                item.category.lowercase().contains(query)
            matchCat && matchQuery
        }

        _uiState.value = _uiState.value.copy(items = filtered)
    }
}
