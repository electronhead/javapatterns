package org.toadcode.util;

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

/**
 * Insert the type's description here.
 * Creation date: (12/12/1999 8:32:25 PM)
 * @author:
 */
public class FixedQueue {

    private Object[] data;
    private int start = 0;
    private int end = 0;
    private int count;

    /**
     * Insert the method's description here.
     * Creation date: (12/12/1999 8:34:40 PM)
     * @param param int
     */
    public FixedQueue(int count) {
        this.count = count;
        data = new Object[count];
    }

    /**
     * Insert the method's description here.
     * Creation date: (12/12/1999 9:17:41 PM)
     */
    public void clear() {

        start = 0;
        end = 0;

        java.util.Arrays.fill(data, null);    // remove references to stored objects
    }

    /**
     * Insert the method's description here.
     * Creation date: (12/12/1999 9:13:58 PM)
     * @return int
     */
    public int size() {
        return end - start;
    }

    /**
     * Insert the method's description here.
     * Creation date: (12/12/1999 8:39:35 PM)
     */
    public Object take() throws UtilityException {

        if (start >= end) {
            throw new UtilityException("Empty FixedQueue");
        } else {
            int index = start % count;
            Object result = data[index];

            data[index] = null;

            start++;

            return result;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (12/12/1999 8:37:48 PM)
     * @param element java.lang.Object
     */
    public void write(Object element) throws UtilityException {

        if (end - start < count) {
            data[end++ % count] = element;
        } else {
            throw new UtilityException("FixedQueue overflow");
        }
    }
}






