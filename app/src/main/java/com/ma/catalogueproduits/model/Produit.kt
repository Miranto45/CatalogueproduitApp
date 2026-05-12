package com.ma.catalogueproduits.model

// Data class représentant un produit
// MANANJAFY Andreas Aslain — Groupe M&A

data class Produit(
    val id: Int = 0,
    val nom: String = "",
    val description: String = "",
    val prix: Double = 0.0,
    val stock: Int = 0,
    val reference: String = ""
)
