package org.datasyslab.geospark.spatialPartitioning;
/*
* osmconvert ---> https://stackoverflow.com/questions/24941936/create-a-csv-file-of-all-the-addresses-in-an-area-from-osm-file
comand ---> osmconvert uk_streets.osm --csv="@lon @lat addr:city addr:street" --csv-headline --csv-separator=, -o=uk_streets.csv
*/
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

        ArrayList<Envelope> samples1 = new ArrayList(samples);
        samples1.sort(new Comparator<Envelope>() {
            @Override
            public int compare(Envelope t1, Envelope t2) {
                if(t1.getMinX()< t2.getMinX()) return -1;
                else if(t1.getMinX()> t2.getMinX()) return 1;
                else return 0;
            }
        });


        double[] arr1 = new double[rootP +1];
        for(int i=0,j=0;i<rootP+1;i++){
            if(i == 0){
                arr1[i] = boundary.getMinX();//samples1.get(j).getMinX();
                j+=(samples1.size()/rootP -1);
            }
            else if(i == rootP){
                arr1[i] = boundary.getMaxX();//samples1.get(j).centre().x;
                j+=(samples1.size()/rootP -1);
            }
            else{
                arr1[i] = samples1.get(j).getMinX();
                j+=(samples1.size()/rootP -1);
            }
        }


        for(int i=0;i<rootP;i++){
            Collections.sort(samples1.subList(i * samples1.size()/rootP, samples1.size() > (i + 1) * (samples1.size()/rootP) ? (i + 1) * (samples1.size()/rootP)  : samples1.size()), new Comparator<Envelope>() {
                @Override
                public int compare(Envelope t1, Envelope t2) {
                    if(t1.getMinY()<t2.getMinY()) return -1;
                    else if(t1.getMinY()>t2.getMinY()) return 1;
                    else return 0;
                }
            });
        }

        double[] arr2 = new double[size];
        /*try {*/
        for (int i = 0, j = 0; i < size; i++) {
            if (i % (rootP + 1) == 0) {
                arr2[i] = boundary.getMinY();
                j += elementsperpartition;
            } else if (i % (rootP + 1) == rootP) {
                arr2[i] = boundary.getMaxY();
                //j += elementsperpartition;
            } else {
                arr2[i] = samples1.get(j).getMinY();
                j += elementsperpartition;
            }
        }


        for(int i=0; i<rootP; i++){
            for(int j=i*(rootP+1); j<((i+1)*(rootP+1)-1); j++){
                grids.add(new Envelope(arr1[i],arr1[i+1],arr2[j],arr2[j+1]));
            }
        }

    }


    public STRPartitioning(List<Envelope> samples, Envelope boundary, int partitions,boolean debug)
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

        System.out.println("---------------------X_SORT_START-----------------------------");
        for(int i=0;i<samples1.size();i++)
            System.out.println(samples1.get(i).getMinX()+"   "+samples1.get(i).getMinY());
        System.out.println("----------------------X_SORT_END----------------------------");

        double[] arr1 = new double[rootP +1];
        for(int i=0,j=0;i<rootP+1;i++){
            if(i == 0){
                arr1[i] = boundary.getMinX();//samples1.get(j).getMinX();
                j+=(samples1.size()/rootP -1);
            }
            else if(i == rootP){
                arr1[i] = boundary.getMaxX();//samples1.get(j).centre().x;
                j+=(samples1.size()/rootP -1);
            }
            else{
                arr1[i] = samples1.get(j).getMinX();
                j+=(samples1.size()/rootP -1);
            }
        }

        for(int i=0;i<rootP+1;i++){
            System.out.print(arr1[i]+" ");
        }
        for(int i=0;i<rootP;i++){
            Collections.sort(samples1.subList(i * samples1.size()/rootP, samples1.size() > (i + 1) * (samples1.size()/rootP) ? (i + 1) * (samples1.size()/rootP)  : samples1.size()), new Comparator<Envelope>() {
                @Override
                public int compare(Envelope t1, Envelope t2) {
                    if(t1.getMinY()<t2.getMinY()) return -1;
                    else if(t1.getMinY()>t2.getMinY()) return 1;
                    else return 0;
                }
            });
        }
        System.out.println("---------------------Y_SORT_START-----------------------------");
        for(int i=0;i<samples1.size();i++)
            System.out.println(samples1.get(i).getMinX()+"   "+samples1.get(i).getMinY());
        System.out.println("----------------------Y_SORT_END----------------------------");
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
                arr2[i] = samples1.get(j).getMinY();
                j += elementsperpartition;
            }
        }


        for(int i=0; i<rootP; i++){
            for(int j=i*(rootP+1); j<((i+1)*(rootP+1)-1); j++){
                grids.add(new Envelope(arr1[i],arr1[i+1],arr2[j],arr2[j+1]));
            }
        }
        System.out.println("-----------------------ARR1---------------------------");
        for(int i=0;i<arr1.length;i++)
            System.out.print(arr1[i]+" ");
        System.out.println("\n-----------------------ARR2---------------------------");
        for(int i=0;i<arr2.length;i++)
            System.out.print(arr2[i]+" ");
        System.out.println("--------------------------------------------------");

        this.getGrids().forEach(l -> System.out.println(l.getMinX()+" "+ l.getMaxX()+" "+l.getMinY()+" "+l.getMaxY()));
        System.out.println("--------------------------GRIDS------------------------");
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
