package augier.fr.phoebius.core

import android.os.Environment
import groovy.json.JsonBuilder
import augier.fr.phoebius.MainActivity


/**
 * This class takes care of centralizing the whole configuration of the application
 *
 * It creates a hidden directory with the name of the application and store the
 * configuration as a JSON. When the application is started, the configuration is
 * automatically loaded from the file.
 */
public class ConfigManager
{
	/**
	 * Shorthand for the separator character in paths
	 */
	public static final S = File.separator

	/**
	 * Aquire the directory containing the configuration file
	 *
	 * If the directory does not exists, then creates it. The
	 * configuration directory is a hidden directory in the user's
	 * home whose name corresponds to the name of the applications
	 * according to the Linux' configurations conventions.
	 *
	 * @return Directory containing the configuration file
	 */
	public static final File getConfDir()
	{
		def E = Environment.externalStorageDirectory.absolutePath
		def A = MainActivity.APP_NAME

		def homeAppDir = new File("${E}/.${A}")
		if(!homeAppDir.exists()){ homeAppDir.mkdir() }
		
		return homeAppDir
	}

	public ConfigManager()
	{
		def builder = new JsonBuilder()

		builder{}

		def file = new File("${confDir}${S}config.json")
		file.write(builder.toPrettyString())
	}
}