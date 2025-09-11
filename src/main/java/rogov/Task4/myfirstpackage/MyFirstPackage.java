package rogov.Task4.myfirstpackage;

public class MyFirstPackage {
    private int a, b;

    public MyFirstPackage(int a, int b) {
        this.a = a;
        this.b = b;
    }
    public MyFirstPackage() {}

    //public int getA() {return a;}
    public void setA(int a) {this.a = a;}

    //public int getB() {return b;}
    public void setB(int b) {this.b = b;}

    public int task(){
        return a & b;
    }
}
