package glog.scrapper.generationmsx;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "game")
public class GenerationMSXGame {
    private String originalTitle;
    private String developedBy;
    private String year;
    private String system;
    private String sound;
    private String kind;
    private String inputDevicesSupported;
    private String genre;
    private String maxPlayers;
    private String maxSimultaneous;
    private String usesKanji;
    private String license;
    private String licence;
    private String language;
    private String conversion;
    private String note;
    private String tagooDatabaseEntry;
    private String mentionedIn;
    private List<GenerationMSXGameRelease> release = new ArrayList<GenerationMSXGameRelease>();

    /**
     * @return the originalTitle
     */
    public String getOriginalTitle() {
        return originalTitle;
    }

    /**
     * @param originalTitle
     *            the originalTitle to set
     */
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    /**
     * @return the developedBy
     */
    public String getDevelopedBy() {
        return developedBy;
    }

    /**
     * @param developedBy
     *            the developedBy to set
     */
    public void setDevelopedBy(String developedBy) {
        this.developedBy = developedBy;
    }

    /**
     * @return the year
     */
    public String getYear() {
        return year;
    }

    /**
     * @param year
     *            the year to set
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * @return the system
     */
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
     * @return the sound
     */
    public String getSound() {
        return sound;
    }

    /**
     * @param sound
     *            the sound to set
     */
    public void setSound(String sound) {
        this.sound = sound;
    }

    /**
     * @return the kind
     */
    public String getKind() {
        return kind;
    }

    /**
     * @param kind
     *            the kind to set
     */
    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     * @return the inputDevicesSupported
     */
    public String getInputDevicesSupported() {
        return inputDevicesSupported;
    }

    /**
     * @param inputDevicesSupported
     *            the inputDevicesSupported to set
     */
    public void setInputDevicesSupported(String inputDevicesSupported) {
        this.inputDevicesSupported = inputDevicesSupported;
    }

    /**
     * @return the genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * @param genre
     *            the genre to set
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * @return the maxPlayers
     */
    public String getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * @param maxPlayers
     *            the maxPlayers to set
     */
    public void setMaxPlayers(String maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    /**
     * @return the maxSimultaneous
     */
    public String getMaxSimultaneous() {
        return maxSimultaneous;
    }

    /**
     * @param maxSimultaneous
     *            the maxSimultaneous to set
     */
    public void setMaxSimultaneous(String maxSimultaneous) {
        this.maxSimultaneous = maxSimultaneous;
    }

    /**
     * @return the usesKanji
     */
    public String getUsesKanji() {
        return usesKanji;
    }

    /**
     * @param usesKanji
     *            the usesKanji to set
     */
    public void setUsesKanji(String usesKanji) {
        this.usesKanji = usesKanji;
    }

    /**
     * @return the license
     */
    public String getLicense() {
        return license;
    }

    /**
     * @param license
     *            the license to set
     */
    public void setLicense(String license) {
        this.license = license;
    }

    /**
     * @return the licence
     */
    public String getLicence() {
        return licence;
    }

    /**
     * @param licence
     *            the licence to set
     */
    public void setLicence(String licence) {
        this.licence = licence;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language
     *            the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the conversion
     */
    public String getConversion() {
        return conversion;
    }

    /**
     * @param conversion
     *            the conversion to set
     */
    public void setConversion(String conversion) {
        this.conversion = conversion;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note
     *            the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return the tagooDatabaseEntry
     */
    public String getTagooDatabaseEntry() {
        return tagooDatabaseEntry;
    }

    /**
     * @param tagooDatabaseEntry
     *            the tagooDatabaseEntry to set
     */
    public void setTagooDatabaseEntry(String tagooDatabaseEntry) {
        this.tagooDatabaseEntry = tagooDatabaseEntry;
    }

    /**
     * @return the mentionedIn
     */
    public String getMentionedIn() {
        return mentionedIn;
    }

    /**
     * @param mentionedIn
     *            the mentionedIn to set
     */
    public void setMentionedIn(String mentionedIn) {
        this.mentionedIn = mentionedIn;
    }

    /**
     * @return the release
     */
    @XmlElementWrapper(name = "releases")
    public List<GenerationMSXGameRelease> getRelease() {
        return release;
    }

    /**
     * @param release
     *            the release to set
     */
    public void setRelease(List<GenerationMSXGameRelease> release) {
        this.release = release;
    }

    public void addRelease(GenerationMSXGameRelease release) {
        this.release.add(release);
    }

}
