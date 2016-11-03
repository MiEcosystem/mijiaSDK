package com.xiaomi.xhome.data;

import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.xiaomi.xhome.XConfig;
import com.xiaomi.xhome.XHomeApplication;
import com.xiaomi.xhome.maml.util.Utils;

import org.w3c.dom.Element;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import miot.api.device.AbstractDevice;

/**
 * Created by ruijun on 3/22/16.
 */
public class Dashboard {
    public static final String TAG = "Dashboard";
    public static final String MODEL_SCENE = "xhome.scene";
    public static final String MODEL_TIME = "xhome.time";
    private String mSpace;
    private String mTheme;
    private ArrayList<DeviceInfo> mDeviceList;
    private ModelThemeManager mModelThemeManager = new ModelThemeManager();
    private Map<String, ThemeDeviceConfig> mThemeConfig;

    private boolean mDirty;

    public Dashboard(String space) {
        setSpace(space);
    }

    public void setSpace(String space) {
        mSpace = space;
    }

    public String getSpace() {
        return mSpace;
    }

    public ModelThemeManager getModelThemeManager() {
        return mModelThemeManager;
    }


    public interface DeviceChangedListener {

        // newly added device
        boolean onDeviceAdd(DeviceInfo di);
    }

    private ArrayList<DeviceChangedListener> mListeners;

    private static class ThemeDeviceConfig {
        public String id;
        public String modelThemeManifest;
        // size in unit block
        public int x = -1;
        public int y = -1;

        public static ThemeDeviceConfig fromXml(Element xmlElement) {
            if (xmlElement == null) return null;

            ThemeDeviceConfig di = new ThemeDeviceConfig();
            di.id = xmlElement.getAttribute("id");
            di.modelThemeManifest = xmlElement.getAttribute("modelTheme");
            di.x = Utils.getAttrAsInt(xmlElement, "x", -1);
            di.y = Utils.getAttrAsInt(xmlElement, "y", -1);
            return di;
        }
    }

    public static class DeviceInfo {
        public String id;
        public String name;
        public String model;
        public ModelThemeManager.ModelThemeInfo modelTheme;
        public String modelThemeManifest;
        // size in unit block
        public int x = -1;
        public int y = -1;
        public AbstractDevice device;

        @Override
        public String toString() {
            return name + " " + id + " " + model;
        }

        public static DeviceInfo fromXml(Element xmlElement) {
            if (xmlElement == null) return null;
            DeviceInfo di = new DeviceInfo();
            di.id = xmlElement.getAttribute("id");
            di.name = xmlElement.getAttribute("name");
            di.model = xmlElement.getAttribute("model");
            di.modelThemeManifest = xmlElement.getAttribute("modelTheme");
            di.x = Utils.getAttrAsInt(xmlElement, "x", -1);
            di.y = Utils.getAttrAsInt(xmlElement, "y", -1);
            if (TextUtils.isEmpty(di.id) || TextUtils.isEmpty(di.name) || TextUtils.isEmpty(di.model))
                return null;
            return di;
        }

    }

    public boolean load() {
        mDeviceList = new ArrayList<DeviceInfo>();
        Element ele = com.xiaomi.xhome.util.Utils.getXmlRoot(XHomeApplication.getInstance().getDashboardConfigPath(mSpace));
        if (ele != null) {
            String theme = ele.getAttribute("theme");
            mTheme = XConfig.DEFAULT_THEME;
            if (!TextUtils.isEmpty(theme)) {
                mTheme = theme;
            }
            Element devicesEle = Utils.getChild(ele, "Devices");
            if (devicesEle != null) {
                Utils.traverseXmlElementChildren(devicesEle, "Device", new Utils.XmlTraverseListener() {
                    @Override
                    public void onChild(Element child) {
                        DeviceInfo di = DeviceInfo.fromXml(child);
                        Log.d(TAG, "load device: " + di.toString());
                        if (di != null) {
                            addDeviceImp(di);
                        } else {
                            Log.e(TAG, "invalid device info" + child.toString());
                        }
                    }
                });
            }
        }
        loadThemeInfo();
        return true;
    }

    private void loadThemeInfo() {
        if (!mModelThemeManager.load(getThemePath())) {
            Log.e(TAG, "fail to load theme: " + mTheme);
            mTheme = XConfig.DEFAULT_THEME;
            mModelThemeManager.load(getThemePath());
        }

        // if not default theme, need to load theme config
        /*
          <Theme>
            <Devices>
                <Device/>
            </Devices>
          </Theme>
         */
//        if (!mTheme.equals(XConfig.DEFAULT_THEME)) {
        //load theme config
        String path = XHomeApplication.getInstance().getDashboardThemeConfigPath(mSpace, mTheme);
        Element ele = com.xiaomi.xhome.util.Utils.getXmlRoot(path);
        mThemeConfig = null;
        if (ele != null) {
            mThemeConfig = new HashMap<String, ThemeDeviceConfig>();
            Element devicesEle = Utils.getChild(ele, "Devices");
            if (devicesEle != null) {
                Utils.traverseXmlElementChildren(devicesEle, "Device", new Utils.XmlTraverseListener() {
                    @Override
                    public void onChild(Element child) {
                        ThemeDeviceConfig di = ThemeDeviceConfig.fromXml(child);
                        Log.d(TAG, "load theme device: " + di.toString());
                        mThemeConfig.put(di.id, di);
                    }
                });
            }
        }
//        }

        updateModelTheme();
    }

    //
    public String getModelThemePath(String modelPack) {
        return getThemePath() + "/" + modelPack;
    }

    private void updateModelTheme() {
        for (DeviceInfo di : mDeviceList) {
            boolean hasConfig = false;
            if (mThemeConfig != null) {
                ThemeDeviceConfig td = mThemeConfig.get(di.id);
                if (td != null) {
                    di.x = td.x;
                    di.y = td.y;
                    di.modelThemeManifest = td.modelThemeManifest;
                    hasConfig = true;
                }
            }

            boolean isDefaultTheme = mTheme.equals(XConfig.DEFAULT_THEME);
            if (!isDefaultTheme && !hasConfig) {
                di.x = -1;
                di.y = -1;
                di.modelThemeManifest = null;
            }

            di.modelTheme = mModelThemeManager.findModelInfo(di.model, di.modelThemeManifest);
        }
    }

    public void setDirty() {
        mDirty = true;
    }

    // get theme path
    public String getThemePath() {
        return XHomeApplication.getInstance().getThemePath(mTheme);
    }

    public String getTheme() {
        return mTheme;
    }

    public void setTheme(String theme) {
        mTheme = theme;
        setDirty();
    }

    public boolean save() {
        if (!mDirty)
            return false;
        Log.d(TAG, "saving dashboard config");

        saveBoard();

        // save theme config, if not default theme
        saveThemeConfig();

        mDirty = false;
        return true;
    }

    public boolean saveBoard() {
        return saveBoard(mTheme);
    }

    public boolean saveBoard(String theme) {
        try {
            if (!TextUtils.isEmpty(mSpace)) {
                XHomeApplication.getInstance().ensurePath(XHomeApplication.getInstance().getDashboardSpacesPath());
            }
            String configPath = XHomeApplication.getInstance().getDashboardConfigPath(mSpace);
            String copyPath = configPath + ".copy";
            File file = new File(copyPath);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, null);
            serializer.startTag(null, "Dashboard");
            serializer.attribute(null, "theme", theme);
            serializer.startTag(null, "Devices");
            for (DeviceInfo di : mDeviceList) {
                serializer.startTag(null, "Device");
                serializer.attribute(null, "id", di.id);
                serializer.attribute(null, "name", di.name);
                serializer.attribute(null, "model", di.model);
//
//                if (theme.equals(XConfig.DEFAULT_THEME)) {
//                    serializer.attribute(null, "modelTheme", di.modelTheme.manifest);
//                    serializer.attribute(null, "x", String.valueOf(di.x));
//                    serializer.attribute(null, "y", String.valueOf(di.y));
//                }
                serializer.endTag(null, "Device");
            }
            serializer.endTag(null, "Devices");
            serializer.endTag(null, "Dashboard");
            serializer.endDocument();
            serializer.flush();
            fos.close();

            File configFile = new File(configPath);
            configFile.delete();
            file.renameTo(configFile);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return true;
        }
        return false;
    }

    public void saveThemeConfig() {
        saveThemeConfig(mTheme);
    }

    private void saveThemeConfig(String theme) {
        try {
            String configPath = XHomeApplication.getInstance().getDashboardThemeConfigPath(mSpace, theme);
            String copyPath = configPath + ".copy";
            File file = new File(copyPath);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, null);
            serializer.startTag(null, "Theme");
            serializer.startTag(null, "Devices");
            for (DeviceInfo di : mDeviceList) {
                serializer.startTag(null, "Device");
                serializer.attribute(null, "id", di.id);
                serializer.attribute(null, "modelTheme", di.modelTheme.manifest);
                serializer.attribute(null, "x", String.valueOf(di.x));
                serializer.attribute(null, "y", String.valueOf(di.y));
                serializer.endTag(null, "Device");
            }
            serializer.endTag(null, "Devices");
            serializer.endTag(null, "Theme");
            serializer.endDocument();
            serializer.flush();
            fos.close();

            File configFile = new File(configPath);
            configFile.delete();
            file.renameTo(configFile);
        } catch (IOException e) {
        }
    }

    public void registerDeviceChangedListener(DeviceChangedListener l) {
        if (mListeners == null) {
            mListeners = new ArrayList<DeviceChangedListener>();
        }
        synchronized (mListeners) {
            mListeners.add(l);
        }
    }

    public void unregisterDeviceChangedListener(DeviceChangedListener l) {
        if (mListeners == null) {
            return;
        }
        synchronized (mListeners) {
            mListeners.remove(l);
        }
    }

    private void addDevice(DeviceInfo dev) {
        addDeviceImp(dev);
        if (mListeners != null) {
            for (DeviceChangedListener l : mListeners) {
                l.onDeviceAdd(dev);
            }
        }
        save();
    }

    // called by loading function internally, load device from config
    private void addDeviceImp(DeviceInfo dev) {
        mDeviceList.add(dev);
        Log.d(TAG, "Add device: " + dev.toString());
    }

    public void removeDevice(DeviceInfo di) {
        mDeviceList.remove(di);
        Log.d(TAG, "Remove device: " + di.toString());
        save();
    }

    public DeviceInfo findDevice(String deviceId) {
        for (DeviceInfo di : mDeviceList) {
            if (di.id.equals(deviceId))
                return di;
        }
        return null;
    }

    // add device from discovery result
    public boolean addDevice(AbstractDevice device) {
        if (findDevice(device.getDeviceId()) != null) {
            Log.d(TAG, "device exists, id: " + device.getDeviceId());
            return false;
        }

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.device = device;
        deviceInfo.name = device.getName();
        deviceInfo.id = device.getDeviceId();
        deviceInfo.model = device.getDeviceModel();
        deviceInfo.modelTheme = mModelThemeManager.findModelInfo(deviceInfo.model, null);
        if (deviceInfo.modelTheme == null) {
            Log.e(TAG, "fail to find device model info, " + deviceInfo.model);
            return false;
        }
        addDevice(deviceInfo);
        mDirty = true;
        return true;
    }

    public boolean addScene(int sceneId, String name) {
        if (findDevice(com.xiaomi.xhome.util.Utils.transSceneId(sceneId)) != null) {
            Log.d(TAG, "scene exists, id: " + sceneId);
            return false;
        }

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.name = name;
        deviceInfo.id = com.xiaomi.xhome.util.Utils.transSceneId(sceneId);
        deviceInfo.model = MODEL_SCENE;
        deviceInfo.modelTheme = mModelThemeManager.findModelInfo(deviceInfo.model, null);
        if (deviceInfo.modelTheme == null) {
            Log.e(TAG, "fail to find device model info, " + deviceInfo.model);
            return false;
        }
        addDevice(deviceInfo);
        mDirty = true;
        return true;
    }

    public ArrayList<DeviceInfo> getDeviceList() {
        return mDeviceList;
    }
}
