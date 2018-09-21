package glog.scrapper.oldcomputer;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "computer")
public class OldComputer {
	private String name;
	private String manufacturer;
	private String type;
	private String origin;
	private String year;
	private String production;
	private String language;
	private String keyboard;
	private String cpu;
	private String speed;
	private String coprocessor;
	private String ram;
	private String vram;
	private String rom;
	private String text;
	private String graphics;
	private String colors;
	private String sound;
	private String size;
	private String ports;
	private String power;
	private String price;
	private String description;
	private String url;
	private List<OldComputerShot> shot = new ArrayList<OldComputerShot>();
	private List<OldComputerAdvert> advert = new ArrayList<OldComputerAdvert>();
	private String hardwareDescription;
	private List<OldComputerHardware> hardware = new ArrayList<OldComputerHardware>();
	private List<OldComputerEmulator> emulator = new ArrayList<OldComputerEmulator>();
	private List<String> link = new ArrayList<String>();
	private String image;

	private String builtInGames;
	private String controllers;
	private String media;
	private String numGames;
	private String peripherals;

	private String switches;
	private String batteries;
	private String buttons;
	private String gun;

	/**
	 * @return the release
	 */
	@XmlElementWrapper(name = "hardwares")
	public List<OldComputerHardware> getHardware() {
		return hardware;
	}

	/**
	 * @return the release
	 */
	@XmlElementWrapper(name = "emulators")
	public List<OldComputerEmulator> getEmulator() {
		return emulator;
	}

	public void addEmulator(OldComputerEmulator emulator) {
		this.emulator.add(emulator);
	}

	public void addHardware(OldComputerHardware hardware) {
		this.hardware.add(hardware);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getProduction() {
		return production;
	}

	public void setProduction(String production) {
		this.production = production;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getKeyboard() {
		return keyboard;
	}

	public void setKeyboard(String keyboard) {
		this.keyboard = keyboard;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getCoprocessor() {
		return coprocessor;
	}

	public void setCoprocessor(String coprocessor) {
		this.coprocessor = coprocessor;
	}

	public String getRam() {
		return ram;
	}

	public void setRam(String ram) {
		this.ram = ram;
	}

	public String getVram() {
		return vram;
	}

	public void setVram(String vram) {
		this.vram = vram;
	}

	public String getRom() {
		return rom;
	}

	public void setRom(String rom) {
		this.rom = rom;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getGraphics() {
		return graphics;
	}

	public void setGraphics(String graphics) {
		this.graphics = graphics;
	}

	public String getColors() {
		return colors;
	}

	public void setColors(String colors) {
		this.colors = colors;
	}

	public String getSound() {
		return sound;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getPorts() {
		return ports;
	}

	public void setPorts(String ports) {
		this.ports = ports;
	}

	public String getPower() {
		return power;
	}

	public void setPower(String power) {
		this.power = power;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the screenshots
	 */
	@XmlElementWrapper(name = "screenshots")
	public List<OldComputerShot> getShot() {
		return shot;
	}

	/**
	 * @param screenshots
	 *            the screenshots to set
	 */
	public void setShot(List<OldComputerShot> shot) {
		this.shot = shot;
	}

	public void addScreenshot(OldComputerShot shotLink) {
		this.shot.add(shotLink);
	}

	/**
	 * @return the screenshots
	 */
	@XmlElementWrapper(name = "adverts")
	public List<OldComputerAdvert> getAdvert() {
		return advert;
	}

	/**
	 * @param screenshots
	 *            the screenshots to set
	 */
	public void setAdvert(List<OldComputerAdvert> advert) {
		this.advert = advert;
	}

	public void addAdvert(OldComputerAdvert advertLink) {
		this.advert.add(advertLink);
	}

	public String getHardwareDescription() {
		return hardwareDescription;
	}

	public void setHardwareDescription(String hardwareDescription) {
		this.hardwareDescription = hardwareDescription;
	}

	/**
	 * @return the screenshots
	 */
	@XmlElementWrapper(name = "links")
	public List<String> getLink() {
		return link;
	}

	/**
	 * @param screenshots
	 *            the screenshots to set
	 */
	public void setLink(List<String> link) {
		this.link = link;
	}

	public void addLink(String link) {
		this.link.add(link);
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getBuiltInGames() {
		return builtInGames;
	}

	public void setBuiltInGames(String builtInGames) {
		this.builtInGames = builtInGames;
	}

	public String getControllers() {
		return controllers;
	}

	public void setControllers(String controllers) {
		this.controllers = controllers;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}

	public String getNumGames() {
		return numGames;
	}

	public void setNumGames(String numGames) {
		this.numGames = numGames;
	}

	public String getPeripherals() {
		return peripherals;
	}

	public void setPeripherals(String peripherals) {
		this.peripherals = peripherals;
	}

	public String getSwitches() {
		return switches;
	}

	public void setSwitches(String switches) {
		this.switches = switches;
	}

	public String getBatteries() {
		return batteries;
	}

	public void setBatteries(String batteries) {
		this.batteries = batteries;
	}

	public String getButtons() {
		return buttons;
	}

	public void setButtons(String buttons) {
		this.buttons = buttons;
	}

	public String getGun() {
		return gun;
	}

	public void setGun(String gun) {
		this.gun = gun;
	}

}
