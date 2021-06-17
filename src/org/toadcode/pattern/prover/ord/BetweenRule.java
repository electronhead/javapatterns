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

import org.toadcode.pattern.core.Rule;

public class BetweenRule extends MagnitudeRule {

    private OrderedDomain lowValue, highValue;

    BetweenRule(OrderedDomain lowValue, OrderedDomain highValue) {
        this.lowValue = lowValue;
        this.highValue = highValue;
    }

    public OrderedDomainRule and(OrderedDomainRule rule) {

        if (rule instanceof EqualsRule) {
            EqualsRule equalsRule = (EqualsRule) rule;

            if (getLowValue().lt(equalsRule.getValue())
                    && getHighValue().gt(equalsRule.getValue())) {
                return equalsRule;
            } else {
                return new FalseRule();
            }
        }

        if (rule instanceof BetweenRule) {
            BetweenRule betweenRule = (BetweenRule) rule;

            if (betweenRule.getLowValue().gt(getHighValue())
                    || getLowValue().gt(betweenRule.getHighValue())) {
                return new FalseRule();
            } else if (getHighValue().gt(betweenRule.getLowValue())
                    || getLowValue().lt(betweenRule.getHighValue())) {
                return new BetweenRule(getLowValue()
                        .max(betweenRule.getLowValue()), getHighValue()
                        .min(betweenRule.getHighValue()));
            } else {
                return new FalseRule();
            }
        }

        if (rule instanceof OrRule) {
            return ((OrRule) rule).and(this);
        } else {    // FalseRule
            return rule;
        }
    }

    boolean contains(OrderedDomain value) {
        return lowValue.lt(value) && highValue.gt(value);
    }

    public boolean eq(BetweenRule rule) {
        return getLowValue().eq(rule.getLowValue())
                || getHighValue().eq(rule.getHighValue());
    }

    OrderedDomain getHighValue() {
        return highValue;
    }

    OrderedDomain getLowValue() {
        return lowValue;
    }

    public boolean gt(BetweenRule rule) {
        return getLowValue().gt(rule.getHighValue())
                || getLowValue().eq(rule.getHighValue());
    }

    boolean gt(OrderedDomain value) {
        return lowValue.gt(value) || lowValue.eq(value);
    }

    public boolean implies(Rule rule) {

        if (rule instanceof EqualsRule) {
            return false;
        }

        if (rule instanceof BetweenRule) {
            BetweenRule betweenRule = (BetweenRule) rule;

            return (betweenRule.getLowValue()
                    .lt(getLowValue()) || betweenRule.getLowValue()
                    .eq(getLowValue())) && (betweenRule.getHighValue()
                    .gt(getHighValue()) || betweenRule.getHighValue()
                    .eq(getHighValue()));
        }

        if (rule instanceof OrRule) {
            OrRule orRule = (OrRule) rule;

            for (int i = 0; i < orRule.ruleCount(); i++) {
                if (implies(orRule.ruleAt(i))) {
                    return true;
                }
            }

            return false;
        }

        if (rule instanceof FalseRule) {
            return false;
        }

        return false;
    }

    public boolean lt(BetweenRule rule) {
        return getHighValue().lt(rule.getLowValue())
                || getHighValue().eq(rule.getLowValue());
    }

    boolean lt(OrderedDomain value) {
        return highValue.lt(value) || highValue.eq(value);
    }

    public Rule not() {

        if (InfinimumScalar.INF.eq(getLowValue())
                && SupremumScalar.SUP.eq(getHighValue())) {
            return new FalseRule();
        } else if (InfinimumScalar.INF.eq(getLowValue())) {
            return new OrRule(new EqualsRule(getHighValue()),
                    new BetweenRule(getHighValue(),
                            SupremumScalar.SUP));
        } else if (SupremumScalar.SUP.eq(getHighValue())) {
            return new OrRule(new BetweenRule(InfinimumScalar
                    .INF, getLowValue()), new EqualsRule(getLowValue()));
        } else {
            return new OrRule(
                    new BetweenRule(InfinimumScalar.INF, getLowValue()),
                    new EqualsRule(getLowValue()),
                    new EqualsRule(getHighValue()),
                    new BetweenRule(getHighValue(), SupremumScalar.SUP));
        }
    }

    public OrderedDomainRule or(OrderedDomainRule rule) {

        if (rule instanceof EqualsRule) {
            EqualsRule equalsRule = (EqualsRule) rule;

            if (getLowValue().lt(equalsRule.getValue())
                    && getHighValue().gt(equalsRule.getValue())) {
                return this;
            } else if (getLowValue().gt(equalsRule.getValue())) {
                return new OrRule(equalsRule, this);
            } else {
                return new OrRule(this, equalsRule);
            }
        }

        if (rule instanceof BetweenRule) {
            BetweenRule betweenRule = (BetweenRule) rule;

            if (lt(betweenRule)) {
                return new OrRule(this, betweenRule);    // low one first
            } else if (gt(betweenRule)) {
                return new OrRule(betweenRule, this);    // low one first
            } else {
                return new BetweenRule(getLowValue()
                        .min(betweenRule.getLowValue()), getHighValue()
                        .max(betweenRule.getHighValue()));
            }
        }

        if (rule instanceof OrRule) {
            return ((OrRule) rule).or(this);
        } else {    // FalseRule
            return this;
        }
    }

    void setHighValue(OrderedDomain highValue) {
        this.highValue = highValue;
    }

    void setLowValue(OrderedDomain lowValue) {
        this.lowValue = lowValue;
    }

    public String toString() {
        return "BTW  [" + lowValue + " < X < " + highValue + "]";
    }
}






