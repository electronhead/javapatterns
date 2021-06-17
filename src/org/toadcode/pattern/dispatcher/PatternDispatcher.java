package org.toadcode.pattern.dispatcher;

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

import org.toadcode.pattern.core.*;


public class PatternDispatcher extends PatternTree {

    /**
     * PatternDispatchers enable the programmer to dispatch
     * methods based on the run-time values passed to the
     * dispatcher instead of the usual Java mechanism of
     * of dispatching on the run-time type of the receiving
     * object and compile-time types of the parameters.
     *
     * It's actually a bit more general than resolutions based on types.
     * Resolution is based on the capabilities provided in the
     * super class, PatternDAG. PatternDispatcher merely provides
     * a mechanism to associate behavior (methods) with the patterns
     * supported in PatternDAG.
     *
     * @param provers   theorem provers that define the basic
     *                  pattern structure supported by the
     *                  dispatcher
     *
     * @see PatternDAG (Prover [])
     */
    public PatternDispatcher(Prover[] provers) {
        super(provers);
    }

    /**
     * Retrieve an object or the result of an Executable.
     *
     * @param rules Rules on which to base lookup.
     *
     * @return Object associated with the least rule implied by the supplied rules.
     * @see PatternTree.get (Rule [])
     */
    public Object get(Rule[] rules) throws PatternStructureException {

        try {
            Object object = super.get(rules);

            if (object == null) {    // [98FEB06]
                return null;
            } else if (object instanceof Executable) {
                Executable executable = (Executable) object;

                // [2000DEC05] executable = executable.copy ();
                Object result = executable.execute(rules);

                return result;
            } else {
                return object;
            }
        } catch (Exception e) {
            throw new PatternStructureException(
                    "PatternDispatcher.get EXCEPTION -- " + e.getMessage());
        } catch (Throwable e) {
            throw new PatternStructureException(
                    "PatternDispatcher.get EXCEPTION -- " + e.getMessage());
        }
    }

    /**
     * Installs an array of Executables using the rules explicitly or implicitly
     * defined in the Executables. The addition or replacement of values
     * is determined as if the elements of the array were "put" individually
     * in the same order as they appear in the array.
     *
     * @param executables   Executables to be installed
     */
    public void put(Executable[] executables) throws DispatchException {

        int i = executables.length;

        while (i-- > 0) {
            put(executables[i]);
        }
    }

    /**
     * Adds an Executable. The supplied rules override
     * executable.getRules (). If the rules are equivalent
     * to an existing tuple of rules, the executable will
     * will replace the existing object.
     *
     * @param rules                 Rules in the form of Rules
     * @param executable    The value associated with the supplied rules
     *
     * @see Executable.combineRules (Object [], Prover [])
     */
    public void put(Rule[] rules, Executable executable)
            throws DispatchException {
        put((Object[]) rules, executable);
    }

    /**
     * Installs an Executable. The supplied rules override
     * executable.getRules (). If the rules are equivalent
     * to an existing tuple of rules, the executable will
     * will replace the existing object.
     *
     * @param rules                 Rules in the form of instances or Rules
     * @param executable    The value associated with the supplied rules
     *
     * @see Executable.combineRules (Object [], Prover [])
     */
    public void put(Object[] rules, Executable executable)
            throws DispatchException {

        Rule[] actualRules = executable.combineRules(rules, provers);

        try {
            executable.setDispatcher(this);

            GraphPattern pattern = putReturningPattern(actualRules,
                    executable);

            executable.setPattern(pattern);
        } catch (PatternStructureException e) {
            System.out.println("PatternDispatcher.put EXCEPTION -- "
                    + e.getMessage());
        }
    }

    /**
     * Installs an Executable using the rules explicitly or implicitly
     * defined in the Executable. If the rules are equivalent
     * to an existing tuple of rules, the executable will
     * will replace the existing object.
     *
     * @param executable    Executable to be installed
     */
    public void put(Executable executable) throws DispatchException {
        put(executable.getRules(), executable);
    }
}






