package glog.scrapper.oldcomputer;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import glog.util.XMLUtil;
import glog.util.XMLWrapper;

public class ConvertXMLToDokuWiki {

    public static void main(String[] args) throws Exception {
        JAXBContext jc = JAXBContext.newInstance(XMLWrapper.class, OldComputer.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        List<OldComputer> oc = XMLUtil.unmarshal(unmarshaller, OldComputer.class, "/home/lestivalet/git/glog-service/oldcomputer/consoles/1241.xml");
        OldComputer c = oc.get(0);

        System.out.println("====== " + c.getManufacturer() + " " + c.getName() + " ======\n\n");
        System.out.println(c.getDescription() + "\n\n");
        System.out.println("^ Spec      ^ Info ^");
        if (c.getName() != null && !c.getName().equals("")) {
            System.out.println("|Name|" + c.getName() + "|");
        }
        if (c.getManufacturer() != null && !c.getManufacturer().equals("")) {
            System.out.println("|Manufacturer|" + c.getManufacturer() + "|");
        }
        if (c.getType() != null && !c.getText().equals("")) {
            System.out.println("|Type|" + c.getType() + "|");
        }
        if (c.getOrigin() != null && !c.getOrigin().equals("")) {
            System.out.println("|Origin|" + c.getOrigin() + "|");
        }
        if (c.getYear() != null && !c.getYear().equals("")) {
            System.out.println("|Year|" + c.getYear() + "|");
        }
        if (c.getProduction() != null && !c.getProduction().equals("")) {
            System.out.println("|Production|" + c.getProduction() + "|");
        }
        if (c.getLanguage() != null && !c.getLanguage().equals("")) {
            System.out.println("|Language|" + c.getLanguage() + "|");
        }
        if (c.getKeyboard() != null && !c.getKeyboard().equals("")) {
            System.out.println("|Keyboard|" + c.getKeyboard() + "|");
        }
        if (c.getCpu() != null && !c.getCpu().equals("")) {
            System.out.println("|CPU|" + c.getCpu() + "|");
        }
        if (c.getSpeed() != null && !c.getSpeed().equals("")) {
            System.out.println("|Speed|" + c.getSpeed() + "|");
        }
        if (c.getCoprocessor() != null && !c.getCoprocessor().equals("")) {
            System.out.println("|Coprocessor|" + c.getCoprocessor() + "|");
        }
        if (c.getRam() != null && !c.getRam().equals("")) {
            System.out.println("|RAM|" + c.getRam() + "|");
        }
        if (c.getVram() != null && !c.getVram().equals("")) {
            System.out.println("|VRAM|" + c.getVram() + "|");
        }
        if (c.getRom() != null && !c.getRom().equals("")) {
            System.out.println("|ROM|" + c.getRom() + "|");
        }
        if (c.getText() != null && !c.getText().equals("")) {
            System.out.println("|Text|" + c.getText() + "|");
        }
        if (c.getGraphics() != null && !c.getGraphics().equals("")) {
            System.out.println("|Graphics|" + c.getGraphics() + "|");
        }
        if (c.getColors() != null && !c.getColors().equals("")) {
            System.out.println("|Colors|" + c.getColors() + "|");
        }
        if (c.getSound() != null && !c.getSound().equals("")) {
            System.out.println("|Sound|" + c.getSound() + "|");
        }
        if (c.getSize() != null && !c.getSize().equals("")) {
            System.out.println("|Size|" + c.getSize() + "|");
        }
        if (c.getPorts() != null && !c.getPorts().equals("")) {
            System.out.println("|Ports|" + c.getPorts() + "|");
        }
        if (c.getPower() != null && !c.getPower().equals("")) {
            System.out.println("|Power|" + c.getPower() + "|");
        }
        if (c.getPrice() != null && !c.getPrice().equals("")) {
            System.out.println("|Price|" + c.getPrice() + "|");
        }
        if (c.getUrl() != null && !c.getUrl().equals("")) {
            System.out.println("|URL|" + c.getUrl() + "|");
        }
        if (c.getHardwareDescription() != null && !c.getHardwareDescription().equals("")) {
            System.out.println("|Hardware|" + c.getHardwareDescription() + "|");
        }
        if (c.getBuiltInGames() != null && !c.getBuiltInGames().equals("")) {
            System.out.println("|Built In Games|" + c.getBuiltInGames() + "|");
        }
        if (c.getControllers() != null && !c.getControllers().equals("")) {
            System.out.println("|Controllers|" + c.getControllers() + "|");
        }
        if (c.getMedia() != null && !c.getMedia().equals("")) {
            System.out.println("|Media|" + c.getMedia() + "|");
        }
        if (c.getNumGames() != null && !c.getNumGames().equals("")) {
            System.out.println("|# Games|" + c.getNumGames() + "|");
        }
        if (c.getPeripherals() != null && !c.getPeripherals().equals("")) {
            System.out.println("|Peripherals|" + c.getPeripherals() + "|");
        }
        if (c.getSwitches() != null && !c.getSwitches().equals("")) {
            System.out.println("|Switches|" + c.getSwitches() + "|");
        }
        if (c.getBatteries() != null && !c.getBatteries().equals("")) {
            System.out.println("|Batteries|" + c.getBatteries() + "|");
        }
        if (c.getButtons() != null && !c.getButtons().equals("")) {
            System.out.println("|Buttons|" + c.getButtons() + "|");
        }
        if (c.getGun() != null && !c.getGun().equals("")) {
            System.out.println("|Gun|" + c.getGun() + "|");
        }

        System.out.println();
        System.out.println();
        System.out.println("====== System Media ======");
        System.out.println("^ Logo ^ Wheel (Classic) ^ Wheel (Carbon) ^ Wheel (Steel) ^");
        System.out.println("|{{:systems:todo.png?50|}}|{{:systems:todo.png?50|}}|{{:systems:todo.png?50|}}|{{:systems:todo.png?50|}}|");
        System.out.println("^ Overlay ^ Marquee ^ 3D Box ^");
        System.out.println("|{{:systems:todo.png?50|}}|{{:systems:todo.png?50|}}|{{:systems:todo.png?50|}}|");
        System.out.println("");
        System.out.println("===== Hyperspin =====");
        System.out.println("");
        System.out.println("^  Theme v1  ^  Theme v2  ^ Theme v3  ^");
        System.out.println("|  {{:systems:todo.png?50|}}  |  {{:systems:todo.png?50|}}  |  {{:systems:todo.png?50|}}  |");
        System.out.println("");
        System.out.println("===== Attract Mode =====");
        System.out.println("");
        System.out.println("^  Theme v1  ^  Theme v2  ^ Theme v3  ^");
        System.out.println("|  {{:systems:todo.png?50|}}  |  {{:systems:todo.png?50|}}  |  {{:systems:todo.png?50|}}  |");
        System.out.println("");
        System.out.println("");
        System.out.println("===== Emulationstation =====");
        System.out.println("");
        System.out.println("^  Theme v1  ^  Theme v2  ^ Theme v3  ^");
        System.out.println("|  {{:systems:todo.png?50|}}  |  {{:systems:todo.png?50|}}  |  {{:systems:todo.png?50|}}  |");
        System.out.println("");
        System.out.println("");
        System.out.println("====== Frontend Integration ======");
        System.out.println("");
        System.out.println("    Instructions to install the system for the following frontends:");
        System.out.println("");
        System.out.println("  * AttractMode");
        System.out.println("  * Emulation Station");
        System.out.println("  * RetroFE");
        System.out.println("  * Hyperspin");
        System.out.println("");
        System.out.println("");
        System.out.println("====== Best Games ======");
        System.out.println("====== Emulators ======");
    }

}
