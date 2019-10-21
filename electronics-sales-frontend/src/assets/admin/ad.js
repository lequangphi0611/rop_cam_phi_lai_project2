$(function() {
    function slideMenu() {
      var activeState = $("#menu-container .menu-list").hasClass("active");
      $("#menu-container .menu-list").animate({left: activeState ? "0%" : "-100%"}, 400);
    }
    $("#menu-wrapper").click(function(event) {
      event.stopPropagation();
      $("#hamburger-menu").toggleClass("open");
      $("#menu-container .menu-list").toggleClass("active");
      slideMenu();
  
      $("body").toggleClass("overflow-hidden");
    });
  
    $(".menu-list").find(".accordion-toggle").click(function() {
      $(this).next().toggleClass("open").slideToggle("fast");
      $(this).toggleClass("active-tab").find(".menu-link").toggleClass("active");
  
      $(".menu-list .accordion-content").not($(this).next()).slideUp("fast").removeClass("open");
      $(".menu-list .accordion-toggle").not(jQuery(this)).removeClass("active-tab").find(".menu-link").removeClass("active");
    });
  }); 

// begin Biểu đồ
  	// bar chart data
	var barData = {
    labels : ["January","February","March","April","May","June"],
    datasets : [
      {
        fillColor : "#48A497",
        strokeColor : "#48A4D1",
        data : [456,479,324,569,702,600]
      },
      {
        fillColor : "rgba(73,188,170,0.4)",
        strokeColor : "rgba(72,174,209,0.4)",
        data : [364,504,605,400,345,320]
      }
      ]
    }
    
    // get bar chart canvas
    var income = document.getElementById("income").getContext("2d");
    
    // draw bar chart
    new Chart(income).Bar(barData);
    //finish