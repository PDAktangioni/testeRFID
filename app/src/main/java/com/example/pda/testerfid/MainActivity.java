package com.example.pda.testerfid;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.view.annotation.event.OnKey;
import com.rscja.deviceapi.RFIDWithUHF;
import com.rscja.utility.StringUtility;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends FragmentActivity  {

    private  int countTag = 0;
    private boolean loopFlag = false;

    boolean flagStart = true;

    Button btTestar;
    TextView tvHelloworld;

    TextView tv9515;
    TextView tvc9f6;
    TextView tvd7da;
    TextView tvea22;

    public RFIDWithUHF mReader;

    private boolean tag9515 = true;
    private boolean tagc9f6 = true;
    private boolean tagd7da = true;
    private boolean tagea22 = true;

    private List<ListaRFID> listRFID = new ArrayList<ListaRFID>();

    Handler handler;

    ListView apps;
    ArrayList <String> checkedValue;
    Button bt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        try {
            mReader = RFIDWithUHF.getInstance();
        } catch (Exception ex) {

            Toast.makeText(this, ex.getMessage(),
                    Toast.LENGTH_SHORT).show();

            return;
        }

        if (mReader != null) {
            new InitTask().execute();
        }

//        btTestar = (Button) findViewById(R.id.btTestar);
//        btTestar.setOnClickListener(new BtInventoryClickListener());
//        tvHelloworld = (TextView)findViewById(R.id.HelloWorld);
//
//        tv9515 = (TextView)findViewById(R.id.tag_9515);
//        tvc9f6 = (TextView)findViewById(R.id.tag_c9f6);
//        tvd7da = (TextView)findViewById(R.id.tag_d7da);
//        tvea22 = (TextView)findViewById(R.id.tag_ea22);



        listRFID.add(new ListaRFID(1, "", "Almofada"));

        listRFID.add(new ListaRFID(1, "3000E2005104070B01690310EA22", "TV"));
        listRFID.add(new ListaRFID(1, "3000E2005104070B00990710C9F6", "Lustre"));

        listRFID.add(new ListaRFID(1, "", "Mesa de centro"));

        listRFID.add(new ListaRFID(1, "3000E2005104070B00990550D7DA", "Radio"));

        listRFID.add(new ListaRFID(1, "", "Tapete"));

        listRFID.add(new ListaRFID(1, "3000E2000016930A021313009515", "Vaso Reliquia"));

        bt1 = (Button) findViewById(R.id.button1);
        apps = (ListView) findViewById(R.id.listView1);



        ListaAdapter Adapter = new ListaAdapter(this,listRFID, new listRFID());
        apps.setAdapter(Adapter);
        apps.setOnItemClickListener(this);



    }

    @Override
    protected void onDestroy() {

        if (mReader != null) {
            mReader.free();
        }
        super.onDestroy();
    }

    public class InitTask extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog mypDialog;

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            return mReader.init();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            mypDialog.cancel();

            if (!result) {
                Toast.makeText(MainActivity.this, "init fail",
                        Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mypDialog = new ProgressDialog(MainActivity.this);
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage("init...");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();
        }

    }

    public boolean vailHexInput(String str) {

        if (str == null || str.length() == 0) {
            return false;
        }

        // O comprimento deve ser uniforme
        if (str.length() % 2 == 0) {
            return StringUtility.isHexNumberRex(str);
        }

        return false;
    }

    public class BtInventoryClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (MainActivity.this.mReader.startInventoryTag((byte) 0, (byte) 0)) {

                loopFlag = true;

                new TagThread(10).start();
            } else {
                MainActivity.this.mReader.stopInventory();
            }

            //run(true);
        }
    }

    /*public void run(boolean loopFlag) {
        String strTid;
        String strResult;

        String[] res = null;

        while (loopFlag) {

            // strUII = mContext.mReader.readUidFormBuffer();
            //
            // Log.i("UHFReadTagFragment", "TagThread uii=" + strUII);
            //
            // if (StringUtils.isNotEmpty(strUII)) {
            // strEPC = mContext.mReader.convertUiiToEPC(strUII);
            //
            // Message msg = handler.obtainMessage();
            // // Bundle bundle = new Bundle();
            // // bundle.putString("tagEPC", strEPC);
            //
            // // msg.setData(bundle);
            //
            // msg.obj = strEPC;
            // handler.sendMessage(msg);
            //
            // }


            res = this.mReader.readTagFormBuffer();

            if (res != null) {

                strTid = res[0];
                if (!strTid.equals("0000000000000000")&&!strTid.equals("000000000000000000000000")) {
                    strResult = "TID:" + strTid + "\n";
                } else {
                    strResult = "";
                }
                Message msg = handler.obtainMessage();
                msg.obj = strResult + "EPC:"
                        + this.mReader.convertUiiToEPC(res[1]) + "@"
                        + res[2];
                handler.sendMessage(msg);
            }
           *//* try {
                sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*//*

        }
    }*/

    public static void sleep(long time) throws InterruptedException {
        throw new RuntimeException("Stub!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    class TagThread extends Thread {

        private int mBetween = 80;
        HashMap<String, String> map;

        public TagThread(int iBetween) {
            mBetween = iBetween;
        }

        public void run() {
            String strTid;
            String strResult;

            String[] res = null;

            while (loopFlag) {

                // strUII = mContext.mReader.readUidFormBuffer();
                //
                // Log.i("UHFReadTagFragment", "TagThread uii=" + strUII);
                //
                // if (StringUtils.isNotEmpty(strUII)) {
                // strEPC = mContext.mReader.convertUiiToEPC(strUII);
                //
                // Message msg = handler.obtainMessage();
                // // Bundle bundle = new Bundle();
                // // bundle.putString("tagEPC", strEPC);
                //
                // // msg.setData(bundle);
                //
                // msg.obj = strEPC;
                // handler.sendMessage(msg);
                //
                // }

                res = MainActivity.this.mReader.readTagFormBuffer();

                if (res != null) {

                    strTid = res[0];
//                    if (!strTid.equals("0000000000000000")&&!strTid.equals("000000000000000000000000")) {
                        strResult = "TID:" + strTid + "\n";
                    /*} else {
                        strResult = "";
                    }*/
                    String teste;
                    String teste2;
                    String aux;

                    teste2 = StringUtility.isEmpty(res[1]) ? "" : (res[1] = res[1].replace("-", "").toUpperCase()).substring(4, res[1].length());
                    //teste = "EPC:" + teste2;

                    if (teste2.equals(tv9515.getText()) && tag9515) {
                        tag9515 = false;
                        countTag++;
                    }

                    else if (teste2.equals(tvc9f6.getText()) && tagc9f6) {
                        tag9515 = false;
                        countTag++;
                    }

                    else if (teste2.equals(tvd7da.getText()) && tagd7da) {
                        tag9515 = false;
                        countTag++;
                    }

                    else if (teste2.equals(tvea22.getText()) && tagea22) {
                        tag9515 = false;
                        countTag++;
                    }



                    if (countTag == 4){
                        loopFlag = false;
                    }



         /*           teste = strResult + "EPC:"
                            + MainActivity.this.mReader.convertUiiToEPC(res[1]) + "@"
                            + res[2];*/
                    //tvHelloworld.setText(tvHelloworld.getText() + " " + teste);

                }
                try {
                    sleep(mBetween);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }

    private void readTag() {


                    if (MainActivity.this.mReader.startInventoryTag((byte) 0, (byte) 0)) {
                        loopFlag = true;

                        new TagThread(10).start();
                    } else {
                        MainActivity.this.mReader.stopInventory();
                    }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 139){

            if (MainActivity.this.mReader.startInventoryTag((byte) 0, (byte) 0)) {

                loopFlag = true;

                //RFID_Scan();

                new TagThread(10).start();
            } else {
                MainActivity.this.mReader.stopInventory();
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    public void RFID_Scan() {
        String strTid;
        String strResult;

        String[] res = null;

        while (loopFlag) {

            res = MainActivity.this.mReader.readTagFormBuffer();

            if (res != null) {

                strTid = res[0];
                String teste;
                String teste2;
                String aux;

                teste2 = StringUtility.isEmpty(res[1]) ? "" : (res[1] = res[1].replace("-", "").toUpperCase()).substring(4, res[1].length());

                if (teste2.equals(tv9515.getText()) && tag9515) {
                    tag9515 = false;
                    tv9515.append(" OK");
                    tv9515.setTextColor(Color.GREEN);
                    countTag++;
                }

                else if (teste2.equals(tvc9f6.getText()) && tagc9f6) {
                    tagc9f6 = false;
                    tvc9f6.append(" OK");
                    tvc9f6.setTextColor(Color.GREEN);
                    countTag++;
                }

                else if (teste2.equals(tvd7da.getText()) && tagd7da) {
                    tagd7da = false;
                    tvd7da.append(" OK");
                    tvd7da.setTextColor(Color.GREEN);
                    countTag++;
                }

                else if (teste2.equals(tvea22.getText()) && tagea22) {
                    tagea22 = false;
                    tvea22.append(" OK");
                    tvea22.setTextColor(Color.GREEN);
                    countTag++;
                }

                if (countTag == 4){
                    loopFlag = false;
                }

            }
        }
    }
}
