package model;

import android.content.Context;
import android.view.View;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board{

    public final int size;


    public int numOfMoves;

    public Board board;

    public final List<Place> places;

    public final List<BoardChangeListener> listeners;

    public final static Random random = new Random();

//Plansza
    public Board(int size) {

       listeners = new ArrayList<BoardChangeListener>();
       this.size = size;
        places = new ArrayList<Place>(size * size);
        //Dodawania kafelków na plansze
        for (int x = 1; x <= size; x++) {
            for (int y = 1; y <= size; y++) {
                       places.add(x == size && y == size ?
                              new Place(x, y, this)
                               : new Place(x, y, (y-1)*size + x, this));
            }
        }
    }

//Różnorakie zmienianie kafelków przy każdym odpaleniu
    public void rearrange() {
        for (int i = 0; i < size*size; i++) {
           swapTiles();
        }
       do {
           swapTiles();
        } while (!solvable() || solved());
   }

//Zamiana kafelek
    public void swapTiles() {
       Place p1 = at(random.nextInt(size) + 1 , random.nextInt(size) + 1);
       Place p2 = at(random.nextInt(size) + 1, random.nextInt(size) + 1);

       if (p1 != p2) {
            Tile t = p1.getTile();
            p1.setTile(p2.getTile());
            p2.setTile(t);
        }
    }

//Możliwe do rozwiązania plansza
    private boolean solvable() {

        int inversion = 0;
        for (Place p: places) {
            Tile pt = p.getTile();
            for (Place q: places) {
                Tile qt = q.getTile();
                if (p != q && pt != null && qt != null &&
                        indexOf(p) < indexOf(q) &&
                        pt.number() > qt.number()) {
                    inversion++;
                }
            }
        }
        final boolean isEvenSize = size % 2 == 0;
        final boolean isEvenInversion = inversion % 2 == 0;
        boolean isBlankOnOddRow = blank().getY() % 2 == 1;

        isBlankOnOddRow = isEvenSize ? !isBlankOnOddRow : isBlankOnOddRow;
        return (!isEvenSize && isEvenInversion) ||
                (isEvenSize && isBlankOnOddRow == isEvenInversion);
    }

//zwrócenie pierwszego indexu p
    private int indexOf(Place p) {
        return (p.getY()-1) * size + p.getX();

    }
//Rozwiązana plansza
    public boolean solved() {
        boolean result = true;
        for (Place p: places) {
            result = result &&
                    ((p.getX() == size && p.getY() == size) ||
                            (p.getTile() != null &&
                                    p.getTile().number() == indexOf(p)));
        }
        return result;
    }

//Poruszanie sie po planszy
    public void slide(Tile tile) {
        for (Place p: places) {
            if (p.getTile() == tile) {
                final Place aa = blank();
                aa.setTile(tile);
                p.setTile(null);

                //if (solved()) {
                //    notifyPuzzleSolved(numOfMoves);
                //}
                return;
            }
        }
    }

    public void slideG(Tile tile) {
        for (Place p: places) {
            if (p.getTile() == null) {
                final Place bb = notblank(tile);
                bb.setTile(null);
                p.setTile(tile);

                return;
            }
        }
    }


//Czy możliwe jest poruszanie
    public boolean slidable(Place place) {


        int x = place.getX();
        int y = place.getY();
        return isBlank(x - 1, y) || isBlank(x + 1, y)
                || isBlank(x, y - 1) || isBlank(x, y + 1);
    }

    public boolean slidableDown(Place place){
        int x = place.getX();
        int y = place.getY();
        return isBlank(x, y + 1);
    }

    public boolean slidableUp(Place place){
        int x = place.getX();
        int y = place.getY();
        return isBlank(x, y - 1);
    }

    public boolean slidableLeft(Place place){
        int x = place.getX();
        int y = place.getY();
        return isBlank(x - 1, y);
    }

    public boolean slidableRight(Place place){
        int x = place.getX();
        int y = place.getY();
        return isBlank(x + 1, y);
        }


    //Czy jest puste miejsce
    private boolean isBlank(int x, int y) {
        return (0 < x && x <= size) && (0 < y && y <= size)
                && at(x,y).getTile() == null;
    }

    public boolean isTile(int x, int y){
        return (0 < x && x <= size) && (0 < y && y <= size)
                && at(x,y).getTile() != null;
    }

    //puste miejsce
    public Place blank() {
        for (Place p: places) {
           if (p.getTile() == null) {
                return p;
            }
        }
       return null;
    }


    public Place notblank(Tile tile){
        for (Place p: places) {
            if (p.getTile() == tile) {
                return p;
            }
        }
        return null;
    }


    public Iterable<Place> places() {
        return places;
    }

    public Place at(int x, int y) {
        for (Place p: places) {
            if (p.getX() == x && p.getY() == y) {
                return p;
            }
        }
       return null;
    }

    //rozmiar
    public int size() {
        return size;
    }

    public void addBoardChangeListener(BoardChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
//Powiadomienie o rozwiązaniu układanki
   private void notifyPuzzleSolved(int numOfMoves) {
        for (BoardChangeListener listener: listeners) {
            listener.solved(numOfMoves);
        }
    }

    public interface BoardChangeListener {

        void solved(int numOfMoves);
    }
}
