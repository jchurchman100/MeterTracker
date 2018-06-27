function openErrors(evt, errorType){
	var i, tabcontent, tablinks, noRes;
	//retrieve page contents
	tabcontent=document.getElementsByClassName("etabcontent");
	//hide elements
	for(i = 0; i < tabcontent.length; i++){
		tabcontent[i].style.display = "none";
	}
	//retrieve tabs
	tablinks = document.getElementsByClassName("etablinks");
	//change active tab
	for(i = 0; i < tablinks.length; i++){
		tablinks[i].className = tablinks[i].className.replace(" active", "");
	}
	
	noRes = document.getElementById("NR");
	if(noRes != null){
		noRes.innerText = "";
	}
	//display correct element
	document.getElementById(errorType).style.display = "block";
	evt.currentTarget.className += " active";
}
