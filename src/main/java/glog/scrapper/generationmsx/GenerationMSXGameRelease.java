package glog.scrapper.generationmsx;

import java.util.ArrayList;
import java.util.List;

public class GenerationMSXGameRelease {
	private String name;
	private String title;
	private String productCode;
	private String pirated;
	private String publisher;
	private String media;
	private String manual;
	private List<GenerationMSXGameMedia> medias = new ArrayList<GenerationMSXGameMedia>();
	private List<String> countries = new ArrayList<String>();
	private String description;

	/**
	 * @return the publisher
	 */
	public String getPublisher() {
		return publisher;
	}

	/**
	 * @param publisher
	 *            the publisher to set
	 */
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	/**
	 * @return the media
	 */
	public String getMedia() {
		return media;
	}

	/**
	 * @param media
	 *            the media to set
	 */
	public void setMedia(String media) {
		this.media = media;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getPirated() {
		return pirated;
	}

	public void setPirated(String pirated) {
		this.pirated = pirated;
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

	public String getManual() {
		return manual;
	}

	public void setManual(String manual) {
		this.manual = manual;
	}

	public List<String> getCountries() {
		return countries;
	}

	public void setCountries(List<String> countries) {
		this.countries = countries;
	}

	public void addCountry(String country) {
		this.countries.add(country);
	}

}
