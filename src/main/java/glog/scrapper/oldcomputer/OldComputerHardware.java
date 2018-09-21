package glog.scrapper.oldcomputer;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "hardware")
@XmlAccessorType(XmlAccessType.FIELD)
public class OldComputerHardware {
	@XmlAttribute
	private String type;

	@XmlElementWrapper(name = "items")
	private List<OldComputerHardwareItem> item = new ArrayList<OldComputerHardwareItem>();

	public List<OldComputerHardwareItem> getItem() {
		return item;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void addItem(OldComputerHardwareItem item) {
		this.item.add(item);
	}

}
