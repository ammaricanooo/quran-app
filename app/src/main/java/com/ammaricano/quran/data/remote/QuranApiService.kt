package com.ammaricano.quran.data.remote

import com.ammaricano.quran.data.model.DoaItem
import com.ammaricano.quran.data.model.ShalatResponse
import com.ammaricano.quran.data.model.SurahDetailResponse
import com.ammaricano.quran.data.model.SurahListResponse
import com.ammaricano.quran.data.model.TafsirResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url

interface QuranApiService {

    @GET("api/v2/surat")
    suspend fun getSurahList(): SurahListResponse

    @GET("api/v2/surat/{nomor}")
    suspend fun getSurahDetail(
        @Path("nomor") nomor: Int
    ): SurahDetailResponse

    @GET("api/v2/tafsir/{nomor}")
    suspend fun getTafsir(
        @Path("nomor") nomor: Int
    ): TafsirResponse

    @GET
    suspend fun getDoaList(
        @Url url: String = "https://equran.id/api/doa"
    ): List<DoaItem>

    @POST("api/v2/shalat")
    suspend fun getJadwalSholat(
        @Body requestBody: Map<String, Any>
    ): ShalatResponse
}
