package org.toadcode.pattern.prover.ord;

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

import org.toadcode.pattern.core.Rule;

public class Tester {

    public static void main(String[] args) {

        boolean isPrinting = true;
        Rule r0, r1, r2, r3, r4, r5;
        boolean b;

        b = XO.lt(6).contains(new IntegerScalar(4));

        if (isPrinting) {
            System.out.println("_____ 1. true = " + b);
        }

        b = XO.lt(4).contains(new IntegerScalar(6));

        if (isPrinting) {
            System.out.println("_____ 2. false = " + b);
        }

        b = XO.lt(4).implies(XO.lt(6));

        if (isPrinting) {
            System.out.println("_____ 3. true = " + b);
        }

        b = XO.lt(6).and(XO.gt(4)).implies(XO.lt(8).and(XO.gt(2)));

        if (isPrinting) {
            System.out.println("_____ 4. true = " + b);
        }

        b = XO.lt(8).and(XO.gt(4)).implies(XO.lt(6).and(XO.gt(2)));

        if (isPrinting) {
            System.out.println("_____ 5. false = " + b);
        }

        b = XO.eq(6).implies(XO.lt(8));

        if (isPrinting) {
            System.out.println("_____ 6. true = " + b);
        }

        r0 = XO.lt(8).or(XO.gt(6));
        r1 = XO.eq(5).or(XO.eq(9));
        b = r1.implies(r0);

        if (isPrinting) {
            System.out.println("_____ 7. true = " + b);
        }

        r0 = XO.lt(6).or(XO.gt(6)).or(XO.eq(6));
        r1 = XO.eq(6);
        b = r1.implies(r0);

        if (isPrinting) {
            System.out.println("_____ 8. true = " + b);
        }

        r0 = XO.lt(6).or(XO.gt(6)).or(XO.eq(6));
        r1 = XO.eq(6).or(XO.eq(8));
        b = r1.implies(r0);

        if (isPrinting) {
            System.out.println("_____ 9. true = " + b);
        }

        r0 = XO.lt(6).or(XO.gt(10)).or(XO.gt(7).and(XO.lt(9)));
        r1 = XO.eq(6).or(XO.eq(8));
        b = r1.implies(r0);

        if (isPrinting) {
            System.out.println("_____ 10. false = " + b);
        }

        r0 = XO.lt(7).or(XO.gt(10)).or(XO.gt(7).and(XO.lt(11)));
        r1 = XO.eq(6).or(XO.eq(8));
        b = r1.implies(r0);

        if (isPrinting) {
            System.out.println("_____ 11. true = " + b);
        }

        r0 = XO.lt(7).or(XO.gt(10)).or(XO.gt(7).and(XO.lt(11))).or(XO.eq(8));
        r1 = XO.eq(6).or(XO.eq(7));
        b = r1.implies(r0);

        if (isPrinting) {
            System.out.println("_____ 12. false = " + b);
        }

        r0 = XO.lt(7).or(XO.gt(10)).or(XO.gt(7).and(XO.lt(11))).or(XO.eq(7));
        r1 = XO.eq(6).or(XO.eq(7));
        b = r1.implies(r0);

        if (isPrinting) {
            System.out.println("_____ 13. true = " + b);
        }

        r0 = ((OrderedDomainRule) XO.lt(7).not()).or(XO.lt(2));
        r1 = XO.eq(1).or(XO.eq(7));
        b = r1.implies(r0);

        if (isPrinting) {
            System.out.println("_____ 14. true = " + b);
        }

        r0 = ((OrderedDomainRule) XO.lt(7).not()).or(XO.lt(2)).not();
        r1 = XO.eq(4).or(XO.eq(6));
        b = r1.implies(r0);

        if (isPrinting) {
            System.out.println("_____ 15. true = " + b);
        }

        r0 = XO.eq(4);
        r1 = XO.gt(5);
        b = r0.implies(r1);

        if (isPrinting) {
            System.out.println("_____ 16. false  = " + b);
        }
    }
}






