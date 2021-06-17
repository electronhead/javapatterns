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
import org.toadcode.util.Utility;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class Executable extends Object implements Cloneable {

    GraphPattern pattern;
    PatternDispatcher dispatcher;
    Object[] args;
    Method executeMethod;

    Rule[] combineRules(Object[] rules, Prover[] provers)
            throws DispatchException {

        Rule[] result = new Rule[provers.length];
        Class[] parameterTypes = getExecuteMethod().getParameterTypes();
        int i = parameterTypes.length;

        if (rules == null) {
            while (i-- > 0) {
                result[i] =
                        provers[i].getRuleUsingArgType(parameterTypes[i]);
            }
        } else {
            Object obj;
            Prover prover;
            Rule rule;
            Class type;

            while (i-- > 0) {
                obj = rules[i];
                prover = provers[i];
                type = parameterTypes[i];

                if (obj instanceof Rule) {
                    rule = (Rule) obj;

                    if (!rule.implies(prover.getRuleUsingArgType(type))) {    // make sure rule implies parameter type's implicit rule
                        throw new DispatchException(
                                "Executable.combineRules::RULE / PARAMETER INCONSISTENCY: "
                                + rule + " / " + type.getName());
                    }

                    result[i] = rule;
                } else if (obj == Arg.class) {
                    result[i] = prover.getRuleUsingArgType(type);
                } else {
                    result[i] = prover.toInstanceRule(obj);
                }
            }
        }

        return result;
    }

    Executable copy() throws CloneNotSupportedException {
        return (Executable) clone();
    }

    public Object execute(Rule[] rules) throws DispatchException, Throwable {

        Object[] args = new Object[rules.length];

        for (int i = 0; i < rules.length; i++) {
            if ((rules[i] instanceof InstanceRule)) {
                args[i] = ((InstanceRule) rules[i]).toObject();
            } else {
                throw new DispatchException(
                        "Executable.execute EXCEPTION -- non-InstanceRule supplied.");
            }
        }

        return execute(args);
    }

    public Object execute(Object[] args) throws DispatchException, Throwable {

        this.args = args;

        Object result = null;
        Method method = null;

        try {
            method = getExecuteMethod();
            result = method.invoke(this, args);
        } catch (java.lang.IllegalAccessException e) {
            System.err.println("Executable.execute (Object[])::METHOD = "
                    + method);

            for (int i = 0; i < args.length; i++) {
                System.err.println("OBJECT [" + i + " ] = " + args[i]);
            }

            e.printStackTrace();

            throw new DispatchException(
                    "Executable.execute IllegalAccessException -- "
                    + e.getMessage());
        } catch (InvocationTargetException e) {
            System.err.println("Executable.execute (Object[])::METHOD = "
                    + method);

            for (int i = 0; i < args.length; i++) {
                System.err.println("OBJECT [" + i + " ] = " + args[i]);
            }

            e.printStackTrace();

            Throwable targetException = e.getTargetException();

            throw targetException;
        } catch (Exception e) {
            System.err.println("Executable.execute (Object[])::METHOD = "
                    + method);

            for (int i = 0; i < args.length; i++) {
                System.err.println("OBJECT [" + i + " ] = " + args[i]);
            }

            e.printStackTrace();

            throw new DispatchException("Executable.execute EXCEPTION -- "
                    + e.getMessage());
        }

        return result;
    }

    PatternDispatcher getDispatcher() {
        return dispatcher;
    }

    protected Method getExecuteMethod() throws DispatchException {

        if (executeMethod == null) {
            try {
                executeMethod = Utility.findDeclaredMethod(getClass(),
                        "execute");
            } catch (Exception ex) {
                throw new DispatchException(
                        "Executable.getExecuteMethod EXCEPTION -- Missing <execute> method.");
            }
        }

        return executeMethod;
    }

    public Object[] getRules() {
        return null;
    }

    public Object nextMethod() throws DispatchException, Throwable {

        GraphPattern[] moreGeneral = pattern.moreGeneral;

        if (moreGeneral.length > 0) {
            GraphPattern nextPattern = moreGeneral[0];
            LookupPattern lookup =
                    new LookupPattern(getDispatcher().convertToRules(args));

            if (lookup.isMoreSpecificThanOrEqualTo(nextPattern)) {
                Object object = nextPattern.getValue();

                return (object instanceof Executable)
                        ? ((Executable) object).execute(args)
                        : object;
            }
        }

        throw new DispatchException(
                "Executable.nextMethod::No more applicable patterns");
    }

    void setDispatcher(PatternDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    void setPattern(GraphPattern pattern) {
        this.pattern = pattern;
    }

    public String toString() {

        StringBuffer result = new StringBuffer("[args ");

        try {
            Class[] parameterTypes = getExecuteMethod().getParameterTypes();
            int count = parameterTypes.length;

            for (int i = 0; i < count; i++) {
                if (i > 0) {
                    result.append(", ");
                }

                result.append(parameterTypes[i].getName());
            }

            result.append("]");
        } catch (Exception e) {
        }

        return result.toString();
    }
}






