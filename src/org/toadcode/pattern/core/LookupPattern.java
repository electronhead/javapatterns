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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LookupPattern extends Pattern {

    private int[] values;
    private int[] keys;
    private int count;
    private static final int EQUAL = 0;
    private static final int MORE_SPECIFIC = 1;
    private static final int OTHERWISE = 2;
    private static final int INITIAL_LOOKUP_CAPACITY = 10;

    // constructors
    public LookupPattern(Rule[] rules) throws PatternStructureException {

        super(rules);

        count = 0;
        keys = new int[INITIAL_LOOKUP_CAPACITY];
        values = new int[INITIAL_LOOKUP_CAPACITY];
    }

    void addUnique(int key, int value) {

        if (count == keys.length) {
            int[] newKeys = new int[keys.length * 2];
            int[] newValues = new int[values.length * 2];

            System.arraycopy(keys, 0, newKeys, 0, count);
            System.arraycopy(values, 0, newValues, 0, count);

            keys = newKeys;
            values = newValues;
        }

        keys[count] = key;
        values[count] = value;

        count++;
    }

    private int compare(int arg1Index, PatternArgument arg1,
                        PatternArgument arg2) {

        // comparisonKey is a unique representation of arg1Index and arg2.index.
        // presumes no more than N (of N << i) unique Pattern arguments.
        // see PatternDAG.compressPatterns ().
        int comparisonKey = (8192 << arg1Index) + arg2.index;
        int comparison = lookup(comparisonKey);

        if (comparison < 0) {
            if (arg1.implies(arg2)) {
                if (arg2.implies(arg1)) {
                    comparison = EQUAL;
                } else {
                    comparison = MORE_SPECIFIC;
                }
            } else {
                comparison = OTHERWISE;
            }

            addUnique(comparisonKey, comparison);
        }

        return comparison;
    }

    private int compare(Pattern pattern) {

        boolean isMoreSpecific = false;
        int count = arguments.length;
        PatternArgument[] arguments2 = pattern.arguments;

        for (int i = 0; i < count; i++) {
            switch (compare(i, arguments[i], arguments2[i])) {

                case MORE_SPECIFIC:
                    isMoreSpecific = true;
                    break;

                case EQUAL:
                    break;

                case OTHERWISE:
                    return OTHERWISE;
            }
        }

        return isMoreSpecific
                ? MORE_SPECIFIC
                : EQUAL;
    }

    public boolean isEqualTo(Pattern pattern) {
        return this.compare(pattern) == EQUAL;
    }

    public boolean isMoreSpecificThan(Pattern pattern) {
        return this.compare(pattern) == MORE_SPECIFIC;
    }

    public boolean isMoreSpecificThanOrEqualTo(Pattern pattern) {

        int comparison = this.compare(pattern);

        return (comparison == EQUAL) || (comparison == MORE_SPECIFIC);
    }

    /**
     Returns least general pattern for the supplied LookupPattern.
     Used in PatternTree lookup, which guarantees that all
     siblings are not comparable, hence if a pattern is found, then
     subsequent patterns need not be tested.
     */
    GraphPattern leastGeneralPatternFrom(List patterns)
            throws PatternStructureException {

        GraphPattern pattern, result;
        Iterator e = patterns.iterator();

        while (e.hasNext()) {
            pattern = (GraphPattern) e.next();
            result = pattern.getLeastGeneralPattern(this);

            if (result != null) {
                return result;
            }
        }

        return null;
    }

    /**
     Returns array of least general Patterns for this starting from a vector of Patterns.
     */
    GraphPattern[] leastGeneralPatternsFrom(List patterns)
            throws PatternStructureException {

        GraphPattern pattern;
        ArrayList list = new ArrayList();
        Iterator e = patterns.iterator();

        while (e.hasNext()) {
            pattern = (GraphPattern) e.next();

            pattern.gatherLeastGeneralPatterns(this, list);
        }

        GraphPattern[] result = new GraphPattern[list.size()];

        list.toArray(result);

        return result;
    }

    private int lookup(int key) {

        for (int i = 0; i < count; i++) {
            if (keys[i] == key) {
                return values[i];
            }
        }

        return -1;
    }

    /**     boolean [] b;
     PatternArgument [] x, y;
     if (((b[i] && x[i] >= y[i]) || !b[i]) for all i),
     then return {x[j]} for j such that !b[j],
     otherwise return null
     */
    public PatternArgument[][] projectFrom(Pattern pattern,
                                           boolean[] projectionMap,
                                           int degree) {

        PatternArgument arg1, arg2;
        PatternArgument[] result = new PatternArgument[degree];
        PatternArgument[] arguments2 = pattern.arguments;

        for (int i = 0, j = 0; i < projectionMap.length; i++) {
            arg1 = arguments[i];
            arg2 = arguments2[i];

            if (projectionMap[i]) {
                if (compare(i, arg1, arg2) == OTHERWISE) {
                    return null;
                }
            } else {
                result[j++] = arg2;
            }
        }

        return new PatternArgument[][]{
            result
        };
    }
}






