package com;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;

import java.util.Map;

public class FeatureBuilderFactory {
    /**
     * create a featureBuilder
     * @param name
     * @param geometryClass
     * @param attrInfo
     * @return
     */
    public static SimpleFeatureBuilder createFeatureBuilder(String name,Class geometryClass,Map<String,Class> attrInfo){
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName(name);
        tb.add("geometry", geometryClass);
        for(String attrName:attrInfo.keySet()){
            tb.add(attrName, attrInfo.get(attrName));
        }
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(tb.buildFeatureType());
        return b;
    }
}