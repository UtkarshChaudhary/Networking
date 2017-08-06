package com.example.lenovo.networking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
public class MainActivity extends AppCompatActivity implements OnDownloadCompleteListener{
    ArrayList<Course> courses;
    ListView courseListView;
    ArrayList<String> courseNames;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        courseListView=(ListView)findViewById(R.id.courseListView);
        courses = new ArrayList<>();
        courseNames=new ArrayList<>();
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,courseNames);
        courseListView.setAdapter(adapter);
        fetchCourses();
    }
    private void fetchCourses() {
        String urlString = "https://codingninjas.in/api/v1/courses";
        CoursesAsynTask coursesAsynTask=new CoursesAsynTask();
        coursesAsynTask.execute(urlString);//execute func. starts another thread
        coursesAsynTask.setOnDownloadCompleteListener(this);
    }
  public  void onDownloadComplete(ArrayList<Course> courseList){
      courses.clear();
      courses.addAll(courseList);
      for(int i=0;i<courses.size();i++){
          courseNames.add(courses.get(i).name);
      }
      adapter.notifyDataSetChanged();
  }
}