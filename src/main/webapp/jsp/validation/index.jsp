<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="../control/head.jsp" %>
    <title>模板管理</title>
</head>
<body>
<%@include file="../control/header.jsp" %>
<div id="content">
    <div id="content-header">
        <div id="breadcrumb"><a href="${baseURI}/index" title="返回首页" class="tip-bottom"><i class="icon-home"></i>首页</a>
            <a href="${baseURI}/validation/index" class="current">模板管理</a></div>
        <h1>模板列表</h1>
    </div>
    <div class="container-fluid">
        <hr>
        <div class="row-fluid">
            <div class="span12">
                <div class="widget-box">
                    <div class="widget-title"><span class="icon"><i class="icon-th"></i></span><h5>列表内容</h5></div>
                    <div class="widget-content nopadding">
                        <div class="dataTables_wrapper" role="grid">
                            <div class="">
                                <div class="dataTables_length">
                                    <label>
                                        每页大小:
                                        <select size="1" name="DataTables_Table_0_length" aria-controls="DataTables_Table_0">
                                            <option value="10" <c:if test="${pagination.pageSize == 10}">selected="selected"</c:if> >10</option>
                                            <option value="25" <c:if test="${pagination.pageSize  == 25}">selected="selected"</c:if> >25</option>
                                            <option value="50" <c:if test="${pagination.pageSize  == 50}">selected="selected"</c:if> >50</option>
                                            <option value="100" <c:if test="${pagination.pageSize  == 100}">selected="selected"</c:if> >100</option>
                                        </select>
                                    </label>
                                </div>
                            </div>
                            <table class="table table-bordered data-table dataTable" id="datagrid_rows">
                                <thead>
                                <tr role="row">
                                    <th class="ui-state-default" role="columnheader" tabindex="0"
                                        aria-controls="DataTables_Table_0" rowspan="1" colspan="1" aria-sort="ascending"
                                        aria-label="Rendering engine: activate to sort column descending"
                                        style="width: 239px;">
                                        <div class="DataTables_sort_wrapper">名称<span
                                                class="DataTables_sort_icon css_right ui-icon ui-icon-triangle-1-n"></span>
                                        </div>
                                    </th>
                                    <th class="ui-state-default" role="columnheader" tabindex="0"
                                        aria-controls="DataTables_Table_0" rowspan="1" colspan="1"
                                        aria-label="Browser: activate to sort column ascending" style="width: 80px;">
                                        <div class="DataTables_sort_wrapper">类型<span
                                                class="DataTables_sort_icon css_right ui-icon ui-icon-carat-2-n-s"></span>
                                        </div>
                                    </th>
                                    <th class="ui-state-default" role="columnheader" tabindex="0"
                                        aria-controls="DataTables_Table_0" rowspan="1" colspan="1"
                                        aria-label="Browser: activate to sort column ascending" style="width: 332px;">
                                        <div class="DataTables_sort_wrapper">修改时间<span
                                                class="DataTables_sort_icon css_right ui-icon ui-icon-carat-2-n-s"></span>
                                        </div>
                                    </th>
                                    <th class="ui-state-default" role="columnheader" tabindex="0"
                                        aria-controls="DataTables_Table_0" rowspan="1" colspan="1"
                                        aria-label="Engine version: activate to sort column ascending"
                                        style="width: 332px;">
                                        <div class="DataTables_sort_wrapper">操作<span
                                                class="DataTables_sort_icon css_right ui-icon ui-icon-carat-2-n-s"></span>
                                        </div>
                                    </th>
                                </tr>
                                </thead>

                                <tbody role="alert" aria-live="polite" aria-relevant="all">
                                <c:forEach items="${list}" varStatus="st" var="item">
                                    <tr class="gradeA ${st.index %2 == 0 ? "odd" : "even"}">
                                        <td class=" sorting_1"><c:out value="${item.name}" /></td>
                                        <td class=" ">${item.type}</td>
                                        <td class=" "><fmt:formatDate value="${item.lastModified}" pattern="yyyy-MM-dd HH:mm:ss" /> </td>
                                        <td class="center ">

                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                            <div class="fg-toolbar ui-toolbar ui-widget-header ui-corner-bl ui-corner-br ui-helper-clearfix">
                                <div class="dataTables_filter" id="DataTables_Table_0_filter">
                                    <label>Search: <input type="text" aria-controls="DataTables_Table_0" id="txtFilter" value="${param.name}"></label></div>
                                <%@include file="../control/pagination.jsp"%>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <form action="index" method="get" style="display: none;" id="frmSearch">
        <input type="hidden" name="name" value="${name}"/>
        <input type="hidden" name="pageSize" value="${pagination.pageSize}" />
        <input type="hidden" name="pageIndex" value="${pagination.pageIndex}" />
    </form>
</div>
<%@include file="../control/footer.jsp" %>
<script type="text/javascript">
    $(function(){
        var f = document.getElementById("frmSearch");
        $("[name='DataTables_Table_0_length']").change(function(e){
            f.pageSize.value = this.value;
            f.submit();
        });
        $(".dataTables_paginate a").click(function(e){
            var $item = $(this);
            if(!$item.hasClass(".ui-state-disabled")){
                f.pageIndex.value = ($item.attr("data-index"));
                f.submit();
            }
            e.preventDefault();
        });
        $("#txtFilter").keyup(function(e){
            if(e.keyCode == 13){
                f.name.value = $.trim(this.value);
                f.pageIndex.value = 1;
                f.submit();
            }
        })
    });
</script>
</body>
</html>
