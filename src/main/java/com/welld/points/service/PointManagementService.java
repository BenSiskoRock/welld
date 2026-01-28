package com.welld.points.service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


import org.springframework.stereotype.Service;


import com.welld.points.pojo.Point;

@Service
public class PointManagementService {

    
    private final Set<Point> points = new HashSet<>();

    public void add(Point newPointToAdd){
        points.add(newPointToAdd);
    }

    public Set<Point> getAllPoints(){
        return Set.copyOf(points);
    }

    public void clearAllPoints(){
        points.clear();
    }



    //Get all the lines parsing points by couple the iteration was done for n *2 times so
    //complexity O(n^2)
    public Map<Point,Map<Point,Set<Point>>> getAllTheLines(Set<Point> currentPoints){
        Map<Point,Map<Point,Set<Point>>> allTheLines = new HashMap<>();

         //Cycle on every single point
         for(Point currentKeyPoint : currentPoints){

            Integer currentX = currentKeyPoint.x();
            Integer currentY = currentKeyPoint.y();

            
            Map<Point,Set<Point>> linesOfTheSinglePoint = new HashMap<>();
            
            //compare every point with other points
            for(Point pointManaged : currentPoints){
                
                Integer pointManagedX = pointManaged.x();
                Integer pointManagedY = pointManaged.y();
                
                //Translate the cordinate so the current point becomes the origin (0,0)
                long gcdPointx = (long) pointManagedX - currentX;
                long gcdPointy = (long) pointManagedY - currentY;

                //not manage duplicates
                if(gcdPointx != 0 || gcdPointy != 0){

                    //I thought to use angles (e.g. atan2) at the base of the triangle as key but require floating-point arithmetic
                    //So I normalized the direction vector by dividing by the greatest common divisor
                    //safely used as a map key.
                    long gcd = BigInteger.valueOf(Math.abs(gcdPointx))
                                    .gcd(BigInteger.valueOf(Math.abs(gcdPointy)))
                                    .longValue();
                    
                    gcdPointx = gcdPointx /gcd; 
                    gcdPointy = gcdPointy /gcd;

                    // Normalize the sign so opposite vectors map to same key (1,2) --> (-1,-2)
                    if (gcdPointx < 0 || (gcdPointx == 0 && gcdPointy < 0)) {
                        gcdPointx = -gcdPointx;
                        gcdPointy = -gcdPointy;
                    }

                    //System.out.print("Direction x:"+gcdPointx+" y:"+gcdPointy);

                    //Direction vector used as line key
                    // 2:4, 4:8 --> this key is always 1:2
                    Point directionKey = new Point( (int)gcdPointx,(int) gcdPointy);

                    //See if the direction was already managed
                    if(linesOfTheSinglePoint.containsKey(directionKey)){
                        
                        Set<Point> pointsOfTheLine = linesOfTheSinglePoint.get(directionKey);
                        pointsOfTheLine.add(pointManaged);
                        linesOfTheSinglePoint.put(directionKey, pointsOfTheLine);

                    }else{

                        //If not create a new entry key+value
                        Set<Point> pointsOfTheLine = new HashSet<>();
                        pointsOfTheLine.add(currentKeyPoint);
                        pointsOfTheLine.add(pointManaged);
                        linesOfTheSinglePoint.put(directionKey, pointsOfTheLine);
                    }
                }

            }
            //Map with all the lines associated to points
            allTheLines.put(currentKeyPoint, linesOfTheSinglePoint);
         }
         return allTheLines;

    } 
    

    //The function that return all the lines with a number of points in input
     public Set<Set<Point>> getTheLinesWithNumberOfPoints(Integer inputNumber){
        
        
         Set<Point> currentPoints = Set.copyOf(points);

         Set<Set<Point>> finalResult = new HashSet<>();
        
        //Retrieving all the lines
        Map<Point,Map<Point,Set<Point>>> allTheLines = getAllTheLines(currentPoints);


         //After retrieving all the lines I filtered on the quantity of them in input
         //Set structure prevent duplicates
         for( Entry<Point, Map<Point, Set<Point>>> singlePoint: allTheLines.entrySet()){

            Map<Point, Set<Point>> mapLinesSinglePoint = singlePoint.getValue();
            
            for( Entry<Point, Set<Point>> singleLineMap :mapLinesSinglePoint.entrySet()){
                Set<Point> singleLine = singleLineMap.getValue();
                if(singleLine.size()>=inputNumber){
                    finalResult.add(Set.copyOf(singleLine));
                }
            }

         }

        return finalResult;
     }
    

}
