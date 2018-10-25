package glog.scrapper.mobygames;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gson.Gson;

import glog.util.IOUtil;

public class PostProcessingJSON {
	public static void main(String[] args) throws Exception {
		Gson gson = new Gson();

		// String json = IOUtil.readFully(new FileInputStream("data/mobygames/MSX - MSX 1/json/arcshu-kagerou-no-jidai-o-koete.json"));
		// MobyGame game = gson.fromJson(json, MobyGame.class);
		// System.out.println(game.getPublishedBy());
		// System.out.println(game.getPublishedBy().charAt(4));

		// int port_no = 27017;
		// String host_name = "localhost", db_name = "msxdb", db_coll_name = "softwares";
		// String client_url = "mongodb://" + host_name + ":" + port_no + "/" + db_name;
		// MongoClientURI uri = new MongoClientURI(client_url);
		// MongoClient mongo_client = new MongoClient(uri);
		// MongoDatabase db = mongo_client.getDatabase(db_name);
		// MongoCollection<Document> coll = db.getCollection(db_coll_name);
		// String[] files = new File("data/mobygames/MSX - MSX 1/json/").list();
		// for (String file : files) {
		// System.out.println(file);
		// String json = IOUtil.readFully(new FileInputStream("data/mobygames/MSX - MSX 1/json/" + file));
		// Document doc = Document.parse(json);
		// doc.remove("shortName");
		// doc.remove("misc");
		// doc.remove("released");
		// doc.remove("alsoFor");
		// doc.remove("genre");
		// doc.remove("perspective");
		// doc.remove("shot");
		// doc.remove("cover");
		// doc.remove("url");
		// doc.remove("country");
		// doc.remove("releaseDate");
		// doc.remove("sport");
		// doc.remove("nonSport");
		// coll.insertOne(doc);
		// System.exit(0);
		// }

		Set<String> companies = new HashSet<String>();
		Set<String> genres = new HashSet<String>();
		for (String file : new File("data/mobygames/MSX - MSX 1/json").list()) {
			String json = IOUtil.readFully(new FileInputStream("data/mobygames/MSX - MSX 1/json/" + file));
			MobyGame game = gson.fromJson(json, MobyGame.class);
			for (MobyGameRelease r : game.getRelease()) {
				for (MobyGameReleaseInfo r1 : r.getReleases()) {
					companies.add(r1.getDeveloper());
					companies.add(r1.getPublisher());
				}
			}
			companies.add(game.getPublishedBy());
			companies.add(game.getDevelopedBy());
			genres.add(game.getGenre());
		}

		// Companies...
		String json = "[\n";
		Iterator<String> iterator = companies.iterator();
		while (iterator.hasNext()) {
			json += "{\"name\":\"" + iterator.next() + "\"},\n";
		}
		json += "]\n";
		IOUtil.write(".", "companies.json", json);

		// Genres...
		json = "[\n";
		iterator = genres.iterator();
		while (iterator.hasNext()) {
			json += "{\"name\":\"" + iterator.next() + "\"},\n";
		}
		json += "]\n";
		IOUtil.write(".", "genres.json", json);

	}
}
