//package com.example.iat359_project;
//
//import java.util.ArrayList;
//
////using a paginator to display owned clothes
//public class Paginator {
//    public static final int ITEMS_PER_PAGE=3;
//    private static MyPlayerDatabase playerdb;
//    public static long totalNumItems = playerdb.getCount();
//    public static long itemsRemaining=totalNumItems%ITEMS_PER_PAGE;
//    public static long lastPage = totalNumItems/ITEMS_PER_PAGE;
//
//
//    public ArrayList<String> generatePage(int currentPage){
//        int startItem=currentPage*ITEMS_PER_PAGE+1;
//        int numOfData=ITEMS_PER_PAGE;
//
//        ArrayList<String>pageData = new ArrayList<>();
//
//        if(currentPage == lastPage && itemsRemaining > 0){
//            for(int i = startItem; i<startItem+itemsRemaining; i++){
//                pageData.add("Number "+i);
//            }
//        }else{
//            for(int i=startItem; i<startItem+numOfData; i++){
//                pageData.add("Number "+i);
//            }
//        }
//
//        return pageData;
//    }
//
//}
