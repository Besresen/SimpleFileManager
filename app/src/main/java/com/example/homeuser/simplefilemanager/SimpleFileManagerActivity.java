package com.example.homeuser.simplefilemanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.text.DateFormat;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuLayout;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;


public class SimpleFileManagerActivity extends Activity {

    private static final int REQUEST_PATH = 1;

    private SwipeMenuListView mListView;

    String curFileName;

    private File currentDir;
    private FileArrayAdapter adapter;
    private FileArrayAdapter adapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_file_manager);

        createExternalStoragePrivateFile();

        mListView = (SwipeMenuListView) findViewById(R.id.list);

		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
                // create "copy" item
                SwipeMenuItem copyItem = new SwipeMenuItem(getApplicationContext());
                copyItem.setBackground(new ColorDrawable(Color.rgb(0x26, 0xB2, 0xF0)));
                copyItem.setTitle("Copy");
                copyItem.setTitleSize(16);
                copyItem.setTitleColor(Color.WHITE);
                copyItem.setWidth(dp2px(90));
                menu.addMenuItem(copyItem);

				// create "open" item
				SwipeMenuItem renameItem = new SwipeMenuItem(getApplicationContext());
				renameItem.setBackground(new ColorDrawable(Color.rgb(0x26, 0xB2, 0xF0)));
				renameItem.setTitle("Rename");
				renameItem.setTitleSize(16);
				renameItem.setTitleColor(Color.WHITE);
                renameItem.setWidth(dp2px(90));
                menu.addMenuItem(renameItem);

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xFF, 0x00, 0x00)));
                deleteItem.setTitle("Delete");
                deleteItem.setTitleSize(16);
                deleteItem.setTitleColor(Color.WHITE);
				deleteItem.setWidth(dp2px(90));
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		mListView.setMenuCreator(creator);

		// step 2. listener item click event
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				//ApplicationInfo item = mAppList.get(position);
				//Item item = adapter.getItem(position);
				switch (index) {
				case 0:
                    // copy
                    Toast.makeText(getApplicationContext(), position + " copy", 0).show();
                    break;
				case 1:
                    // rename
                    renameFile(position);
                    break;
                case 2:
                    // delete
                    //delete(item);
                    //mAppList.remove(position);
                    //mAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), position + " delete", 0).show();
                    break;
				}

				return false;
			}
		});
		
		// set SwipeListener
		mListView.setOnSwipeListener(new OnSwipeListener() {
			
			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}
			
			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});

		// other setting
//		listView.setCloseInterpolator(new BounceInterpolator());

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                Toast.makeText(getApplicationContext(), position + " click", 0).show();

                Item o = adapter.getItem(position);
                if(o.getPath()!="/sdcard") {
                    if (o.getImage().equalsIgnoreCase("directory_icon") || o.getImage().equalsIgnoreCase("directory_up")) {
                        TextView tm =  (TextView)findViewById(R.id.titleManager);
                        /*String str = o.getPath();
                        if(str.compareTo("/")!=0) {
                            tm.setText(o.getPath() + "/");
                        }
                        else {
                            tm.setText(o.getPath());
                        }*/
                        tm.setText(o.getPath());
                        currentDir = new File(o.getPath());
                        fill(currentDir);
                    } else {
                        onFileClick(o);
                    }
                }
            }
        });

		// test item long click
		mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getApplicationContext(), position + " long click", 0).show();
				return false;
			}
		});
		
        currentDir = new File("/sdcard");
        fill(currentDir);
    }

    public void getfile(View view){
        currentDir = new File("/");
        fill(currentDir);
    }

	void createExternalStoragePrivateFile() {
		File wallpaperDirectory = new File("/sdcard/");
        File file = new File(wallpaperDirectory, "DemoFile.jpg");
		try {
			InputStream is = getResources().openRawResource(R.drawable.directory_up);
			OutputStream os = new FileOutputStream(file);
			byte[] data = new byte[is.available()];
			is.read(data);
			os.write(data);
			is.close();
			os.close();
		} catch (IOException e) {
			Log.w("ExternalStorage", "Error writing " + file, e);
		}

        File wallpaperDirectory2 = new File("/sdcard/");
        File file2 = new File(wallpaperDirectory2, "DemoFile1.jpg");
        try {
            InputStream is = getResources().openRawResource(R.drawable.directory_icon);
            OutputStream os = new FileOutputStream(file2);
            byte[] data = new byte[is.available()];
            is.read(data);
            os.write(data);
            is.close();
            os.close();
        } catch (IOException e) {
            Log.w("ExternalStorage", "Error writing " + file2, e);
        }
	}

    private void createFile(String Text){

        File fileName = null;
        String sdState = android.os.Environment.getExternalStorageState();
        if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
            File sdDir = android.os.Environment.getExternalStorageDirectory();
            fileName = new File(sdDir, "primer.txt");
        } else {
            fileName = this.getCacheDir();
        }
        if (!fileName.exists())
            fileName.mkdirs();
        try {
            FileWriter f = new FileWriter(fileName);
            f.write("hello world");
            f.flush();
            f.close();
            Toast.makeText(getApplicationContext(), "true", 0).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "false", 0).show();
        }
    }


    private void renameFile(int position){

        final Item o = adapter.getItem(position);
        final File dir = new File(o.getParentPath()+"/");
        final File myFile = new File(dir, o.getName());
        final Context context = this;

        //boolean deleted = myFile.delete();

        String filename = o.getName();

        AlertDialog.Builder fileDialog = new AlertDialog.Builder(this);
        fileDialog.setTitle("Rename file");
        final EditText input = new EditText(this);
        input.setText(filename);
        fileDialog.setView(input);
        fileDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        final String fileName = input.getText().toString();
                        final File newFile = new File(dir, fileName);

                        if(newFile.exists()) {

                            AlertDialog.Builder existsDialog = new AlertDialog.Builder(context);
                            existsDialog.setTitle("Exists file");
                            TextView tv = new TextView(context);
                            tv.setGravity(Gravity.CENTER);
                            tv.setTextSize(16);
                            tv.setTextColor(Color.rgb(0x09, 0x09, 0x09));
                            tv.setText("\nFile: '" + fileName + "' - exists. \nReplace?");
                            existsDialog.setView(tv);

                            existsDialog.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {

                                            Toast.makeText(getApplicationContext(), "replace", 0).show();

                                            boolean success = myFile.renameTo(newFile);

                                            if(success == true) {
                                                o.setName(fileName);
                                                Item delItem = searchFileInAdapter(fileName);
                                                if(delItem != null) {
                                                    adapter.remove(delItem);
                                                }
                                                adapter.notifyDataSetChanged();
                                            }

                                        }
                                    });
                            existsDialog.setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {

                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog alertExistsDialog = existsDialog.create();
                            alertExistsDialog.show();
                        }
                        else {
                            boolean success = myFile.renameTo(newFile);

                            if(success == true) {
                                o.setName(fileName);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        //Toast.makeText(getApplicationContext(), fileName + " rename: "+Boolean.toString(success), 0).show();
                    }
                });
        fileDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                        dialog.dismiss();
                    }
                });
        AlertDialog alertFileDialog = fileDialog.create();
        alertFileDialog.show();
    }


    private Item searchFileInAdapter(String name) {

        String str;
        name = name.toLowerCase();

        if(name.length() > 0 && adapter.getCount() > 0) {

            for (int i = 0; i < adapter.getCount(); i++) {

                str = adapter.getItem(i).getName().toLowerCase();

                if (str.compareTo(name) == 0) {
                    return adapter.getItem(i);
                }
            }
        }

        return null;
    }

	private void open(Item o) {
		// open app

        Toast.makeText(getApplicationContext(), o.getName() + " click", 0).show();

        if(o.getPath()!=null) {
            if (o.getImage().equalsIgnoreCase("directory_icon") || o.getImage().equalsIgnoreCase("directory_up")) {
                TextView tm =  (TextView)findViewById(R.id.titleManager);
                String str = o.getPath();
                if(str.compareTo("/")!=0) {
                    tm.setText(o.getPath() + "/");
                }
                else {
                    tm.setText(o.getPath());
                }
                currentDir = new File(o.getPath());
                fill(currentDir);
            } else {
                onFileClick(o);
            }
        }
	}
	
    private void fill(File f)
    {
        File[]dirs = f.listFiles();
        this.setTitle("Current Dir: "+f.getName());
        List<Item>dir = new ArrayList<Item>();
        List<Item>fls = new ArrayList<Item>();
        long fsizeByte = 0;
        double fsizeConvert = 0;
        String fsize = "";

        try{
            for(File ff: dirs)
            {
                Date lastModDate = new Date(ff.lastModified());
                DateFormat formater = DateFormat.getDateTimeInstance();
                String date_modify = formater.format(lastModDate);

                if(ff.isDirectory()){

                    File[] fbuf = ff.listFiles();
                    int buf = 0;
                    if(fbuf != null){
                        buf = fbuf.length;
                    }
                    else buf = 0;
                    String num_item = String.valueOf(buf);
                    if(buf == 0) num_item = num_item + " item";
                    else num_item = num_item + " items";

                    //String formated = lastModDate.toString();
                    dir.add(new Item(ff.getName(),num_item,date_modify,ff.getAbsolutePath(), ff.getParent(), "dir","directory_icon"));
                }
                else
                {
                    fsizeByte = ff.length();

                    if(fsizeByte >= 1048576){
                        fsizeConvert = (double) fsizeByte/1048576;
                        //fsizeConvert = BigDecimal.valueOf(fsizeConvert).setScale(0, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                        fsize = Math.round(fsizeConvert) + " mb";
                    } else if(fsizeByte >= 1024){
                        fsizeConvert = (double) fsizeByte/1024;
                        //fsizeConvert = BigDecimal.valueOf(fsizeConvert).setScale(0, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                        fsize = Math.round(fsizeConvert) + " kb";
                    }
                    else {
                        fsize = fsizeByte + " b";
                    }

                    fls.add(new Item(ff.getName(), fsize, date_modify, ff.getAbsolutePath(), ff.getParent(), "file", "file_icon"));
                }
            }
        }
        catch(Exception e)
        {

        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);

        String ppp = f.getPath();

        //if(!f.getName().equalsIgnoreCase("sdcard"))
        //if(f.getPath()!="/sdcard" && f.getName()!=null && f.getPath()!=null && f.getParent()!=null)
        if (!f.getPath().equalsIgnoreCase("/sdcard"))
            dir.add(0,new Item("..","","",f.getParent(),f.getParent(), "dir","directory_icon"));
        adapter = new FileArrayAdapter(this,R.layout.file_view,dir);
        //this.setListAdapter(adapter);

        //adapter2 = new FileArrayAdapter(this, R.layout.file_view, dir);

        mListView.setAdapter(adapter);
    }

    //@Override
    protected void onListItemClick(SwipeMenuListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        //super.onListItemClick(l, v, position, id);
        Item o = adapter.getItem(position);
        if(o.getPath()!=null) {
            if (o.getImage().equalsIgnoreCase("directory_icon") || o.getImage().equalsIgnoreCase("directory_up")) {
                TextView tm =  (TextView)findViewById(R.id.titleManager);
                String str = o.getPath();
                if(str.compareTo("/")!=0) {
                    tm.setText(o.getPath() + "/");
                }
                else {
                    tm.setText(o.getPath());
                }
                currentDir = new File(o.getPath());
                fill(currentDir);
            } else {
                onFileClick(o);
            }
        }
    }

    private void onFileClick(Item o)
    {
        //Toast.makeText(this, "Folder Clicked: "+ currentDir, Toast.LENGTH_SHORT).show();
        /*Intent intent = new Intent();
        intent.putExtra("GetPath",currentDir.toString());
        intent.putExtra("GetFileName",o.getName());
        setResult(RESULT_OK, intent);
        finish();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_simple_file_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
