package ru.nsu.icg.wireframe.model;

import lombok.Getter;
import lombok.Setter;
import org.ejml.simple.SimpleMatrix;

import java.util.List;

@Getter
@Setter
public class WireframePoints{
    private List<List<SimpleMatrix>> generatingPointsList;
    private List<List<SimpleMatrix>> circlesPointsList;
    public WireframePoints(List<List<SimpleMatrix>> generatingPointsList, List<List<SimpleMatrix>> circlesPointsList){
        this.generatingPointsList = generatingPointsList;
        this.circlesPointsList = circlesPointsList;
    }
    public WireframePoints(){}
}
