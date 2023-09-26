function draw_chart(city, data_label, xaxis_data, chart_data) {

	outputImg = document.getElementById("outputImg");
	// TODO: delete previous children
	//outputImg.innerHTML = '';
	newElem = document.createElement("canvas");
	elemId = city + "-" + data_label;
	newElem.setAttribute("id",elemId);

	//const ctx = document.getElementById('myChart');

  new Chart(newElem, {
    type: 'line',
    data: {
      labels: xaxis_data,
      datasets: [{
        label: data_label,
	fill: false,
        data: chart_data,
	//fill: false,
	//borderColor: 'rgb(75, 192, 192)',
	//tension: 0.1,
        borderWidth: 1
      }]
    },
    options: {
      responsive: true,
      //maintainAspectRatio: false,
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

function draw_chart_orig() {
const ctx = document.getElementById('myChart');

  new Chart(ctx, {
    type: 'bar',
    data: {
      labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
      datasets: [{
        label: '# of Votes',
        data: [12, 19, 3, 5, 2, 3],
        borderWidth: 1
      }]
    },
    options: {
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });
}

function validate() {
	ids = ["e_we","e_sn","e_vert","ref_lat","ref_lon","truelat1","truelat2"]
	empty_ids = []
	for (i=0; i<ids.length; i++) {
	    val = document.getElementById(ids[i]).value;
	    if (isNaN(val)) {
		empty_ids.push(ids[i]);
	    }
	}
	if (empty_ids.length > 0) {
		alert("Should be a number:" + empty_ids.join(", "))
		return false;
	}

	// Check format of start_date and end_date
	// YYYY-MM-DD_HH:MM:SS
	var regEx = /^\d{4}-\d{2}-\d{2}_\d{2}:\d{2}:\d{2}$/;
	start_date = document.getElementById("start_date").value;
	end_date = document.getElementById("end_date").value;
	s_format = start_date.match(regEx);
	e_format = end_date.match(regEx);

	if (!s_format || !e_format) {
		incorrect_format = " incorrect format.";
		who = "";
		if (!s_format) {
			who = "start_date";
		}
		if (!e_format) {
			who = who + " end_date";
		}
		stat = who + " " + incorrect_format;
		alert(stat)
		return false;
	}
	return true; 
}

function get_city_status(city) {
      var xhttp;
      xhttp = new XMLHttpRequest();
      xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
          //Reference: https://stackoverflow.com/questions/33914245/how-to-replace-the-entire-html-webpage-with-ajax-response

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

function get_city_status_graph(city) {
	//alert(runRef.id)

      var xhttp;
      xhttp = new XMLHttpRequest();
      xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
          //Reference: https://stackoverflow.com/questions/33914245/how-to-replace-the-entire-html-webpage-with-ajax-response

         fieldData = JSON.parse(this.responseText);
         console.log(fieldData);
         console.log("-------");

	/*
	 imageData = fieldData["imageData"];
	 
	 var currentImg = document.getElementById("output-img");
	 if (currentImg != null) {
	 	currentImg.parentNode.removeChild(currentImg);
	 }
	
	 statusString = document.getElementById("status_string");
	 statusString.innerHTML = "Output of " + runRef.id;
	 //statusString.setAttribute("style", "display:none;");

	 imgElem = document.createElement("img");
	 srcVal = "data:image/png;base64," + imageData; 
	 imgElem.setAttribute("src", srcVal);
	 imgElem.setAttribute("id", "output-img");
	 
	 document.getElementById("outputImg").appendChild(imgElem);*/
	 output_string = "";
	 statusString = document.getElementById("status_string");
	 statusString.innerHTML = "Output of " + city.id;
	 // Clear the previous canvas elements;
	 outputImg = document.getElementById("outputImg");
	 outputImg.innerHTML = '';
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

      url = "/status?city=" + city.id;
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
