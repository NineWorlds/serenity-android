package com.castillo.dd;

interface DSInterface {

    void clearDownloadlist();
    void addFileDownloadlist( in String url, in String destination, in String filename, in int position );
    void downloadFile( in int position );
    int getDownloadStatus (in int position);
    String getDownloadFilename (in int position);
    int getDownloadProgress (in int position);
    String getDownloadEllapsedTime (in int position);
    String getDownloadRemainingTime (in int position);    
    float getDownloadSpeed (in int position);
    long getDownloadLaunchTime(in int position);
    int getDownloadlistSize ();    
    void pause();
    void resume();    
}