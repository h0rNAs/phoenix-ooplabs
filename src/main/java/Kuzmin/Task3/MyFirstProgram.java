package Kuzmin.Task3;
class MyFirstClass {
    public static void main(String[] args){
        MySecondClass o = new MySecondClass(1,2);
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
class MySecondClass {
    private int a, b;
    public int getA(){
        return a;
    }
    public int getB(){
        return b;
    }
    public void setA(int a){
        this.a = a;
    }
    public void setB(int b){
        this.b = b;
    }
    public MySecondClass(int a, int b) {
        this.a = a;
        this.b = b;
    }
    public int shift(){
        return a << b;
    }
}