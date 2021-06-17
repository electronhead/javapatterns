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

import org.toadcode.pattern.core.Rule;


public abstract class TopicRule implements Rule {

    /**
     * Insert the method's description here.
     * Creation date: (1/27/2000 10:21:21 AM)
     * @param args java.lang.String[]
     */
    public static void main(String[] args) {

        String s1 = args[0];
        String s2 = args[1];

        System.out.println("IMPLIES? " + (stringImpliesString(s1, s2)
                ? "YES"
                : "NO"));
        System.out.println("INTERSECTS? " + (stringIntersectsString(s1, s2)
                ? "YES"
                : "NO"));
        System.out.println("L=>L: "
                + (new LikeRule(s1).implies(new LikeRule(s2))
                ? "YES"
                : "NO"));
        System.out.println("L=>N: "
                + (new LikeRule(s1).implies(new NotLikeRule(s2))
                ? "YES"
                : "NO"));
        System.out.println("N=>L: "
                + (new NotLikeRule(s1).implies(new LikeRule(s2))
                ? "YES"
                : "NO"));
        System.out.println("N=>N: "
                + (new NotLikeRule(s1).implies(new NotLikeRule(s2))
                ? "YES"
                : "NO"));
    }

    public static boolean stringEqualsString(String s1, String s2) {
        return stringImpliesString(s1, s2) && stringImpliesString(s2, s1);
    }

    /*
    Assumes that
    1. each * is alone between periods ('.').
    2. each # is at the end of a string and "alone".

    implies ("a.bcd.e", "a.*.e")                                                    == true
    implies ("a.w.xyz.q", "a.w.abc.#")                      == false
    implies (any non-empty String, "#")     == true
    implies (any non-empty String, "*")             == false
    */
    public static boolean stringImpliesString(String s1, String s2) {

        if (s1 == null) {
            return false;
        } else if (s2 == null) {
            return false;
        } else {
            int len1 = s1.length();
            int len2 = s2.length();
            int i1 = 0;                          // running index of s1
            int i2 = 0;                          // running index of s2
            char c1, c2;

            while ((i1 < len1) && (i2 < len2)) {    // within bounds of s1 and s2
                c1 = s1.charAt(i1);
                c2 = s2.charAt(i2);

                if (c2 == '#') {
                    i1 = len1;
                    i2 = len2;
                } else if (c2 == '*') {
                    i2 += 2;                        // move two characters beyond '*'
                    i1 = s1.indexOf('.', i1);       // next '.' in s1

                    if (i1 < 0) {

                        // '.' not found
                        i1 = len1;
                    } else {

                        // '.' found, move one character beyond '.'
                        i1++;
                    }
                } else if (c1 == c2) {
                    i1++;
                    i2++;
                } else {
                    return false;
                }
            }

            return (i1 >= len1) && (i2 >= len2);    // s1 and s2 exhausted
        }
    }

    /*
    Assumes that
    1. each * is alone between periods ('.').
    2. each # is at the end of a string and "alone".

    intersects ("a.bcd.*", "a.*.e")                         == true
    intersects ("a.w.xyz.q", "a.w.abc.#")                   == false
    intersects (any non-empty String, "#")                  == true
    intersects (any non-empty String, "*")                  == false
    */
    public static boolean stringIntersectsString(String s1, String s2) {

        if (s1 == null) {
            return false;
        } else if (s2 == null) {
            return false;
        } else {
            int len1 = s1.length();
            int len2 = s2.length();
            int i1 = 0;                          // running index of s1
            int i2 = 0;                          // running index of s2
            char c1, c2;

            while ((i1 < len1) && (i2 < len2)) {    // within bounds of s1 and s2
                c1 = s1.charAt(i1);
                c2 = s2.charAt(i2);

                if ((c1 == '#') || (c2 == '#')) {
                    i1 = len1;
                    i2 = len2;
                } else if (c2 == '*') {
                    i2 += 2;                        // move two characters beyond '*'
                    i1 = s1.indexOf('.', i1);       // next '.' in s1

                    if (i1 < 0) {

                        // '.' not found
                        i1 = len1;
                    } else {

                        // '.' found, move one character beyond '.'
                        i1++;
                    }
                } else if (c1 == '*') {
                    i1 += 2;                        // move two characters beyond '*'
                    i2 = s2.indexOf('.', i2);       // next '.' in s2

                    if (i2 < 0) {

                        // '.' not found
                        i2 = len2;
                    } else {

                        // '.' found, move one character beyond '.'
                        i2++;
                    }
                } else if (c1 == c2) {
                    i1++;
                    i2++;
                } else {
                    return false;
                }
            }

            return (i1 >= len1) && (i2 >= len2);    // s1 and s2 exhausted
        }
    }
}






