# projet1 JSFS de : TARTARE Theophane

## Instalation 
dans un terminal aller dans client et faite 

```
npm install

npm install socket.io chart.js socket.io-client

npm install websocket

npm install html-webpack-plugin --save-dev

npm install copy-webpack-plugin --save-dev

npm install copy-webpack-plugin --save-dev

npm install --save-dev style-loader css-loader

npm install mongoose


```

faite la meme chose dans server

puis toujours dans vote/serser 

creé un dossier avec 

``` mkdir fichierMongod ```


## Compilation 
dans un terminal aller dans client et faite 

``` npm run build ```


## Execution
dans un premier terminal faite 

``` mongod --dbpath fichierMongod ```

si vous voulez ajouter les donnée de depart faite 

``` mongoimport --db student --collection etudiants --file mongoimport.json --jsonArray ```

dans un deuxieme terminal aller dans serveur et faite 

``` npm run start ```

enfin sur firefox aller sur le lien ``` http://localhost:3000/ ``` 


