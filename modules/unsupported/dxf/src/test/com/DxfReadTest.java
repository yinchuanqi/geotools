package com;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.geotools.data.dxf.DXFDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Created by yinchuanqi on 2017/5/25.
 */
public class DxfReadTest {
    @Test
    public void readDxf() throws Exception{
        //File file = new File("/Users/yinchuanqi/Downloads/text_block.dxf");
        File file = new File("/Users/yinchuanqi/Downloads/xx1-2f.dxf");
        //File file = new File("/Users/yinchuanqi/Downloads/single_block.dxf");
        //File file = new File("/Users/yinchuanqi/Downloads/chair_test.dxf");
        //File file = new File("/Users/yinchuanqi/Desktop/chair_test.dxf");

        CRS.decode("EPSG:3875");
        Set<String> geomTypeSet = new HashSet<String>();

        Map<String,List<Geometry>> geoTypeWithGeometry = new HashMap<String,List<Geometry>>();
        URL url = file.toURI().toURL();
        DXFDataStore dxfDataStore = new DXFDataStore(url,"EPSG:3875");
        SimpleFeatureCollection sfc = dxfDataStore.getFeatureSource(dxfDataStore.getTypeNames()[0]).getFeatures();
        SimpleFeatureIterator sfcIter = sfc.features();
        while (sfcIter.hasNext()){

            SimpleFeature smf = sfcIter.next();
            Geometry geometry = (Geometry)smf.getDefaultGeometry();
            geomTypeSet.add(geometry.getGeometryType());
            if(null==geoTypeWithGeometry.get(geometry.getGeometryType())){
                geoTypeWithGeometry.put(geometry.getGeometryType(),new ArrayList<Geometry>());
            }
            geoTypeWithGeometry.get(geometry.getGeometryType()).add(geometry);
        }

        for(Map.Entry<String,List<Geometry>> entry : geoTypeWithGeometry.entrySet()){
            String geomType = entry.getKey();
            List<Geometry> geoms = entry.getValue();
            toGeojson(geoms,"/Users/yinchuanqi/Desktop/dxf/"+geomType+".json");
        }

        System.out.println(geomTypeSet);
    }
    static public void toGeojson(List<Geometry> geometryList,String filePath) throws Exception{
        GeometryFactory geometryFactory = new GeometryFactory();
        DefaultFeatureCollection ftc = new DefaultFeatureCollection(null, null);
        SimpleFeatureBuilder sfb = FeatureBuilderFactory.createFeatureBuilder("desk", geometryList.get(0).getClass(),new HashMap<String,Class>());
        int index = 1;
        for(Geometry geometry : geometryList){
            try {
                sfb.add(geometry);
                ftc.add(sfb.buildFeature(""+index++ ));
            } catch (Exception e) {
                System.out.println("============"+index);
                e.printStackTrace();
            }
        }
        FeatureJSON fjson = new FeatureJSON();
        StringWriter writer = new StringWriter();
        fjson.writeFeatureCollection(ftc,writer);
        writeStringToFile(writer.toString(),filePath);
    }

    static public void writeStringToFile(String content,String filepath){
        FileWriter fileWritter = null;
        BufferedWriter bufferWriter = null;
        try {
            fileWritter = new FileWriter(filepath,false);
            bufferWriter = new BufferedWriter(fileWritter);
            bufferWriter.write(content);
            bufferWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }finally {
            if(null!=bufferWriter){
                try {
                    bufferWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }

        }
    }
}
