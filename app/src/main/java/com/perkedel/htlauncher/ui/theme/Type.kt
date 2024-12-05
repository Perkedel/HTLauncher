package com.perkedel.htlauncher.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.googlefonts.Font
//import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.perkedel.htlauncher.R

//val provider = GoogleFont.Provider(
//    providerAuthority = "com.google.android.gms.fonts",
//    providerPackage = "com.google.android.gms",
//    certificates = R.array.com_google_android_gms_fonts_certs
//)
//
//val bodyFontFamily = FontFamily(
//    Font(
//        googleFont = GoogleFont("Ubuntu"),
//        fontProvider = provider,
//    )
//)
//
//val displayFontFamily = FontFamily(
//    Font(
//        googleFont = GoogleFont("Ubuntu"),
//        fontProvider = provider,
//    )
//)

// MATERIAL GENERATOR
// http://material-foundation.github.io?primary=%230052EB&bodyFont=Ubuntu&displayFont=Ubuntu&colorMatch=true

// Default Material 3 typography values
val baseline = Typography()

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)