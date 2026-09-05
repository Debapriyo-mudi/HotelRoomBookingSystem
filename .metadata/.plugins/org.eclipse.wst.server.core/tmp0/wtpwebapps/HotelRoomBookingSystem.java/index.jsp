<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
jakarta.servlet.http.HttpSession s=request.getSession(false);
boolean logged=s!=null&&s.getAttribute("user_id")!=null;
String name=logged?(String)s.getAttribute("full_name"):"";
String email=logged?(String)s.getAttribute("email"):"";
if(name==null||name.trim().isEmpty())name="User";
if(email==null)email="";
String ctx=request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0">
<title>StayFinder | Find Your Perfect Stay</title>
<link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@600;700&display=swap" rel="stylesheet">

<style>
*{margin:0;padding:0;box-sizing:border-box}
html{scroll-behavior:smooth}
body{font-family:"DM Sans",Arial,sans-serif;color:#172033;background:#f5f7fa;overflow-x:hidden}
a{text-decoration:none}

.navbar{position:absolute;top:0;left:0;width:100%;height:80px;padding:0 7%;display:flex;align-items:center;justify-content:space-between;z-index:100;color:white;border-bottom:1px solid rgba(255,255,255,.15)}
.logo{color:white;font-size:23px;font-weight:700}
.nav-links{display:flex;gap:25px;align-items:center}
.nav-links>a{color:white;font-size:14px}
.nav-links>a:hover{opacity:.8}

.profile-wrapper{position:relative}
.profile-button{display:flex;align-items:center;gap:9px;padding:7px 13px 7px 7px;border:1px solid rgba(255,255,255,.45);border-radius:30px;color:white;cursor:pointer;background:rgba(255,255,255,.1)}
.profile-avatar{width:35px;height:35px;border-radius:50%;background:#f5c36a;color:#17324d;display:flex;align-items:center;justify-content:center;font-weight:700}
.profile-name{max-width:140px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;font-weight:600}
.profile-menu{position:absolute;top:53px;right:0;width:260px;background:white;color:#172033;border-radius:14px;padding:18px;box-shadow:0 15px 40px rgba(0,0,0,.25);display:none}
.profile-menu.show{display:block}
.profile-header{padding-bottom:14px;border-bottom:1px solid #e5eaf0;margin-bottom:10px}
.profile-header strong{display:block;font-size:17px;color:#12355b;margin-bottom:5px}
.profile-header span{display:block;font-size:12px;color:#718096;word-break:break-all}
.profile-menu a{display:block;padding:11px 8px;color:#172033;border-radius:8px;font-size:14px}
.profile-menu a:hover{background:#f1f5f9}
.logout{color:#dc2626!important;border-top:1px solid #e5eaf0;margin-top:8px;padding-top:14px!important}

.hero{min-height:760px;padding:150px 7% 90px;display:flex;align-items:center;background:linear-gradient(90deg,rgba(5,15,28,.93),rgba(5,15,28,.65),rgba(5,15,28,.35)),url("<%=ctx%>/images/hotel-hero.jpg");background-size:cover;background-position:center}
.hero-content{width:100%;max-width:1200px;margin:auto;color:white}
.badge{display:inline-block;padding:9px 17px;border-radius:30px;background:rgba(255,255,255,.12);border:1px solid rgba(255,255,255,.25);margin-bottom:20px;font-size:13px}
.hero h1{max-width:800px;font-family:"Playfair Display",Georgia,serif;font-size:clamp(48px,6vw,78px);line-height:1.05;margin-bottom:20px}
.hero h1 span{color:#f5c36a}
.hero p{max-width:650px;color:#e3e8ef;font-size:17px;line-height:1.7;margin-bottom:38px}

.search-container{width:100%;background:white;padding:10px;border-radius:18px;box-shadow:0 25px 70px rgba(0,0,0,.35)}
.search-box{display:grid;grid-template-columns:1.5fr 1fr 1fr 1.1fr 190px;gap:9px}
.field{min-height:75px;background:#f5f7fa;border:1px solid #e3e8ef;border-radius:12px;padding:12px 15px;display:flex;align-items:center;gap:10px}
.icon{font-size:21px}
.field-content{width:100%}
.field label{display:block;color:#718096;font-size:10px;font-weight:700;text-transform:uppercase;letter-spacing:1px;margin-bottom:5px}
.field input,.field select{width:100%;border:0;outline:0;background:transparent;font:600 14px "DM Sans";color:#172033}
.guest-selects{display:flex;gap:5px}
.guest-selects select{min-width:0}
.search-button{min-height:75px;border:0;border-radius:12px;background:linear-gradient(135deg,#e19a28,#b96808);color:white;font:700 16px "DM Sans";cursor:pointer;padding:0 20px;transition:.25s}
.search-button:hover{transform:translateY(-3px);box-shadow:0 10px 25px rgba(185,104,8,.4)}

.autocomplete{position:relative;width:100%}
.suggestions{position:absolute;left:0;right:0;top:52px;background:white;color:#172033;border-radius:10px;box-shadow:0 12px 30px rgba(0,0,0,.25);overflow:hidden;display:none;z-index:9999}
.suggestion{padding:11px 14px;border-bottom:1px solid #eee;cursor:pointer}
.suggestion:hover{background:#f1f5f9}
.suggestion strong{display:block;font-size:14px}
.suggestion small{color:#718096;font-size:12px}

.trust{background:white;padding:25px 7%;border-bottom:1px solid #e5eaf0}
.trust-grid{max-width:1200px;margin:auto;display:grid;grid-template-columns:repeat(4,1fr);gap:20px}
.trust-item{text-align:center;color:#68778b;font-size:13px}
.trust-item strong{display:block;color:#172033;font-size:15px;margin-bottom:4px}

.section{width:90%;max-width:1200px;margin:85px auto}
.section-title{text-align:center;margin-bottom:10px;font:700 40px "Playfair Display",Georgia,serif;color:#102a43}
.section-subtitle{text-align:center;color:#718096;margin-bottom:40px}

.destinations{display:grid;grid-template-columns:repeat(4,1fr);gap:20px}
.destination{height:280px;position:relative;overflow:hidden;border-radius:18px;background-size:cover;background-position:center;display:flex;align-items:flex-end;transition:.3s}
.destination:hover{transform:translateY(-7px);box-shadow:0 18px 35px rgba(0,0,0,.18)}
.destination:after{content:"";position:absolute;inset:0;background:linear-gradient(transparent 30%,rgba(0,0,0,.88))}
.destination-content{position:relative;z-index:2;color:white;padding:22px}
.destination h3{font:700 27px "Playfair Display",Georgia,serif;margin-bottom:5px}
.destination p{font-size:13px;color:#e5e7eb}

.kolkata{background-image:url("<%=ctx%>/images/kolkata.jpg")}
.goa{background-image:url("<%=ctx%>/images/goa.jpg")}
.jaipur{background-image:url("<%=ctx%>/images/jaipur.jpg")}
.mumbai{background-image:url("<%=ctx%>/images/mumbai.jpg")}
.delhi{background-image:url("<%=ctx%>/images/delhi.jpg")}
.kerala{background-image:url("<%=ctx%>/images/kerala.jpg")}
.agra{background-image:url("<%=ctx%>/images/agra.jpg")}
.varanasi{background-image:url("<%=ctx%>/images/varanasi.jpg")}

.features{display:grid;grid-template-columns:repeat(4,1fr);gap:20px}
.feature{background:white;padding:28px 22px;border-radius:15px;border:1px solid #e5eaf0;text-align:center}
.feature-icon{font-size:30px;margin-bottom:15px}
.feature h3{color:#17324d;margin-bottom:8px}
.feature p{color:#718096;font-size:13px;line-height:1.6}

.cta{width:90%;max-width:1200px;margin:0 auto 80px;padding:55px;border-radius:22px;background:linear-gradient(110deg,#071b30,#174a78);color:white;text-align:center}
.cta h2{font:700 36px "Playfair Display",Georgia,serif;margin-bottom:10px}
.cta p{color:#dce8f5;margin-bottom:22px}
.cta a{display:inline-block;padding:13px 25px;border-radius:9px;background:#f5c36a;color:#17324d;font-weight:700}

footer{background:#071727;color:#9fb2c5;padding:35px 7%;text-align:center;font-size:13px}

@media(max-width:1050px){
.search-box{grid-template-columns:1fr 1fr}
.search-button{min-height:65px}
.destinations{grid-template-columns:1fr 1fr}
.features,.trust-grid{grid-template-columns:1fr 1fr}
}

@media(max-width:650px){
.navbar{padding:0 5%}
.nav-links{gap:8px}
.nav-links>a{display:none}
.profile-name{display:none}
.profile-button{padding:5px}
.hero{padding:130px 5% 60px;min-height:900px}
.hero h1{font-size:48px}
.search-box,.destinations,.features,.trust-grid{grid-template-columns:1fr}
.guest-selects{display:grid;grid-template-columns:1fr 1fr}
.search-button{min-height:65px}
.section{margin:60px auto}
.cta{padding:40px 25px}
}
</style>
</head>

<body>

<header class="navbar">
<a href="<%=ctx%>/index.jsp" class="logo">🏨 StayFinder</a>

<nav class="nav-links">
<a href="<%=ctx%>/index.jsp">Home</a>

<%if(logged){%>
<div class="profile-wrapper">

<div class="profile-button" onclick="toggleProfile()">
<div class="profile-avatar"><%=name.substring(0,1).toUpperCase()%></div>
<span class="profile-name"><%=name%></span>
<span>▼</span>
</div>

<div class="profile-menu" id="profileMenu">
<div class="profile-header">
<strong><%=name%></strong>
<span><%=email%></span>
</div>

<a href="<%=ctx%>/myBookings">📋 My Bookings</a>
<a href="<%=ctx%>/index.jsp">🏠 Home</a>
<a href="<%=ctx%>/logout" class="logout">🚪 Logout</a>
</div>

</div>

<%}else{%>
<a href="<%=ctx%>/login.html">Login</a>
<a href="<%=ctx%>/register.html">Register</a>
<%}%>
</nav>
</header>

<section class="hero" id="hero">
<div class="hero-content">

<div class="badge">✦ Trusted stays across India</div>

<%if(logged){%>
<h1>Welcome,<br><span><%=name%></span></h1>
<p>Welcome back to StayFinder. Find your next comfortable stay and manage your reservations easily.</p>
<%}else{%>
<h1>Find Your <span>Perfect</span><br>Stay</h1>
<p>Discover beautiful hotels, comfortable rooms and memorable stays at destinations you'll love.</p>
<%}%>

<div class="search-container">

<form class="search-box" id="hotelSearchForm">

<div class="field">
<div class="icon">📍</div>
<div class="field-content autocomplete">

<label>Destination</label>

<input
id="location"
name="location"
type="text"
placeholder="Where do you want to go?"
autocomplete="off"
required>

<div class="suggestions" id="suggestions"></div>

</div>
</div>

<div class="field">
<div class="icon">📅</div>
<div class="field-content">
<label>Check-in</label>
<input name="check_in" id="checkIn" type="date" required>
</div>
</div>

<div class="field">
<div class="icon">📅</div>
<div class="field-content">
<label>Check-out</label>
<input name="check_out" id="checkOut" type="date" required>
</div>
</div>

<div class="field">
<div class="icon">👥</div>
<div class="field-content">

<label>Guests</label>

<div class="guest-selects">

<select name="adults">
<option value="1">1 Adult</option>
<option value="2" selected>2 Adults</option>
<option value="3">3 Adults</option>
<option value="4">4 Adults</option>
<option value="5">5 Adults</option>
<option value="6">6 Adults</option>
</select>

<select name="children">
<option value="0" selected>0 Children</option>
<option value="1">1 Child</option>
<option value="2">2 Children</option>
<option value="3">3 Children</option>
<option value="4">4 Children</option>
<option value="5">5 Children</option>
</select>

</div>
</div>
</div>

<button class="search-button" type="submit">Search Hotels →</button>

</form>
</div>
</div>
</section>

<section class="trust">
<div class="trust-grid">

<div class="trust-item">⭐<strong>4.8/5</strong>Average guest rating</div>
<div class="trust-item">🏨<strong>50+ Properties</strong>Comfortable stays</div>
<div class="trust-item">🔒<strong>Secure Booking</strong>Safe experience</div>
<div class="trust-item">💬<strong>24/7 Support</strong>We're here to help</div>

</div>
</section>

<section class="section">

<h2 class="section-title">Popular Destinations</h2>
<p class="section-subtitle">Explore some of India's most loved cities and getaways.</p>

<div class="destinations">

<div class="destination kolkata">
<div class="destination-content">
<h3>Kolkata</h3>
<p>The City of Joy • Culture & Heritage</p>
</div>
</div>

<div class="destination goa">
<div class="destination-content">
<h3>Goa</h3>
<p>Beaches • Sunsets • Relaxation</p>
</div>
</div>

<div class="destination jaipur">
<div class="destination-content">
<h3>Jaipur</h3>
<p>The Pink City • Royal Heritage</p>
</div>
</div>

<div class="destination mumbai">
<div class="destination-content">
<h3>Mumbai</h3>
<p>City of Dreams • Coastal Life</p>
</div>
</div>

<div class="destination delhi">
<div class="destination-content">
<h3>New Delhi</h3>
<p>History • Monuments • Modern India</p>
</div>
</div>

<div class="destination kerala">
<div class="destination-content">
<h3>Kerala</h3>
<p>Backwaters • Nature • Peace</p>
</div>
</div>

<div class="destination agra">
<div class="destination-content">
<h3>Agra</h3>
<p>Taj Mahal • Mughal Heritage</p>
</div>
</div>

<div class="destination varanasi">
<div class="destination-content">
<h3>Varanasi</h3>
<p>Ghats • Spirituality • Tradition</p>
</div>
</div>

</div>
</section>

<section class="section">

<h2 class="section-title">Why Choose StayFinder?</h2>
<p class="section-subtitle">Everything you need for a comfortable hotel booking experience.</p>

<div class="features">

<div class="feature">
<div class="feature-icon">🏨</div>
<h3>Quality Hotels</h3>
<p>Find comfortable rooms for every type of traveller.</p>
</div>

<div class="feature">
<div class="feature-icon">💰</div>
<h3>Great Prices</h3>
<p>Discover stays at prices that fit your budget.</p>
</div>

<div class="feature">
<div class="feature-icon">🔐</div>
<h3>Secure Booking</h3>
<p>Your booking information stays protected.</p>
</div>

<div class="feature">
<div class="feature-icon">⭐</div>
<h3>Easy Experience</h3>
<p>Search, choose and book your room easily.</p>
</div>

</div>
</section>

<section class="cta">
<h2>Ready for your next stay?</h2>
<p>Find your hotel and make your next trip memorable.</p>
<a href="#hero">Start Searching →</a>
</section>

<footer>© 2026 StayFinder • Hotel Room Booking System</footer>

<script>

function toggleProfile(){
const menu=document.getElementById("profileMenu");
if(menu)menu.classList.toggle("show");
}

document.addEventListener("click",function(e){
const wrapper=document.querySelector(".profile-wrapper");
const menu=document.getElementById("profileMenu");

if(wrapper&&menu&&!wrapper.contains(e.target)){
menu.classList.remove("show");
}
});


/* DATE VALIDATION */

const checkIn=document.getElementById("checkIn");
const checkOut=document.getElementById("checkOut");

const today=new Date().toISOString().split("T")[0];

checkIn.min=today;
checkOut.min=today;

checkIn.addEventListener("change",function(){

checkOut.min=checkIn.value;

if(checkOut.value&&checkOut.value<=checkIn.value){
checkOut.value="";
}

});


/* CITY AUTOCOMPLETE */

const locationInput=document.getElementById("location");
const suggestions=document.getElementById("suggestions");

let cities=[];

fetch("<%=ctx%>/destinations")
.then(function(response){

if(!response.ok){
throw new Error("Destination servlet returned "+response.status);
}

return response.json();

})
.then(function(data){

if(Array.isArray(data)){
cities=data;
}

})
.catch(function(error){

console.log("Could not load destinations:",error);

});


locationInput.addEventListener("input",function(){

const typed=this.value.trim().toLowerCase();

suggestions.innerHTML="";

if(typed.length===0){

suggestions.style.display="none";
return;

}


/*
 * Search the loaded city list.
 * Example:
 * KOL → Kolkata, West Bengal
 */

const matches=cities.filter(function(city){

return city.name &&
city.name.toLowerCase().startsWith(typed);

}).slice(0,8);


if(matches.length===0){

suggestions.style.display="none";
return;

}


matches.forEach(function(city){

const item=document.createElement("div");

item.className="suggestion";

const cityName=city.name || "";
const stateName=city.state || "";

item.innerHTML=
"<strong>📍 "+cityName+"</strong>"+
"<small>"+stateName+"</small>";


item.addEventListener("click",function(){

/*
 * Display the full location to the user.
 */
locationInput.value=cityName+", "+stateName;

/*
 * Store ONLY the city name for database search.
 */
locationInput.dataset.city=cityName;

suggestions.style.display="none";

});

suggestions.appendChild(item);

});

suggestions.style.display="block";

});


document.addEventListener("click",function(e){

if(!e.target.closest(".autocomplete")){

suggestions.style.display="none";

}

});


/* HOTEL SEARCH */

document.getElementById("hotelSearchForm").addEventListener("submit",function(e){

e.preventDefault();

let location=locationInput.value.trim();

const selectedCity=locationInput.dataset.city;


/*
 * If user selected a suggestion:
 *
 * Kolkata, West Bengal
 *
 * becomes:
 *
 * Kolkata
 */

if(selectedCity){

location=selectedCity;

}


/*
 * Also handle manually typed:
 *
 * Kolkata, West Bengal
 *
 * just in case.
 */

else if(location.includes(",")){

location=location.split(",")[0].trim();

}


const inDate=checkIn.value;
const outDate=checkOut.value;

const adults=document.querySelector("[name='adults']").value;
const children=document.querySelector("[name='children']").value;


if(!location){

alert("Please enter a destination.");
return;

}

if(!inDate){

alert("Please select your check-in date.");
return;

}

if(!outDate){

alert("Please select your check-out date.");
return;

}

if(outDate<=inDate){

alert("Check-out must be after check-in.");
return;

}


/*
 * IMPORTANT:
 * Only the city is sent to the search servlet.
 *
 * Example:
 * Kolkata, West Bengal
 * ↓
 * location=Kolkata
 */

window.location.href=
"<%=ctx%>/searchHotels"+
"?location="+encodeURIComponent(location)+
"&check_in="+encodeURIComponent(inDate)+
"&check_out="+encodeURIComponent(outDate)+
"&adults="+encodeURIComponent(adults)+
"&children="+encodeURIComponent(children);

});

</script>

</body>
</html>