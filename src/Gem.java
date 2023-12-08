public class Gem {
    public enum Type{
        B, B_F, B_L,
        R, R_F, R_L,
        O, O_F, O_L,
        G, G_F, G_L,
        Y, Y_F, Y_L,
        S
    }
    public float shiftX;
    public float shiftY;
    public Type type;
    public Gem(Type t){
        type = t;
        shiftX = 0;
        shiftY = 0;
    }
}
