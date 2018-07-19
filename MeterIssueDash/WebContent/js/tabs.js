function openErrors(evt, errorType){
	var i, tabcontent, tablinks, noRes;
	tabcontent=document.getElementsByClassName("etabcontent");	//retrieve page contents
	
	//Hide elements not currently being displayed
	for(i = 0; i < tabcontent.length; i++){
		tabcontent[i].style.display = "none";
	}
	
	//Change the active tab
	tablinks = document.getElementsByClassName("etablinks");	//retrieve tabs
	for(i = 0; i < tablinks.length; i++){
		tablinks[i].className = tablinks[i].className.replace(" active", "");
	}
	
	//Remove no results text when clicking a new tab
	noRes = document.getElementById("NR");
	if(noRes != null){
		noRes.innerText = "";
	}
	
	//Display the current relevant table data
	document.getElementById(errorType).style.display = "block";
	evt.currentTarget.className += " active";
}
