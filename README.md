
# BDD Connector
### Ce logiciel en Java permet de manipuler une base de données distante ou locale à l'aide d'une interface conviviale. Il offre les fonctionnalités suivantes :

 - Connexion à une base de données via JDBC
 - Exécution de requêtes SQL (SELECT, INSERT, UPDATE, DELETE, etc.)
 - Affichage des résultats de requêtes sous forme de tableaux
 - Création/suppression de tables et de bases de données
 - etc.

## Prérequis

 - Java 11 ou supérieur
 - Une base de données compatible JDBC (MySQL, PostgreSQL, Oracle, etc.)

## Installation 

 1.  Téléchargez le fichier .jar du logiciel à partir de la page de release du dépôt.
 2.  Assurez-vous que Java est bien installé sur votre ordinateur.
 3. Ouvrez une invite de commande et exécutez la commande suivante :

    java -jar nom_du_fichier.jar

## Dépendances 
### Ce logiciel utilise les bibliothèques suivantes:

 - [Legui](https://github.com/SpinyOwl/legui) pour la création de l'interface utilisateur. Legui est une bibliothèque de widgets pour OpenGL conçue pour la création d'interfaces simples et rapides à mettre en place.
 - [LWJGL](https://www.lwjgl.org/) (Lightweight Java Game Library) pour l'accès aux fonctionnalités de bas niveau (OpenGL, OpenAL, etc.) nécessaires à l'exécution de Legui.
