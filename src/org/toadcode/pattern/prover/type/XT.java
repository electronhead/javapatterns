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
import org.toadcode.pattern.core.Prover;
import org.toadcode.pattern.core.Rule;

import java.io.Serializable;


public class XT implements Serializable, Prover {

    public final static XT x = new XT();
    static final XT NULL = new XT();

    // Prover implementation

    public InstanceRule toInstanceRule(Object object) {
        return XT.is(object);
    }

    public Class getRuleClass() {
        return TypeRule.class;
    }

    public Rule getRuleUsingArgType(Class aClass) {
        return new OfRule(aClass);
    }

    // is
    public static IsRule is(Object object) {
        return new IsRule(object);
    }

    // of
    public static OfRule of(Class type) {
        return new OfRule(type);
    }
}






