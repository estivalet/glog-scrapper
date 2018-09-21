package glog.scrapper.segaretro;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "game")
public class SegaRetroGame {
    private String name;
    private String publisher;
    private String developer;
    private String systems;
    private String romSize;
    private String genre;
    private String players;
    private String link;
    private List<SegaRetroGameRelease> release = new ArrayList<SegaRetroGameRelease>();
    private List<String> shots = new ArrayList<String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getSystems() {
        return systems;
    }

    public void setSystems(String systems) {
        this.systems = systems;
    }

    public String getRomSize() {
        return romSize;
    }

    public void setRomSize(String romSize) {
        this.romSize = romSize;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPlayers() {
        return players;
    }

    public void setPlayers(String players) {
        this.players = players;
    }

    /**
     * @return the release
     */
    @XmlElementWrapper(name = "releases")
    public List<SegaRetroGameRelease> getRelease() {
        return release;
    }

    /**
     * @param release
     *            the release to set
     */
    public void setRelease(List<SegaRetroGameRelease> release) {
        this.release = release;
    }

    public void addRelease(SegaRetroGameRelease release) {
        this.release.add(release);
    }

    public List<String> getShots() {
        return shots;
    }

    public void setShots(List<String> shots) {
        this.shots = shots;
    }

    public void addShot(String shot) {
        this.shots.add(shot);
    }

    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link
     *            the link to set
     */
    public void setLink(String link) {
        this.link = link;
    }

}
