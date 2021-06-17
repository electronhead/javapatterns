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

import java.io.*;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


public class Utility {

    public static String addPrefixToName(String prefix, String name) {

        StringBuffer nameBuffer = new StringBuffer(prefix.length()
                + name.length());

        nameBuffer.append(prefix);
        nameBuffer.append(Character.toUpperCase(name.charAt(0)));
        nameBuffer.append(name.substring(1));

        return nameBuffer.toString();
    }

    public static Object bytesToObject(byte[] bytes) throws UtilityException {

        try {
            ByteArrayInputStream s1 = new ByteArrayInputStream(bytes);
            ObjectInputStream s2 = new ObjectInputStream(s1);
            Object object = s2.readObject();

            s2.close();

            return object;
        } catch (Exception e) {
            throw new UtilityException("Utility.bytesToObject EXCEPTION -- "
                    + e.getMessage());
        }
    }

    // jar file
    public static String[] classNamesFromNameArray(String[] names) {

        ArrayList classNames = new ArrayList();
        String name, suffix;
        int index;

        for (int i = 0; i < names.length; i++) {
            name = names[i];
            index = name.lastIndexOf('.');
            suffix = name.substring(index + 1);

            if (suffix.equals("class")) {
                classNames.add(name.substring(0, index));
            }
        }

        String[] result = new String[classNames.size()];

        classNames.toArray(result);

        return result;
    }

    public static String convertArrayToString(Object[] objects) {

        StringBuffer buffer = new StringBuffer();
        int count = objects.length;

        for (int i = 0; i < count; i++) {
            buffer.append("[" + objects[i] + "]");
        }

        return buffer.toString();
    }

    // date and time stuff
    public static Object[] dateSqlDateTime() {

        java.util.Date date = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        GregorianCalendar calendar = new GregorianCalendar();

        calendar.setTime(date);

        String sqlTime = calendar.get(Calendar.HOUR) + ":"
                + calendar.get(Calendar.MINUTE) + ":"
                + calendar.get(Calendar.SECOND);

        return new Object[]{date, sqlDate, sqlTime};
    }

    public static Object[] dateTime() {

        java.util.Date date = new java.util.Date();
        GregorianCalendar calendar = new GregorianCalendar();

        calendar.setTime(date);

        String time = calendar.get(Calendar.HOUR) + ":"
                + calendar.get(Calendar.MINUTE) + ":"
                + calendar.get(Calendar.SECOND);

        return new Object[]{date, time};
    }

    public static String duplicateSlashes(String pathString) {

        StringBuffer pathBuffer = new StringBuffer(pathString);
        int start = pathBuffer.length() - 1;
        int i;

        for (i = start; i >= 0; i--) {
            if (pathBuffer.charAt(i) == '\\') {
                pathBuffer.insert(i, '\\');
            }
        }

        return pathBuffer.toString();
    }

    public static Method findDeclaredMethod(Class clas, String methodName)
            throws Exception {

        Method[] methods = clas.getDeclaredMethods();
        Method method = null;
        int count = methods.length;

        for (int i = 0; i < count; i++) {
            if (methods[i].getName().equals(methodName)) {
                method = methods[i];

                break;
            }
        }

        if (method == null) {
            throw new Exception("Utility.findDeclaredMethod: can't find <"
                    + methodName + ">");
        }

        return method;
    }

    public static Method findNamedMethod(Class clas, String methodName)
            throws Exception {

        Method method;
        Method[] methods = clas.getMethods();
        int count = methods.length;

        for (int i = 0; i < count; i++) {
            method = methods[i];

            if (method.getName().equals(methodName)) {

                //&& 0 == method.getParameterTypes ().length)
                return method;
            }
        }

        throw new Exception("Utility.findNamedMethod: can't find <"
                + methodName + ">");
    }

    public static String insertBackslashes(String aString) {

        StringBuffer buffer = new StringBuffer();
        int count = aString.length();
        char c;

        for (int i = 0; i < count; i++) {
            c = aString.charAt(i);

            if (c == '"') {
                buffer.append('\\');
            }

            buffer.append(c);
        }

        return buffer.toString();
    }

    public static ArrayList intersection(ArrayList v1, Object[] v2) {

        ArrayList result = new ArrayList();
        int i = v2.length;

        while (i-- > 0) {
            if (v1.contains(v2[i])) {
                result.add(v2[i]);
            }
        }

        return result;
    }

    public static ArrayList intersection(ArrayList v1, ArrayList v2) {

        ArrayList result = new ArrayList();
        Iterator e = v1.iterator();

        while (e.hasNext()) {
            Object element = e.next();

            if (v2.contains(element)) {
                result.add(element);
            }
        }

        return result;
    }

    public static void main(String[] args)
            throws IOException, ClassNotFoundException {

        String[] names = classNamesFromNameArray(

                //namesFromZipFile ("d:\\ehs\\jdk1.1\\lib\\place.jar"));
                namesFromJarURL("jar:http://209.180.166.18/place.jar!/"));

        for (int i = 0; i < names.length; i++) {
            System.out.println("i = " + i + "; name = " + names[i]);
        }

        URL url =
                new URL("jar:http://209.180.166.18/place.jar!/");    //loadClass
        URLClassLoader loader = new URLClassLoader(new URL[]{url});
        Class c = loader.loadClass(names[0]);

        //JarLoader loader = new JarLoader (new URL [] {url});
        //Class c = loader.getClass (names [0]);
        System.out.println("class c = " + c);
    }

    public static boolean multipleMatches(String source, String[] targets) {

        String target;
        int sourceIndex;

        for (int i = 0; i < targets.length; i++) {
            target = targets[i];
            sourceIndex = source.indexOf(target, 0);

            if (sourceIndex < 0) {
                return false;
            }
        }

        return true;
    }

    public static String[] namesFromJarURL(String urlString)
            throws IOException {

        URL url = new URL(urlString);
        JarURLConnection connection = (JarURLConnection) url.openConnection();
        JarFile jar = connection.getJarFile();

        return namesFromZipFile(jar);
    }

    public static String[] namesFromZipFile(String filePath)
            throws IOException {

        FileInputStream inputStream = null;
        String[] result = null;

        try {
            inputStream = new FileInputStream(filePath);

            ZipInputStream zipStream = new ZipInputStream(inputStream);

            result = namesFromZipInputStream(zipStream);
        } catch (Exception e) {
            System.out.println("Utility.namesFromZipFile -- OOPS!");
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                System.out.println("Utility.namesFromZipFile -- OOPS! OOPS!");
            }
        }

        return result;
    }

    public static String[] namesFromZipFile(ZipFile zipFile)
            throws IOException {

        String[] result = null;
        ArrayList names = new ArrayList();
        ZipEntry entry;
        Enumeration entries = zipFile.entries();

        while (entries.hasMoreElements()) {
            entry = (ZipEntry) entries.nextElement();

            if (entry.isDirectory()) {
                continue;
            } else {
                names.add(entry.getName());
            }
        }

        result = new String[names.size()];

        names.toArray(result);

        return result;
    }

    public static String[] namesFromZipInputStream(ZipInputStream zipStream)
            throws IOException {

        String[] result = null;
        ArrayList names = new ArrayList();
        ZipEntry entry;

        while ((entry = zipStream.getNextEntry()) != null) {
            if (entry.isDirectory()) {
                continue;
            } else {
                names.add(entry.getName());
            }
        }

        result = new String[names.size()];

        names.toArray(result);

        return result;
    }

    /*
    public static void main (String [] args) {
            String source = "ge:  [Solid][SOLID ODBC Driver][SOLID Server]SOLID";
            String [] targets = new String [] {
                    "SOLID", "river"
            };
            System.out.println (
                    source.regionMatches (true, 0, targets [0], 0, targets [0].length ())
            );
            System.out.println (
                    multipleMatches (source, targets)
            );
    }
    */
    public static byte[] objectToBytes(Object object)
            throws UtilityException {

        try {
            ByteArrayOutputStream s1 = new ByteArrayOutputStream();
            ObjectOutputStream s2 = new ObjectOutputStream(s1);

            s2.writeObject(object);

            byte[] bytes = s1.toByteArray();

            s2.close();

            return bytes;
        } catch (Exception e) {
            throw new UtilityException("Utility.objectToBytes EXCEPTION -- "
                    + e.getMessage());
        }
    }

    /** Return { directory path, file name, file extension } */
    public static String[] parsePath(File file) {

        String[] result = new String[3];
        String directoryPath = file.getParent();

        if (directoryPath == null) {
            result[0] = "";
        } else {
            result[0] = directoryPath + "\\";
        }

        String filename = file.getName();
        int dotIndex = filename.indexOf(".");

        if (dotIndex == -1) {
            result[1] = "";
            result[2] = "";
        } else if (dotIndex == filename.length()) {
            result[1] = filename;
            result[2] = "";
        } else {
            result[1] = filename.substring(0, dotIndex);
            result[2] = filename.substring(dotIndex + 1);
        }

        return result;
    }

    public static void print(PrintStream stream, Object object,
                             String methodName, String message) {

        stream.print(object.getClass().getName());
        stream.print('.');
        stream.print(methodName);
        stream.print("::");
        stream.println(message);
        stream.println("===================================");
    }

    /** Print to Writer */
    public static void print(PrintStream stream, Object obj1,
                             String methodName, String message, Object obj2) {

        stream.print(obj1.getClass().getName());
        stream.print('.');
        stream.print(methodName);
        stream.print("::");
        stream.println(message);
        stream.println("---");
        stream.println(obj2);
        stream.println("===================================");
    }

    public static ArrayList union(ArrayList v1, Object[] v2) {

        ArrayList result = (ArrayList) v1.clone();
        int i = v2.length;

        while (i-- > 0) {
            if (!result.contains(v2[i])) {
                result.add(v2[i]);
            }
        }

        return result;
    }

    public static ArrayList union(ArrayList v1, ArrayList v2) {

        ArrayList result = (ArrayList) v1.clone();
        Iterator e = v2.iterator();

        while (e.hasNext()) {
            Object element = e.next();

            if (!result.contains(element)) {
                result.add(element);
            }
        }

        return result;
    }
}






