<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="../control/head.jsp" %>
    <title>模板校验</title>
</head>
<body>
<%@include file="../control/header.jsp" %>
<div id="content">
    <div id="content-header">
        <div id="breadcrumb">
            <a href="${baseURI}/index" title="返回首页" class="tip-bottom"><i class="icon-home"></i>首页</a>
            <a href="${baseURI}/preValidate" class="current">模板校验</a>
        </div>
        <h1>模板校验</h1>
    </div>
    <div class="container-fluid">
        <hr />
        <div class="row-fluid">
            <div class="span12">
                <div class="widget-box">
                    <div class="widget-title">
                    <span class="icon">
                        <i class="icon-th-list"></i>
                    </span>
                        <h5>模板校验</h5>
                    </div>
                    <div class="widget-content nopadding">
                        <form class="form-horizontal" id="form">
                            <div class="control-group">
                                <label class="control-label">模板号</label>
                                <div class="controls">
                                    <input type="number" id="templateNum" name="templateID" maxlength="7" min="0" step="0">
                                    <span id="templateTip" class="by label hidden">请输入模板号</span>
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label">校验方式</label>
                                <div class="controls">
                                    <input type="radio" name="type" value="3" checked />
                                    <span>填写XML地址</span>
                                    <input type="radio" name="type" value="1"/>
                                    <span>填写XML内容</span>
                                </div>
                                <div class="controls" id="divType3">
                                    <input name="xml" id="txtType1" type="url" class="span10" placeholder="输入xml的地址" />
                                </div>
                                <div class="controls hidden" id="divType1">
                                    <textarea id="txtType3" name="xml" rows="10" class="span10" disabled placeholder="输入xml的内容"></textarea>
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label">验证结果</label>
                                <div class="controls">
                                    <textarea disabled id="textarea" name="textarea" rows="10" class="span10" placeholder="点击【验证】按钮开始校验文件" readonly></textarea>
                                </div>
                            </div>
                            <div class="form-actions">
                                <button type="submit" class="btn btn-success" id="validate">验证</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="../control/footer.jsp" %>
<link rel="stylesheet" href="${baseURI}/plugin/uploadifive/css/uploadifive.css"/>
<script type="text/javascript" src="${baseURI}/plugin/uploadifive/js/jquery.uploadifive.min.js"></script>
<script>
    (function () {
        $("input[name='type']").click(function(){
            var value = $(this).val(),
                    $parent = $(this).parent().parent(),
                    id = "divType" + value;
            $parent.children(".controls").each(function(index, item){
                if(index !== 0){
                    var cid = $(item).attr("id");
                    if(cid == id){
                        $(item).find("input,textarea").removeAttr("disabled");
                        $(item).removeClass("hidden");
                    }
                    else{
                        $(item).find("input,textarea").attr("disabled", "disabled");
                        $(item).addClass("hidden");
                    }
                }
            });

        });
        $('#validate').click(function () {
            var obj = {};
            if ($('#templateNum').val().trim() == '') {
                $('#templateTip').removeClass("hidden");
                return false;
            }
            $.ajax({
                type: 'post',
                dataType: 'json',
                url: 'validate',
                data: $("#form").serialize(),
                success: function (data) {
                    var message = null;
                    if(data && data.message){
                        //message = data.message.replace(/\n/gi, "<br />");
                        message = data.message;
                        $('#textarea').val(message).css('color', 'red');
                    }
                    else{
                        message = "";
                    }
                }
            });
            return false;
        });
        $('#templateNum').keyup(function () {
            var reg = /\d+/gi
            if (reg.exec($(this).val())){
                $('#templateTip').addClass("hidden");
            }
            else{
                $('#templateTip').removeClass("hidden");
            }
        }).keydown(function (event) {
            if (event.keyCode == 13){
                return false;
            }
        });
    })();
</script>
</body>
</html>