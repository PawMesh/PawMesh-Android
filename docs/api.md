# PawMesh Android API Notes

Base URL is provided by the backend team.

All authenticated requests use:

```txt
Authorization: Bearer {accessToken}
```

## Screen API Summary

| Screen | Action | API |
|---|---|---|
| Welcome | Anonymous login | `POST /auth/anonymous` |
| Dog profile | Generate AI profile | `POST /dogs/ai-profile` |
| Dog profile | Save dog profile | `POST /dogs/me` |
| Owner profile | Save owner profile | `POST /owners/me` |
| Map | Start walk | `POST /walk/start` |
| Map | Load nearby dogs | `GET /dogs/nearby` |
| Map | Load dog detail | `GET /dogs/{dogId}` |
| Walk request | Send request | `POST /walk-requests` |
| Walk request | Received requests | `GET /walk-requests/received` |
| Walk together | Current walk | `GET /walk/current` |
| Walk together | Update location | `PATCH /walk/location` |
| Walk together | End walk | `POST /walk/end` |
| Friends | Friend list | `GET /friends` |
| My page | My data | `GET /me` |

## Location Policy

- Android keeps the selected photo locally until the user taps AI profile generation.
- Android sends location only while a walk session is active.
- The map refreshes nearby dog markers by polling `GET /dogs/nearby`.
- The app stops location updates when the walk ends.
