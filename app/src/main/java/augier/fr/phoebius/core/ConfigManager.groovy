package augier.fr.phoebius.core
import android.os.Environment
import augier.fr.phoebius.MainActivity
import groovy.json.JsonBuilder

class ConfigManager
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

		def root = builder.people {
			person {
				firstName 'Guillame'
				lastName 'Laforge'
				// Named arguments are valid values for objects too
				address(
						city: 'Paris',
						country: 'France',
						zip: 12345,
						)
				married true
				// a list of values
				conferences 'JavaOne', 'Gr8conf'
			}
		}
		def file = new File("${confDir}${S}config.json")
		file.write(builder.toPrettyString())
	}
}