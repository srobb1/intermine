package org.intermine.objectstore.intermine;

/*
 * Copyright (C) 2002-2005 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;

import org.intermine.testing.OneTimeTestCase;
import org.intermine.sql.DatabaseFactory;
import org.intermine.util.TypeUtil;

public class TruncatedSqlGeneratorTest extends SqlGeneratorTest
{
    public TruncatedSqlGeneratorTest(String arg) {
        super(arg);
    }

    public static Test suite() {
        return OneTimeTestCase.buildSuite(TruncatedSqlGeneratorTest.class);
    }

    public static void oneTimeSetUp() throws Exception {
        SqlGeneratorTest.oneTimeSetUp();
        setUpResults();
        db = DatabaseFactory.getDatabase("db.unittest");
    }

    public static void setUpResults() throws Exception {
        results.put("SelectSimpleObject", "SELECT intermine_Alias.OBJECT AS \"intermine_Alias\", intermine_Alias.id AS \"intermine_Aliasid\" FROM InterMineObject AS intermine_Alias WHERE intermine_Alias.class = 'org.intermine.model.testmodel.Company' ORDER BY intermine_Alias.id");
        results2.put("SelectSimpleObject", Collections.singleton("InterMineObject"));
        results.put("SubQuery", "SELECT DISTINCT intermine_All.intermine_Arrayname AS a1_, intermine_All.intermine_Alias AS \"intermine_Alias\" FROM (SELECT DISTINCT intermine_Array.OBJECT AS intermine_Array, intermine_Array.CEOId AS intermine_ArrayCEOId, intermine_Array.addressId AS intermine_ArrayaddressId, intermine_Array.id AS intermine_Arrayid, intermine_Array.name AS intermine_Arrayname, intermine_Array.vatNumber AS intermine_ArrayvatNumber, 5 AS intermine_Alias FROM InterMineObject AS intermine_Array WHERE intermine_Array.class = 'org.intermine.model.testmodel.Company') AS intermine_All ORDER BY intermine_All.intermine_Arrayname, intermine_All.intermine_Alias");
        results2.put("SubQuery", Collections.singleton("InterMineObject"));
        results.put("WhereSimpleEquals", "SELECT DISTINCT a1_.name AS a2_ FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND a1_.vatNumber = 1234 ORDER BY a1_.name");
        results2.put("WhereSimpleEquals", Collections.singleton("InterMineObject"));
        results.put("WhereSimpleNotEquals", "SELECT DISTINCT a1_.name AS a2_ FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND a1_.vatNumber != 1234 ORDER BY a1_.name");
        results2.put("WhereSimpleNotEquals", Collections.singleton("InterMineObject"));
        results.put("WhereSimpleNegEquals", "SELECT DISTINCT a1_.name AS a2_ FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND a1_.vatNumber != 1234 ORDER BY a1_.name");
        results2.put("WhereSimpleNegEquals", Collections.singleton("InterMineObject"));
        results.put("WhereSimpleLike", "SELECT DISTINCT a1_.name AS a2_ FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND a1_.name LIKE 'Company%' ORDER BY a1_.name");
        results2.put("WhereSimpleLike", Collections.singleton("InterMineObject"));
        results.put("WhereEqualsString", "SELECT DISTINCT a1_.name AS a2_ FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND a1_.name = 'CompanyA' ORDER BY a1_.name");
        results2.put("WhereEqualsString", Collections.singleton("InterMineObject"));
        results.put("WhereAndSet", "SELECT DISTINCT a1_.name AS a2_ FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND (a1_.name LIKE 'Company%' AND a1_.vatNumber > 2000) ORDER BY a1_.name");
        results2.put("WhereAndSet", Collections.singleton("InterMineObject"));
        results.put("WhereOrSet", "SELECT DISTINCT a1_.name AS a2_ FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND (a1_.name LIKE 'CompanyA%' OR a1_.vatNumber > 2000) ORDER BY a1_.name");
        results2.put("WhereOrSet", Collections.singleton("InterMineObject"));
        results.put("WhereNotSet", "SELECT DISTINCT a1_.name AS a2_ FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND ( NOT (a1_.name LIKE 'Company%' AND a1_.vatNumber > 2000)) ORDER BY a1_.name");
        results2.put("WhereNotSet", Collections.singleton("InterMineObject"));
        results.put("WhereSubQueryField", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, a1_.name AS orderbyfield0 FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Department' AND a1_.name IN (SELECT DISTINCT a1_.name FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Department') ORDER BY a1_.name, a1_.id");
        results2.put("WhereSubQueryField", Collections.singleton("InterMineObject"));
        results.put("WhereSubQueryClass", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND a1_.id IN (SELECT DISTINCT a1_.id FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND a1_.name = 'CompanyA') ORDER BY a1_.id");
        results2.put("WhereSubQueryClass", Collections.singleton("InterMineObject"));
        results.put("WhereNotSubQueryClass", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND a1_.id NOT IN (SELECT DISTINCT a1_.id FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND a1_.name = 'CompanyA') ORDER BY a1_.id");
        results2.put("WhereNotSubQueryClass", Collections.singleton("InterMineObject"));
        results.put("WhereNegSubQueryClass", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND a1_.id NOT IN (SELECT DISTINCT a1_.id FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND a1_.name = 'CompanyA') ORDER BY a1_.id");
        results2.put("WhereNegSubQueryClass", Collections.singleton("InterMineObject"));
        results.put("WhereClassClass", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, a2_.OBJECT AS a2_, a2_.id AS a2_id FROM InterMineObject AS a1_, InterMineObject AS a2_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND a2_.class = 'org.intermine.model.testmodel.Company' AND a1_.id = a2_.id ORDER BY a1_.id, a2_.id");
        results2.put("WhereClassClass", Collections.singleton("InterMineObject"));
        results.put("WhereNotClassClass", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, a2_.OBJECT AS a2_, a2_.id AS a2_id FROM InterMineObject AS a1_, InterMineObject AS a2_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND a2_.class = 'org.intermine.model.testmodel.Company' AND a1_.id != a2_.id ORDER BY a1_.id, a2_.id");
        results2.put("WhereNotClassClass", Collections.singleton("InterMineObject"));
        results.put("WhereNegClassClass", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, a2_.OBJECT AS a2_, a2_.id AS a2_id FROM InterMineObject AS a1_, InterMineObject AS a2_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND a2_.class = 'org.intermine.model.testmodel.Company' AND a1_.id != a2_.id ORDER BY a1_.id, a2_.id");
        results2.put("WhereNegClassClass", Collections.singleton("InterMineObject"));
        Integer id1 = (Integer) TypeUtil.getFieldValue(data.get("CompanyA"), "id");
        results.put("WhereClassObject", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND a1_.id = " + id1 + " ORDER BY a1_.id");
        results2.put("WhereClassObject", Collections.singleton("InterMineObject"));
        results.put("Contains11", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, a2_.OBJECT AS a2_, a2_.id AS a2_id FROM InterMineObject AS a1_, InterMineObject AS a2_ WHERE a1_.class = 'org.intermine.model.testmodel.Department' AND a2_.class = 'org.intermine.model.testmodel.Manager' AND (a1_.managerId = a2_.id AND a1_.name = 'DepartmentA1') ORDER BY a1_.id, a2_.id");
        results2.put("Contains11", Collections.singleton("InterMineObject"));
        results.put("ContainsNot11", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, a2_.OBJECT AS a2_, a2_.id AS a2_id FROM InterMineObject AS a1_, InterMineObject AS a2_ WHERE a1_.class = 'org.intermine.model.testmodel.Department' AND a2_.class = 'org.intermine.model.testmodel.Manager' AND (a1_.managerId != a2_.id AND a1_.name = 'DepartmentA1') ORDER BY a1_.id, a2_.id");
        results2.put("ContainsNot11", Collections.singleton("InterMineObject"));
        results.put("ContainsNeg11", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, a2_.OBJECT AS a2_, a2_.id AS a2_id FROM InterMineObject AS a1_, InterMineObject AS a2_ WHERE a1_.class = 'org.intermine.model.testmodel.Department' AND a2_.class = 'org.intermine.model.testmodel.Manager' AND (a1_.managerId != a2_.id AND a1_.name = 'DepartmentA1') ORDER BY a1_.id, a2_.id");
        results2.put("ContainsNeg11", Collections.singleton("InterMineObject"));
        results.put("Contains1N", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, a2_.OBJECT AS a2_, a2_.id AS a2_id FROM InterMineObject AS a1_, InterMineObject AS a2_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND a2_.class = 'org.intermine.model.testmodel.Department' AND (a1_.id = a2_.companyId AND a1_.name = 'CompanyA') ORDER BY a1_.id, a2_.id");
        results2.put("Contains1N", Collections.singleton("InterMineObject"));
        results.put("ContainsN1", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, a2_.OBJECT AS a2_, a2_.id AS a2_id FROM InterMineObject AS a1_, InterMineObject AS a2_ WHERE a1_.class = 'org.intermine.model.testmodel.Department' AND a2_.class = 'org.intermine.model.testmodel.Company' AND (a1_.companyId = a2_.id AND a2_.name = 'CompanyA') ORDER BY a1_.id, a2_.id");
        results2.put("ContainsN1", Collections.singleton("InterMineObject"));
        results.put("ContainsMN", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, a2_.OBJECT AS a2_, a2_.id AS a2_id FROM InterMineObject AS a1_, InterMineObject AS a2_, CompanysContractors AS indirect0 WHERE a1_.class = 'org.intermine.model.testmodel.Contractor' AND a2_.class = 'org.intermine.model.testmodel.Company' AND ((a1_.id = indirect0.Companys AND indirect0.Contractors = a2_.id) AND a1_.name = 'ContractorA') ORDER BY a1_.id, a2_.id");
        results2.put("ContainsMN", new HashSet(Arrays.asList(new String[] {"InterMineObject", "CompanysContractors"})));
        results.put("ContainsDuplicatesMN", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, a2_.OBJECT AS a2_, a2_.id AS a2_id FROM InterMineObject AS a1_, InterMineObject AS a2_, OldComsOldContracts AS indirect0 WHERE a1_.class = 'org.intermine.model.testmodel.Contractor' AND a2_.class = 'org.intermine.model.testmodel.Company' AND (a1_.id = indirect0.OldComs AND indirect0.OldContracts = a2_.id) ORDER BY a1_.id, a2_.id");
        results2.put("ContainsDuplicatesMN", new HashSet(Arrays.asList(new String[] {"InterMineObject", "OldComsOldContracts"})));
        id1 = (Integer) TypeUtil.getFieldValue(data.get("EmployeeA1"), "id");
        results.put("ContainsObject", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Department' AND a1_.managerId = " + id1 + " ORDER BY a1_.id");
        results2.put("ContainsObject", Collections.singleton("InterMineObject"));
        results.put("SimpleGroupBy", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, COUNT(*) AS a2_ FROM InterMineObject AS a1_, InterMineObject AS a3_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND a3_.class = 'org.intermine.model.testmodel.Department' AND a1_.id = a3_.companyId GROUP BY a1_.OBJECT, a1_.CEOId, a1_.addressId, a1_.id, a1_.name, a1_.vatNumber ORDER BY a1_.id, COUNT(*)");
        results2.put("SimpleGroupBy", Collections.singleton("InterMineObject"));
        results.put("MultiJoin", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, a2_.OBJECT AS a2_, a2_.id AS a2_id, a3_.OBJECT AS a3_, a3_.id AS a3_id, a4_.OBJECT AS a4_, a4_.id AS a4_id FROM InterMineObject AS a1_, InterMineObject AS a2_, InterMineObject AS a3_, InterMineObject AS a4_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND a2_.class = 'org.intermine.model.testmodel.Department' AND a3_.class = 'org.intermine.model.testmodel.Manager' AND a4_.class = 'org.intermine.model.testmodel.Address' AND (a1_.id = a2_.companyId AND a2_.managerId = a3_.id AND a3_.addressId = a4_.id AND a3_.name = 'EmployeeA1') ORDER BY a1_.id, a2_.id, a3_.id, a4_.id");
        results2.put("MultiJoin", Collections.singleton("InterMineObject"));
        results.put("SelectComplex", "SELECT DISTINCT (AVG(a1_.vatNumber) + 20) AS a3_, a2_.name AS a4_, a2_.OBJECT AS a2_, a2_.id AS a2_id FROM InterMineObject AS a1_, InterMineObject AS a2_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND a2_.class = 'org.intermine.model.testmodel.Department' GROUP BY a2_.OBJECT, a2_.companyId, a2_.id, a2_.managerId, a2_.name ORDER BY (AVG(a1_.vatNumber) + 20), a2_.name, a2_.id");
        results2.put("SelectComplex", Collections.singleton("InterMineObject"));
        results.put("SelectClassAndSubClasses", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, a1_.name AS orderbyfield0 FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Employee' ORDER BY a1_.name, a1_.id");
        results2.put("SelectClassAndSubClasses", Collections.singleton("InterMineObject"));
        results.put("SelectInterfaceAndSubClasses", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Employable' ORDER BY a1_.id");
        results2.put("SelectInterfaceAndSubClasses", Collections.singleton("InterMineObject"));
        results.put("SelectInterfaceAndSubClasses2", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.RandomInterface' ORDER BY a1_.id");
        results2.put("SelectInterfaceAndSubClasses2", Collections.singleton("InterMineObject"));
        results.put("SelectInterfaceAndSubClasses3", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.ImportantPerson' ORDER BY a1_.id");
        results2.put("SelectInterfaceAndSubClasses3", Collections.singleton("InterMineObject"));
        results.put("OrderByAnomaly", "SELECT DISTINCT 5 AS a2_, a1_.name AS a3_ FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' ORDER BY a1_.name");
        results2.put("OrderByAnomaly", Collections.singleton("InterMineObject"));
        Integer id2 = (Integer) TypeUtil.getFieldValue(data.get("CompanyA"), "id");
        Integer id3 = (Integer) TypeUtil.getFieldValue(data.get("DepartmentA1"), "id");
        results.put("SelectClassObjectSubquery", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_, InterMineObject AS a2_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND a2_.class = 'org.intermine.model.testmodel.Department' AND (a1_.id = " + id2 + " AND a1_.id = a2_.companyId AND a2_.id IN (SELECT DISTINCT a1_.id FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Department' AND a1_.id = " + id3 + ")) ORDER BY a1_.id");
        results2.put("SelectClassObjectSubquery", Collections.singleton("InterMineObject"));
        results.put("SelectUnidirectionalCollection", "SELECT DISTINCT a2_.OBJECT AS a2_, a2_.id AS a2_id FROM InterMineObject AS a1_, InterMineObject AS a2_, HasSecretarysSecretarys AS indirect0 WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND a2_.class = 'org.intermine.model.testmodel.Secretary' AND (a1_.name = 'CompanyA' AND (a1_.id = indirect0.Secretarys AND indirect0.HasSecretarys = a2_.id)) ORDER BY a2_.id");
        results2.put("SelectUnidirectionalCollection", new HashSet(Arrays.asList(new String[] {"InterMineObject", "HasSecretarysSecretarys"})));
        results.put("EmptyAndConstraintSet", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND true ORDER BY a1_.id");
        results2.put("EmptyAndConstraintSet", Collections.singleton("InterMineObject"));
        results.put("EmptyOrConstraintSet", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND false ORDER BY a1_.id");
        results2.put("EmptyOrConstraintSet", Collections.singleton("InterMineObject"));
        results.put("EmptyNandConstraintSet", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND false ORDER BY a1_.id");
        results2.put("EmptyNandConstraintSet", Collections.singleton("InterMineObject"));
        results.put("EmptyNorConstraintSet", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND true ORDER BY a1_.id");
        results2.put("EmptyNorConstraintSet", Collections.singleton("InterMineObject"));
        results.put("BagConstraint", "SELECT DISTINCT Company.OBJECT AS \"Company\", Company.id AS \"Companyid\" FROM InterMineObject AS Company WHERE Company.class = 'org.intermine.model.testmodel.Company' AND (Company.name IN ('CompanyA', 'goodbye', 'hello')) ORDER BY Company.id");
        results2.put("BagConstraint", Collections.singleton("InterMineObject"));
        results.put("BagConstraint2", "SELECT DISTINCT Company.OBJECT AS \"Company\", Company.id AS \"Companyid\" FROM InterMineObject AS Company WHERE Company.class = 'org.intermine.model.testmodel.Company' AND (Company.id IN (" + id2 + ")) ORDER BY Company.id");
        results2.put("BagConstraint2", Collections.singleton("InterMineObject"));
        results.put("InterfaceField", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Employable' AND a1_.name = 'EmployeeA1' ORDER BY a1_.id");
        results2.put("InterfaceField", Collections.singleton("InterMineObject"));
        results.put("InterfaceReference", NO_RESULT);
        results2.put("InterfaceReference", Collections.singleton("InterMineObject"));
        results.put("InterfaceCollection", NO_RESULT);
        results2.put("InterfaceCollection", Collections.singleton("InterMineObject"));
        Set res = new HashSet();
        res.add("SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, a1__1.debt AS a2_, a1_.age AS a3_ FROM InterMineObject AS a1_, InterMineObject AS a1__1 WHERE a1_.class = 'org.intermine.model.testmodel.Employee' AND a1_.id = a1__1.id AND a1__1.class = 'org.intermine.model.testmodel.Broke' AND (a1__1.debt > 0 AND a1_.age > 0) ORDER BY a1_.id, a1__1.debt, a1_.age");
        res.add("SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, a1_.debt AS a2_, a1__1.age AS a3_ FROM InterMineObject AS a1_, InterMineObject AS a1__1 WHERE a1_.class = 'org.intermine.model.testmodel.Broke' AND a1_.id = a1__1.id AND a1__1.class = 'org.intermine.model.testmodel.Employee' AND (a1_.debt > 0 AND a1__1.age > 0) ORDER BY a1_.id, a1_.debt, a1__1.age");
        results.put("DynamicInterfacesAttribute", res);
        results2.put("DynamicInterfacesAttribute", Collections.singleton("InterMineObject"));
        res = new HashSet();
        res.add("SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_, InterMineObject AS a1__1 WHERE a1_.class = 'org.intermine.model.testmodel.Employable' AND a1_.id = a1__1.id AND a1__1.class = 'org.intermine.model.testmodel.Broke' ORDER BY a1_.id");
        res.add("SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_, InterMineObject AS a1__1 WHERE a1_.class = 'org.intermine.model.testmodel.Broke' AND a1_.id = a1__1.id AND a1__1.class = 'org.intermine.model.testmodel.Employable' ORDER BY a1_.id");
        results.put("DynamicClassInterface", res);
        results2.put("DynamicClassInterface", Collections.singleton("InterMineObject"));
        res = new HashSet();
        res.add("SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, a2_.OBJECT AS a2_, a2_.id AS a2_id, a3_.OBJECT AS a3_, a3_.id AS a3_id FROM InterMineObject AS a1_, InterMineObject AS a1__1, InterMineObject AS a2_, InterMineObject AS a3_ WHERE a1_.class = 'org.intermine.model.testmodel.Department' AND a1_.id = a1__1.id AND a1__1.class = 'org.intermine.model.testmodel.Broke' AND a2_.class = 'org.intermine.model.testmodel.Company' AND a3_.class = 'org.intermine.model.testmodel.Bank' AND (a2_.id = a1_.companyId AND a3_.id = a1__1.bankId) ORDER BY a1_.id, a2_.id, a3_.id");
        res.add("SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, a2_.OBJECT AS a2_, a2_.id AS a2_id, a3_.OBJECT AS a3_, a3_.id AS a3_id FROM InterMineObject AS a1_, InterMineObject AS a1__1, InterMineObject AS a2_, InterMineObject AS a3_ WHERE a1_.class = 'org.intermine.model.testmodel.Broke' AND a1_.id = a1__1.id AND a1__1.class = 'org.intermine.model.testmodel.Department' AND a2_.class = 'org.intermine.model.testmodel.Company' AND a3_.class = 'org.intermine.model.testmodel.Bank' AND (a2_.id = a1__1.companyId AND a3_.id = a1_.bankId) ORDER BY a1_.id, a2_.id, a3_.id");
        results.put("DynamicClassRef1", res);
        results2.put("DynamicClassRef1", Collections.singleton("InterMineObject"));
        res = new HashSet();
        res.add("SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, a2_.OBJECT AS a2_, a2_.id AS a2_id, a3_.OBJECT AS a3_, a3_.id AS a3_id FROM InterMineObject AS a1_, InterMineObject AS a1__1, InterMineObject AS a2_, InterMineObject AS a3_ WHERE a1_.class = 'org.intermine.model.testmodel.Department' AND a1_.id = a1__1.id AND a1__1.class = 'org.intermine.model.testmodel.Broke' AND a2_.class = 'org.intermine.model.testmodel.Company' AND a3_.class = 'org.intermine.model.testmodel.Bank' AND (a1_.companyId = a2_.id AND a1__1.bankId = a3_.id) ORDER BY a1_.id, a2_.id, a3_.id");
        res.add("SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, a2_.OBJECT AS a2_, a2_.id AS a2_id, a3_.OBJECT AS a3_, a3_.id AS a3_id FROM InterMineObject AS a1_, InterMineObject AS a1__1, InterMineObject AS a2_, InterMineObject AS a3_ WHERE a1_.class = 'org.intermine.model.testmodel.Broke' AND a1_.id = a1__1.id AND a1__1.class = 'org.intermine.model.testmodel.Department' AND a2_.class = 'org.intermine.model.testmodel.Company' AND a3_.class = 'org.intermine.model.testmodel.Bank' AND (a1__1.companyId = a2_.id AND a1_.bankId = a3_.id) ORDER BY a1_.id, a2_.id, a3_.id");
        results.put("DynamicClassRef2", res);
        results2.put("DynamicClassRef2", Collections.singleton("InterMineObject"));
        res = new HashSet();
        res.add("SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, a2_.OBJECT AS a2_, a2_.id AS a2_id, a3_.OBJECT AS a3_, a3_.id AS a3_id FROM InterMineObject AS a1_, InterMineObject AS a1__1, InterMineObject AS a2_, InterMineObject AS a3_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND a1_.id = a1__1.id AND a1__1.class = 'org.intermine.model.testmodel.Bank' AND a2_.class = 'org.intermine.model.testmodel.Department' AND a3_.class = 'org.intermine.model.testmodel.Broke' AND (a1_.id = a2_.companyId AND a1_.id = a3_.bankId) ORDER BY a1_.id, a2_.id, a3_.id");
        res.add("SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, a2_.OBJECT AS a2_, a2_.id AS a2_id, a3_.OBJECT AS a3_, a3_.id AS a3_id FROM InterMineObject AS a1_, InterMineObject AS a1__1, InterMineObject AS a2_, InterMineObject AS a3_ WHERE a1_.class = 'org.intermine.model.testmodel.Bank' AND a1_.id = a1__1.id AND a1__1.class = 'org.intermine.model.testmodel.Company' AND a2_.class = 'org.intermine.model.testmodel.Department' AND a3_.class = 'org.intermine.model.testmodel.Broke' AND (a1_.id = a2_.companyId AND a1_.id = a3_.bankId) ORDER BY a1_.id, a2_.id, a3_.id");
        results.put("DynamicClassRef3", res);
        results2.put("DynamicClassRef3", Collections.singleton("InterMineObject"));
        res = new HashSet();
        res.add("SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, a2_.OBJECT AS a2_, a2_.id AS a2_id, a3_.OBJECT AS a3_, a3_.id AS a3_id FROM InterMineObject AS a1_, InterMineObject AS a1__1, InterMineObject AS a2_, InterMineObject AS a3_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND a1_.id = a1__1.id AND a1__1.class = 'org.intermine.model.testmodel.Bank' AND a2_.class = 'org.intermine.model.testmodel.Department' AND a3_.class = 'org.intermine.model.testmodel.Broke' AND (a2_.companyId = a1_.id AND a3_.bankId = a1_.id) ORDER BY a1_.id, a2_.id, a3_.id");
        res.add("SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, a2_.OBJECT AS a2_, a2_.id AS a2_id, a3_.OBJECT AS a3_, a3_.id AS a3_id FROM InterMineObject AS a1_, InterMineObject AS a1__1, InterMineObject AS a2_, InterMineObject AS a3_ WHERE a1_.class = 'org.intermine.model.testmodel.Bank' AND a1_.id = a1__1.id AND a1__1.class = 'org.intermine.model.testmodel.Company' AND a2_.class = 'org.intermine.model.testmodel.Department' AND a3_.class = 'org.intermine.model.testmodel.Broke' AND (a2_.companyId = a1_.id AND a3_.bankId = a1_.id) ORDER BY a1_.id, a2_.id, a3_.id");
        results.put("DynamicClassRef4", res);
        results2.put("DynamicClassRef4", Collections.singleton("InterMineObject"));
        res = new HashSet();
        res.add("SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_, InterMineObject AS a1__1, InterMineObject AS a2_, InterMineObject AS a2__1 WHERE a1_.class = 'org.intermine.model.testmodel.Employable' AND a1_.id = a1__1.id AND a1__1.class = 'org.intermine.model.testmodel.Broke' AND a2_.class = 'org.intermine.model.testmodel.HasAddress' AND a2_.id = a2__1.id AND a2__1.class = 'org.intermine.model.testmodel.Broke' AND a1_.id = a2_.id ORDER BY a1_.id");
        res.add("SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_, InterMineObject AS a1__1, InterMineObject AS a2_, InterMineObject AS a2__1 WHERE a1_.class = 'org.intermine.model.testmodel.Employable' AND a1_.id = a1__1.id AND a1__1.class = 'org.intermine.model.testmodel.Broke' AND a2_.class = 'org.intermine.model.testmodel.Broke' AND a2_.id = a2__1.id AND a2__1.class = 'org.intermine.model.testmodel.HasAddress' AND a1_.id = a2_.id ORDER BY a1_.id");
        res.add("SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_, InterMineObject AS a1__1, InterMineObject AS a2_, InterMineObject AS a2__1 WHERE a1_.class = 'org.intermine.model.testmodel.Broke' AND a1_.id = a1__1.id AND a1__1.class = 'org.intermine.model.testmodel.Employable' AND a2_.class = 'org.intermine.model.testmodel.HasAddress' AND a2_.id = a2__1.id AND a2__1.class = 'org.intermine.model.testmodel.Broke' AND a1_.id = a2_.id ORDER BY a1_.id");
        res.add("SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_, InterMineObject AS a1__1, InterMineObject AS a2_, InterMineObject AS a2__1 WHERE a1_.class = 'org.intermine.model.testmodel.Broke' AND a1_.id = a1__1.id AND a1__1.class = 'org.intermine.model.testmodel.Employable' AND a2_.class = 'org.intermine.model.testmodel.Broke' AND a2_.id = a2__1.id AND a2__1.class = 'org.intermine.model.testmodel.HasAddress' AND a1_.id = a2_.id ORDER BY a1_.id");
        results.put("DynamicClassConstraint", res);
        results2.put("DynamicClassConstraint", Collections.singleton("InterMineObject"));
        results.put("ContainsConstraintNull", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Employee' AND a1_.addressId IS NULL ORDER BY a1_.id");
        results2.put("ContainsConstraintNull", Collections.singleton("InterMineObject"));
        results.put("ContainsConstraintNotNull", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Employee' AND a1_.addressId IS NOT NULL ORDER BY a1_.id");
        results2.put("ContainsConstraintNotNull", Collections.singleton("InterMineObject"));
        results.put("SimpleConstraintNull", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Manager' AND a1_.title IS NULL ORDER BY a1_.id");
        results2.put("SimpleConstraintNull", Collections.singleton("InterMineObject"));
        results.put("SimpleConstraintNotNull", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Manager' AND a1_.title IS NOT NULL ORDER BY a1_.id");
        results2.put("SimpleConstraintNotNull", Collections.singleton("InterMineObject"));
        results.put("TypeCast", "SELECT DISTINCT (a1_.age)::TEXT AS a2_ FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Employee' ORDER BY (a1_.age)::TEXT");
        results2.put("TypeCast", Collections.singleton("InterMineObject"));
        results.put("IndexOf", "SELECT STRPOS(a1_.name, 'oy') AS a2_ FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Employee' ORDER BY STRPOS(a1_.name, 'oy')");
        results2.put("IndexOf", Collections.singleton("InterMineObject"));
        results.put("Substring", "SELECT SUBSTR(a1_.name, 2, 2) AS a2_ FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Employee' ORDER BY SUBSTR(a1_.name, 2, 2)");
        results2.put("Substring", Collections.singleton("InterMineObject"));
        results.put("Substring2", "SELECT SUBSTR(a1_.name, 2) AS a2_ FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Employee' ORDER BY SUBSTR(a1_.name, 2)");
        results2.put("Substring2", Collections.singleton("InterMineObject"));
        results.put("OrderByReference", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id, a1_.departmentId AS orderbyfield0 FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Employee' ORDER BY a1_.departmentId, a1_.id");
        results2.put("OrderByReference", Collections.singleton("InterMineObject"));
        String largeBagConstraintText = new BufferedReader(new InputStreamReader(TruncatedSqlGeneratorTest.class.getClassLoader().getResourceAsStream("test/truncatedLargeBag.sql"))).readLine();
        results.put("LargeBagConstraint", largeBagConstraintText);
        results2.put("LargeBagConstraint", Collections.singleton("InterMineObject"));

        results2.put("LargeBagConstraintUsingTable", Collections.singleton("InterMineObject"));
        results.put("LargeBagConstraintUsingTable", "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Employee' AND a1_.name IN (SELECT value FROM " + SqlGeneratorTest.LARGE_BAG_TABLE_NAME + ") ORDER BY a1_.id");
    }

    protected DatabaseSchema getSchema() {
        return new DatabaseSchema(model, Collections.singletonList(model.getClassDescriptorByName("org.intermine.model.InterMineObject")));
    }
    public String getRegisterOffset1() {
        return "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' ORDER BY a1_.id";
    }
    public String getRegisterOffset2() {
        return "SELECT DISTINCT a1_.OBJECT AS a1_, a1_.id AS a1_id FROM InterMineObject AS a1_ WHERE a1_.class = 'org.intermine.model.testmodel.Company' AND ";
    }
}
