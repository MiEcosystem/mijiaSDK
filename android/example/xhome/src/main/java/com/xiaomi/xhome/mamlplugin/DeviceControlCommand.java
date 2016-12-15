package com.xiaomi.xhome.mamlplugin;

import android.text.TextUtils;
import android.util.Log;

import com.xiaomi.xhome.maml.ActionCommand;
import com.xiaomi.xhome.maml.data.Expression;
import com.xiaomi.xhome.maml.data.Variables;
import com.xiaomi.xhome.maml.elements.ScreenElement;
import com.xiaomi.xhome.maml.util.ReflectionHelper;
import com.xiaomi.xhome.ui.DeviceView;

import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * <DeviceControlCommand service="AirPurifier:BaseService:1" action="setMode" params="'auto'" paramTypes="Mode:string">
 * <p/>
 * </DeviceControlCommand>
 **/
public class DeviceControlCommand extends ActionCommand {
    public static final String TAG_NAME = "DeviceControlCommand";
    public static final String TAG = "DeviceControlCommand";
    private final String mServiceName;
    private final String mActionName;
    private final Expression mServiceNameExp;
    private final Expression mActionNameExp;
    private ParamsHelper mParamsHelper;
    private DeviceView mDeviceView;

    public DeviceControlCommand(ScreenElement screenElement, Element ele) {
        super(screenElement);

        mServiceNameExp = Expression.build(getVariables(), ele.getAttribute("serviceExp"));
        mServiceName = ele.getAttribute("service");
        mActionNameExp = Expression.build(getVariables(), ele.getAttribute("actionExp"));
        mActionName = ele.getAttribute("action");

        try {
            mParamsHelper = new ParamsHelper(ele.getAttribute("paramTypes"), ele.getAttribute("params"), getVariables());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

    }

    public static class ParamsHelper {
        private final Variables mVariables;

        public static class ParamInfo {
            public String name;
            public Class<?> type;
            public ObjVar objVar;
            public Expression exp;
        }

        private Map<String, ParamInfo> mParams;
        private Map<String, Object> mParamValues;

        public ParamsHelper(String types, String paramValues, Variables vars) throws ClassNotFoundException, IllegalArgumentException {
            mParams = new HashMap<String, ParamInfo>();

            mVariables = vars;
            // if params contains object array (obj[#index]), note there must not any ',' in the expression
            Expression[] exps = Expression.buildMultiple(mVariables, paramValues);
            if (exps != null && !TextUtils.isEmpty(types)) {

                String[] paramTypesArr = TextUtils.split(types, ",");
                if (paramTypesArr.length != exps.length) {
                    throw new IllegalArgumentException("types and params length not match. " + types + " " + paramValues);
                }

                for (int i = 0; i < paramTypesArr.length; i++) {
                    String[] items = paramTypesArr[i].split(":");
                    if (items.length != 2) {
                        throw new IllegalArgumentException("invalid param name and type format: " + paramTypesArr[i]);
                    }

                    ParamInfo info = new ParamInfo();
                    info.name = items[0];
                    info.type = ReflectionHelper.strTypeToClass(items[1]);
                    info.exp = exps[i];
                    mParams.put(info.name, info);
                    Log.d(TAG, "Param: " + info.name + " " + info.type);
                }
            }
        }

        public void init() {
            for (ParamInfo info : mParams.values()) {
                ObjVar ov = null;
                Class<?> c = info.type;
                if (c.isPrimitive() && !c.isArray() || c == String.class) {
                    continue;
                }
                Expression exp = info.exp;
                if (exp != null) {
                    String name = exp.evaluateStr();
                    if (!TextUtils.isEmpty(name)) {
                        ov = new ObjVar(name, mVariables);
                    }
                }
                info.objVar = ov;
            }

        }

        public void finish() {
            mParamValues = null;
        }

        public Map<String, Object> getParamValues() {
            return mParamValues;
        }

        public void prepareParams() {
            if (mParams != null) {
                if (mParamValues == null) {
                    mParamValues = new HashMap<String, Object>();
                }
                for (ParamInfo info : mParams.values()) {
                    Object value;
                    Class<?> paraClass = info.type;
                    Expression expression = info.exp;
                    if (expression == null)
                        continue;
                    if (paraClass == String.class) {
                        value = expression.evaluateStr();
                    } else if (paraClass == int.class) {
                        value = (int) (long) expression.evaluate();
                    } else if (paraClass == long.class) {
                        value = (long) expression.evaluate();
                    } else if (paraClass == boolean.class) {
                        value = expression.evaluate() > 0 ? true : false;
                    } else if (paraClass == double.class) {
                        value = expression.evaluate();
                    } else if (paraClass == float.class) {
                        value = (float) expression.evaluate();
                    } else if (paraClass == byte.class) {
                        value = (byte) (long) expression.evaluate();
                    } else if (paraClass == short.class) {
                        value = (short) (long) expression.evaluate();
                    } else if (paraClass == char.class) {
                        value = (char) (long) expression.evaluate();
                    } else {
                        value = info.objVar != null ? info.objVar.get() : null;
                    }
                    mParamValues.put(info.name, value);
                    Log.d(TAG, "prepareParams: " + info.name + " " + value);
                }
            }
        }
    }

    @Override
    public void init() {
        super.init();
        mDeviceView = (DeviceView) getVariables().get("__objView");
        if (mParamsHelper != null) {
            mParamsHelper.init();
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (mParamsHelper != null)
            mParamsHelper.finish();
        mDeviceView = null;
    }

    @Override
    protected void doPerform() {
        if (mParamsHelper != null) {
            mParamsHelper.prepareParams();
        }
        mDeviceView.invokeAction(
                mServiceNameExp != null ? mServiceNameExp.evaluateStr() : mServiceName,
                mActionNameExp != null ? mActionNameExp.evaluateStr() : mActionName,
                mParamsHelper != null ? mParamsHelper.getParamValues() : null);
    }
}
