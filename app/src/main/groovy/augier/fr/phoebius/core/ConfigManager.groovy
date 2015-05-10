package augier.fr.phoebius.core


import android.os.Environment
import android.util.Log
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
class ConfigManager
{
	// Well known keys
	public static final String WKK_PLAYLIST = "playlists"
	/**
	 * Shorthand for the separator character in paths
	 */
	private Map configs = [:]

	ConfigManager()
	{
		if(!configFile.exists()) configFile.createNewFile()
		else
		{
			try{ configs  = new JsonSlurper().parse(configFile) as Map }
			catch(Exception e)
			{
				Log.e(this.class.toString(), "Enable to parse file: ${e}")
				flushFile()
			}

		}
	}

	public void addValue(String k, String v){ configs[k] = v }
	public void addValue(String k, Map v){ configs[k] = v }
	public void addValue(String k, List v){ configs[k] = v }

	public Object getAt(String k){ return configs[k] }

	private flushFile(){ configFile.write("{}") }
	public void dump(){ configFile.write(new JsonBuilder(configs).toString()) }

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

	private static File getConfigFile(){ return new File(getConfDir(), "configs.json") }
}