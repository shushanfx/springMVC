<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!--Header-part-->
<div id="header">
    <h1><a href="/">Matrix Admin</a></h1>
</div>
<!--close-Header-part-->
<!--top-Header-menu-->
<div id="user-nav" class="navbar navbar-inverse">
    <ul class="nav">
    </ul>
</div>
<!--close-top-Header-menu-->
<!--start-top-serch-->
<div id="search">
    <input type="text" placeholder="Search here..."/>
    <button type="submit" class="tip-bottom" title="Search"><i class="icon-search icon-white"></i></button>
</div>
<!--close-top-serch-->
<div id="sidebar">
    <ul>
        <li><a href="${baseURI}/index"><i class="icon icon-home"></i> <span>主页</span></a></li>
        <li class="submenu"><a href="#"><i class="icon icon-th-list"></i> <span>xml校验</span> <span
                class="label label-important">3</span></a>
            <ul>
                <li><a href="${baseURI}/validation/index">模板管理</a></li>
                <li><a href="${baseURI}/validation/preUpload">上传模板</a></li>
                <li><a href="${baseURI}/validation/preValidate">模板校验</a></li>
            </ul>
        </li>
        <li class="submenu"><a href="#"><i class="icon icon-th-list"></i> <span>xml预览</span> <span
                class="label label-important">2</span></a>
            <ul>
                <li><a href="${baseURI}/validation/index">新闻预览</a></li>
                <li><a href="${baseURI}/validation/preValidate">普通xml预览</a></li>
            </ul>
        </li>
        <li><a href="${baseURI}/json/list"><i class="icon icon-tint"></i> <span>JSON工具</span></a></li>
        <li><a href="/tool/vr"><i class="icon icon-tint"></i> <span>VR工具</span></a></li>
        <li><a href="/theme/matrix/index.html"><i class="icon icon-star"></i> <span>静态代码</span></a></li>
        <li class="content"><span>Monthly Bandwidth Transfer</span>
            <div class="progress progress-mini progress-danger active progress-striped">
                <div style="width: 77%;" class="bar"></div>
            </div>
            <span class="percent">77%</span>
            <div class="stat">21419.94 / 14000 MB</div>
        </li>
        <li class="content"><span>Disk Space Usage</span>
            <div class="progress progress-mini active progress-striped">
                <div style="width: 87%;" class="bar"></div>
            </div>
            <span class="percent">87%</span>
            <div class="stat">604.44 / 4000 MB</div>
        </li>
    </ul>
</div>
