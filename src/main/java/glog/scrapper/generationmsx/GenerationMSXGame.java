package glog.scrapper.generationmsx;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "game")
public class GenerationMSXGame {
	private String url;
	private String originalTitle;
	private String translatedTitle;
	private String developedBy;
	private String year;
	private String system;
	private String sound;
	private String kind;
	private String inputDevicesSupported;
	private String genre;
	private String maxPlayers;
	private String maxSimultaneous;
	private String usesKanji;
	private String license;
	private String licence;
	private String language;
	private String note;
	private String tagooDatabaseEntry;
	private String credits;
	private String ram;
	private String vram;
	private String portedBy;
	private List<GenerationMSXGameMentions> mentions = new ArrayList<GenerationMSXGameMentions>();
	private List<String> conversions = new ArrayList<String>();
	private List<String> seealso = new ArrayList<String>();
	private List<String> group = new ArrayList<String>();
	private List<GenerationMSXGameRelease> releases = new ArrayList<GenerationMSXGameRelease>();
	private List<GenerationMSXGameMedia> medias = new ArrayList<GenerationMSXGameMedia>();

	/**
	 * @return the originalTitle
	 */
	public String getOriginalTitle() {
		return originalTitle;
	}

	/**
	 * @param originalTitle
	 *            the originalTitle to set
	 */
	public void setOriginalTitle(String originalTitle) {
		this.originalTitle = originalTitle;
	}

	/**
	 * @return the developedBy
	 */
	public String getDevelopedBy() {
		return developedBy;
	}

	/**
	 * @param developedBy
	 *            the developedBy to set
	 */
	public void setDevelopedBy(String developedBy) {
		this.developedBy = developedBy;
	}

	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}

	/**
	 * @return the system
	 */
	public String getSystem() {
		return system;
	}

	/**
	 * @param system
	 *            the system to set
	 */
	public void setSystem(String system) {
		this.system = system;
	}

	/**
	 * @return the sound
	 */
	public String getSound() {
		return sound;
	}

	/**
	 * @param sound
	 *            the sound to set
	 */
	public void setSound(String sound) {
		this.sound = sound;
	}

	/**
	 * @return the kind
	 */
	public String getKind() {
		return kind;
	}

	/**
	 * @param kind
	 *            the kind to set
	 */
	public void setKind(String kind) {
		this.kind = kind;
	}

	/**
	 * @return the inputDevicesSupported
	 */
	public String getInputDevicesSupported() {
		return inputDevicesSupported;
	}

	/**
	 * @param inputDevicesSupported
	 *            the inputDevicesSupported to set
	 */
	public void setInputDevicesSupported(String inputDevicesSupported) {
		this.inputDevicesSupported = inputDevicesSupported;
	}

	/**
	 * @return the genre
	 */
	public String getGenre() {
		return genre;
	}

	/**
	 * @param genre
	 *            the genre to set
	 */
	public void setGenre(String genre) {
		this.genre = genre;
	}

	/**
	 * @return the maxPlayers
	 */
	public String getMaxPlayers() {
		return maxPlayers;
	}

	/**
	 * @param maxPlayers
	 *            the maxPlayers to set
	 */
	public void setMaxPlayers(String maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	/**
	 * @return the maxSimultaneous
	 */
	public String getMaxSimultaneous() {
		return maxSimultaneous;
	}

	/**
	 * @param maxSimultaneous
	 *            the maxSimultaneous to set
	 */
	public void setMaxSimultaneous(String maxSimultaneous) {
		this.maxSimultaneous = maxSimultaneous;
	}

	/**
	 * @return the usesKanji
	 */
	public String getUsesKanji() {
		return usesKanji;
	}

	/**
	 * @param usesKanji
	 *            the usesKanji to set
	 */
	public void setUsesKanji(String usesKanji) {
		this.usesKanji = usesKanji;
	}

	/**
	 * @return the license
	 */
	public String getLicense() {
		return license;
	}

	/**
	 * @param license
	 *            the license to set
	 */
	public void setLicense(String license) {
		this.license = license;
	}

	/**
	 * @return the licence
	 */
	public String getLicence() {
		return licence;
	}

	/**
	 * @param licence
	 *            the licence to set
	 */
	public void setLicence(String licence) {
		this.licence = licence;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the tagooDatabaseEntry
	 */
	public String getTagooDatabaseEntry() {
		return tagooDatabaseEntry;
	}

	/**
	 * @param tagooDatabaseEntry
	 *            the tagooDatabaseEntry to set
	 */
	public void setTagooDatabaseEntry(String tagooDatabaseEntry) {
		this.tagooDatabaseEntry = tagooDatabaseEntry;
	}

	/**
	 * @return the release
	 */
	@XmlElementWrapper(name = "releases")
	public List<GenerationMSXGameRelease> getReleases() {
		return releases;
	}

	/**
	 * @param release
	 *            the release to set
	 */
	public void setReleases(List<GenerationMSXGameRelease> release) {
		this.releases = release;
	}

	public void addRelease(GenerationMSXGameRelease release) {
		this.releases.add(release);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<GenerationMSXGameMentions> getMentions() {
		return mentions;
	}

	public void setMentions(List<GenerationMSXGameMentions> mentions) {
		this.mentions = mentions;
	}

	public void addMention(GenerationMSXGameMentions mention) {
		this.mentions.add(mention);
	}

	public List<String> getConversions() {
		return conversions;
	}

	public void setConversions(List<String> conversions) {
		this.conversions = conversions;
	}

	public void addConversion(String conversion) {
		this.conversions.add(conversion);
	}

	public List<GenerationMSXGameMedia> getMedias() {
		return medias;
	}

	public void setMedias(List<GenerationMSXGameMedia> medias) {
		this.medias = medias;
	}

	public void addMedia(GenerationMSXGameMedia media) {
		this.medias.add(media);
	}

	public String getTranslatedTitle() {
		return translatedTitle;
	}

	public void setTranslatedTitle(String translatedTitle) {
		this.translatedTitle = translatedTitle;
	}

	public String getCredits() {
		return credits;
	}

	public void setCredits(String credits) {
		this.credits = credits;
	}

	public String getRam() {
		return ram;
	}

	public void setRam(String ram) {
		this.ram = ram;
	}

	public String getVram() {
		return vram;
	}

	public void setVram(String vram) {
		this.vram = vram;
	}

	public String getPortedBy() {
		return portedBy;
	}

	public void setPortedBy(String portedBy) {
		this.portedBy = portedBy;
	}

	public List<String> getSeealso() {
		return seealso;
	}

	public void setSeealso(List<String> seealso) {
		this.seealso = seealso;
	}

	public void addSeealso(String setSeealso) {
		this.seealso.add(setSeealso);
	}

	public List<String> getGroup() {
		return group;
	}

	public void setGroup(List<String> group) {
		this.group = group;
	}

	public void addGroup(String group) {
		this.group.add(group);
	}

}
