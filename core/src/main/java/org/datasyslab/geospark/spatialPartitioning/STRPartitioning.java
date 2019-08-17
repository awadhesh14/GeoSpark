package org.datasyslab.geospark.spatialPartitioning;

import com.vividsolutions.jts.geom.Envelope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class STRPartitioning implements Serializable {

    /**
     * The grids.
     */
    final List<Envelope> grids = new ArrayList<>();

    public STRPartitioning(List<Envelope> samples, Envelope boundary, int partitions)
            throws Exception
    {
        int rootP = (int) Math.sqrt(partitions);
        int size = (rootP + 1) * rootP;
        int elementsperpartition = samples.size()/partitions;
        int xBinSize = samples.size()/rootP;
        System.out.println("sample size = "+samples.size());
        System.out.println("rootP = "+rootP);
        System.out.println("size = "+size);
        System.out.println("elementsperpartition = "+elementsperpartition);
        System.out.println("xBinSize = "+ xBinSize);
        ArrayList<Envelope> samples1 = new ArrayList(samples);
        samples1.sort(new Comparator<Envelope>() {
            @Override
            public int compare(Envelope t1, Envelope t2) {
                if(t1.getMinX()< t2.getMinX()) return -1;
                else if(t1.getMinX()> t2.getMinX()) return 1;
                else return 0;
            }
        });
        if (boundary.getMinX()==samples1.get(0).getMinX()) {
            System.out.println("life is good on MINIMUM");
        }
        else {
            System.out.println("life is BAD on MINIMUM");
            System.out.println(boundary.getMinX()+"  " +samples1.get(0).getMinX());
        }
        if (boundary.getMaxX()==samples1.get(samples1.size()-1).getMinX()) {
            System.out.println("life is good on MAXIMUM");
        }
        else {
            System.out.println("life is BAD on MAXIMUM");
            System.out.println(boundary.getMaxX()+"  " +samples1.get(samples1.size()-1).getMinX());
        }
        double[] arr1 = new double[rootP +1];
        for(int i=0,j=0;i<rootP+1;i++){
            if(i == 0){
                arr1[i] = samples1.get(j).centre().x;
                j+=(samples1.size()/rootP -1);
            }
            else if(i == rootP){
                arr1[i] = boundary.getMaxX();//samples1.get(j).centre().x;
                j+=(samples1.size()/rootP -1);
            }
            else{
                arr1[i] = samples1.get(j).centre().y;
                j+=(samples1.size()/rootP -1);
            }
        }

        for(int i=0;i<rootP+1;i++){
            System.out.print(arr1[i]+" ");
        }
        /*for(int i=0;i<rootP;i++){
            Collections.sort(samples1.subList((i * xBinSize), (i + 1) * xBinSize*//*FIXME: last some elements are left*//*), new Comparator<Envelope>() {
                @Override
                public int compare(Envelope t1, Envelope t2) {
                    if(t1.centre().y < t2.centre().y) return -1;
                    else if(t1.centre().y> t2.centre().y) return 1;
                    else return 0;
                }
            });
         }*/
        double[] arr2 = new double[size];
        /*try {*/
        for (int i = 0, j = 0; i < size; i++) {
            System.out.println("i= "+i+" j= "+j);
            if (i % (rootP + 1) == 0) {
                arr2[i] = boundary.getMinY();
                j += elementsperpartition;
            } else if (i % (rootP + 1) == rootP) {
                arr2[i] = boundary.getMaxY();
                //j += elementsperpartition;
            } else {
                arr2[i] = samples1.get(j).centre().y;
                j += elementsperpartition;
            }
        }
        /*}
        catch(IndexOutOfBoundsException e){
            for(int i=0;i<size;i++){
                System.out.print(arr2[i]+" ");
            }
            System.out.println(e);
        }*/

        for(int i=0; i<rootP; i++){
            for(int j=0; j<rootP; j++){
                grids.add(new Envelope(arr1[i],arr1[i+1],arr2[j],arr2[j+1]));
            }
        }
        /*Double root = Math.sqrt(partitions);//partitions = 16
        int xPartitions = root.intValue(); //xPartitions = 4
        int noOfLines = xPartitions-1;     //noOfLines = 3
        int xBinSize = samples1.size()/xPartitions; //xBin = 25
        int yBinSize = xBinSize/xPartitions;          //yBin = 6

        double[] xlines = new double[noOfLines+2];
        for(int i=1;i<=noOfLines;i++) {
            xlines[i] = samples1.get((i * xBinSize)-1).centre().x;
        }
        xlines[0] = boundary.getMinX();
        xlines[noOfLines+1] = boundary.getMaxX();


        double[][] ylines = new double[xPartitions][noOfLines+2];//[4][5]
        for(int i=0;i<xPartitions;i++){
            Collections.sort(samples1.subList((i * xBinSize), (i + 1) * xBinSize*//*FIXME: last some elements are left*//*), new Comparator<Envelope>() {
                @Override
                public int compare(Envelope t1, Envelope t2) {
                    if(t1.centre().y < t2.centre().y) return -1;
                    else if(t1.centre().y> t2.centre().y) return 1;
                    else return 0;
                }
            });
        }*/








        /*STRtree strtree = new STRtree(samples.size() / partitions);
        for (Envelope sample : samples) {
            strtree.insert(sample, sample);
        }

        List<Envelope> envelopes = strtree.queryBoundary();
        for (Envelope envelope : envelopes) {
            grids.add(envelope);
        }*/
    }


    /**
     * Gets the grids.
     *
     * @return the grids
     */
    public List<Envelope> getGrids()
    {

        return this.grids;
    }
}
