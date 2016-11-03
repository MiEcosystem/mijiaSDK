package com.xiaomi.xhome.mamlplugin;

import com.xiaomi.xhome.maml.ActionCommand;
import com.xiaomi.xhome.maml.ObjectFactory;
import com.xiaomi.xhome.maml.elements.ScreenElement;

import org.w3c.dom.Element;

/**
 * Created by ruijun on 3/21/16.
 */
public class XHomeActionCommandFactory extends ObjectFactory.ActionCommandFactory {
    @Override
    protected ActionCommand doCreate(ScreenElement screenElement, Element ele) {
        String tag = ele.getNodeName();
        if (tag.equals(DeviceControlCommand.TAG_NAME)) {
            return new DeviceControlCommand(screenElement, ele);
        }else if (tag.equals(SceneControlCommand.TAG_NAME)) {
            return new SceneControlCommand(screenElement, ele);
        }

        return null;
    }}
