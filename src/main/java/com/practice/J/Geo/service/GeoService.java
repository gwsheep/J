package com.practice.J.Geo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.sampled.Line;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeoService {

    private final GeometryFactory geometryFactory;

    public String getPointItem() {

        Point point = geometryFactory.createPoint(new Coordinate(1, 2));
        return this.geoToString(point);

    }

    public String getLineItem() {

        List<Coordinate> coordinates = List.of(
                                                new Coordinate(10, 10),
                                                new Coordinate(20, 20),
                                                new Coordinate(30, 40)
                                        );

        LineString lineString = geometryFactory.createLineString(coordinates.toArray(new Coordinate[]{}));
        return this.geoToString(lineString);

    }

    //expected to edit
    public String getPolygonItem() {

        Polygon polygon = geometryFactory.createPolygon(new Coordinate[] {});
        return this.geoToString(polygon);

    }

    private String geoToString(Geometry geometry) {
        WKTWriter writer = new WKTWriter();
        return writer.write(geometry);
    }

}
