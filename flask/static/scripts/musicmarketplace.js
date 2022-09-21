      var lessons = {};

(function get_signups() {
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
              name = student['name'];
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
      get_signups();
  }, 5000); // 5 seconds
})();

      function show_lesson(lesson_name) {
        lesson_details = lessons[lesson_name];
        demo_url = lesson_details['demo_url'];
        days = lesson_details['days'];
        timings = lesson_details['timings'];
        alert("lesson details - " + demo_url + " " + days + " " + timings);
      }

      function show_lesson_details(demo_url, days, timings) {
        alert("lesson details - " + demo_url + " " + days + " " + timings);
      }

      function store_lesson() {
        lesson_name = document.getElementById("lesson-name-input").value;
        if (lesson_name == "") {
          alert("Lesson Name cannot be empty.");
          return;
        }
        lesson = document.createElement("li");
        lesson.setAttribute("id", lesson_name + "-li");
        lesson_anchor = document.createElement("a");
        lesson_anchor.setAttribute("href","#");
        lesson_anchor.setAttribute("onclick","show_lesson('" + lesson_name + "')");
        lesson_anchor.innerHTML = lesson_name;
        lesson.appendChild(lesson_anchor);
        document.getElementById("lesson_list").appendChild(lesson);
        lesson_details = {};

        demo_url = document.getElementById("demo-url").value;
        timings = document.querySelector('input[name="timings-radio"]:checked').value;
        days_of_week = get_days_of_week();

        lesson_details['demo_url'] = demo_url;
        lesson_details['days'] = days_of_week;
        lesson_details['timings'] = timings;
        lesson_details['instrument'] = lesson_name;
        lessons[lesson_name] = lesson_details;
      }

      function get_days_of_week() {
        days_of_week = [];

        if (document.querySelector('input[name="monday"]:checked')) {
          days_of_week.push(document.getElementById("monday").value);
        }

        if (document.querySelector('input[name="tuesday"]:checked')) {
          days_of_week.push(document.getElementById("tuesday").value);
        }

        return days_of_week;
      }

      function take_input() {
        document.getElementById("welcome").setAttribute("style","display:none");
        document.getElementById("take-input").setAttribute("style","display:block");
      }

      function learn_music() {
        url = "/lessons?instrument=guitar";

        var xhr = new XMLHttpRequest();
        xhr.open('GET', url, true); // async=true -> asynchronous
        xhr.addEventListener("readystatechange", search_results_handler);
        xhr.send();
      }

      function search_results_handler() {
        if (this.readyState == 4 && this.status == 200) {
            fieldData = JSON.parse(this.responseText);
            console.log(fieldData);
            console.log("-------");
            found_lessons = fieldData['lessons'];

            contentcolDiv = document.getElementById("contentcol");
            contentcolDiv.innerHTML = "";
            contentcolDiv.appendChild(document.createElement("br"));

            for(i=0;i<found_lessons.length; i++) {
              lesson_details = found_lessons[i];

              foundItemList = document.createElement("li");
              contentcolDiv.appendChild(foundItemList);
              lessonDiv = document.createElement("div");
              instrument = lesson_details["instrument"];
              demo_url = lesson_details["demo_url"];
              days = lesson_details["days"];
              timings = lesson_details["timings"];
              foundItemList.innerHTML = instrument + " " + demo_url + " " + days + " " + timings;
            }
          }
      }

      function learn_music_local_var() {
        contentcolDiv = document.getElementById("contentcol");
        contentcolDiv.innerHTML = "";
        contentcolDiv.appendChild(document.createElement("br"));

        searchByInstrumentHR = document.createElement("hr");
        contentcolDiv.appendChild(searchByInstrumentHR);

          lesson_instrument = "guitar";
          i = 0;
          for(const [key, value] of Object.entries(lessons)) {
            lesson_details = value;
            instrument = lesson_details['instrument'].toLowerCase();
            if (instrument.includes(lesson_instrument)) {
              foundItemList = document.createElement("li");
              contentcolDiv.appendChild(foundItemList);
              lessonDiv = document.createElement("div");
              demo_url = lesson_details["demo_url"];
              days = lesson_details["days"];
              timings = lesson_details["timings"];
              foundItemList.innerHTML = instrument + " " + demo_url + " " + days + " " + timings;
              //foundItemList.appendChild(lessonDiv);
            }
            i++;
          }
        
      }
