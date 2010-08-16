package org.intermine.bio.web.struts;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.intermine.api.InterMineAPI;
import org.intermine.api.results.ExportResultsIterator;
import org.intermine.api.results.ResultElement;
import org.intermine.metadata.Model;
import org.intermine.model.FastPathObject;
import org.intermine.model.bio.LocatedSequenceFeature;
import org.intermine.model.bio.Organism;
import org.intermine.pathquery.Path;
import org.intermine.pathquery.PathException;
import org.intermine.pathquery.PathQuery;
import org.intermine.pathquery.PathQueryBinding;
import org.intermine.util.PropertiesUtil;
import org.intermine.util.TypeUtil;
import org.intermine.web.logic.config.WebConfig;
import org.intermine.web.logic.export.ExportException;
import org.intermine.web.logic.export.ExportHelper;
import org.intermine.web.logic.export.http.HttpExporterBase;
import org.intermine.web.logic.pathqueryresult.PathQueryResultHelper;
import org.intermine.web.logic.results.PagedTable;
import org.intermine.web.logic.session.SessionMethods;
import org.intermine.web.struts.InterMineAction;
import org.intermine.web.util.URLGenerator;
import org.jfree.util.Log;

/*
 * Copyright (C) 2002-2010 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

/**
 * Generate feature path query.
 *
 * @author Fengyuan Hu
 */
public class GalaxyExportAction extends InterMineAction
{
//    private static final Logger LOG = Logger.getLogger(GalaxyExportAction.class);
    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception {
        HttpSession session = request.getSession();
        final InterMineAPI im = SessionMethods.getInterMineAPI(session);
        Model model = im.getModel();
        WebConfig webConfig = SessionMethods.getWebConfig(request);

        String  tableName = (String) request.getParameter("tableName");
        PagedTable pt = SessionMethods.getResultsTable(session, tableName);

        PathQuery query = pt.getWebTable().getPathQuery();

        // Prepare Response
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Make a new view
        Integer index = Integer.parseInt(request.getParameter("index"));
        String prefix = request.getParameter("prefix");

        String type = TypeUtil.unqualifiedName(ExportHelper
                .getColumnClasses(pt).get(index).toString());
        // Type - Class name. e.g. Gene, Protein, etc.
        List<Path> view = PathQueryResultHelper.getDefaultView(type, model, webConfig,
                prefix, false);
        view = getFixedView(view);

        // Reorder the view, move chr, start and end to the first three columns
        List<Path> newView = new ArrayList<Path>();
        newView.add(view.get(5)); //Chr
        newView.add(view.get(6)); //start
        newView.add(view.get(7)); //end
        view.removeAll(newView);
        newView.addAll(view);
        query.setViewPaths(newView);

        // Build Webservice URL
        String queryXML = PathQueryBinding.marshal(query, "tmpName", model.getName(),
                                                   PathQuery.USERPROFILE_VERSION);
        String encodedQueryXML = URLEncoder.encode(queryXML, "UTF-8");
        StringBuffer stringUrl = new StringBuffer(
                new URLGenerator(request).getPermanentBaseURL()
                        + "/service/query/results?query=" + encodedQueryXML
                        + "&size=1000000");

        // Get extra information - genomeBuild & organism & info
        ResultManipulater rm = new ResultManipulater();
        int idx = 8; // organism is the 8th in the view from webConfig
        Map<Integer, String> orgNameMap = rm.findOrganisms(pt, request, idx);

        /*
         * Same function as ResultManipulater, but slower
        Map<Integer, String> orgNameMap = new LinkedHashMap<Integer, String>();

        for (int i = 0; i < pt.getExactSize(); i++) {
            Organism org = ((LocatedSequenceFeature) pt.getWebTable().getResultElements(i).get(0)
                    .get(index).getValue().getObject()).getOrganism();
            orgNameMap.put(org.getTaxonId(), org.getShortName());
        }
        */

        String genomeBuild = "";
        String organism = "";
        if (orgNameMap != null) {

            Properties props = PropertiesUtil.getProperties();
            if (orgNameMap.keySet().size() == 1) {
                if (orgNameMap.keySet().iterator().next() == 7227) {
                    genomeBuild = props.getProperty("genomeVersion.fly");
                }
                if (orgNameMap.keySet().iterator().next() == 6239) {
                    genomeBuild = props.getProperty("genomeVersion.worm");
                }
            }

            organism = Arrays.toString(orgNameMap.values().toArray());
        }

        // Write to Response
        StringBuffer viewString = new StringBuffer();
        viewString.append("|");
        for (Path path : newView) {
            viewString.append(path.toStringNoConstraints());
            viewString.append("|");
        }

        StringBuffer returnString = new StringBuffer();
        // URL>>>>>info>>>organism>>>>>genomeBuild
        returnString.append(stringUrl);
        returnString.append(">>>>>");
        returnString.append(viewString);
        returnString.append(">>>>>");
        returnString.append(organism);
        returnString.append(">>>>>");
        returnString.append(genomeBuild);
        out.println(returnString.toString());

        out.flush();
        out.close();

        return null;
    }

    /**
     * If view contains joined organism, this will make sure, that organism is
     * joined as a inner join. Else constraint on organism doesn't work.
     *
     * @param pathQuery
     * @param joinPath
     * @throws PathException
     * */
    private List<Path> getFixedView(List<Path> view) throws PathException {
        String invalidPath = ":organism";
        String validPath = ".organism";
        List<Path> ret = new ArrayList<Path>();
        for (Path path : view) {
            if (path.toString().contains(invalidPath)) {
                String newPathString = path.toString().replace(invalidPath,
                        validPath);
                path = new Path(path.getModel(), newPathString);
            }
            ret.add(path);
        }
        return ret;
    }
}

/**
 *
 * @author Fengyuan Hu
 *
 */
class ResultManipulater extends HttpExporterBase
{
    private static final Logger LOG = Logger.getLogger(ResultManipulater.class);

    /**
     *
     * @param pt
     * @param request
     * @param index
     * @return
     */
    public Map<Integer, String> findOrganisms(PagedTable pt,
            HttpServletRequest request, int index) {

        if (pt.getEstimatedSize() > 10000) {}
        ExportResultsIterator resultIt = getResultRows(pt, request);

        Map<Integer, String> orgNameMap = new LinkedHashMap<Integer, String>();
        try {
            while (resultIt.hasNext()) {
                List<ResultElement> row = resultIt.next();
                List<ResultElement> elWithObject = getResultElements(row, index);
                for (ResultElement re : elWithObject) {
                    Organism org = (Organism) re.getObject();
                    orgNameMap.put(org.getTaxonId(), org.getShortName());
                }
            }
        } catch (Exception ex) {
            throw new ExportException("Export failed", ex);
        }

        return orgNameMap;

    }

    private List<ResultElement> getResultElements(List<ResultElement> row, int index) {
        List<ResultElement> els = new ArrayList<ResultElement>();
        if (row.get(index) != null) {
            els.add(row.get(index));
        }
        return els;
    }

}
