package me.ziry.tankWar;
import java.awt.*;

/**
 * 血块类，碰撞此血块增加Ph值
 * @author Ziry
 */
public class AddPh {
	
	/**
	 * 血块X坐标
	 */
	private int x = 0;
	
	/**
	 * 血块Y坐标
	 */
	private int y = 300;
	
	/**
	 * 血块宽度
	 */
	private static final int WIDTH = 20;
	
	/**
	 * 血块高度
	 */
	private static final int HEIGHT = 40;
	
	/**
	 * 血块是否活着
	 */
	private boolean live = true;

	/**
	 * 大管家引用
	 */
	TankWar tw;
	
	/**
	 * 构造大管家应用
	 * @param tw	大管家（主窗口）
	 */
	public AddPh(TankWar tw) {
		this.tw = tw;
	}
	
	/**
	 * 血块是否活着
	 * @return true为活着false为不存在
	 */
	public boolean isLive() {
		return live;
	}

	/**
	 * 设置是否活着
	 * @param live
	 */
	public void setLive(boolean live) {
		this.live = live;
	}
	
	/**
	 * 画出血块
	 * @param g	得到画笔
	 */
	public void drawAddPh(Graphics g) {
		
		//如果不存在不画
		if(!live) {
			return;
		}
		
		//画出血块
		Color oldC = g.getColor();
		g.setColor(Color.red);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(oldC);
		goDirection();
		
	}
	
	/**
	 * 得到血块所在方块
	 * @return	返回Rectangle对象
	 */
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	/**
	 * 运动sin轨迹
	 */
	public void goDirection() {
		
		y = (int)(Math.sin( 0.05*x ) * 150 ) + 300;
		if( x >= TankWar.GAME_WIDTH )  {
			x = 0;
		}
		x += 2;
		
	} 
	
}
