package backend;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;

import org.jinstagram.Instagram;
import org.jinstagram.entity.common.Caption;
import org.jinstagram.entity.common.Images;
import org.jinstagram.entity.common.Videos;
import org.jinstagram.entity.users.basicinfo.UserInfo;
import org.jinstagram.entity.users.basicinfo.UserInfoData;
import org.jinstagram.entity.users.feed.CarouselMedia;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.entity.users.feed.UserFeed;
import org.jinstagram.exceptions.InstagramException;
import org.json.JSONException;
import org.json.JSONObject;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.PhotoUpload;
import com.vk.api.sdk.objects.photos.responses.WallUploadResponse;
import com.vk.api.sdk.objects.video.SaveResult;

public class Driver {

	private static Instagram instagram;
	private static UserFeed userFeed;
	private static String vkAccessToken;

	public static void postPhoto(int groupId, String photoPath)
			throws ApiException, ClientException, FileNotFoundException {

		File f = new File(photoPath + ".jpg");

		String photoText = getTextFromFile(photoPath + ".txt");

		TransportClient transportClient = HttpTransportClient.getInstance();
		VkApiClient vk = new VkApiClient(transportClient);

		UserActor actor = new UserActor(groupId, vkAccessToken);
		PhotoUpload serverResponse = vk.photos().getWallUploadServer(actor).groupId(groupId).execute();

		WallUploadResponse uploadResponse = vk.upload().photoWall(serverResponse.getUploadUrl(), f).execute();

		List<Photo> photoList = vk.photos().saveWallPhoto(actor, uploadResponse.getPhoto())
				.server(uploadResponse.getServer()).hash(uploadResponse.getHash()).groupId(groupId).execute();

		Photo photo = photoList.get(0);
		String attachId = "photo" + photo.getOwnerId() + "_" + photo.getId();
		vk.wall().post(actor).ownerId(-groupId).attachments(attachId).message(photoText).fromGroup(true).execute();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void postVideo(int groupId, String videoPath)
			throws ApiException, ClientException, FileNotFoundException {

		File f = new File(videoPath + ".mp4");

		String videoText = getTextFromFile(videoPath + ".txt");

		TransportClient transportClient = HttpTransportClient.getInstance();
		VkApiClient vk = new VkApiClient(transportClient);

		UserActor actor = new UserActor(groupId, vkAccessToken);
		SaveResult videoResponse = vk.videos().save(actor).groupId(groupId).wallpost(true).description(videoText)
				.execute();
		vk.upload().video(videoResponse.getUploadUrl(), f).execute();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void postGallery(int groupId, int size, String galleryPath)
			throws ApiException, ClientException, FileNotFoundException {

		String galleryText = getTextFromFile(galleryPath + ".txt");

		ArrayList<File> files = new ArrayList<File>();

		for (int i = 1; i <= size; i++) {
			String mediaPath = galleryPath + "_" + Integer.toString(i) + ".jpg";
			File f = new File(mediaPath);
			if (f.exists()) {
				files.add(f);
			} else {
				mediaPath = galleryPath + "_" + Integer.toString(i) + ".mp4";
				if (f.exists()) {
					files.add(f);
				}
			}
		}

		TransportClient transportClient = HttpTransportClient.getInstance();
		VkApiClient vk = new VkApiClient(transportClient);

		UserActor actor = new UserActor(groupId, vkAccessToken);

		StringBuilder attachments = new StringBuilder();

		for (int i = 0; i < files.size(); i++) {
			File f = files.get(i);
			if (f.getPath().endsWith(".jpg")) {
				PhotoUpload serverResponse = vk.photos().getWallUploadServer(actor).groupId(groupId).execute();

				WallUploadResponse uploadResponse = vk.upload().photoWall(serverResponse.getUploadUrl(), f).execute();

				List<Photo> photoList = vk.photos().saveWallPhoto(actor, uploadResponse.getPhoto())
						.server(uploadResponse.getServer()).hash(uploadResponse.getHash()).groupId(groupId).execute();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				Photo photo = photoList.get(0);
				if (i != 0) {
					attachments.append(",");
				}
				attachments.append("photo" + photo.getOwnerId() + "_" + photo.getId());
			} else {
				// TODO videos uploading
			}
		}
		vk.wall().post(actor).ownerId(-groupId).attachments(attachments.toString()).message(galleryText).fromGroup(true)
				.execute();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private static String getTextFromFile(String path) throws FileNotFoundException {
		StringBuilder s = new StringBuilder();
		Scanner in = new Scanner(new File(path));
		while (in.hasNext()) {
			s.append(in.nextLine());
		}
		in.close();
		return s.toString();
	}

	public static void connectToInstagram(String login) throws InstagramException {

		instagram = new Instagram("", "", "127.0.0.1");

		userFeed = instagram.searchUser(login);
	}

	public static void connectToVk(String email, String pass) throws IOException, JSONException {

		URL url = new URL(
				"https://oauth.vk.com/token?grant_type=password&client_id=-------&client_secret=--------&username="
						+ email + "&password=" + pass + "&scope=friends,offline,photos,groups,wall,video");
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

		String input = br.readLine();
		br.close();
		JSONObject jObject = new JSONObject(input);
		vkAccessToken = jObject.getString("access_token");
	}

	public static void savePosts() throws MalformedURLException, IOException {

		deleteAllFilesInFolder("./gallery");
		deleteAllFilesInFolder("./videos");
		deleteAllFilesInFolder("./pictures");

		String instagramUserId = userFeed.getUserList().get(0).getId();

		UserInfo userInfo = instagram.getUserInfo(instagramUserId);

		UserInfoData userData = userInfo.getData();
		System.out.println("id : " + userData.getId());
		System.out.println("username : " + userData.getUsername());

		// Get 10 most recent user posts
		MediaFeed mediaFeed = instagram.getRecentMediaFeed(instagramUserId);
		List<MediaFeedData> mediaFeeds = mediaFeed.getData();

		int numPhotos = 1;
		int numVideos = 1;
		int numGalleries = 1;
		String photoPath = new String();
		String videoPath = new String();
		String galleryPath = new String();
		for (MediaFeedData mediaData : mediaFeeds) {
			System.out.println();
			System.out.println("id : " + mediaData.getId());
			System.out.println("link : " + mediaData.getLink());
			System.out.println("tags : " + mediaData.getTags().toString());
			System.out.println("filter : " + mediaData.getImageFilter());
			String type = mediaData.getType();
			System.out.println("type : " + type);

			Images images = null;
			Videos videos = null;
			List<CarouselMedia> carousel = null;

			// Save gallery
			if (type.equals("carousel")) {
				carousel = mediaData.getCarouselMedia();
				if (carousel != null) {
					System.out.println("-- Gallery --");
					System.out.println("gallery : " + carousel.toString());
					galleryPath = "./gallery/" + Integer.toString(numGalleries);
					for (int i = 0; i < carousel.size(); i++) {
						CarouselMedia media = carousel.get(i);
						if (media.getType().equals("image")) {
							String mediaPath = galleryPath + "_" + Integer.toString(i + 1) + ".jpg";
							BufferedImage img = ImageIO
									.read(new URL(media.getImages().getStandardResolution().getImageUrl()));
							File file = new File(mediaPath);
							if (!file.exists()) {
								file.createNewFile();
							}
							ImageIO.write(img, "jpg", file);
						} else if (media.getType().equals("video")) {
							String mediaPath = galleryPath + "_" + Integer.toString(i + 1) + ".mp4";
							URL url = new URL(media.getVideos().getStandardResolution().getUrl());
							ReadableByteChannel rbc = Channels.newChannel(url.openStream());
							FileOutputStream fos = new FileOutputStream(mediaPath);
							fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
							fos.close();
							rbc.close();
						}
					}
					Caption c = mediaData.getCaption();
					if (c.getText() != null) {
						String path = "./gallery/" + Integer.toString(numGalleries) + ".txt";
						FileWriter writer = new FileWriter(path, false);
						writer.write(c.getText());
						writer.close();
					}
					numGalleries++;
				}
			}
			// Save photo
			else if (type.equals("image")) {
				images = mediaData.getImages();
				if (images != null) {
					System.out.println("-- Images --");
					System.out.println("images : " + images.toString());
					photoPath = "./pictures/" + Integer.toString(numPhotos) + ".jpg";
					BufferedImage img = ImageIO.read(new URL(images.getStandardResolution().getImageUrl()));
					File file = new File(photoPath);
					if (!file.exists()) {
						file.createNewFile();
					}
					ImageIO.write(img, "jpg", file);
					Caption c = mediaData.getCaption();
					if (c.getText() != null) {
						photoPath = "./pictures/" + Integer.toString(numPhotos) + ".txt";
						FileWriter writer = new FileWriter(photoPath, false);
						writer.write(c.getText());
						writer.close();
					}
					numPhotos++;
				}
			} else
			// Save video
			if (type.equals("video")) {
				videos = mediaData.getVideos();
				if (videos != null) {
					System.out.println("-- Videos --");
					System.out.println("videos : " + videos.toString());
					URL url = new URL(videos.getStandardResolution().getUrl());
					ReadableByteChannel rbc = Channels.newChannel(url.openStream());
					videoPath = "./videos/" + Integer.toString(numVideos) + ".mp4";
					FileOutputStream fos = new FileOutputStream(videoPath);
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
					fos.close();
					rbc.close();
					Caption c = mediaData.getCaption();
					if (c.getText() != null) {
						videoPath = "./videos/" + Integer.toString(numVideos) + ".txt";
						FileWriter writer = new FileWriter(videoPath, false);
						writer.write(c.getText());
						writer.close();
					}
					numVideos++;
				}
			}
		}
	}

	private static void deleteAllFilesInFolder(String path) {
		for (File myFile : new File(path).listFiles())
			if (myFile.isFile())
				myFile.delete();
	}

}
