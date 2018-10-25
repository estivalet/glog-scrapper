package glog.scrapper.mobygames;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "game")
public class MobyGame {
	private String name;
	private String shortName;
	private String description;
	private String publishedBy;
	private String developedBy;
	private String released;
	private String alsoFor;
	private String genre;
	private String perspective;
	private String theme;
	private String nonSport;
	private String sport;
	private String misc;
	private String country;
	private String releaseDate;
	private String url;
	private String system;
	private String visual;
	private String gamePlay;
	private String setting;
	private List<MobyGameRelease> release = new ArrayList<MobyGameRelease>();
	private List<String> shot = new ArrayList<String>();
	private List<String> cover = new ArrayList<String>();

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the publishedBy
	 */
	public String getPublishedBy() {
		return publishedBy;
	}

	/**
	 * @param publishedBy
	 *            the publishedBy to set
	 */
	public void setPublishedBy(String publishedBy) {
		this.publishedBy = publishedBy;
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
	 * @return the released
	 */
	public String getReleased() {
		return released;
	}

	/**
	 * @param released
	 *            the released to set
	 */
	public void setReleased(String released) {
		this.released = released;
	}

	/**
	 * @return the alsoFor
	 */
	public String getAlsoFor() {
		return alsoFor;
	}

	/**
	 * @param alsoFor
	 *            the alsoFor to set
	 */
	public void setAlsoFor(String alsoFor) {
		this.alsoFor = alsoFor;
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
	 * @return the perspective
	 */
	public String getPerspective() {
		return perspective;
	}

	/**
	 * @param perspective
	 *            the perspective to set
	 */
	public void setPerspective(String perspective) {
		this.perspective = perspective;
	}

	/**
	 * @return the theme
	 */
	public String getTheme() {
		return theme;
	}

	/**
	 * @param theme
	 *            the theme to set
	 */
	public void setTheme(String theme) {
		this.theme = theme;
	}

	/**
	 * @return the nonSport
	 */
	public String getNonSport() {
		return nonSport;
	}

	/**
	 * @param nonSport
	 *            the nonSport to set
	 */
	public void setNonSport(String nonSport) {
		this.nonSport = nonSport;
	}

	/**
	 * @return the sport
	 */
	public String getSport() {
		return sport;
	}

	/**
	 * @param sport
	 *            the sport to set
	 */
	public void setSport(String sport) {
		this.sport = sport;
	}

	/**
	 * @return the misc
	 */
	public String getMisc() {
		return misc;
	}

	/**
	 * @param misc
	 *            the misc to set
	 */
	public void setMisc(String misc) {
		this.misc = misc;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the releaseDate
	 */
	public String getReleaseDate() {
		return releaseDate;
	}

	/**
	 * @param releaseDate
	 *            the releaseDate to set
	 */
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the screenshots
	 */
	@XmlElementWrapper(name = "screenshots")
	public List<String> getShot() {
		return shot;
	}

	/**
	 * @param screenshots
	 *            the screenshots to set
	 */
	public void setShot(List<String> shot) {
		this.shot = shot;
	}

	/**
	 * @return the release
	 */
	@XmlElementWrapper(name = "releases")
	public List<MobyGameRelease> getRelease() {
		return release;
	}

	/**
	 * @param release
	 *            the release to set
	 */
	public void setRelease(List<MobyGameRelease> release) {
		this.release = release;
	}

	/**
	 * @return the cover
	 */
	@XmlElementWrapper(name = "covers")
	public List<String> getCover() {
		return cover;
	}

	/**
	 * @param cover
	 *            the cover to set
	 */
	public void setCover(List<String> cover) {
		this.cover = cover;
	}

	public void addRelease(MobyGameRelease release) {
		this.release.add(release);
	}

	public void addScreenshot(String shotLink) {
		this.shot.add(shotLink);
	}

	public void addCover(String coverLink) {
		this.cover.add(coverLink);

	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getVisual() {
		return visual;
	}

	public void setVisual(String visual) {
		this.visual = visual;
	}

	public String getGamePlay() {
		return gamePlay;
	}

	public void setGamePlay(String gamePlay) {
		this.gamePlay = gamePlay;
	}

	public String getSetting() {
		return setting;
	}

	public void setSetting(String setting) {
		this.setting = setting;
	}

}
