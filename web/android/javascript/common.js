function initToastr(){
	toastr.options = {
			  "closeButton": false,
			  "positionClass": "toast-top-center",
			  "timeOut": "3000"
			};
}
function showInfo(msg){
	initToastr();
	toastr.info(msg);
}
function showWarning(msg){
	initToastr();
	toastr.warning(msg);
}
function showSuccess(msg){
	initToastr();
	toastr.success(msg);
}
function showError(msg){
	initToastr();
	toastr.error(msg);
}
function showConfirm(title,msg,even){
	confirm(title, msg, even, {confirmButtonText: '确定', cancelButtonText: '取消', width: 300,height:200});
}
function closeShow(){
	$(".BeAlert_overlay,.BeAlert_box").remove();
}

/**
 * 切换到登陆界面
 */
function logout(){
	var sof = window.longmai;
	sof.SOF_Disconnect();
	window.location.href='myTrade.do?method=logout';
}
