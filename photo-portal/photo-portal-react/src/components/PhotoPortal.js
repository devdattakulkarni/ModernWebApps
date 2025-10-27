import React, { useRef, useState, useEffect } from "react";

const PhotoPortal = () => {
  const [searchType, setSearchType] = useState("Name");
  const [searchInput, setSearchInput] = useState("");
  const [photoList, setPhotoList] = useState([]);
  const [photoUploadStatus, setPhotoUploadStatus] = useState("");
  const [adminUser, setAdminUser] = useState(true);
  const [selectedPhoto, setSelectedPhoto] = useState(null);
  const modalRef = useRef();
  const [photoName, setPhotoName] = useState(null);
  const [photoComments, setPhotoComments] = useState(null);
  
  const openModal = (name) => {
     setPhotoName(name);
	  

     // Requirement 3.6 Retrieve photo comments from the backend  	  
     fetchPhotoComments(name);

     const modal = new window.bootstrap.Modal(modalRef.current);
     modal.show();
  };

  // ----------------------------------------------------------------------
  // Fetch existing photos on component load
  // ----------------------------------------------------------------------
  useEffect(() => {
    fetchPhotos();
    
    const intervalId = setInterval(() => {
	    fetchPhotos();
    }, 100000);

    return () => clearInterval(intervalId);
  }, []);

  useEffect( () => {
          const userType = localStorage.getItem("usertype");
	  if (userType === "generaluser" ) {
          	setAdminUser(false);
	  }
	  if (userType === "admin" ) {
		setAdminUser(true);
	  }
  }, []);

  const fetchPhotos = async () => {
    try {
      const response = await fetch("http://localhost:5000/api/photos");
      if (!response.ok) {
        throw new Error("Failed to fetch photos");
      }
      const data = await response.json();
      setPhotoList(data.photos); // backend should return {"photos": [...]}
    } catch (error) {
      console.error("Error fetching photos:", error);
      setPhotoUploadStatus("Error loading photo list");
    }
  };

  const fetchPhotoComments = async (photoName) => {
	  // Requirement 3.6
	  // Make a fetch call to the backend to retrieve photocomments
	  // Then setPhotoComments
  };

  // ----------------------------------------------------------------------
  // Search form submission
  // ----------------------------------------------------------------------
  const handleSearch = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch("http://localhost:5000/api/search", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ type: searchType, query: searchInput }),
      });

      if (!response.ok) throw new Error("Search failed");
      const data = await response.json();
      setPhotoList(data.results);
    } catch (err) {
      console.error(err);
      setPhotoUploadStatus("Search failed");
    }
  };

  // ----------------------------------------------------------------------
  // Upload photo handler
  // ----------------------------------------------------------------------
  const handleUpload = async (e) => {
    e.preventDefault();

    const formData = new FormData(e.target);

    try {
      const response = await fetch("http://localhost:5000/api/upload", {
        method: "POST",
        body: formData, // no headers for multipart/form-data
      });

      const data = await response.json();

      if (response.ok) {
        setPhotoUploadStatus(data.message || "Upload successful!");
        await fetchPhotos(); // refresh photo list
        e.target.reset(); // clear form
      } else {
        setPhotoUploadStatus(data.message || "Upload failed");
      }
    } catch (error) {
      console.error("Error uploading photo:", error);
      setPhotoUploadStatus("Upload failed due to network error");
    }
  };

  const showPhotoDetails = (photo) => {
    setSelectedPhoto(photo);
    console.log("Selected photo:", photo);
    alert("Photo: " + photo.name + " " + photo.date_taken + " " + photo.tags)
  };

  // ----------------------------------------------------------------------
  // Render
  // ----------------------------------------------------------------------
  return (

    <div>
      <div className="modal fade"
        ref={modalRef}
        tabIndex="-1"
        aria-hidden="true"
      >
        <div className="modal-dialog modal-dialog-centered">
          <div className="modal-content">
            <div className="modal-header">
              <h5 className="modal-title">{photoName}</h5>
              <button
                type="button"
                className="btn-close"
                data-bs-dismiss="modal"
                aria-label="Close"
              ></button>
            </div>
            <div className="modal-body text-center">
              {photoName && (
		<div>
                <img
                  src={`http://localhost:5000/static/images/photos/${photoName}`}
                  alt={photoName}
                  style={{ maxWidth: "100%" }}
                />
	        {/* Requirement 3.6 Display photoComments in the provided - Get rid of comments afterwards  <div>    </div>  */}
		</div>
              )}
            </div>
          </div>
        </div>
      </div>

    <div className="container-fluid">
      <div className="row mvh-100 flex-column flex-md-row">
        {/* LEFT PANEL */}
        <div
          className="col-md-2 div_left"
          style={{
            height: "100vh",
            backgroundColor: "lightblue",
          }}
        >
          {/* Search Form */}
          <div className="row">
            <form id="search_form" onSubmit={handleSearch}>
              <label style={{ paddingLeft: "10px" }}>Search</label>
              <select
                id="search_select"
                name="search_select"
                value={searchType}
                onChange={(e) => setSearchType(e.target.value)}
                style={{ marginLeft: "10px" }}
              >
                <option value="Name">Name</option>
                <option value="Date">Date</option>
                <option value="Tags">Tags</option>
              </select>
              <input
                type="text"
                name="search_input"
                id="search_input"
                value={searchInput}
                onChange={(e) => setSearchInput(e.target.value)}
                style={{ marginLeft: "10px" }}
              />
              <button style={{ marginLeft: "10px" }}>Search</button>
            </form>
          </div>

          <hr />

          {/* Photo list */}
          <div className="row">
            <div>
              <ol id="photo_list">
                {photoList.map((photo) => (
                  <li key={photo.name}>
                    <a
                      href="#"
                      onClick={(e) => {
                        e.preventDefault();
                        showPhotoDetails(photo);
                      }}
                    >
                      {photo.name}
                    </a>
                  </li>
                ))}
              </ol>
            </div>
          </div>
        </div>

        {/* MIDDLE PANEL */}
        <div className="col-md-8 flex-grow-1 div_right" id="entity-form-container">
          <div id="status">
            <div id="status_string" style={{ color: "green" }}>
              {photoUploadStatus}
            </div>
          </div>

	{adminUser && (
          <div id="uploadPhotoFormDiv" style={{ marginTop: "-10px" }}>
            <form id="uploadPhotoForm" onSubmit={handleUpload} encType="multipart/form-data">
              <label>Photo File</label>
              <input
                className="input"
                type="file"
                name="photo_file"
                id="photo_file"
                required
              />
              <br />
              <br />

              <div className="tab1">
                <b style={{ color: "blue" }}>Photo metadata</b>
                <table>
                  <tbody>
                    <tr>
                      <td><label>Date taken</label></td>
                      <td>
                        <input
                          type="text"
                          name="date_taken"
                          placeholder="MM/DD/YYYY"
                          id="date_taken"
                        />
                      </td>
                    </tr>
                    <tr>
                      <td><label>Tags</label></td>
                      <td>
                        <input
                          type="text"
                          name="tags"
                          placeholder="comma separated tags"
                          id="tags"
                        />
                      </td>
                    </tr>
		    {/* Requirement 3.5 - Add input of type textarea for comments  */}
                  </tbody>
                </table>
                <button className="btn btn-primary">Submit</button>
              </div>
            </form>
          </div>
	)}
        </div>

        {/* RIGHT PANEL */}
        <div
          className="col-md-2 flex-grow-1 div_right"
          id="entity-form-container-2"
          style={{
            height: "100vh",
            backgroundColor: "lightblue",
          }}
        >
          <div className="row">
            <div>
              <h6>Thumbnails:</h6>
              <ol id="available_photos_list">
                {photoList.map((photo) => (
                  <figure key={photo.name}>
                    <li>
                      <img
                        src={`http://localhost:5000/static/images/photos/${photo.name}`}
                        height="60"
                        width="60"
                        alt={photo.name}
                        onClick={() => openModal(photo.name)}
                        style={{ cursor: "pointer" }}
                      />
                    </li>
                    <figcaption>{photo.name}</figcaption>
                  </figure>
                ))}
              </ol>
            </div>
          </div>
        </div>
      </div>
    </div>

   </div>
  );
};

export default PhotoPortal;

