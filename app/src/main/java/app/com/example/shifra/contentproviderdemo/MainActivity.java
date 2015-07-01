package app.com.example.shifra.contentproviderdemo;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "ContentProviderDemo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,null,null,null);

        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            do{
                int contactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                int hasPhoneNumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                //if there is a phone number for this contact
                if (hasPhoneNumber > 0){
                    String [] projection = new String [] {ContactsContract.CommonDataKinds.Phone.NUMBER};
                    String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";
                    String [] selectionArgs = {contactID + ""};
                    Cursor phoneCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            projection, selection, selectionArgs,null);
                    if (phoneCursor != null && phoneCursor.getCount()> 0){
                        phoneCursor.moveToFirst();
                        do {
                            String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.
                                    CommonDataKinds.Phone.NUMBER));
                            Log.d(TAG, "Name:" + contactName + ", Phone Number" + phoneNumber);
                        }while (phoneCursor.moveToNext());
                    }
                    phoneCursor.close();
                }

            }while (cursor.moveToNext());

        }
        cursor.close();
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
}
