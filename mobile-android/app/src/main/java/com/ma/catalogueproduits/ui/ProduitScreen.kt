package com.ma.catalogueproduits.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ma.catalogueproduits.model.Produit
import com.ma.catalogueproduits.viewmodel.ProduitViewModel

// Écran principal — Catalogue Produits
// Groupe M&A : ANDRIAMAHERISON Tanteliniaina Miranto & MANANJAFY Andreas Aslain

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProduitScreen(viewModel: ProduitViewModel = viewModel()) {

    val produits by viewModel.produits.collectAsState()
    val chargement by viewModel.chargement.collectAsState()
    val erreur by viewModel.erreur.collectAsState()
    val message by viewModel.message.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var produitAModifier by remember { mutableStateOf<Produit?>(null) }

    // Auto-effacer les notifications après 3 secondes
    LaunchedEffect(message, erreur) {
        if (message != null || erreur != null) {
            kotlinx.coroutines.delay(3000)
            viewModel.effacerNotifications()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                "Catalogue Produits",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                            Text(
                                "Groupe M&A",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.75f)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = { viewModel.chargerProduits() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Rafraîchir", tint = Color.White)
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    produitAModifier = null
                    showDialog = true
                },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Ajouter") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF0F4FF))
        ) {

            // Carte résumé
            if (produits.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatBox("Produits", "${produits.size}")
                        VerticalDivider(color = Color.White.copy(alpha = 0.4f), modifier = Modifier.height(36.dp))
                        StatBox("Stock total", "${produits.sumOf { it.stock }}")
                        VerticalDivider(color = Color.White.copy(alpha = 0.4f), modifier = Modifier.height(36.dp))
                        StatBox("Rupture", "${produits.count { it.stock == 0 }}")
                    }
                }
            }

            // Notification succès
            AnimatedVisibility(
                visible = message != null,
                enter = fadeIn(), exit = fadeOut()
            ) {
                message?.let {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(it, color = Color.White, modifier = Modifier.padding(12.dp), fontSize = 13.sp)
                    }
                }
            }

            // Notification erreur
            AnimatedVisibility(
                visible = erreur != null,
                enter = fadeIn(), exit = fadeOut()
            ) {
                erreur?.let {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFB71C1C)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(it, color = Color.White, modifier = Modifier.padding(12.dp), fontSize = 13.sp)
                    }
                }
            }

            // Barre de chargement
            if (chargement) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // État vide
            if (produits.isEmpty() && !chargement) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(72.dp),
                            tint = Color(0xFFBBCCEE)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Catalogue vide",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                        Text(
                            "Appuyez sur Ajouter pour créer un produit",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray
                        )
                    }
                }
            }

            // Liste
            LazyColumn(
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                items(produits, key = { it.id }) { produit ->
                    ProduitCard(
                        produit = produit,
                        onEdit = {
                            produitAModifier = produit
                            showDialog = true
                        },
                        onDelete = { viewModel.supprimerProduit(produit.id) }
                    )
                }
            }
        }

        // Dialog ajout / modification
        if (showDialog) {
            DialogFormulaire(
                produitInitial = produitAModifier,
                onDismiss = {
                    showDialog = false
                    produitAModifier = null
                },
                onConfirm = { produit ->
                    if (produitAModifier != null) {
                        viewModel.modifierProduit(produitAModifier!!.id, produit)
                    } else {
                        viewModel.ajouterProduit(produit)
                    }
                    showDialog = false
                    produitAModifier = null
                }
            )
        }
    }
}

@Composable
fun StatBox(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(label, color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
    }
}

@Composable
fun ProduitCard(
    produit: Produit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val (stockColor, stockLabel) = when {
        produit.stock > 10 -> Pair(Color(0xFF2E7D32), "En stock")
        produit.stock > 0  -> Pair(Color(0xFFF57C00), "Stock faible")
        else               -> Pair(Color(0xFFC62828), "Rupture")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Barre couleur stock
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .height(64.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(stockColor)
            )
            Spacer(modifier = Modifier.width(12.dp))

            // Infos produit
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    produit.nom,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "Réf : ${produit.reference}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                if (produit.description.isNotBlank()) {
                    Text(
                        produit.description,
                        fontSize = 12.sp,
                        color = Color(0xFF555555),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "${String.format("%.0f", produit.prix)} Ar",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF1565C0)
                    )
                    Surface(
                        color = stockColor.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            "$stockLabel · ${produit.stock}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            fontSize = 11.sp,
                            color = stockColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Actions
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = onEdit, modifier = Modifier.size(38.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = "Modifier", tint = Color(0xFF1565C0), modifier = Modifier.size(20.dp))
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(38.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = Color(0xFFC62828), modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
fun DialogFormulaire(
    produitInitial: Produit?,
    onDismiss: () -> Unit,
    onConfirm: (Produit) -> Unit
) {
    var nom         by remember { mutableStateOf(produitInitial?.nom ?: "") }
    var description by remember { mutableStateOf(produitInitial?.description ?: "") }
    var prix        by remember { mutableStateOf(produitInitial?.prix?.let { if (it == 0.0) "" else it.toString() } ?: "") }
    var stock       by remember { mutableStateOf(produitInitial?.stock?.toString() ?: "") }
    var reference   by remember { mutableStateOf(produitInitial?.reference ?: "") }

    var errNom by remember { mutableStateOf(false) }
    var errRef by remember { mutableStateOf(false) }
    var errPrix by remember { mutableStateOf(false) }

    val titre = if (produitInitial != null) "Modifier le produit" else "Nouveau produit"

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(18.dp),
        title = { Text(titre, fontWeight = FontWeight.Bold, color = Color(0xFF1565C0)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ChampSaisie("Nom du produit *", nom, { nom = it; errNom = false }, errNom, "Champ obligatoire")
                ChampSaisie("Référence *", reference, { reference = it; errRef = false }, errRef, "Champ obligatoire")
                ChampSaisie("Description", description, { description = it })
                ChampSaisie("Prix (Ariary) *", prix, { prix = it; errPrix = false }, errPrix, "Entrez un prix valide", KeyboardType.Decimal)
                ChampSaisie("Stock", stock, { stock = it }, type = KeyboardType.Number)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    errNom = nom.isBlank()
                    errRef = reference.isBlank()
                    errPrix = prix.isBlank() || prix.toDoubleOrNull() == null
                    if (!errNom && !errRef && !errPrix) {
                        onConfirm(
                            Produit(
                                nom = nom.trim(),
                                description = description.trim(),
                                prix = prix.toDoubleOrNull() ?: 0.0,
                                stock = stock.toIntOrNull() ?: 0,
                                reference = reference.trim()
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
            ) {
                Text(if (produitInitial != null) "Modifier" else "Ajouter", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Annuler") }
        }
    )
}

@Composable
fun ChampSaisie(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    isError: Boolean = false,
    errMsg: String = "",
    type: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label, fontSize = 13.sp) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = type != KeyboardType.Text || label == "Description",
        maxLines = if (label == "Description") 3 else 1,
        isError = isError,
        supportingText = if (isError) ({ Text(errMsg, color = Color.Red, fontSize = 11.sp) }) else null,
        keyboardOptions = KeyboardOptions(keyboardType = type),
        shape = RoundedCornerShape(10.dp)
    )
}
