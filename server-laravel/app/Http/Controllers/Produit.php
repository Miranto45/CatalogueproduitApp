<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

// Modèle Produit — MANANJAFY Andreas Aslain
// Projet 4 Catalogue Produits — Groupe M&A

class Produit extends Model
{
    // champs qu'on peut remplir depuis le controller
    protected $fillable = [
        'nom',
        'description',
        'prix',
        'stock',
        'reference',
    ];

    protected $casts = [
        'prix'  => 'float',
        'stock' => 'integer',
    ];
}
