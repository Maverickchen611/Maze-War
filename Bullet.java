public class Bullet {
    private int x;
    private int y;
    private int speedX;
    private int speedY;
    private boolean isActive;

    public Bullet(int startX, int startY, int speedX, int speedY) {
        this.x = startX;
        this.y = startY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.isActive = true;
    }

    public void update() {
        x += speedX;
        y += speedY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(int speedY){
        this.speedY = speedY;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


    @Override
    public String toString() {
        return "Start(" + x + ", " + y + ") "+"Speed(" + speedX + ", " + speedY + ")";
    }
}
