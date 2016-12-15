package com.xiaomi.xhome.util;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by ruijun on 3/22/16.
 */
public class Utils {

    public static final String TAG = "Utils";

    public static void unzip(InputStream is, String path) throws IOException {
        unzip(new ZipInputStream(new BufferedInputStream(is)), path);
    }

    public static void unzip(ZipInputStream zis, String path) throws IOException {
        if (!path.endsWith("/"))
            path += "/";

        String filename;
        ZipEntry ze;
        byte[] buffer = new byte[1024 * 10];
        int count;

        while ((ze = zis.getNextEntry()) != null) {
            filename = ze.getName();

            if (ze.isDirectory()) {
                File fmd = new File(path + filename);
                fmd.mkdirs();
                continue;
            }

            FileOutputStream fo = new FileOutputStream(path + filename);

            while ((count = zis.read(buffer)) != -1) {
                fo.write(buffer, 0, count);
            }

            fo.close();
            zis.closeEntry();
        }

        zis.close();
    }


    public static Element getXmlRoot(String path) {
        InputStream is = null;

        try {
            is = new FileInputStream(path);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(is);
            return doc.getDocumentElement();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } catch (OutOfMemoryError e) {
            Log.e(TAG, e.toString());
        } catch (ParserConfigurationException e) {
            Log.e(TAG, e.toString());
        } catch (SAXException e) {
            Log.e(TAG, e.toString());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }

        return null;
    }

    public static boolean delDir(File path) {
        delDirContent(path);
        return path.delete();
    }

    public static void delDirContent(File path) {
        if (path == null) {
            return;
        }

        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    delDir(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
    }

    public static String transSceneId(int id) {
        return "scene" + id;
    }

    public static int transSceneId(String id) {
        if (id.startsWith("scene")) {
            try {
                return Integer.parseInt(id.substring(5));
            } catch (NumberFormatException e) {
            }
        }
        return -1;
    }
}
