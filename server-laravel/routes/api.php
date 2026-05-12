<?php

use App\Http\Controllers\ProduitController;
use Illuminate\Support\Facades\Route;

// Routes API — Projet 4 Catalogue Produits
// Groupe M&A

Route::apiResource('produits', ProduitController::class);
