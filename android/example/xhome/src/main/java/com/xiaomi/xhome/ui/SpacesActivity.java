package com.xiaomi.xhome.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.mistatistic.sdk.MiStatInterface;
import com.xiaomi.xhome.R;
import com.xiaomi.xhome.XHomeApplication;
import com.xiaomi.xhome.maml.util.FilenameExtFilter;

import java.io.File;
import java.util.ArrayList;

public class SpacesActivity extends Activity {
    private static String TAG = SpacesActivity.class.getSimpleName();
    private ListView mList;

    private SpaceListAdapter mSpacesAdapter = new SpaceListAdapter();
    private ArrayList<String> mSpaces = new ArrayList<String>();
    private int mCurrentSelection;
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spacelist);

//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mList = (ListView) findViewById(R.id.list);
        mList.setAdapter(mSpacesAdapter);
        mList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                mCurrentSelection = position;
            }
        });


        setupButtons();

        mSpaces.add(getString(R.string.default_space));
        File spacesDir = new File(XHomeApplication.getInstance().getDashboardSpacesPath());
        if (spacesDir.exists()) {
            String[] exts = {"xml"};
            String[] files = spacesDir.list(new FilenameExtFilter(exts));
            if (files != null) {
                for (String file : files) {
                    mSpaces.add(ripFileNameExt(file));
                }
                mSpacesAdapter.notifyDataSetChanged();
            }
        }
        setTitle(R.string.space_activity);

        ////////////////////////////////////////////////////
        MiStatInterface.recordCountEvent("UI", "space activity launch");
    }

    private void setupButtons() {
        Button btn = (Button) findViewById(R.id.btn_new);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = new EditText(SpacesActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);

                new AlertDialog.Builder(SpacesActivity.this).setView(input).
                        setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = input.getText().toString();
                                if (!mSpaces.contains(name)) {
//                                    mSpaces.add(name);
//                                    mSpacesAdapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                    XHomeApplication.getInstance().switchSpace(name);
                                    finish();
                                }
                            }
                        }).setNegativeButton(R.string.button_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).setCancelable(true).setTitle(R.string.space_new).show();
            }
        });

        btn = (Button) findViewById(R.id.btn_delete);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentSelection < 0 || mCurrentSelection >= mSpaces.size()) {
                    return;
                }
                final String space = mCurrentSelection == 0 ? null : mSpaces.get(mCurrentSelection);
                if (TextUtils.equals(space, XHomeApplication.getInstance().getDashboard().getSpace())) {
                    Toast.makeText(SpacesActivity.this, R.string.cannot_delete_current_space, Toast.LENGTH_SHORT).show();
                    return;
                }
                final String message = getString(R.string.are_u_sure_delete) + "\n" + mSpaces.get(mCurrentSelection);
                new AlertDialog.Builder(SpacesActivity.this).setMessage(message).
                        setPositiveButton(R.string.button_ok,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        File sp = new File(XHomeApplication.getInstance().getDashboardConfigPath(space));
                                        if (sp.exists())
                                            sp.delete();
                                        if (mCurrentSelection > 0) {
                                            mSpaces.remove(mCurrentSelection);
                                            mSpacesAdapter.notifyDataSetChanged();
                                        }
                                        mCurrentSelection = -1;
                                        mList.clearFocus();
                                        mList.clearChoices();
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton(R.string.button_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).setCancelable(true).setTitle(R.string.space_delete).show();
            }
        });
        btn = (Button) findViewById(R.id.btn_switchto);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentSelection < 0 || mCurrentSelection >= mSpaces.size()) {
                    return;
                }
                final String space = mCurrentSelection == 0 ? null : mSpaces.get(mCurrentSelection);

                AlertDialog.Builder builder = new AlertDialog.Builder(SpacesActivity.this);
                final AlertDialog dialog = builder.setTitle(R.string.switching_space).setCancelable(false).show();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        XHomeApplication.getInstance().switchSpace(space);
                        Toast.makeText(SpacesActivity.this, R.string.switch_space_ok, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, 50);
            }
        });
    }

    private String ripFileNameExt(String fileName) {
        if (fileName == null) {
            return null;
        }
        int ind = fileName.lastIndexOf('.');
        if (ind == -1)
            return fileName;
        return fileName.substring(0, ind);
    }

    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }


    public class SpaceListAdapter extends BaseAdapter {
        public SpaceListAdapter() {
        }

        @Override
        public int getCount() {
            return mSpaces.size();
        }

        @Override
        public Object getItem(int index) {
            return mSpaces.get(index);
        }

        @Override
        public long getItemId(int index) {
            return index;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = LayoutInflater.from(SpacesActivity.this).inflate(R.layout.spacelist_item, null);
            }
            String space = mSpaces.get(position);
            String curSpace = XHomeApplication.getInstance().getDashboard().getSpace();
            if (position == 0 && TextUtils.isEmpty(curSpace) || TextUtils.equals(space, curSpace)) {
                space += "  [*]";
            }
            TextView temp = (TextView) view.findViewById(R.id.name);
            temp.setText(space);
            return view;
        }
    }

}
