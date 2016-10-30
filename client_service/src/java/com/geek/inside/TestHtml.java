/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geek.inside;

import java.util.regex.Pattern;
public class TestHtml {
    // adapted from post by Phil Haack and modified to match better
    public final static String tagStart=
        "\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)\\>";
    public final static String tagEnd=
        "\\</\\w+\\>";
    public final static String tagSelfClosing=
        "\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)/\\>";
    public final static String htmlEntity=
        "&[a-zA-Z][a-zA-Z0-9]+;";
    public final static String orphelin = "(<area|<br|<hr|<img|<input|<link|<meta|<param)((.[^>])*)>"; 
    public final static Pattern htmlPattern=Pattern.compile(
      "("+tagStart+".*"+tagEnd+")|("+tagSelfClosing+")|("+htmlEntity+")|("+orphelin+")",
      Pattern.DOTALL
    );
    
    /**
     * Will return true if s contains HTML markup tags or entities.
     *<area /> , <br /> , <hr /> , <img /> , <input /> , <link /> , <meta /> , <param /> 
     * @param s String to test
     * @return true if string contains HTML
     */
    public static boolean isHtml(String s) {
        boolean ret=false;
        if (s != null) {
            ret=htmlPattern.matcher(s).find();
        }
        return ret;
    }

}

