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

import org.toadcode.pattern.core.InstanceRule;
import org.toadcode.pattern.core.Rule;


public class IsRule extends TypeRule implements InstanceRule {

    Object value;

    public IsRule(Object value) {

        this.value = (value == null)
                ? XT.NULL
                : value;
    }

    public boolean implies(Rule rule) {

        if (rule instanceof OfRule) {
            Class type = value.getClass();
            Class type2 = ((OfRule) rule).type;

            return type2.isAssignableFrom(type);
        }

        if (rule instanceof IsRule) {
            Object value2 = ((IsRule) rule).value;

            return value.equals(value2);
        }

        if (rule instanceof NotOfRule) {
            Class type = value.getClass();
            Class type2 = ((NotOfRule) rule).type;

            return !(type.isAssignableFrom(type2)
                    || type2.isAssignableFrom(type));
        }

        if (rule instanceof IsNotRule) {
            Object value2 = ((IsNotRule) rule).value;

            return !value.equals(value2);
        }

        return false;
    }

    public Rule not() {
        return new IsNotRule(value);
    }

    public Object toObject() {
        return value;
    }

    public String toString() {
        return "IS   [" + value + "]";
    }
}






