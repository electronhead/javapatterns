package org.toadcode.pattern.prover.topic;

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

import org.toadcode.pattern.core.InstanceRule;
import org.toadcode.pattern.core.Rule;


public class NotLikeRule extends TopicRule implements InstanceRule {

    String string;

    /**
     * Insert the method's description here.
     * Creation date: (1/25/2000 10:52:53 AM)
     * @param string java.lang.String
     */
    public NotLikeRule(String string) {
        this.string = string;
    }

    public boolean implies(Rule rule) {

        if (rule instanceof NotLikeRule) {
            String s1 = string;
            String s2 = ((NotLikeRule) rule).string;

            return s1.equals(s2);
        }

        return false;
    }

    /**
     * Insert the method's description here.
     * Creation date: (1/27/2000 10:21:21 AM)
     * @param args java.lang.String[]
     */
    public static void main(String[] args) {

        LikeRule r, s;

        r = new LikeRule(args[0]);
        s = new LikeRule(args[1]);

        System.out.println(r.implies(s)
                ? "YES"
                : "NO");
    }

    public Rule not() {
        return new LikeRule(string);
    }

    /**
     * Insert the method's description here.
     * Creation date: (1/25/2000 10:00:42 PM)
     * @return java.lang.Object
     */
    public Object toObject() {
        return string;
    }

    /**
     * Insert the method's description here.
     * Creation date: (1/25/2000 10:00:42 PM)
     * @return java.lang.Object
     */
    public String toString() {
        return " NOT LIKE   [" + string + "]";
    }
}






