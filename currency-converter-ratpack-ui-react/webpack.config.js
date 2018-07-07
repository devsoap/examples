const path = require('path');
module.exports = {
  mode: 'development',
  entry: './src/main/jsx/bootstrap.js',
  output: {
    path: path.resolve(__dirname, 'build', 'resources', 'main', 'static'),
    filename: 'bundle.js'
  },
  module: {
      rules: [
        {
          test: /.*\/jsx\/.*/,
          loader: 'babel-loader',
          query: {
            presets: ['es2015', 'react']
          }
        }
      ]
    },
};
