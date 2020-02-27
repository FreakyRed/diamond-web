package com.company.gamestudio.game.core;

import java.util.ArrayList;
import java.util.List;

public class Square extends Tile {

    private List<Piece> pieces;

    Square(){

    }

    public Square(List<Piece> listOfPieces){
        pieces = listOfPieces;
    }

    @Override
    public List<Piece> getPieces() {
        return this.pieces;
    }

    @Override
    protected void addPieces(Piece[] pieces) {
        for(int i = 0; i<this.pieces.size() ; i++){
            this.pieces.set(i,pieces[i]);
        }
    }


    public List<Piece> fillListWithPieces() {
        List<Piece> newList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            newList.add(new Piece());
        }
        return newList;
    }
}
