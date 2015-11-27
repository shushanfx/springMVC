<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!--Footer-part-->
<div class="row-fluid">
    <div id="footer" class="span12"> 2013 &copy; Matrix Admin. Brought to you by <a href="http://themedesigner.in/">Themedesigner.in</a>
    </div>
</div>
<!--end-Footer-part-->
<script src="${baseURI}/theme/matrix/js/jquery.min.js"></script>
<script src="${baseURI}/theme/matrix/js/jquery.ui.custom.js"></script>
<script src="${baseURI}/theme/matrix/js/bootstrap.min.js"></script>
<script src="${baseURI}/theme/matrix/js/matrix.js"></script>
<script type="text/javascript">
    $(function(){
        var sideBarComponent = $("#sidebar");
        var initMenu = function(){
            var url = window.location.pathname;
            if(!url || url === "/"){
                url = "/index";
            }
            $("#sidebar").find(">ul a").each(function(index,item){
                var href = $.trim($(item).attr("href")), $li, $parent;
                if(href && href.indexOf(url) == 0){
                    $li = $(item).parent().addClass("active");
                    $parent = $li.parent();
                    if($parent.is("ul") && $parent.parent().hasClass("submenu")){
                        $parent.show();
                        $parent.parent().addClass("open")
                    }
                    return false;
                }
            });
        }
        if(sideBarComponent.width() > 50){
            initMenu();
        }
    });
</script>