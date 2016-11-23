package me.ziry.tankWar;
import java.awt.*;

/**
 * 墙
 * 敌军和子弹无法穿越
 */
public class Wall {

	//墙坐标
	private int x;
	private int y;
	
	//大小
	private int width = 40;
	private int height = 200;
	
	private static Toolkit tool = Toolkit.getDefaultToolkit();	
	private static Image img = 
			tool.getImage( Wall.class.getClassLoader().getResource("images/wall.jpg") );
	
	//构造位置
	public Wall(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	//构造位置和大小
	public Wall(int x, int y, int width, int height) {
		this(x, y);
		this.width = width;
		this.height = height;
	}

	//画出墙
	public void drawWall(Graphics g) {
		
		g.drawImage(img, x, y, null);
		
	}
	
	//打到墙方块
	public Rectangle getRect() {
		return new Rectangle(x, y, width, height);
	}
	

}
