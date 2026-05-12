<?php

namespace App\Http\Controllers;

use App\Models\Produit;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

// Controller CRUD pour les produits
// Groupe M&A — ANDRIAMAHERISON Tanteliniaina Miranto & MANANJAFY Andreas Aslain

class ProduitController extends Controller
{
    // GET /api/produits — retourne tous les produits
    public function index(): JsonResponse
    {
        $produits = Produit::orderBy('created_at', 'desc')->get();
        return response()->json($produits);
    }

    // GET /api/produits/{id} — un seul produit
    public function show(Produit $produit): JsonResponse
    {
        return response()->json($produit);
    }

    // POST /api/produits — ajouter un produit
    public function store(Request $request): JsonResponse
    {
        $data = $request->validate([
            'nom'         => 'required|string|max:255',
            'description' => 'nullable|string',
            'prix'        => 'required|numeric|min:0',
            'stock'       => 'required|integer|min:0',
            'reference'   => 'required|string|max:100|unique:produits,reference',
        ]);

        $produit = Produit::create($data);
        return response()->json($produit, 201);
    }

    // PUT /api/produits/{id} — modifier un produit
    public function update(Request $request, Produit $produit): JsonResponse
    {
        $data = $request->validate([
            'nom'         => 'sometimes|required|string|max:255',
            'description' => 'nullable|string',
            'prix'        => 'sometimes|required|numeric|min:0',
            'stock'       => 'sometimes|required|integer|min:0',
            'reference'   => 'sometimes|required|string|max:100|unique:produits,reference,' . $produit->id,
        ]);

        $produit->update($data);
        return response()->json($produit);
    }

    // DELETE /api/produits/{id} — supprimer un produit
    public function destroy(Produit $produit): JsonResponse
    {
        $produit->delete();
        return response()->json(null, 204);
    }
}
