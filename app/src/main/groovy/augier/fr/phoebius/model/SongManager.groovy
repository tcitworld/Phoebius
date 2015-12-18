package augier.fr.phoebius.model


import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import augier.fr.phoebius.PhoebiusApplication
import augier.fr.phoebius.core.ConfigManager
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.OnBackground
import groovy.transform.CompileStatic


/**
 * This class manages the creation, listing and iteration through the musics
 *
 * This class is a singleton to easier it's use everywhere in the code.
 * It is an abstraction of Andrdoid FS
 */
@CompileStatic
enum SongManager
{
    INSTANCE()

    private NamedPlaylistsSet playlists = [Test: new Playlist()]
    private Long currentSongId
    private boolean loop

    private SongManager()
    {
        Album.query()
        Song.query()
        createPlaylists()
        currentSongId = firstId
        loop = false
    }

    public void addToPlaylist(String name, Song song){ playlists[name].add(song) }

    private void createPlaylists()
    {
        def res = configManager[ConfigManager.WKK_PLAYLIST] as Map
        res?.each{
            String name = it.key as String
            def songs = it.value as List
            playlists[name] = new Playlist()
            songs.each{
                def song = it as Map<String, String>
                playlists[name].add(Song.fromMap(song))
            }
        }
    }

    public void dispose()
    {
        def bckpPl = [:]
        playlists.each{
            def plName = []
            it.value.each{ plName.add(it.toMap()) }
            bckpPl[it.key] = plName
        }
        configManager.addValue(ConfigManager.WKK_PLAYLIST, bckpPl)
    }

    /** Overriding of [] operator */
    public Song getAt(int idx){ return currSongList[idx] }

    public SongManager next()
    {
        currentSongId = nextSong?.ID ?: firstId
        return this
    }

    public SongManager previous()
    {
        currentSongId = previousSong?.ID ?: firstId
        return this
    }

    /**
     * Retrives the index of a Song by its {@link Song#getID() ID}
     * @param id Song ID
     * @return Index in the current list or -1 if not found
     */
    private int findIndexById(Long id)
    {
        return currSongList.findIndexOf{
            Song it -> return it.ID == id
        }
    }

    /**
     * Retrieve a Song by its {@link Song#getID() ID}
     * @param id Song ID
     * @return Song in the current list or null if not found
     */
    private Song findById(Long id)
    {
        int idx = findIndexById(id)
        return idx >= 0 && idx < currSongList.size() ? currSongList[idx] : null
    }

    //region GET/SET
    /** @return Index of the next Song or -1 if no next song    */
    private int getNextSongIdx()
    {
        int currentSongIdx = findIndexById(currentSongId)
        if(currentSongIdx == this.lenght - 1 && !loop)
        { return -1 }
        else
        { return (currentSongIdx + 1) % this.lenght }
    }

    /** @return Index of the previous Song or -1 if no previous song    */
    private int getPreviousSongIdx()
    {
        int currentSongIdx = findIndexById(currentSongId)
        if(currentSongIdx == 0 && !loop)
        { return -1 }
        else
        { return (currentSongIdx + this.lenght - 1) % this.lenght }
    }

    /**
     * Retrives the next song to be played
     *
     * @return The next song in the list or null if last song and
     * the list doesn't loop
     */
    public Song getNextSong()
    {
        int idx = nextSongIdx
        if(idx < 0) return null
        else
        { return currSongList[idx] }
    }

    /**
     * Retrives the previous song to be played
     *
     * @return The next song in the list or null if first song and
     * the list doesn't loop
     */
    public Song getPreviousSong()
    {
        int idx = previousSongIdx
        if(idx < 0) return null
        else return currSongList[idx]
    }

    private ConfigManager getConfigManager(){ return ConfigManager.INSTANCE }

    private Long getFirstId(){ return songList[0]?.ID ?: -1 }

    /** @return The length of the current playing playlist    */
    public int getLenght(){ return currSongList.size() }

    /** @return Whether the playback is looped on the list or not    */
    public boolean isLooped(){ return loop }

    /** Set the playback loop */
    public void setLoop(boolean loop){ this.loop = loop }

    /** @return The current playing list    */
    public Playlist getCurrSongList(){ return Song.allSongs }

    /** @return The current playing song    */
    public Song getCurrentSong(){ return findById(currentSongId) }

    /** Sets the current playing song */
    public void setCurrentSong(Song song){ this.currentSongId = song?.ID ?: getFirstId() }

    /** @return The complete song list    */
    public Playlist getSongList(){ return currSongList }

    public ArrayList<String> getAllPlaylists(){ return playlists.keySet() as ArrayList<String> }

    public Playlist getPlaylist(String name){ return playlists[name] ?: new Playlist() }

    //endregion
}
