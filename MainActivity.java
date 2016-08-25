package com.unpam.networkapplication; 
 
import java.io.InputStream; 
import java.net.URL; 
import java.util.ArrayList; 
import java.util.HashMap; 
import java.util.List; 
import java.util.Map; 
 
import org.apache.http.HttpResponse; 
import org.apache.http.HttpStatus; 
import org.apache.http.client.HttpClient; 
import org.apache.http.client.methods.HttpGet; 
import org.apache.http.impl.client.DefaultHttpClient; 
import org.apache.http.protocol.HTTP; 
import org.apache.http.util.EntityUtils; 
import org.json.JSONException; 
import org.simpleframework.xml.Serializer; 
import org.simpleframework.xml.core.Persister; 
 
import android.app.Activity; 
import android.app.ProgressDialog; 
import android.os.AsyncTask; 
import android.os.Bundle; 
import android.view.Gravity; 
import android.view.Menu; 
import android.view.View; 
import android.view.View.OnClickListener; 
import android.widget.ListView; 
import android.widget.SimpleAdapter; 
import android.widget.Toast;
import com.google.myjson.Gson; 
import com.unpam.jsonmodel.GResult; 
import com.unpam.model.Result; 
 
public class MainActivity extends Activity implements 
OnClickListener { 
  private String torestoXmlAddress = 
"http://toresto.com/restaurant.xml"; 
  private String torestoJsonAddress = 
"http://toresto.com/restaurant.json"; 
   
  private final String ID = "id"; 
  private final String NAMA = "nama"; 
  private final String ALAMAT = "alamat"; 
  private final String PHONE = "phone"; 
   
  List<Map<String, String>> items = new ArrayList<Map<String, 
String>>(); 
  ListView listView; 
 
  @Override 
  protected void onCreate(Bundle savedInstanceState) { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.activity_main); 
 
 
  findViewById(R.id.parseXmlButton).setOnClickListener(this); 
 
  findViewById(R.id.parseJsonButton).setOnClickListener(this); 
 
    listView = (ListView) findViewById(R.id.dataListView); 
  } 
 
  @Override 
  public boolean onCreateOptionsMenu(Menu menu) { 
    // Inflate the menu; this adds items to the action bar 
if it is present. 
    getMenuInflater().inflate(R.menu.activity_main, menu); 
    return true; 
  } 
 
  @Override 
  public void onClick(View v) { 
    // TODO Auto-generated method stub 
    switch (v.getId()) { 
    case R.id.parseXmlButton: 
      new Thread() { 
        public void run() { 
          parseSimpleXml(); 
        }; 
      }.start(); 
      break; 
    case R.id.parseJsonButton: 
      new MyJsonTask().execute(torestoJsonAddress); 
    }
	} 
 
  public void parseSimpleXml() { 
    try { 
      URL url = new URL(torestoXmlAddress); 
      InputStream is = url.openStream(); 
      Serializer serializer = new Persister(); 
      final Result restaurant = 
serializer.read(Result.class, is); 
 
      runOnUiThread(new Runnable() { 
 
        @Override 
        public void run() { 
          Toast toast = 
Toast.makeText(MainActivity.this, "Ada " + 
restaurant.getData().size() + " data",Toast.LENGTH_LONG); 
          toast.setGravity(Gravity.TOP | 
Gravity.RIGHT, 0, 0); 
          toast.show(); 
 
          items.clear(); 
          Map<String, String> map; 
          for (int i = 0; i < 
restaurant.getData().size(); i++) { 
            map = new HashMap<String, 
String>(); 
         
  map.put(ID,Integer.toString(restaurant.getData().get(i).getId
())); 
            map.put(NAMA, 
restaurant.getData().get(i).getName()); 
            map.put(ALAMAT, 
restaurant.getData().get(i).getAddress()); 
            map.put(PHONE, 
restaurant.getData().get(i).getPhone().getExt()+ " - " + 
restaurant.getData().get(i).getPhone().getText()); 
            items.add(map); 
          } 
 
          String[] from = new String[] { ID, 
NAMA, ALAMAT, PHONE }; 
          int[] to = new int[] { R.id.textView1, 
R.id.textView2,R.id.textView3, R.id.textView4 }; 
          SimpleAdapter adapter = new 
SimpleAdapter(MainActivity.this, items, 
R.layout.custom_list_4,from, to); 
          listView.clearChoices(); 
          listView.setAdapter(adapter); 
        } 
      }); 
 
    } catch (final Exception e) { 
      runOnUiThread(new Runnable() { 
 
        @Override
		public void run() { 
          Toast.makeText(MainActivity.this, 
e.getMessage(),Toast.LENGTH_LONG).show(); 
        } 
      }); 
    } 
  } 
 
  public String getResponseFromURL(String url) { 
    String response = null; 
    HttpClient httpclient = null; 
    try { 
      HttpGet httpget = new HttpGet(url); 
      httpclient = new DefaultHttpClient(); 
      HttpResponse httpResponse = 
httpclient.execute(httpget); 
 
      final int statusCode = 
httpResponse.getStatusLine().getStatusCode(); 
      if (statusCode != HttpStatus.SC_OK) { 
        throw new Exception("Got HTTP " + statusCode 
+ " (" + httpResponse.getStatusLine().getReasonPhrase() + ')'); 
      } 
 
      response = 
EntityUtils.toString(httpResponse.getEntity(),HTTP.UTF_8); 
 
    } catch (Exception e) { 
 
    } finally { 
      if (httpclient != null) { 
     
  httpclient.getConnectionManager().shutdown(); 
        httpclient = null; 
      } 
    } 
    return response; 
  } 
 
  public GResult parseGson(String url) throws JSONException { 
    String response = getResponseFromURL(url); 
 
    Gson gson = new Gson(); 
    GResult gResult = gson.fromJson(response, 
GResult.class); 
 
    gResult.getResult().getData().get(0).getName(); 
    return gResult; 
  } 
 
  class MyJsonTask extends AsyncTask<String, Void, GResult> { 
    ProgressDialog dialog; 
 
    @Override 
    protected void onPreExecute() { 
	dialog = ProgressDialog.show(MainActivity.this, 
null, "Loading"); 
    } 
 
    @Override 
    protected GResult doInBackground(String... params) { 
      try { 
        GResult gResult = parseGson(params[0]); 
        return gResult; 
      } catch (Exception e) { 
        return null; 
      } 
    } 
 
    @Override 
    protected void onPostExecute(GResult result) { 
      dialog.cancel(); 
      if (result != null) { 
        Toast.makeText(MainActivity.this,"Ada "+ 
result.getResult().getCount() + " 
data",Toast.LENGTH_SHORT).show(); 
 
        items.clear(); 
        Map<String, String> map; 
        for (int i = 0; i < 
result.getResult().getData().size(); i++) { 
          map = new HashMap<String, String>(); 
       
  map.put(ID,Integer.toString(result.getResult().getData().get(
i).getId())); 
          map.put(NAMA, 
result.getResult().getData().get(i).getName()); 
          map.put(ALAMAT, 
result.getResult().getData().get(i).getAddress());     
     
          items.add(map); 
        } 
 
        String[] from = new String[] { ID, NAMA, 
ALAMAT }; 
        int[] to = new int[] { R.id.textView1, 
R.id.textView2, R.id.textView3}; 
        SimpleAdapter adapter = new 
SimpleAdapter(MainActivity.this,items, R.layout.custom_list_3, 
from, to); 
        listView.setAdapter(adapter); 
      } else { 
        // munculkan pesan error 
        Toast.makeText(MainActivity.this,"Tidak 
dapat membaca data",Toast.LENGTH_SHORT).show(); 
      } 
    } 
  } 
}