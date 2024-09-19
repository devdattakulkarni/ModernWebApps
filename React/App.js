import './App.css';
import { useState } from 'react';

export default function App() {
    return (
	    <France />
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


