// vite.config.ts
import { defineConfig } from 'vite';

export default defineConfig({
  server: {
    hmr: false,
    watch: {
      usePolling: true,
    },
    fs: {
      strict: false
    }
  },
  optimizeDeps: {
    force: true
  }
});
