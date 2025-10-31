# Bank Simulator

This repository contains a Java (Jersey/Maven) backend and a React + Vite frontend for a small bank simulator used for learning and testing.

Contents
- `src/` - Java backend sources (Jersey resources, services, DAO)
- `frontend/` - React + Vite frontend
- `pom.xml` - Maven build file

Quick start (local)

1) Create DB and run migrations

 - Ensure MySQL is running and `application.properties` has correct JDBC settings.
 - Run the `DBConfig` main to create database/tables, or use the provided `sample_data.sql` in `src/main/java/model`.

2) Build backend

```powershell
# from project root
.\mvnw.cmd clean package -DskipTests
```

Deploy the produced `target/bank-simulator.war` to your servlet container (Tomcat) or run from your IDE.

3) Start frontend (dev)

```powershell
cd frontend
npm install
# set API base if backend is not on same host/port
# create frontend/.env with VITE_API_BASE=http://localhost:8080/bank-simulator/api
npm run dev
```

Troubleshooting
- If the frontend receives HTML when expecting JSON, verify `VITE_API_BASE` points to the backend URL (example: `http://localhost:8080/bank-simulator/api`) or add a Vite proxy in `frontend/vite.config.js`.
- If you see JSON parse errors when calling endpoints from PowerShell, use `Invoke-RestMethod` or `curl.exe` (not the PowerShell curl alias) and ensure request body is valid JSON.

Create a GitHub repo and push (suggested)

Option A (recommended, using GitHub CLI):

```powershell
# Install GitHub CLI (gh) and authenticate first: gh auth login
# From project root
git init
git branch -M main
git add .
git commit -m "Initial commit"
# create remote and push
gh repo create <your-username>/bank-simulator --public --source=. --remote=origin --push
```

Option B (manual via GitHub website):

 - Create a new repo on github.com.
 - Then add remote and push:

```powershell
git remote add origin https://github.com/<your-username>/bank-simulator.git
git push -u origin main
```

CI
- A GitHub Actions workflow is included at `.github/workflows/ci.yml` that builds the Java backend and runs a frontend build on push.

If you want, I can also open a PR template, CONTRIBUTING.md, or create the remote on your behalf if you provide a GitHub token — otherwise follow the steps above to push.
