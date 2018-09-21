package glog.scrapper.lemonamiga;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "review")
public class LemonAmigaMagazineReview {
	private String magazine;
	private String link;

	public String getMagazine() {
		return magazine;
	}

	public void setMagazine(String magazine) {
		this.magazine = magazine;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
