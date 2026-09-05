<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>

<%
    String checkIn = (String) request.getAttribute("checkIn");
    String checkOut = (String) request.getAttribute("checkOut");

    List<String> rooms =
        (List<String>) request.getAttribute("rooms");
%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Available Rooms | StayFinder</title>

    <style>

        /* =========================
           GLOBAL
        ========================== */

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family:
                "Segoe UI",
                Arial,
                sans-serif;

            background: #f4f7fb;
            color: #1f2937;
            min-height: 100vh;
        }


        /* =========================
           NAVIGATION
        ========================== */

        nav {
            height: 72px;

            background:
                linear-gradient(
                    135deg,
                    #0f2f52,
                    #174a78
                );

            display: flex;
            align-items: center;
            justify-content: space-between;

            padding: 0 7%;

            color: white;

            box-shadow:
                0 3px 15px
                rgba(0, 0, 0, 0.12);

            position: sticky;
            top: 0;
            z-index: 1000;
        }


        .logo {
            display: flex;
            align-items: center;
            gap: 9px;

            font-size: 23px;
            font-weight: 700;
            letter-spacing: 0.3px;
        }


        .nav-links {
            display: flex;
            align-items: center;
            gap: 30px;
        }


        .nav-links a {
            color: white;
            text-decoration: none;

            font-size: 15px;
            font-weight: 500;

            transition:
                color 0.25s ease,
                transform 0.25s ease;
        }


        .nav-links a:hover {
            color: #fbbf24;
            transform: translateY(-1px);
        }


        /* =========================
           HERO HEADER
        ========================== */

        .page-header {
            background:
                linear-gradient(
                    rgba(15, 47, 82, 0.92),
                    rgba(23, 74, 120, 0.92)
                );

            color: white;

            padding: 55px 20px 65px;

            text-align: center;
        }


        .page-header h1 {
            font-size: 38px;
            margin-bottom: 12px;

            letter-spacing: 0.3px;
        }


        .page-header p {
            font-size: 16px;
            color: #dbeafe;
        }


        /* =========================
           SEARCH SUMMARY
        ========================== */

        .search-summary {
            width: 90%;
            max-width: 1050px;

            margin: -28px auto 45px;

            background: white;

            border-radius: 14px;

            padding: 20px 25px;

            display: flex;
            align-items: center;
            justify-content: space-between;

            box-shadow:
                0 8px 30px
                rgba(0, 0, 0, 0.10);
        }


        .date-item {
            display: flex;
            flex-direction: column;
            gap: 5px;
        }


        .date-label {
            font-size: 12px;
            text-transform: uppercase;

            color: #64748b;

            letter-spacing: 0.8px;
            font-weight: 600;
        }


        .date-value {
            font-size: 16px;
            font-weight: 600;
            color: #12355b;
        }


        .search-again {
            background: #d97706;
            color: white;

            text-decoration: none;

            padding: 11px 20px;

            border-radius: 8px;

            font-size: 14px;
            font-weight: 600;

            transition:
                background 0.25s ease,
                transform 0.25s ease;
        }


        .search-again:hover {
            background: #b85f00;
            transform: translateY(-2px);
        }


        /* =========================
           MAIN CONTAINER
        ========================== */

        .container {
            width: 90%;
            max-width: 1100px;

            margin: 0 auto 70px;
        }


        .results-heading {
            display: flex;
            justify-content: space-between;
            align-items: center;

            margin-bottom: 25px;
        }


        .results-heading h2 {
            color: #12355b;
            font-size: 25px;
        }


        .results-count {
            color: #64748b;
            font-size: 14px;
        }


        /* =========================
           ROOM GRID
        ========================== */

        .rooms {
            display: grid;

            grid-template-columns:
                repeat(
                    auto-fit,
                    minmax(300px, 1fr)
                );

            gap: 25px;
        }


        /* =========================
           ROOM CARD
        ========================== */

        .room-card {
            background: white;

            border-radius: 16px;

            overflow: hidden;

            box-shadow:
                0 6px 25px
                rgba(15, 47, 82, 0.08);

            border:
                1px solid #e5e7eb;

            transition:
                transform 0.3s ease,
                box-shadow 0.3s ease;
        }


        .room-card:hover {
            transform: translateY(-6px);

            box-shadow:
                0 14px 35px
                rgba(15, 47, 82, 0.14);
        }


        /* =========================
           ROOM IMAGE AREA
        ========================== */

        .room-image {
            height: 190px;

            background:
                linear-gradient(
                    rgba(15, 47, 82, 0.25),
                    rgba(15, 47, 82, 0.25)
                ),
                url("images/hotel-room.jpg");

            background-size: cover;
            background-position: center;

            position: relative;
        }


        .available-badge {
            position: absolute;

            top: 15px;
            right: 15px;

            background: #16a34a;

            color: white;

            padding: 6px 12px;

            border-radius: 20px;

            font-size: 12px;
            font-weight: 600;
        }


        /* =========================
           ROOM CONTENT
        ========================== */

        .room-content {
            padding: 23px;
        }


        .room-title {
            display: flex;
            justify-content: space-between;
            align-items: center;

            margin-bottom: 12px;
        }


        .room-title h3 {
            color: #12355b;
            font-size: 20px;
        }


        .room-number {
            font-size: 12px;
            color: #64748b;

            background: #f1f5f9;

            padding: 5px 9px;

            border-radius: 6px;
        }


        .room-details {
            display: flex;
            flex-wrap: wrap;

            gap: 8px;

            margin: 15px 0;
        }


        .detail {
            background: #f8fafc;

            border: 1px solid #e2e8f0;

            padding: 7px 10px;

            border-radius: 7px;

            font-size: 13px;

            color: #475569;
        }


        .room-description {
            color: #64748b;

            font-size: 14px;

            line-height: 1.6;

            margin-bottom: 20px;
        }


        /* =========================
           PRICE
        ========================== */

        .room-footer {
            display: flex;

            justify-content: space-between;
            align-items: center;

            padding-top: 17px;

            border-top:
                1px solid #e5e7eb;
        }


        .price-section {
            display: flex;
            flex-direction: column;
        }


        .price {
            color: #12355b;

            font-size: 23px;

            font-weight: 700;
        }


        .per-night {
            font-size: 12px;
            color: #64748b;

            margin-top: 2px;
        }


        /* =========================
           BOOK BUTTON
        ========================== */

        .book-btn {
            display: inline-block;

            background:
                linear-gradient(
                    135deg,
                    #12355b,
                    #174a78
                );

            color: white;

            text-decoration: none;

            padding: 11px 20px;

            border-radius: 8px;

            font-size: 14px;
            font-weight: 600;

            transition:
                transform 0.25s ease,
                box-shadow 0.25s ease;
        }


        .book-btn:hover {
            transform: translateY(-2px);

            box-shadow:
                0 6px 15px
                rgba(18, 53, 91, 0.25);
        }


        /* =========================
           NO ROOMS
        ========================== */

        .no-rooms {
            background: white;

            border-radius: 16px;

            padding: 60px 30px;

            text-align: center;

            box-shadow:
                0 6px 25px
                rgba(0,0,0,0.07);
        }


        .no-rooms-icon {
            font-size: 50px;
            margin-bottom: 15px;
        }


        .no-rooms h2 {
            color: #12355b;
            margin-bottom: 10px;
        }


        .no-rooms p {
            color: #64748b;
            margin-bottom: 25px;
        }


        /* =========================
           FOOTER
        ========================== */

        footer {
            background: #0f2f52;

            color: #dbeafe;

            text-align: center;

            padding: 28px;

            font-size: 14px;
        }


        /* =========================
           RESPONSIVE
        ========================== */

        @media (max-width: 700px) {

            nav {
                padding: 0 5%;
            }


            .nav-links {
                gap: 15px;
            }


            .page-header h1 {
                font-size: 30px;
            }


            .search-summary {
                flex-direction: column;

                align-items: flex-start;

                gap: 18px;
            }


            .search-again {
                width: 100%;
                text-align: center;
            }


            .results-heading {
                flex-direction: column;

                align-items: flex-start;

                gap: 7px;
            }


            .rooms {
                grid-template-columns: 1fr;
            }

        }

    </style>

</head>


<body>


    <!-- =========================
         NAVIGATION
    ========================== -->

    <nav>

        <div class="logo">
            🏨 StayFinder
        </div>


        <div class="nav-links">

            <a href="index.html">
                Home
            </a>

            <a href="login.html">
                Login
            </a>

            <a href="register.html">
                Register
            </a>

        </div>

    </nav>



    <!-- =========================
         PAGE HEADER
    ========================== -->

    <section class="page-header">

        <h1>
            Available Rooms
        </h1>

        <p>
            Find the perfect room for your stay
        </p>

    </section>



    <!-- =========================
         SEARCH SUMMARY
    ========================== -->

    <div class="search-summary">

        <div class="date-item">

            <span class="date-label">
                Check-in
            </span>

            <span class="date-value">
                <%= checkIn %>
            </span>

        </div>


        <div class="date-item">

            <span class="date-label">
                Check-out
            </span>

            <span class="date-value">
                <%= checkOut %>
            </span>

        </div>


        <a
            href="index.html"
            class="search-again">

            Modify Search

        </a>

    </div>



    <!-- =========================
         ROOM RESULTS
    ========================== -->

    <main class="container">


        <div class="results-heading">

            <h2>
                Recommended Rooms
            </h2>


            <span class="results-count">

                <%
                    if (rooms != null) {
                %>

                    <%= rooms.size() %> room(s) available

                <%
                    }
                %>

            </span>

        </div>



        <div class="rooms">


            <%

                if (rooms != null && !rooms.isEmpty()) {

                    for (String room : rooms) {

                        /*
                         * Current RoomDAO returns:
                         *
                         * room_id - room_number -
                         * room_type - price_per_night
                         */

                        String[] roomData =
                            room.split(" - ", 4);

                        String roomId =
                            roomData.length > 0
                            ? roomData[0]
                            : "";

                        String roomNumber =
                            roomData.length > 1
                            ? roomData[1]
                            : "";

                        String roomType =
                            roomData.length > 2
                            ? roomData[2]
                            : "";

                        String price =
                            roomData.length > 3
                            ? roomData[3]
                            : "";

            %>


                <article class="room-card">


                    <!-- Room Image -->

                    <div class="room-image">

                        <span class="available-badge">
                            Available
                        </span>

                    </div>



                    <!-- Room Information -->

                    <div class="room-content">


                        <div class="room-title">

                            <h3>
                                <%= roomType %> Room
                            </h3>

                            <span class="room-number">
                                Room <%= roomNumber %>
                            </span>

                        </div>



                        <div class="room-details">

                            <span class="detail">
                                🛏️ Comfortable Stay
                            </span>

                            <span class="detail">
                                🛁 Private Bathroom
                            </span>

                            <span class="detail">
                                📶 Free Wi-Fi
                            </span>

                        </div>



                        <p class="room-description">

                            Enjoy a comfortable and relaxing
                            stay with modern facilities and
                            convenient services.

                        </p>



                        <!-- Price + Booking -->

                        <div class="room-footer">


                            <div class="price-section">

                                <span class="price">

                                    <%= price %>

                                </span>

                                <span class="per-night">
                                    per night
                                </span>

                            </div>


                            <a
                                href="booking.html"
                                class="book-btn">

                                Book Now

                            </a>


                        </div>


                    </div>

                </article>


            <%

                    }

                } else {

            %>


                <div class="no-rooms">


                    <div class="no-rooms-icon">
                        🏨
                    </div>


                    <h2>
                        No Rooms Available
                    </h2>


                    <p>
                        We couldn't find an available room
                        for your selected dates.
                    </p>


                    <a
                        href="index.html"
                        class="search-again">

                        Search Again

                    </a>


                </div>


            <%

                }

            %>


        </div>

    </main>



    <!-- =========================
         FOOTER
    ========================== -->

    <footer>

        <p>
            © 2026 StayFinder Hotel Booking System
        </p>

    </footer>


</body>

</html>