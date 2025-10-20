import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    // Podešavanje proxy-ja za rešavanje CORS problema tokom razvoja
    proxy: {
      // Svi zahtevi koji počinju sa /api biće preusmereni na backend server
      '/api': {
        target: 'http://localhost:8080', // URL vašeg Spring Boot backend-a
        changeOrigin: true, // Neophodno za virtuelne hostove
        // Ne treba nam rewrite, jer su nam putanje na frontendu i backendu iste (/api/...)
      }
    }
  }
});
