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

import java.util.Date;


class DateScalar extends Scalar {

    Date value;

    public DateScalar(Date value) {
        this.value = value;
    }

    public boolean eq(OrderedDomain element) {

        if (element instanceof DateScalar) {
            return getValue().getTime()
                    == ((DateScalar) element).getValue().getTime();
        } else {
            return false;
        }
    }

    public Date getValue() {
        return value;
    }

    public boolean lt(OrderedDomain element) {

        if (element instanceof DateScalar) {
            return getValue().getTime()
                    < ((DateScalar) element).getValue().getTime();
        }

        if (element == InfinimumScalar.INF) {
            return false;
        }

        if (element == SupremumScalar.SUP) {
            return true;
        } else {
            return false;
        }
    }

    public Object toObject() {
        return getValue();
    }
}






