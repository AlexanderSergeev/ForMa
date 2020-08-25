package backend;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

public class Main {

	public static void main(String[] args) throws MalformedURLException, IOException, ApiException, ClientException {

		String instagram_login = "";
		Driver.connectToInstagram(instagram_login);

		String vk_email = "";
		String vk_pass = "";
		try {
			Driver.connectToVk(vk_email, vk_pass);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Driver.savePosts();

		int vkGroupId = 142125004;
		String path = "./gallery/2";
		//Driver.postPhoto(vkGroupId, path);
		//Driver.postVideo(vkGroupId, path);
		Driver.postGallery(vkGroupId, 3, path);
	}
}
