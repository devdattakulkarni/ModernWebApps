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

            available_photos_div = document.getElementById("available_photos_list");

            for(i=0;i<photo_images.length; i++) {
              photo_name = photo_images[i];

              figure_elem = document.createElement("figure");
              figure_caption_elem = document.createElement("figurecaption");
              figure_caption_elem.textContent = photo_name;

              // Add thumbnail, if not already present.
              if (document.getElementById(photo_name + "-thumbnail-li") == null) {
                  thumbnail = document.createElement("li");
                  thumbnail.setAttribute("id", photo_name + "-thumbnail-li");
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

}

// Ref: https://stackoverflow.com/questions/30865456/javascript-loop-through-array-with-delay
// index: index in the photos_images to diplay a photo.
function display_photo(index, photo_images) {

          photo_name = photo_images[index];
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
// action will be "delete" or "update".
function handle_update_delete_form(action) {


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
