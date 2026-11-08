# Silent Pulse

Standalone Android companion panel for Minecraft Bedrock.

## Project
- Kotlin Android app
- Floating overlay with draggable header
- Four tabs: Combat, Visuals, Cosmetics and Misc
- Launch installed Minecraft from the home screen
- GitHub Actions builds a debug APK and uploads it as an artifact

## Build
Open this repository in Android Studio or run:
`gradle :app:assembleDebug`

Requires JDK 17, Android SDK platform 35 and Gradle 8.7.

## Important scope
All feature switches are local interface previews. This project does not inject into Minecraft, read or modify game memory, alter packets, automate PvP actions, or bypass anti-cheat.
