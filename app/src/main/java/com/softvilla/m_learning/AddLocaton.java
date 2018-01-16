package com.softvilla.m_learning;

import com.orm.SugarRecord;

/**
 * Created by Malik on 12/07/2017.
 */

public class AddLocaton extends SugarRecord<AddLocaton> {

    public AddLocaton(){
        isSend = false;
    }
    boolean isSend;
    long identityimg;

    String LocationName, Image, studyLevel, ObjectIdentity;
    double LocationLat, LocationLng;

}
