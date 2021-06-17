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

import org.toadcode.pattern.core.Rule;


public class OfRule extends TypeRule {

    Class type;

    public OfRule(Class type) {
        this.type = type;
    }

    public boolean implies(Rule rule) {

        if (rule instanceof OfRule) {
            return ((OfRule) rule).type.isAssignableFrom(type);
        }

        if (rule instanceof IsRule) {
            return false;
        }

        if (rule instanceof NotOfRule) {
            Class type2 = ((NotOfRule) rule).type;

            return !(type.isAssignableFrom(type2)
                    || type2.isAssignableFrom(type));
        }

        if (rule instanceof IsNotRule) {
            Class type2 = ((IsNotRule) rule).value.getClass();

            return !(type.isAssignableFrom(type2)
                    || type2.isAssignableFrom(type));
        }

        return false;
    }

    public Rule not() {
        return new NotOfRule(type);
    }

    public String toString() {
        return "OF   [" + type + "]";
    }
}






