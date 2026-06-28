# Firl - Modern Dating & Social App

Firl is a premium Android dating application built with a focus on high-quality matching, lifestyle discovery, and a modern user interface. It combines the familiar "swipe" interaction with interest-based discovery to help users find meaningful connections.

## 🚀 Key Features

- **Advanced Matching Engine**: Uses the Haversine formula for precise geographic proximity and a weighted scoring system based on interests, activity level, and profile completeness.
- **Lifestyle Discovery**: Browse users by specific interests and lifestyle goals (e.g., "Long Term Relationship", "Travel Partner", "Techie").
- **Modern Chat Interface**: A polished messaging experience with real-time feedback, typing indicators, and a contemporary UI.
- **Dynamic User Profiles**: Interactive profiles with "Profile Strength" tracking and customizable lifestyle tags.
- **High-Contrast Dark Theme**: Designed with accessibility and aesthetics in mind, using Material 3 principles.

## 🛠 Technical Stack

- **Platform**: Android (Java/Kotlin)
- **UI Framework**: Material Design 3 (M3)
- **Image Loading**: Glide
- **Data Persistence**: Firebase Realtime Database
- **Authentication**: Firebase Auth
- **Utilities**: Timber (Logging), ButterKnife (View Binding)

## 📐 Matching Logic Breakdown

The compatibility score is calculated using the following weights:
- **30% Shared Interests**: Based on case-insensitive overlap of user tags.
- **25% Location**: Normalized geographic distance using the Haversine formula.
- **20% Age Preference**: Proximity to the user's ideal age range.
- **15% Activity Level**: Matching based on how active both users are in the app.
- **10% Profile Completeness**: Rewarding users who provide more information.

## 📦 Project Structure

- `app/src/main/java/com/Denzo/firl/`:
    - `Discover/`: Interest-based search and profile previews.
    - `Likes/`: The core swiping interface and matching logic.
    - `chat/`: Real-time messaging implementation.
    - `Model/`: Data models (User, MatchPerson, etc.) and repositories.
    - `Profile/`: User account management and preferences.
    - `Utils/`: Core engines like the `MatchingEngine`.

## 🤝 Getting Started

1. Clone the repository.
2. Open in Android Studio (Ladybug or newer recommended).
3. Connect your Firebase project (or use the built-in Mock repositories for testing).
4. Run on an emulator or physical device (Min SDK 26).

---

Developed with ❤️ for the Firl Community.
