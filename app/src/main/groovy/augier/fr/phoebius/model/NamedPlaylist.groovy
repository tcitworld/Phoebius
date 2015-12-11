package augier.fr.phoebius.model;


public class NamedPlaylist extends Playlist
{
    private String name

    NamedPlaylist(String name, List<Song> songs = [])
    {
        this.name = name
        this.songs.addAll(songs)
    }

    String getName(){ return name }

    @Override boolean equals(o)
    {
        if(this.is(o)) return true
        if(getClass() != o.class) return false

        NamedPlaylist songs = (NamedPlaylist)o

        if(name != songs.name) return false

        return true
    }

    @Override int hashCode(){ return name.hashCode() }
}