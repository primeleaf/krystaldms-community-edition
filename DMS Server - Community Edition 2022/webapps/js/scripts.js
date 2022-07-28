$.validator.setDefaults({
    highlight: function(element) {
        $(element).closest('.mb-3').addClass('has-error');
    },
    unhighlight: function(element) {
        $(element).closest('.mb-3').removeClass('has-error');
    },
    errorElement: 'span',
    errorClass: 'help-block',
    errorPlacement: function(error, element) {
    	if(element.attr("id") == "txtDomainName"){
    		error.insertAfter(element.parent().parent());
    	}else  if(element.parent('.input-group').length) {
            error.insertAfter(element.parent());
        }else{
        	error.insertAfter(element);
        }
    },
    submitHandler: function(form) { 
    	if(form.id == "frmLogin"){
			 $("#btnLogin").html("<i class='spinner-border text-white spinner-border-sm'></i> Please Wait");
		}
    	var formtype = $(form).attr("form-type");
    	if("ajax" == formtype){
    		var container = $(form).attr("datatarget");
    		if(form.id == "frmShareDocument"){
   			 	$("#btnShare").button('loading');
    		}
    		$.ajax({
				type:"POST",
				url  : $(form).attr("action"),
				data : $(form).serialize(),
				success : function (response){
					$(container).html(response);
					if(form.id == "frmChangePassword" || form.id == "frmChangeUserPassword" ){
						$(form).each (function(){
							  this.reset();
						});
					}
					if($("#btnShare").length){
		    			$("#btnShare").button('reset');
		    		}
				}
			});
	    	return false;
    	}
		if("ajaxUpload" == formtype){
			var container = $(form).attr("datatarget");
			if(form.id == "frmComposeDocument"){
				$("#txtDocumentContent").val($(".ql-editor").html());
			}
			// Create an FormData object 
	        var data = new FormData(form);
	        var submitUrl = $(form).attr("action");
			// disabled the submit button
	        $(".btn-wait").prop("disabled", true);
			$(container).html('');
			$('.progress').removeClass("d-none");
			$('.progress-bar').css("width","0%");
	        $.ajax({
	            type: "POST",
	            enctype: 'multipart/form-data',
	            url: submitUrl,
	            data: data,
	            processData: false,
	            contentType: false,
	            cache: false,
	            timeout: 600000,
				dataType: 'html',
	            success: function (response) {
	            	$(container).html("<div class='alert alert-success alert-dismissible' role='alert'><i class='bi bi-check-circle h6 me-1'></i>"+response+"<button type='button' class='btn-close' data-bs-dismiss='alert' aria-label='Close'></button></div>");  
	                $(".btn-wait").prop("disabled", false);
	                $(form).each (function(){
						this.reset();
					});
					$('.progress').addClass("d-none");
					$('.progress-bar').css("width","0%");
					if(form.id == "frmAttachments"){
						url = $(".internal").attr("data-src");
						$("#resultAttachments").load(url);
					}
	            },
	            error: function (e) {
	            	var errorMessage = JSON.parse(e.responseText);
	            	$(container).html("<div class='alert alert-danger alert-dismissible' role='alert'><i class='bi bi-x-circle h6 me-1'></i>"+errorMessage.error+ "<button type='button' class='btn-close' data-bs-dismiss='alert' aria-label='Close'></button></div>");  
	                $(".btn-wait").prop("disabled", false);
					$('.progress').addClass("d-none");
					$('.progress-bar').css("width","0%");
	            },
				xhr: function() {
				        var xhr = new window.XMLHttpRequest();
				        xhr.upload.addEventListener("progress", function(evt) {
				            if (evt.lengthComputable) {
				                var percentComplete = (evt.loaded / evt.total) * 100;
				                $('.progress-bar').css("width", percentComplete+"%");
				            }
				       }, false);
				       return xhr;
				}
	        });
			return false;
		}
    	form.submit();
	}
});

$.validator.addMethod('complexPassword', function(value, element) {
	return this.optional(element) || (value.match(/[a-z]/) && value.match(/[A-Z]/) && value.match(/[0-9]/) && value.match(/[\W_]/));
},'Password must contain 1 lower case ,1 upper case, 1 numeric and  special character.');


$.validator.addMethod('alpha', function(value, element){
	var regex = new RegExp("^[A-Za-z][A-Za-z _]*$");
    var key = value;
    if (!regex.test(key)) {
       return false;
    }
    return true;
},'Invalid input');

$.validator.addMethod('alphaSpace', function(value, element){
	var regex = new RegExp("^[A-Za-z][A-Za-z _]*(?:_[A-Za-z]+)*$");
    var key = value;
    if (!regex.test(key)) {
       return false;
    }
    return true;
},'Invalid input');

$.validator.addMethod("multiemail", function (value, element) {
    if (this.optional(element)) {
        return true;
    }
    var emails = value.split(','),
        valid = true;
 
    for (var i = 0, limit = emails.length; i < limit; i++) {
        value = jQuery.trim(emails[i])
        valid = valid && jQuery.validator.methods.email.call(this, value, element);
    }
    return valid;
}, "Please use a comma to separate multiple email addresses.");

$(document).ready(function() {
	$("#frmEditDocumentClassIndexes").validate({
		submitHandler : function(form) {
			var result = true;
			$(".indexOrder").each(function(){
				var currentIndexVlaue = $(this).val();
				var obj = $(this).closest("tr").next().find(".indexOrder");
				while(typeof obj.val() != "undefined"){
					if(currentIndexVlaue == obj.val()){
						BootstrapDialog.alert("Please select valid Index Order");
						result = false;
						return false;
					}else{
						obj = $(obj).closest("tr").next().find(".indexOrder");
					}
				}
			});
			if(result){
				form.submit();
			}
		}
	});
	
	$('#clock').countdown(timeout).on('update.countdown', function(event) {
		var format = '%H:%M:%S';
		$(this).html(event.strftime(format));
	}).on('finish.countdown', function(event) {
	})
	//Added by Rahul Kubadia on 25-Jan-2016 for 2016 R2
	$('#uptime').countdown(uptime,{elapse: true}).on('update.countdown', function(event) {
		if (event.elapsed) {
			$(this).html(event.strftime('%D:%H:%M:%S'));
		}
	});
	$(document).on("submit", ".modalForm" , function(e){
		e.preventDefault();
		var validator = $(this).validate();  
		if($(this).valid()){
			var container = $(this).attr("datatarget");
			$.ajax({
				type : "POST",
				url  : $(this).attr("action"),
				data : $(this).serialize(),
				success : function (response){
					$(container).html(response);
					validator.resetForm();
				}
			});
	    	return false;
		}
	}); 
	
	$("#frmBulkAction").validate({
		errorElement: 'div',
	    errorContainer: "#errorWrapper",
	    errorLabelContainer:  '#errorDiv',
		errorClass : 'alert alert-danger',
	});
	
	$("#btnBulkDelete").click(function() {
	    if ($("#frmBulkAction").valid()) {
	    	BootstrapDialog.confirm("Are you sure, you want to delete these documents?", function(result){
	            if(result) {
                    $("#frmBulkAction").attr('action','/console/bulkdelete');
	            	$("#frmBulkAction").submit();
	            }
	    	});
	    }
	});
	
	$("#btnBulkDownload").click(function() {
	    if ($("#frmBulkAction").valid()) {
        	$("#frmBulkAction").attr('action','/console/bulkdownload');
        	$("#frmBulkAction").submit();
        }
	});
	
	$("#checkAll").change(function () {
	    $("input:checkbox").prop('checked', $(this).prop("checked"));
	});
	
	$('body').on('hidden.bs.modal', '.modal', function () {
		$(this).removeData('bs.modal');
	});
	
	//$('.tip').tooltip();
	
	const toolTips = document.querySelectorAll(".tip");
	toolTips.forEach(t=> {
		new bootstrap.Tooltip(t);
	});
	
	
	$("form").each(function () { 
		   var validator = $(this).validate();  //add var here
		   validator.resetForm();
		   $("#btnReset").click(function () {
		       validator.resetForm();
		   });
		   $("#btnResetForm").click(function () {
		       validator.resetForm();
		   });
		   $(".reset").click(function () {
		       validator.resetForm();
		   });
	});
	
	$(".internal").click(function(e) { //this function is used for tabs on view document view
		container=$(this).attr("datatarget");
		url = $(this).attr("data-src");
		$(container).html("<div class='text-center'><h3><i class='fa fa-spinner fa-spin'></i> Please wait...</h3></div>");
		$.ajax({
			type:"GET",
			url  : url,
			success : function (response){
				$(container).html(response);
				$(".viewdocument").colorbox({iframe:true,rel:"viewdocument", width:"100%", height:"100%", current : "Document {current} of {total}"});
			}
		});
	});
	
	$(document).on("click",".confirm", function(e) { //confirm delete for ajax and non ajax
		e.preventDefault();
		var targetUrl = $(this).attr("href");
		var title =  $(this).attr("confirmationMessage");
		var container = $(this).attr("datatarget");
		BootstrapDialog.confirm(title, function(result){
            if(result) {
            	if(container){
            		$(container).html("<div class='text-center'><h3><i class='fa fa-spinner fa-spin'></i> Please wait...</h3></div>");
    	        	$(container).load(targetUrl);
            	}else{
            		window.location.href=targetUrl;
            	}
            }
		});
	});
	$('.shortdate').datepicker({
		format: 'yyyy-mm-dd',
		clearBtn:true,
		todayHighlight:true,
		autoclose:true
	});
	
	$('#txtAnnouncementExpiryDate').datepicker("setStartDate",new Date());
	$('#txtExpiryDate').datepicker("setStartDate",new Date());
	
	$( ".autocomplete").typeahead({
		remote: {
			url: "",
			replace: function() {
				var $focused = $(':focus');
				var columnName = $focused.attr("id");
				var classId = $focused.attr("cid");
				var value = $focused.val();
				var myurl = "/console/autocomplete?&term="+encodeURIComponent(value)+"&indexname="+encodeURIComponent(columnName)+"&classid="+encodeURIComponent(classId);
				return myurl;
			}
		},
		limit:10,
		cache:false
	});
	
	$('.tt-hint').addClass('form-control');
	$(".autosubmit").bind("change",function(){
		var classid = $(this).val();
		window.location.href="/console/newdocument?classid="+classid;
	});
	
	$("#cmbIndexType").bind("change",function(){
		var selectedIndex = $('#cmbIndexType :selected').index();
		$("#txtIndexLength").prop('readonly',true);
		switch (selectedIndex){
		case 1 : 
			$("#txtIndexLength").val("10");
			break;
		case 2 : 
			$("#txtIndexLength").val("12");
			break;
		case 3 : 
			$("#txtIndexLength").val("50");
			break;
		case 4 : 
			$("#txtIndexLength").val("100");
			break;
		default:
			$("#txtIndexLength").prop('readonly',false);
				break;
		}
	});
	
	$(".operator").bind("change",function(){
		var selectedValue = $(this).val();
		var indexName = $(this).prop("name");
		var indexControlName =  indexName.substring(9); //removed the initial "operator_"
		$("#" + indexControlName + "_div").addClass("hidden");
		if(selectedValue == 7 ){
			$("#" + indexControlName + "_div").removeClass("hidden");
		}
	});
	
	$("#cmbIndexName").bind("change",function(){
		var indexName = $(this).val();
	    isAutoIndex = false;
	    $("#prefix").hide();
		$("#pattern").hide();
		$("#defaultvalue").show();
		
	    for(i = 0; i < autoIndexes.length; i++){
	        if(indexName == autoIndexes[i]){
	        	isAutoIndex = true;
	        	$("#prefix").show();
				$("#pattern").show();
				$("#defaultvalue").hide();
	            return;
	        }
	    }
	});
	
	$(".selectRow").bind("click", function(){
	    var selectType = $(this).val();
	    var check=eval("$('#cbRow_"+selectType+"').prop('checked')");
	    var elements = $(":input");
	    for(i=0; i<elements.length; i++)   {
	           var element = elements[i];
	           if((element.type == "checkbox") && (element.value == selectType) && (!element.disabled)) {
	                 element.checked =check;
	           }
	    }
	});
	
	$(".selectColumn").bind("click", function(){
	    var selectType = $(this).val();
	    var check=eval("$('#"+selectType+"Column').prop('checked')");
	    var elements = $(":input");
	    for(i=0; i<elements.length; i++)   {
	           var element = elements[i];
	           var val = element.name.indexOf(selectType);
	           if((element.type == "checkbox") && (val == 0) && (!element.disabled)) {
	                 element.checked =check;
	           }
	    }
	});
	$(".viewdocument").colorbox({iframe:true,rel:"viewdocument", width:"100%", height:"100%", current : "Document {current} of {total}", onClosed:function(){window.location.reload(true);}});
	
	$('#profilePic').click(function(){
		 $('input[type=file]').click();
	});

	$("#profilepicture").on("change", function() {
		 $("#profilepiccontent").html("<div class='text-center'><h3><i class='fa fa-spinner fa-spin'></i></div>");
		 $("#frmMyProfile").submit();
	});
	
	
	if( $("#hpUserCount").length > 0 ){
		getLoggedInUserCount();
		setInterval("getLoggedInUserCount()", 5000);
	}
	//Added by Rahul Kubadia on 25-Jan-2016 for 2016 R2
	if( $("#hpDocumentCount").length > 0 ){
		getTotalDocumentCount();
		setInterval("getTotalDocumentCount()", 5000);
	}

});

var getLoggedInUserCount = function(){
	$.ajax({
		url: '/stats?mode=users',
		type: 'GET',
		success: function (data) {
			setTimeout(function(){$('#hpUserCount').html(data);},500);
		},
		error: function (jxhr, msg, err) {
			setTimeout(function(){$('#hpUserCount').html("0");},500);
		}
	});

}

//Added by Rahul Kubadia on 08-Feb-2016 for 2016 R2
var getTotalDocumentCount = function(){
	$.ajax({
		url: '/stats?mode=documents',
		type: 'GET',
		success: function (data) {
			setTimeout(function(){$('#hpDocumentCount').html(data);},500);
		},
		error: function (jxhr, msg, err) {
			setTimeout(function(){$('#hpDocumentCount').html("0");},500);
		}
	});
}
