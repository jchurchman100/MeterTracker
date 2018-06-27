function defaultOpen(errorType){
	document.getElementById
	var i, tabcontent, tablinks;
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
	//display correct element
	console.log(errorType.substring(0,3));
	document.getElementById(errorType.substring(0,3)).style.display = "block";
	document.getElementById(errorType).className += " active";
}