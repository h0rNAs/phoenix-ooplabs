package Kuzmin.Task4;
import Kuzmin.Task4.myfirstpackage.*;
class MyFirstClass {
    public static void main(String[] args){
        MyFirstPackage o = new MyFirstPackage(1,2);
        System.out.println("Bit shift:");
        for (int i = 1; i<=8; i++){
            for (int j = 1; j<=8; j++){
                o.setA(i);
                o.setB(j);
                System.out.print(o.shift());
                System.out.print("\t");
            }
            System.out.println();
        }
    }
}