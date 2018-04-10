package com.example.jiazai.AirhockeyActivity.object;

import com.example.jiazai.AirhockeyActivity.util.Geometry.*;

import java.util.ArrayList;
import java.util.List;
import static android.opengl.GLES20.*;

public class ObjectBuilder {

    static interface DrawCommand {
        void draw();
    }

    static class GeneratedData {
        final float[] vertexData;
        final  List<DrawCommand> drawList;

        GeneratedData(float[] vertexData, List<DrawCommand> drawList) {
            this.vertexData = vertexData;
            this.drawList = drawList;
        }
    }

    private GeneratedData build() {
        return new GeneratedData(vertexData, drawList);
    }

    private static final int FLOAT_PER_VERTEX = 3;
    private final float[] vertexData;
    private final List<DrawCommand> drawList = new ArrayList<DrawCommand>();
    private int offset = 0;

    private ObjectBuilder(int sizeInVertices) {
        vertexData = new float[sizeInVertices * FLOAT_PER_VERTEX];
    }

    /* use triangle to make circle
    *  numPoints is the number of the points ON the circle bound
    *  we need a center and a repeated first vertex*/
    private static int sizeOfCircleInVertices(int numPoints) {
        return 1+(numPoints+1);
    }

    private static int sizeOfOpenCylinderInVertices(int numPoints) {
        return (numPoints + 1)*2;
    }

    private void appendCircle(Circle circle, int numPoints) {
        final int startVertex = offset / FLOAT_PER_VERTEX;
        final int numVertices = sizeOfCircleInVertices(numPoints);
        //Center point of fan
        vertexData[offset++] = circle.center.x;
        vertexData[offset++] = circle.center.y;
        vertexData[offset++] = circle.center.z;

        // Fan around center point <= is used because we want to generate
        // the point at the starting angle twice to complete the fan
        for (int i = 0; i <= numPoints; i++) {
            float angleInRadius = ((float)i / (float)numPoints) * (float) (Math.PI * 2f);

            vertexData[offset++] = circle.center.x + circle.radius * (float)Math.cos(angleInRadius);
            vertexData[offset++] = circle.center.y;
            vertexData[offset++] = circle.center.z + circle.radius * (float)Math.sin(angleInRadius);
        }

        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertices);
            }
        });
    }

    private void appendOpenCylinder(Cylinder cylinder, int numPoints) {
        final int startVertex = offset / FLOAT_PER_VERTEX;
        final int numVertices = sizeOfOpenCylinderInVertices(numPoints);
        final float yStart = cylinder.center.y - (cylinder.height / 2f);
        final float yEnd = cylinder.center.y + (cylinder.height / 2f);

        for (int i = 0; i <= numPoints; i++) {
            float angleInRadius = ((float)i / (float)numPoints) * (float) (Math.PI * 2f);

            float xPosition = cylinder.center.x + cylinder.radius * (float)Math.cos(angleInRadius);
            float zPosition = cylinder.center.z + cylinder.radius * (float)Math.sin(angleInRadius);

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yStart;
            vertexData[offset++] = zPosition;

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yEnd;
            vertexData[offset++] = zPosition;
        }

        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices);
            }
        });
    }

    static GeneratedData createPuck(Cylinder puck, int numPoints) {
        int size = sizeOfCircleInVertices(numPoints)
                + sizeOfOpenCylinderInVertices(numPoints);

        ObjectBuilder builder = new ObjectBuilder(size);

        Circle puckTop = new Circle(
                puck.center.translateY(puck.height / 2f),
                puck.radius);

        builder.appendCircle(puckTop, numPoints);
        builder.appendOpenCylinder(puck, numPoints);

        return builder.build();
    }

    static GeneratedData createMallet(Point center, float radius, float height, int numPoints) {
        int size = sizeOfCircleInVertices(numPoints) * 2
                + sizeOfOpenCylinderInVertices(numPoints) * 2;

        ObjectBuilder builder = new ObjectBuilder(size);

        // First, generate the mallet base
        float baseHeight = height * 0.25f;

        Circle baseCircle = new Circle(center.translateY(-baseHeight), radius);
        Cylinder baseCylinder = new Cylinder(baseCircle.center.translateY(-baseHeight/2f), radius, baseHeight);

        builder.appendCircle(baseCircle, numPoints);
        builder.appendOpenCylinder(baseCylinder, numPoints);

        return builder.build();
    }
}
