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

import org.toadcode.pattern.core.InstanceRule;
import org.toadcode.pattern.core.Rule;

import java.util.Collection;
import java.util.Iterator;


public class InRule extends SetRule implements InstanceRule {

    public InRule(Object object) {

        if (object instanceof Object[]) {
            addObjects((Object[]) object);
        } else {
            addObject(object);
        }
    }

    public InRule(Collection objects) {
        super(objects);
    }

    public SetRule and(SetRule rule) {

        if (rule instanceof InRule) {
            return new InRule(intersection(objects, rule.objects));
        } else {
            return new InRule(difference(objects, rule.objects));
        }
    }

    public boolean implies(Rule rule) {

        if (rule instanceof InRule) {
            return ((InRule) rule).objects.containsAll(objects);
        }

        if (rule instanceof OutRule) {
            return hasEmptyIntersection(objects, ((OutRule) rule).objects);
        }

        return false;
    }

    public Rule not() {
        return new OutRule(objects);
    }

    public SetRule or(SetRule rule) {

        if (rule instanceof InRule) {
            return new InRule(union(objects, rule.objects));
        } else {
            return new OutRule(difference(rule.objects, objects));
        }
    }

    public Object toObject() {

        Iterator i = objects.iterator();

        if (i.hasNext()) {
            return i.next();
        } else {
            return null;
        }
    }

    public String toString() {
        return toString("IN   ");
    }
}






