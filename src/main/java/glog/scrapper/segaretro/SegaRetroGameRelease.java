package glog.scrapper.segaretro;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "release")
public class SegaRetroGameRelease {
	private String country;
	private String date;
	private String price;
	private String code;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
