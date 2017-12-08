 function mate(content){
 	    //匹配英文小写字母
		var reg1 = /[a-z]/g;
		var lowerletterCount = 0;
		if (content.match(reg1) != null) {
			lowerletterCount = content.match(reg1).length;
		}		
		//匹配数字
		var reg2 = /\d/g;
		var numberCount = 0;
		if (content.match(reg2) != null) {
			numberCount = content.match(reg2).length;
		}
		//匹配特殊符号(~！@#￥%^&*（）)
		var reg3 = /[\~!@#\$%\^\&\*\(\)]/g;
		var mark = 0;
		if (content.match(reg3) != null) {
			mark = content.match(reg3).length;
		}	
		// 匹配英文大写字母
		var reg4 = /[A-Z]/g;
        var upperletterCount = 0;
        if (content.match(reg4) != null) {
            upperletterCount = content.match(reg4).length;
        }
		if(lowerletterCount >0){
            	lowerletterCount = 1;
        } 
        if(upperletterCount >0){
               	upperletterCount = 1;
         }
        if(numberCount >0){
               	numberCount = 1;
         }
        if(mark >0){
               	mark = 1;
         }
		return lowerletterCount+upperletterCount+numberCount+mark;
}