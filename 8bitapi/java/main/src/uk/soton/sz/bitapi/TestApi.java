package uk.soton.sz.bitapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
/**
 * Test example for using the 8bit API for image recognition
 * Docu: http://www.8bitvision.com/dev
 * You will need to request an API Key - create an account, login, go to your profile, select Apps, "Create New App"
 * @author zerr
 *
 */
public class TestApi {
	
	static public String configdirectory="/home/zerr/.8bit/";
	
	public static void main(String[] args) {

		TestApi api = new TestApi();

		Properties prop = new Properties();
		FileInputStream input;
		try {
			input = new FileInputStream(new File(configdirectory,"config.properties"));
			prop.load(input);

			String key = prop.getProperty("api-key");
String imgurl="https://www.daysoftheyear.com/wp-content/images/hot-dog-day2-e1437733838777-808x380.jpg";
			try {
				System.out.println(api.sendRequest(key, "scene",
						imgurl));
				System.out.println(api.sendRequest(key, "concept",
						imgurl));
				System.out.println(api.sendRequest(key, "object",
						imgurl));
				System.out.println(api.sendRequest(key, "food",
						imgurl));

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private String sendRequest(String apikey, String model, String url) throws ClientProtocolException, IOException {

		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpPost httpPost = new HttpPost("http://api.8bit.ai/tag");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("apikey", apikey));
		nvps.add(new BasicNameValuePair("url", url));

		nvps.add(new BasicNameValuePair("modelkey", model));

		nvps.add(new BasicNameValuePair("lang", "eng"));

		httpPost.setEntity(new UrlEncodedFormEntity(nvps));

		CloseableHttpResponse response2 = httpclient.execute(httpPost);

		StringBuffer sb = new StringBuffer();
		try {
			System.out.println(response2.getStatusLine());
			HttpEntity entity2 = response2.getEntity();

			InputStream is = entity2.getContent();

			JSONTokener tokener = new JSONTokener(is);

			JSONObject dico = new JSONObject(tokener);
			JSONArray results = dico.getJSONArray("results");

			for (int i = 0; i < results.length(); i++) {
				JSONObject result = (JSONObject) results.get(i);
				if (result.has("tag")) {
					String tag = result.get("tag").toString();
					String score = result.get("score").toString();

					System.out.println(tag + ":" + score);
				}
			}

			sb.append(dico);

			/*
			 * BufferedReader br=new BufferedReader(new InputStreamReader(is,
			 * "UTF8"));
			 * 
			 * 
			 * String line=null; while((line=br.readLine())!=null) {
			 * if(sb.length()>0) sb.append("\n");
			 * 
			 * sb.append(line); }
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
