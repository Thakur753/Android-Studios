# Blinkit Clone - Professional Features Walkthrough

I have implemented high-impact, real-time features and a smart AI chatbot to give your project a premium look for your college submission.

## Implemented Features

### 1. Real-Time "10-Min Delivery" Countdown
- **Home Screen:** A live ticking timer in the Top Bar shows the delivery estimate.
- **Order Tracking:** When an order is placed, the countdown continues on the tracking screen.
- **Logic:** Handled in `HomeViewModel` using a coroutine-based timer.

### 2. Live Stock Listener
- **Product Cards:** Added a "Low Stock" badge (e.g., "Only 3 left!") that updates in real-time.
- **Technology:** Uses Firebase Realtime Database's `ValueEventListener` (acting as a Snapshot listener) to push updates instantly.

### 3. Voice Search Integration
- **Search Bar:** Integrated a Microphone icon inside the `CustomSearchBar`.
- **Functionality:** Uses Android's `SpeechRecognizer` to capture voice input and filter products immediately.

### 4. Shimmer Loading Effect
- **Animation:** Replaced generic progress bars with a professional shimmer animation using `rememberInfiniteTransition`.
- **User Experience:** Appears while categories and products are fetching from Firebase.

### 5. AI Customer Support Chatbot
- **Entry Point:** A Floating Action Button (FAB) on the Home screen.
- **Smart Logic:** A keyword-matching engine in `HomeViewModel` that understands "order", "refund", "coupon", etc.
- **Bot Behavior:** Includes a "Bot is typing..." simulation to mimic real-time processing.
- **UI:** Modern chat bubbles with smooth auto-scrolling to the latest message.

## Code References

- **Business Logic:** [HomeViewModel.kt](file:///C:/Users/LENOVO/AndroidStudioProjects/BlinkitClone/app/src/main/java/com/example/blinkitclone/viewmodels/HomeViewModel.kt)
- **Home UI Features:** [HomeScreen.kt](file:///C:/Users/LENOVO/AndroidStudioProjects/BlinkitClone/app/src/main/java/com/example/blinkitclone/screens/HomeScreen.kt)
- **Chat & Tracking UI:** [MainScreen.kt](file:///C:/Users/LENOVO/AndroidStudioProjects/BlinkitClone/app/src/main/java/com/example/blinkitclone/screens/MainScreen.kt)

## How to Verify (For Viva)
1. **Chatbot:** Open Chat, type "coupon" -> Bot replies with code.
2. **Stock Update:** Change `stockCount` to `3` for any product in the Firebase Console -> App shows "Only 3 left!".
3. **Voice Search:** Click Mic -> Speak "Milk" -> Search bar fills automatically.
