package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        MovieTree movieTree=new MovieTree();

        try{
            BufferedReader br = new BufferedReader(new FileReader(new File("ml-latest-small/movies.csv")));

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
                        //might add further functions to reformat lines properly

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

        //testing to see if method will print a subset of binary tree
        movieTree.subSet(movieTree.getRoot(),"Toy Story  ".hashCode(), "Grumpier Old Men ".hashCode());

    }

}

class Node{

    //value being stored in node is of type Movie, accessor methods are used to pull variables
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
    private Node root;

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
        //if root is null then tree is empty and root should be initalized to new value
        if(this.root==null){
            this.root= new Node(newMovie);
            size++;
        }

        Node parent=null;
        Node current=root;

        //while loop going through tree finding next spot to insert node
        //checks to see if val of new node is smaller then root/current node. if yes, it moves to left child and checks again
        //keeps checking for values of children vs new node value, once it reaches a null value it will create and new node and add it to the tree
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

    //this code is meant to print out all values of between two keys but for some reason i cannot get it to work
    //start and end are hashcodes of movie titles to tell the program where to start and end looking for movie names
    //Node node is a refernce to root value of tree
    void subSet(Node node, int start, int end) {

        if (node == null) {
            return;
        }

        //checks to see if start is bigger than current value and goes left if it is
        if (start < node.val.getTitleHash()) {
            subSet(node.left, start, end);
        }

        //checks to see if the current cal falls inbetween the start and end paramter and if it does it prints it
        if (start <= node.val.getTitleHash() && end >= node.val.getTitleHash()) {
            System.out.print(node.val.getTitle() + " " + node.val.getReleaseYear());
        }

        //if the current value is smaller then the end the program goes right
        if (end > node.val.getTitleHash()) {
            subSet(node.right, start, end);
        }
    }

    //method to check if a value exists in the tree
    public boolean contains(String movie){
        //start with the root node
        Node current=root;

        //if root node is null then tree is empty and value cant exist in tree
        while (current != null){

            //if the movie being looked for has a title smaller then the current node, program looks at left child. if bigger then program looks at right child
            if(movie.compareToIgnoreCase(current.val.getTitle())<0){
                current=current.left;;
            }else if(movie.compareToIgnoreCase(current.val.getTitle())>0){
                current=current.right;
            }else{
                return true;
            }
        }

        //if while loop gets to a null child then value doesnt exists and method returns false
        return false;
    }
}


class Movie implements Comparable<Movie>{
    private int movieID;
    private String title;
    private int titleHash; //making title into a hashcode to make comparing titles easier in the future
    private int releaseYear;
    private ArrayList<String> genres = new ArrayList<String>(); //storing genres in a arraylist. easier to add genres without having to adjust array size

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

