
//一般格式为  http://localhost:60002/
var root = getRootPath();

/**
 * ajax请求后台数据
 * 
 * @param url 		请求URl 示例：warningPlatformController/getPlatform
 * @param params	拼接请求参数 typeCode=-1 
 * @returns json数据对象 
 */
function ajaxGetData(url,params) {
	var obj = null;
	$.ajax({
		type : 'get',
		url : root + url+"?"+params,
		async : false,
		cache : false,
		dataType : 'json',
		success : function(data) {
			obj = data;
		}
	});
	return obj;
}

/**
 * ajax请求后台(异步),并处理业务逻辑
 * 
 * @param url 		请求URl 示例：warningPlatformController/getPlatform
 * @param params	拼接请求参数 typeCode=-1
 * @param callback	业务逻辑处理 
 * @returns json数据对象 
 */
function ajaxGetDataCall(url,params,callback) {
	var obj = null;
	$.ajax({
		type : 'get',
		url : root + url+"?"+encodeURI(params),
		async : true,
		cache : false,
		dataType : 'json',
		success : function(data) {
			if(callback!=null){
				if (typeof (callback) == 'function') {
					callback(data);
			    }
			}
		}
	});
}
/**
 * 获取请求后台的URl
 * 
 * @param url 		请求URl 示例：warningPlatformController/getPlatform
 * @param params	拼接请求参数 typeCode=-1 
 * @returns json数据对象 
 */
function getRequestUrl(url,params) {
	return root + url+"?"+params;
}

/**
 * 由JSON字符串转换为JSON对象
 * 
 * @param str json格式字符串
 */
function transStrToJosnObj(str) {
	return eval('(' + str + ')');
}
/**
 * js获取项目根路径
 */
function getRootPath() {
	// 获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
	var curWwwPath = window.document.location.href;
	// 获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
	var pathName = window.document.location.pathname;
	var pos = curWwwPath.indexOf(pathName);
	// 获取主机地址，如： http://localhost:8083
	var localhostPaht = curWwwPath.substring(0, pos);
	// 获取带"/"的项目名，如：/uimcardprj
	var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
	//return (localhostPaht + projectName);
	return localhostPaht + '/';
}
/**
 * 获取列表中的最大值
 * @param dataList 对象数组
 * @param item 数据项
 */
function getMax(dataList,item){
	if(dataList==null||dataList.length==0){
		return null;
	}
	if(item==null||item==""){
		var max = dataList[0];
		for(var i=1;i<dataList.length;i++){ 
			if(max<dataList[i]){
				max=dataList[i];
			}
		}
		return max;
	}else{
		var max = dataList[0][item];
		for(var i=1;i<dataList.length;i++){ 
			if(max<dataList[i][item]){
				max=dataList[i][item];
			}
		}
		return max;
	}
}
/** 
 * 获取当前URL参数值 集合
 * @param name  参数名称 
 * @return  参数值 
 */  
 
function getUrlVars() {
	var vars = [], hash;
	var href = window.location.href;
	if(href.lastIndexOf("#")==href.length-1){
		href=href.substring(0,href.length-1);
	}
	var hashes = href.slice(
			href.indexOf('?') + 1).split('&');
	for (var i = 0; i < hashes.length; i++) {
		hash = hashes[i].split('=');
		vars.push(hash[0]);
		vars[hash[0]] = hash[1];
	}
	return vars;
}
//产品类型
function productType(){    
   $.ajax({
		type:"get",
		url:getRootPath()+"/typeListController/getType?temp="+new Date().getTime(), 		
		success: function(data){
			var ywNavCon=template("productType",data);
			var html =	"<li class='active'>全部</li>"+
						"<li>手机</li>"+
						"<li>汽车</li>"+
						"<li>食品</li>"+
						"<li>家电</li>"+
						"<li>化妆品</li>"+
						"<li>电梯</li>";
			html = html+ywNavCon;
			$("#productTypeList").html(html);
			$(".set-list-ul li").click(function() {
				$(this).addClass("active").siblings().removeClass("active");
			});
			$('.set-list-ul li').click(function(){
				$("#searchInputPanorama").val("");
				$("#searchInputArea").val("");
				loadDataForHtml();
			});
		},
		
	})
	
}

//产品类型--重点产品专用
function productType2(){    
   $.ajax({
		type:"get",
		url:getRootPath()+"/typeListController/getType?temp="+new Date().getTime(), 		
		success: function(data){
			var ywNavCon=template("productType",data);
			var html =	'<li class="set-list active" onclick="getSeriousProblemProduct($(this).text())">手机</li>'+
						'<li class="set-list" onclick="getSeriousProblemProduct($(this).text())">汽车</li>'+
						'<li class="set-list" onclick="getSeriousProblemProduct($(this).text())">食品</li>'+
						'<li class="set-list" onclick="getSeriousProblemProduct($(this).text())">家电</li>'+
						'<li class="set-list" onclick="getSeriousProblemProduct($(this).text())">化妆品</li>'+
						'<li class="set-list" onclick="getSeriousProblemProduct($(this).text())">电梯</li>';
			html = html+ywNavCon;
			$("#productTypeList").html(html);
			$(".set-list-ul li").click(function() {
				$(this).addClass("active").siblings().removeClass("active");
			});
			var industryName = getQueryString('industryName');
			if(industryName != ''){
				$(".set-list-ul li").removeClass("active");
			}
			industryHight(industryName);
		},
		
	})
	
}

//系统操作：退出
$(".logout").click(function(){
	$.ajax({
		type :'post',
		url : getRootPath() + "zj/logout",
		async : false,
		cache : false,
		success : function(data) {						
			if(data.result=="0"){
				location.href= getRootPath() + "zj/login.html";
			}else{
				return;
			}
		}
	});
})

	