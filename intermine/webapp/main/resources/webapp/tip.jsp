<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<!-- tip.jsp -->

<script type="text/javascript" src="style/niftycube.js"></script>
<script type="text/javascript">
window.onload=function(){
Nifty("div#tipbox","big");
}
</script>
<div class="body" align="center">
	<div id="tipbox">
	<h3>Tips:</h3>
	  <p><jsp:include page="tips/tip${randomTip}_short.jsp"/></p>
	  <html:link action="/tip?id=${randomTip}"><img src="<html:rewrite page="/tips/images/tip${randomTip}a.png"/>" width="300"/></html:link>
	<p><html:link action="/tip?id=${randomTip}">Read more &gt;&gt;&gt;</html:link></p>
	</div>
</div>

<!-- /tip.jsp -->
