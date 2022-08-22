package com.example.rollingball.util;

import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Transform;

public class Skybox extends Group {

    private static final float SKYBOX_SCALE = 2000.0f;
    public Skybox() {
        //
        //             7---6
        //             |-Y |
        //         4---3---2---A---C
        //         |-X |+Z |+X |-Z |
        //         5---0---1---B---D
        //             |+Y |
        //             9---8
        //
        //  4 == 7 == C
        //  5 == 9 == D
        //  6 == A
        //  8 == B

        // 8 -> 7
        //             4---6
        //             |-Y |
        //         4---3---2---6---4
        //         |-X |+Z |+X |-Z |
        //         5---0---1---7---5
        //             |+Y |
        //             5---7
        //

        float points[] = {
                //    X                    Y                      Z
            -0.5f * SKYBOX_SCALE,  0.5f * SKYBOX_SCALE,  0.5f * SKYBOX_SCALE,// 0 // front
             0.5f * SKYBOX_SCALE,  0.5f * SKYBOX_SCALE,  0.5f * SKYBOX_SCALE,// 1
             0.5f * SKYBOX_SCALE, -0.5f * SKYBOX_SCALE,  0.5f * SKYBOX_SCALE,// 2
            -0.5f * SKYBOX_SCALE, -0.5f * SKYBOX_SCALE,  0.5f * SKYBOX_SCALE,// 3

            -0.5f * SKYBOX_SCALE, -0.5f * SKYBOX_SCALE, -0.5f * SKYBOX_SCALE,// 4 // left
            -0.5f * SKYBOX_SCALE,  0.5f * SKYBOX_SCALE, -0.5f * SKYBOX_SCALE,// 5

             0.5f * SKYBOX_SCALE, -0.5f * SKYBOX_SCALE, -0.5f * SKYBOX_SCALE,// 6 // up

             0.5f * SKYBOX_SCALE,  0.5f * SKYBOX_SCALE, -0.5f * SKYBOX_SCALE,// 7 // down
        };

        //
        //             7---6                     4---6
        //             |-Y |                     |-Y |
        //         4---3---2---A---C         4---3---2---6---4
        //         |-X |+Z |+X |-Z |         |-X |+Z |+X |-Z |
        //         5---0---1---B---D         5---0---1---7---5
        //             |+Y |                     |+Y |
        //             9---8                     5---7
        //
        //  4 == 7 == C
        //  5 == 9 == D
        //  6 == A
        //  8 == B

        float uv[] = {
           0.25f    , 0.6666f  ,
           0.5f     , 0.6666f  ,
           0.5f     , 0.3333f  ,
           0.25f    , 0.3333f  ,

           0.0f     , 0.3333f  ,
           0.0f     , 0.6666f  ,

           0.5f     , 0.0f     ,
           0.25f    , 0.0f     ,

           0.5f     , 1.0f     ,
           0.25f    , 1.0f     ,

           0.75f    , 0.3333f  ,
           0.75f    , 0.6666f  ,

           1.0f     , 0.3333f  ,
           1.0f     , 0.6666f  ,
        };

        //         UV                        VERTEX
        //             7---6                     4---6
        //             |-Y |                     |-Y |
        //         4---3---2---A---C         4---3---2---6---4
        //         |-X |+Z |+X |-Z |         |-X |+Z |+X |-Z |
        //         5---0---1---B---D         5---0---1---7---5
        //             |+Y |                     |+Y |
        //             9---8                     5---7
        //
        //  4 == 7 == C
        //  5 == 9 == D
        //  6 == A
        //  8 == B
        //
        //  8 -> 7
        int faces[] = {
                0 ,  0 ,  1,   1,   2,   2,//front
                0 ,  0 ,  2,   2,   3,   3,

                5 ,  5 ,  0,   0,   3,   3,//left
                5 ,  5 ,  3,   3,   4,   4,


                3 ,  3 ,  2,   2,   6,   6,//up
                3 ,  3 ,  6,   6,   4,   4,


                5 ,  9 ,  1,   1,   0,   0,//down
                5 ,  9 ,  7,   8,   1,   1,


                1 ,  1 ,  7,  11,   6,  10,//right
                1 ,  1 ,  6,  10,   2,   2,


                7 ,  11,  4,  12,   6,  10,//back
                7 ,  11,  5,  13,   4,  12
        };

        TriangleMesh tm = new TriangleMesh();
        tm.getPoints().addAll(points);
        tm.getTexCoords().addAll(uv);
        tm.getFaces().addAll(faces);

        Image tex = new Image(this.getClass().getClassLoader().getResourceAsStream("skybox.png"));
        PhongMaterial mat = new PhongMaterial();
        mat.setDiffuseMap(tex);

        MeshView mw = new MeshView();
        mw.setMesh(tm);
        mw.setMaterial(mat);

        this.getChildren().add(mw);
        AmbientLight al = new AmbientLight();
        this.getChildren().add(al);
    }

    public void update(Transform camera_transform) {

    }
}
