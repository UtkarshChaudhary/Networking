package com.example.lenovo.networking;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
public class CoursesAsynTask extends AsyncTask<String,Void,ArrayList<Course>> {//ArrayList<Course> is result type
    //String is input type
    //void is current status type void means we donot want to return status
    OnDownloadCompleteListener mListener;
    void setOnDownloadCompleteListener(OnDownloadCompleteListener listener) {
        mListener = listener;
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */

    @Override
    protected ArrayList<Course> doInBackground(String... params) {
        String urlString = params[0];
        try {
            URL url = new URL(urlString);//this func. throws exception of MalformedURLException type
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();//this func. throws exception of IOException type
            urlConnection.setRequestMethod("GET"); //"GET" is used to fetch data
            urlConnection.connect();//to start connecting
            InputStream inputStream = urlConnection.getInputStream(); //to fetch data

            Scanner s = new Scanner(inputStream); //to input data from inputStream
            String str = "";
            while (s.hasNext()) {//hasNext() means while it has some data
                str += s.nextLine();
            }
            Log.i("TAG", str);
            return parseCourses(str);

        } catch (MalformedURLException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private ArrayList<Course> parseCourses(String str) {
        try {
            JSONObject coursesJson = new JSONObject(str);
            JSONObject data = coursesJson.getJSONObject("data");//if we want to retrive jason object value corresponding to key data
            JSONArray courseJsonArray = data.getJSONArray("courses");//if we want to retrive jason object Array value corresponding to key courses
            ArrayList<Course> courseList = new ArrayList<>();

            for (int i = 0; i < courseJsonArray.length(); i++) {
                JSONObject courseJson = (JSONObject) courseJsonArray.get(i);
                int id = courseJson.getInt("id");
                String name = courseJson.getString("name");
                String title = courseJson.getString("title");
                String overview = courseJson.getString("overview");
                Course c = new Course(id, title, name, overview);
                courseList.add(c);

            }
            return courseList;
        } catch (JSONException e) {

        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Course> courses) { //onPostExecute works on main thread
        super.onPostExecute(courses);
        if (mListener != null) {
            mListener.onDownloadComplete(courses);
        }
    }
}
    interface OnDownloadCompleteListener{
        void onDownloadComplete(ArrayList<Course> courseList);
    }

