import { useState } from "react";
import { useNavigate } from "react-router-dom";


export default function GeneralUser() {

   const [gmail, setGmail] = useState("");
   const navigate = useNavigate();

   const handleSubmit = async (e) => {

   e.preventDefault();
   const response = await fetch("http://localhost:5000/api/generaluserlogin", {
           method: "POST",
           headers: {"Content-Type": "application/json"},
           body: JSON.stringify({gmail}),
   });

   if (response.ok) {
           const data = await response.json();
           localStorage.setItem("name", data.username);
	   localStorage.setItem("usertype", "generaluser");
           navigate("/photo-portal");
   }

   };


  return (
    <div>
          <div className="centered">
          <img
                  src="/images/longhorn.png"
                  className="rounded mx-auto d-block"
                  style={{ width: "300px", height: "150px", justifyContent: "center"  }}
                  alt="Longhorn"
          />
          </div>

          <div>
                <div className="centered"><h5>Photo Portal</h5></div>
                <br/>
                <br/>
	        {/* Requirement 4.3: Show user not found if a gmail address that is not registered is entered. */}
                <div className="centered">
                <form onSubmit={handleSubmit} method="post" className="form-signin" style={{display: "flex", backgroundColor:"transparent", justifyContent:"center", alignItems: "center" }}    >
	                {/* Requirement 4.2 Add input box for Gmail */}
                        <br/>
                        <br/>
                        <button type="submit" className="btn btn-primary">Login</button>
                </form>
                </div>
          </div>

          <div className="centered"><h6><a href="https://www.cs.utexas.edu/~devdatta">Contact us</a></h6></div>

     </div>
  );
}
