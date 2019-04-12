package glog.scrapper.generationmsx;

import java.util.ArrayList;
import java.util.List;

public class GenerationMSXCompany {
	private String url;
	private String name;
	private String fullName;
	private String address;
	private String subCompanies;
	private String labels;
	private String type;
	private String country;
	private String website;
	private String foundedIn;
	private String mainCompany;
	private String softwareNotes;
	private String hardwareNotes;
	private List<GenerationMSXGameMedia> medias = new ArrayList<GenerationMSXGameMedia>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSubCompanies() {
		return subCompanies;
	}

	public void setSubCompanies(String subCompanies) {
		this.subCompanies = subCompanies;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getFoundedIn() {
		return foundedIn;
	}

	public void setFoundedIn(String foundedIn) {
		this.foundedIn = foundedIn;
	}

	public String getMainCompany() {
		return mainCompany;
	}

	public void setMainCompany(String mainCompany) {
		this.mainCompany = mainCompany;
	}

	public String getSoftwareNotes() {
		return softwareNotes;
	}

	public void setSoftwareNotes(String softwareNotes) {
		this.softwareNotes = softwareNotes;
	}

	public String getHardwareNotes() {
		return hardwareNotes;
	}

	public void setHardwareNotes(String hardwareNotes) {
		this.hardwareNotes = hardwareNotes;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}