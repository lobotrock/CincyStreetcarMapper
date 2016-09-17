'use strict';
function showStreetcar() {
    var routes = document.getElementsByClassName("routeTextDir");
    for (var i = 0; i < routes.length; i++) {

        var routeName = new String(routes[i].textContent);

        if(routeName.length > 1 && routeName.indexOf("Streetcar") !== -1){
            routes[i].click();
        }
    }
    document.getElementById("toolsContentLeft").style.display = 'none';

  }