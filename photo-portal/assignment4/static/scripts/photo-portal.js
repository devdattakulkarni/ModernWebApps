(function get_photos() {
  setTimeout(() => {
    make_photos_call();
    get_photos();
  }, 5000); // 5 seconds
})();

function make_photos_call() {
        url = "/photos";

        var xhr = new XMLHttpRequest();
        xhr.open('GET', url, true); // async=true -> asynchronous

        xhr.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            fieldData = JSON.parse(this.responseText);
            photo_images = fieldData["photos"];

            // Arrays to store the ids of the photos that are currently present in the backend;
            present_thumbnail_photo_list = [];
            present_photo_anchor_list = [];

            for(i=0;i<photo_images.length; i++) {
              photo_obj = JSON.parse(photo_images[i])
              photo_name = photo_obj['name'];
              photo_tags = photo_obj['tags'];
              photo_date_taken = photo_obj['date_taken'];

              figure_elem = document.createElement("figure");
              figure_caption_elem = document.createElement("figurecaption");
              figure_caption_elem.textContent = photo_name;

              // Add thumbnail, if not already present.
              thumbnail_id = photo_name + "-thumbnail-li";
              present_thumbnail_photo_list.push(thumbnail_id);
              if (document.getElementById(thumbnail_id) == null) {
                  thumbnail = document.createElement("li");
                  thumbnail.setAttribute("id", thumbnail_id);
                  thumbnail_label = document.createElement("label");
                  thumbnail_label.textContent = photo_name;
                  thumbnail_img = document.createElement("img");
                  thumbnail_img.height = 60;
                  thumbnail_img.width = 60;
                  thumbnail_img_id = photo_name + "-thumbnail-img";
                  thumbnail_img.setAttribute("id", thumbnail_img_id);
                  thumbnail_img.setAttribute("src","../static/images/photos/" + photo_name);
                  thumbnail_img.setAttribute("data-toggle", "modal");
                  thumbnail_img.setAttribute("data-target", "#imageModal");
                  thumbnail_img.setAttribute("onclick", "preview_photo('" + photo_name + "')");
                  thumbnail.appendChild(thumbnail_label);
                  thumbnail.appendChild(thumbnail_img);

                  document.getElementById("available_photos_list").appendChild(thumbnail);
              }

              // Add photo link, if not already present.
              photo_anchor_id = photo_name + "-photo-anchor";
              present_photo_anchor_list.push(photo_anchor_id);
              if (document.getElementById(photo_anchor_id) == null) {
                photo_anchor_li = document.createElement("li");
                photo_anchor_li.setAttribute("id", photo_anchor_id);
                photo_anchor = document.createElement("a");
                photo_anchor.textContent = photo_name;
                photo_anchor.setAttribute("href","#");
                photo_anchor.setAttribute("id", photo_anchor_id + "-a-element")
                photo_onclick_handler = "show_photo_details('" + photo_name + "','" + photo_date_taken + "','" + photo_tags + "')";
                photo_anchor.setAttribute("onclick", photo_onclick_handler);
                photo_anchor_li.appendChild(photo_anchor);

                document.getElementById("photo_list").appendChild(photo_anchor_li);
              } else {
                // Update the onclick_handler - may be tags have changed;
                photo_onclick_handler = "show_photo_details('" + photo_name + "','" + photo_date_taken + "','" + photo_tags + "')";
                elem = document.getElementById(photo_anchor_id + "-a-element");
                elem.setAttribute("onclick",photo_onclick_handler);
              }
          }

          // Remove photos from thumbnail list that have been deleted;
          available_photos_div = document.getElementById("available_photos_list");
          for (child of available_photos_div.children) {
              if ( !present_thumbnail_photo_list.includes(child.id) ) {
                  child.parentNode.removeChild(child);
              }
          }

          // Remove photos from left-side list that have been deleted;
          left_side_photos_div = document.getElementById("photo_list");
          for (child of left_side_photos_div.children) {
              if ( !present_photo_anchor_list.includes(child.id) ) {
                  child.parentNode.removeChild(child);
              }
          }

        }
      }
      xhr.send();
}

// Variable that controls whether slide show should be stopped or keep on going
slide_show_on = true;

// Retrieves the photo names from the backend by making AJAX call to /photos endpoint
function start_slide_show() {
  console.log("start_slide_show called.")

  // Requirement 5.1 
  // Make AJAX call to "/photos" to retrieve the photos.
  // Then call display_photo()

  slide_show_on = true;
  url = "/photos";
  var xhr = new XMLHttpRequest();
  xhr.open('GET', url, true); // async=true -> asynchronous
  xhr.onreadystatechange = function () {
  if (this.readyState == 4 && this.status == 200) {
      fieldData = JSON.parse(this.responseText);
      console.log(fieldData);
      console.log("-------");
      photo_images = fieldData["photos"];
      if (slide_show_on == true) {
          display_photo(0, photo_images);
      }
   }
  }
  xhr.send();
}

// Ref: https://stackoverflow.com/questions/30865456/javascript-loop-through-array-with-delay
// index: index in the photos_images to diplay a photo.
function display_photo(index, photo_images) {

          photo_name = JSON.parse(photo_images[index])['name'];
          figure_elem = document.createElement("figure");
          figure_elem.setAttribute("id","big-img");
          figure_caption_elem = document.createElement("figurecaption");
          figure_caption_elem.textContent = photo_name;

          big_img = document.createElement("img");

          // Delete previous image
          var previousImg = document.getElementById("big-img");
          if (previousImg != null) {
            previousImg.parentNode.removeChild(previousImg);
          }

          big_img.setAttribute("src","../static/images/photos/" + photo_name);

          slideshowImgDiv = document.getElementById("slideshowImage");
          figure_elem.appendChild(big_img);
          figure_elem.appendChild(figure_caption_elem);
          slideshowImgDiv.appendChild(figure_elem);

          newIndex = index+1;
          if (newIndex >= photo_images.length) {
            newIndex = 0;
          }

          if (slide_show_on == true) {
              setTimeout(display_photo, 3000, newIndex, photo_images);
          }
}

// Stop the slide show
// Requirement 5.2
function stop_slide_show() {
  console.log("stop_slide_show called.")
  slide_show_on = false;
}


function validate_data() {
  // hint: https://developer.mozilla.org/en-US/docs/Web/API/HTMLSelectElement/value
  option = document.getElementById("search_select").value;

  input_text = document.getElementById("search_input").value;
  if (input_text == "") {
    alert("Input cannot be empty for " + option);
    return false;
  } else {
    return true;
  }
}

// Function that handles update and delete of the form.
// Requirements 3.3 and 3.4
function handle_update_delete_form(action) {

  deleteUpdateForm = document.getElementById("update_delete_form");

  if (action == "delete") {
    deleteUpdateForm.submit();
  }

  if (action == "update") {

  photo = document.querySelector('input[name="photos_to_update_delete"]:checked').value;
  console.log("Selected photo:" + photo);

  document.getElementById("photo_name_label").textContent = photo;

  url = "/photos/" + photo + "/tags";

  var xhr = new XMLHttpRequest();
  xhr.open('GET', url, true); // async=true -> asynchronous
  xhr.onreadystatechange = function () {
  if (this.readyState == 4 && this.status == 200) {
      fieldData = JSON.parse(this.responseText);
      console.log(fieldData);
      console.log("-------");
      photo_tags = fieldData["tags"]; 
      console.log("Tags:" + photo_tags)

      current_labels = document.getElementById("current_labels");
      current_labels.textContent = photo_tags;

      hidden_input = document.getElementById("photo_name_for_tags_update");
      hidden_input.setAttribute("value", photo);
   }
  }
  xhr.send();
  }

}

function show_photo_details(photo_name, date_taken, tags) {
  alert("Photo:" + photo_name + " date_taken:" + date_taken + " tags:" + tags);
}

function preview_photo(photo_name) {
  // Delete previous image
  var previousImg = document.getElementById("preview-image");
  if (previousImg != null) {
    previousImg.parentNode.removeChild(previousImg);
  }

  preview_img = document.createElement("img");
  preview_img.setAttribute("id", "preview-image");
  preview_img.setAttribute("src","../static/images/photos/" + photo_name);
  document.getElementById("image-modal-body").appendChild(preview_img);
}

function show_settings() {
  alert("Settings called.");
}

function logout() {
  document.getElementById("settings-and-logout-form").submit();
}
