package org.toadcode.pattern.prover.type;

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

        r0 = XT.of(java.util.Dictionary.class);
        r1 = XT.is(new java.util.Hashtable());
        b = r1.implies(r0);

        if (isPrinting) {
            System.out.println("_____ 16. true = " + b);
        }

        r0 = XT.of(java.util.Dictionary.class);
        r1 = XT.of(java.util.Hashtable.class);
        b = r0.implies(r1);

        if (isPrinting) {
            System.out.println("_____ 17. false = " + b);
        }

        r0 = XT.of(java.util.Dictionary.class);
        r1 = XT.of(java.util.Hashtable.class);
        b = r1.implies(r0);

        if (isPrinting) {
            System.out.println("_____ 18. true = " + b);
        }

        b = r0.not().implies(r1);

        if (isPrinting) {
            System.out.println("_____ 19. false = " + b);
        }

        b = r0.implies(r1.not());

        if (isPrinting) {
            System.out.println("_____ 20. false = " + b);
        }
    }
}






