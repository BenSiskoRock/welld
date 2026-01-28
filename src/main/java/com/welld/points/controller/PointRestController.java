package com.welld.points.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.welld.points.dto.PointDto;
import com.welld.points.pojo.Point;
import com.welld.points.service.PointManagementService;

import jakarta.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping
public class PointRestController {

    private final PointManagementService pointService;

    public PointRestController(PointManagementService service) {
        this.pointService = service;
    }


    @GetMapping("/health")
    public String health() {
        return "OK";
    }


    @PostMapping("/point")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewPoint( @RequestBody PointDto newPoint) {
        // I allowed negative cordinates
        // I didn't allowed null or String cordinates
        pointService.add(new Point(newPoint.x(),newPoint.y()));
    }
    

    @GetMapping("/space")
    public Set<Point> getAllTheCurrentPoints() {
        return pointService.getAllPoints();
    }
    
    @GetMapping("/lines/{n}")
    public Set<Set<Point>> getMethodName(@NotNull @PathVariable Integer n) {
        return pointService.getTheLinesWithNumberOfPoints(n);
    }
    

    @DeleteMapping("/space")
    public void deleteAllPoints(){
        pointService.clearAllPoints();
    }
    

}
