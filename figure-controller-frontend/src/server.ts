import express from 'express';
import { fileURLToPath } from 'node:url';
import { dirname, resolve } from 'node:path';
import { isMainModule } from '@angular/ssr/node';

const serverDistFolder = dirname(fileURLToPath(import.meta.url));
const browserDistFolder = resolve(serverDistFolder, '../browser');

const app = express();

/** Serve static browser files */
app.use(
  express.static(browserDistFolder, {
    maxAge: '1y',
    index: false,
    redirect: false,
  })
);

/** Serve index.html for all routes (client-side routing) */
app.get('*', (req, res) => {
  res.sendFile(resolve(browserDistFolder, 'index.html'));
});

/** Start server */
if (isMainModule(import.meta.url)) {
  const port = process.env['PORT'] || 4000;
  app.listen(port, () =>
    console.log(`✔ Server running on http://localhost:${port}`)
  );
}

export default app;

// import express from 'express';
// import { fileURLToPath } from 'node:url';
// import { dirname, resolve } from 'node:path';
// import { isMainModule } from '@angular/ssr/node';
// import { createNodeRequestHandler } from '@angular/ssr/node';  // ← Add this import

// const serverDistFolder = dirname(fileURLToPath(import.meta.url));
// const browserDistFolder = resolve(serverDistFolder, '../browser');

// const app = express();

// /** Serve static browser files */
// app.use(
//   express.static(browserDistFolder, {
//     maxAge: '1y',
//     index: false,
//     redirect: false,
//   })
// );

// /** Serve index.html for all routes (client-side routing) */
// app.get('*', (req, res) => {
//   res.sendFile(resolve(browserDistFolder, 'index.html'));
// });

// /** Start server */
// if (isMainModule(import.meta.url)) {
//   const port = process.env['PORT'] || 4000;
//   app.listen(port, () =>
//     console.log(`✔ Server running on http://localhost:${port}`)
//   );
// }

// // ← Add this export (fixes the warning)
// export const reqHandler = createNodeRequestHandler(app);

// export default app;
