package glog.scrapper.lemonamiga;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "game")
public class LemonAmigaGame {
	private String developer;
	private String design;
	private String manager;
	private String producer;
	private String coder;
	private String graphics;
	private String musician;
	private String hardware;
	private String disks;
	private String genre;
	private String license;
	private String language;
	private String players;
	private String relationship;
	private List<String> shot = new ArrayList<String>();
	private List<String> advert = new ArrayList<String>();
	private List<String> box = new ArrayList<String>();
	private String url;
	private String name;
	private List<LemonAmigaMagazineReview> review = new ArrayList<LemonAmigaMagazineReview>();

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public String getDesign() {
		return design;
	}

	public void setDesign(String design) {
		this.design = design;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public String getCoder() {
		return coder;
	}

	public void setCoder(String coder) {
		this.coder = coder;
	}

	public String getGraphics() {
		return graphics;
	}

	public void setGraphics(String graphics) {
		this.graphics = graphics;
	}

	public String getMusician() {
		return musician;
	}

	public void setMusician(String musician) {
		this.musician = musician;
	}

	public String getHardware() {
		return hardware;
	}

	public void setHardware(String hardware) {
		this.hardware = hardware;
	}

	public String getDisks() {
		return disks;
	}

	public void setDisks(String disks) {
		this.disks = disks;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getPlayers() {
		return players;
	}

	public void setPlayers(String players) {
		this.players = players;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	/**
	 * @return the cover
	 */
	@XmlElementWrapper(name = "shots")
	public List<String> getShot() {
		return shot;
	}

	public void addShot(String shotlink) {
		this.shot.add(shotlink);

	}

	/**
	 * @return the cover
	 */
	@XmlElementWrapper(name = "adverts")
	public List<String> getAdvert() {
		return advert;
	}

	/**
	 * @param cover
	 *            the cover to set
	 */
	public void setAdvert(List<String> advert) {
		this.advert = advert;
	}

	public void addAdvert(String advertlink) {
		this.advert.add(advertlink);

	}

	/**
	 * @return the cover
	 */
	@XmlElementWrapper(name = "boxes")
	public List<String> getBox() {
		return box;
	}

	/**
	 * @param cover
	 *            the cover to set
	 */
	public void setBox(List<String> box) {
		this.box = box;
	}

	public void addBox(String boxlink) {
		this.box.add(boxlink);

	}

	public void setUrl(String link) {
		this.url = link;

	}

	public void setName(String name) {
		this.name = name;

	}

	@XmlElementWrapper(name = "reviews")
	public List<LemonAmigaMagazineReview> getReview() {
		return review;
	}

	public void setReview(List<LemonAmigaMagazineReview> review) {
		this.review = review;
	}

	public void addReview(LemonAmigaMagazineReview reviewLink) {
		this.review.add(reviewLink);
	}

	public String getUrl() {
		return url;
	}

	public String getName() {
		return name;
	}

	public void setShot(List<String> shot) {
		this.shot = shot;
	}

}
