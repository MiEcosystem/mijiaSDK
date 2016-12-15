package com.xiaomi.xhome.data;

import android.text.TextUtils;
import android.util.Log;

import com.xiaomi.xhome.util.Utils;

import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruijun on 3/23/16.
 */
public class ModelThemeManager {
    public static final String TAG = "ModelThemeManager";
    // the default maml manifest file
    public static final String DEF_MANIFEST_NAME = "manifest.xml";
    public static final String THEME_INFO_FILE_NAME = "manifest.xml";
    public static final int DEF_BLOCK_SIZE = 270;
    private Map<String, ModelThemeInfo> mModels;
    private int mBlockSize;
    private boolean mLongClick;
    private int mPadding;

    public int getPadding() {
        return mPadding;
    }

    public static class ModelThemeInfo {
        // model name
        public String name;
        // panel size, in block
        public int w;
        public int h;
        // description
        public String desc;
        // the theme package
        public String pack;
        // the manifest path
        public String manifest;
        // strings base path
        public String strings;

        public ModelThemeInfo next;

        @Override
        public String toString() {
            return "Model: " + name + " " + w + "," + h;
        }
    }

    // load from a theme folder
    public boolean load(String path) {
        Log.d(TAG, "loading... " + path);

        mModels = new HashMap<String, ModelThemeInfo>();
        String filepath = path + "/" + THEME_INFO_FILE_NAME;
        Element root = Utils.getXmlRoot(filepath);
        if (root == null) {
            Log.e(TAG, "no xml root found in file: " + filepath);
            return false;
        }

        mBlockSize = com.xiaomi.xhome.maml.util.Utils.getAttrAsInt(root,"block",DEF_BLOCK_SIZE);
        mPadding = com.xiaomi.xhome.maml.util.Utils.getAttrAsInt(root,"padding",0);

        mLongClick = Boolean.parseBoolean(root.getAttribute("longClick"));

        com.xiaomi.xhome.maml.util.Utils.traverseXmlElementChildren(root, "Model", new com.xiaomi.xhome.maml.util.Utils.XmlTraverseListener() {
            @Override
            public void onChild(Element child) {
                ModelThemeInfo model = new ModelThemeInfo();
                model.name = child.getAttribute("name");
                model.desc = child.getAttribute("desc");
                String pack = child.getAttribute("pack");
                if (!TextUtils.isEmpty(pack)) {
                    model.pack = pack;
                } else {
                    model.pack = model.name;
                }
                String manifest = child.getAttribute("manifest");
                if (!TextUtils.isEmpty(manifest)) {
                    model.manifest = manifest;
                } else {
                    model.manifest = DEF_MANIFEST_NAME;
                }
                // strings folder base path,  [path]/strings/...
                String strings = child.getAttribute("strings");
                if (!TextUtils.isEmpty(strings)) {
                    model.strings = strings;
                }

                model.w = com.xiaomi.xhome.maml.util.Utils.getAttrAsInt(child, "w", 0);
                model.h = com.xiaomi.xhome.maml.util.Utils.getAttrAsInt(child, "h", 0);
                Log.d(TAG, "load model: " + model.toString());
                ModelThemeInfo head = mModels.get(model.name);
                if (head == null) {
                    mModels.put(model.name, model);
                } else {
                    while (head.next != null) {
                        head = head.next;
                    }
                    head.next = model;
                }
            }
        });

        return true;
    }

    public int getBlockSize() {
        return mBlockSize;
    }

    public boolean supportLongClick() {
        return mLongClick;
    }

    // ind: index of modeltheme in the link list
    public ModelThemeInfo findModelInfo(String name, int ind) {
        ModelThemeInfo head = mModels != null ? mModels.get(name) : null;
        if (head == null || ind <= 0)
            return head;

        int i = 0;
        while (head.next != null) {
            if (ind == i)
                return head;
            head = head.next;
            i++;
        }

        // could not find the index specified, return head
        return head;
    }

    public ModelThemeInfo findModelInfo(String name, String modelTheme) {
        ModelThemeInfo head = mModels != null ? mModels.get(name) : null;
        if (head == null || TextUtils.isEmpty(modelTheme))
            return head;
        ModelThemeInfo head1 = head;
        while (head1 != null) {
            if (TextUtils.equals(head1.manifest, modelTheme))
                return head1;
            head1 = head1.next;
        }
        // could not find specified modeltheme manifest, just return first one
        return head;
    }
}
