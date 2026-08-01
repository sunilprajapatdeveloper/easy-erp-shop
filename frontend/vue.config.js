const { defineConfig } = require("@vue/cli-service");

module.exports = defineConfig({
  /*
   |--------------------------------------------------------------------------
   | Build & Output
   |--------------------------------------------------------------------------
   */
  outputDir: "dist",
  publicPath: "/",
  productionSourceMap: false,

  /*
   |--------------------------------------------------------------------------
   | Disable ESLint (IMPORTANT for Docker build)
   |--------------------------------------------------------------------------
   */
  lintOnSave: false,

  /*
   |--------------------------------------------------------------------------
   | Transpilation
   |--------------------------------------------------------------------------
   */
  transpileDependencies: true,

  /*
   |--------------------------------------------------------------------------
   | Development Server (DEV ONLY)
   |--------------------------------------------------------------------------
   | This is used ONLY when running `npm run serve`
   | It does NOT affect Docker + Nginx production
   |--------------------------------------------------------------------------
   */
  devServer: {
    allowedHosts: "all",
    host: "0.0.0.0",
    port: 8080,

    /*
     | WebSocket config (ngrok / local dev)
     */
    client: {
      webSocketURL: {
        protocol: "wss",
        hostname: "noncondensible-catchingly-beatriz.ngrok-free.dev",
        port: 443,
        pathname: "/ws",
      },
    },

    /*
     | Proxy API during local development
     */
    proxy: {
      "/api": {
        target: "http://localhost:9090",
        changeOrigin: true,
        secure: false,
      },
    },
  },

  /*
   |--------------------------------------------------------------------------
   | Webpack Optimizations
   |--------------------------------------------------------------------------
   */
  configureWebpack: {
    optimization: {
      splitChunks: {
        chunks: "all",
      },
    },
  },
});
