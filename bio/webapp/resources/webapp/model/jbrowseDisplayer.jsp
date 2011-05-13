<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- jBrowseDisplayer.jsp -->

<c:if test="${((!empty object.chromosomeLocation && !empty object.chromosome)
                || cld.unqualifiedName == 'Chromosome') && cld.unqualifiedName != 'ChromosomeBand'}">

  <div class="geneInformation">

    <h3 class="overlapping">Genome Browser</h3>

    <c:set var="baseUrl" value="http://jbrowse.org/ucsc/hg19"/>
    <c:set var="chr" value="${object.chromosomeLocation.locatedOn.primaryIdentifier}"/>
    <c:set var="padding" value="${10}"/>
    <c:set var="offset" value="${fn:substringBefore((object.length * 0.1), '.')}"/>

    <c:set var="start" value="${object.chromosomeLocation.start - offset}"/>
    <c:set var="end" value="${object.chromosomeLocation.end + offset}"/>
    <c:set var="tracks" value="Gene Track,mRNA Track"/>
    <c:set var="jbLink" value="${baseUrl}?loc=chr${chr}:${start}..${end}&tracks=${tracks}"/>

    <p>Click and drag the browser to move the view.  Drag and drop tracks from left menu into the main
	   panel to see the data. Note that some SNPs are recorded with multiple locations - these are marked with
	   an asterisk (*). Clicking on individual features will take you to the report page for that feature.
    <a href="${jbLink}" target="jbrowse">Centre on ${object.primaryIdentifier}</a></p>
	<div id="GenomeBrowser" style="height: 300px; width: 98%;border: 1px solid #dfdfdf; padding: 1%"></div>
    <p><a href="http://jbrowse.org">JBrowse</a> genome browser</p>
</div>

<script type="text/javascript">
/* <![CDATA[ */
var bookmarkCallback = function(brwsr) {
    return window.location.protocol
       + "//" + window.location.host
       + window.location.pathname
       + "?loc=" + brwsr.visibleRegion()
       + "&tracks=" + brwsr.visibleTracks();
    }
var dataDir = window.location.protocol
       + "//" + window.location.host
	   + "/jbrowse/data";
var b = new Browser({
    containerID: "GenomeBrowser",
    refSeqs: refSeqs,
    trackData: trackInfo,
    defaultTracks: "DNA,gene,mRNA,noncodingRNA",
    location: "chr${chr}:${start}..${end}",
    tracks: "${tracks}",
    bookmark: bookmarkCallback,
    dataRoot: dataDir
});
/* ]]> */
</script>

</c:if>
<!-- /jBrowseDisplayer.jsp -->