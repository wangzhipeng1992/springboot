//调用ajax回调
// 在url请求后调用
function callbackResult(data, successFunction, errorFunction) {
	ajaxIsGoing__ = false;
	// 如果data属性是假的(!数据,不存在)
	if (!data || !data.resultCode || data.resultCode !== '0000') {
		if (errorFunction && typeof errorFunction === 'function') {
			if (data) {
				if(data.resultCode === '0002'){
					// 此时后台验证失败，跳转页面
					ssoError(data.resultData);
				}else{
					errorFunction(data.resultDesc);
				}
			} else {
				errorFunction();
			}
		}
		return false;
	}
	if (successFunction && typeof successFunction === 'function') {
		// data指返回结构,data.resultData则获得了指定显示数据(对象)
		successFunction(data.resultData);
	}
}

function ssoError(url){
	var winArray = [window];
	var this_opener = null;
	do{
		if(!this_opener){
			// window.opener：是window.open打开的子页面调用父页面对象
			// window.parent：是iframe页面调用父页面对象
			// window.dialogArguments：模态框调用父级页面对象
			this_opener = window.opener || window.dialogArguments;
		}else{
			this_opener = this_opener.opener || this_opener.dialogArguments;
		}
		// 向数组末尾添加元素，并返回新的长度
		winArray.push(this_opener);
	}while(this_opener);
	// 删除并返回数组的最后一个元素
	winArray.pop();
	if(winArray.length > 1){
		var topWin = winArray.pop();
		topWin.top.location.href = url;
		$.each(winArray, function(index, win){
			win.close();
		});
	}else{
		top.location.href = url;
	}
}

var ajaxIsGoing__ = false;
function beforeSendAjax_() {
	ajaxIsGoing__ = true;
}

function isPositiveInteger(value) {
	if ((/^(\+|-)?[1-9]\d*$/.test(value)) && Number(value) > 0) {
		return true;
	}
	return false;
}

function isCookieEnable() {
	if (navigator.cookieEnabled){
		return true;
	}
	var result = false;
	document.cookie = "testcookie=yes;";
	var cookieSet = document.cookie;
	if (cookieSet.indexOf("testcookie=yes") != -1){
		result = true;
	}
	return result;
}

var __pageMenuCode__ = {
	"1": "DBRW",
	"2": "WDSQ",
	"3": "CGX"
};
//参数：待办详情页1，我的申请详情页2，草稿详情页3
function reloadParentWindow(pageId){
	if(!window.opener || !window.opener.parent || !window.opener.parent.location.href){
		return false;
	}
	var url = window.opener.parent.location.href;
	var menuCode = url.substring(url.lastIndexOf("#") + 1);
	// 判断父页面是不是对应的列表页
	if (menuCode == __pageMenuCode__[String(pageId)]){
		window.opener.reloadPaging();
	}
}


$(function() {
	if (!isCookieEnable()) {
		alert("对不起，您的浏览器的Cookie被禁用，请开启\nSorry,your browser's Cookie is disabled,please open");
	}
	$(document).keydown(function(event) {
		if (event.keyCode == 116) {
			if (ajaxIsGoing__) {
				return false;
			}
		}
	});
});