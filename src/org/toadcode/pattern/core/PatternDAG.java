package org.toadcode.pattern.core;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;


public class PatternDAG extends Object implements Serializable {

    protected ArrayList topPatterns, patterns;
    protected Prover[] provers;

    public PatternDAG(Prover[] provers) {

        this.provers = provers;
        patterns = new ArrayList();
        topPatterns = new ArrayList();
    }

    void addPattern(GraphPattern pattern) {
        patterns.add(pattern);
        pattern.setIndex(patterns.size() - 1);
    }

    protected void checkRulesForGet(Rule[] rules)
            throws PatternStructureException {
        checkRuleStructure(rules);
    }

    protected void checkRulesForPut(Rule[] rules)
            throws PatternStructureException {
        checkRuleStructure(rules);
    }

    protected void checkRuleStructure(Rule[] rules)
            throws PatternStructureException {

        int count = provers.length;

        // Check for rule count...
        if (rules.length != count) {
            throw new PatternStructureException(
                    "PatternDAG.checkRuleStructure -- Number of supplied rules ("
                    + rules.length + ") != " + count);
        }

        // Check to make each rule's class is a subclass of the prover's base rule class...
        int i = count;

        while (i-- > 0) {
            if (!provers[i].getRuleClass()
                    .isAssignableFrom(rules[i].getClass())) {
                throw new PatternStructureException(
                        "PatternDAG.checkRuleStructure::RULE / PROVER INCONSISTENCY: "
                        + rules[i] + " / " + provers[i]);
            }
        }
    }

    void compressPatterns() {

        topPatterns.trimToSize();
        patterns.trimToSize();

        /*
        Replace all pattern arguments with unique ones.

        NOTE: This section MUST be included for proper
        operation of the caching mechanism in LookupPattern.
        */
        ArrayList patArgs = new ArrayList();
        Iterator e1, e2;
        PatternArgument[] args;
        PatternArgument arg1, arg2;
        GraphPattern pat;
        int i;
        boolean wasFound;
        int replacementCount = 0;
        int totalCount = 0;

        e1 = patterns.iterator();

        while (e1.hasNext()) {    // iterate through all patterns
            pat = (GraphPattern) e1.next();
            args = pat.arguments;
            i = args.length;

            while (i-- > 0) {     // iterate through arguments
                totalCount++;

                arg1 = args[i];
                e2 = patArgs.iterator();
                wasFound = false;

                while (e2.hasNext()) {    // iterate through unique pattern args
                    arg2 = (PatternArgument) e2.next();

                    if (arg2.isEqualTo(arg1)) {
                        args[i] = arg2;
                        wasFound = true;

                        replacementCount++;

                        break;
                    }
                }

                if (!wasFound) {
                    patArgs.add(arg1);
                }
            }
        }

        int j = patArgs.size();

        while (j-- > 0) {
            ((PatternArgument) patArgs.get(j)).index = j;
        }
    }

    public Rule[] convertToRules(Object[] args)
            throws PatternStructureException {

        Rule[] rules = new Rule[args.length];

        for (int i = 0; i < args.length; i++) {
            rules[i] = provers[i].toInstanceRule(args[i]);
        }

        return rules;
    }

    public Object get(Rule[] rules) throws PatternStructureException {

        checkRulesForGet(rules);
        GraphPattern[] patterns = leastGeneralPatternsFor(rules);
        return (patterns.length == 0)
                ? null
                : patterns[0].getValue();
    }

    public Object get(Object[] args) throws PatternStructureException {
        return get(convertToRules(args));
    }

    public Object[] getAll(Rule[] rules) throws PatternStructureException {

        checkRulesForGet(rules);

        GraphPattern[] patterns = leastGeneralPatternsFor(rules);
        int i = patterns.length;
        Object[] result = new Object[i];

        while (i-- > 0) {
            result[i] = patterns[i].getValue();
        }

        return result;
    }

    public GraphPattern[] getTopPatterns() {

        GraphPattern[] result = new GraphPattern[topPatterns.size()];

        topPatterns.toArray(result);

        return result;
    }

    GraphPattern[] leastGeneralPatternsFor(Rule[] rules)
            throws PatternStructureException {

        // assumes rules have already been checked.
        LookupPattern pattern = new LookupPattern(rules);

        return pattern.leastGeneralPatternsFrom(topPatterns);
    }

    public void priint(String methodName, String text) {

        System.out.println("\r\nMETHOD " + getClass().getName() + "."
                + methodName);
        System.out.println("\t" + text);
        System.out.println(" ");
    }

    public void print() {
        System.out.println(this);
    }

    /**
     Return array of PatternArgument arrays that were projected from
     the set of all GraphPattern in this using the supplied LookupPattern.
     */
    public PatternArgument[][] projectUsing(
            Rule[] rules, boolean[] projectionMap)
            throws PatternStructureException {

        Iterator e = patterns.iterator();
        ArrayList projectedPats = new ArrayList();
        GraphPattern pat;
        PatternArgument[][] patArgs;
        LookupPattern pattern = new LookupPattern(rules);
        int degree = 0;

        for (int i = 0; i < projectionMap.length; i++) {
            if (!projectionMap[i]) {
                degree++;
            }
        }

        while (e.hasNext()) {
            pat = (GraphPattern) e.next();
            patArgs = pattern.projectFrom(pat, projectionMap, degree);
            if (patArgs != null) {
                for (int j = 0; j < patArgs.length; j++) {
                    projectedPats.add(patArgs[j]);
                }
            }
        }

        PatternArgument[][] result =
                new PatternArgument[projectedPats.size()][];

        projectedPats.toArray(result);

        return result;
    }

    public PatternArgument[][] projectUsing(
            Object[] args, boolean[] projectionMap)
            throws PatternStructureException {

        int i = args.length;
        Rule[] rules = new Rule[i];

        while (i-- > 0) {
            rules[i] = provers[i].toInstanceRule(args[i]);
        }

        return projectUsing(rules, projectionMap);
    }

    public void put(Rule[] rules, Object value)
            throws PatternStructureException {
        putReturningPattern(rules, value);
    }

    /**
     Returns the GraphPattern associated with the supplied rules.
     If the GraphPattern induced by the supplied rules isEqualto
     a GraphPattern in the PatternDAG, then the method replaces the existing
     pattern's value with the supplied value and returns the existing GraphPattern.
     */
    protected GraphPattern putReturningPattern(Rule[] rules, Object value)
            throws PatternStructureException {

        checkRulesForPut(rules);

        GraphPattern p = new GraphPattern(rules, value);
        GraphPattern msp;
        Iterator e;
        GraphPattern[] mgps = leastGeneralPatternsFor(rules);
        int count = mgps.length;

        if (count == 0) {                         // look at topPatterns
            e = topPatterns.iterator();

            while (e.hasNext()) {
                msp = (GraphPattern) e.next();    // more specific pattern

                if (msp.isMoreSpecificThan(p)) {    // msp cannot be more general than p
                    msp.addPatternAsMoreGeneral(p);
                    p.addPatternAsMoreSpecific(msp);
                }
            }
        }                                         // [1999AUG03] bug fix
        else {
            GraphPattern mgp;
            int i;

            i = count;

            while (i-- > 0) {
                mgp = mgps[i];

                if (mgp.isEqualTo(p)) {           // just replace value and return
                    mgp.setValue(p.getValue());

                    return mgp;
                }
            }

            i = count;

            while (i-- > 0) {
                mgp = mgps[i];                    // more general pattern

                GraphPattern[] msps = mgp.moreSpecific;

                for (int j = 0; j < msps.length; j++) {
                    msp = msps[j];

                    if (msp.isMoreSpecificThan(p)) {    // msp cannot be more general than p
                        mgp.removePatternAsMoreSpecific(msp);
                        msp.removePatternAsMoreGeneral(mgp);
                        msp.addPatternAsMoreGeneral(p);
                        p.addPatternAsMoreSpecific(msp);
                    }
                }

                mgp.addPatternAsMoreSpecific(p);
                p.addPatternAsMoreGeneral(mgp);
            }
        }

        addPattern(p);
        rebuildTopPatterns();
        compressPatterns();

        return p;
    }

    void rebuildTopPatterns() {

        topPatterns.clear();

        GraphPattern pat;
        Iterator pats = patterns.iterator();

        while (pats.hasNext()) {
            pat = (GraphPattern) pats.next();

            if (pat.moreGeneral.length == 0) {
                topPatterns.add(pat);
            }
        }
    }

    /**
     * Attempts to remove the GraphPattern associated with the supplied rules.
     * If successful, returns removed pattern, else returns null.
     *
     * @param rules                 The pattern that
     */
    public GraphPattern remove(Rule[] rules)
            throws PatternStructureException {

        checkRulesForGet(rules);

        LookupPattern lp = new LookupPattern(rules);
        GraphPattern p, mgp, msp, msgp;
        boolean shouldAdd;
        Iterator e = patterns.iterator();

        while (e.hasNext()) {         // loop through all patterns
            p = (GraphPattern) e.next();

            if (p.isEqualTo(lp)) {    // REMOVE <p>, RECONSTRUCT DAG, and EXIT
                if (p.moreGeneral.length == 0) {    // special handling for having no more generals
                    for (int i = 0; i < p.moreSpecific.length; i++) {    // loop through p's more specific
                        msp = p.moreSpecific[i];

                        msp.removePatternAsMoreGeneral(p);
                    }
                } else {
                    for (int i = 0; i < p.moreGeneral.length; i++) {    // loop through p's more general
                        mgp = p.moreGeneral[i];

                        mgp.removePatternAsMoreSpecific(p);

                        for (int j = 0; j < p.moreSpecific.length; j++) {    // loop through p's more specific
                            msp = p.moreSpecific[j];

                            msp.removePatternAsMoreGeneral(p);

                            shouldAdd = true;

                            for (int k = 0; k < msp.moreGeneral.length; k++) {    // loop through msp's more general
                                msgp = msp.moreGeneral[k];

                                if (msgp.isMoreSpecificThan(mgp)) {    // don't need to "connect" mgp and msp
                                    shouldAdd = false;

                                    break;
                                }
                            }

                            if (shouldAdd) {
                                mgp.addPatternAsMoreSpecific(msp);
                                msp.addPatternAsMoreGeneral(mgp);
                            }
                        }
                    }
                }

                removePattern(p);
                rebuildTopPatterns();
                compressPatterns();

                return p;
            }
        }

        return null;
    }

    void removePattern(GraphPattern pattern) {

        patterns.remove(pattern);

        Iterator e = patterns.iterator();
        GraphPattern p;
        int i = 0;

        while (e.hasNext()) {
            p = (GraphPattern) e.next();

            p.setIndex(i++);
        }
    }

    /**
     Return array of Objects that were selected from
     the set of all GraphPatterns in <this> using the supplied rules.
     */
    public Object[] selectUsing(Rule[] rules, boolean[] projectionMap)
            throws PatternStructureException {

        Iterator e = patterns.iterator();
        ArrayList selectedObjects = new ArrayList();
        GraphPattern pat;
        PatternArgument[][] patArgs;
        LookupPattern pattern = new LookupPattern(rules);
        int degree = 0;

        for (int i = 0; i < projectionMap.length; i++) {
            if (!projectionMap[i]) {
                degree++;
            }
        }

        while (e.hasNext()) {
            pat = (GraphPattern) e.next();
            patArgs = pattern.projectFrom(pat, projectionMap, degree);

            if (patArgs.length > 0) {
                selectedObjects.add(pat.getValue());
            }
        }

        Object[] result = new Object[selectedObjects.size()][];

        selectedObjects.toArray(result);

        return result;
    }

    public Object[] selectUsing(Object[] args, boolean[] projectionMap)
            throws PatternStructureException {

        Rule[] rules = new Rule[args.length];

        for (int i = 0; i < args.length; i++) {
            rules[i] = provers[i].toInstanceRule(args[i]);
        }

        return selectUsing(rules, projectionMap);
    }

    public String toString() {

        StringBuffer result = new StringBuffer("BEGIN "
                + getClass().getName()
                + "...");
        GraphPattern pat;
        Iterator e = patterns.iterator();

        while (e.hasNext()) {
            pat = (GraphPattern) e.next();

            result.append("\r\n\t");
            result.append(pat.toString());
        }

        result.append("\r\n\r\n...END " + getClass().getName());

        return result.toString();
    }
}






