import './App.css';
import { useState } from 'react';

export default function App() {
    return (
	    //<France />
	    <LandingPage />
    );
}

function LandingPage() {
	return(
		<>
		<div>   <TopSection />
			<LeftSection />
			<MiddleSection />
			<RightSection />
		</div>
		</>
	);
}

function TopSection() {

	function logout() {
		alert("Logout called.");
	}

	function show_settings() {
		alert("Show settings called.");
	}

	return(
	<>
        <div className="container-fluid" style={{backgroundColor:"transparent"}}>
                <div className="row">
                        <div className="col-4">
                                <div className="text-left">
                                        <a href="https://www.cs.utexas.edu/~devdatta" target="_blank">MWA</a>
                                </div>
                        </div>
                        <div className="col-4">
                                <h5 style={{marginTop: 5, paddingLeft: 120}}>Photo Portal</h5>
                        </div>
                        <div className="col-4 text-right">
                                <div className="dropdown">
                                        <button className="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                                testuser
                                                <span className="caret"></span>
                                        </button>
                                        <ul className="dropdown-menu text-center" aria-labelledby="dropdownMenu1">
                                                <li><a href="#" onClick={show_settings}>Settings</a></li>
                                                <li>
                                                        <hr className="dropdown-divider"/>
                                                </li>
                                                <li><a href="#" onClick={logout}>Logout</a></li>
                                        </ul>
                                </div>
                        </div>
                </div>
        </div>
        <hr/ >
	</>
	);
}

function LeftSection() {

	function display_upload_photo_form() {
		alert("Display form called.")
	}

	function search() {
		alert("Search called.");
	}

	return(
		<>
		<div className="container-fluid">

		  <div className="row  mvh-100 flex-column flex-md-row">
                        <div className="col-md-2 div_left" style={{height:"90vh",backgroundColor: "lightblue",float:"left", marginTop:-16}}>
                                <div className="row" id="uploadPhoto">
					<div className="col-2">
                                        <button onClick={display_upload_photo_form} style={{marginLeft:5, marginTop:5}}>Upload Photo</button>
					</div>
                                </div>
                                <hr></hr>

                                <div className="row">
                                        <label style={{paddingLeft:10}}>Search (with tags)</label>
					<div className="col-2">
                                        <input type="text" style={{marginLeft:10}} />
                                        <button name="search" style={{marginLeft:10}} onClick={search}>Search</button>
					</div>
                                </div>
                                <hr></hr>

                                <div className="row">
                                        <div>
                                                <ol id="photo_list">
                                                </ol>
                                        </div>
                                </div> 
                        </div>
		  </div>

		</div>
		</>
	);
}

function MiddleSection() {
	return(
		<div>TODO</div>
	);
}

function RightSection() {
	return(
		<div>TODO: Thumbnails </div>
	);
}






function France() {

	const [totalclicks, setTotalclicks] = useState(0);
	var myclicks;

	function addClick(clickcount) {
		myclicks = totalclicks + clickcount;
		setTotalclicks(myclicks);
		alert("Total clicks:" + myclicks);
	}

	return (
		<>
		<div>
			<Column color="blue" onClick={addClick}/>
			<Column color="white" onClick={addClick}/>
			<Column color="red"  onClick={addClick}/>
		</div>
		</>
	);
}

function Column({color, onClick}) {

	const [clicks, setClicks] = useState(0);
	var myclicks;
	
	function handleClick(color) {
		myclicks = clicks + 1;
		setClicks(myclicks);
		alert("My color is " + color + " click:" + myclicks);
		onClick(1);
	}

	return (
		<div className="Column-layout" style={{backgroundColor: color}} onClick={() => handleClick(color)}></div>
	);
}


