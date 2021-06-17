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

import java.io.Serializable;

abstract public class Pattern extends Object implements Serializable {

    PatternArgument[] arguments;

    public Pattern(Rule[] rules) {

        arguments = new PatternArgument[rules.length];

        for (int i = 0; i < rules.length; i++) {
            arguments[i] = new PatternArgument(rules[i]);
        }
    }

    public boolean equals(Object object) {

        if (object instanceof Pattern) {
            Pattern pattern = (Pattern) object;

            return this.isEqualTo(pattern);
        }

        if (object == this) {
            return true;
        }

        return false;
    }

    public Rule[] getRules() {

        int count = arguments.length;
        Rule[] result = new Rule[count];

        for (int i = 0; i < count; i++) {
            result[i] = arguments[i].argumentRule;
        }

        return result;
    }

    public boolean isAmbiguousWith(Pattern pattern) {
        Rule rule1, rule2;
        boolean b12, b21, se12, se21;
        b12 = false;
        b21 = false;
        int i = arguments.length;
        while (i-- > 0) {
            rule1 = arguments[i].argumentRule;
            rule2 = pattern.arguments[i].argumentRule;
            se12 = !rule2.implies(rule1.not()); // instead of rule1.implies (rule2)
            se21 = !rule1.implies(rule2.not()); // instead of rule2.implies (rule1)
            if (!(se12 || se21)) return false; // not comparable
            if (se12 && !se21) b12 = true;
            if (!se12 && se21) b21 = true;
        }
        return b12 && b21;
    }

    boolean isEqualTo(Pattern pattern) {
        return this.isMoreSpecificThanOrEqualTo(pattern)
                && pattern.isMoreSpecificThanOrEqualTo(this);
    }

    /**     Return true if all arguments are more specific or equal and
     at least one argument is more specific.
     */
    public boolean isMoreSpecificThan(Pattern pattern) {
        return this.isMoreSpecificThanOrEqualTo(pattern)
                && !pattern.isMoreSpecificThanOrEqualTo(this);
    }

    /**     Return true if all arguments are more specific or equal and
     at least one argument is more specific.
     */
    public abstract boolean isMoreSpecificThanOrEqualTo(Pattern pattern);

    public String toString() {

        PatternArgument arg;
        StringBuffer result = new StringBuffer();

        for (int i = 0; i < arguments.length; i++) {
            arg = arguments[i];

            result.append("\r\n\t" + arg.toString());
        }

        return result.toString();
    }
}






