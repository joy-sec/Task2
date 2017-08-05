package sust.hackathon.helper.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceActivity;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import sust.hackathon.helper.R;
import sust.hackathon.helper.database.MyDBFunction;
import sust.hackathon.helper.model.DataTemp;

import static android.app.Activity.RESULT_OK;

/**
 * Created by joy on 8/5/17.
 */

public class HomeFragment extends Fragment {

    private int PICK_IMAGE_REQUEST=1;
    private String filePath;
    private Bitmap bitmap;
    ImageView imageView;
    Button button,button2,btn_camera,submit;
    File file;
    int flag;
    private String strToken;
    private EditText editText,etTitle;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.fragment_home,container,false);
        imageView= (ImageView) view.findViewById(R.id.image);
        button= (Button) view.findViewById(R.id.button);
        button2= (Button) view.findViewById(R.id.button2);
        editText=(EditText)view.findViewById(R.id.editText);
        btn_camera= (Button) view.findViewById(R.id.camera_btn);
        etTitle=(EditText) view.findViewById(R.id.etTitle);
        submit=(Button)view.findViewById(R.id.btnSubmit);
        //progress dialog
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading........");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataTemp dataTemp=new DataTemp();

                    dataTemp.setDetails(editText.getText().toString());
                    dataTemp.setName(etTitle.getText().toString());

                    MyDBFunction myDBFunction=new MyDBFunction(getContext());

                    myDBFunction.addingData(dataTemp);
                    etTitle.setText("  ");
                    editText.setText("   ");



            }
        });
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag == 0) {

                    // -- code for taking photo --

                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i, 99);

                } else if(flag == 1) {

                    //--- code for saving photo ---

                    savePhotoToMySdCard(bitmap);

                    Toast.makeText(getContext(), "Photo saved to sd card!", Toast.LENGTH_SHORT).show();

                    flag = 0;
                    btn_camera.setText("Take Photo");

                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getToken();
            }
        });

        return view;
    }
    private void showFileChooser(){


        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);

        /*
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

        */
    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri u = data.getData();

            filePath = getRealPathFromURI(u,getContext());

            System.out.println("PATH    :::::::::::"+filePath);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), u);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if(requestCode == 99 && resultCode == RESULT_OK && data != null){

            bitmap = (Bitmap) data.getExtras().get("data");

            imageView.setImageBitmap(bitmap);

            flag = 1;
            btn_camera.setText("Save Photo");

        }
    }

    private void getToken(){
        progressDialog.show();

        String url ="http://113.11.120.208/upload";

        String h="/storage/emulated/0/DCIM/016.jpg";

        File myFile = new File(filePath);

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        try {
            params.put("sampleFile", myFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        client.setTimeout(500000);
        client.post(url, params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String res) {
                        System.out.println(res);

                        strToken=res;
                        getData();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        System.out.println("ERROR RES  "+res);
                        progressDialog.cancel();
                    }
                }
        );
    }

    public static String getRealPathFromURI(Uri contentUri, Context activity) {
        String path = null;
        try {
            final String[] proj = {MediaStore.MediaColumns.DATA};
            final Cursor cursor = ((Activity) activity).managedQuery(contentUri, proj, null, null, null);
            final int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
        } catch (Exception e) {
        }
        if (path != null && path.length() > 0) {
            return path;
        } else return contentUri.getPath();
    }
    private void savePhotoToMySdCard(Bitmap bit){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String pname = sdf.format(new Date());


        String root = Environment.getExternalStorageDirectory().toString();
        File folder = new File(root+"/SCC_Photos");
        folder.mkdirs();

        File my_file = new File(folder, pname+".png");

        try {

            FileOutputStream stream = new FileOutputStream(my_file);
            bit.compress(Bitmap.CompressFormat.PNG, 80, stream);
            stream.flush();
            stream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getData(){

        String url="http://113.11.120.208/do_ocr?src=";
        AsyncHttpClient client = new AsyncHttpClient();

        client.setTimeout(50000);

        client.get(url+strToken, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                //Toast.makeText(MainActivity.this,response.getString("response"),Toast.LENGTH_LONG).show();

                //System.out.println(response);
                progressDialog.cancel();

                try {
                    editText.setText(response.getString("response"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {

progressDialog.cancel();
            }
        });

    }

public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
