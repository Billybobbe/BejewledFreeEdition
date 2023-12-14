public class Gem {
    public enum Type{
        R, G, B, O, Y, S
    }
    public enum Charge{
        N, F, L
    }
    public float shiftX;
    public float shiftY;
    public float size;
    public Type type;
    public Charge charge;
    public Gem(Type t){
        type = t;
        shiftX = 0;
        shiftY = 0;
        size = 1;
        charge = Charge.N;
    }
}
