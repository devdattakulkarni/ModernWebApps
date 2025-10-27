import React from "react";
import {useEffect, useState} from "react";
import { useNavigate } from "react-router-dom";


export default function Navbar() {

  const [userName, setUserName] = useState("");
  const navigate = useNavigate(); 
  
  useEffect( () => {
	  const storedName = localStorage.getItem("name");
	  setUserName(storedName);
  }, []); 

   const logout = async (e) => {

   e.preventDefault();
   const response = await fetch("http://localhost:5000/api/logout", {
   	   headers: {"Content-Type": "application/json"},        
	   method: "POST",
	   credentials: "include",
	   body: JSON.stringify({userName}),
   });

   if (response.ok) {
	  const data = await response.json();
	  const usertype = data.usertype;
	  if (usertype === "admin") {
	  	navigate("/admin");
	  } else {
	  	navigate("/");
	  }
   }

   };


  const showSettings = () => {
	  alert("Show settings called.")
  };

  return (
    <div style="height:10vh" className="container-fluid" style={{ backgroundColor: "transparent" }}>
      <div className="row">
        <div className="col">
          <div className="text-left">
            <a href="https://www.cs.utexas.edu/~devdatta" target="_blank" rel="noreferrer">
              MWA
            </a>
          </div>
        </div>

        <div>
          <h5 style={{ marginTop: "5px" }}>Photo Portal</h5>
        </div>

        <div className="col text-right">
          <div className="dropdown">
            <button
              className="btn btn-default dropdown-toggle"
              type="button"
              id="dropdownMenu1"
              data-toggle="dropdown"
              aria-haspopup="true"
              aria-expanded="true"
            >
              {userName}
              <span className="caret"></span>
            </button>

            <form id="settings-and-logout-form" action="logout" method="post">
              <ul className="dropdown-menu text-center" aria-labelledby="dropdownMenu1">
                <li>
                  <a href="#" onClick={showSettings}>
                    Settings
                  </a>
                </li>
                <li>
                  <hr className="dropdown-divider" />
                </li>
                <li>
                  <a href="#" onClick={logout}>
                    Logout
                  </a>
                </li>
              </ul>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

