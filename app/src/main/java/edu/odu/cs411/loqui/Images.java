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
