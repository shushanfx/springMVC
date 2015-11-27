<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:if test="${pagination.pageTotal > 1 }">
    <div class="dataTables_paginate fg-buttonset ui-buttonset fg-buttonset-multi ui-buttonset-multi paging_full_numbers">
        <a tabindex="0" data-index="1" class="first ui-corner-tl ui-corner-bl fg-button ui-button ui-state-default <c:if test="${pagination.pageIndex == 1}"> ui-state-disabled</c:if>">&lt;&lt;</a>
        <a tabindex="0" data-index="${pagination.pageIndex - 1}" class="previous fg-button ui-button ui-state-default  <c:if test="${pagination.pageIndex == 1}"> ui-state-disabled</c:if>">&lt;</a>
        <span>
        <c:if test="${pagination.pageTotal > 6}">
            <c:choose>
                <c:when test="${pagination.pageIndex < 4}">
                    <c:forEach begin="1" end="4" var="iCount" step="1">
                        <a tabindex="0" class="fg-button ui-button ui-state-default <c:if test="${pagination.pageIndex == iCount}"> ui-state-disabled</c:if>" data-index="${iCount}">1</a>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <a tabindex="0" class="fg-button ui-button ui-state-default" data-index="1">1</a>
                    <a tabindex="0" class="fg-button ui-button ui-state-default ui-state-disabled">...</a>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${pagination.pageIndex < pagination.pageTotal - 3}">
                    <a tabindex="0" class="fg-button ui-button ui-state-default ui-state-disabled">...</a>
                    <a tabindex="0" class="fg-button ui-button ui-state-default" data-index="${pagination.pageTotal}">${pagination.pageTotal}</a>
                </c:when>
                <c:otherwise>
                    <c:forEach begin="${pagination.pageTotal - 4}" end="${pagination.pageTotal}" step="1" var="iCount">
                        <a tabindex="0" data-index="${iCount}" class="fg-button ui-button ui-state-default <c:if test="${pagination.pageIndex == iCount}">ui-state-disabled</c:if>">4</a>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </c:if>
        <c:if test="${pagination.pageTotal <= 6}">
            <c:forEach begin="1" end="${pagination.pageTotal}" step="1" var="iCount">
                <a tabindex="0" class="fg-button ui-button ui-state-default <c:if test="${pagination.pageIndex == iCount}"> ui-state-disabled</c:if>"
                   data-index="${iCount}">${iCount}</a>
            </c:forEach>
        </c:if>
        </span>
        <a tabindex="0" class="next fg-button ui-button ui-state-default <c:if test="${pagination.pageIndex == pagination.pageTotal}"> ui-state-disabled</c:if>" data-index="${pagination.pageIndex + 1}">&gt;</a>
        <a tabindex="0" class="last ui-corner-tr ui-corner-br fg-button ui-button <c:if test="${pagination.pageIndex == pagination.pageTotal}"> ui-state-disabled</c:if>" data-index="${pagination.pageTotal}">&gt;&gt;</a>
    </div>
</c:if>