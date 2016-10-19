package uk.soton.sz.bitapi;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class TestApi {
public static void main(String[] args) {
	
	TestApi api = new TestApi();
	
	String key="6f8bba3a-e578-4344-a127-08da35599a42";
	
	try {
		//System.out.println(api.sendRequest(key,"food","http://www.2sfg.com/globalassets/corporate/home-page/hero-home-6-rice-desktop.jpg"));
		//System.out.println(api.sendRequest(key,"food","http://www.2sfg.com/globalassets/corporate/home-page/hero-home-6-rice-desktop.jpg"));
		//System.out.println(api.sendRequest(key,"food","http://www.2sfg.com/globalassets/corporate/home-page/hero-home-6-rice-desktop.jpg"));
		//System.out.println(api.sendRequest(key,"food","http://www.2sfg.com/globalassets/corporate/home-page/hero-home-6-rice-desktop.jpg"));
		System.out.println(api.sendRequest(key,"food","https://www.daysoftheyear.com/wp-content/images/hot-dog-day2-e1437733838777-808x380.jpg"));
		System.out.println(api.sendRequest(key,"concept","https://www.daysoftheyear.com/wp-content/images/hot-dog-day2-e1437733838777-808x380.jpg"));

	
	
	} catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

private String sendRequest(String apikey, String model, String url) throws ClientProtocolException, IOException {
	
	CloseableHttpClient httpclient = HttpClients.createDefault();
	
	
			

	HttpPost httpPost = new HttpPost("http://api.8bit.ai/tag");
	List <NameValuePair> nvps = new ArrayList <NameValuePair>();
	nvps.add(new BasicNameValuePair("apikey", apikey));
	nvps.add(new BasicNameValuePair("url", url));
	
	nvps.add(new BasicNameValuePair("modelkey",model));
	
	nvps.add(new BasicNameValuePair("lang", "eng"));
	
	
	httpPost.setEntity(new UrlEncodedFormEntity(nvps));
	
	CloseableHttpResponse response2 = httpclient.execute(httpPost);
	
	
	
	
	 StringBuffer sb=new StringBuffer();
	try {
	    System.out.println(response2.getStatusLine());
	    HttpEntity entity2 = response2.getEntity();
	    
	    
	   InputStream is = entity2.getContent();
	   
	   JSONTokener tokener = new JSONTokener(is);
	   
	   JSONObject dico = new JSONObject(tokener);
	     JSONArray results = dico.getJSONArray("results");
	     
	     for(int i=0;i<results.length();i++)
	     {
	    	 JSONObject result = (JSONObject) results.get(i);
	    	 if(result.has("tag")){
	    	 String tag = result.get("tag").toString();
	    	 String score = result.get("score").toString();
	    	 
	    	   System.out.println(tag+":"+score);
	    	 }
	     }
		  
	   
	  sb.append(dico);
	   
	   /*
	   BufferedReader br=new BufferedReader(new InputStreamReader(is, "UTF8"));
	   
	  
	   String line=null;
	   while((line=br.readLine())!=null)
	   {
		   if(sb.length()>0) sb.append("\n");
		   
		   sb.append(line);
	   }
	    */
	    // do something useful with the response body
	    // and ensure it is fully consumed
	    EntityUtils.consume(entity2);
	} finally {
	    response2.close();
	}
	return sb.toString();
}
}
