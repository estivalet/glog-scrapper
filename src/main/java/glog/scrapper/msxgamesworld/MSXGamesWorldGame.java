package glog.scrapper.msxgamesworld;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "game")
public class MSXGamesWorldGame {

	private String url;
	private String name;
	private String description;
	private String developer;
	private String publisher;
	private String year;
	private String type;
	private String format;
	private String generation;
	private String sound;
	private String languages;
	private String control;
	private String players;
	private String region;
	private String country;
	private String instructions;
	private String productId;
	private String typeOfLicense;
	private String requires;
	private String price;
	private List<String> remake = new ArrayList<String>();
	private List<MSXGamesWorldGameRelease> release = new ArrayList<MSXGamesWorldGameRelease>();
	private List<String> caratula = new ArrayList<String>();
	private List<String> captura = new ArrayList<String>();

	@XmlElementWrapper(name = "releases")
	public List<MSXGamesWorldGameRelease> getRelease() {
		return release;
	}

	/**
	 * @param release
	 *            the release to set
	 */
	public void addRelease(MSXGamesWorldGameRelease release) {
		this.release.add(release);
	}

	@XmlElementWrapper(name = "caratulas")
	public List<String> getCaratula() {
		return caratula;
	}

	public void addCaratula(String caratula) {
		this.caratula.add(caratula);
	}

	@XmlElementWrapper(name = "capturas")
	public List<String> getCaptura() {
		return captura;
	}

	public void addCaptura(String captura) {
		this.captura.add(captura);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getGeneration() {
		return generation;
	}

	public void setGeneration(String generation) {
		this.generation = generation;
	}

	public String getSound() {
		return sound;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}

	public String getLanguages() {
		return languages;
	}

	public void setLanguages(String languages) {
		this.languages = languages;
	}

	public String getControl() {
		return control;
	}

	public void setControl(String control) {
		this.control = control;
	}

	public String getPlayers() {
		return players;
	}

	public void setPlayers(String players) {
		this.players = players;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getTypeOfLicense() {
		return typeOfLicense;
	}

	public void setTypeOfLicense(String typeOfLicense) {
		this.typeOfLicense = typeOfLicense;
	}

	public String getRequires() {
		return requires;
	}

	public void setRequires(String requires) {
		this.requires = requires;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public void addRemake(String url) {
		this.remake.add(url);
	}

	@XmlElementWrapper(name = "remakes")
	public List<String> getRemake() {
		return this.remake;
	}
}
