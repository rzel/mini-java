class NestedMethodCall{
    public static void main(String[] a){
        System.out.println(new NMC().m(10));
    }
}

class NMC {
	
    public int m(int num){
    	X y;
    	
    	y = this.f();
    	return num;
    }
    
    public X f() {
    	return new X();
    }
}

class X {}
