package glog.scrapper.msxgamesworld;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import glog.util.IOUtil;

public class PostProcessingJSON {

	public static void main(String[] args) throws Exception {

		String[] okeys = { "Product ID", "Year of release", "Languages", "Format", "Controls", "links", "Released by", "misc", "S-RAM", "Developed by", "Sound", "MGW ID", "url", "Type", "Generation", "Price", "series", "name", "capturas",
				"Number of players", "Region", "Country", "Genre", "caratulas", "publications" };
		String[] nkeys = { "productId", "year", "languages", "format", "controls", "links", "releasedBy", "misc", "SRAM", "developedBy", "sound", "", "url", "type", "generation", "price", "series", "name", "shots", "players", "region",
				"country", "genre", "titles", "publications" };
		String[] files = new File("data/msxgamesworldv2").list();
		for (String file : files) {
			if ("images".equals(file) || "processed".equals(file)) {
				continue;
			}
			Gson gson = new Gson();
			File jsonFile = Paths.get("c:/Users/luisoft/git/glog-scrapper/data/msxgamesworldv2/" + file).toFile();
			JsonObject jsonObject = gson.fromJson(new FileReader(jsonFile), JsonObject.class);
			for (int i = 0; i < okeys.length; i++) {
				// get value from old key and replace by the new one
				JsonElement value = jsonObject.get(okeys[i]);
				if (value != null) {
					jsonObject.remove(okeys[i]);
					if (!nkeys[i].equals("")) {
						jsonObject.add(nkeys[i], value);
					}
				}
			}
			jsonObject.addProperty("imported", false);
			Gson gson2 = new GsonBuilder().setPrettyPrinting().create();
			String json = gson2.toJson(jsonObject);
			System.out.println(json);
			IOUtil.write("c:/Users/luisoft/git/glog-scrapper/data/msxgamesworldv2/processed/" + file, json);
		}

		// list all keys.
		// String[] files = new File("msxgamesworldv2").list();
		// Set<String> keys = new HashSet<String>();
		// for (String file : files) {
		// Gson gson = new Gson();
		// File jsonFile = Paths.get("c:/Users/luisoft/git/glog-service/msxgamesworldv2/1.json").toFile();
		// JsonObject jsonObject = gson.fromJson(new FileReader(jsonFile), JsonObject.class);
		// for (String key : jsonObject.keySet()) {
		// keys.add(key);
		// }
		// }

		// for (String key : keys) {
		// System.out.println(key);
		// }

		// jsonObject.remove("MGW ID");
		// JsonElement value = jsonObject.get("Type");
		// jsonObject.remove("Type");
		// jsonObject.add("type", value);
		// System.out.println(jsonObject);

	}
}
