package augier.fr.phoebius.model;


public class AlbumList implements List<Album>
{
	private ArrayList<Album> albums = []

	AlbumList(List<Album> albums=[]){ this.addAll(albums) }

	public void sort(){ albums.sort{ it.albumTitle } }

    //region OVERRIDE
	@Override public void add(int i, Album album){ albums.add(i, album) }

	@Override public boolean add(Album album){ return albums.add(album) }

	@Override public boolean addAll(int i, Collection<? extends Album> c){ albums.addAll(i, c) }

	@Override public boolean addAll(Collection<? extends Album> c){ return albums.addAll(c)}

	@Override public void clear(){ albums.clear() }

	@Override public boolean contains(Object o){ return albums.contains(o) }

	@Override public boolean containsAll(Collection<?> c){ return albums.containsAll(c) }

	@Override public Album get(int i){ return albums.get(i) }

	@Override public int indexOf(Object o){ return albums.indexOf(o) }

	@Override public boolean isEmpty(){ return albums.isEmpty() }

	@Override public Iterator<Album> iterator(){ return albums.iterator() }

	@Override public int lastIndexOf(Object o){ return albums.lastIndexOf(o) }

	@Override public ListIterator<Album> listIterator(){ return albums.listIterator() }

	@Override public ListIterator<Album> listIterator(int i){ return albums.listIterator(i) }

	@Override public Album remove(int i){ return albums.remove(i) }

	@Override public boolean remove(Object o){ return albums.remove(o) }

	@Override public boolean removeAll(Collection<?> c){ return albums.removeAll(c) }

	@Override public boolean retainAll(Collection<?> c){ return albums.retainAll(c)}

	@Override public Album set(int i, Album album){ return albums.set(i, album) }

	@Override public int size(){ return albums.size() }

	@Override public AlbumList subList(int i, int i1){ return albums.subList(i, i1) }

	@Override public Object[] toArray(){ return albums.toArray() }

	@Override public <T> T[] toArray(T[] ts){ return albums.toArray(ts) }
    //endregion
}
