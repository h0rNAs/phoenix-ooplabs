package rogov.Task4;

import rogov.Task4.myfirstpackage.*;

class MyFirstClass {
    public static void main(String[] args) {
        MyFirstPackage o = new MyFirstPackage();
        System.out.println(o.task());
        for (int i = 1; i <= 8; i++) {
            o.setA(i);
            for (int j = 1; j <= 8; j++) {
                o.setB(j);
                System.out.print(o.task());
                System.out.print(" ");
            }
            System.out.println();
        }

    }
}
