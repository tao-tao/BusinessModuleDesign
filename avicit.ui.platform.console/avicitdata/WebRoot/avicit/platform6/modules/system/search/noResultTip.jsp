<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div id="noResultTip" style="margin-top: 35px;margin-left: 395px" >  
	<p class="STYLE1" >没有符合条件的结果</p>
	
	<div id="noResultTip" style="margin-top: 50px" > 
		<p class="STYLE3" >在您的密级和权限范围内，找不到和您查询关键词&nbsp;<font class="STYLE4">${keyword}</font>&nbsp;相符的内容和信息</p>
		
		<p class="STYLE3" >您可以尝试：</p>
		<ul>
			<li class="STYLE3">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			    检查输入字词有无错误
			</li>
			<li class="STYLE3">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				是否包含系统不识别的外国语文字或特殊字符
			</li>
			<li class="STYLE3">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			    换用其他相关的查询字词
			</li>
			<li class="STYLE3">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				改用其他较常见的中英文字词
			</li>
			<li class="STYLE3">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				联系管理员，添加你输入的专业词汇
			</li>
			<li class="STYLE3">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			   联系管理员，在索引中收录您期望查找的内容
			</li>
		</ul>
	</div>
</div>

