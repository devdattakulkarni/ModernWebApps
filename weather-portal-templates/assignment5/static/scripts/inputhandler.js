// city = name of the city
// data_label = label that goes with the data
// xaxis_data = number of days
// chart_data = the actual values to plot (temperature, precipitation, snow)
function draw_chart(city, data_label, xaxis_data, chart_data) {

  outputImg = document.getElementById("outputImg");
  newElem = document.createElement("canvas");
  elemId = city + "-" + data_label;
  newElem.setAttribute("id",elemId);

  new Chart(newElem, {
    type: 'line',
    data: {
      labels: xaxis_data,
      datasets: [{
        label: data_label,
	fill: false,
        data: chart_data,
        borderWidth: 1
      }]
    },
    options: {
      responsive: true,
      scales: {
        y: {
	  display: true,
	  title: {
		display: true,
		text: data_label,
          },
          beginAtZero: true
        },
	x: {
	  display: true,
	  title: {
		display: true,
		text: "Day"
	  }
	}
      }
    }
  });

  outputImg.appendChild(newElem);
}

function get_city_status(city) {
      var xhttp;
      xhttp = new XMLHttpRequest();
      xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
         fieldData = JSON.parse(this.responseText);
         console.log(fieldData);
         console.log("-------");
         output_string = "";
         statusString = document.getElementById("status_string");
         statusString.innerHTML = "Output of " + city.id;
         // Clear the previous canvas elements;
         outputImg = document.getElementById("outputImg");
	 outputImg.innerHTML = "";
	 fieldTable = document.createElement("table");
	 fieldTable.setAttribute("class","table");
	 if ('name' in fieldData) {
	 	nameTD = document.createElement("td");
	 	nameTD.innerHTML = "name";
	 	nameValTD = document.createElement("td");
	 	nameValTD.innerHTML = fieldData['name'];
	 	nameRow = document.createElement("tr");
	 	nameRow.appendChild(nameTD)
	 	nameRow.appendChild(nameValTD);
	 	fieldTable.appendChild(nameRow);
	 }
	 if ('month' in fieldData) {
	 	nameTD = document.createElement("td");
	 	nameTD.innerHTML = "month";
	 	nameValTD = document.createElement("td");
	 	nameValTD.innerHTML = fieldData['month'];
	 	nameRow = document.createElement("tr");
	 	nameRow.appendChild(nameTD)
	 	nameRow.appendChild(nameValTD);
	 	fieldTable.appendChild(nameRow);
	 }
	 if ('year' in fieldData) {
	 	nameTD = document.createElement("td");
	 	nameTD.innerHTML = "year";
	 	nameValTD = document.createElement("td");
	 	nameValTD.innerHTML = fieldData['year'];
	 	nameRow = document.createElement("tr");
	 	nameRow.appendChild(nameTD)
	 	nameRow.appendChild(nameValTD);
	 	fieldTable.appendChild(nameRow);
	 }
	 if ('params' in fieldData) {
	 	nameTD = document.createElement("td");
	 	nameTD.innerHTML = "params";
	 	nameValTD = document.createElement("td");
	 	nameValTD.innerHTML = fieldData['params'];
	 	nameRow = document.createElement("tr");
	 	nameRow.appendChild(nameTD)
	 	nameRow.appendChild(nameValTD);
	 	fieldTable.appendChild(nameRow);
	 }
         outputImg.appendChild(fieldTable);

         // Show the image
         document.getElementById("status").setAttribute("style","display:block;");

         //Hide the form
         document.getElementById("newRunFormDiv").setAttribute("style","display:none;");
         document.getElementById("addCityFormDiv").setAttribute("style","display:none;");

	}
      };

      url = "/status?city=" + city.id;
      xhttp.open("GET", url, true);
      xhttp.send();
}


// TODO: Assignment 4 show the graph
function get_city_weather_graphs(city) {

      var xhttp;
      xhttp = new XMLHttpRequest();
      xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {

         fieldData = JSON.parse(this.responseText);
         console.log(fieldData);
         console.log("-------");

	 output_string = "";
	 statusString = document.getElementById("status_string");
	 statusString.innerHTML = "Output of " + city.id;
	 // Clear the previous canvas elements;
	 outputImg = document.getElementById("outputImg");
	 outputImg.innerHTML = '';

	 // Example fieldData: 
         // {'2023-08-TMAX': '411,406,400,406,411,406,411,411,422,417,422,411,411,411,389,400,433,411,400,417,-9999,-9999,-9999', '2023-08-TMIN': '256,261,256,261,256,256,256,256,250,256,261,267,261,250,267,228,250,250,239,256,-9999,-9999,-9999', '2023-08-PRCP': '-9999,-9999,-9999'}
	 // Iterate through the <key, value> pairs and call draw_chart for each 
	 for (const [key, value] of Object.entries(fieldData)) {
  		console.log(`${key}: ${value}`);
		vals = value.split(",");
		values = [];
		xaxis_vals = [];
		count = 1;
		for(const elem of vals) {
			if (elem != "-9999") {
				// Convert the number to decimal
				parts = elem.split("");
				elem1 = parts.slice(0, parts.length-1);
				elem1.push(".");
				elem1.push(parts[parts.length-1]);
				elem2 = elem1.join("");
				values.push(elem2);
				// Xaxis Day counter.
				xaxis_vals.push(count);
				count = count + 1;
			}
		}
		key1 = key;
		if (key.includes("TMAX") || key.includes("TMIN")) {
			key1 = key1 + " (Celcius)"
		}
		console.log(city.id + " " + key1 + " " + values);
	 	draw_chart(city.id, key1, xaxis_vals, values);
	  }

	 // Show the image
	 document.getElementById("status").setAttribute("style","display:block;");

	 //Hide the form
	 document.getElementById("newRunFormDiv").setAttribute("style","display:none;");

        }
      };

      url = "/weather_params?city=" + city.id;
      xhttp.open("GET", url, true);
      xhttp.send();
}

function show_settings() {
	alert("To be done")
}

function logout() {
      var xhttp;
      xhttp = new XMLHttpRequest();
      xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
          //Reference: https://stackoverflow.com/questions/33914245/how-to-replace-the-entire-html-webpage-with-ajax-response
          $("html").html(xhttp.responseText);
	  window.history.pushState("object or string", "Title", "/");
        }
      };

      url = "/logout"
      xhttp.open("GET", url, true);
      xhttp.send();
}

function display_new_run_form() {
	newRunFormDiv = document.getElementById("newRunFormDiv");
	newRunFormDiv.setAttribute("style", "display:block;");

	statusDiv = document.getElementById("status");
	statusDiv.setAttribute("style", "display:none;");

	document.getElementById("addCityFormDiv").setAttribute("style","display:none;");
}

function display_add_city_form() {
	addCityFormDiv = document.getElementById("addCityFormDiv");
	addCityFormDiv.setAttribute("style", "display:block;");

	statusDiv = document.getElementById("status");
	statusDiv.setAttribute("style", "display:none;");

	document.getElementById("newRunForm").setAttribute("style","display:none;");
}
