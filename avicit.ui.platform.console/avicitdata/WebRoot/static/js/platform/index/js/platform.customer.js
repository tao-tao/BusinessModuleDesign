/**
 *说明：时间计算函数，对某个固定时间进行任意时间加减。
 *参数一(dtDate)：时间类型或者字符类型，加减参照时间，固定时间，可以是date类型，也可以是格式为"yyyy-MM-dd HH:mm:ss"的字符串。
 *参数二(addOrReduce)：字符类型，"+"或者"-"。
 *参数三(offset)：加减步长。
 *参数四(strInterval)：加减时间的单位，6种"年"、"月"、"天"、"时"、"分"、"秒"。
 *参数五(returnType)：返回值类型，若为null或者字符串"date"，返回date类型，若为'yyyy-MM-dd'、'yyyy-MM-dd HH:mm'、'yyyy-MM-dd HH:mm:ss'则返回固定格式的字符串。
 *返回值:date类型或者格式化的字符串。
 */
function calcDate(dtDate,addOrReduce,offset,strInterval,returnType){
	if(isNaN(offset)) return "";
	var year,month,day,hour,minute,second;
	if(typeof(dtDate)=="object"){
	  	year=dtDate.getFullYear();
		month=dtDate.getMonth()+1;
		if(month*1<10) month='0'+month;
		day=dtDate.getDate();
		if(day*1<10)day='0'+day;
		hour=dtDate.getHours();
		if(hour*1<10)hour='0'+hour;
		minute=dtDate.getMinutes();
		if(minute*1<10)minute='0'+minute;
		second=dtDate.getSeconds();
		if(second*1<10)second='0'+second;
	}else if(typeof(dtDate)=="string"){
		year=dtDate.substring(0,4);
		if(isNaN(year)) return "";
		month=dtDate.substring(5,7);
		if(isNaN(month)) return "";		
		day=dtDate.substring(8,10);
		if(isNaN(day)) return "";
		hour=dtDate.substring(11,13);
		if(isNaN(hour)) return "";
		minute=dtDate.substring(14,16);
		if(isNaN(minute)) return "";
		second=dtDate.substring(17,19);
		if(isNaN(second)) return "";
	}	
	month=month*1-1;
	var date="";
	if(addOrReduce=="+"){
		switch(strInterval){
			case '年': date=new Date(year*1+offset*1,month,day,hour,minute,second);break;
			case '月': date=new Date(year,month*1+offset*1,day,hour,minute,second);break;
			case '天': date=new Date(year,month,day*1+offset*1,hour,minute,second);break;
			case '时': date=new Date(year,month,day,hour*1+offset*1,minute,second);break;
			case '分': date=new Date(year,month,day,hour,minute*1+offset*1,second);break;	
			case '秒': date=new Date(year,month,day,hour,minute,second*1+offset*1);break;		
			default: return date;
		}
	}else if(addOrReduce=="-"){
		switch(strInterval){
			case '年': date=new Date(year*1-offset*1,month,day,hour,minute,second);break;
			case '月': date=new Date(year,month*1-offset*1,day,hour,minute,second);break;
			case '天': date=new Date(year,month,day*1-offset*1,hour,minute,second);break;
			case '时': date=new Date(year,month,day,hour*1-offset*1,minute,second);break;
			case '分': date=new Date(year,month,day,hour,minute*1-offset*1,second);break;	
			case '秒': date=new Date(year,month,day,hour,minute,second*1-offset*1);break;		
			default: return date;
		}
	}
	if(null==returnType||returnType=="date") return date;
	else{
		year=date.getFullYear();
		month=date.getMonth()+1;
		if(month*1<10) month='0'+month;
		day=date.getDate();
		if(day*1<10)day='0'+day;
		hour=date.getHours();
		if(hour*1<10)hour='0'+hour;
		minute=date.getMinutes();
		if(minute*1<10)minute='0'+minute;
		second=date.getSeconds();
		if(second*1<10)second='0'+second;
		var newdate="";
		switch(returnType){
			case 'yyyy-MM-dd': newdate=year+"-"+month+"-"+day;break;
			case 'yyyy-MM-dd HH:mm': newdate=year+"-"+month+"-"+day+" "+hour+":"+minute;break;
			case 'yyyy-MM-dd HH:mm:ss': newdate=year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;break;
			default: return newdate;
		}
	}	
	return newdate;
}