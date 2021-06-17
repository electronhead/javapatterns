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
import org.toadcode.pattern.core.Rule;


public class EqualsRule extends MagnitudeRule implements InstanceRule {

    private OrderedDomain value;

    public EqualsRule(OrderedDomain value) {
        this.value = value;
    }

    public OrderedDomainRule and(OrderedDomainRule rule) {

        if (rule instanceof OrRule) {
            return ((OrRule) rule).and(this);
        }

        if (rule instanceof BetweenRule) {
            return ((BetweenRule) rule).and(this);
        }

        if (rule instanceof EqualsRule) {
            EqualsRule equalsRule = (EqualsRule) rule;

            if (getLowValue().lt(equalsRule.getValue())
                    && getHighValue().gt(equalsRule.getValue())) {
                return equalsRule;
            } else {
                return new FalseRule();
            }
        } else {
            return new FalseRule();    // FalseRule
        }
    }

    public boolean contains(OrderedDomain value) {
        return this.value.eq(value);
    }

    public OrderedDomain getHighValue() {
        return value;
    }

    public OrderedDomain getLowValue() {
        return value;
    }

    public OrderedDomain getValue() {
        return value;
    }

    public boolean gt(OrderedDomain value) {
        return this.value.gt(value);
    }

    public boolean implies(Rule rule) {

        if (rule instanceof OrRule) {
            OrRule orRule = (OrRule) rule;

            for (int i = 0; i < orRule.ruleCount(); i++) {
                if (implies(orRule.ruleAt(i))) {
                    return true;
                }
            }

            return false;
        }

        if (rule instanceof BetweenRule) {
            return ((BetweenRule) rule).contains(getValue());
        }

        if (rule instanceof EqualsRule) {
            return getValue().eq(((EqualsRule) rule).getValue());
        }

        if (rule instanceof FalseRule) {
            return false;
        }

        return false;
    }

    public boolean lt(OrderedDomain value) {
        return this.value.lt(value);
    }

    public Rule not() {
        return new OrRule(new BetweenRule(InfinimumScalar.INF, getValue()),
                new BetweenRule(getValue(), SupremumScalar.SUP));
    }

    public OrderedDomainRule or(OrderedDomainRule rule) {

        if (rule instanceof OrRule) {
            return ((OrRule) rule).or(this);
        }

        if (rule instanceof BetweenRule) {
            return ((BetweenRule) rule).or(this);
        }

        if (rule instanceof EqualsRule) {
            EqualsRule equalsRule = (EqualsRule) rule;

            if (getValue().lt(equalsRule.getValue())) {
                return new OrRule(this, equalsRule);    // low one first
            } else if (getValue().gt(equalsRule.getValue())) {
                return new OrRule(equalsRule, this);    // low one first
            } else {
                return this;
            }
        }

        return this;    // FalseRule
    }

    public void setHighValue(OrderedDomain value) {
        this.value = value;
    }

    public void setLowValue(OrderedDomain value) {
        this.value = value;
    }

    public Object toObject() {
        return value.toObject();
    }

    public String toString() {
        return "EQ   [" + value + "]";
    }
}






