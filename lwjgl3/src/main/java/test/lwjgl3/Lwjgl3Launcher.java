package test.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import test.Main;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
	public static void main(String[] args) {

		String host;
		if (args.length > 0) host = args[0];
		else host = "127.0.0.1";

		int port;
		if (args.length > 1) port = Integer.parseInt(args[1]);
		else port = 35000;
		createApplication(host, port);
	}

	private static Lwjgl3Application createApplication(String host, int port) {
		return new Lwjgl3Application(new Main(host, port), getDefaultConfiguration());
	}

	private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
		Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
		configuration.setTitle("pacman");
		configuration.setWindowedMode(640, 480);
		configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
		return configuration;
	}
}