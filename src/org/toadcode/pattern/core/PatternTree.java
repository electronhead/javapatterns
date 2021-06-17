package org.toadcode.pattern.core;

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

public class PatternTree extends PatternDAG {

    public PatternTree(Prover[] provers) {
        super(provers);
    }

    protected void checkRulesForPut(Rule[] rules)
            throws PatternStructureException {

        super.checkRulesForPut(rules);

        /*
        Disallow rules that are not equal to AND ambiguous with any existing graph pattern.
        This means that a PatternTree is potentially a disconnected set of trees.
        */
        GraphPattern pattern;
        LookupPattern lookupPattern = new LookupPattern(rules);
        GraphPattern[] patterns = leastGeneralPatternsFor(rules);
        GraphPattern[] moreSpecific;
        int i = patterns.length;

        while (i-- > 0) {
            moreSpecific = patterns[i].moreSpecific;

            for (int j = 0; j < moreSpecific.length; j++) {
                pattern = moreSpecific[j];

                if (!lookupPattern.isEqualTo(pattern)) {
                    if (lookupPattern.isAmbiguousWith(pattern)) {
                        System.out.println(
                                "= = = = = = = = = = = = = = = = = = = = = = =");
                        System.out.println("Supplied pattern = \r\n"
                                + new LookupPattern(rules));
                        System.out.println("\nAmbiguous pattern = "
                                + pattern);
                        System.out.println(
                                "= = = = = = = = = = = = = = = = = = = = = = =");

                        throw new PatternStructureException(
                                "PatternTree.checkRulesForPut -- Ambiguous pattern.");
                    }
                }
            }
        }
    }

    GraphPattern[] leastGeneralPatternsFor(Rule[] rules)
            throws PatternStructureException {

        // assumes rules have already been checked.
        LookupPattern pattern = new LookupPattern(rules);
        GraphPattern result = pattern.leastGeneralPatternFrom(topPatterns);

        if (result == null) {
            return new GraphPattern[0];
        } else {
            return new GraphPattern[]{result};
        }
    }
}






