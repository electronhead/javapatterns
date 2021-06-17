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
import org.toadcode.pattern.core.Prover;
import org.toadcode.pattern.core.Rule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class XS implements Serializable, Prover {

    private final static Rule ARGTYPE_RULE = new OutRule(new ArrayList());
    public static XS x = new XS();

    // Prover implementation
    public InstanceRule toInstanceRule(Object object) {
        return XS.in(object);
    }

    public Class getRuleClass() {
        return SetRule.class;
    }

    public Rule getRuleUsingArgType(Class aClass) { // always true
        return ARGTYPE_RULE;
    }

    // in
    public static InRule in(Object object) {
        return new InRule(object);
    }

    public static InRule in(Collection collection) {
        return new InRule(collection);
    }

    // out
    public static OutRule out(Object object) {
        return new OutRule(object);
    }

    public static OutRule out(Collection collection) {
        return new OutRule(collection);
    }
}






