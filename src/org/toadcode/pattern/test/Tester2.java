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

import org.toadcode.pattern.core.PatternStructureException;
import org.toadcode.pattern.core.Prover;
import org.toadcode.pattern.core.Rule;
import org.toadcode.pattern.dispatcher.DispatchException;
import org.toadcode.pattern.dispatcher.Executable;
import org.toadcode.pattern.dispatcher.PatternDispatcher;
import org.toadcode.pattern.prover.ord.XO;
import org.toadcode.pattern.prover.set.XS;


public class Tester2 {

    /*
    public static void testPatternDispatcher2 ()
    throws PatternStructureException, DispatchException
    {
            PickAndPlaceDispatcher pd = new PickAndPlaceDispatcher ();
            pd.execute (
                    new GeneralActivity (),
                    new ObjectQuery (),
                    new ObjectQuery (),
                    "queryResult",
                    new User (),
                    "etc");
    }
    */
    public static void main(String[] args)
            throws PatternStructureException, DispatchException {
        testPatternDispatcher();
    }

    static void print(Object o) {
        System.out.println("\r\n");
        System.out.println(o.toString());
    }

    static void print(String s) {
        System.out.println("\r\n");
        System.out.println(s);
    }

    static void print(boolean b) {
        System.out.println("\r\n");
        System.out.println(new Boolean(b));
    }

    public static class foo1 extends Executable {
        public String execute(Integer i1, Integer i2, String s) {
            return "First One.";
        }

        public Object[] getRules() {
            return new Object[]{XO.gt(0), XO.gt(0), "bingo"};
        }
    }

    public static class foo2 extends Executable {
        public String execute(Integer i1, Integer i2, String s) {
            return s;
        }
    }

    public static class foo3 extends Executable {
        public String execute(Integer i1, Integer i2, String s) {
            return "HOOAH!!!";
        }
    }

    public static void testPatternDispatcher()
            throws PatternStructureException, DispatchException {

        PatternDispatcher pd = new PatternDispatcher(new Prover[]{XO.x,
                                                                  XO.x,
                                                                  XS.x});
        Rule[] rs1, rs2, rs3, rs4;

        rs1 = new Rule[]{XO.gt(0), XO.gt(0), XS.in("bingo")};
        rs2 = new Rule[]{XO.gt(2), XO.gt(4), XS.in("bingo")};
        rs3 = new Rule[]{XO.gt(2), XO.gt(5), XS.in("bingo")};
        rs4 = new Rule[]{XO.gt(5), XO.gt(5), XS.in("bingo")};

        pd.put(rs1, "RS1");
        pd.put(rs4, "RS4");
        pd.put(rs2, "RS2");
        pd.put(rs3, "RS3");
        pd.put(new foo3());
        pd.print();
        //print(pd.get(new Rule[]{ XO.eq(6), XO.eq(6), XS.in("bingo") }));
        print(pd.get(new Rule[]{XO.eq(5), XO.eq(5), XS.in("bingo")}));
        //print(pd.get(new Object[]{ new Integer(-1), new Integer(4), "bingo" }));
    }
}






