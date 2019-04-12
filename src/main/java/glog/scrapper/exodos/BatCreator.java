package glog.scrapper.exodos;

import java.io.File;
import java.util.Arrays;

import glog.util.IOUtil;

public class BatCreator {
	public static void main(String[] args) throws Exception {
		String folder = "F:\\EmuDreams\\platforms\\DOS2\\Games\\!dos\\";
		String[] files = new File(folder).list();
		Arrays.sort(files);
		for (String file : files) {
			File dir = new File(folder + file);
			if (dir.isDirectory()) {
				String[] f2 = dir.list();
				for (String game : f2) {
					if (game.endsWith(".bat") && !game.toLowerCase().equals("install.bat")) {
						String name = game.substring(0, game.indexOf(".bat"));
						System.out.println(file + " = " + name);

						IOUtil.write("c:/temp/dos/" + name + ".bat", "cd " + file + "\n\"" + name + "\"");
					}
				}
			}
		}
	}

}
