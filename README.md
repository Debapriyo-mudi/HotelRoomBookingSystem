# 🏨 StayFinder - Hotel Room Booking System

StayFinder is a Java-based web application designed to provide a simple, user-friendly, and professional platform for searching destinations, exploring hotels, checking available rooms, and making hotel room bookings.

## 🚀 Features

- 🔐 User Registration and Login
- 👤 User Profile
- 🔎 Search hotels by destination
- 🌍 1200+ Indian destinations
- 🏨 Multiple hotels for different destinations
- 🛏️ Multiple room types
- 💰 Room price and capacity information
- 📅 Check-in and check-out date selection
- 👨‍👩‍👧 Guest selection
- ✅ Hotel room booking
- 📋 My Bookings section
- 🗃️ MySQL database integration
- 📱 Responsive and professional user interface
- ⚡ Dynamic hotel and room availability
- 🔒 Session-based user authentication

## ⚙️ How the System Works

### 1. User Registration

A new user can create an account by providing the required details.

### 2. User Login

Registered users can log in using their email and password.

### 3. Search Destination

Users can search for a destination or city using the search box.

The system retrieves the corresponding hotels from the MySQL database.

### 4. Explore Hotels

After searching for a destination, available hotels are displayed with information such as:

- Hotel name
- Location
- Description
- Rating
- Hotel image
- Available rooms

### 5. Select a Room

Users can select an available room according to:

- Room type
- Price per night
- Capacity
- Availability

### 6. Make a Booking

Users select their check-in and check-out dates and confirm their booking.

The booking information is stored in the MySQL database.

### 7. View My Bookings

Logged-in users can view their previous and current bookings from the **My Bookings** section.

## 🛠️ Technologies Used

### Frontend
- HTML5
- CSS3
- JavaScript
- JSP

### Backend
- Java
- Jakarta Servlets
- JDBC

### Database
- MySQL 8.0

### Server
- Apache Tomcat 10.1

### Build Tool
- Apache Maven

### IDE
- Eclipse IDE

## 🗄️ Database Structure

The project uses a MySQL database named:

`hotel_booking`

Main tables include:

- `users`
- `destinations`
- `hotels`
- `rooms`
- `bookings`

The database establishes relationships between destinations, hotels, rooms, users, and bookings to manage the complete booking workflow.

## 🔄 Project Workflow

```text
User
  ↓
Registration / Login
  ↓
Search Destination
  ↓
Find Hotels
  ↓
View Available Rooms
  ↓
Select Room
  ↓
Select Check-in / Check-out
  ↓
Confirm Booking
  ↓
Booking Stored in MySQL
  ↓
My Bookings
