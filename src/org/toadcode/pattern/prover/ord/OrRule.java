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

import java.util.ArrayList;


public class OrRule extends OrderedDomainRule {

    private ArrayList rules;

    public OrRule() {
        rules = new ArrayList();
    }

    public OrRule(MagnitudeRule rule1, MagnitudeRule rule2) {

        this();

        rules.add(rule1);
        rules.add(rule2);
    }

    public OrRule(MagnitudeRule rule1, MagnitudeRule rule2,
                  MagnitudeRule rule3) {

        this(rule1, rule2);

        rules.add(rule3);
    }

    public OrRule(MagnitudeRule rule1, MagnitudeRule rule2,
                  MagnitudeRule rule3, MagnitudeRule rule4) {

        this(rule1, rule2, rule3);

        rules.add(rule4);
    }

    public OrderedDomainRule and(OrderedDomainRule rule) {

        if (rule instanceof OrRule) {
            return copy().andCombine((OrRule) rule);
        }

        if (rule instanceof BetweenRule) {
            return copy().andCombine((BetweenRule) rule);
        }

        if (rule instanceof EqualsRule) {
            return copy().andCombine((EqualsRule) rule);
        }

        return rule;    // FalseRule
    }

    OrderedDomainRule andCombine(BetweenRule rule) {

        int count = ruleCount();
        OrderedDomainRule andRule;
        OrRule result = new OrRule();

        for (int i = 0; i < count; i++) {
            andRule = rule.and(ruleAt(i));

            if (andRule instanceof MagnitudeRule) {
                result.appendRule((MagnitudeRule) andRule);
            }
        }

        switch (result.ruleCount()) {

            case 0:
                return new FalseRule();

            case 1:
                return (MagnitudeRule) result.ruleAt(0);

            default :
                return result;
        }
    }

    // andCombine
    OrderedDomainRule andCombine(EqualsRule rule1) {

        int count = rules.size();
        OrderedDomain value = rule1.getValue();
        MagnitudeRule rule2;

        for (int i = 0; i < count; i++) {
            rule2 = ruleAt(i);

            if (rule2.contains(value)) {
                return rule1;
            }
        }

        return new FalseRule();
    }

    OrderedDomainRule andCombine(OrRule rule) {

        int count1 = ruleCount();
        int count2 = rule.ruleCount();
        OrderedDomainRule result = this;
        MagnitudeRule rule_i1, rule_i2;
        OrderedDomainRule andRule;

        for (int i1 = 0; i1 < count1; i1++) {
            rule_i1 = rule.ruleAt(i1);

            for (int i2 = 0; i2 < count2; i2++) {
                rule_i2 = rule.ruleAt(i2);
                andRule = rule_i1.and(rule_i2);

                if (andRule instanceof MagnitudeRule) {
                    result = result.or(andRule);
                }
            }
        }

        return result;
    }

    public void appendRule(MagnitudeRule rule) {
        rules.add(rule);
    }

    public OrRule copy() {

        OrRule result = new OrRule();

        for (int i = 0; i < ruleCount(); i++) {
            result.appendRule(ruleAt(i));
        }

        return result;
    }

    public boolean implies(Rule rule) {

        if (rule instanceof OrRule) {
            boolean implicationExists;
            OrRule rule1, rule2;

            rule1 = (OrRule) rule;
            rule2 = this;

            MagnitudeRule subRule1, subRule2;
            int count1, count2;

            count1 = rule1.ruleCount();
            count2 = rule2.ruleCount();

            for (int i2 = 0; i2 < count2; i2++) {
                subRule2 = rule2.ruleAt(i2);
                implicationExists = false;

                for (int i1 = 0; i1 < count1; i1++) {
                    subRule1 = rule1.ruleAt(i1);

                    if (subRule2.implies(subRule1)) {
                        implicationExists = true;

                        break;
                    }
                }

                if (!implicationExists) {
                    return false;
                }
            }

            return true;
        }

        if (rule instanceof BetweenRule) {
            BetweenRule betweenRule = (BetweenRule) rule;

            for (int i = 0; i < ruleCount(); i++) {
                if (!ruleAt(i).implies(betweenRule)) {
                    return false;
                }
            }

            return true;
        }

        if (rule instanceof EqualsRule) {
            return false;
        }

        // always false since OrRule always has at least two rules
        if (rule instanceof FalseRule) {
            return false;
        }

        return false;
    }

    public Rule not() {

        OrderedDomainRule result = (OrderedDomainRule) ruleAt(0).not();

        for (int i = 1; i < ruleCount(); i++) {
            result = result.and((OrderedDomainRule) ruleAt(i).not());
        }

        return result;
    }

    public OrderedDomainRule or(OrderedDomainRule rule) {

        if (rule instanceof OrRule) {
            return copy().orCombine((OrRule) rule);
        }

        if (rule instanceof BetweenRule) {
            return copy().orCombine((BetweenRule) rule);
        }

        if (rule instanceof EqualsRule) {
            return copy().orCombine((EqualsRule) rule);
        }

        return this;    // FalseRule
    }

    OrderedDomainRule orCombine(BetweenRule rule) {

        int count = rules.size();
        OrderedDomain low = rule.getLowValue();
        OrderedDomain high = rule.getHighValue();
        OrderedDomain betterLow, betterHigh;
        MagnitudeRule rule_i;
        OrderedDomain low_i, high_i;

        for (int i = 0; i < count; i++) {
            rule_i = ruleAt(i);
            low_i = rule_i.getLowValue();
            high_i = rule_i.getHighValue();

            // rule < rule_i
            if (high.lt(low_i)) {
                rules.set(i, rule);

                return this;
            }

            // rule completely covers rule_i
            if (low.lt(low_i) && high.gt(high_i)) {
                rules.remove(i);

                i = i - 1;

                continue;
            }

            // rule_i completely covers rule
            if (low.gt(low_i) && high.lt(high_i)) {
                return this;
            }

            // rule covers low end of rule_i, but not high end
            if (low.lt(low_i) && high.gt(low_i) && high.lt(high_i)) {
                rule_i.setLowValue(low);

                return this;
            }

            // rule covers high end of rule_i, but not low end
            if (low.gt(low_i) && low.lt(high_i) && high.gt(high_i)) {
                rule.setLowValue(low_i);
                rules.remove(i);

                i = i - 1;

                continue;
            }

            // rule > rule_i and last i
            if (low.gt(high_i) && (i == count - 1)) {
                rules.add(rule);

                continue;
            }

            // rule > rule_i
            if (low.gt(high_i)) {
                ;
            }
        }    // do nothing

        if (rules.size() == 1) {
            return (OrderedDomainRule) rules.get(0);
        } else {
            return this;
        }
    }

    // orCombine
    OrderedDomainRule orCombine(EqualsRule rule1) {

        int count = rules.size();
        OrderedDomain value = rule1.getValue();
        MagnitudeRule rule2;

        for (int i = 0; i < count; i++) {
            rule2 = (MagnitudeRule) rules.get(i);

            if (rule2.contains(value)) {
                return this;
            }

            if (rule2.gt(value)) {
                rules.set(i, rule1);

                return this;
            }
        }

        return this;
    }

    OrderedDomainRule orCombine(OrRule rule) {

        int count = rule.ruleCount();
        OrderedDomainRule result = this;
        MagnitudeRule rule_i;

        for (int i = 0; i < count; i++) {
            rule_i = rule.ruleAt(i);

            if (rule_i instanceof EqualsRule) {
                result = ((OrRule) result).orCombine((EqualsRule) rule_i);
            } else if (rule_i instanceof BetweenRule) {
                result = ((OrRule) result).orCombine((BetweenRule) rule_i);
            }
        }

        return result;
    }

    public MagnitudeRule ruleAt(int i) {
        return (MagnitudeRule) rules.get(i);
    }

    public int ruleCount() {
        return rules.size();
    }

    public String toString() {

        StringBuffer result = new StringBuffer("OR   [");

        for (int i = 0; i < ruleCount(); i++) {
            result.append(" " + i + " " + ruleAt(i));
        }

        return result.toString() + "]";
    }
}






