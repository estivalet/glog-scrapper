package glog.scrapper.generationmsx;

import static com.mongodb.client.model.Filters.eq;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import glog.util.IOUtil;

public class PostProcessingJSON {

	public static void importCompanies() throws Exception {
		// Store in mongodb.
		int port_no = 27017;
		String host_name = "localhost", db_name = "msxdb", db_coll_name = "companies";
		String client_url = "mongodb://" + host_name + ":" + port_no + "/" + db_name;
		MongoClientURI uri = new MongoClientURI(client_url);
		MongoClient mongo_client = new MongoClient(uri);
		MongoDatabase db = mongo_client.getDatabase(db_name);
		MongoCollection<Document> coll = db.getCollection(db_coll_name);
		String[] files = new File("data/generationmsx/companies").list();
		for (String file : files) {
			System.out.println(file);
			String json = IOUtil.readFully(new FileInputStream("data/generationmsx/companies/" + file));
			Document doc = Document.parse(json);
			coll.insertOne(doc);
		}
		mongo_client.close();
		System.exit(0);

	}

	public static void importGames() throws Exception {
		// Store in mongodb.
		int port_no = 27017;
		String host_name = "localhost", db_name = "msxdb", db_coll_name = "generationmsx";
		String client_url = "mongodb://" + host_name + ":" + port_no + "/" + db_name;
		MongoClientURI uri = new MongoClientURI(client_url);
		MongoClient mongo_client = new MongoClient(uri);
		MongoDatabase db = mongo_client.getDatabase(db_name);
		MongoCollection<Document> coll = db.getCollection(db_coll_name);
		String[] files = new File("data/generationmsx/json").list();
		for (String file : files) {
			System.out.println(file);
			String json = IOUtil.readFully(new FileInputStream("data/generationmsx/json/" + file));
			Document doc = Document.parse(json);
			coll.insertOne(doc);
		}
		mongo_client.close();
		System.exit(0);

	}

	public static void importGamesUsinMyStructure() throws Exception {
		int port_no = 27017;
		String host_name = "localhost", db_name = "msxdb", db_coll_name = "softwares";
		String client_url = "mongodb://" + host_name + ":" + port_no + "/" + db_name;
		MongoClientURI uri = new MongoClientURI(client_url);
		MongoClient mongo_client = new MongoClient(uri);
		MongoDatabase db = mongo_client.getDatabase(db_name);
		MongoCollection<Document> companies = db.getCollection("companies");
		MongoCollection<Document> softwares = db.getCollection("softwares");
		MongoCollection<Document> genres = db.getCollection("genres");
		String[] files = new File("data/generationmsx/json").list();
		for (String file : files) {
			System.out.println(file);
			String json = IOUtil.readFully(new FileInputStream("data/generationmsx/json/2247.json"));// + file));
			Document doc = Document.parse(json);

			// Find company.
			Document company = companies.find(eq("name", doc.get("developedBy"))).first();

			// Find genre.
			String g = doc.getString("genre");
			if (g != null) {
				g = g.split(",")[0];
			}
			Document genre = genres.find(eq("name", g)).first();

			// Get mentions to adjust description.
			List<Document> mentions = (List<Document>) doc.get("mentions");
			List<Document> mentions2 = new ArrayList<Document>();
			for (int i = 0; i < mentions.size(); i++) {
				String mentionUrl = mentions.get(i).get("url").toString();
				String mentionText = mentions.get(i).get("text").toString();
				String mentionDescription = mentions.get(i).get("description").toString().replaceAll("\\s+", " ");
				Document mention = new Document();
				mention.put("url", mentionUrl);
				mention.put("text", mentionText);
				mention.put("description", mentionDescription);
				mentions2.add(mention);
			}

			// Get releases to adjust
			List<Document> releases = (List<Document>) doc.get("releases");

			List<Document> releases2 = new ArrayList<Document>();
			for (int i = 0; i < releases.size(); i++) {
				String name = releases.get(i).get("name").toString().replaceAll("\\s+", " ");
				String media = "";
				if (releases.get(i).get("media") != null) {
					media = releases.get(i).get("media").toString().replaceAll("\\s+", " ");
				}
				List<Document> medias = (List<Document>) releases.get(i).get("medias");
				Document companyPub = companies.find(eq("name", releases.get(i).get("publisher"))).first();
				Document release = new Document();
				release.put("name", name);
				release.put("media", media);
				release.put("medias", medias);
				if (companyPub != null) {
					release.put("publisher", companyPub.get("_id"));
				}
				releases2.add(release);
			}

			// Add it.
			Document software = new Document();
			software.put("name", doc.get("originalTitle"));
			software.put("year", doc.get("year"));
			software.put("system", doc.get("system"));
			software.put("mentions", mentions2);
			software.put("releases", releases2);
			if (company.get("_id") != null) {
				software.put("developer", company.get("_id"));
			}
			if (genre.get("_id") != null) {
				software.put("genre", genre.get("_id"));
			}
			software.put("medias", doc.get("medias"));
			softwares.insertOne(software);

			break;
		}
		mongo_client.close();
		System.exit(0);
	}

	public static void generateListComparingGenerationMSXWithAttractMode() throws Exception {
		Set<String> genmsxtitle = new HashSet<String>();
		Gson gson = new Gson();
		for (String file : new File("d:/msx/generationmsx/json").list()) {
			String json = IOUtil.readFully(new FileInputStream("d:/msx/generationmsx/json/" + file));
			GenerationMSXGame game = gson.fromJson(json, GenerationMSXGame.class);
			if ("Game".equals(game.getKind())) {
				String title = game.getTranslatedTitle();
				if (title.trim().equals("") || title.contains("Japanese")) {
					title = game.getOriginalTitle();
				}
				if (title.indexOf("(") > 0) {
					title = title.substring(0, title.indexOf("(") - 1);
				}
				genmsxtitle.add(title + ";" + file);
			}
		}

		Iterator<String> iterator = genmsxtitle.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
	}

	public static void main(String[] args) throws Exception {
		PostProcessingJSON.generateListComparingGenerationMSXWithAttractMode();
		System.exit(0);

		PostProcessingJSON.importGamesUsinMyStructure();
		System.exit(0);

		Gson gson = new Gson();

		Set<String> companies = new HashSet<String>();
		Set<String> genres = new HashSet<String>();
		for (String file : new File("c:/temp/generationmsx/json").list()) {
			String json = IOUtil.readFully(new FileInputStream("c:/temp/generationmsx/json/" + file));
			GenerationMSXGame game = gson.fromJson(json, GenerationMSXGame.class);
			for (GenerationMSXGameRelease r : game.getReleases()) {
				if (r.getPublisher() != null) {
					companies.add(r.getPublisher().replaceAll("\\s+", " "));
				}
			}
			companies.add(game.getDevelopedBy().replaceAll("\\s+", " "));
			companies.add(game.getPortedBy().replaceAll("\\s+", " "));
			String[] gens = game.getGenre().split(",");
			for (String genre : gens) {
				genres.add(genre);
			}
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
