package nvt.glucosemonitoring;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import org.w3c.dom.Text;

import java.io.IOException;


public class ResultScreen extends Activity {

    private TextView unitView;
    private TextView titleView;
    private TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_screen);

        resultView = (TextView) findViewById(R.id.result);
        unitView = (TextView) findViewById(R.id.units);
        titleView = (TextView) findViewById(R.id.readTitle);

        resultView.setVisibility(View.VISIBLE);
        unitView.setVisibility(View.VISIBLE);
        titleView.setVisibility(View.VISIBLE);

        displayResult();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result_screen, menu);
        return true;
    }

    public void displayResult(){
        //((Button) v).setText("Please Connect USB monitor");

        String str = null;
        int zIndex = -1;
        int cIndex = -1;
        int colon = 0;

        // Get UsbManager from Android.
        // MainActivity.manager = (UsbManager) getSystemService(Context.USB_SERVICE);

        //Toaster(MainActivity.manager + "");
        // Find the first available driver.
        //UsbSerialDriver driver = UsbSerialProber.acquire(MainActivity.manager);
        if (MainActivity.driver != null) {
            try {
                // driver.open();
                // driver.setBaudRate(9600);

                byte buffer[] = new byte[4096];
                while ((zIndex == -1) && (cIndex == -1)) {

                    int numBytesRead = MainActivity.driver.read(buffer, 1000);
                    str = new String(buffer, "UTF-8");

                    zIndex = str.indexOf("Z");
                    cIndex = str.indexOf(":");

                }

            } catch (IOException e) {
                Toaster("Exception");    // Deal with error.
            }


            MainActivity.resultLength = zIndex - cIndex + 2;
            str.getChars(cIndex + 2, zIndex, MainActivity.arr, 0);

            if (MainActivity.resultLength == 1)
                ((TextView) resultView).setText("" + MainActivity.arr[0]);//Final version should only be three digits
            else if (MainActivity.resultLength == 2)
                ((TextView) resultView).setText("" + MainActivity.arr[0] +  "" + MainActivity.arr[1]);
            else if (MainActivity.resultLength == 3)
                ((TextView) resultView).setText("" + MainActivity.arr[0] +  "" + MainActivity.arr[1] + "" + MainActivity.arr[2]);
            else
                ((TextView) resultView).setText("" + MainActivity.arr[0] +  "" + MainActivity.arr[1] + "" + MainActivity.arr[2] + "" + MainActivity.arr[3]);

        }
    }

    public void onButtonPress(View v) {
        startActivity(new Intent(ResultScreen.this, delay.class));
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


    public void Toaster(String string) {
        Toast.makeText(this, string, Toast.LENGTH_LONG).show();
    }
}