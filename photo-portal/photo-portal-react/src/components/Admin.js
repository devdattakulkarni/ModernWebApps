import { useState } from "react";
import { useNavigate } from "react-router-dom";


export default function Admin() {

   const [name, setName] = useState("");
   const [password, setPassword] = useState("");
   const [userFound, setUserFound] = useState(true);
   const navigate = useNavigate();

   const handleSubmit = async (e) => {
   
   e.preventDefault();
   const response = await fetch("http://localhost:5000/api/adminlogin", {
	   method: "POST",
	   headers: {"Content-Type": "application/json"},
	   body: JSON.stringify({name, password}),
   });

   if (response.ok) {

	   const data = await response.json();
	   const message = data.message;
	   console.log("Message:", message); 
	   localStorage.setItem("name", data.username);
	   localStorage.setItem("usertype", "admin");
	   if (message === "Success") {
	   	navigate("/photo-portal");
	   } else {
		   setUserFound(false);
	   }
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
                <div className="centered"><h5>Photo Portal Admin</h5></div>
                <br/>
	        {!userFound && (
	        <div className="centered" style={{ color: "red", marginTop: "10px" }}> {name} not found.    </div>
		)}
                <div className="centered">
                <form onSubmit={handleSubmit} action="login" method="post">
                        <input type="text" name="username" placeholder="Username" value={name} style={{display: "block" }}  onChange={(e) => setName(e.target.value)} required/>
	                <br/>
                        <input type="password" name="password" placeholder="Password" value={password} style={{display: "block" }} 
	                onChange={(e) => setPassword(e.target.value)}
	  	        required/>
	                <br/>
                        <div className="centered"> <button type="submit" className="btn btn-primary">Login</button> </div>
                </form>
                </div>
          </div>

          <div className="centered"><h6><a href="https://www.cs.utexas.edu/~devdatta">Contact us</a></h6></div>
     </div>
  );
}
