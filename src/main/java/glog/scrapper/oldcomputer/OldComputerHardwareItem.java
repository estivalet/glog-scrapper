package glog.scrapper.oldcomputer;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "item")
public class OldComputerHardwareItem {
	private String photo;
	private String description;

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
