package augier.fr.phoebius.core

import android.os.Environment
import groovy.json.JsonBuilder
import augier.fr.phoebius.MainActivity

public class ConfigManager
{
	public static final S = File.separator
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