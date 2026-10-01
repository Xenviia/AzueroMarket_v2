AzueroMarket v2

AzueroMarket is an Android marketplace application designed to connect local entrepreneurs with customers through a digital platform.

The application provides separate experiences for customers and entrepreneurs, allowing customers to browse products, manage a shopping cart, place orders, and communicate with product owners. Entrepreneurs can manage their products, monitor sales information, manage stock, and respond to customer inquiries.

The project was developed as part of my university studies, focusing on Android application development, user interface design, application state management, database integration, and client-side marketplace functionality.

OVERVIEW

AzueroMarket is designed as a digital marketplace for local entrepreneurs.

The application has two main user roles:

Customer

Customers can browse available products, view product details and stock, add products to their cart, confirm orders, and communicate with entrepreneurs through the messaging system.

Entrepreneur

Entrepreneurs can manage their products and stock, view basic sales information, and respond to customer messages.

FEATURES

Customer

* User registration and login
* Product catalog
* Product categories
* Product filtering
* Product details
* Real-time stock display
* Related products from the same entrepreneur
* Shopping cart
* Quantity management
* Order confirmation
* Product-related conversations
* Conversation list
* Customer-to-entrepreneur messaging

Entrepreneur

* Entrepreneur login
* Product management
* Product stock management
* Sales statistics
* Add product functionality
* Customer message management
* Product-related conversations

ORDER AND PRICING

The application includes a service charge and ITBMS calculation during the checkout process.

The current model uses:

* Product price
* 5% service charge
* 7% ITBMS

The product price is treated separately from the service charge, with the service charge and applicable tax added to the customer's total.

TECHNOLOGIES

Mobile Application

* Kotlin
* Android Studio
* Android SDK
* XML
* Gradle

Database and Backend

* Supabase
* SQL
* Supabase Realtime

Local Application Components

* SharedPreferences
* Local mock data source
* In-memory cart management

APPLICATION ARCHITECTURE

The application is organized into several layers for models, utilities, and user interface components.

A simplified structure is:

AzueroMarket
|
├── Models
|
├── Utilities
|   ├── Session Management
|   ├── Mock Data Source
|   └── Cart Management
|
└── UI
├── Login
├── Registration
├── Customer Home
├── Entrepreneur Home
├── Product Details
├── Shopping Cart
├── Chat
├── Customer
└── Entrepreneur

PROJECT STRUCTURE

app/src/main/java/com/azueromarket/

├── AzueroMarketApp.kt
├── model/
│   └── Models.kt
├── utils/
│   ├── SessionManager.kt
│   ├── MockDataSource.kt
│   └── CarritoManager.kt
└── ui/
├── login/
├── register/
├── home/
├── producto/
├── carrito/
├── chat/
├── cliente/
└── emprendedor/

USER FLOW

Customer

Login
|
v
Home
|
v
Product Catalog
|
v
Product Details
|
├── Available Stock → Add to Cart → Confirm Order
|
└── No Stock → Contact Entrepreneur

Messages
|
v
Conversations
|
v
Chat

Entrepreneur

Login
|
v
My Store
|
v
Product Management
|
v
Stock Management

Messages
|
v
Customer Conversations
|
v
Reply to Customer

DATABASE

The project includes a Supabase database setup script through SUPABASE_SETUP.sql.

The database structure is designed to support functionality such as:

* Users
* Products
* Orders
* Conversations
* Messages

Supabase Realtime is also prepared for communication features involving messages, conversations, and orders.

CURRENT DEVELOPMENT STATUS

The application currently uses a local mock data source for part of its functionality while the Supabase integration is being developed.

The project structure is prepared for replacing local mock data with real Supabase queries and expanding the application's real-time functionality.

FUTURE IMPROVEMENTS

Planned improvements include:

* Complete integration with Supabase data operations
* Real-time chat using Supabase Realtime
* Product image uploads using Supabase Storage
* Push notifications using Firebase Cloud Messaging
* Customer and entrepreneur order history
* Product search
* Product ratings and reviews
* Online payment integration

PROJECT GOALS

This project was developed to gain practical experience in:

* Android application development with Kotlin
* Mobile user interface development
* Role-based application flows
* State management
* Shopping cart implementation
* Database integration
* Authentication and session management
* Real-time communication
* Git and GitHub version control

AUTHOR

Hilary Rodríguez

Software Development Student
