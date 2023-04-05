'use strict'
// If your port is set to 80,
// use administrator privileges to execute the command line.
// For example, Mac: sudo npm run
const port = 9527 // dev port
const baseUrl = 'http://127.0.0.1:8001/'

// All configuration item explanations can be find in https://cli.vuejs.org/config/
module.exports = {
  /**
   * You will need to set publicPath if you plan to deploy your site under a sub path,
   * for example GitHub Pages. If you plan to deploy your site to https://foo.github.io/bar/,
   * then publicPath should be set to "/bar/".
   * In most cases please use '/' !!!
   * Detail: https://cli.vuejs.org/config/#publicpath
   */
  publicPath: './',
  outputDir: 'admin',
  assetsDir: 'adminStatic',
  productionSourceMap: false,
  devServer: {
    port: port,
    open: true,
    proxy: {
      '/API-BACKEND/': {
        target: baseUrl,
        changeOrigin: true,
        pathRewrite: {
          '^/API-BACKEND/': '/'
        }
      },
    },
  },
}
