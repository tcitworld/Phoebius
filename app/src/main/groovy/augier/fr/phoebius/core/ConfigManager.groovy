package augier.fr.phoebius.core


import android.os.Environment
import augier.fr.phoebius.MainActivity
import augier.fr.phoebius.PhoebiusApplication
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic


/**
 * This class takes care of centralizing the whole configuration of the application
 *
 * It creates a hidden directory with the name of the application and store the
 * configuration as a JSON. When the application is started, the configuration is
 * automatically loaded from the file.
 */
@CompileStatic
abstract class ConfigManager
{
	/**
	 * Shorthand for the separator character in paths
	 */
	private static File configFile
	private static def configs = [:]

	static {
		configFile = new File(getConfDir(), "configs.json")
		if(!configFile.exists()) configFile.createNewFile()
		configs = new JsonSlurper().parse(configFile) as LinkedHashMap
	}

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
	private static File getConfDir()
	{
		def E = Environment.externalStorageDirectory.absolutePath
		def A = PhoebiusApplication.APP_NAME

		def homeAppDir = new File("${E}", ".${A}")
		if(!homeAppDir.exists()) homeAppDir.mkdir()
		return homeAppDir
	}

	public static void addKey(String k, String v)
		{ configs[k] = new JsonSlurper().parseText(v).toString() }
	public static void dump(){ configFile.write(new JsonBuilder(configs).toString()) }
	public static String getJson(){ return new JsonBuilder(configs).toPrettyString() }
	public static getKey(String k){ return configs[k] }
}