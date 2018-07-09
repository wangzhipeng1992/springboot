function goPage() {
	var pageNumber = $.trim($("#pageNumber").val());
	if (!pageNumber) {
		alert("请输入页数/Please enter the page number");
		return false;
	}
	if (!isPositiveInteger(pageNumber)) {
		alert("请输入正确的页数/Invalid page number");
		return false;
	}
	var requestData = {
		"pageIndex" : pageNumber,
		"pageSize" : $("#pageSizeSelect").val()
	};
	loadData(requestData);
}

function selectData() {
	var requestData = {
		"pageIndex" : "1",
		"pageSize" : $("#pageSizeSelect option:first").val()
	};
	loadData(requestData);
}

function nextPage() {
	if (Number($("#currentPageNumber").val()) >= Number($("#totalPages").text())) {
		return false;
	}
	var currentPageNumber = $("#currentPageNumber").val();
	var requestData = {
		"pageIndex" : String(++currentPageNumber),
		"pageSize" : $("#currentPageSize").val()
	};
	loadData(requestData);
}

function prevPage() {
	if (Number($("#currentPageNumber").val()) <= 1) {
		return false;
	}
	var currentPageNumber = $("#currentPageNumber").val();
	var requestData = {
		"pageIndex" : String(--currentPageNumber),
		"pageSize" : $("#currentPageSize").val()
	};
	loadData(requestData);
}

// 每页条数变动时isCompulsive传true，其他传false或不传
function firstPage(isCompulsive) {
	if (!isCompulsive && Number($("#currentPageNumber").val()) <= 1) {
		return false;
	}
	var requestData = {
		"pageIndex" : "1",
		"pageSize" : $("#currentPageSize").val()
	};
	// isCompulsive为true时，取页面实际显示的每页条数
	if(isCompulsive){
		requestData["pageSize"] = $("#pageSizeSelect").val();
	}
	loadData(requestData);
}

function lastPage() {
	if (Number($("#currentPageNumber").val()) >= Number($("#totalPages").text())) {
		return false;
	}
	var requestData = {
		"pageIndex" : $("#totalPages").text(),
		"pageSize" : $("#currentPageSize").val()
	};
	loadData(requestData);
}

function setCurrentPagesAndCount(totalCount, perCount, currentPageNumber){
	$("#totalPages").text(Math.ceil(totalCount/perCount));
	$("#pageSizeSelect").val(perCount);
	$("#pageNumber").val(currentPageNumber);
	$("#currentPageSize").val(perCount);
	$("#currentPageNumber").val(currentPageNumber);
}

//列表页面删除或者批复后调用此方法刷新列表
function reloadPaging(){
	//此时删除后当前页还有数据,刷新后回到当前页码
	if($("table#listTable tr").length > 3){
		var requestData = {
			"pageIndex": $("#currentPageNumber").val(),
			"pageSize": $("#currentPageSize").val()
		};
		loadData(requestData);
	}
	//此时删除后当前页已无数据
	else{
		//若当前已经是第一页
		if($("#currentPageNumber").val() == '1'){
			var requestData = {
				"pageIndex": "1",
				"pageSize": $("#currentPageSize").val()
			};
			loadData(requestData);
		}
		// 若当前不是第一页，回到前一页
		else{
			prevPage();
		}
	}
}