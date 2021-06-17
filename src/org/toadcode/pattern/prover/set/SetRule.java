package org.toadcode.pattern.prover.set;

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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


abstract public class SetRule extends Object implements Rule {

    protected HashSet objects;

    public SetRule() {
        this.objects = new HashSet();
    }

    public SetRule(Collection objects) {
        this.objects = new HashSet(objects);
    }

    void addObject(Object object) {
        objects.add(object);
    }

    void addObjects(Object[] objs) {

        int count = objs.length;

        for (int i = 0; i < count; i++) {
            objects.add(objs[i]);
        }
    }

    abstract public SetRule and(SetRule rule);

    static Set difference(Set v1, Set v2) {

        HashSet result = new HashSet(v1);
        Iterator e = v2.iterator();

        while (e.hasNext()) {
            result.remove(e.next());
        }

        return result;
    }

    /** This method is needed because the Set operations applied in subclasses that
     use Object.equals (..).
     */
    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }

        if (object == null) {
            return false;
        }

        if (object instanceof SetRule) {
            SetRule rule2 = (SetRule) object;

            return this.implies(rule2) && rule2.implies(this);
        }

        return false;
    }

    // helper methods
    static boolean hasEmptyIntersection(Set v1, Set v2) {

        Iterator e = v1.iterator();

        while (e.hasNext()) {
            Object element = e.next();

            if (v2.contains(element)) {
                return false;
            }
        }

        return true;
    }

    // helper methods
    static Set intersection(Set v1, Set v2) {

        HashSet result = new HashSet();
        Iterator e = v1.iterator();

        while (e.hasNext()) {
            Object element = e.next();

            if (v2.contains(element)) {
                result.add(element);
            }
        }

        return result;
    }

    abstract public SetRule or(SetRule rule);

    public String toString(String prefix) {

        if (objects.size() == 0) {
            return prefix + "[]";
        } else {
            StringBuffer result = new StringBuffer(prefix);
            Iterator e = objects.iterator();

            while (e.hasNext()) {
                result.append("[" + e.next() + "]");
            }

            return result.toString();
        }
    }

    static Set union(Set v1, Set v2) {

        HashSet result = new HashSet(v1);

        result.addAll(v2);

        return result;
    }
}






