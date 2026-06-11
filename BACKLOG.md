# 📋 Backlog FinShop — Sprint 1

## 👥 Acteurs
- **Client** → achète des produits
- **Admin** → gère la plateforme
- **Comptable** → consulte les rapports financiers

---

## 📌 User Stories

### US-01 — Connexion
**En tant que** utilisateur
**Je veux** me connecter avec mon email et mot de passe
**Afin d'** accéder à mon espace personnel sécurisé

**Critères d'acceptation :**
- [ ] Je peux me connecter avec email + mot de passe valides
- [ ] Je reçois un message d'erreur si les identifiants sont incorrects
- [ ] Je suis redirigé vers mon dashboard après connexion
- [ ] Mon token expire après 24h d'inactivité

---

### US-02 — Inscription
**En tant que** nouvel utilisateur
**Je veux** créer un compte avec mon nom, prénom, email et mot de passe
**Afin d'** accéder à la plateforme FinShop

**Critères d'acceptation :**
- [ ] Tous les champs sont obligatoires
- [ ] L'email doit être unique
- [ ] Le mot de passe doit faire minimum 8 caractères
- [ ] La confirmation doit correspondre au mot de passe
- [ ] Un message de succès s'affiche après inscription
- [ ] Un message d'erreur s'affiche si l'email existe déjà

---

### US-03 — Ajout produit
**En tant qu'** admin
**Je veux** ajouter un produit avec nom, prix, description, quantité et catégorie
**Afin que** les clients puissent découvrir et acheter les produits disponibles

**Critères d'acceptation :**
- [ ] Tous les champs obligatoires sont validés
- [ ] Le prix doit être un nombre positif
- [ ] La quantité en stock doit être >= 0
- [ ] Le produit apparaît dans le catalogue après ajout
- [ ] Un message d'erreur s'affiche si un champ est invalide

---

### US-04 — Panier
**En tant que** client
**Je veux** ajouter un produit à mon panier et gérer les quantités
**Afin de** retrouver tous mes produits sélectionnés au moment de valider ma commande

**Critères d'acceptation :**
- [ ] Je peux ajouter un produit au panier en un clic
- [ ] Je peux augmenter ou diminuer la quantité
- [ ] Je peux supprimer un produit du panier
- [ ] Le total se met à jour automatiquement
- [ ] Si le stock est insuffisant, un message m'avertit
- [ ] Mon panier est sauvegardé si je quitte la page

---

### US-05 — Dashboard financier
**En tant que** comptable
**Je veux** consulter le tableau de bord financier filtrable par période
**Afin de** suivre les performances financières de l'entreprise

**Critères d'acceptation :**
- [ ] Je peux filtrer par jour, mois ou année
- [ ] Je vois le CA total de la période sélectionnée
- [ ] Je vois le nombre de commandes de la période
- [ ] Je vois les produits les plus vendus
- [ ] Je peux exporter les données en PDF ou Excel