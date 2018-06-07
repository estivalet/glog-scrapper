package glog.scrapper.mobygames;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;

public class MobyGameRelease {
	private String system;
	private List<MobyGameReleaseInfo> releases = new ArrayList<MobyGameReleaseInfo>();

	/**
	 * @return the system
	 */
	@XmlAttribute
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
	 * @return the releases
	 */
	@XmlElementWrapper(name = "releases-info")
	public List<MobyGameReleaseInfo> getReleases() {
		return releases;
	}

	/**
	 * @param releases
	 *            the releases to set
	 */
	public void setReleases(List<MobyGameReleaseInfo> releases) {
		this.releases = releases;
	}

	public void addReleaseInfo(MobyGameReleaseInfo release) {
		this.releases.add(release);
	}

}
