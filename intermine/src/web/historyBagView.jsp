<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="im"%>

<!-- historyBagView.jsp -->
<html:xhtml/>

<c:if test="${!empty PROFILE.savedBags}">
  <html:form action="/modifyBag">
    <im:heading id="bagHistory">
      <fmt:message key="query.savedbags.header"/>
    </im:heading>
    <im:body id="bagHistory">
    <table class="results" cellspacing="0">
      <tr>
        <th>
          &nbsp;
        </th>
        <th align="left">
          <fmt:message key="query.savedbags.namecolumnheader"/>
        </th>
        <th align="right">
          <fmt:message key="query.savedbags.countcolumnheader"/>
        </th>
      </tr>
      <c:forEach items="${PROFILE.savedBags}" var="savedBag">
        <tr>
          <td>
            <html:multibox property="selectedBags">
              <c:out value="${savedBag.key}"/>
            </html:multibox>
          </td>
          <td align="left">
            <html:link action="/bagDetails?bagName=${savedBag.key}">
              <c:out value="${savedBag.key}"/>
            </html:link>
          </td>
          <td align="right">
            <c:out value="${savedBag.value.size}"/>&nbsp;
          </td>
        </tr>
      </c:forEach>
    </table>
    <br/>
    <html:submit property="delete">
      <fmt:message key="history.delete"/>
    </html:submit>
    <c:if test="${fn:length(PROFILE.savedBags) >= 2}">
      <html:submit property="union">
        <fmt:message key="history.union"/>
      </html:submit>
      <html:submit property="intersect">
        <fmt:message key="history.intersect"/>
      </html:submit>
    </c:if>
  </im:body>
  </html:form>
  <br/>
</c:if>
<!-- /historyBagView.jsp -->
