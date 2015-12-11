package augier.fr.phoebius.model


import groovy.transform.CompileStatic

@CompileStatic
public class NamedPlaylistsSet implements Map<String, Playlist>
{
    private LinkedHashMap<String, Playlist> playlisSet = [:]

    NamedPlaylistsSet(Map<? extends String, ? extends Playlist> map = [:]){ playlisSet.putAll(map) }

    @Override void clear(){ playlisSet.clear() }

    @Override boolean containsKey(Object o){ playlisSet.containsKey(o) }

    @Override boolean containsValue(Object o){ playlisSet.containsValue(o) }

    @Override Set<Map.Entry<String, Playlist>> entrySet(){ return playlisSet.entrySet() }

    @Override Playlist get(Object o){ return playlisSet.get(o) }

    @Override boolean isEmpty(){ return playlisSet.isEmpty() }

    @Override Set<String> keySet(){ return playlisSet.keySet() }

    @Override Playlist put(String s, Playlist songs)
    {
        if(playlisSet.containsKey(s)) return null
        return playlisSet.put(s, songs)
    }

    @Override void putAll(Map<? extends String, ? extends Playlist> map)
    {
        map.each{
            this.put(it.key, it.value)
        }
    }

    @Override Playlist remove(Object o){ return playlisSet.remove(o) }

    @Override int size(){ return playlisSet.size() }

    @Override Collection<Playlist> values(){ return playlisSet.values() }
}
