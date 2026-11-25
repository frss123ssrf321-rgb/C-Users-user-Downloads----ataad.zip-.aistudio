package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AtaadRole
import com.example.ui.components.AtaadCard
import com.example.ui.components.AtaadHexagonLogo
import com.example.ui.components.roleIcon
import com.example.ui.theme.BgLight
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardWhite
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldSurface
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyLight
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextOnNavy
import com.example.ui.theme.TextOnNavyMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VerifiedGreen
import kotlinx.coroutines.delay

@Composable
fun AtaadSplashScreen(
    onFinished: () -> Unit
) {
    var revealed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (revealed) 1f else 0.78f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "splash_logo_scale"
    )

    LaunchedEffect(Unit) {
        revealed = true
        delay(1750L)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(NavyDark, NavyPrimary, NavyLight)
                )
            )
            .clickable { onFinished() }
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(28.dp)
            .testTag("splash_screen")
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .widthIn(max = 460.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AtaadHexagonLogo(
                size = 112.dp,
                modifier = Modifier.scale(scale)
            )
            Spacer(modifier = Modifier.height(22.dp))
            AnimatedVisibility(visible = revealed, enter = fadeIn()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "عَتاد",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = CardWhite
                    )
                    Text(
                        text = "ATAAD",
                        style = MaterialTheme.typography.titleMedium.copy(letterSpacing = 5.sp),
                        color = GoldAccent
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "منصة الأسطول الافتراضي للمعدات الثقيلة\nفي المملكة العربية السعودية",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextOnNavyMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SplashTrustChip("بديل خلال 24 ساعة")
                SplashTrustChip("حساب ضمان Escrow")
                SplashTrustChip("فوترة ZATCA")
            }
            Text(
                text = "اضغط للمتابعة الفورية",
                style = MaterialTheme.typography.labelSmall,
                color = GoldLight.copy(alpha = 0.85f)
            )
        }
    }
}

@Composable
private fun SplashTrustChip(text: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.35f))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            style = MaterialTheme.typography.labelSmall,
            color = GoldLight
        )
    }
}

@Composable
fun RoleSelectionScreen(
    onEnterPortal: (AtaadRole) -> Unit,
    onStartRegistration: (AtaadRole) -> Unit
) {
    Scaffold(
        containerColor = BgLight,
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // Deep Industrial Navy Brand Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(listOf(NavyDark, NavyPrimary))
                    )
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 22.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 600.dp)
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AtaadHexagonLogo(size = 54.dp)
                        Column {
                            Row(
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "عَتاد",
                                    style = MaterialTheme.typography.displaySmall,
                                    color = TextOnNavy
                                )
                                Text(
                                    text = "ATAAD",
                                    style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp),
                                    color = GoldAccent,
                                    modifier = Modifier.padding(bottom = 5.dp)
                                )
                            }
                            Text(
                                text = "الأسطول الافتراضي لقطاع المقاولات السعودي",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextOnNavyMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.08f),
                        border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.45f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Security,
                                contentDescription = "حماية عَتاد",
                                tint = GoldAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "اختر نوع حسابك لتجربة البوابات الأربعة أو استعراض مسارات التسجيل والتوثيق",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextOnNavy
                            )
                        }
                    }
                }
            }

            // 4 Role Selection Cards
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 600.dp)
                    .align(Alignment.CenterHorizontally),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "اختر نوع حسابك",
                            style = MaterialTheme.typography.titleLarge,
                            color = NavyPrimary
                        )
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = GoldSurface,
                            border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.VerifiedUser,
                                    contentDescription = "نموذج تفاعلي",
                                    tint = VerifiedGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "نموذج تفاعلي فوري",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NavyPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                items(AtaadRole.entries) { role ->
                    RoleSelectionCard(
                        role = role,
                        onEnterPortal = { onEnterPortal(role) },
                        onStartRegistration = { onStartRegistration(role) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "يمكنك التبديل بين الأدوار الأربعة في أي وقت عبر «مبدّل الأدوار» في أعلى كل شاشة.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun RoleSelectionCard(
    role: AtaadRole,
    onEnterPortal: () -> Unit,
    onStartRegistration: () -> Unit
) {
    AtaadCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("role_card_${role.name.lowercase()}"),
        onClick = onEnterPortal,
        contentPadding = PaddingValues(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(NavyPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = roleIcon(role),
                        contentDescription = role.titleAr,
                        tint = GoldAccent,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = role.titleAr,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = role.subtitleAr,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onEnterPortal,
                    modifier = Modifier
                        .weight(1.35f)
                        .height(48.dp)
                        .testTag("enter_portal_${role.name.lowercase()}"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NavyPrimary,
                        contentColor = CardWhite
                    )
                ) {
                    Text(
                        text = "دخول البوابة",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }

                OutlinedButton(
                    onClick = onStartRegistration,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("register_role_${role.name.lowercase()}"),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.2.dp, GoldAccent),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Filled.HowToReg,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "تسجيل وتوثيق",
                        style = MaterialTheme.typography.labelMedium,
                        color = NavyPrimary
                    )
                }
            }
        }
    }
}
