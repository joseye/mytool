handleFiles = function(files) {  
	for (var i = 0; i < files.length; i++) {
		var xhr = new XMLHttpRequest();  
		var form = new FormData();
	    form.append("file",files[i]);
	    xhr.onload = function () {
	    	var json=eval(xhr.responseText);
	    	$("#inputcontent").val(json.content);
	    	$("#message").html("total line is <font color='red'>"+json.total+"</font>.");
	    };
	    xhr.open("POST", "uploadFile.do");
	    xhr.send(form);
	}
} ;

document.addEventListener("dragenter", function(e){  
	inputcontent.style.borderColor = 'gray';  
}, false);  
document.addEventListener("dragleave", function(e){  
	inputcontent.style.borderColor = 'gray';  
}, false);  
inputcontent.addEventListener("dragenter", function(e){  
	inputcontent.style.borderColor = 'gray';  
	inputcontent.style.backgroundColor = 'white';  
}, false);  
inputcontent.addEventListener("dragleave", function(e){  
	inputcontent.style.backgroundColor = 'transparent';  
}, false);  
inputcontent.addEventListener("dragenter", function(e){  
    e.stopPropagation();  
    e.preventDefault();  
}, false);  
inputcontent.addEventListener("dragover", function(e){  
    e.stopPropagation();  
    e.preventDefault();  
}, false);  
inputcontent.addEventListener("drop", function(e){  
    e.stopPropagation();  
    e.preventDefault();  
    if(!e.dataTransfer.files){
    	alert('Your browser could not support drag and drop to upload file.Please try to copy you sql here or try it in chrome or firefox.');
    	return ;
    }
    if(e.dataTransfer.files.length==0){
    	return;
    }
    handleFiles(e.dataTransfer.files);  
}, false);