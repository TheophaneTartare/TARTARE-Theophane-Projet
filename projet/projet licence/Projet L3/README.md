# Règles du Jeu 

# Contexte du projet

Dans ce projet de groupe de troisième année de Licence, il fallait adapter le jeu **Railroad Ink** en utilisant le langage de notre choix ainsi qu’un réflecteur.  

Vous pouvez consulter le dossier *Rétrospective* si vous souhaitez plus de détails sur la conception et la répartition des tâches de ce projet.

# Compiler et lancer 

# Les autres modules
Pour utiliser les modules autres que regle et clientJoueur/Arbitre, il suffit d'aller dans le module voulu (/websocket/module), de mettre les fichiers (y compris les documents texte) dans websocket/,

et d'exécuter les fichiers Python avec : ``` python3 module.py  ``` ou les fichiers JavaScript avec : ``` node fichier.js ``` 

Voici un rapide résumé des modules.

## Prototype
Fichiers abandonnés des clients Joueur et Arbitre.

## Client Idiot
Se contente de se connecter au réflecteur et de lancer l'interface graphique.



## ReflecteurPY
Contient des fichiers faits pour nous familiariser avec le réflecteur.

- connexion.py : Lit un fichier fic.txt et envoie son contenu caractère par caractère au serveur WebSocket.
- historique.py : Se connecte à un serveur WebSocket, affiche les messages reçus et les enregistre dans reçu.txt.



## ReflecteurJS

Contient les équivalents JavaScript des fichiers éponymes dans ReflecteurPY.


# Le jeu 
Pour les clients Joueurs, Arbitres et Idiots il y a différente versions.

Avant tout lancer le reflector dans un terminal : >> websocket$ ./reflector 

Pour lancer les programmes : 

clientArbitreV1: >> websocket$ python3 clientArbitreV1.py

clientJoueurV1: >> websocket$ python3 clientJoueurV1.py

clientArbitreV2: >> websocket$ python3 clientArbitreV2.py

clientJoueurV2: >> websocket$ python3 clientJoueurV2.py

# Règles du jeu 
# pour compiler les classes

Il faut se placer dans >> websocket/regles/src et faire python3 nom_de_la_classe.py 


# lancer les test 

Il faut se placer dans >> websocket/regles/test et faire python3 nom_de_la_classeTest.py

# Lien vers le slide de la soutenance 

https://docs.google.com/presentation/d/1k9gGYd-By6-u_ZfMWQNGPEHV5_owX4O7aNknBY02X-U/edit?usp=sharing

# Lien vers le dossier retrospectif 

https://docs.google.com/document/d/1GiVlhjcOd_63MnfOJh2l-H5Nc0gZCXUXa8ZJfIecQ-U/edit?usp=sharing

## Règles générales : 
### Deroulement du Jeu
- Une partie dure 7 rounds.
- À chaque round, il faut lancer les dés pour déterminer les tuiles disponibles et chaque joueur doit tracer les tuiles sur sa grille 7x7 en respectant les règles de placement.
- Chaque joueur trace ses voies simultanément, sans influencer les autres.
### Calcul des Scores
####  Réseaux connectés
- Un réseau est un ensemble de sorties connectées entre elles par des voies.
- Plus un réseau connecte de sorties, plus il rapporte de points .
#### Plus longue autoroute et plus long chemin de fer
- il faut compter le nombre de cases consécutives connectées
- Chaque case rapporte 1 point.
- Les boucles ne sont comptées qu’une seule fois.
####  Cases centrales
- Les cases centrales sont les 9 cases au centre de la grille.
- Chaque case centrale utilisée par une voie rapporte 1 point.
####  Pénalités pour les fausses routes
- Une fausse route est une extrémité de voie non connectée à une autre voie ou à une sortie.
- Chaque fausse route entraîne une pénalité de -1 point.
#### Score total
- il faut additionner les points des réseaux connectés, des longues routes, et des cases centrales.
- il faut soustraire les points de pénalité des fausses routes.
### Fin du round et fin de la partie
### Fin du round 
- Une fois que tous les joueurs ont tracé toutes les Voies disponibles, le round prend fin.
- Chaque joueur doit indiquer les cases dans lesquelles il a tracé des Voies en reportant le numéro du round en cours dans le coin blanc de chaque case qu’il vient de remplir.
- Les Voies tracées dans un round précédent ne peuvent pas être effacées.
- il faut lancer de nouveau les dés Voie pour commencer le round suivant.
### Fin de la partie
- La partie se termine à la fin du 7e round.
- Le joueur ayant obtenu le plus de points gagne la partie.
- En cas d’égalité, le joueur avec le moins de Fausses Routes l’emporte et si l’égalité persiste, les joueurs se partagent la victoire.


## Grille ( Plateau.py) : 
- La grille est une carte de 7x7 cases.
- Les joueurs placent les tuiles sur les cases libres de leur grille, en respectant les contraintes de compatibilité, de placments valides...
## Dés: 
- Dés de base : Il y a trois dés qui déterminent les tuiles à placer. Chaque dé a des faces représentant des sections de route ou de train

- Dé de connexions : Un dé supplémentaire peut être utilisé pour des connexions spéciales ou des effets supplémentaires

- Tirage des dés : À chaque tour, les dés sont lancés pour déterminer les tuiles disponibles pour tous les joueurs. Chaque joueur doit ensuite placer ces tuiles sur sa grille.

- Face d'un dé : Une route, un rail, une station

- À chaque tirage de dés, dessiner les 4 tuiles obtenues dans l'ordre souhaité.

## Tuiles ( Tuile.py): 
- Types de tuiles : Les tuiles représentent des sections de route (Highway) ou de train (Railway), des jonctions (Junction), des courbes (Curve), et des stations (Station).

- Orientation des tuiles : Les tuiles peuvent être placées dans différentes orientations (droite, gauche, inversée, etc.). Certaines tuiles sont symétriques. donc soit Droites , soit Courbes , soit Symétriques 

- Tuiles spéciales : Il existe des tuiles spéciales qui peuvent être utilisées pour des effets spécifiques, comme des connexions supplémentaires ou des objectifs particuliers. Peut enutiliser 3 en une partie et seulement 1 par round. 

- Tuiles valides et bien placées et relier 
- Verifier que les tuiles placer sont bien valides et relier


Les tuiles placées doivent respecter les règles de connexion entre les routes et les rails :

   -> Les routes doivent se connecter aux routes.
   -> Les rails doivent se connecter aux rails
   -> Les jonctions peuvent connecter à la fois des routes et des rails
   -> Les stations peuvent se connecter à des routes et des rails. 

- Ligne bonus : À chaque round, le joueur a le choix d'utiliser ou non une voie spéciale, en plus des 4 tuiles du tirage des dés. Le joueur peut utiliser au maximum 3 voies spéciales par partie et une seule fois par round, en plus des tuiles tirées des dés.



