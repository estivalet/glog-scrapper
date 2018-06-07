package glog.scrapper.mobygames;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "release-info")
public class MobyGameReleaseInfo {
    private String publisher;
    private String developer;
    private String ported;
    private String country;
    private String releaseDate;
    private String ean13;
    private String comments;

    /**
     * @return the publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * @param publisher
     *            the publisher to set
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * @return the developer
     */
    public String getDeveloper() {
        return developer;
    }

    /**
     * @param developer
     *            the developer to set
     */
    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    /**
     * @return the ported
     */
    public String getPorted() {
        return ported;
    }

    /**
     * @param ported
     *            the ported to set
     */
    public void setPorted(String ported) {
        this.ported = ported;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country
     *            the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the releaseDate
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * @param releaseDate
     *            the releaseDate to set
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param comments
     *            the comments to set
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * @return the ean13
     */
    public String getEan13() {
        return ean13;
    }

    /**
     * @param ean13
     *            the ean13 to set
     */
    public void setEan13(String ean13) {
        this.ean13 = ean13;
    }

}
