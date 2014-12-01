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
	<style type="text/css">
	#mask{
		background-image:url("resources/images/loading.gif");
		background-repeat:no-repeat;
		background-position:center center ;
		position: absolute;
		left: 0;
		top:0;
		width: 100%;
		height: 100%;
		background-color: #ddd;
		opacity: 0.5;
		display: none;
		/* opacity: 0.5;
    	filter: Alpha(opacity=50); */
		
		/* z-index: 9999; */
		
	}
	#query{
		display: block;
	}
	
	</style>
</head>
<body>
    <!-- Fixed navbar -->
    <div class="navbar navbar-default navbar-fixed-top" role="navigation"  >
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
		  <div  class="tab-pane active" id="home" data-toggle="context" data-target="#context-menu" style="background-color: #DDDDFF;" >
		  <textarea id="inputcontent" data-toggle="context"   rows="15" cols="150" id="soap" placeholder="Please input your soap here or input orderid then double click to generate end-complete soap."  required></textarea><br/>
			<div id="dblist"  class="form-inline;" style="margin-top: 20px;"></div>
			<div id="message" style="font-family: fantasy;font-size: x-large;font-weight: bold;"></div>
		   <input type="button"  class="btn btn-primary"  value="execute" onclick='sendSoap();' />
		   <input type="button"  class="btn btn-primary"  value="Clear" onclick="myclear();" />
		  </div>
		  <div class="tab-pane" id="profile">
		  	<textarea rows="15" cols="150" id="xmlresult" readonly="readonly" ></textarea>
		  <div  class="table-responsive">
		  	<div class="progress" id="query">
			  <div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 20%;"></div>
			  
			</div>
			<div id="queryresult"></div>
		  </div>
		  </div>
		</div>
	</div> <!-- /container -->
	<div id="context-menu" >
  <ul class="dropdown-menu" role="menu">
    <li><a tabindex="-1" href="#" id="updatedealidmenu">Update dealid</a></li>
  </ul>
</div>
  <div id="mask"></div>
<script src="resources/js/jquery-1.11.1.min.js"></script>
<script src="resources/js/bootstrap.min.js"></script>
<script src="resources/js/icheck.min.js"></script>
<script src="resources/js/html5shiv.js"></script>
<script src="resources/js/bootstrap-contextmenu.js"></script>
<script src="resources/js/userfile/loopload.js"></script>
<script src="resources/js/userfile/uploadfile.js"></script>
<script type="text/javascript">

function myclear(){
	$("#xmlresult").val("");
	$("#inputcontent").val("");
	initQueryDiv();
	$("#message").html("");
}


$(function(){ 
	$("#xmlresult").hide();
	$("#queryresult").show();
	$('.contextmenu').contextmenu();	
	$("#updatedealidmenu").click(function(){
		setupdateoppid();
	});
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
function checkNum(str){
	var reg = new RegExp("^[0-9]*$");
	 if(!reg.test(str)){
		// alert('orderid must be a numnber');
		 //$("#inputcontent").val("");
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
	 $("#inputcontent").val("oppidupdate:"+sinput);
}
function generateECXml(sinput){
	if(!sinput){
		return ;
	}
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
function showloading(){
	$("#mask").show();
}
function hideloading(){
	$("#mask").animate({opacity:'toggle'},1000);
	/* $("#mask").hide(); */
}
function loadInputcontent(){
	var sinput=$("#inputcontent").val();
	sinput=sinput.Trim();
	return sinput;
}
function sendSoap(){
	$("#xmlresult").val("");
	var sinput=loadInputcontent();
	var select=$('input[name="env"]:checked').val();
	
	if(!sinput){
		alert('Please input orderid or soap');
		return ;
	}
	//query DB
	var start=sinput.substr(0,6);
	start=start.toLowerCase();
	if(start=='select'||start=='update'||start=='delete'||start=='insert'){
		queryDB(select,sinput);
		return ;
	}else{
		if(sinput.indexOf('oppidupdate:')==-1){
			generateECXml(sinput);
			sinput=loadInputcontent();
		}
		if(sinput.indexOf('SOAP:Envelope xmlns:SOAP')==-1 && sinput.indexOf('oppidupdate:')==-1){
			return;
		}
		$.ajax({  
            url: "sendSoap.do",  
            data: {env:select,soap:sinput},           
            method: "post",  
            beforeSend:function(){
            	showloading();
            },  
            complete:function(data){  
            	hideloading();
        	},  
            success: function(data) {  
            	copytoboard(data);
	   			$("#xmlresult").val(data);
	   			$("#xmlresult").show();
	   			$("#queryresult").hide();
	   			$('#myTab a:last').tab('show'); 
            }  
        }); 
	}
}
function copytoboard(str){
	try{
		if(document.all){
			window.clipboardData.setData("Text",str); 
		}else{
			//alert(' only can copy in IE');
		}
	} catch(err) {}
}
function initQueryDiv(){
	$("#queryresult").html("");
}
function queryDB(env,str){
	$.post("executeQuery.do",{env:env,sqls:str},function(data){
		 $("#xmlresult").hide();
		 $("#queryresult").show();
		$('#myTab a:last').tab('show');
		var json=eval(data);
		initQueryDiv();
		loop_md5=json.md5;
		loop_count=json.total;
		$("#query .progress-bar-info").css("width",progress+"%");
		$("#query .progress-bar-info").text(progress+" %");
		$("#query").show();
		var str="<div class=\"col-md-9\">init successfully, please wait,,,"+loop_md5+"<div>";
		$("#queryresult").append(str);
	 });
}
String.prototype.Trim = function()  
{  
	return this.replace(/(^\s*)|(\s*$)/g, "");  
} ; 
String.prototype.LTrim = function()  
{  
return this.replace(/(^\s*)/g, "");  
}  ;
String.prototype.RTrim = function()  
{  
return this.replace(/(\s*$)/g, "");  
}  ;
</script>
</body>
</html>
