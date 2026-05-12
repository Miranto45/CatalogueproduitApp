package com.ma.catalogueproduits.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ma.catalogueproduits.api.RetrofitInstance
import com.ma.catalogueproduits.model.Produit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

// ViewModel — logique métier du catalogue produits
// ANDRIAMAHERISON Tanteliniaina Miranto & MANANJAFY Andreas Aslain — Groupe M&A

class ProduitViewModel : ViewModel() {

    private val api = RetrofitInstance.api

    private val _produits = MutableStateFlow<List<Produit>>(emptyList())
    val produits: StateFlow<List<Produit>> = _produits

    private val _chargement = MutableStateFlow(false)
    val chargement: StateFlow<Boolean> = _chargement

    private val _erreur = MutableStateFlow<String?>(null)
    val erreur: StateFlow<String?> = _erreur

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    init {
        chargerProduits()
    }

    fun chargerProduits() {
        viewModelScope.launch {
            _chargement.value = true
            _erreur.value = null
            try {
                _produits.value = api.getProduits()
            } catch (e: Exception) {
                _erreur.value = "Erreur de connexion : ${e.localizedMessage}"
            } finally {
                _chargement.value = false
            }
        }
    }

    fun ajouterProduit(produit: Produit) {
        viewModelScope.launch {
            try {
                _erreur.value = null
                api.createProduit(produit)
                chargerProduits()
                _message.value = "Produit ajouté avec succès"
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                _erreur.value = "Erreur serveur (${e.code()}) : $errorBody"
            } catch (e: Exception) {
                _erreur.value = "Erreur réseau : ${e.localizedMessage}"
            }
        }
    }

    fun modifierProduit(id: Int, produit: Produit) {
        viewModelScope.launch {
            try {
                _erreur.value = null
                api.updateProduit(id, produit)
                chargerProduits()
                _message.value = "Produit modifié avec succès"
            } catch (e: Exception) {
                _erreur.value = "Erreur lors de la modification : ${e.localizedMessage}"
            }
        }
    }

    fun supprimerProduit(id: Int) {
        viewModelScope.launch {
            try {
                _erreur.value = null
                api.deleteProduit(id)
                chargerProduits()
                _message.value = "Produit supprimé"
            } catch (e: Exception) {
                _erreur.value = "Erreur lors de la suppression."
            }
        }
    }

    fun effacerNotifications() {
        _message.value = null
        _erreur.value = null
    }
}
