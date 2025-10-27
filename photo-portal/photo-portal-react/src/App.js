import { useState } from "react";
import { useNavigate } from "react-router-dom";

import { Routes, Route } from "react-router-dom";
import logo from './logo.svg';
import './App.css';

import GeneralUser from "./components/GeneralUser";
import Admin from "./components/Admin";
import Navbar from "./components/Navbar";
import PhotoPortal from "./components/PhotoPortal";


function App() {

  return (
    <Routes>
      {/* path defines the URL, element defines what component to render */}
      {/* Requirement 4.1 Add a Route for "/" for GeneralUser component  */}
      <Route path="/admin" element={<Admin />} /> 
      <Route path="/photo-portal" element={
	      <>
	      <Navbar />
	      <PhotoPortal />
	      </>
      } 
      />
    </Routes>
  );
}

export default App;

