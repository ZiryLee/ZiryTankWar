package me.ziry.tankWar;
import java.awt.*;
/**
 * 爆炸类，用于坦克爆炸
 */
public class Explode {
	
	//位置
	private int x = 0;
	private int y = 0;
	
	//大管家引用
	private TankWar tw = null;
	
	//是否还在爆炸
	private boolean live = true;
	
	
	//爆炸image
	private static Toolkit tool = Toolkit.getDefaultToolkit();	
	private static Image[] img = {
		tool.getImage( Explode.class.getClassLoader().getResource("images/1.jpg") ),
		tool.getImage( Explode.class.getClassLoader().getResource("images/2.jpg") ),
		tool.getImage( Explode.class.getClassLoader().getResource("images/3.jpg") ),
		tool.getImage( Explode.class.getClassLoader().getResource("images/4.jpg") ),
		tool.getImage( Explode.class.getClassLoader().getResource("images/5.jpg") ),
		tool.getImage( Explode.class.getClassLoader().getResource("images/6.jpg") )
	};
	
	
	private boolean initialize = false;
	//控制爆炸大小
	int index = 1;
	
	public Explode(int x, int y, TankWar tw) {
		this.x = x; 
		this.y = y;
		this.tw = tw;
	}
	

	//绘制爆炸
	public void drawExplode(Graphics g) {
		
		if( !initialize ) {
			for (int i = 0; i < img.length; i++) {
				g.drawImage(img[i], -100, -100, null);
			}		
			initialize = true;
		}
		
		if( index < img.length && live ) {
			
			g.drawImage(img[index], x, y, null);
			index++;
			
		} else {			
			live = false;
			index = 1;
			tw.explodes.remove(this);			
		}
		
	}
	
}
