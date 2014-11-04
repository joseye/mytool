<%@ page isELIgnored="false"%>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>GTM Dev Tool</title>
	<!-- <link href="resources/css/bootstrap-theme.min.css" rel="stylesheet"> -->
	<link href="resources/css/bootstrap.min.css" rel="stylesheet">
	<link href="resources/css/square/purple.css" rel="stylesheet">
	<!-- Just for debugging purposes. Don't actually copy this line! -->
	<!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
	<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
	<!--[if lt IE 9]>
	  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
	  <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
	<![endif]-->
</head>
<body>
    <!-- Fixed navbar -->
    <div class="navbar navbar-default navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <a class="navbar-brand" href="welcome.do">GTM Dev Tool</a>
        </div>
        <div class="navbar-collapse collapse" >
          <ul class="nav navbar-nav" > 
            <li class="active"><a href="welcome.do" >Home</a></li>
            <li><a href="welcome.do" >DownLoad</a></li>
           <li><a href="howto.html">How to Use</a></li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
             <li>${requestScope.message} </li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>
    <div class="container" style="margin-top: 50px;">
	    <h1>Here is a  tool for GTM BLiS DEV</h1>
	    <ul class="nav nav-tabs" role="tablist" id="myTab">
		  <li class="active"><a href="#home" role="tab" data-toggle="tab">Send</a></li>
		  <li><a href="#profile" role="tab" data-toggle="tab">Result</a></li>
		</ul>
		<div class="tab-content">
		  <div class="tab-pane active" id="home">
		  <textarea id="inputcontent" rows="15" cols="150" id="soap" placeholder="Please input your soap here or input orderid then double click to generate end-complete soap." ondblclick="setupdateoppid()" required></textarea><br/>
			<div id="dblist"  class="form-inline;" style="margin-top: 20px;"></div>
			<div id="message" style="font-family: fantasy;font-size: x-large;font-weight: bold;"></div>
		   <input type="button"  class="btn btn-primary"  value="execute" onclick='sendSoap();' />
		   <input type="button"  class="btn btn-primary"  value="Clear" onclick="myclear();" />
		  </div>
		  <div class="tab-pane" id="profile">
		  <textarea rows="15" cols="150" id="xmlresult"></textarea>
		  <div id="queryresult" class="table-responsive">
		  </div>
			  
		  </div>
		</div>
	</div> <!-- /container -->
	
<script src="resources/js/jquery-1.11.1.min.js"></script>
<script src="resources/js/bootstrap.min.js"></script>
<script src="resources/js/icheck.min.js"></script>
<script src="resources/js/html5shiv.js"></script>
<script type="text/javascript">
md5="",load=0,count=0,user="";
function myclear(){
	$("#xmlresult").val("");
	$("#inputcontent").val("");
	initQueryDiv();
}
function loaddata(){
	if(md5){
		$.get("loadData.do?md5="+md5+"&load="+load+"&count="+count,function(data){
			var json=eval(data);
			load=json.load;
			count=json.count;
			var flag=json.flag;
			if(flag=='false'){
				md5=null;
				//alert('all data has been load.');
			}
			var list=json.list;
			if(list){
				var str;
				$.each(list, function(i, o) {
					str+="<tr><td>"+o.index+"</td><td>"+o.sql+"</td><td>"+o.cost+"</td><td>";
					if(o.color){
						str+="<font color='"+o.color+"'>";
					}
					str+=o.result
					if(o.color){
						str+="</font>";
					}
					str+="</td></tr>" 
				});
				$("#appendbody").append(str);
			}
			
		});
	}
}
setInterval(loaddata,15000);

$(function(){ 
	$("#xmlresult").hide();
	$("#queryresult").show();
	/*$('.navbar-nav a').click(function (e) {
	  e.preventDefault();
	  $(this).tab('show')
	});*/
	$.get("getDBInfos.do",function(data){
	     var json = $.parseJSON(data);
	     $(json).each(function(index, obj){
	         var env = json[index];
	         var str="<label class='radio'><input type='radio' name='env' ";
	         if(env=='DEV'){
	        	 str+="checked";
	         }
	         str+=" value='"+env+"' >"+env+"&nbsp;&nbsp;&nbsp;</label>"
	         $("#dblist").append(str);
	     });
	     $('.radio').iCheck({
	 	    checkboxClass: 'icheckbox_square',
	 	    radioClass: 'iradio_square-purple',
	 	    increaseArea: '30%' // optional
	 	  });
	     
	 });
}) ;
String.prototype.Trim = function()  
{  
return this.replace(/(^\s*)|(\s*$)/g, "");  
}  
String.prototype.LTrim = function()  
{  
return this.replace(/(^\s*)/g, "");  
}  
String.prototype.RTrim = function()  
{  
return this.replace(/(\s*$)/g, "");  
}  
function checkNum(str){
	var reg = new RegExp("^[0-9]*$");
	 if(!reg.test(str)){
		 alert('orderid must be a numnber');
		 $("#inputcontent").val("");
		 return false ;
	 }
	 return true;
}

function setupdateoppid(){
	var sinput=$("#inputcontent").val();
	sinput=sinput.Trim();
	if(!sinput){
		return ;
	}
	if(!checkNum(sinput)){
		return ;
	}
	 $("#inputcontent").val("updateoppid:"+sinput);
}
function generateECXml(sinput){
	if(!(sinput.indexOf('SOAP:Envelope xmlns:SOAP')>-1)){
		if(!checkNum(sinput)){
			return ;
		}
		 var ecxml="<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"+
		 "	<SOAP:Body>                                                        \n"+
		 "		<InsertPortfolio xmlns=\"http://www.webex.com/blis/1.0/custom\"> \n"+
		 "			<OrderID>{orderid}</OrderID>                              \n"+
		 "			<CreatedBy>1</CreatedBy>                                   \n"+
		 "		</InsertPortfolio>                                             \n"+
		 "	</SOAP:Body>                                                       \n"+
		 "</SOAP:Envelope>";
		 $("#inputcontent").val(ecxml.replace(/\{orderid\}/,sinput))
	}
}
function sendSoap(){
	var sinput=$("#inputcontent").val();
	var select=$('input[name="env"]:checked').val();
	sinput=sinput.Trim();
	if(!sinput){
		alert('Please input orderid or soap');
	}
	//query DB
	if(sinput.indexOf('select')>-1){
		queryDB(select,sinput);
		return ;
	}else{
		if(sinput.indexOf('updateoppid:')==-1){
			generateECXml(sinput);
		}
		sinput=$("#inputcontent").val();
		$.get("sendSoap.do?env="+select+"&soap="+sinput,function(data){
			 $("#xmlresult").val(data);
			 $("#xmlresult").show();
			 $("#queryresult").hide();
			$('#myTab a:last').tab('show')
		 });
	}
}
function initQueryDiv(){
	//var str="<div class=\"row\" >    <div class=\"col-md-1\">index</div>    <div class=\"col-md-5\">sql</div>   <div class=\"col-md-1\">cost</div>  <div class=\"col-md-3\">rsult</div></div>";
	var table="<table class=\"table table-striped\">    <thead>    <tr>      <th>#</th>      <th>sql</th>      <th>cost</th>      <th>result</th></tr></thead><tbody id=\"appendbody\"></tbody></table>"
	$("#queryresult").html("");
	$("#queryresult").append(table);
}
function queryDB(env,str){
	$.post("executeQuery.do",{env:env,sqls:str},function(data){
		 $("#xmlresult").hide();
		 $("#queryresult").show();
		$('#myTab a:last').tab('show');
		var json=eval(data);
		initQueryDiv();
		md5=json.md5;
		var str="<div class=\"col-md-9\">init successfully,,,,md5 fro this query is:"+md5+"<div>";
		$("#queryresult").append(str);
	 });
}

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
}  

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

function loadUSer(){
	try{
		var locator = new ActiveXObject ("WbemScripting.SWbemLocator"); 
		var service = locator.ConnectServer(".");
		var properties = service.ExecQuery("SELECT * FROM Win32_UserAccount");
		var e = new Enumerator (properties);
		for (;!e.atEnd();e.moveNext ()){
			var p = e.item ();
			user=p.Domain;
			break;
			
		}
	}catch(e){
		
	}
}

 </script>
</body>
</html>
