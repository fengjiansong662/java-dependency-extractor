package fjs.jde.main;

// GraphViz.java - a simple API to call dot from Java programs

/*$Id$*/
/*
 ******************************************************************************
 *                                                                            *
 *              (c) Copyright 2003 Laszlo Szathmary                           *
 *                                                                            *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms of the GNU Lesser General Public License as published by   *
 * the Free Software Foundation; either version 2.1 of the License, or        *
 * (at your option) any later version.                                        *
 *                                                                            *
 * This program is distributed in the hope that it will be useful, but        *
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY *
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public    *
 * License for more details.                                                  *
 *                                                                            *
 * You should have received a copy of the GNU Lesser General Public License   *
 * along with this program; if not, write to the Free Software Foundation,    *
 * Inc., 675 Mass Ave, Cambridge, MA 02139, USA.                              *
 *                                                                            *
 ******************************************************************************
 */
import java.io.*;
import java.util.*;

/**
 * <dl>
 * <dt>Purpose: GraphViz Java API
 * <dd>
 *
 * <dt>Description:
 * <dd> With this Java class you can simply call dot
 *      from your Java programs
 * <dt>Example usage:
 * <dd>
 * <pre>
 *    GraphViz gv = new GraphViz();
 *    gv.addln(gv.start_graph());
 *    gv.addln("A -> B;");
 *    gv.addln("A -> C;");
 *    gv.addln(gv.end_graph());
 *    System.out.println(gv.getDotSource());
 *
 *    File out = new File("out.gif");
 *    gv.writeGraphToFile(gv.getGraph(gv.getDotSource()), out);
 * </pre>
 * </dd>
 *
 * </dl>
 *
 * @version v0.1, 2003/12/04 (Decembre)
 * @author  Laszlo Szathmary (<a href="szathml@delfin.unideb.hu">szathml@delfin.unideb.hu</a>)
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class GraphViz {
        private static String TEMP_DIR = "F:/graphviz-2.38";//Í¼Æ¬±£´æµÄÂ·¾¶
        //private static String DOT = "C:\\Program Files\\Graphviz2.30\\bin\\dot.exe";
        private static String DOT="F:\\graphviz-2.38\\release\\bin\\dot.exe";

        private StringBuilder graph3 = new StringBuilder();


        public GraphViz() {
        }


        public String getDotSource() {
                return graph3.toString();
        }


        public void add(String line) {
                graph3.append(line);
        }


        public void addln(String line) {
                graph3.append(line + "\n");
        }


        public void addln() {
                graph3.append('\n');
        }


        public byte[] getGraph(String dot_source, String type) {
                File dot;
                byte[] img_stream = null;
                try {
                        dot = writeDotSourceToFile(dot_source);
                        if (dot != null) {
                                img_stream = get_img_stream(dot, type);
                                if (dot.delete() == false)
                                        System.err.println("Warning: " + dot.getAbsolutePath()
                                                        + " could not be deleted!");
                                return img_stream;
                        }
                        return null;
                } catch (java.io.IOException ioe) {
                        return null;
                }
        }


        public int writeGraphToFile(byte[] img, String file) {
                File to = new File(file);
                return writeGraphToFile(img, to);
        }


        public int writeGraphToFile(byte[] img, File to) {
                try {
                        FileOutputStream fos = new FileOutputStream(to);
                        fos.write(img);
                        fos.close();
                } catch (java.io.IOException ioe) {
                        return -1;
                }
                return 1;
        }


        private byte[] get_img_stream(File dot, String type) {
                File img;
                byte[] img_stream = null;
                try {
                        img = File.createTempFile("graph_", "." + type, new File(
                                        GraphViz.TEMP_DIR));
                        Runtime rt = Runtime.getRuntime();
                        String[] args = { DOT, "-T" + type, dot.getAbsolutePath(), "-o",
                                        img.getAbsolutePath() };
                        Process p = rt.exec(args);
                        p.waitFor();
                        FileInputStream in = new FileInputStream(img.getAbsolutePath());
                        img_stream = new byte[in.available()];
                        in.read(img_stream);
                        if (in != null)
                                in.close();
                        if (img.delete() == false)
                                System.err.println("Warning: " + img.getAbsolutePath()
                                                + " could not be deleted!");
                } catch (java.io.IOException ioe) {
                        System.err
                                        .println("Error:    in I/O processing of tempfile in dir "
                                                        + GraphViz.TEMP_DIR + "\n");
                        System.err.println("       or in calling external command");
                        ioe.printStackTrace();
                } catch (java.lang.InterruptedException ie) {
                        System.err.println("Error: the execution of the external program was interrupted");
                        ie.printStackTrace();
                }
                return img_stream;
        }


        private File writeDotSourceToFile(String str) throws java.io.IOException {
                File temp;
                try {
                        temp = File.createTempFile("graph_", ".dot.tmp", new File(
                                        GraphViz.TEMP_DIR));
                        FileOutputStream fos=new FileOutputStream(temp.getAbsolutePath());
                        BufferedWriter br=new BufferedWriter(new OutputStreamWriter(fos,"UTF-8"));
                        br.write(str);
                        br.close();
                } catch (Exception e) {
                        System.err.println("Error: I/O error while writing the dot source to temp file!");
                        return null;
                }
                return temp;
        }


        public String start_graph() {
                return "digraph G {";
        }


        public String end_graph() {
                return "}";
        }


        public void readSource(String input) {
                StringBuilder sb = new StringBuilder();
                try {
                        FileInputStream fis = new FileInputStream(input);
                        DataInputStream dis = new DataInputStream(fis);
                        BufferedReader br = new BufferedReader(new InputStreamReader(dis));
                        String line;
                        while ((line = br.readLine()) != null) {
                                sb.append(line);
                        }
                        dis.close();
                } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage());
                }
                this.graph3 = sb;
        }
}
