loop_md5="",loop_load=0,loop_count=0,loop_startquery='true',progress=20;
function appendData(list){
	if(list){
		list=eval(list);
		var str="";
		$.each(list, function(i, o) {
			str+= table="<table class=\"table table-striped\"><tbody>";
			str+="<tr><td>index:"+o["seq"]+"</td><td>"+o["sql"]+"</td><td>cost:"+o.cost+"</td></tr><tr><td colspan='3'>";
			var result=eval("("+o["result"]+")");
			var first=true;
			var title="<thead>";
			var subrow="";
			str+="<table>";
			$.each(result, function(j, row) {
				subrow+="<tr>";
				for(key in row){
					if(first){
						title+="<th>&nbsp;&nbsp;&nbsp;"+key+"&nbsp;&nbsp;&nbsp;&nbsp;  </th>";
					}
					subrow+="<td>"+row[key].toString()+"</td>";
				}
				first=false;
				subrow+="</tr>";
			});
			title+="</thead>";
			str+=title;
			str+=subrow;
			str+="</table></td></tr></tbody></table>";
		});
		$("#queryresult").append(str);
	}
}
function initparam(){
	loop_load=0;
	loop_md5=null;
	loop_startquery='true';
	progress=20;
}
function loaddata(){
	if(loop_md5){
		if(progress==100){
			progress==0;
			$("#query").animate({opacity:'hide'},1000);
			initparam();
			return;
		}else{
			if(progress<=30){
				progress+=3;
			}else if(progress>30 && progress<50){
				progress+=1;
			}
		}
		$("#query .progress-bar-info").css("width",progress+"%");
		$("#query .progress-bar-info").text(progress+" %");
		$.ajax({url:'loadData.do',
					data:{
								md5:loop_md5,
								load:loop_load,
								count:loop_count,
								startquery:loop_startquery
							},
					type:'post',    
				    cache:false,    
				    dataType:'json',
					success:function(data,textStatus){
						var json=eval(data);
						
						loop_load=json.load;
						loop_count=json.count;
						var temp=(loop_load*100)/loop_count;
						if(temp>progress){
							progress=temp;
						}
						$("#query .progress-bar-info").css("width",progress+"%");
						$("#query .progress-bar-info").text(progress+" %");
						loop_startquery=json.startquery;
						var flag=json.flag;
						if(flag=='false'){
							initparam();
						}
						 appendData(json.list);
					},
					error:function(XMLHttpRequest, textStatus, errorThrown){
						//alert(textStatus);
						//alert(errorThrown);
						initparam();
					}
			
		});
	}else{
		initparam();
		$("#query").animate({opacity:'hide'},1000);
	}	
}
setInterval(loaddata,1500);