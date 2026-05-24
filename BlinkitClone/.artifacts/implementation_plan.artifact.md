# High-Impact Features and AI Customer Support Chatbot

This plan outlines the implementation of several professional, real-time features and an AI-simulated chatbot for the Blinkit Clone project using Jetpack Compose.

## Proposed Changes

### [Component: UI - Screens]

#### [HomeScreen.kt](file:///C:/Users/LENOVO/AndroidStudioProjects/BlinkitClone/app/src/main/java/com/example/blinkitclone/screens/HomeScreen.kt)
- Add a countdown timer to the `TopBarSection`.
- Add a Voice Search icon to the `CustomSearchBar` and integrate `SpeechRecognizer`.
- Update `ProductItemUI` to show a "Low Stock" badge based on real-time Firestore data.
- Add a Floating Action Button (FAB) for the Support Chat.
- Implement a shimmer loading effect placeholder for product sections.

#### [MainScreen.kt](file:///C:/Users/LENOVO/AndroidStudioProjects/BlinkitClone/app/src/main/java/com/example/blinkitclone/screens/MainScreen.kt)
- Add a countdown timer to the `OrderTrackingUI`.
- Add `SupportChatScreen` for the AI chatbot interface.

### [Component: ViewModels]

#### [HomeViewModel.kt](file:///C:/Users/LENOVO/AndroidStudioProjects/BlinkitClone/app/src/main/java/com/example/blinkitclone/viewmodels/HomeViewModel.kt)
- Implement `Firestore` SnapshotListener for real-time stock updates.
- Add logic for the keyword-matching Chatbot engine.
- Manage countdown timer state.

## Verification Plan

### Manual Verification
- **Countdown Timer**: Open the Home screen or place an order to see the 10-minute timer ticking down.
- **Real-Time Stock**: Modify `stock_count` in Firestore console and verify the "Only X left!" badge updates instantly in the app.
- **Voice Search**: Click the microphone icon, speak a product name, and verify it populates the search bar.
- **Shimmer Loading**: Clear app cache or simulate slow network to see the shimmer effect before products load.
- **AI Chatbot**: Click the Chat FAB, send messages with keywords like "order", "coupon", "refund", and verify the smart responses and "typing..." animation.
