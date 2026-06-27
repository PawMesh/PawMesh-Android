# Android Convention

## Package

Use a package name under:

```txt
com.pawmesh.app
```

## Networking

- Keep API request and response DTOs close to API clients.
- Do not hardcode production URLs in source files.
- Handle loading, success, empty, and error states on every API screen.

## UI

- Build MVP screens first with mock data.
- Replace mock data with API calls screen by screen.
- Keep map markers and bottom sheets lightweight for the hackathon demo.
