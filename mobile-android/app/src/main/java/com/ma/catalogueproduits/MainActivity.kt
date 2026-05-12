package com.ma.catalogueproduits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ma.catalogueproduits.ui.ProduitScreen
import com.ma.catalogueproduits.ui.theme.CatalogueProduitsMaTheme

// Projet 4 — Catalogue Produits
// Groupe M&A : ANDRIAMAHERISON Tanteliniaina Miranto & MANANJAFY Andreas Aslain

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatalogueProduitsMaTheme {
                ProduitScreen()
            }
        }
    }
}
