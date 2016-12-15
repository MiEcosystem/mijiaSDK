package com.xiaomi.xhome.mamlplugin;

import com.xiaomi.xhome.maml.ActionCommand;
import com.xiaomi.xhome.maml.data.Expression;
import com.xiaomi.xhome.maml.elements.ScreenElement;
import com.xiaomi.xhome.ui.SceneView;

import org.w3c.dom.Element;

/**
 * <DeviceControlCommand service="AirPurifier:BaseService:1" action="setMode" params="'auto'" paramTypes="Mode:string">
 * <p/>
 * </DeviceControlCommand>
 **/
public class SceneControlCommand extends ActionCommand {
    public static final String TAG_NAME = "SceneCommand";
    public static final String TAG = "SceneControlCommand";
    private final Expression mCommandExp;
    private final String mCommand;
    private SceneView mSceneView;

    public SceneControlCommand(ScreenElement screenElement, Element ele) {
        super(screenElement);

        mCommandExp = Expression.build(getVariables(), ele.getAttribute("commandExp"));
        mCommand = ele.getAttribute("command");
    }

    @Override
    public void init() {
        super.init();
        mSceneView = (SceneView) getVariables().get("__objView");
    }

    @Override
    public void finish() {
        super.finish();
        mSceneView = null;
    }

    @Override
    protected void doPerform() {
        String command = mCommandExp != null ? mCommandExp.evaluateStr() : mCommand;
        if ("run".equals(command)) {
            mSceneView.runScene();
        }else if ("enable".equals(command)) {
            mSceneView.enableScene(true);
        }else if ("disable".equals(command)) {
            mSceneView.enableScene(false);
        }
    }
}
