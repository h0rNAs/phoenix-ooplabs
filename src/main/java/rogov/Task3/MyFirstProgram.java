package rogov.Task3;

class MyFirstClass {
    public static void main(String[] args) {
        MySecondClass o = new MySecondClass();
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

class MySecondClass{
    private int a, b;

    public MySecondClass(int a, int b) {
        this.a = a;
        this.b = b;
    }
    public MySecondClass() {}

    public int getA() {return a;}
    public void setA(int a) {this.a = a;}

    public int getB() {return b;}
    public void setB(int b) {this.b = b;}

    public int task(){
        return a & b;
    }
}
