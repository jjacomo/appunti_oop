package a06.e2;

public class LogicImpl implements Logic{


    private int size;

    public LogicImpl(int size) {
        this.size = size;
    }

    @Override
    // public boolean selection(int x, int y) {
    public boolean selection(int position) {
        int x = position%size;
        int y = position/size;
        return x != 0 && x != (size-1) && y != 0 && y != (size-1);
    }

}
