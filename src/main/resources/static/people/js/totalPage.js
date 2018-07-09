var userInfoTrCount = $("#userInfoTable tr").length;
var countNum = $("#userInfoTable tr").filter(":lt("+(userInfoTrCount-3)+"):gt(1)").length;
$('#light-pagination').pagination({
	pages: Math.ceil(countNum/3),
	cssStyle: 'light-theme'
});
