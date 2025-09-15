const fs = require('fs');
const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyPlugin = require('copy-webpack-plugin');



const homePath = path.resolve(__dirname, './src/home.js');
console.log("Checking file:", homePath);
console.log("File exists?", fs.existsSync(homePath));


module.exports = {
  entry: {
    'home' : path.resolve(__dirname, './src/home.js') ,
    'admin' : path.resolve(__dirname, './src/admin.js') ,
    'vote' : path.resolve(__dirname, './src/vote.js') ,
    'about' : path.resolve(__dirname, './src/about.js') 
  }, 
  
  output: {
    path: path.resolve(__dirname, '../serveur/public'),
    filename: 'scripts/[name]-bundle.js'
  } ,
  plugins: [
    new HtmlWebpackPlugin({
        filename: 'home.html',
        template: './html/home.html',
        chunks: ['home']
    }),
    new HtmlWebpackPlugin({
      filename: 'admin.html',
      template: './html/admin.html',
      chunks: ['admin']
    }),
    new HtmlWebpackPlugin({
      filename: 'vote.html',
      template: './html/vote.html',
      chunks: ['vote']
    }),
    new HtmlWebpackPlugin({
      filename: 'about.html',
      template: './html/about.html',
      chunks: ['about']
    }),
    new CopyPlugin({
      patterns: [
       {
          context: path.resolve(__dirname, './style'),
          from: '*.css',
	        to:   'style/[name][ext]',
          noErrorOnMissing: true
	    },
  ]
  })
],

  mode: 'development'
};
