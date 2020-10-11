<<<<<<< HEAD
package edu.odu.cs411.loqui;

import android.content.Context;
import android.content.res.TypedArray;

import org.apache.commons.io.FilenameUtils;

import java.util.*;

/**
 * Created by Richard Atwell on 4/8/2020
 * Old Dominion University
 * ratwe002@odu.edu
 */
public class Images {

    private TypedArray imageArray;
    private int index;
    private String name;

    public Images(Context context) {

        imageArray = context.getResources().obtainTypedArray(R.array.speechImages);
        randomizeArray();

    }

    public void randomizeArray(){
        Random random = new Random();
        index = random.nextInt(imageArray.length());
        name = FilenameUtils.getBaseName(imageArray.getString(index));
    }

    public String getName(){return name;}

    public int getIndex(){return index;}

    public TypedArray getImages(){return imageArray;}

}
=======
version https://git-lfs.github.com/spec/v1
oid sha256:698c495c699951d26469f25b221d7a3ce614fe3610e7aeb69f2cf7adff871b18
size 872
>>>>>>> 69b172438de57648718503ac42d98caad0ea5cf8
