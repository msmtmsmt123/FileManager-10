package com.bmeath.filemanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by bm on 15/04/17.
 */

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    private File[] currentDirList;
    private static String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    ArrayList contents;
    Boolean showHidden = false;
    FileAdapter fileAdapter;
    private ListView lView;
    File currentDir;
    String parent;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            for (int i = 0; i < permissions.length; i++)
            {
                int havePermission = ContextCompat.checkSelfPermission(this, permissions[i]);
                if (havePermission == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{permissions[i]}, 0);
                }
            }

        }

        lView = (ListView) findViewById(R.id.lView);

        // set title to path
        cd(Environment.getExternalStorageDirectory().getAbsolutePath());
        ls();
    }

    public void onItemClick(AdapterView<?> adapterView, View v, int position, long id)
    {
        File f = (File) fileAdapter.getItem(position);

        if (f.isDirectory())
        {
            if (f.getName().equals(".."))
            {
                cd(currentDir.getParent());
                ls();
            }
            else
            {
                cd(f.getAbsolutePath());
                ls();
            }
        }
    }

    private void cd(String newPath)
    {
        if (newPath.equals("../"))
        {
            currentDir = currentDir.getParentFile();
        }
        else
        {
            currentDir = new File(newPath);
        }
        parent = currentDir.getParent();
        setTitle(newPath);
    }

    private void ls()
    {
        // get names of current directory contents
        contents = new ArrayList();
        if (currentDir.canRead())
        {
            currentDirList = currentDir.listFiles();

            // convert string array to arraylist
            if (currentDirList != null) {
                if (showHidden) {
                    contents = new ArrayList(Arrays.asList(currentDirList));
                }
                else
                {
                    // exclude hidden items
                    for (int i = 0; i < currentDirList.length; i++)
                    {
                        if (!currentDirList[i].isHidden())
                        {
                            contents.add(currentDirList[i]);
                        }
                    }
                }
            }

            // sort alphabetically
            Collections.sort(contents);

            if (parent != null)
            {
                contents.add(0, new File("../"));
            }
        }
        else
        {
            setTitle(currentDir.getName() + " (unreachable)");
            contents.add(0, new File("../"));
        }

        // link file names to ListView using FileAdapter
        fileAdapter = new FileAdapter(this, contents, currentDir.getName());
        lView.setAdapter(fileAdapter);
        lView.setOnItemClickListener(MainActivity.this);
    }

    private boolean mkdir(String title)
    {
        return false;
    }

    private boolean mkFile()
    {
        return false;
    }

    private boolean rm(String path)
    {
        return false;
    }

    private boolean mv(String src, String dst)
    {
        return false;
    }

    private boolean cp(String src, String dst)
    {
        return false;
    }
}