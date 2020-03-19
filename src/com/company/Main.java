package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        MovieTree movieTree=new MovieTree();

        try{
            BufferedReader br = new BufferedReader(new FileReader(new File("/Users/xhuljantinaj/Desktop/ml-latest-small/movies.csv")));

            String line;

            while( (line=br.readLine())!=null) {

                String[] lineArr = line.split("[,()|]", -1);
                int movieID = 0;
                String title = "";
                int releaseYear = 0;
                ArrayList<String> genres = new ArrayList<String>();

                if (lineArr.length > 3) {
                    try {
                        Integer.parseInt(lineArr[2]);
                        for (int i = 0; i < lineArr.length; i++) {
                            if (i == 0)
                                movieID = Integer.parseInt(lineArr[0]);
                            if (i == 1)
                                title = lineArr[i];
                            if (i == 2)
                                releaseYear = Integer.parseInt(lineArr[i]);
                            if (i > 2 && !lineArr[i].equalsIgnoreCase(""))
                                genres.add(lineArr[i]);
                        }
                    } catch (NumberFormatException e) {

                        //if there is a issue with the formatting then that line will be skipped

                        }

                    if(movieID!=0) {
                        Movie tempMovie = new Movie(movieID, title, releaseYear, genres);
                        movieTree.add(tempMovie);

                    }


                }

            }

        }catch(FileNotFoundException e){
            System.out.println("File Does Not Exist");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

class Node{
    Movie val;
    Node left, right;

    Node(Movie movie){
        this.val=movie;
        left=null;
        right=null;
    }
}

class MovieTree{

    private int size=0;
    static private Node root;

    MovieTree(Movie newMovie){
        root=new Node(newMovie);
    }

    MovieTree(){
        root=null;
    }

    public int getSize(){
        return size;
    }

    boolean add(Movie newMovie){
        if(this.root==null){
            this.root= new Node(newMovie);
            size++;
        }

        Node parent=null;
        Node current=root;

        while(current!=null){
            if(newMovie.getTitle().compareToIgnoreCase(current.val.getTitle())<0){
                parent=current;
                current=current.left;
            }else if(newMovie.getTitle().compareToIgnoreCase(current.val.getTitle())>0){
                parent=current;
                current=current.right;
            }else{
                return false;
            }
        }

        if(newMovie.getTitle().compareToIgnoreCase(parent.val.getTitle())<0){
            parent.left=new Node(newMovie);
        }else{
            parent.right=new Node(newMovie);
        }

        size++;
        return true;
    }

    public Node getRoot(){return root;}

    public boolean contains(String movie){
        Node current=root;

        while (current != null){
            if(movie.compareToIgnoreCase(current.val.getTitle())<0){
                current=current.left;;
            }else if(movie.compareToIgnoreCase(current.val.getTitle())>0){
                current=current.right;
            }else{
                return true;
            }
        }
        return false;
    }
}



class Movie implements Comparable<Movie>{
    private int movieID;
    private String title;
    private int titleHash;
    private int releaseYear;
    private ArrayList<String> genres = new ArrayList<String>();

    public Movie(int movieID, String title,int releaseYear, ArrayList<String> genres){
        this.title=title;
        this.releaseYear=releaseYear;
        this.movieID=movieID;
        this.genres=genres;
        this.titleHash=title.hashCode();
    }

    public int getTitleHash(){return titleHash;}
    public String getTitle(){ return title;}
    public int getReleaseYear(){ return releaseYear;}

    public String toString(){
        String temp=movieID+" "+title+releaseYear;
        for(int i=0;i<genres.size();i++){
            temp+=" "+genres.get(i);
        }

        return temp;
    }

    @Override
    public int compareTo(Movie other) {
        return this.getTitle().compareToIgnoreCase(other.getTitle());

    }
}

