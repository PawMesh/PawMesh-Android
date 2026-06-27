# PawMesh Android

PawMesh Android client repository.

PawMesh is a pet walking social app that helps nearby dogs and guardians meet through a map-based walking flow.

## Role

- Android UI and navigation
- Photo selection for AI dog profile generation
- Map screen and nearby dog markers
- Walk request, walk session, friend list, notifications, and my page UI
- REST API integration with the Spring Boot backend

## Related Repositories

- Backend: https://github.com/PawMesh/PawMesh-Backend
- Docs hub: https://github.com/PawMesh/Paw_Mesh

## Suggested Structure

```txt
PawMesh-Android/
├─ app/              # Android application source
├─ docs/             # API notes for Android integration
└─ .github/          # Issue and PR templates
```

## MVP API Flow

1. Anonymous login: `POST /auth/anonymous`
2. AI dog profile generation: `POST /dogs/ai-profile`
3. Save dog profile: `POST /dogs/me`
4. Save owner profile: `POST /owners/me`
5. Start walk: `POST /walk/start`
6. Nearby dogs: `GET /dogs/nearby`
7. Send walk request: `POST /walk-requests`
8. Friends: `GET /friends`

## Branch Convention

```txt
feat/{feature-name}
fix/{bug-name}
chore/{task-name}
docs/{doc-name}
```

## Commit Convention

```txt
feat: add nearby dog map screen
fix: handle empty friend list
chore: configure Android project
docs: update API contract
```
