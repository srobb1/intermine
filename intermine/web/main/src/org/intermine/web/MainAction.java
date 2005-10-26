package org.intermine.web;

/*
 * Copyright (C) 2002-2005 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.intermine.objectstore.query.ConstraintOp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Action to handle button presses on the main tile
 * @author Mark Woodbridge
 */
public class MainAction extends InterMineAction
{
    /** 
     * Method called when user has finished updating a constraint
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return an ActionForward object defining where control goes next
     * @exception Exception if the application business logic throws
     *  an exception
     */
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception {
        HttpSession session = request.getSession();
        PathQuery query = (PathQuery) session.getAttribute(Constants.QUERY);
        MainForm mf = (MainForm) form;

        PathNode node = (PathNode) query.getNodes().get(mf.getPath());
        
        Integer cindex = (request.getParameter("cindex") != null)
                ? new Integer(request.getParameter("cindex")) : null;

        String label = null, id = null, code = query.getUnusedConstraintCode();
        boolean editable = false;
        int previousConstraintCount = query.getAllConstraints().size();
        
        if (cindex != null) {
            // We're updating an existing constraint, just remove the old one
            Constraint c = (Constraint) node.getConstraints().get(cindex.intValue());
            node.removeConstraint(c);
            label = c.getDescription();
            id = c.getIdentifier();
            editable = c.isEditable();
            code = c.getCode();
            
            if (request.getParameter("template") != null) {
                // We're just updating template settings
                node.getConstraints().add(new Constraint(c.getOp(), c.getValue(), mf.isEditable(),
                        mf.getTemplateLabel(), c.getCode(), mf.getTemplateId()));
                mf.reset(mapping, request);
                return mapping.findForward("query");
            }
        }
        
        if (request.getParameter("attribute") != null && cindex == null) {
            // New constraint
            label = mf.getTemplateLabel();
            id = mf.getTemplateId();
            editable = mf.isEditable();
        }
        
        if (request.getParameter("attribute") != null) {
            ConstraintOp constraintOp = ConstraintOp.
                getOpForIndex(Integer.valueOf(mf.getAttributeOp()));
            Object constraintValue = mf.getParsedAttributeValue();
            Constraint c = new Constraint(constraintOp, constraintValue, editable, label, code, id);
            node.getConstraints().add(c);
        }

        if (request.getParameter("bag") != null) {
            ConstraintOp constraintOp = ConstraintOp.
                getOpForIndex(Integer.valueOf(mf.getBagOp()));
            Object constraintValue = mf.getBagValue();
            Constraint c = new Constraint(constraintOp, constraintValue, false, label, code, id);
            node.getConstraints().add(c);
        }

        if (request.getParameter ("loop") != null) {
            ConstraintOp constraintOp = ConstraintOp.
                getOpForIndex(Integer.valueOf(mf.getLoopQueryOp()));
            Object constraintValue = mf.getLoopQueryValue();
            Constraint c = new Constraint(constraintOp, constraintValue, false, label, code, id);
            node.getConstraints().add(c);
        }
        
        if (request.getParameter("subclass") != null) {
            node.setType(mf.getSubclassValue());
            session.setAttribute("path", mf.getSubclassValue());
        }
        
        if (request.getParameter("nullnotnull") != null) {
            if (mf.getNullConstraint().equals("NotNULL")) {
                node.getConstraints()
                    .add(new Constraint(ConstraintOp.IS_NOT_NULL, null, false, label, code, id));
            } else {
                node.getConstraints()
                    .add(new Constraint(ConstraintOp.IS_NULL, null, false, label, code, id));
            }
        }
        
        if (request.getParameter("expression") != null) {
            query.setConstraintLogic(request.getParameter("expr"));
            query.syncLogicExpression(SessionMethods.getDefaultOperator(session));
        }
        
        if (request.getParameter("defaultOperator") != null) {
            session.setAttribute(Constants.DEFAULT_OPERATOR, request.getParameter("operator"));
        }
        
        if (query.getAllConstraints().size() == previousConstraintCount + 1) {
            query.syncLogicExpression(SessionMethods.getDefaultOperator(session));
        }
        
        mf.reset(mapping, request);

        return mapping.findForward("query");
    }
}
