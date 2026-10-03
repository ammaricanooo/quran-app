package com.ammaricano.quran.ui.screens.artikel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ammaricano.quran.data.model.ArticleCategory
import com.ammaricano.quran.data.model.ArticleImage
import com.ammaricano.quran.data.model.ArticleItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class DetailedArticle(
    val id: Int,
    val title: String,
    val slug: String,
    val category: String,
    val imageUrl: String?,
    val publishedAt: String,
    val content: String
)

data class ArtikelUiState(
    val categories: List<Pair<Int, String>> = listOf(
        0 to "Semua Kajian",
        78 to "Tafsir Al-Qur'an",
        11 to "Syariah & Fiqih",
        75 to "Tasawuf & Akhlak",
        51 to "Hikmah & Kisah",
        89 to "Sirah Nabawiyah",
        9 to "Khutbah & Ceramah",
        85 to "Ramadhan & Puasa",
        72 to "Ilmu Hadits"
    ),
    val selectedCategoryId: Int = 0,
    val searchQuery: String = "",
    val items: List<DetailedArticle> = emptyList(),
    val isLoading: Boolean = false,
    val selectedArticle: DetailedArticle? = null
)

class ArtikelViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(ArtikelUiState())
    val uiState: StateFlow<ArtikelUiState> = _uiState.asStateFlow()

    private val fallbackArticles = listOf(
        DetailedArticle(
            id = 90001,
            title = "Tafsir Surat Al-Insyirah: Janji Kemudahan di Balik Setiap Kesulitan",
            slug = "tafsir-surat-al-insyirah-janji-kemudahan",
            category = "Tafsir Al-Qur'an",
            imageUrl = "https://images.unsplash.com/photo-1609599006353-e629aaabfeae?q=80&w=800&auto=format&fit=crop",
            publishedAt = "18 Sep 2026",
            content = "Surat Al-Insyirah (Asy-Syarh) diturunkan di Makkah pada masa-masa awal dakwah Islam ketika Rasulullah SAW dan para sahabat menghadapi tekanan berat dari kaum Quraisy.\n\n" +
                "1. Kelapangan Dada sebagai Modal Utama\n" +
                "Allah SWT membuka surat ini dengan pertanyaan retoris: 'Bukankah Kami telah melapangkan dadamu (Muhammad)?' (QS. Al-Insyirah: 1). Para ulama menjelaskan bahwa kelapangan dada di sini mencakup pembersihan hati secara fisik dan maknawi berupa cahaya hikmah serta ketenangan bathin.\n\n" +
                "2. Dua Kemudahan Menemani Satu Kesulitan\n" +
                "Dalam ayat kelima dan keenam difirmankan: 'Maka sesungguhnya bersama kesulitan ada kemudahan. Sesungguhnya bersama kesulitan ada kemudahan.' Kata Al-'Usr (kesulitan) berbentuk makrifah (tunggal), sedangkan Yusran (kemudahan) berbentuk nakirah dan berulang. Ibnu Abbas RA berkata: 'Satu kesulitan tidak akan pernah mampu mengalahkan dua kemudahan.'\n\n" +
                "3. Berpindah dari Satu Kebaikan ke Kebaikan Lain\n" +
                "Surat ini ditutup dengan perintah: 'Maka apabila engkau telah selesai (dari suatu urusan), tetaplah bekerja keras (untuk urusan yang lain).' Islam mengajarkan agar waktu senantiasa diisi dengan amal kebajikan tanpa berputus asa."
        ),
        DetailedArticle(
            id = 90002,
            title = "Hukum Shalat Jamak dan Qashar: Tuntunan Syariat bagi Musafir",
            slug = "hukum-shalat-jamak-dan-qashar-musafir",
            category = "Syariah & Fiqih",
            imageUrl = "https://images.unsplash.com/photo-1542816417-0983c9c9ad53?q=80&w=800&auto=format&fit=crop",
            publishedAt = "12 Sep 2026",
            content = "Sholat jamak dan qashar merupakan rukhshah (keringanan) dari Allah bagi hamba-Nya yang sedang bepergian jauh.\n\n" +
                "1. Syarat Diperbolehkannya Qashar\n" +
                "Jarak perjalanan sekurang-kurangnya mencapai 2 marhalah (sekitar 81-88 km), tujuan perjalanan bukan untuk maksiat, dan sholat yang diqashar adalah sholat fardhu yang berjumlah 4 rakaat (Dzuhur, Ashar, dan Isya).\n\n" +
                "2. Perbedaan Jamak Taqdim dan Ta'khir\n" +
                "Jamak Taqdim dilaksanakan pada waktu sholat yang pertama (misalnya Dzuhur bersama Ashar di waktu Dzuhur). Jamak Ta'khir dilaksanakan pada waktu sholat kedua (misalnya Maghrib bersama Isya di waktu Isya).\n\n" +
                "3. Mensyukuri Keringanan Ibadah\n" +
                "Rasulullah ﷺ bersabda bahwa sesungguhnya Allah menyukai apabila rukhsah-Nya diambil sebagaimana Dia membenci apabila larangan-Nya diterjang."
        ),
        DetailedArticle(
            id = 90003,
            title = "Adab Berdoa Agar Cepat Diijabah: Rahasia Waktu Mustajab",
            slug = "adab-berdoa-agar-cepat-diijabah",
            category = "Tasawuf & Akhlak",
            imageUrl = "https://images.unsplash.com/photo-1590076215667-875d4ef2d7ee?q=80&w=800&auto=format&fit=crop",
            publishedAt = "05 Sep 2026",
            content = "Doa adalah otak dan inti dari ibadah. Namun seringkali seorang muslim merasa doanya belum kunjung dikabulkan. Ada adab dan etika penting yang diajarkan oleh Rasulullah SAW.\n\n" +
                "1. Memulai dengan Pujian dan Shalawat\n" +
                "Nabi ﷺ mengajarkan sebelum memanjatkan hajat, awali dengan memuji keagungan Allah SWT dan membaca shalawat kepada Nabi Muhammad SAW.\n\n" +
                "2. Yakin dan Penuh Harap\n" +
                "Berdoalah kepada Allah dalam keadaan yakin bahwa doa tersebut pasti didengar dan diijabah. Hindari berdoa dengan ragu atau menganggap remeh kuasa Allah.\n\n" +
                "3. Memanfaatkan Waktu Mustajab\n" +
                "Waktu sepertiga malam terakhir, saat sujud dalam sholat, antara adzan dan iqamah, serta saat turun hujan adalah momen emas terkabulnya permohonan hamba."
        ),
        DetailedArticle(
            id = 90004,
            title = "Kisah Kejujuran Pedagang di Era Salafus Shalih",
            slug = "kisah-kejujuran-pedagang-salafus-shalih",
            category = "Hikmah & Kisah",
            imageUrl = "https://images.unsplash.com/photo-1584551246679-0daf3d275d0f?q=80&w=800&auto=format&fit=crop",
            publishedAt = "28 Agu 2026",
            content = "Kejujuran dalam berniaga adalah pintu keberkahan yang tak pernah usang dimakan zaman.\n\n" +
                "Para pedagang muslim terdahulu tidak semata-mata mencari laba materi. Mereka memprioritaskan ridha Allah dan keterbukaan kondisi barang kepada pembeli.\n\n" +
                "Rasulullah ﷺ bersabda: 'Pedagang yang jujur dan amanah akan dikumpulkan bersama para nabi, orang-orang shiddiqin, dan syuhada di hari kiamat kelak.' Keberkahan harta terletak pada keikhlasan dan integritas, bukan pada manipulasi timbangan atau janji manis semata."
        )
    )

    init {
        loadArticles()
    }

    fun selectCategory(categoryId: Int) {
        _uiState.value = _uiState.value.copy(selectedCategoryId = categoryId)
        filterOrFetch()
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        filterOrFetch()
    }

    fun selectArticle(article: DetailedArticle?) {
        _uiState.value = _uiState.value.copy(selectedArticle = article)
    }

    private fun loadArticles() {
        filterOrFetch()
    }

    private fun filterOrFetch() {
        val catId = _uiState.value.selectedCategoryId
        val query = _uiState.value.searchQuery.trim().lowercase()

        val filtered = fallbackArticles.filter { article ->
            val matchCat = when (catId) {
                0 -> true
                78 -> article.category.contains("Tafsir", ignoreCase = true)
                11 -> article.category.contains("Syariah", ignoreCase = true)
                75 -> article.category.contains("Tasawuf", ignoreCase = true)
                51 -> article.category.contains("Hikmah", ignoreCase = true)
                else -> true
            }
            val matchQuery = query.isEmpty() ||
                article.title.lowercase().contains(query) ||
                article.content.lowercase().contains(query)
            matchCat && matchQuery
        }

        _uiState.value = _uiState.value.copy(items = filtered)
    }
}
