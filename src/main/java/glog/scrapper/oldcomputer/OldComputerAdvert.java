package glog.scrapper.oldcomputer;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "advert")
public class OldComputerAdvert {
	private String description;
	private String link;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
