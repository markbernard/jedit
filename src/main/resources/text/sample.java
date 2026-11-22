package abc;

/**
 * 
 * @author Mark Bernard
 */
public class Abc {
    private int def;

    /**
     * 
     */
    public Abc() {
        System.out.println("This is a test");
        System.out.println('a');
    }
    
    /**
     * @return the def
     */
    public int getDef() {
        return def;
    }
    
    /**
     * @param def
     */
    public void setDef(int def) {
        this.def = def;
    }
    
    public static void main(String... args) {
        Abc a = new Abc();
        a.setDef(1);
        System.out.println(a.getDef());
    }
}