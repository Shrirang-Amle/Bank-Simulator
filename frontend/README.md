Bank Simulator — Frontend

This is a small React + Vite admin interface for the Bank Simulator backend in the parent project.

Quick start

1. Install dependencies

   npm install

2. Run the dev server

   npm run dev

3. Open http://localhost:5173

Login

This frontend uses a simple backend `/auth/login` endpoint. Use the credentials: admin / Adm1n$ecure!2025

Note about "breached password" warnings

- Modern browsers (Chrome, Edge, Firefox) can show a "password found in a data breach" message when you type a commonly-seen password. That warning comes from the browser's password manager and indicates the specific string you typed has been observed in public leaks elsewhere — it does not mean this application has been breached. To avoid seeing that warning while developing, use the strong developer password above or change it in `src/services/api.js` and `src/pages/Login.jsx` to a value you prefer.

Notes and assumptions

- The backend API base is expected under `/api` when the frontend is served from the same host as the backend. If you run the frontend on a different host/port (e.g., `vite` dev server), set `VITE_API_BASE` in a `.env` file in this folder, e.g.: `VITE_API_BASE=http://localhost:8080/bank-simulator/api`

- The backend currently has no authentication endpoints. The frontend uses a mocked login and stores a token in localStorage for gating admin routes only.

- If you see CORS errors when the frontend (dev server) calls the backend, enable CORS in the Java app (Jersey) or run the frontend proxying through the backend host. Example for Jersey: add a CORS filter that sets `Access-Control-Allow-Origin: *` for dev.

Next steps

- Add a real login API and verify JWT/session handling.
- Improve styles and UX, add pagination and better error handling.
- Add e2e tests (Cypress) and unit tests for components.
