/**
 * 
 */

function myFunction() {
    var x, text;

    // Get the value of input field with id="numb"

    x = document.getElementById("numb").value;

    // If x is Not a Number or less than one or greater than 10

    if (isNaN(x) || x < 1 || x > 10) {
        text = "Invalid input.";
    } else {
        text = "Correct input.";
    }
    document.getElementById("demo").innerHTML = text;
    
    console.log("Testing console.");
    console.log(text);
}