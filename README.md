# WellD Project Pattern Recognition
Implementation of the pattern recognition exercise

## Scope
Implementig 4 rest api to manage N points in the cartesian plane

Add a point to the space
POST /point with body { "x": ..., "y": ... }

Get all points in the space
GET /space

Get all line segments passing through at least N points. Note that a line segment should be a set of
points.
GET /lines/{n}

Remove all points from the space
DELETE /space

## My implementation
I used Spring Boot 3.5.9.

I decided to manage integer positive and negative numbers as input.
Letters, strings, and float numbers return HTTP 400.

The most challenging part was implementing this API:
GET /lines/{n}

I divided the problem in two different parts:

### Retrieve all the lines for every point in input
For this approach, I first considered that a line is always defined by at least two points.
A line has two characteristics:
direction
space positioning

#### Space positioning
To manage the space positioning, I decided to define the current point I was examining as the center:
P0(x0, y0)
For every other point Pn(xn, yn), I subtract the current point coordinates:
Ptemp = (xn - x0, yn - y0)


#### Direction
For the direction, I initially thought about using the base angle of the triangle, but it was too difficult to manage because it would require floating-point values as keys.
So instead, I decided to use a normalized vector as the direction key.
I computed the greatest common divisor (GCD) between xtemp and ytemp, and normalized the vector:
Ptemp = (xtemp / gcd, ytemp / gcd)
This produces a normalized direction vector and also allows managing opposite vectors correctly.
I used this normalized vector as the direction key, and I added the points to a Set to avoid duplicates.

Result structure

The final result is a map of map of sets, structured like this:

{
  (x0, y0) -> {
      (xtemp, ytemp) -> [ (x1, y1), (x2, y2), ... ]
  }
}


### Filter the results

After retrieving all the lines, I filtered them based on the number of points they contain.
I parsed all the maps, extracted the sets that contain at least n points, and added them to another set.
The final result is a:
Set<Set<Point>>
Using a Set of sets allows avoiding duplicates, since the same line can be found starting from different points.



