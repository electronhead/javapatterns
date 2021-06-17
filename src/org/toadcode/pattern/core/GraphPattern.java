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


public class GraphPattern extends Pattern {

    private int index;
    public GraphPattern[] moreGeneral;
    public GraphPattern[] moreSpecific;
    private Object value;

    public GraphPattern(Rule[] rules, Object value)
            throws PatternStructureException {

        super(rules);

        this.moreSpecific = new GraphPattern[0];
        this.moreGeneral = new GraphPattern[0];
        this.value = value;
    }

    void addPatternAsMoreGeneral(GraphPattern pattern) {
        moreGeneral = addPatternTo(moreGeneral, pattern);
    }

    void addPatternAsMoreSpecific(GraphPattern pattern) {
        moreSpecific = addPatternTo(moreSpecific, pattern);
    }

    GraphPattern[] addPatternTo(GraphPattern[] patterns,
                                GraphPattern pattern) {

        GraphPattern[] tmp = new GraphPattern[patterns.length + 1];

        System.arraycopy(patterns, 0, tmp, 0, patterns.length);

        tmp[patterns.length] = pattern;

        return tmp;
    }

    /** Gather least general GraphPatterns for the supplied LookupPattern. */
    final boolean gatherLeastGeneralPatterns(
            LookupPattern lookupPattern, ArrayList gatheredPatterns)
            throws PatternStructureException {

        if (lookupPattern.isEqualTo(this)) {
            if (!gatheredPatterns.contains(this)) {
                gatheredPatterns.add(this);
            }

            return true;
        }

        if (lookupPattern.isMoreSpecificThan(this)) {
            GraphPattern pattern;
            boolean found = false;

            for (int i = 0; i < moreSpecific.length; i++) {
                pattern = moreSpecific[i];
                found = found
                        || pattern.gatherLeastGeneralPatterns(lookupPattern,
                                gatheredPatterns);
            }

            if (!found && !gatheredPatterns.contains(this)) {
                gatheredPatterns.add(this);
            }

            return true;
        }

        return false;
    }

    PatternArgument[] getArguments() {
        return arguments;
    }

    private int getIndex() {
        return index;
    }

    /**
     Returns least general pattern for the supplied LookupPattern.
     Used in PatternTree lookup, which guarantees that all
     siblings are not comparable. Hence if a pattern is found, then
     subsequent patterns need not be tested.
     */
    GraphPattern getLeastGeneralPattern(LookupPattern lookupPattern)
            throws PatternStructureException {

        if (lookupPattern.isEqualTo(this)) {
            return this;
        }

        if (lookupPattern.isMoreSpecificThan(this)) {
            GraphPattern pattern;

            for (int i = 0; i < moreSpecific.length; i++) {
                pattern =
                        moreSpecific[i].getLeastGeneralPattern(lookupPattern);

                if (pattern != null) {
                    return pattern;
                }
            }

            return this;
        }

        return null;
    }

    public GraphPattern[] getMoreSpecificPatterns() {
        return moreSpecific;
    }

    public Object getValue() {
        return value;
    }

    /**     Return true if all arguments are more specific or equal and
     at least one argument is more specific.
     */
    public boolean isMoreSpecificThanOrEqualTo(Pattern pattern) {

        PatternArgument[] arguments2 = pattern.arguments;
        int count = arguments.length;

        for (int i = 0; i < count; i++) {
            if (!arguments[i].implies(arguments2[i])) {
                return false;
            }
        }

        return true;
    }

    void removePatternAsMoreGeneral(GraphPattern p) {
        moreGeneral = removePatternFrom(moreGeneral, p);
    }

    void removePatternAsMoreSpecific(GraphPattern p) {
        moreSpecific = removePatternFrom(moreSpecific, p);
    }

    GraphPattern[] removePatternFrom(GraphPattern[] patterns,
                                     GraphPattern pattern) {

        int index = -1;

        for (int i = 0; i < patterns.length; i++) {
            if (patterns[i] == pattern) {
                index = i;

                break;
            }
        }

        if (index == -1) {
            return patterns;
        } else {
            int newLength = patterns.length - 1;
            GraphPattern[] tmp = new GraphPattern[newLength];

            if (index == 0) {
                System.arraycopy(patterns, 1, tmp, 0, newLength);
            } else {
                System.arraycopy(patterns, 0, tmp, 0, index);
                System.arraycopy(patterns, index + 1, tmp, index,
                        newLength - index);
            }

            return tmp;
        }
    }

    void setIndex(int index) {
        this.index = index;
    }

    void setValue(Object value) {
        this.value = value;
    }

    public String toString() {

        Iterator args, patterns;
        PatternArgument arg;
        GraphPattern pattern;
        int count;
        StringBuffer result = new StringBuffer();

        // include index...
        result.append("\r\n[");
        result.append(index);
        result.append("]");

        // include more general indices
        result.append(" <[");

        for (int i = 0; i < moreGeneral.length; i++) {
            if (i > 0) {
                result.append(", ");
            }

            result.append(moreGeneral[i].getIndex());
        }

        result.append("]");

        // include more specific indices
        result.append(" >[");

        for (int i = 0; i < moreSpecific.length; i++) {
            if (i > 0) {
                result.append(", ");
            }

            result.append(moreSpecific[i].getIndex());
        }

        result.append("]");
        result.append("\r\n\r\n\tTYPE   [");
        result.append(value.getClass());
        result.append("]\r\n\tVALUE  ");
        result.append(value.toString());
        result.append("\r\n");

        return result.toString() + super.toString();
    }
}






