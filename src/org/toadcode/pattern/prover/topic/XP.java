package org.toadcode.pattern.prover.topic;

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


public class XP implements Serializable, Prover {

    private final static Rule ARGTYPE_RULE = new LikeRule("#");

    public final static XP x = new XP();

    /**
     * Insert the method's description here.
     * Creation date: (1/25/2000 9:38:44 PM)
     */
    public Class getRuleClass() {
        return TopicRule.class;
    }

    /**
     * Insert the method's description here.
     * Creation date: (1/25/2000 9:37:36 PM)
     */
    public Rule getRuleUsingArgType(Class aClass) { // always true
        return ARGTYPE_RULE;
    }

    /**
     * Insert the method's description here.
     * Creation date: (1/25/2000 9:46:23 PM)
     * @return boolean
     * @param object java.lang.Object

     XP.match ("abc.*").or (XP.match ("xyz.#"))
     */
    public static LikeRule match(Object object) {

        return (object instanceof String)
                ? new LikeRule((String) object)
                : null;
    }

    /**
     * Insert the method's description here.
     * Creation date: (1/25/2000 9:39:54 PM)
     */
    public InstanceRule toInstanceRule(Object object) {
        return XP.match(object);
    }
}






