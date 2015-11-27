<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="../control/head.jsp" %>
    <title>模板上传</title>
</head>
<body>
<%@include file="../control/header.jsp" %>
<div id="content">
    <div id="content-header">
        <div id="breadcrumb">
            <a href="${baseURI}/index" title="返回首页" class="tip-bottom"><i class="icon-home"></i>首页</a>
            <a href="${baseURI}/preUpload" class="current">模板上传</a>
        </div>
        <h1>VR校验</h1>
    </div>
    <style>
        #templateNum {
            width: 150px !important;
            margin: 0 30px 0 0;
        }

        #textarea {
            color: #aaa;
        }

        #uploadTip, #templateTip {
            margin-left: 30px;
            color: red;
            display: none;
        }

        .uploadifive-button {
            float: left;
            border: 0;
            margin-right: 20px;
        }

        #start_upload {
            width: 100px;
            height: 35px;
            line-height: 25px;
            text-align: center;
            margin-bottom: 10px;
        }

        #queue {
            padding: 0 3px 3px;
            border: 1px solid #E5E5E5;
            min-height: 200px;
            margin-bottom: 10px;
            margin-left: 0;
            overflow: auto;
            clear: both;
        }
    </style>
    <div class="container-fluid">
        <hr />
        <div class="row-fluid">
            <div class="span12">
                <div class="widget-box">
                    <div class="widget-title">
                    <span class="icon">
                        <i class="icon-th-list"></i>
                    </span>
                        <h5>模板上传</h5>
                    </div>
                    <div class="widget-content nopadding">
                        <form class="form-horizontal">
                            <div class="control-group">
                                <label class="control-label">文件上传</label>

                                <div class="controls">
                                    <input id="file_upload" name="file_upload" type="file" multiple="true" class="btn btn-primary">
                                    <button id="start_upload" type="button" class="btn btn-primary uploadifive-button">开始上传</button>
                                    <span id="uploadTip"></span>
                                    <div id="queue" class="span10"></div>
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label">模板号</label>

                                <div class="controls">
                                    <input type="text" id="templateNum" name="templateNum">
                                    <button type="button" id="validate" class="btn btn-primary">验证</button>
                                    <span id="templateTip">请输入模板号</span>
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label">验证结果</label>
                                <div class="controls">
                                    <textarea id="textarea" name="textarea" rows="10" class="span10">点击"验证"按钮开始校验文件</textarea>
                                </div>
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
        $('#file_upload').uploadifive({
            'auto': false,
            'buttonText': '选择文件',
            'queueID': 'queue',
            'uploadScript': 'upload?type=xml',
            'onUploadComplete': function (file, data) {
                $('#uploadTip').text('上传成功！').show();
            }
        });
        $('#start_upload').click(function () {
            $('#file_upload').uploadifive('upload');
        });

        $('#validate').click(function () {
            if ($('#templateNum').val().trim() == '') {
                $('#templateTip').show();
                return false;
            }
            $.ajax({
                type: 'post',
                dataType: 'text',
                url: 'validate',
                data: {
                    templateNum: $('#templateNum').val()
                },
                success: function (data) {
                    $('#textarea').val(data).css('color', 'red');
                }
            });
        });
        $('#templateNum').keyup(function () {
            if ($(this).val().trim() != '')
                $('#templateTip').hide();
        }).keydown(function (event) {
            if (event.keyCode == 13)
                return false;
        });
    })();
</script>
</body>
</html>