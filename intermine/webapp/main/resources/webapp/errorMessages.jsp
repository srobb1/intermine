<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>

<!-- errorMessages.jsp -->
<tiles:importAttributes/>


<script type="text/javascript" charset="utf-8">

var haserrors=0;
var haslookup=0;
var hasmessages=0;
    

<!-- ERRORS -->
<logic:messagesPresent>
    <html:messages id="error">
      new Insertion.Bottom('error_msg','<str:replace replace="NL" with="<br>" newlineToken="NL"><c:out value="${error}" escapeXml="false"/></str:replace><br/>');
      haserrors=1;
    </html:messages>
</logic:messagesPresent>

<!-- LOOKUP & PORTAL -->
<logic:messagesPresent name="PORTAL_MSG">
  <html:messages id="message" name="PORTAL_MSG">
      new Insertion.Bottom('lookup_msg','<str:replace replace="NL" with="<br>" newlineToken="NL"><c:out value="${message}" escapeXml="false"/></str:replace><br/>');
      haslookup=1;
  </html:messages>
</logic:messagesPresent>

<logic:messagesPresent name="LOOKUP_MSG">
  <html:messages id="message" name="LOOKUP_MSG">
      new Insertion.Bottom('lookup_msg','<str:replace replace="NL" with="<br>" newlineToken="NL"><c:out value="${message}" escapeXml="false"/></str:replace><br/>');
      haslookup=1;
  </html:messages>
</logic:messagesPresent>

<!-- ERRORS II -->
<c:if test="${!empty ERRORS}">
    <c:forEach items="${ERRORS}" var="error">
      new Insertion.Bottom('error_msg','<str:replace replace="NL" with="<br>" newlineToken="NL"><c:out value="${error}" escapeXml="false"/></str:replace><br/>');
      haserrors=1;
    </c:forEach>
  <c:remove var="ERRORS" scope="session"/>
</c:if>

<!-- MESSAGES -->
<logic:messagesPresent message="true">
    <html:messages id="message" message="true">
      new Insertion.Bottom('msg','<str:replace replace="NL" with="<br>" newlineToken="NL"><c:out value="${message}" escapeXml="false"/></str:replace><br/>');
      hasmessages=1;
    </html:messages>
</logic:messagesPresent>

<!-- MESSAGES II -->
<c:if test="${!empty MESSAGES}">
    <c:forEach items="${MESSAGES}" var="message">
      new Insertion.Bottom('msg','<str:replace replace="NL" with="<br>" newlineToken="NL"><c:out value="${message}" escapeXml="false"/></str:replace><br/>');
      hasmessages=1;
    </c:forEach>
  <c:remove var="MESSAGES" scope="session"/>
</c:if>

if(haserrors) {
    Effect.Appear('error_msg',  { duration: 2.0 });
}
if(hasmessages) {
    Effect.Appear('msg',  { duration: 2.0 });
}
if(haslookup) {
    Effect.Appear('lookup_msg', { duration: 2.0 });
}

</script>


<!-- /errorMessages.jsp -->
