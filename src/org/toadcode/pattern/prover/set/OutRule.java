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


public class OutRule extends SetRule {

    public OutRule(Object object) {

        if (object instanceof Object[]) {
            addObjects((Object[]) object);
        } else {
            addObject(object);
        }
    }

    public OutRule(Collection objects) {
        super(objects);
    }

    public SetRule and(SetRule rule) {

        if (rule instanceof InRule) {
            return new InRule(difference(rule.objects, objects));
        } else {
            return new OutRule(union(objects, rule.objects));
        }
    }

    public boolean implies(Rule rule) {

        if (rule instanceof InRule) {
            return false;
        }

        if (rule instanceof OutRule) {
            return objects.containsAll(((OutRule) rule).objects);
        }

        return false;
    }

    public Rule not() {
        return new InRule(objects);
    }

    public SetRule or(SetRule rule) {

        if (rule instanceof InRule) {
            return new OutRule(difference(objects, rule.objects));
        } else {

            // OutRule
            return new OutRule(intersection(rule.objects, objects));
        }
    }

    public String toString() {
        return toString("OUT  ");
    }
}






