import sys
from PyQt5.QtWidgets import QApplication, QMainWindow, QWidget
from PyQt5.QtGui import QPainter, QPen, QFont, QPixmap, QTransform, QPolygon
from PyQt5.QtCore import Qt, QPoint
from interfaceGraphique.Dictionnaire import images, placement, orientation
import os   
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), "../regles/src")))
from Joueur import Joueur

class Dessin(QWidget):
    """
    Classe qui gére l'affichage du plateau de jeu et des tuiles.
    """
    def __init__(self, nombreCase,idJoueur, parent=None,wsapp=None):
        """
        Initialise le widget du plateau de jeu.
        :param nombreCase: Nombre de cases sur le plateau.
        :param idJoueur: Identifiant du joueur.
        :param parent: Widget parent.
        :param wsapp: WebSocket pour communication websocket.
        """
        super().__init__(parent)
        self.wsapp=wsapp
        self.idJoueur=idJoueur
        #attributs du plateau de jeu  
        self.decalage = 200
        self.tailleCase = 50
        self.decalageGrandeImage = self.tailleCase
        self.nombreCase = nombreCase
        self.TailleGrandeCase = (self.tailleCase*4)
        self.tailleCentreGrille = 2
        self.decalageLettre = self.tailleCase//2 +10
        self.decalageChiffre = self.tailleCase//2 +10
        self.largeurFleche=self.TailleGrandeCase // 2
        self.hauteurFleche=self.TailleGrandeCase
        self.orientationGrandeTuile=0

        self.TuileSelectIndex = None

        self.xPositionGrandeTuile=self.decalage + (self.nombreCase*self.tailleCase+self.decalageChiffre+self.largeurFleche + self.decalageGrandeImage)
        self.yPositionGrandeTuile=self.decalage + (self.tailleCase*self.nombreCase)//4 
        self.xPositionFlecheGauche=self.xPositionGrandeTuile-self.largeurFleche
        self.xPositonFlecheDroite=self.xPositionGrandeTuile+(self.TailleGrandeCase) //2+self.TailleGrandeCase // 2//2
        self.yPositionFleche=self.yPositionGrandeTuile + (self.TailleGrandeCase - self.TailleGrandeCase // 2) // 2
        #attributs des tuiles 
        self.image = images
        self.tuile = QPixmap("")
        self.nomImg = None
        self.grandeTuile = QPixmap("")

        
        self.tuilesReserve =[ 
            {"type":"HH", "image": QPixmap(self.image["HH"]).scaledToWidth(self.TailleGrandeCase // 2),"special":True, "select": False, "placed": False},
            {"type":"SH", "image": QPixmap(self.image["SH"]).scaledToWidth(self.TailleGrandeCase // 2),"special":True, "select": False, "placed": False},
            {"type":"SHR", "image": QPixmap(self.image["SHR"]).scaledToWidth(self.TailleGrandeCase // 2),"special":True, "select": False, "placed": False},
            {"type":"RR", "image": QPixmap(self.image["RR"]).scaledToWidth(self.TailleGrandeCase // 2),"special":True, "select": False, "placed": False},
            {"type":"SR", "image": QPixmap(self.image["SR"]).scaledToWidth(self.TailleGrandeCase // 2),"special":True, "select": False, "placed": False},
            {"type":"SS", "image": QPixmap(self.image["SS"]).scaledToWidth(self.TailleGrandeCase // 2),"special":True, "select": False, "placed": False}
        ]

        self.tuilesPlacer = [] 


        self.nomImgGrandeTuile=None
        self.listeTuile=[]

        #tuile selectionné par la souris 
        self.TuileSelect = None
        self.imageTuileSelect = None
        self.positionSouris=None

        self.joueur = Joueur(idJoueur)

    def paintEvent(self, event):
        """
        Gère l'affichage graphique.
        """
        painter = QPainter(self)
        font = QFont("Arial", 12)
        painter.setFont(font)
        pen = QPen()
        pen.setWidth(2)
        painter.setPen(pen)

        self.dessinGrille(painter)
        
        pen.setWidth(1)
        pen.setStyle(Qt.DotLine)
        painter.setPen(pen)

        self.dessinCase(painter)
        self.lettre(painter)
        self.chiffre(painter)
        self.dessinSortie(painter)
        self.dessinTuileReserve(painter)
        painter.setOpacity(1)
        

        self.dessinGrandeTuile(painter)
        self.dessinFleche(painter)

        #Afficher les toutes les tuiles de la grille 
        for tuile in self.listeTuile:
            self.dessinTuile(tuile["coordonne"][0], tuile["coordonne"][1], tuile["orientation"], painter,tuile["image"],tuile["flip"])
        #Affiche la tuile pendant le deplacement
        if self.TuileSelect and self.imageTuileSelect:
            tailleTuile = self.imageTuileSelect.scaled(self.tailleCase, self.tailleCase)
            self.dessinGrandeTuile(painter, self.positionSouris, tailleTuile)

        

    # à supprimer pas utilisé s 
    # def setNomImg(self, nomImg):
    #     self.nomImg = nomImg
    #     self.update()

    def setNomImgGrandeTuile(self, nomImg):
        """
        Change le nom de l'image de la grande tuile.
        Param:
        nomImg (str): Le nom de l'image à utiliser pour la grande tuile.
        """
        self.nomImgGrandeTuile = nomImg
        self.grandeTuile = QPixmap(self.image[nomImg])
        self.update()

    def placeTuile(self, nom, orientation1, cord):
        """
        Place une tuile sur la grille avec une orientation et des coordonnées spécifiées.

        Param:
            nom (str): Le nom de l'image de la tuile.
            orientation1 (str): L'orientation de la tuile (ex: "F", "FL", "FR", "FU").
            cord (str): Les coordonnées où placer la tuile (ex: "C4", "E2", "D5").
        """
        image = QPixmap(self.image[nom])
        flip=False
        if(orientation1 in["F","FL","FR","FU"]):
            flip=True
        orientation_tuile = orientation[orientation1]
        coord = (placement[cord[0]], placement[cord[1]])
        
        self.listeTuile.append({"image": image, "orientation": orientation_tuile, "coordonne": coord,"flip":flip})
        self.update()

    def dessinGrille(self,painter):
        """
        Dessine le contour de la grille du plateau.
        """
        largeur_grille = self.nombreCase * self.tailleCase
        painter.drawRect(self.decalage, self.decalage, largeur_grille, largeur_grille)
        painter.drawRect(self.decalage + self.tailleCase * 2, self.decalage + self.tailleCase * 2,
                         largeur_grille - self.tailleCentreGrille * self.tailleCase * 2, largeur_grille - self.tailleCentreGrille * self.tailleCase * 2)

        

    def dessinCase(self,painter):
        """
        Dessine la grille du plateau avec les cases.
        """
        largeur_grille = self.nombreCase * self.tailleCase
        for i in range(self.nombreCase + 1):
            x = self.decalage + i * self.tailleCase
            y = self.decalage + i * self.tailleCase

            painter.drawLine(x, self.decalage, x, self.decalage + largeur_grille)
            painter.drawLine(self.decalage, y, self.decalage + largeur_grille, y)

    def chiffre(self,painter):
        """
        Affiche les chiffres des lignes du plateau.
        """
        for i in range(self.nombreCase):
            number = str(self.nombreCase - i)
            y = self.decalage + i * self.tailleCase + self.tailleCase // 2
            painter.drawText(self.decalage - self.decalageChiffre , y, number) 
            painter.drawText(self.decalage + self.nombreCase * self.tailleCase + self.decalageChiffre, y, number)

    def lettre(self,painter):
        """
        Affiche les lettres des colonnes du plateau.
        """
        for i in range(self.nombreCase):
            letter = chr(65 + i)
            x = self.decalage + i * self.tailleCase + self.tailleCase // 2
            painter.drawText(x, (self.decalage - self.decalageLettre) , letter)
            painter.drawText(x, self.decalage + self.nombreCase * self.tailleCase +self.decalageLettre, letter)

    def dessinSortie(self,painter):
        """
        Affiche les sorties du plateau.
        """
        self.dessinSortieTriangle(painter)
        self.dessinSymbolleSortie(painter)

    
    
    def dessinSortieTriangle(self,painter):
        for i in range(2, self.nombreCase, 2):
            x = self.decalage + i * self.tailleCase
            y = self.decalage + i * self.tailleCase
            z = self.decalage + self.nombreCase * self.tailleCase 


            painter.setPen(QPen(Qt.SolidLine))
        
            self.dessinTriangle(painter, x - self.tailleCase , self.decalage, orientation["N"])
            self.dessinTriangle(painter, self.decalage, y, orientation["E"])
            self.dessinTriangle(painter, z, y - self.tailleCase, orientation["W"])
            self.dessinTriangle(painter, x , z, orientation["S"])

    def dessinSymbolleSortie(self,painter):
        for i in range(2, self.nombreCase, 2):
            x = self.decalage + i * self.tailleCase
            y = self.decalage + i * self.tailleCase
            z = self.decalage + self.nombreCase * self.tailleCase

            if (i == 2 or i == 6) : 
                self.dessinRouteSortie(painter, x - self.tailleCase , self.decalage, orientation["N"])
                self.dessinRouteSortie(painter, x , z, orientation["S"])
                self.dessinRailSortie(painter, self.decalage, y, orientation["E"])
                self.dessinRailSortie(painter, z, y - self.tailleCase, orientation["W"])
            else :
                self.dessinRailSortie(painter, x - self.tailleCase , self.decalage, orientation["N"])
                self.dessinRailSortie(painter, x , z, orientation["S"])
                self.dessinRouteSortie(painter, self.decalage, y, orientation["E"])
                self.dessinRouteSortie(painter, z, y - self.tailleCase, orientation["W"])


    def dessinTriangle(self, painter, x, y, orientation):
        painter.save()  
        painter.translate(x, y)  
        painter.rotate(orientation) 
        pen = QPen() 
        pen.setStyle(Qt.SolidLine)  
        painter.setPen(pen)

        millieu = self.tailleCase // 2  
        points = QPolygon([
            QPoint( millieu + 15, 0), 
            QPoint( millieu - 15, 0),
            QPoint( millieu, -15)
        ])
        painter.drawPolygon(points)
        painter.restore() 

    def dessinRouteSortie(self, painter, x, y, orientation):
        painter.save()  
        painter.translate(x, y)  
        painter.rotate(orientation) 
        pen = QPen() 
        pen.setStyle(Qt.SolidLine)  
        painter.setPen(pen)

        millieu = self.tailleCase // 2
        painter.drawLine(millieu-10,0,millieu-10,-20)
        painter.drawLine(millieu+10,0,millieu+10,-20)
        painter.drawLine(millieu,-5,millieu,-10)
        painter.drawLine(millieu,-15,millieu,-20)
        painter.restore() 

    def dessinRailSortie(self, painter, x, y, orientation):
        painter.save()  
        painter.translate(x, y)  
        painter.rotate(orientation) 
        pen = QPen() 
        pen.setStyle(Qt.SolidLine)  
        painter.setPen(pen)

        millieu = self.tailleCase // 2
        painter.drawLine(millieu,0,millieu,-20)
        painter.drawLine(millieu-5,-5,millieu+5,-5)
        painter.drawLine(millieu-5,-15,millieu+5,-15)
        painter.restore() 

    

    def dessinGrandeTuile(self, painter, positionSouris=None, image=None):
        """
        Dessine la grande tuile sur l'interface graphique.

        Param:
            painter (QPainter): L'objet utilisé pour dessiner la tuile.
            positionSouris : La position actuelle de la souris
            image (QPixmap): Une image pour la grande tuile. 

        La fonction dessine la grande tuile soit à sa position fixe, soit à l'emplacement de la 
        souris, permettant un effet de déplacement.
        """
        #Cas par défault 
        if image == None:
            image = self.grandeTuile.scaled(self.TailleGrandeCase, self.TailleGrandeCase)
        #Deplacement de la tuile  
        if positionSouris:
            x = positionSouris.x() - image.width() // 2
            y = positionSouris.y() - image.height() // 2
        else:
            x = self.xPositionGrandeTuile
            y = self.yPositionGrandeTuile
        painter.drawPixmap(x, y, image)

    def dessinFleche(self,painter):
        """
        Affiche les fleches pour tourner la grande tuile.
        """
        fleche1 = QPixmap("interfaceGraphique/graphics/fleche.png")
        fleche1 = fleche1.scaledToWidth(self.TailleGrandeCase // 2)
        fleche1 = fleche1.scaledToHeight(self.TailleGrandeCase // 2)

        x = self.xPositionFlecheGauche
        y = self.yPositionFleche

        transform1 = QTransform().rotate(45)
        fleche1 = fleche1.transformed(transform1)
        painter.drawPixmap(x, y, fleche1)

        fleche2 = QPixmap("interfaceGraphique/graphics/fleche.png")
        fleche2 = fleche2.scaledToWidth(self.TailleGrandeCase // 2)
        fleche2 = fleche2.scaledToHeight(self.TailleGrandeCase // 2)

        x = self.xPositionGrandeTuile+(self.grandeTuile.width()) //2+self.TailleGrandeCase // 2//2

        transform2 = QTransform().rotate(140)
        transform2.scale(1, -1)
        fleche2 = fleche2.transformed(transform2)

        painter.drawPixmap(x, y, fleche2)

    def dessinTuile(self, row, col, orientation,painter,image,flip):
        """
        Dessine une tuile sur la grille en fonction de sa position, orientation et transformation.

        Param:
            row (int): Indice de la ligne où placer la tuile.
            col (int): Indice de la colonne où placer la tuile.
            orientation (int): Angle de rotation de la tuile en degrés.
            painter (QPainter): L'objet utilisé pour dessiner la tuile.
            image (QPixmap): L'image de la tuile à dessiner.
            flip (bool): Indique si l'image doit être retournée horizontalement.

        """
        img = image.scaledToWidth(self.tailleCase)
        img = image.scaledToHeight(self.tailleCase)
        y = self.decalage + col * self.tailleCase + (self.tailleCase - img.width()) // 2
        x = self.decalage + row * self.tailleCase + (self.tailleCase - img.height()) // 2


        transform = QTransform().rotate(orientation)
        if flip:
            transform.scale(-1,1)
        img = img.transformed(transform)

        painter.drawPixmap(x, y, img)

    # à supprimer ? 
    # def placeImage(self, nomImg):
    #     painter.drawPixmap(10, 20, img)


    def ajoutTuileReserve(self, name, special=False):
        self.joueur.ajoutTuilesMain([name])
        # self.tuilesReserve[name] = {"image": QPixmap(self.image[name]).scaledToWidth(self.TailleGrandeCase // 2), "select": False, "placed": False, "special": special}
        self.tuilesReserve.append({"type": name,"image": QPixmap(self.image[name]).scaledToWidth(self.TailleGrandeCase // 2),"special": special,"select": False,"placed": False})
        self.update()
        
    
    def dessinTuileReserve(self,painter) :
        x_positionSpecial = self.decalage // 2
        y_positionSpecial = 20
        x_positionNormal = self.xPositionGrandeTuile
        y_positionNormal = self.yPositionGrandeTuile * 2

        for data in self.tuilesReserve:
            image = data["image"]
            if data["select"] or data["placed"]:
                opacity = 0.3
            else:
                opacity = 1.0
            painter.setOpacity(opacity)

            if data["special"]:
                painter.drawPixmap(x_positionSpecial, y_positionSpecial, image)
                x_positionSpecial += image.width() + 10  
            else:
                painter.drawPixmap(x_positionNormal, y_positionNormal, image)
                x_positionNormal += image.width() + 10  

                

    def mousePressEvent(self, event):
        """
        Gère l'évenement quand la souris est pressé.

        Cette fonction détecte où l'utilisateur a cliqué et effectue différentes actions en conséquence :

        1. Sélection de la grande tuile :
        - Si le clic est dans la zone de la grande tuile, celle-ci est sélectionnée.

        2. Rotation de la grande tuile :
        - Si le clic est sur la flèche gauche, la tuile est tournée de -90°.
        - Si le clic est sur la flèche droite, la tuile est tournée de +90°.

        3. Sélection d'une tuile spéciale :
        - Si le clic est sur une tuile spéciale non placée, elle devient sélectionnée.

        4. Sélection d'une tuile normale dans la réserve :
        - Si le clic est sur une tuile normale, elle est sélectionnée.

        """
        x = self.xPositionGrandeTuile
        y = self.yPositionGrandeTuile

        #Dans la grande image
        if (event.pos().x() >= x and event.pos().x() <= x + self.TailleGrandeCase and event.pos().y() >= y and event.pos().y() <= y + self.TailleGrandeCase):
            self.TuileSelect = self.nomImgGrandeTuile
            self.imageTuileSelect = QPixmap(self.image[self.nomImgGrandeTuile])
            self.positionSouris = event.pos()

        #Dans la fleche de gauche
        x_gauche = self.xPositionFlecheGauche
        y_fleche = self.yPositionFleche

        if (event.pos().x() >= x_gauche and event.pos().x() <= x_gauche + self.largeurFleche and event.pos().y() >= y_fleche and event.pos().y() <= y_fleche + self.hauteurFleche):
            self.orientationGrandeTuile-=90
            transform = QTransform().rotate(self.orientationGrandeTuile)
            self.grandeTuile = QPixmap(self.image[self.nomImgGrandeTuile]).transformed(transform)
            self.update()

        #Dans la fleche de droite
        x_droite =self.xPositionGrandeTuile

        if (event.pos().x() >= self.xPositonFlecheDroite+self.tailleCase and event.pos().x() <=self.xPositonFlecheDroite+self.largeurFleche+self.tailleCase and event.pos().y() >= y_fleche and event.pos().y() <= y_fleche + self.hauteurFleche):
            self.orientationGrandeTuile+=90 
            image_origine = QPixmap(self.image[self.nomImgGrandeTuile]) 
            transform = QTransform().rotate(self.orientationGrandeTuile)
            self.grandeTuile = image_origine.transformed(transform)
            self.update()

        #dans une tuile special
        x_tuileSpecial = self.decalage // 2
        y_tuileSpecial = 20
        x_positionNormal = self.xPositionGrandeTuile
        y_positionNormal = self.yPositionGrandeTuile * 2

        
        for i, data in enumerate(self.tuilesReserve):
            tuile = data["image"]
            largeur = tuile.width()
            hauteur = tuile.height()
            if data["special"]:
                
                
                if (x_tuileSpecial <= event.pos().x() <= x_tuileSpecial + largeur and
                    y_tuileSpecial <= event.pos().y() <= y_tuileSpecial + hauteur):


                    if not data["placed"]:
                        for tuile_data in self.tuilesReserve:
                            tuile_data["select"] = False

                        self.setNomImgGrandeTuile(data["type"])
                        self.positionSouris = event.pos()
                        data["select"] = True
                        self.TuileSelectIndex = i
                        self.update()
                        return

                x_tuileSpecial += largeur + 10
            else: #dans une tuile pas special
                
                if (x_positionNormal <= event.pos().x() <= x_positionNormal + largeur and
                    y_positionNormal <= event.pos().y() <= y_positionNormal + hauteur):


                    for tuile in self.tuilesReserve:
                        tuile["select"] = False

                    self.setNomImgGrandeTuile(data["type"])
                    self.positionSouris = event.pos()
                    data["select"] = True
                    self.TuileSelectIndex = i
                    self.update()
                    return

                x_positionNormal += largeur + 10

        



    def mouseReleaseEvent(self, event):
        """
        Gère l'événement de relâchement du bouton de la souris.

        Cette fonction permet de placer une tuile sur la grille si elle est sélectionnée
        et si la case cible est valide.

        """
        if self.TuileSelect:
            xSouris = event.pos().x()
            ySouris = event.pos().y()
        
            xCase = (xSouris - self.decalage) // self.tailleCase
            yCase = (ySouris - self.decalage) // self.tailleCase

            # Vérification de la case dans la grille 
            if 0 <= xCase < self.nombreCase and 0 <= yCase < self.nombreCase:
                # verification si la case est deja ocuper 
                 if not self.predCasePrise(xCase,yCase) :
                    image = QPixmap(self.image[self.TuileSelect])
                    self.listeTuile.append({"image": image, "orientation": self.orientationGrandeTuile, "coordonne": (xCase, yCase), "flip": False})

                    if not self.joueur.isSpecial(self.TuileSelect) :
                        print(f"Joueur main {self.joueur.mainVide()} ")
                        self.joueur.deleteTuile(self.TuileSelect)

                    if self.wsapp:
                        self.wsapp.send(f"{self.idJoueur} PLACES {self.TuileSelect} {self.trouverOrientation()} {self.trouverPosition(xCase,yCase)}")

                    if self.joueur.mainVide():
                        print(f"Joueur {self.idJoueur} a fini son tour, envoi de YIELDS")
                        self.wsapp.send(f"{self.idJoueur} YIELDS")

                    if self.TuileSelectIndex is not None:
                        tuile_data = self.tuilesReserve[self.TuileSelectIndex]
                        tuile_data["placed"] = True
                            
                        if not tuile_data["special"]:
                            self.tuilesPlacer.append(self.tuilesReserve[self.TuileSelectIndex])
                            del self.tuilesReserve[self.TuileSelectIndex]

                    self.resetGrandeImage()

            self.orientationGrandeTuile = 0
            #self.grandeTuile = QPixmap(self.image[self.nomImgGrandeTuile])
            self.resetTuileSelect()

    def retourArriereGrahpique(self) :
        self.joueur.retourArriereJoueur()
        self.tuilesReserve.append(self.tuilesPlacer[-1])
        del self.tuilesPlacer[-1] 
        del self.listeTuile[-1] 
        self.update()

    def resetGrandeImage(self):
        """
        Réinitialise l'image de la grande tuile et ses attributs associés.
        """
        self.nomImgGrandeTuile = None
        self.grandeTuile = QPixmap()
        self.TuileSelect = None 
        self.positionSouris = None
        self.update()

    def trouverOrientation(self):
        orientation=self.orientationGrandeTuile%360
        if(orientation==0):
            return "N"
        elif(orientation==90):
            return "W"
        elif(orientation==180):
            return "S"
        elif(orientation==270):
            return "E"

    def predCasePrise(self,xCase,yCase) :
        """
        Vérifie si une case de la grille est déjà occupée par une tuile.
        """
        for tuile in self.listeTuile :
            if tuile["coordonne"][0] == xCase and tuile["coordonne"][1] == yCase :
                return True
        return False

    def resetTuileSelect(self) :
        self.TuileSelect = None
        self.positionSouris = None
        self.update()

    def trouverPosition(self,xCase,yCase):
        ligne = chr(ord('A') + xCase)
        colonne = str(7-yCase)
        return ligne + colonne

    def mouseMoveEvent(self, event):
        """
        Gère le déplacement de la tuile sélectionnée avec la souris.
        """
        if self.TuileSelect:
            self.positionSouris = event.pos()
            self.update()
    