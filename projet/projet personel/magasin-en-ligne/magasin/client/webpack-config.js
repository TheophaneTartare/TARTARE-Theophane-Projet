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
    new CopyPlugin({
      patterns: [
       {
          context: path.resolve(__dirname, './style'),
          from: '*.css',
	        to:   'style/[name][ext]',
          noErrorOnMissing: true
	    },
  ]
  }),
  ],
  module: {
    rules : [
      {
        test: /\.css$/,
        use: [
          { loader: 'style-loader' },
          { loader: 'css-loader' }
        ]
      }
    ]
  },


  mode: 'development'
};
