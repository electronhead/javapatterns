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

class InfinimumScalar extends Scalar {

    static final InfinimumScalar INF = new InfinimumScalar();
    private static final Infinimum value = new Infinimum();

    public boolean eq(OrderedDomain element) {
        return element == InfinimumScalar.INF;
    }

    public Infinimum getValue() {
        return value;
    }

    public boolean lt(OrderedDomain element) {
        return !eq(element);
    }

    public Object toObject() {
        return value;
    }

    public String toString() {
        return "INF";
    }
}






