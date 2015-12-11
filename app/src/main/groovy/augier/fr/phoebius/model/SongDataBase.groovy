package augier.fr.phoebius.model


import groovy.transform.CompileStatic;

@CompileStatic
public abstract class SongDataBase
{
    protected static Playlist allSongs = []
    protected static AlbumList allAlbums = []

    static Playlist getAllSongs(){ return allSongs }
    static AlbumList getAllAlbums(){ return allAlbums }
}
