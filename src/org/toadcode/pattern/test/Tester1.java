package org.toadcode.pattern.test;

/*
	Copyright (C) 1996-2002 Gary Beaver

	RULE-BASED PATTERN MATCHING LIBRARY

	This library is free software; you can redistribute it and/or
	modify it under the terms of the GNU Lesser General Public
	License as published by the Free Software Foundation; either
	version 2.1 of the License, or (at your option) any later version.

	This library is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
	Lesser General Public License for more details.

	You should have received a copy of the GNU Lesser General Public
	License along with this library; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

	If you have questions regarding this library, please contact
	Gary Beaver at beaver@acm.org.
*/

import org.toadcode.pattern.core.*;
import org.toadcode.pattern.prover.ord.XO;
import org.toadcode.pattern.prover.set.XS;
import org.toadcode.pattern.prover.type.XT;


public class Tester1 {

    public static void main(String[] args) throws PatternStructureException {

        //testPatternDAG();

        //testPatternTree ();

        testProjectUsing();
    }

    static void print(Object o) {
        System.out.println("\r\n");
        System.out.println(String.valueOf(o));
    }

    static void print(String s) {
        System.out.println("\r\n");
        System.out.println(s);
    }

    static void print(boolean b) {
        System.out.println("\r\n");
        System.out.println(new Boolean(b));
    }

    public static void testPatternDAG() throws PatternStructureException {

        PatternDAG pd = new PatternDAG(new Prover[]{XT.x, XO.x, XS.x});
        Rule[] r1, r2, r3;

        r1 = new Rule[]{XT.of(String.class), XO.gt(0), XS.in("bingo")};
        r2 = new Rule[]{XT.is("bing"), XO.gt(5), XS.in("bingo")};
        r3 = new Rule[]{XT.is("bing"), XO.gt(3), XS.in("bingo")};

        pd.put(r1, "First One");
        pd.put(r2, "Second One");
        pd.put(r3, "Third One");
        pd.print();
        print(pd.get(new Rule[]{XT.is("bing"), XO.eq(2), XS.in("bingo")}));
        print(pd.get(new Rule[]{XT.is("bing"), XO.eq(4), XS.in("bingo")}));
        print(pd.get(new Rule[]{XT.is("bing"), XO.eq(6), XS.in("bingo")}));
        pd.remove(r2);
        pd.print();
        pd.remove(r1);
        pd.print();
        pd.remove(r3);
        pd.print();
    }

    public static void testPatternTree() throws PatternStructureException {

        PatternTree pt = new PatternTree(new Prover[]{XO.x, XO.x, XS.x});
        Rule[] rs1, rs2, rs3, rs4;

        rs1 = new Rule[]{XO.gt(0), XO.gt(0), XS.in("bingo")};
        rs2 = new Rule[]{XO.gt(2), XO.gt(4), XS.in("bingo")};
        rs3 = new Rule[]{XO.gt(4), XO.gt(2), XS.in("bingo")};
        rs4 = new Rule[]{XO.gt(5), XO.gt(5), XS.in("bingo")};

        pt.put(rs1, "First One");
        pt.put(rs4, "Fourth One");

        /*
        pt.put (
                rs2,
                "Second One");
        */
        pt.put(rs3, "Third One");
        pt.print();
        print(pt.get(new Rule[]{XO.eq(6), XO.eq(6), XS.in("bingo")}));

        /*
        print (
                pt.get (new Rule [] {XT.is ("bing"), XO.eq (4), XS.in ("bingo")}));

        print (
                pt.get (new Rule [] {XT.is ("bing"), XO.eq (6), XS.in ("bingo")}));
        */
        pt.remove(rs4);
        pt.remove(rs1);
        pt.remove(rs3);
        pt.print();
    }

    public static void testProjectUsing() throws PatternStructureException {

        PatternDAG pd = new PatternDAG(new Prover[]{XO.x, XS.x});
        Rule[] r1, r2, r3;

        r1 = new Rule[]{XO.gt(0), XS.in("B")};
        r2 = new Rule[]{XO.gt(3), XS.in("B")};
        r3 = new Rule[]{XO.gt(5), XS.in("C")};

        pd.put(r1, "First One");
        pd.put(r2, "Second One");
        pd.put(r3, "Third One");
        pd.print();

        PatternArgument[][]pa=pd.projectUsing(
                                            //new Object [] {
                                            //    new Integer(4),""
                                            //},
                                              new Rule [] {
                                                  XO.eq(7),
                                                  XS.in("Z")
                                              },
                                            new boolean [] {
                                                true,
                                                false
                                            }
                                            );
        for(int i=0;i<pa.length;i++){
            for(int j=0;j<pa[i].length;j++){
                print("I = " + i + "; J = " + j);
                print(pa[i][j]);
            }
        }
    }
}






