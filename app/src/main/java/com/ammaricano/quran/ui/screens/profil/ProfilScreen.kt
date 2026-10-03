package com.ammaricano.quran.ui.screens.profil

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ammaricano.quran.ui.theme.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.ammaricano.quran.data.remote.FirebaseHelper

@Composable
fun ProfilScreen(
    viewModel: ProfilViewModel,
    onSurahClick: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                FirebaseHelper.auth.signInWithCredential(credential)
            } catch (_: Exception) { }
        }
    }

    val handleLogin = {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("939819138285-placeholder.apps.googleusercontent.com")
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        googleSignInLauncher.launch(googleSignInClient.signInIntent)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(BgPrimary, BgPrimary2, BgPrimary)
                )
            )
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 44.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Profil",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary
            )
            if (state.user != null) {
                IconButton(
                    onClick = { viewModel.signOut() },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Red.copy(alpha = 0.15f))
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Logout,
                        contentDescription = "Logout",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        if (state.user == null) {
            // Login Prompt
            LoginPrompt(onLogin = handleLogin)
        } else {
            // Logged in content
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Profile Card
                item {
                    ProfileCard(
                        state = state,
                        onEditName = { viewModel.startEditName() },
                        onCancelEdit = { viewModel.cancelEditName() },
                        onSaveEdit = { viewModel.saveDisplayName() },
                        onEditNameChange = { viewModel.onEditNameChange(it) }
                    )
                }

                // Stats Row
                item {
                    StatsRow(state = state)
                }

                // Last Read
                item {
                    LastReadCard(
                        lastRead = state.lastRead,
                        onSurahClick = onSurahClick
                    )
                }

                // Tab Buttons
                item {
                    TabSelector(
                        activeTab = state.activeTab,
                        onTabChange = { viewModel.setActiveTab(it) }
                    )
                }

                // Tab Content
                when (state.activeTab) {
                    0 -> {
                        // Ringkasan
                        item {
                            SummaryContent(state = state, onSurahClick = onSurahClick)
                        }
                    }
                    1 -> {
                        // Bookmarks
                        item {
                            BookmarkFilterRow(
                                activeFilter = state.bookmarkFilter,
                                onFilterChange = { viewModel.setBookmarkFilter(it) }
                            )
                        }
                        val filtered = state.bookmarks.filter { bm ->
                            state.bookmarkFilter == "all" ||
                                    (bm["category"] as? String) == state.bookmarkFilter
                        }
                        if (filtered.isEmpty()) {
                            item {
                                EmptyBookmarks()
                            }
                        } else {
                            items(filtered, key = { it["id"].toString() }) { bookmark ->
                                BookmarkCard(
                                    bookmark = bookmark,
                                    onDelete = {
                                        viewModel.deleteBookmark(it)
                                    }
                                )
                            }
                        }
                    }
                    2 -> {
                        // Hafalan Asmaul Husna
                        item {
                            HafalanContent(memorizedCount = state.memorizedAsmaul.size)
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }
}

@Composable
private fun LoginPrompt(onLogin: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(PrimaryBlue.copy(alpha = 0.2f), PrimaryBlue2.copy(alpha = 0.08f))
                    )
                )
                .border(1.dp, PrimaryBlue.copy(alpha = 0.3f), RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = null,
                tint = PrimaryBlue2,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Masuk untuk Melanjutkan",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Login dengan Google untuk menyimpan progres, bookmark, dan hafalan Asmaul Husna.",
            fontSize = 13.sp,
            color = TextMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 280.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onLogin,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = BgPrimary
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.height(48.dp)
        ) {
            Text(
                text = "Login dengan Google",
                fontWeight = FontWeight.Black,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun ProfileCard(
    state: ProfilUiState,
    onEditName: () -> Unit,
    onCancelEdit: () -> Unit,
    onSaveEdit: () -> Unit,
    onEditNameChange: (String) -> Unit
) {
    val user = state.user ?: return

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        PrimaryBlue.copy(alpha = 0.15f),
                        PrimaryBlue2.copy(alpha = 0.05f)
                    )
                )
            )
            .border(1.dp, PrimaryBlue.copy(alpha = 0.2f), RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(PrimaryBlue.copy(alpha = 0.2f))
                    .border(2.dp, PrimaryBlue.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (user.photoUrl != null) {
                    AsyncImage(
                        model = user.photoUrl.toString(),
                        contentDescription = "Profile",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = (user.displayName?.firstOrNull() ?: 'U').toString().uppercase(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = PrimaryBlue2
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                if (state.isEditingName) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = state.editNameValue,
                            onValueChange = onEditNameChange,
                            modifier = Modifier.weight(1f).height(48.dp),
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 14.sp,
                                color = TextPrimary
                            ),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryBlue2,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                cursorColor = PrimaryBlue2
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        IconButton(onClick = onSaveEdit) {
                            Icon(Icons.Rounded.Check, "Save", tint = EmeraldGreen)
                        }
                        IconButton(onClick = onCancelEdit) {
                            Icon(Icons.Rounded.Close, "Cancel", tint = TextMuted)
                        }
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = user.displayName ?: "User",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "Edit nama",
                            tint = PrimaryBlue2,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { onEditName() }
                        )
                    }
                }

                Text(
                    text = user.email ?: "",
                    fontSize = 12.sp,
                    color = TextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun StatsRow(state: ProfilUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            icon = Icons.Rounded.Bookmark,
            label = "Bookmark",
            value = state.bookmarks.size.toString(),
            color = PrimaryBlue2,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            icon = Icons.Rounded.Star,
            label = "Hafalan",
            value = "${state.memorizedAsmaul.size}/99",
            color = TextGold,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            icon = Icons.Rounded.MenuBook,
            label = "Terakhir",
            value = if (state.lastRead != null) "Ayat ${(state.lastRead["ayatNo"] as? Number)?.toInt() ?: "-"}" else "-",
            color = EmeraldGreen,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(color.copy(alpha = 0.08f))
            .border(1.dp, color.copy(alpha = 0.15f), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Black, color = TextPrimary)
            Text(text = label, fontSize = 10.sp, color = TextMuted)
        }
    }
}

@Composable
private fun LastReadCard(
    lastRead: Map<String, Any?>?,
    onSurahClick: (Int) -> Unit
) {
    if (lastRead == null) return
    val surahNo = (lastRead["surahNo"] as? Number)?.toInt() ?: return
    val surahName = lastRead["surahName"] as? String ?: return
    val ayatNo = (lastRead["ayatNo"] as? Number)?.toInt() ?: 1

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(PrimaryBlue.copy(alpha = 0.15f), PrimaryBlue2.copy(alpha = 0.05f))
                )
            )
            .border(1.dp, PrimaryBlue.copy(alpha = 0.2f), RoundedCornerShape(22.dp))
            .clickable { onSurahClick(surahNo) }
            .padding(18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Bookmark, null, tint = PrimaryBlue2, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Terakhir Dibaca", fontSize = 11.sp, color = PrimaryBlue2, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(surahName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text("Ayat ke-$ayatNo", fontSize = 12.sp, color = TextMuted)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(PrimaryBlue)
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text("Lanjutkan", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
private fun TabSelector(activeTab: Int, onTabChange: (Int) -> Unit) {
    val tabs = listOf("Ringkasan", "Bookmark", "Hafalan")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        tabs.forEachIndexed { index, label ->
            val isSelected = activeTab == index
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) Color.White else Color.Transparent)
                    .clickable { onTabChange(index) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                    color = if (isSelected) BgPrimary else TextMuted
                )
            }
        }
    }
}

@Composable
private fun SummaryContent(state: ProfilUiState, onSurahClick: (Int) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Aktivitas Terkini", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)

        if (state.bookmarks.isEmpty()) {
            Text(
                "Belum ada aktivitas. Mulai baca Al-Qur'an dan tambahkan bookmark!",
                fontSize = 13.sp, color = TextMuted
            )
        } else {
            state.bookmarks.take(5).forEach { bm ->
                BookmarkCard(bookmark = bm, onDelete = null)
            }
        }
    }
}

@Composable
private fun BookmarkFilterRow(activeFilter: String, onFilterChange: (String) -> Unit) {
    val filters = listOf(
        "all" to "Semua",
        "surah" to "Surah",
        "doa" to "Doa",
        "hadits" to "Hadits",
        "dzikir" to "Dzikir",
        "robithoh" to "Robithoh"
    )
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(filters) { (key, label) ->
            val isSelected = activeFilter == key
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isSelected) Color.White else Color.White.copy(alpha = 0.05f))
                    .border(1.dp, if (isSelected) Color.White else Color.White.copy(alpha = 0.1f), CircleShape)
                    .clickable { onFilterChange(key) }
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) BgPrimary else TextMuted
                )
            }
        }
    }
}

@Composable
private fun EmptyBookmarks() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Rounded.BookmarkBorder, null, tint = TextMuted, modifier = Modifier.size(48.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text("Belum ada bookmark", fontSize = 14.sp, color = TextMuted)
    }
}

@Composable
private fun BookmarkCard(
    bookmark: Map<String, Any?>,
    onDelete: ((String) -> Unit)?
) {
    val title = bookmark["title"] as? String ?: "Bookmark"
    val category = bookmark["category"] as? String ?: ""
    val subtitle = bookmark["subtitle"] as? String ?: ""
    val id = bookmark["id"] as? String ?: ""

    val categoryColor = when (category) {
        "surah" -> PrimaryBlue2
        "doa" -> Color(0xFF2DD4BF)
        "hadits" -> Color(0xFFFB7185)
        "dzikir" -> Color(0xFF34D399)
        "robithoh" -> Color(0xFFC084FC)
        else -> PrimaryBlue2
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(categoryColor.copy(alpha = 0.06f))
            .border(1.dp, categoryColor.copy(alpha = 0.15f), RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(categoryColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Bookmark,
                    contentDescription = null,
                    tint = categoryColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                if (subtitle.isNotBlank()) {
                    Text(subtitle, fontSize = 11.sp, color = TextMuted, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Text(
                    category.replaceFirstChar { it.uppercase() },
                    fontSize = 10.sp,
                    color = categoryColor,
                    fontWeight = FontWeight.Bold
                )
            }
            if (onDelete != null) {
                IconButton(onClick = { onDelete(id) }, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Rounded.Delete, "Hapus", tint = Color.Red.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
private fun HafalanContent(memorizedCount: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(TextGold.copy(alpha = 0.1f))
                .border(2.dp, TextGold.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$memorizedCount",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = TextGold
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "dari 99 Asmaul Husna",
            fontSize = 14.sp,
            color = TextMuted
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Tandai hafalan di halaman Asmaul Husna",
            fontSize = 12.sp,
            color = TextMuted.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Progress bar
        val progress = memorizedCount / 99f
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(8.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.08f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(TextGold, Color(0xFFF59E0B))
                        )
                    )
            )
        }
    }
}
