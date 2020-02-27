package com.company.gamestudio.game.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Triangle extends Tile {

    private List<Piece> pieces = new ArrayList<>();

    public Triangle(){

    }

    public Triangle(List<Piece> pieces){
        this.pieces = pieces;
    }

    @Override
    public List<Piece> getPieces() {
        return pieces;
    }

    @Override
    protected void addPieces(Piece[] pieces) {
        for(int i = 0 ; i<this.pieces.size() ; i++){
            this.pieces.set(i,pieces[i]);
        }
    }

    public List<Piece> fillListWithPieces() {
        List<Piece> newList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            newList.add(new Piece());
        }
        return newList;
    }
}
