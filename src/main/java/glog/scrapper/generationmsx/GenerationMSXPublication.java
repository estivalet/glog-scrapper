package glog.scrapper.generationmsx;

import java.util.ArrayList;
import java.util.List;

public class GenerationMSXPublication {
	private String url;
	private String title;
	private String publisher;
	private String publication;
	private String type;
	private String author;
	private String isbn10;
	private String language;
	private String format;
	private String pages;
	private String price;
	private String link;
	private String mentions;
	private String hardware;
	private String note;
	private List<GenerationMSXGameMedia> medias = new ArrayList<GenerationMSXGameMedia>();

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getPublication() {
		return publication;
	}

	public void setPublication(String publication) {
		this.publication = publication;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIsbn10() {
		return isbn10;
	}

	public void setIsbn10(String isbn10) {
		this.isbn10 = isbn10;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getPages() {
		return pages;
	}

	public void setPages(String pages) {
		this.pages = pages;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getMentions() {
		return mentions;
	}

	public void setMentions(String mentions) {
		this.mentions = mentions;
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

	public String getHardware() {
		return hardware;
	}

	public void setHardware(String hardware) {
		this.hardware = hardware;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
