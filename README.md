# LoyaltyCards 💳
Final Android Project for the *Mobile Development* Course at the University of Parma, A.Y. 2021-2022. 

**LoyaltyCards** is a native Android application built with Java, designed to help users manage, store, and utilize their digital loyalty and reward cards. 

## 📱 Features & Capabilities

The application integrates several core functionalities to manage digital cards:
*   **Barcode & QR Code Scanning:** Utilizes the ZXing library and JourneyApps ZXing Embedded for rapid card scanning and generation.
*   **Google Services Integration:** Features extensive Google Play Services support, including Location, Maps, Wallet, Analytics, Identity, and Drive.
*   **Local Data Persistence:** Uses AndroidX Room for robust, offline-capable local database storage of user card data.
*   **Modern UI Components:** Built with AndroidX libraries including ConstraintLayout, RecyclerView, ViewPager2, SwipeRefreshLayout, and Google's Material Design components.
*   **Color Customization:** Integrates the Ambilwarna color picker library, allowing users to customize the appearance of their stored cards.

## 🏗️ Project Architecture

The repository follows a standard Android Gradle build structure:
*   `app/src/main/java/`: Contains the primary Java source code.
*   `app/src/main/res/`: Holds XML layouts, vector drawables, mipmap icons, and UI values.
*   `app/build.gradle`: Application-level build script managing dependencies and versions.
*   `gradle/wrapper/`: Includes the Gradle Wrapper (v7.2) for consistent, localized builds.

## 🛠️ Requirements & Tech Stack

*   **Language:** Java
*   **Build System:** Gradle (v7.2)
*   **Frameworks:** AndroidX, Google Play Services, Google HTTP/OAuth Clients
*   **Testing:** JUnit 4.13, Espresso Core 3.4.0

## 🚀 Getting Started

1. Clone the repository: `git clone https://github.com/<your-username>/LoyaltyCards.git`
2. Open the project in **Android Studio**.
3. Sync the project with Gradle files.
4. Select an Android Emulator or a connected physical device and press **Run** (`Shift + F10`).

## 📄 License

This project is licensed under the [MIT License](LICENSE).
