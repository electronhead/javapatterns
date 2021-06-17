package org.toadcode.pattern.prover.ord;

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
import java.util.Date;


public class XO implements Serializable, Prover {

    private final static Rule ARGTYPE_RULE = new BetweenRule(
            InfinimumScalar.INF, SupremumScalar.SUP
    );

    public static final XO x = new XO();

    // Prover implementation

    public Class getRuleClass() {
        return OrderedDomainRule.class;
    }

    public Rule getRuleUsingArgType(Class aClass) {   // always true
        return ARGTYPE_RULE;
    }

    public InstanceRule toInstanceRule(Object object) {
        return XO.eq(object);
    }

    // Methods

    public static EqualsRule eq(double value) {
        return eq(new DoubleScalar(new Double(value)));
    }

    public static EqualsRule eq(float value) {
        return eq(new FloatScalar(new Float(value)));
    }

    public static EqualsRule eq(int value) {
        return eq(new IntegerScalar(new Integer(value)));
    }

    public static EqualsRule eq(long value) {
        return eq(new LongScalar(new Long(value)));
    }

    // eq
    static EqualsRule eq(OrderedDomain value) {
        return new EqualsRule(value);
    }

    public static EqualsRule eq(Double value) {
        return eq(new DoubleScalar(value));
    }

    public static EqualsRule eq(Float value) {
        return eq(new FloatScalar(value));
    }

    public static EqualsRule eq(Integer value) {
        return eq(new IntegerScalar(value));
    }

    public static EqualsRule eq(Long value) {
        return eq(new LongScalar(value));
    }

    public static EqualsRule eq(Object value) {

        if (value instanceof String) {
            return eq((String) value);
        }

        if (value instanceof Date) {
            return eq((Date) value);
        }

        if (value instanceof Integer) {
            return eq((Integer) value);
        }

        if (value instanceof Long) {
            return eq((Long) value);
        }

        if (value instanceof Float) {
            return eq((Float) value);
        }

        if (value instanceof Double) {
            return eq((Double) value);
        }

        return null;
    }

    public static EqualsRule eq(String value) {
        return eq(new StringScalar(value));
    }

    public static EqualsRule eq(Date value) {
        return eq(new DateScalar(value));
    }

    // ge
    static OrderedDomainRule ge(OrderedDomain value) {
        return (OrderedDomainRule) lt(value).not();
    }

    public static BetweenRule gt(double value) {
        return gt(new DoubleScalar(value));
    }

    public static BetweenRule gt(float value) {
        return gt(new FloatScalar(value));
    }

    public static BetweenRule gt(int value) {
        return gt(new IntegerScalar(value));
    }

    public static BetweenRule gt(long value) {
        return gt(new LongScalar(value));
    }

    // gt
    static BetweenRule gt(OrderedDomain value) {
        return new BetweenRule(value, SupremumScalar.SUP);
    }

    // le
    static OrderedDomainRule le(OrderedDomain value) {
        return (OrderedDomainRule) gt(value).not();
    }

    public static BetweenRule lt(double value) {
        return lt(new DoubleScalar(value));
    }

    public static BetweenRule lt(float value) {
        return lt(new FloatScalar(value));
    }

    public static BetweenRule lt(int value) {
        return lt(new IntegerScalar(value));
    }

    public static BetweenRule lt(long value) {
        return lt(new LongScalar(value));
    }

    // lt
    static BetweenRule lt(OrderedDomain value) {
        return new BetweenRule(InfinimumScalar.INF, value);
    }

    public static BetweenRule lt(String value) {
        return lt(new StringScalar(value));
    }

    public static BetweenRule lt(java.sql.Date value) {
        return lt(new DateScalar(value));
    }
}






