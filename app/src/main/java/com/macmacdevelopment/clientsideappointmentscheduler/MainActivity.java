package com.macmacdevelopment.clientsideappointmentscheduler;



import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends Activity
{

    Spinner spinner1;
     String result;
    Button buttonConnect, buttonClear;
    TextView textIn,textResponse;
    InputStream inputStream=null;
    Socket socket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonConnect = (Button) findViewById(R.id.connect);
        buttonClear = (Button) findViewById(R.id.clear);
        textIn = (TextView) findViewById(R.id.textin);
        textResponse = (TextView) findViewById(R.id.response);
        buttonClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                textResponse.setText("");
            }
        });

        spinner1 = (Spinner) findViewById(R.id.spinnerOfDays);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.day_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        addListenerOnSpinnerItemSelection();
        addListenerOnButton();
    }

    public void addListenerOnSpinnerItemSelection() {

        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }


    public void addListenerOnButton() {

        spinner1 = (Spinner) findViewById(R.id.spinnerOfDays);
        buttonConnect = (Button) findViewById(R.id.connect);
        buttonConnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MyClientTask myClientTask = new MyClientTask();
                myClientTask.execute();
            }
        });
    }

    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;
           String response = "";
            try {
                socket = new Socket("192.168.56.1", 9996);

                dataOutputStream = new DataOutputStream(
                        socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream.writeUTF(String.valueOf(spinner1.getSelectedItem()));
                response = dataInputStream.readUTF();
                textIn.setText(dataInputStream.readUTF());

                // setup for Output from computer server
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];
                int bytesRead;
                //InputStream from socket

                inputStream = socket.getInputStream();

                //inputStream.read() will block if no data return
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response += byteArrayOutputStream.toString("UTF-8");
                    textResponse.setText(response);
                }
                textResponse.setText(response);
            }catch(Exception e){
                e.printStackTrace();

            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {


            //textResponse.setText(response);
            super.onPostExecute(result);
        }
    }

}