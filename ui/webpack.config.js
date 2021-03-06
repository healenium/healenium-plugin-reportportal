const webpack = require('webpack');
const path = require('path');

const config = {
  entry: [
    './src/index.js'
  ],
  output: {
    path: path.resolve(__dirname, 'build'),
    filename: 'main.js',
    libraryTarget: 'umd',
  },
  module: {
    rules: [
      {
        test: /\.(js|jsx)$/,
        use: 'babel-loader',
        exclude: /node_modules/
      },
      {
        test: /\.svg$/,
        loader: 'svg-inline-loader'
      }
    ]
  },
  resolve: {
    extensions: [
      '.js',
      '.jsx'
    ],
    alias: {
      components: path.resolve(__dirname, 'src/components'),
      constants: path.resolve(__dirname, 'src/constants'),
      hooks: path.resolve(__dirname, 'src/hooks'),
    },
  },
};

module.exports = config;
