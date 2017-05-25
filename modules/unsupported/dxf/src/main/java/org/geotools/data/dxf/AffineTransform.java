package org.geotools.data.dxf;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Created by yinchuanqi on 2017/5/18.
 */
public class AffineTransform {
    public double _rotate  = 0;
    public double _xScale = 1;
    public double _yScale = 1;
    public double _baseX = 0;
    public double _baseY = 0;

    public AffineTransform(double _baseX, double _baseY, double _rotate, double _xScale, double _yScale) {
        this._rotate = _rotate;
        this._xScale = _xScale;
        this._yScale = _yScale;
        this._baseX = _baseX;
        this._baseY = _baseY;
    }

    public AffineTransform(AffineTransform affineTransform) {
        this._rotate = affineTransform._rotate;
        this._xScale = affineTransform._xScale;
        this._yScale = affineTransform._yScale;
        this._baseX = affineTransform._baseX;
        this._baseY = affineTransform._baseY;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new AffineTransform(this);
    }

    public Coordinate transform(Coordinate coord){
        Coordinate newCoord = new Coordinate(coord);

        /*double angle = Angle.toRadians(_rotate);
        angle = Angle.angle(coord) + angle;


        double radius = Math.sqrt(Math.pow(coord.x, 2) + Math.pow(coord.y, 2));

        //rotate first
        newCoord.x = radius * Math.cos(angle);
        newCoord.y = radius * Math.sin(angle);*/



        newCoord.x = newCoord.x*_xScale;
        newCoord.y = newCoord.y*_yScale;

        newCoord = rotateCoord(new Coordinate(0,0),newCoord,(_rotate)*(Math.PI/180));

        //平移
        newCoord.x += _baseX;
        newCoord.y += _baseY;

        return newCoord;
    }

    private Coordinate rotateCoord(Coordinate anchor,Coordinate coordinate, double angle){
        java.awt.geom.AffineTransform affineTransform = java.awt.geom.AffineTransform.getRotateInstance(angle, anchor.x, anchor.y);
        MathTransform mathTransform = new AffineTransform2D(affineTransform);
        Geometry res = null;
        GeometryFactory geometryFactory = new GeometryFactory();
        Geometry c = geometryFactory.createPoint(coordinate);
        try {
            res= JTS.transform(c, mathTransform);
        } catch (TransformException e) {
            e.printStackTrace();
        }
        return res.getCoordinate();
    }
}
