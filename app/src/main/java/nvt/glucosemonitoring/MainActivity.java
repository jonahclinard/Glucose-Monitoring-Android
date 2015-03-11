package nvt.glucosemonitoring;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.hardware.usb.*;
import android.widget.Toast;
import android.content.Context;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {

    private Context viewer;
    private int result = 124;
    static int resultLength = 0;
    public static UsbManager manager;
    UsbDevice monitor;
    public static char arr[] = new char[4];
    private static String TAG = "MainActivity";

    public static UsbSerialDriver driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get UsbManager from Android.
        manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        driver = UsbSerialProber.acquire(manager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void buttonOnClick(View v) {
        Button button = (Button) v;
        ((Button) v).setText("Please Connect USB monitor");
        displayResult();
    }

    public void displayResult() {
        String str = null;
        int zIndex = -1;
        int cIndex = -1;
        int colon = 0;


        //Toaster(manager + "");
        // Find the first available driver.

        if (driver != null) {
                try {
                    driver.open();
                    driver.setBaudRate(9600);

                    byte buffer[] = new byte[4096];
                    while ((zIndex == -1) && (cIndex == -1)) {

                        int numBytesRead = driver.read(buffer, 1000);
                        str = new String(buffer, "UTF-8");

                        zIndex = str.indexOf("Z");
                        cIndex = str.indexOf(":");

                    }

                } catch (IOException e) {
                    Toaster("Exception");    // Deal with error.
                }



            resultLength = zIndex - cIndex + 2;
            str.getChars(cIndex + 2, zIndex, arr, 0);
          /*  try {
                driver.close();
            }
            catch (IOException e){
                //handled
            }
            */
            startActivity(new Intent(MainActivity.this, delay.class));
        }

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



    public void Toaster(String string){
        Toast.makeText(this, string, Toast.LENGTH_LONG).show();
    }
}
