function check_and_refresh() {

  signupsDiv1Style = document.getElementById("signups-div").style.display;
  console.log("Signups Div style:" + signupsDiv1Style);
  if (signupsDiv1Style == "block") {

     setTimeout(() => {

          url = "/signups";

          var xhr = new XMLHttpRequest();
          xhr.open('GET', url, true); // async=true -> asynchronous

          xhr.onreadystatechange = function () {
          if (this.readyState == 4 && this.status == 200) {
              fieldData = JSON.parse(this.responseText);
              console.log(fieldData);
              console.log("-------");
              students = fieldData['signups'];

              signupsDiv = document.getElementById("signups");

              for(i=0;i<students.length; i++) {
                student = students[i];
                name = student['entity'];
                if (document.getElementById("student-" + name) == null) {
                  studentItem = document.createElement("li");
                  studentItem.setAttribute("id","student-" + name);
                  signupsDiv.appendChild(studentItem);
                  studentItem.innerHTML = name;
                }
            }
          }
        }

          xhr.send();
        check_and_refresh();
    }, 5000); // 5 seconds
  }
}

function take_input() {
  document.getElementById("welcome").setAttribute("style","display:none");
  document.getElementById("take-input").setAttribute("style","display:block");
}

// TODO: Add search music functionality; 
function search_music() {

  // Steps: Parse the input instrument; make AJAX call; show the results; create form to signup.")

}

function learn_music() {

    document.getElementById("contentcol").innerHTML = "";

    searchLabel = document.createElement("label");
    searchLabel.setAttribute("class","label");
    searchLabel.innerHTML = "Instrument to search"

    searchBox = document.createElement("input");
    searchBox.setAttribute("type","text");
    searchBox.setAttribute("id","instrument-to-search");

    searchButton = document.createElement("button");
    searchButton.innerHTML = "Search"
    searchButton.setAttribute("onclick","search_music()");

    document.getElementById("contentcol").appendChild(searchLabel);
    document.getElementById("contentcol").appendChild(document.createElement("br"));
    document.getElementById("contentcol").appendChild(searchBox);
    document.getElementById("contentcol").appendChild(document.createElement("br"));
    document.getElementById("contentcol").appendChild(searchButton);

}
