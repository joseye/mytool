<html>
<head>
<title>my tool</title>
<link href="resources/css/bootstrap-theme.min.css" rel="stylesheet">
<link href="resources/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container theme-showcase" role="main">
<div class="page-header">
        <h1>Here is a  tool for BLiS</h1>
</div>
<div style="width: 500px;margin-bottom: 50px;">
order id: <input type="text" maxlength="20" id="orderid" class="form-control" placeholder="Please Input the order id here " required  >
oppid id: <input type="text" value="1019969" name="oppid" id="oppid" maxlength="20" class="form-control" placeholder="Please Input the opp id here " required  >
<div id="dblist"></div>
</div>
<div  class="container" >
<input type="button"  class="btn btn-primary" value="get status" onclick="getstatus()" />
<input type="button"  class="btn btn-primary" value="updateoppid" onclick="updateOppid()"/>
<input type="button"  class="btn btn-primary"  value="send end-complete soap" onclick="endComplete()" />
</div>
<br/>
<textarea rows="10" cols="100" id="soap"></textarea><br/>
<input type="button" class="btn btn-primary" value="send soap"  onclick="sendSoap()" />
<div id="statusDIV"></div>

</div>


<script src="resources/js/jquery-1.11.1.min.js"></script>
<script src="resources/js/bootstrap.min.js"></script>
<script type="text/javascript">
 $.get("getDBInfos.do",function(data){
     var json = $.parseJSON(data);
     $(json).each(function(index, obj){
         var env = json[index];
         str="<label class='radio'><input type='radio' name='env' ";
         if(env=='DEV'){
        	 str+="checked";
         }
         str+=" value='"+env+"' >"+env+"&nbsp;&nbsp;&nbsp;</label>"
         $("#dblist").append(str);
     });
     
 });
 function getstatus(){
	 var orderid=$("#orderid").val();
	 if(!orderid){
		 alert('Please input the order id');
		 return ;
	 }
	 var select=$('input[name="env"]:checked').val();
	 $.get("getOrdersatus.do?env="+select+"&orderid="+orderid,function(data){
		 alert(data);
	 });
 }
 function updateOppid(orderid){
	 var orderid=$("#orderid").val();
	 if(!orderid){
		 alert('Please input the order id');
		 return ;
	 }
	 var select=$('input[name="env"]:checked').val();
	 $.get("updateOpp.do?env="+select+"&orderid="+orderid+"&oppid="+$("#oppid").val(),function(data){
		 alert(data);
	     
	 });
 }
 

 
 function endComplete(){
	 var orderid=$("#orderid").val();
	 if(!orderid){
		 alert('Please input the order id');
		 return ;
	 }
	 var select=$('input[name="env"]:checked').val();
	 $.get("endcomplete.do?env="+select+"&orderid="+orderid,function(data){
	     	alert(data);
	 });
 }
 
 function sendSoap(){
	 var soap=$("#soap").val();
	 if(!orderid){
		 alert('Please input the order id');
		 return ;
	 }
	 var select=$('input[name="env"]:checked').val();
	 $.get("sendSoap.do?env="+select+"&soap="+soap,function(data){
		 alert(data);
	 });
 }
 
 </script>
</body>
</html>
